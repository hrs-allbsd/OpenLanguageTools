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
import java.awt.event.*;

import java.util.Collections;
import java.util.List;
import org.jvnet.olt.editor.util.Bundle;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.Locale;

import javax.swing.*;

import org.jvnet.olt.editor.util.Languages;
import org.jvnet.olt.editor.util.Language;


public class OpenMiniTMPanel extends JDialog {
    private static final Logger logger = Logger.getLogger(OpenMiniTMPanel.class.getName());
    JLabel informationLabel = new JLabel();
    JLabel projectNameLabel = new JLabel();
    JComboBox projectNameComboBox;
    JComboBox sourceComboBox = new JComboBox();
    JComboBox targetComboBox = new JComboBox();
    JLabel sourceIconLabel = new JLabel();
    JLabel targetIconLabel = new JLabel();
    JLabel sourceLabel = new JLabel();
    JLabel targetLabel = new JLabel();
    boolean keyTyped = false;
    Vector<Language> languages = new Vector<Language>();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();
    JLabel TranslatorLabel = new JLabel();
    JTextField translatorTextField = new JTextField();
    private Vector allProjects;
    private String projectName;
    private String srcLang;
    private String tgtLang;
    private boolean didCancel;

    static private Bundle bundle = Bundle.getBundle(OpenMiniTMPanel.class.getName());
    // bug 4737485
    class ListenForEnter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            okButton.doClick();
        }
    }

    public OpenMiniTMPanel(JFrame parent, List allProjects) {
        super(parent, bundle.getString("Open_Project"), true);
        this.allProjects = new Vector(allProjects);
        Collections.sort(this.allProjects);

        languages = Languages.getLanguages();

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        informationLabel.setHorizontalAlignment(SwingConstants.LEFT);
        informationLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        informationLabel.setText(bundle.getString("Please_select_a_project_from_project_list:"));
        informationLabel.setBounds(new Rectangle(1, 9, 401, 24));
        this.getContentPane().setLayout(null);
        projectNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        projectNameLabel.setText(bundle.getString("Project_List__:"));
        projectNameLabel.setBounds(new Rectangle(4, 59, 116, 35));

        projectNameComboBox = new JComboBox(allProjects);
        projectNameComboBox.setBounds(new Rectangle(134, 59, 267, 35));

        projectNameComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    projectNameComboBox_actionPerformed(e);
                }
            });

        sourceIconLabel.setBounds(new Rectangle(24, 132, 49, 44));
        targetIconLabel.setBounds(new Rectangle(24, 221, 49, 44));
        sourceLabel.setText(bundle.getString("Source_Language:"));
        sourceLabel.setBounds(new Rectangle(24, 108, 182, 23));
        targetLabel.setText(bundle.getString("Target_Language:"));
        targetLabel.setBounds(new Rectangle(24, 188, 144, 31));
        
        Language lngUS = new Language ( Locale.US);
        
        sourceComboBox = new JComboBox(languages);
        sourceComboBox.setEnabled(false);

        sourceComboBox.setSelectedItem(lngUS);

        targetComboBox = new JComboBox(languages);
        targetComboBox.setEnabled(false);

        targetComboBox.setSelectedItem(lngUS);
        targetComboBox.setBounds(new Rectangle(79, 219, 322, 48));
        sourceComboBox.setBounds(new Rectangle(79, 131, 322, 47));
        okButton.setText(bundle.getString("Ok"));
        okButton.setBounds(new Rectangle(36, 295, 105, 36));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.setBounds(new Rectangle(275, 295, 105, 36));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });

        this.getContentPane().add(informationLabel, null);
        this.getContentPane().add(projectNameLabel, null);
        this.getContentPane().add(projectNameComboBox, null);
        this.getContentPane().add(sourceLabel, null);
        this.getContentPane().add(sourceComboBox, null);
        this.getContentPane().add(sourceIconLabel, null);
        this.getContentPane().add(targetLabel, null);
        this.getContentPane().add(targetIconLabel, null);
        this.getContentPane().add(targetComboBox, null);
        this.getContentPane().add(okButton, null);
        this.getContentPane().add(cancelButton, null);

        this.setSize(435, 360);
        this.setResizable(false);

        // bug 4737485
        ListenForEnter listener = new ListenForEnter();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        projectNameComboBox.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);
        okButton.registerKeyboardAction(listener, ks, JComponent.WHEN_FOCUSED);

        if (allProjects.isEmpty()) {
            okButton.setEnabled(false);
        } else {
            ProjectInfo p = (ProjectInfo)allProjects.get(0);
            setFlags(p.getSourceLang(), p.getTargetLang());

            this.srcLang = p.getSourceLang();
            this.tgtLang = p.getTargetLang();
            this.projectName = p.getName();
        }
    }

    void projectNameComboBox_actionPerformed(ActionEvent e) {
        ProjectInfo p = (ProjectInfo)projectNameComboBox.getSelectedItem();
        this.srcLang = p.getSourceLang();
        this.tgtLang = p.getTargetLang();
        this.projectName = p.getName();

        setFlags(this.srcLang, this.tgtLang);
    }

    private void setFlags(String srcLang, String tgtLang) {
        Language sourceLan = new Language (srcLang);
        Language targetLan = new Language (tgtLang);

        
        sourceComboBox.setSelectedItem(sourceLan);
        targetComboBox.setSelectedItem(targetLan);

        String imagePath = Languages.getFlagPath(srcLang);
        sourceIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
        imagePath = Languages.getFlagPath(tgtLang);
        targetIconLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        setVisible(false);
        didCancel = true;
    }

    void okButton_actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    public boolean didCancel() {
        return didCancel;
    }

    public String getTargetLang() {
        return tgtLang;
    }

    public String getSourceLang() {
        return srcLang;
    }

    public String getProjectName() {
        return projectName;
    }
}
