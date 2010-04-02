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

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

import java.util.Iterator;
import java.util.LinkedList;
import org.jvnet.olt.editor.util.Bundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;

import org.jvnet.olt.editor.model.TransProject;
import org.jvnet.olt.editor.util.Languages;
import org.jvnet.olt.minitm.MiniTMException;


public class MergeMiniTMPanel extends JDialog {
    private static final Logger logger = Logger.getLogger(MergeMiniTMPanel.class.getName());
    JLabel informationLabel = new JLabel();
    JLabel projectNameLabel = new JLabel();

    //JComboBox projectNameComboBox;
    ProjectComboBox projectNameComboBox; //= new ProjectComboBox();
    JComboBox sourceComboBox = new JComboBox();
    JComboBox targetComboBox = new JComboBox();
    JLabel sourceIconLabel = new JLabel();
    JLabel targetIconLabel = new JLabel();
    JLabel sourceLabel = new JLabel();
    JLabel targetLabel = new JLabel();
    Vector oldProjects = new Vector();

    //Vector oldTransIDs = new Vector();
    Vector oldSourceLans = new Vector();
    Vector oldTargetLans = new Vector();
    boolean keyTyped = false;
    Vector languages = new Vector();
    JButton nextButton = new JButton();
    JButton cancelButton = new JButton();
    String sourceLan = "";
    String targetLan = "";
    JFrame parent;
    JLabel translatorLabel = new JLabel();
    JTextField transltorTextField = new JTextField();
    CardLayout cards = new CardLayout();
    JPanel panel = new JPanel();
    TransProject project = null;
    private String miniTmDir;
    private Backend backend;

    private Bundle bundle = Bundle.getBundle(MergeMiniTMPanel.class.getName());
    
    class ProjectComboBox extends JComboBox {
        private String editorContent;

        ProjectComboBox(Vector v) {
            super(v);
        }

        void keepContent() {
            Object editComp = editor.getEditorComponent();

            if (editComp instanceof JTextField) {
                editorContent = ((JTextField)editComp).getText();
            }
        }

        boolean hasContent() {
            return (editorContent != null) && (editorContent.length() > 0);
        }

        void setEditText() {
            Object editComp = editor.getEditorComponent();

            if (editComp instanceof JTextField) {
                ((JTextField)editComp).setText(editorContent);
            }
        }

        public Object getSelectedItem() {
            String project = (String)super.getSelectedItem();
            StringTokenizer tokens = new StringTokenizer(project, "_", false);
            int count = tokens.countTokens();

            if (count == 3) {
                project = (String)tokens.nextElement();
            } else {
                project = "";
            }

            return project;
        }
    }

    class ProjectListCellRenderer extends JLabel implements ListCellRenderer {
        public ProjectListCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String project = value.toString();
            StringTokenizer tokens = new StringTokenizer(project, "_", false);
            int count = tokens.countTokens();

            if (count == 3) {
                project = (String)tokens.nextElement();

                //project = project.substring(0,project.lastIndexOf("_"));
            } else {
                project = "";
            }

            setText(project);
            setBackground(isSelected ? list.getSelectionBackground() : Color.white);
            setForeground(isSelected ? list.getSelectionForeground() : Color.black);

