/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;

import org.jvnet.olt.editor.util.Languages;


//TODO remove absoulte layout and replace with something more flexible
public class NewMiniTMPanel extends JDialog {
    private static final Logger logger = Logger.getLogger(NewMiniTMPanel.class.getName());
    public static final String PROPERTY_PROJECT = "PROPERT_PROJECT";
    JLabel informationLabel = new JLabel();
    JLabel projectNameLabel = new JLabel();
    JComboBox sourceComboBox = new JComboBox();
    JComboBox targetComboBox = new JComboBox();
    JLabel sourceIconLabel = new JLabel();
    JLabel targetIconLabel = new JLabel();
    JLabel sourceLabel = new JLabel();
    JLabel targetLabel = new JLabel();
    Vector languages = new Vector();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();
    JTextField jProjName = new JTextField();
    private Backend backend;
    private java.util.List propertyListeners;
    private String targetLang;
    private String sourceLang;
    private String projectName;
    private boolean didCancel;

    //List of ProjectInfos -- all known projects
    private List allProjects;

    // bug 4737485
    class ListenForEnter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            okButton.doClick();
        }
    }

    public NewMiniTMPanel(JFrame parent, List allProjects) {
        super(parent, "New Project", true);
        this.allProjects = allProjects;

        languages = Languages.getLanguagesBySort();

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.backend = backend;
    }

    private void jbInit() throws Exception {
        informationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        informationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        informationLabel.setText("Please enter the following information for this project:");
        informationLabel.setBounds(new Rectangle(5, 21, 401, 24));
        this.getContentPane().setLayout(null);
        projectNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        projectNameLabel.setText("Project Name:");
        projectNameLabel.setBounds(new Rectangle(0, 58, 116, 35));

        sourceIconLabel.setBounds(new Rectangle(16, 129, 49, 44));
        targetIconLabel.setBounds(new Rectangle(16, 218, 49, 44));
        sourceLabel.setText("Source Language:");
        sourceLabel.setBounds(new Rectangle(16, 105, 182, 23));
        targetLabel.setText("Target Language:");
        targetLabel.setBounds(new Rectangle(16, 185, 144, 31));
        sourceComboBox = new JComboBox(languages);

        sourceComboBox.setSelectedItem(Languages.getLanguageName("US"));
        targetComboBox = new JComboBox(languages);

        targetComboBox.setSelectedItem(Languages.getLanguageName("US"));
        targetComboBox.setBounds(new Rectangle(73, 216, 328, 48));
        targetComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    targetComboBox_actionPerformed(e);
                }
            });
        sourceComboBox.setBounds(new Rectangle(73, 128, 328, 47));
        sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sourceComboBox_actionPerformed(e);
                }
            });
        okButton.setText("Ok");
        okButton.setBounds(new Rectangle(36, 312, 105, 36));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        cancelButton.setText("Cancel");
        cancelButton.setBounds(new Rectangle(275, 312, 105, 36));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });

        sourceIconLabel.setIcon(new ImageIcon(getClass().getResource(Languages.getFlagPathForLan((String)sourceComboBox.getSelectedItem()))));
        targetIconLabel.setIcon(new ImageIcon(getClass().getResource(Languages.getFlagPathForLan((String)targetComboBox.getSelectedItem()))));

        jProjName.setBounds(new Rectangle(112, 61, 282, 33));
        this.getContentPane().add(okButton, null);
        this.getContentPane().add(cancelButton, null);
        this.getContentPane().add(targetLabel, null);
        this.getContentPane().add(targetIconLabel, null);
        this.getContentPane().add(targetComboBox, null);
        this.getContentPane().add(projectNameLabel, null);
        this.getContentPane().add(sourceLabel, null);
        this.getContentPane().add(sourceIconLabel, null);
        this.getContentPane().add(sourceComboBox, null);
        this.getContentPane().add(informationLabel, null);
        this.getContentPane().add(jProjName, null);
        this.setSize(435, 380);
        this.setResizable(false);

        // bug 4737485
        ListenForEnter listener = new ListenForEnter();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        jProjName.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);
        sourceComboBox.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);
        targetComboBox.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);
        okButton.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);

        /* disable combos as long as there's no text
        sourceComboBox.setEnabled(false);
        targetComboBox.setEnabled(false);
        jProjName.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent ke){
                boolean enableCombos = jProjName.getText() != null && jProjName.getText().length() > 0;
                sourceComboBox.setEnabled(enableCombos);
                targetComboBox.setEnabled(enableCombos);

            }
        });
         */
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        setVisible(false);

        didCancel = true;
    }

    void okButton_actionPerformed(ActionEvent e) {
        String sourceLan = (String)sourceComboBox.getSelectedItem();
        String targetLan = (String)targetComboBox.getSelectedItem();

        String projectName = jProjName.getText().trim();

        if (projectName.equals("")) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Please enter a project name.", "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (projectName.indexOf("_") != -1) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Please enter a name that does not contain the \"_\" character", "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (sourceLan.equals(targetLan)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Source language may not be the same as target language!", "Error", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (!checkProjectExists(projectName, Languages.getLanguageCode(sourceLan), Languages.getLanguageCode(targetLan))) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, "The name you selected for the new project already exists.\r\n Please select a different project name.", "Error", JOptionPane.WARNING_MESSAGE);

            //TODO how about offering to open the project ???
            return;
        }

        //This should never ever happen !!! The previous file must be closed prior to creating new project
        /*
        if(backend.hasCurrentFile()) {//a .xlf file is opened
            if(!Languages.getLanguageCode(sourceLan).equals(parent.sourceLan) ||
                    !Languages.getLanguageCode(targetLan).equals(parent.targetLan)) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this,"The language combination in this file does not match \r\nthe language combination in the selected mini-TM(s).\r\n Please select a different mini-TM","Error",JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
         */
        this.projectName = projectName;
        this.targetLang = Languages.getLanguageCode(targetLan);
        this.sourceLang = Languages.getLanguageCode(sourceLan);

        setVisible(false);
    }

    boolean checkProjectExists(String projectName, String sourceLan, String targetLan) {
        ProjectInfo nfo = new ProjectInfo(projectName, sourceLan, targetLan);

        return !allProjects.contains(nfo);
    }

    void sourceComboBox_actionPerformed(ActionEvent e) {
        String sourceLan = (String)sourceComboBox.getSelectedItem();
        String imagePath = Languages.getFlagPathForLan(sourceLan);
        sourceIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }

    void targetComboBox_actionPerformed(ActionEvent e) {
        String targetLan = (String)targetComboBox.getSelectedItem();
        String imagePath = Languages.getFlagPathForLan(targetLan);
        targetIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public boolean didCancel() {
        return didCancel;
    }
}
