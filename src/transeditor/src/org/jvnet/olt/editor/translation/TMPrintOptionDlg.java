/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;
import org.jvnet.olt.editor.util.Bundle;

import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.*;


public class TMPrintOptionDlg extends JDialog implements ChangeListener {
    private Bundle bundle = Bundle.getBundle(TMPrintOptionDlg.class.getName());
    private static final Logger logger = Logger.getLogger(TMPrintOptionDlg.class.getName());
    public static int contentType = 1; //1--all,2--target only,3--source only
    public static int tagType = 1; //1--all,2--without only,3--abb only
    public static boolean showType = false;
    public static boolean showNumber = false;
    JTabbedPane tabbedOptionPane = new JTabbedPane();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel contentPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(0, 10, 0, 0);
        }
    };

    JPanel tagsPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(0, 10, 0, 0);
        }
    };

    JPanel othersPanel = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JLabel contentLabel = new JLabel();
    JPanel contentSubPanel = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JRadioButton sourceAndTargetRadioButton = new JRadioButton();
    JRadioButton sourceRadioButton = new JRadioButton();
    JRadioButton targetRadioButton = new JRadioButton();
    JPanel actionPanel = new JPanel();
    JButton cancelButton = new JButton();
    JButton okButton = new JButton();
    BorderLayout borderLayout3 = new BorderLayout();
    JRadioButton abbreTagsRadioButton = new JRadioButton();
    JRadioButton withoutTagsRadioButton = new JRadioButton();
    JRadioButton withTagsRadioButton = new JRadioButton();
    JPanel tagsSubPanel = new JPanel();
    GridLayout gridLayout2 = new GridLayout();
    JLabel tagsLabel = new JLabel();
    JCheckBox showTypeCheckBox = new JCheckBox();
    JCheckBox showNumCheckBox = new JCheckBox();

    public TMPrintOptionDlg() {
        setTitle(bundle.getString("Print_Options..."));

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        setSize(420, 250);
        setResizable(false);
    }

    void init() {
        switch (contentType) {
        case 1:
            sourceAndTargetRadioButton.setSelected(true);

            break;

        case 2:
            targetRadioButton.setSelected(true);

            break;

        case 3:
            sourceRadioButton.setSelected(true);

            break;
        }

        switch (tagType) {
        case 1:
            withTagsRadioButton.setSelected(true);

            break;

        case 2:
            withoutTagsRadioButton.setSelected(true);

            break;

        case 3:
            abbreTagsRadioButton.setSelected(true);

            break;
        }

        showTypeCheckBox.setSelected(showType);
        showNumCheckBox.setSelected(showNumber);
    }

    public void stateChanged(ChangeEvent e) {
        //logger.finer("changed to "+tabbedOptionPane.getSelectedIndex());
        //tabbedOptionPane.getSelectedComponent().setBackground(Color.darkGray);
        //tabbedOptionPane.setForegroundAt(tabbedOptionPane.getSelectedIndex(),Color.lightGray);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        contentPanel.setLayout(borderLayout2);
        contentLabel.setText(""); //Whether the source or target or both should be printed:");
        contentSubPanel.setLayout(gridLayout1);

        //sourceAndTargetRadioButton.setSelected(true);
        sourceAndTargetRadioButton.setText(bundle.getString("Source_and_Target_Language"));
        sourceRadioButton.setText(bundle.getString("Source_Language_Only"));
        targetRadioButton.setText(bundle.getString("Target_Language_Only"));

        ButtonGroup contentGroup = new ButtonGroup();
        tabbedOptionPane.setBackground(new Color(220, 220, 220));

        //tabbedOptionPane.setForeground(Color.lightGray);
        tabbedOptionPane.addChangeListener(this);
        tagsSubPanel.setBackground(Color.lightGray);
        contentGroup.add(sourceAndTargetRadioButton);
        contentGroup.add(targetRadioButton);
        contentGroup.add(sourceRadioButton);
        gridLayout1.setRows(3);
        gridLayout1.setColumns(1);
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        okButton.setMaximumSize(new Dimension(85, 27));
        okButton.setMinimumSize(new Dimension(85, 27));
        okButton.setPreferredSize(new Dimension(85, 27));
        okButton.setText(bundle.getString("OK"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        tagsPanel.setLayout(borderLayout3);
        abbreTagsRadioButton.setText(bundle.getString("Abbreviated_Tags"));
        withoutTagsRadioButton.setText(bundle.getString("Without_Tags"));
        withTagsRadioButton.setText(bundle.getString("With_Tags"));

        ButtonGroup tagGroup = new ButtonGroup();
        tagGroup.add(withTagsRadioButton);
        tagGroup.add(withoutTagsRadioButton);
        tagGroup.add(abbreTagsRadioButton);

        //withTagsRadioButton.setSelected(true);
        tagsSubPanel.setLayout(gridLayout2);
        gridLayout2.setRows(3);
        gridLayout2.setColumns(1);
        tagsLabel.setText(""); //Whether or not tags should be printed:");
        othersPanel.setLayout(null);
        showTypeCheckBox.setText(bundle.getString("Show_Translation_Type"));
        showTypeCheckBox.setBounds(new Rectangle(11, 8, 294, 66));
        showNumCheckBox.setText(bundle.getString("Show_Segment_Number"));
        showNumCheckBox.setBounds(new Rectangle(11, 77, 294, 66));
        this.getContentPane().add(tabbedOptionPane, BorderLayout.CENTER);
        tabbedOptionPane.add(contentPanel, bundle.getString("Languages"));
        contentPanel.add(contentLabel, BorderLayout.NORTH);
        contentPanel.add(contentSubPanel, BorderLayout.CENTER);
        contentSubPanel.add(sourceAndTargetRadioButton, null);
        contentSubPanel.add(targetRadioButton, null);
        contentSubPanel.add(sourceRadioButton, null);
        tabbedOptionPane.add(tagsPanel, bundle.getString("Tags"));
        tagsPanel.add(tagsSubPanel, BorderLayout.CENTER);
        tagsSubPanel.add(withTagsRadioButton, null);
        tagsSubPanel.add(withoutTagsRadioButton, null);
        tagsSubPanel.add(abbreTagsRadioButton, null);
        tagsPanel.add(tagsLabel, BorderLayout.NORTH);
        tabbedOptionPane.add(othersPanel, bundle.getString("Other..."));
        othersPanel.add(showTypeCheckBox, null);
        othersPanel.add(showNumCheckBox, null);
        this.getContentPane().add(actionPanel, BorderLayout.SOUTH);
        actionPanel.add(okButton, null);
        actionPanel.add(cancelButton, null);
    }

    void okButton_actionPerformed(ActionEvent e) {
        int type = 1;

        if (this.targetRadioButton.isSelected()) {
            type = 2;
        } else if (this.sourceRadioButton.isSelected()) {
            type = 3;
        }

        contentType = type;

        type = 1;

        if (this.withoutTagsRadioButton.isSelected()) {
            type = 2;
        } else if (this.abbreTagsRadioButton.isSelected()) {
            type = 3;
        }

        tagType = type;
        showType = showTypeCheckBox.isSelected();
        showNumber = showNumCheckBox.isSelected();

        setVisible(false);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