            return this;
        }
    }

    public MergeMiniTMPanel(JFrame parent, String title, String miniTmDir, Backend backend) {
        super(parent, title, true);
        this.miniTmDir = miniTmDir;

        this.backend = backend;

        this.parent = parent;
        init();

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        projectNameComboBox.setRenderer(new ProjectListCellRenderer());

        TransProject p = backend.getProject();

        if ((p.getProjectName() != null) && !p.getProjectName().trim().equals("")) {
            projectNameComboBox.setSelectedIndex(1);
        } else {
            projectNameComboBox.setSelectedIndex(0);
        }
    }

    public void init() {
        oldProjects.clear();
        oldTargetLans.clear();
        oldSourceLans.clear();

        oldProjects.addElement("");

        //oldTransIDs.addElement("");
        oldTargetLans.add("");
        oldSourceLans.add("");

        java.util.List projectHistory = new LinkedList(backend.getConfig().getProjectHistory());

        for (Iterator i = projectHistory.iterator(); i.hasNext();) {
            String p = (String)i.next();

            String[] parts = p.split("_");

            if ((parts == null) || (parts.length != 3)) {
                continue;
            }

            oldProjects.addElement(p);
            oldSourceLans.addElement(parts[1]);
            oldTargetLans.addElement(parts[2]);
        }

        languages = Languages.getLanguages();
        languages.insertElementAt(Languages.NO_LANGUAGE, 0);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(cards);

        panel.setLayout(null);

        informationLabel.setForeground(Color.black);
        informationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        informationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        informationLabel.setText(bundle.getString("Please_enter_the_following_information_for_this_project:"));
        informationLabel.setBounds(new Rectangle(1, 9, 401, 24));

        projectNameLabel.setForeground(Color.black);
        projectNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        projectNameLabel.setText(bundle.getString("Project_Name:"));
        projectNameLabel.setBounds(new Rectangle(0, 58, 116, 35));

        projectNameComboBox = new ProjectComboBox(oldProjects);
        projectNameComboBox.setEditable(true);
        projectNameComboBox.setBounds(new Rectangle(118, 59, 283, 33));
        ((JTextField)projectNameComboBox.getEditor().getEditorComponent()).addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    projectNameComboBox_actionPerformed(e);
                }
            });

        projectNameComboBox.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyListener() {
                public void keyTyped(KeyEvent e) {
                    projectNameComboBox_keyTyped(e);
                }

                public void keyPressed(KeyEvent e) {
                    projectNameComboBox_keyPressed(e);
                }

                public void keyReleased(KeyEvent e) {
                    projectNameComboBox_keyReleased(e);
                }
            });
        projectNameComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    projectNameComboBox_itemStateChanged(new ItemEvent((JComboBox)e.getSource(), ItemEvent.ITEM_STATE_CHANGED, projectNameComboBox.getSelectedItem(), ItemEvent.SELECTED));
                }
            });
        projectNameComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    projectNameComboBox_itemStateChanged(e);
                }
            });
        sourceIconLabel.setBounds(new Rectangle(16, 129, 49, 38));
        targetIconLabel.setBounds(new Rectangle(16, 218, 49, 35));
        sourceLabel.setText(bundle.getString("Source_Language:"));
        sourceLabel.setBounds(new Rectangle(16, 105, 182, 23));
        targetLabel.setText(bundle.getString("Target_Language:"));
        targetLabel.setBounds(new Rectangle(16, 185, 144, 31));

        sourceComboBox = new JComboBox(languages);

        sourceComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    sourceComboBox_itemStateChanged(e);
                }
            });
        sourceComboBox.setSelectedItem(Languages.getLanguageName("US"));
        targetComboBox = new JComboBox(languages);
        targetComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    targetComboBox_itemStateChanged(e);
                }
            });
        targetComboBox.setSelectedItem(Languages.getLanguageName("US"));
        targetComboBox.setBounds(new Rectangle(73, 216, 328, 38));
        sourceComboBox.setBounds(new Rectangle(73, 128, 328, 38));
        nextButton.setMnemonic('N');
        nextButton.setText(bundle.getString("Next"));
        nextButton.setBounds(new Rectangle(240, 288, 82, 26));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    nextButton_actionPerformed(e);
                }
            });
        cancelButton.setMnemonic('C');
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.setBounds(new Rectangle(340, 288, 82, 26));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        translatorLabel.setForeground(Color.black);
        translatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translatorLabel.setText(bundle.getString("Translator_ID:"));
        translatorLabel.setBounds(new Rectangle(0, 35, 116, 35));
        transltorTextField.setBounds(new Rectangle(118, 36, 251, 33));
        transltorTextField.setEditable(false);
        transltorTextField.setText(backend.getConfig().getTranslatorID());
        panel.add(informationLabel, null);
        panel.add(projectNameComboBox, null);

        //panel.add(translatorLabel, null);
        //panel.add(transltorTextField, null);
        panel.add(cancelButton, null);
        panel.add(nextButton, null);
        panel.add(targetComboBox, null);
        panel.add(targetIconLabel, null);
        panel.add(targetLabel, null);
        panel.add(sourceComboBox, null);
        panel.add(sourceIconLabel, null);
        panel.add(sourceLabel, null);
        panel.add(projectNameLabel, null);
        this.getContentPane().add("first", panel);
        this.setSize(450, 345);
        this.setResizable(true);
    }
    public Insets getInsets() {
        return new Insets(20, 10, 0, 0);
    }

    public String getProjectName() {
        JTextField field = (JTextField)projectNameComboBox.getEditor().getEditorComponent();

        return field.getText().trim();
    }

    void projectNameComboBox_itemStateChanged(ItemEvent e) {
        if (!nextButton.isEnabled()) {
            return;
        }

        int index = projectNameComboBox.getSelectedIndex();

        if (index != -1) {
            if (index != 0) {
                //transltorTextField.setText((String)oldTransIDs.elementAt(index));
                sourceLan = (String)oldSourceLans.elementAt(index);

                targetLan = (String)oldTargetLans.elementAt(index);

                sourceComboBox.setSelectedItem(Languages.findByCode(sourceLan));
                targetComboBox.setSelectedItem(Languages.findByCode(targetLan));

                JComboBox source = projectNameComboBox;
                JTextField field = (JTextField)source.getEditor().getEditorComponent();
                field.setText((String)projectNameComboBox.getSelectedItem());
            } else {
                //transltorTextField.setText("");
                sourceComboBox.setSelectedItem(Languages.findByCode("US"));
                targetComboBox.setSelectedItem(Languages.findByCode("US"));
            }
        } else {
            if (projectNameComboBox.hasContent()) {
                projectNameComboBox.setEditText();
            }
        }
    }

    void sourceComboBox_itemStateChanged(ItemEvent e) {
        if (sourceComboBox.getSelectedItem() != null) {
            Languages.Language lng = (Languages.Language)sourceComboBox.getSelectedItem();
            sourceLan = lng.getShortCode();

            if (sourceLan.trim().equals("")) {
                sourceComboBox.setSelectedItem(Languages.findByCode("US"));
            } else {
                String imagePath = Languages.getFlagPath(sourceLan);
                sourceIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
            }
        }
    }

    void targetComboBox_itemStateChanged(ItemEvent e) {
        if (targetComboBox.getSelectedItem() != null) {
            Languages.Language lng = (Languages.Language)targetComboBox.getSelectedItem();            
            targetLan = lng.getShortCode();

            if (targetLan.trim().equals("")) {
                targetComboBox.setSelectedItem(Languages.findByCode("US"));
            } else {
                String imagePath = Languages.getFlagPath(targetLan);
                targetIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
            }
        }
    }

    /**
     * key listener
     */
    void projectNameComboBox_keyPressed(KeyEvent e) {
    }

    void projectNameComboBox_keyTyped(KeyEvent e) {
        keyTyped = true;
    }

    void projectNameComboBox_keyReleased(KeyEvent e) {
        JComboBox source = projectNameComboBox;
        JTextField field = (JTextField)source.getEditor().getEditorComponent();

        String id = field.getText();

        if ((id == null) || (id.trim().length() == 0)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            nextButton.setEnabled(false);

            return;
        }

        if ((id != null) && (id.trim().length() > 20)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            field.setText(id.substring(0, id.length() - 1));

            return;
        }

        nextButton.setEnabled(true);

        if ((field.getText().trim() != "") && (field.getSelectionStart() == field.getSelectionEnd()) && (field.getSelectionStart() != 0)) {
            String value = (String)field.getText().substring(0, field.getSelectionStart());
            int index = oldProjects.indexOf(value);

            if (index != -1) {
                source.setSelectedIndex(index);
                source.setPopupVisible(true);
            } else {
                for (int i = 0; i < oldProjects.size(); i++) {
                    String temp = (String)oldProjects.elementAt(i);

                    if (temp.startsWith(value)) {
                        index = i;

                        break;
                    }
                }

                if (index == -1) {
                    source.setPopupVisible(false);
                } else {
                    source.setSelectedIndex(index);
                    field.setText(value);
                    source.setPopupVisible(true);
                }
            }
        } else {
            source.setPopupVisible(false);
        }

        projectNameComboBox.keepContent();
        keyTyped = false;
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    void nextButton_actionPerformed(ActionEvent e) {
        String projectName = this.getProjectName();

        if (projectName.equals("") || sourceLan.equals("") || targetLan.equals("")) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, bundle.getString("You_must_enter_a_project_name."), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        } else if (sourceLan.equals(targetLan)) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, bundle.getString("Source_language_can_not_be_same_as_target_language."), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        } else if (!checkProjectValid(projectName)) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, bundle.getString("The_project_name_you_entered_is_invalid.It_can_not_include_underscore_character"), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        } /*else if(!checkTransIDValid(transltorTextField.getText())) {
        Toolkit.getDefaultToolkit().beep();

        JOptionPane.showMessageDialog(this,"The translator ID you entered is invalid.It can not include \"_\"","Error",JOptionPane.WARNING_MESSAGE);
        return;
        }*/
        else if (!checkValid(projectName, sourceLan, targetLan)) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(this, bundle.getString("The_name_you_selected_for_the_new_project_already_exists._Please_select_a_different_project_name."), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

            return;
        } else {
            if (backend.hasCurrentFile()) { //a .tm file is opened

                if (!sourceLan.equals(backend.getProject().getSrcLang()) || !targetLan.equals(backend.getProject().getSrcLang())) {
                    Toolkit.getDefaultToolkit().beep();

                    JOptionPane.showMessageDialog(this, bundle.getString("The_language_combination_in_this_file_does_not_match_the_language_combination_in_the_selected_mini-TM(s)._Please_select_a_different_mini-TM"), bundle.getString("Error"), JOptionPane.WARNING_MESSAGE);

                    return;
                }
            }

            try {
                // don't care what dataType this MiniTM is, since we're only creating it to merge it anyway.
                project = new TransProject(projectName, sourceLan, targetLan, miniTmDir, "");

                java.util.List projectHistory = backend.getConfig().getProjectHistory();

                MergeMiniTMTablePanel panel = new MergeMiniTMTablePanel(this, getContentPane(), cards, miniTmDir, projectHistory);
                this.setTitle(bundle.getString("Merge_Tool_Options"));
                this.getContentPane().add("second", panel);
                cards.show(this.getContentPane(), "second");
            } catch (MiniTMException mtme) {
                logger.throwing(getClass().getName(), "nextButton_actionPerformed", mtme);
                logger.severe("Exception:" + mtme);
            }
        }
    }

    boolean checkProjectValid(String projectName) {
        if (projectName.indexOf("_") != -1) {
            return false;
        }

        return true;
    }

    boolean checkTransIDValid(String transID) {
        if (transID.indexOf("_") != -1) {
            return false;
        }

        return true;
    }

    //TODO remove; this is a dupe
    boolean checkValid(String projectName, String sourceLan, String targetLan) {
        String temp = projectName + "_" + sourceLan + "_" + targetLan;

        java.util.List projectHistory = backend.getConfig().getProjectHistory();

        return !projectHistory.contains(temp);
    }

    void projectNameComboBox_actionPerformed(ActionEvent e) {
        nextButton.doClick();
    }
}
