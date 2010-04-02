/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.net.URL;
import java.util.Iterator;

import java.util.List;
import org.jvnet.olt.editor.util.Bundle;
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

    static private Bundle bundle = Bundle.getBundle(NewMiniTMPanel.class.getName());
    
    // bug 4737485
    class ListenForEnter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            okButton.doClick();
        }
    }

    public NewMiniTMPanel(JFrame parent, List allProjects) {
        super(parent, bundle.getString("New_Project"), true);
        this.allProjects = allProjects;

//        languages = Languages.getLanguagesBySort();
        languages = Languages.getLanguages();

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
        informationLabel.setText(bundle.getString("Please_enter_the_following_information_for_this_project:"));
        informationLabel.setBounds(new Rectangle(5, 21, 401, 24));
        this.getContentPane().setLayout(null);
        projectNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        projectNameLabel.setText(bundle.getString("Project_Name:"));
        projectNameLabel.setBounds(new Rectangle(0, 58, 116, 35));

        sourceIconLabel.setBounds(new Rectangle(16, 129, 49, 44));
        targetIconLabel.setBounds(new Rectangle(16, 218, 49, 44));
        sourceLabel.setText(bundle.getString("Source_Language:"));
        sourceLabel.setBounds(new Rectangle(16, 105, 182, 23));
        targetLabel.setText(bundle.getString("Target_Language:"));        
        targetLabel.setBounds(new Rectangle(16, 185, 144, 31));
        
        Languages.Language lngUS = Languages.findByCode("US");
        
        ItemListener itemLstnr =   new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    Languages.Language lng = (Languages.Language)e.getItem();
                    String path = Languages.getFlagPath(lng.getShortCode());
                    URL res = getClass().getResource(path);    
                    ImageIcon icon = new ImageIcon(res);            

                    if(e.getSource() == sourceComboBox){
                        sourceIconLabel.setIcon(icon);                        
                    }
                    else{
                        targetIconLabel.setIcon(icon);
                        
                    }
                }
            }
        };

        
        sourceComboBox = new JComboBox(languages);

        sourceComboBox.addItemListener(itemLstnr);
        
        
        
        sourceComboBox.setSelectedItem(lngUS);
        targetComboBox = new JComboBox(languages);
        targetComboBox.addItemListener(itemLstnr);
        
        targetComboBox.setSelectedItem(lngUS);
        targetComboBox.setBounds(new Rectangle(73, 216, 328, 48));
//        targetComboBox.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    targetComboBox_actionPerformed(e);
//                }
//            });
        sourceComboBox.setBounds(new Rectangle(73, 128, 328, 47));
//        sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    sourceComboBox_actionPerformed(e);
//                }
//            });
        okButton.setText(bundle.getString("Ok"));
        okButton.setBounds(new Rectangle(36, 312, 105, 36));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.setBounds(new Rectangle(275, 312, 105, 36));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
            
            
/*        Languages.Language lng = (Languages.Language)sourceComboBox.getSelectedItem();
        String path = Languages.getFlagPathForLan(lng.getShortCode());
        URL res = getClass().getResource(path);    
        ImageIcon icon = new ImageIcon(res);            
        sourceIconLabel.setIcon(icon);
        targetIconLabel.setIcon(new ImageIcon(getClass().getResource(Languages.getFlagPathForLan((String)targetComboBox.getSelectedItem()))));
*/
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

    void setFlagIcon(){
        
    }
    
    void cancelButton_actionPerformed(ActionEvent e) {
        setVisible(false);

        didCancel = true;
    }

    void okButton_actionPerformed(ActionEvent e) {
        String sourceLan = ((Languages.Language)sourceComboBox.getSelectedItem()).getShortCode();
        String targetLan = ((Languages.Language)targetComboBox.getSelectedItem()).getShortCode();

        String projectName = jProjName.getText().trim();

        if (projectName.equals("")) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, bundle.getString("Please_enter_a_project_name."), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (projectName.indexOf("_") != -1) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, bundle.getString("Please_enter_a_name_that_does_not_contain_the_underscore_character"), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (sourceLan.equals(targetLan)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, bundle.getString("Source_language_may_not_be_the_same_as_target_language!"), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (!checkProjectExists(projectName,sourceLan, targetLan)) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, bundle.getString("The_name_you_selected_for_the_new_project_already_exists.\r\n_Please_select_a_different_project_name."), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

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
        this.targetLang = targetLan;
        this.sourceLang = sourceLan;

        setVisible(false);
    }

    boolean checkProjectExists(String projectName, String sourceLan, String targetLan) {
        ProjectInfo nfo = new ProjectInfo(projectName, sourceLan, targetLan);

        return !allProjects.contains(nfo);
    }
/*
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
*/
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
