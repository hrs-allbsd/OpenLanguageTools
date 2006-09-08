/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;
import org.jvnet.olt.editor.util.Bundle;

import javax.swing.*;
import javax.swing.event.*;

import org.jvnet.olt.editor.model.TMData;


public class SpellCheckerDIALOG extends JDialog implements ListSelectionListener {
    private Bundle bundle = Bundle.getBundle(SpellCheckerDIALOG.class.getName());
    
    public static JTextField errorTextField = new JTextField();
    public static int retValue = -1;
    public static int diff = 0;
    public static int curDiff = 0;
    public static int actionSignal = 0;
    public static JList suggestionList = new JList();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel actionPanel = new JPanel();
    JButton cancelButton = new JButton();
    JButton changeButton = new JButton();
    JButton ignoreButton = new JButton();
    JPanel centerPanel = new JPanel();
    JLabel errorLabel = new JLabel();
    JButton addButton = new JButton();
    JLabel suggestionLabel = new JLabel();
    public Thread parentThread = null;
    JScrollPane scr = new JScrollPane(suggestionList);
    PivotTextPane textPane;
    int index;
    String word;
    JButton ignoreAllButton = new JButton();
    Backend backend;

    public SpellCheckerDIALOG(MainFrame frame, Backend backend) { //,PivotTextPane textPane,int index) {
        super(frame, Bundle.getBundle(SpellCheckerDIALOG.class.getName()).getString("Spelling_Checker"), true);

        this.backend = backend;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSize(415, 300);
        setResizable(false);

        WindowListener wl = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                retValue = -1;
            }
        };

        this.addWindowListener(wl);
        getGlassPane().addMouseListener(new MouseAdapter() {
            });
    }

    public SpellCheckerDIALOG() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLan(String lan) {
        setTitle(bundle.getString("Spelling_Checker_-_") + lan);
    }

    /**
     * initialize a reference to the target text component,which can used later
     * to change some text. This method do not conform to MVC,but it imply
     * currently and can do.
     */
    public void setEditor(String word, int index) {
        //this.textPane = textPane;
        this.index = index;
        this.word = word;
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        cancelButton.setMnemonic('E');
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        changeButton.setMnemonic('C');
        changeButton.setText(bundle.getString("Change"));
        changeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeButton_actionPerformed(e);
                }
            });
        ignoreButton.setMnemonic('I');
        ignoreButton.setText(bundle.getString("Ignore"));
        ignoreButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ignoreButton_actionPerformed(e);
                }
            });
        centerPanel.setLayout(null);
        errorLabel.setForeground(Color.black);
        errorLabel.setText(bundle.getString("Not_in_dictionary:"));
        errorLabel.setBounds(new Rectangle(4, 5, 167, 27));
        errorTextField.setToolTipText("");
        errorTextField.setText(bundle.getString("Helo"));
        errorTextField.setBounds(new Rectangle(4, 34, 318, 31));
        addButton.setMnemonic('A');
        addButton.setText(bundle.getString("Add"));
        addButton.setBounds(new Rectangle(329, 34, 66, 31));
        addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addButton_actionPerformed(e);
                }
            });
        suggestionLabel.setForeground(Color.black);
        suggestionLabel.setText(bundle.getString("Suggestions:"));
        suggestionLabel.setBounds(new Rectangle(4, 70, 160, 24));
        suggestionList.addListSelectionListener(this);
        scr.setBounds(new Rectangle(4, 95, 390, 140));
        ignoreAllButton.setMnemonic('n');
        ignoreAllButton.setText(bundle.getString("Ignore_All"));
        ignoreAllButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ignoreAllButton_actionPerformed(e);
                }
            });
        this.getContentPane().add(actionPanel, BorderLayout.SOUTH);
        actionPanel.add(ignoreButton, null);
        actionPanel.add(ignoreAllButton, null);
        actionPanel.add(changeButton, null);
        actionPanel.add(cancelButton, null);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(errorLabel, null);
        centerPanel.add(suggestionLabel, null);
        centerPanel.add(scr, null);
        centerPanel.add(errorTextField, null);
        centerPanel.add(addButton, null);
        changeButton.setEnabled(false);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public void valueChanged(ListSelectionEvent e) {
        int status = backend.getTMData().tmsentences[PivotTextPane.wordInRowIndex].getTranslationStatus();

        if (status == TMData.TMSentence.VERIFIED) {
            return;
        }

        if (suggestionList.getSelectedIndex() != -1) {
            changeButton.setEnabled(true);
        }
    }

    int addButton_actionPerformed(ActionEvent e) {
        String newWord = errorTextField.getText().trim();

        if (newWord.equals("")) {
            //setVisible(false);
            return -2; //no response
        }

        TMData tmpdata = backend.getTMData();
        String old = tmpdata.tmsentences[PivotTextPane.wordInRowIndex].getTranslation(); //textPane.getText();
        String newStr = old.substring(0, index) + newWord + old.substring(index + word.length());
        Object[] v = org.jvnet.olt.editor.util.BaseElements.extractContent(newStr);
        tmpdata.tmsentences[PivotTextPane.wordInRowIndex].setTranslation(newStr);

        retValue = 0;
        diff += (newWord.length() - word.length());
        curDiff = newWord.length() - word.length();

        actionSignal = 1;
        addButton.setEnabled(false);
        disableGUI();

        setVisible(false);

        return retValue;
    }

    int ignoreButton_actionPerformed(ActionEvent e) {
        retValue = 1;

        actionSignal = 1;
        ignoreButton.setEnabled(false);
        disableGUI();
        setVisible(false);

        return 1;
    }

    int changeButton_actionPerformed(ActionEvent e) {
        if ((suggestionList == null) || (suggestionList.getSelectedValue() == null)) {
            return -2;
        }

        String newWord = ((String)suggestionList.getSelectedValue()).trim();

        if (newWord.equals(word)) {
            return -2; //no response
        }

        TMData tmpdata = backend.getTMData();

        String old = tmpdata.tmsentences[PivotTextPane.wordInRowIndex].getTranslation(); //textPane.getText();
        String newStr = old.substring(0, index) + newWord + old.substring(index + word.length());

        //Object[] v = org.jvnet.olt.editor.util.BaseElements.extractContent(newStr);
        tmpdata.tmsentences[PivotTextPane.wordInRowIndex].setTranslation(newStr);
        tmpdata.bTMFlags[AlignmentMain.testMain1.tableView.getSelectedRow()] = true;
        MainFrame.getAnInstance().setBHasModified(true);

        retValue = 2;
        diff += (newWord.length() - word.length());
        curDiff = newWord.length() - word.length();

        actionSignal = 1;
        changeButton.setEnabled(false);
        disableGUI();
        setVisible(false);

        return 2;
    }

    int cancelButton_actionPerformed(ActionEvent e) {
        retValue = 3;

        actionSignal = 1;
        setVisible(false);

        return 3;
    }

    int ignoreAllButton_actionPerformed(ActionEvent e) {
        retValue = 4;

        actionSignal = 1;
        ignoreAllButton.setEnabled(false);
        disableGUI();
        setVisible(false);

        return 4;
    }

    public void disableGUI() {
    }

    public void enableGUI() {
    }

    void setThread(Thread t) {
        parentThread = t;
    }
}
