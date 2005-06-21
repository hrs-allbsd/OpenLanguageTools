/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import org.jvnet.olt.editor.model.*;


/**
 * FindAndReplacePanel
 * JPanel's subcomponent,which will let user search a word or sentence
 * in the current document(Alignment or TM),and replace with other words
 * or sentence after finding.
 */

// bug 4743624
//public class FindAndReplacePanel extends JPanel implements ActionListener {
//TODO change to Dialog ?
public class FindAndReplacePanel extends JPanel implements ActionListener, KeyListener {
    private static final Logger logger = Logger.getLogger(FindAndReplacePanel.class.getName());
    public static boolean REPLACE = false;
    public static boolean REPLACE_ALL = true;
    public static boolean stop = false;

    /**
     * find criteria panel,which is on the left of this panel
     * including find text,replace text,case sensitive,
     * search from start and direction(forward or backward)
     */
    JPanel findSubPanel = new JPanel();

    /**
     * action panel,which is on the right of this panel,
     * including find,replace,replace all,cancel and help
     */
    JPanel actionPanel = new JPanel();

    /**
     * Replace All button
     */
    JButton replaceAllButton = new JButton();

    /**
     * Cancel button
     */
    JButton cancelButton = new JButton();

    /**
     * replace button
     */
    JButton replaceButton = new JButton();

    /**
     * find button
     */
    JButton findButton = new JButton();

    /**
     * help button
     */
    JButton helpButton = new JButton();

    /**
     * layout of the actionPanel
     */
    FlowLayout flowLayout1 = new FlowLayout();

    /**
     * a panel,which contains the find text component and
     * the replace text component
     */
    JPanel textPanel = new JPanel();

    /**
     * a panel,which contains forward radio button
     * and backward radio button
     */
    JPanel sourcePanel = new JPanel();

    /**
     * a panel,which contains case sensitive and search from start check boxes
     */
    JPanel optionPanel = new JPanel();

    /**
     * a label,to show on the left of the textfield of find text
     */
    JLabel findLabel = new JLabel();

    /**
     * a label,to show on the left of the textfield of replace text
     */
    JLabel replaceLabel = new JLabel();

    /**
     * border of option panel
     */
    TitledBorder titledBorder1;

    /**
     * border of direction panel
     */
    TitledBorder titledBorder2;

    /**
     * layout of direction panel
     */
    GridLayout gridLayout2 = new GridLayout();

    /**
     * forward radio button
     */
    JRadioButton targetRadioButton = new JRadioButton();

    /**
     * backward radio button
     */
    JRadioButton sourceRadioButton = new JRadioButton();

    /**
     * layout of option panel
     */
    GridLayout gridLayout3 = new GridLayout();

    /**
     * option,to search from strat of the document
     */
    JCheckBox searchDirectionOption = new JCheckBox();

    /**
     * option,to search with case sensitively
     */
    JCheckBox caseOption = new JCheckBox();

    /**
     * layout of find panel
     */
    GridLayout gridLayout1 = new GridLayout();

    /**
     * layout of this panel
     */
    GridBagLayout gridBagLayout2 = new GridBagLayout();

    /**
     * find combobox,the text in which will be found
     */
    JComboBox findComboBox = new JComboBox();

    /**
     * replace combobox,the text in which will be used to replace
     */
    JComboBox replaceComboBox = new JComboBox();

    /**
     * layout of text panel
     */
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    /*public static Result oldSrcResult = null;
    public static Result srcResult = null;
    public static Result oldTargetResult = null;
    public static Result targetResult = null;*/
    private JTextPane textArea = new JTextPane();
    private JScrollPane scr = new JScrollPane(textArea);

    /**
     * temporary varibles
     * editType:
     *    current edit type
     * currentSentence:
     *    current sentence number when finding or replacing
     * currentItem:
     *    current sentence item when finding or replacing
     */

    //int editType,currentSentence,currentItem;

    /**
     * static varibles
     * oldStrFind:
     *    recall the last finding word or sentence
     * oldStrReplace:
     *    recall the last replacing word or sentence
     */

    //public static String oldStrFind=null;
    //public static String oldStrReplace=null;

    /**
     * static varibles
     * oldFind:
     *    recall the history finding words or sentences
     * oldReplace:
     *    recall the history replacing words or sentences
     */

    //public static Vector oldFind=new Vector();
    //public static Vector oldReplace=new Vector();

    /**
     * temporary varibales,which can be used to see whether
     * the current search criteria is a new finding.
     */

    //boolean oldCaseSensitive=false;
    //boolean oldForward=true;
    //boolean oldFromStart=false;
    //boolean needNewFind=true;

    /**
     * current search result
     */

    //Vector result=new Vector();

    /**
     * current browse index of the current finding result
     */

    //int indexInResult = 0;

    /**
     * item values of the visible document
     */

    //int[] items;
    JDialog parent = null;

    /**
     * constructor
     * construct its UI
     */
    private Backend backend;
    Thread thread = null;

    {
        textArea.setSize(200, 50);
        textArea.setBackground(new Color(204, 204, 204));
        textArea.setEditable(false);
        textArea.setFont(MainFrame.dlgFont);
        scr.setBorder(null);
    }

    public FindAndReplacePanel(JDialog findDlg, Backend backend) {
        this.backend = backend;
        parent = findDlg;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    void init() {
        ((JTextField)findComboBox.getEditor().getEditorComponent()).requestFocus();
        parent.getGlassPane().addMouseListener(new MouseAdapter() {
            });

        // bug 4736736 ---------------------------
        //this calls the repainting the buttons
        if (sourceRadioButton.isSelected()) {
            //logger.finer("bFlagWriteProtection="+!MainFrame.bFlagWriteProtection);
            replaceButton.setEnabled(isWriteProtect());
            replaceAllButton.setEnabled(isWriteProtect());
        } else if (this.targetRadioButton.isSelected()) {
            replaceButton.setEnabled(true);
            replaceAllButton.setEnabled(true);
        }

        //end added ------------------------------
    }

    /**
     * paint its UI
     */
    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(SystemColor.inactiveCaptionBorder, 1), "Options");
        titledBorder2 = new TitledBorder(BorderFactory.createLineBorder(SystemColor.inactiveCaptionText, 1), "Search in");
        this.setLayout(gridBagLayout2);
        actionPanel.setLayout(flowLayout1);
        replaceAllButton.setMaximumSize(new Dimension(120, 27));
        replaceAllButton.setMinimumSize(new Dimension(120, 27));
        replaceAllButton.setPreferredSize(new Dimension(120, 27));
        replaceAllButton.setToolTipText("Replace all found string");
        replaceAllButton.setText("Replace All");
        replaceAllButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replaceAllButton_actionPerformed(e);
                }
            });
        cancelButton.setMaximumSize(new Dimension(120, 27));
        cancelButton.setMinimumSize(new Dimension(120, 27));
        cancelButton.setPreferredSize(new Dimension(120, 27));
        cancelButton.setToolTipText("Close");
        cancelButton.setText("Close");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        replaceButton.setMaximumSize(new Dimension(120, 27));
        replaceButton.setMinimumSize(new Dimension(120, 27));
        replaceButton.setPreferredSize(new Dimension(120, 27));
        replaceButton.setToolTipText("Replace the current string");
        replaceButton.setText("Replace");
        replaceButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replaceButton_actionPerformed(e);
                }
            });
        findButton.setMaximumSize(new Dimension(120, 27));
        findButton.setMinimumSize(new Dimension(120, 27));
        findButton.setPreferredSize(new Dimension(120, 27));
        findButton.setToolTipText("Start finding");
        findButton.setText("Find");
        findButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findButton_actionPerformed(e);
                }
            });
        helpButton.setMaximumSize(new Dimension(120, 27));
        helpButton.setMinimumSize(new Dimension(120, 27));
        helpButton.setPreferredSize(new Dimension(120, 27));
        helpButton.setToolTipText("Help for finding or replacing");
        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    helpButton_actionPerformed(e);
                }
            });
        findSubPanel.setLayout(gridLayout1);
        sourcePanel.setLayout(gridLayout2);
        textPanel.setLayout(gridBagLayout1);
        findLabel.setHorizontalAlignment(SwingConstants.LEFT);
        findLabel.setText("Text to find:");

        replaceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        replaceLabel.setText("Replace With:");
        optionPanel.setBorder(titledBorder1);
        optionPanel.setLayout(gridLayout3);
        sourcePanel.setBorder(titledBorder2);
        gridLayout2.setColumns(2);
        targetRadioButton.setToolTipText("Search in the target language");
        targetRadioButton.setText("Target language");
        targetRadioButton.addActionListener(this);

        ButtonGroup bg = new ButtonGroup();
        findComboBox.setToolTipText("Enter a word or string to find");

        //findComboBox.requestFocus();
        findComboBox.getEditor().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findButton.requestFocus();
                    findButton.doClick();
                }
            });

        findButton.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (e.getModifiers() == 0)) {
                        findButton.doClick();
                    }
                }
            });
        replaceComboBox.setToolTipText("Enter a word or string to replace ");
        caseOption.setToolTipText("Match case sensitivity");
        searchDirectionOption.setToolTipText("search down");
        searchDirectionOption.setSelected(true);
        sourceRadioButton.setToolTipText("Search in the source language");
        bg.add(targetRadioButton);
        bg.add(sourceRadioButton);
        sourceRadioButton.setSelected(true);

        //these two command noted by cl141268
        //replaceButton.setEnabled(!MainFrame.bFlagWriteProtection);
        //replaceAllButton.setEnabled(!MainFrame.bFlagWriteProtection);
        sourceRadioButton.setText("Source language");
        sourceRadioButton.addActionListener(this);
        findSubPanel.setMaximumSize(new Dimension(238, 159));
        findSubPanel.setMinimumSize(new Dimension(200, 200));
        findSubPanel.setPreferredSize(new Dimension(200, 200));
        searchDirectionOption.setText("Down");
        gridLayout3.setColumns(1);
        gridLayout3.setRows(2);
        caseOption.setText("Match Case");
        this.setMaximumSize(new Dimension(280, 240));
        this.setMinimumSize(new Dimension(280, 240));
        this.setPreferredSize(new Dimension(456, 300));
        actionPanel.setMaximumSize(new Dimension(100, 200));
        actionPanel.setMinimumSize(new Dimension(100, 200));
        actionPanel.setPreferredSize(new Dimension(100, 200));
        gridLayout1.setColumns(1);
        gridLayout1.setRows(3);
        textPanel.setMinimumSize(new Dimension(181, 76));
        textPanel.setPreferredSize(new Dimension(181, 76));
        findComboBox.setEditable(true);
        replaceComboBox.setEditable(true);

        // bug 4743624
        ///////////////////////////////////////////////////
        ((JTextField)findComboBox.getEditor().getEditorComponent()).addKeyListener(this);
        ((JTextField)replaceComboBox.getEditor().getEditorComponent()).addKeyListener(this);

        ///////////////////////////////////////////////////
        this.add(findSubPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 0), 0, 0));
        findSubPanel.add(textPanel, null);
        textPanel.add(replaceLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 3, 5, 0), 22, 18));
        textPanel.add(findComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 14, 6, 3), 9, 4));
        textPanel.add(replaceComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 14, 5, 3), 9, 4));

        textPanel.add(findLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 27, 24));
        findSubPanel.add(sourcePanel, null);
        findSubPanel.add(optionPanel, null);
        optionPanel.add(caseOption, null);
        optionPanel.add(searchDirectionOption, null);
        sourcePanel.add(sourceRadioButton, null);
        sourcePanel.add(targetRadioButton, null);
        this.add(actionPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 1), 0, 0));
        actionPanel.add(findButton, null);
        actionPanel.add(replaceButton, null);
        actionPanel.add(replaceAllButton, null);
        actionPanel.add(cancelButton, null);

        //actionPanel.add(helpButton, null);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sourceRadioButton) {
            replaceButton.setEnabled(!isWriteProtect());
            replaceAllButton.setEnabled(!isWriteProtect());
        } else if (e.getSource() == targetRadioButton) {
            replaceButton.setEnabled(true);
            replaceAllButton.setEnabled(true);
        }
    }

    //TODO add listener to protection
    private boolean isWriteProtect() {
        return backend.getConfig().isBFlagWriteProtection();
    }

    private String getReplaceString() {
        if (replaceComboBox.getSelectedItem() == null) {
            return ((JTextField)replaceComboBox.getEditor().getEditorComponent()).getText().trim();
        } else {
            if (replaceComboBox.getSelectedIndex() != -1) {
                return (String)replaceComboBox.getSelectedItem();
            } else {
                return ((JTextField)replaceComboBox.getEditor().getEditorComponent()).getText().trim();
            }
        }
    }

    Search checkSearch() {
        String searchString = (String)findComboBox.getSelectedItem();

        if (searchString == null) {
            searchString = ((JTextField)findComboBox.getEditor().getEditorComponent()).getText();
        } else {
            if (findComboBox.getSelectedIndex() == -1) {
                searchString = ((JTextField)findComboBox.getEditor().getEditorComponent()).getText();
            }
        }

        if (searchString.equals("")) {
            return null;
        }

        boolean caseFlag = caseOption.isSelected();
        boolean forwardFlag = searchDirectionOption.isSelected();

        return new Search(searchString, caseFlag, forwardFlag);
    }

    /**
     * add the replacing string to the replacing combobox and replacing history
     */
    void addToReplaceList(String str) {
        if ((str == null) || str.trim().equals("")) {
            return;
        }

        for (int i = 0; i < this.replaceComboBox.getItemCount(); i++) {
            if (this.replaceComboBox.getItemAt(i).toString().equals(str)) {
                return;
            } else {
                continue;
            }
        }

        this.replaceComboBox.addItem(str);
    }

    /**
     * add the finding string to the finding combobox and finding history
     */
    void addToFindList(String str) {
        if ((str == null) || str.trim().equals("")) {
            return;
        }

        for (int i = 0; i < this.findComboBox.getItemCount(); i++) {
            if (this.findComboBox.getItemAt(i).toString().equals(str)) {
                return;
            } else {
                continue;
            }
        }

        this.findComboBox.addItem(str);
    }

    public boolean disableGUI() {
        if (stop) {
            return false;
        }

        parent.getGlassPane().setVisible(true);
        parent.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        stop = true;

        return true;
    }

    public void enableGUI() {
        parent.getGlassPane().setVisible(false);
        parent.getGlassPane().setCursor(Cursor.getDefaultCursor());
        stop = false;
    }

    /**
     * find a word or sentence in the current document.
     * First check the finding string,if ok,then judge if it is a new
     * finding,then search the result and highlight the first found
     * string in the main window.Otherwise,just highlight the next
     * found string.
     */
    void findButton_actionPerformed(ActionEvent e) {
        if (!disableGUI()) {
            return;
        }

        // stop editing once, but will call startEditing in doSearchResult()
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            Object[] message = new Object[1];
            String informationString = "Find what in " + ((this.sourceRadioButton.isSelected()) ? "source?" : "target?");
            textArea.setSize(200, 50);
            textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;

            String[] options = { "Ok" };
            int r = JOptionPane.showOptionDialog(this.getRootPane(), // the parent that the dialog blocks
                message, // the dialog message array
                "Warning", // the title of the dialog window
                JOptionPane.DEFAULT_OPTION, // option type
                JOptionPane.WARNING_MESSAGE, // message type
                null, // optional icon, use null to use the default icon
                options, // options string array, will be made into buttons
                options[0]);

            switch (r) {
            case 0: // ok
                break;
            }

            this.enableGUI();

            return;
        }

        addToFindList(s.what);

        //bug 4763116++
        if (this.sourceRadioButton.isSelected()) {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain1.tableView.getSelectedRow();
                TMInnerPanel.oldSrcResult = null;
            }

            Result r = TMInnerPanel.getSearchResult(s, true, false, "");
            TMInnerPanel.doSearchResult(r, s, true);
        } else {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain2.tableView.getSelectedRow();
                TMInnerPanel.oldTargetResult = null;
            }

            Result r = TMInnerPanel.getSearchResult(s, false, false, "");
            TMInnerPanel.doSearchResult(r, s, false);
        }

        stopWhile();
    }

    public void stopWhile() {
        //if(thread == null) {
        thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.currentThread().sleep(200);
                        } catch (Exception ex) {
                        }

                        enableGUI();
                    }
                });

        //}
        thread.start();
    }

    /**
     * replace the string in the current document with the replacing string.
     * After checking the finding text and replacing text is OK,
     * it will get finding result. If it is a new finding,it will find
     * the new result.At the end,replace the first found result.
     */
    void replaceButton_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            String informationString = "Replace what in " + ((this.sourceRadioButton.isSelected()) ? "source?" : "target?");
            JOptionPane.showMessageDialog(this.getRootPane(), informationString, "Warning", JOptionPane.WARNING_MESSAGE);

            return;
        }

        addToFindList(s.what);
        addToReplaceList((String)replaceComboBox.getSelectedItem());

        //adjustment
        if (this.sourceRadioButton.isSelected()) {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain1.tableView.getSelectedRow();
                TMInnerPanel.oldSrcResult = null;
            }
        } else {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain2.tableView.getSelectedRow();
                TMInnerPanel.oldTargetResult = null;
            }
        }

        TMData tmpdata = backend.getTMData();

        boolean tagProtect = backend.getConfig().isTagProtection();

        if (this.sourceRadioButton.isSelected()) {
            Result r = null;
            boolean doUnselect = true;

            if (TMInnerPanel.oldSrcResult == null) {
                r = TMInnerPanel.getSearchResult(s, true, false, "");
            }
            // bug 4736729
            else if (TMInnerPanel.oldSrcResult.search.equals(s)) {
                if (canReplace(TMInnerPanel.oldSrcResult, true, tagProtect)) {
                    String temp = (tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].getSource();
                    String tempStr = temp.substring(0, TMInnerPanel.oldSrcResult.position) + getReplaceString() + temp.substring(TMInnerPanel.oldSrcResult.position + TMInnerPanel.oldSrcResult.search.what.length());

                    //undo for replace
                    DocumentUndoableEdit edit = new DocumentUndoableEdit(true, "REPLACE", TMInnerPanel.oldSrcResult.rowIndex, 0, new String(temp), new String(tempStr), ((tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].getTranslationType(), ((tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].getTranslationType());
                    MainFrame.undo.addDocumentEdit(edit);

                    //------------------------------------
                    (tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].setSource(tempStr);

                    //(tmpdata).tmsentences[TMInnerPanel.oldSrcResult.rowIndex].setTranslationType(4);
                    MainFrame.getAnInstance().setBHasModified(true);
                    tmpdata.bTMFlags[TMInnerPanel.oldSrcResult.rowIndex] = true;
                } else {
                    JOptionPane.showMessageDialog(this.getRootPane(), "This item cannot be replaced as tag protection is switched on!", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                //---------------------------------------------
                r = TMInnerPanel.getSearchResult(s, true, true, getReplaceString());
            } else {
                r = TMInnerPanel.getSearchResult(s, true, false, "");
            }

            doReplaceResult(r, s, true, doUnselect, REPLACE);
        } else { //replace target

            Result r = null;
            boolean doUnselect = true;

            if (TMInnerPanel.oldTargetResult == null) {
                r = TMInnerPanel.getSearchResult(s, false, false, "");
            } // bug 4736729
            else if (TMInnerPanel.oldTargetResult.search.equals(s)) {
                if (canReplace(TMInnerPanel.oldTargetResult, false, tagProtect)) {
                    String temp = (tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].getTranslation();
                    String tempStr = temp.substring(0, TMInnerPanel.oldTargetResult.position) + getReplaceString() + temp.substring(TMInnerPanel.oldTargetResult.position + TMInnerPanel.oldTargetResult.search.what.length());

                    //undo for replace
                    DocumentUndoableEdit edit = new DocumentUndoableEdit(false, "REPLACE", TMInnerPanel.oldTargetResult.rowIndex, 0, new String(temp), new String(tempStr), ((tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].getTranslationType(), ((tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].getTranslationStatus() * 10) + TMData.TMSentence.USER_TRANSLATION);
                    MainFrame.undo.addDocumentEdit(edit);

                    //------------------------------------
                    (tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].setTranslation(tempStr);
                    (tmpdata).tmsentences[TMInnerPanel.oldTargetResult.rowIndex].setTranslationType(TMData.TMSentence.USER_TRANSLATION);
                    MainFrame.getAnInstance().setBHasModified(true);
                    tmpdata.bTMFlags[TMInnerPanel.oldTargetResult.rowIndex] = true;
                } else {
                    JOptionPane.showMessageDialog(this.getRootPane(), "This item cannot be replaced as tag protection is switched on!", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                //---------------------------------------------
                r = TMInnerPanel.getSearchResult(s, false, true, getReplaceString());
            } else {
                r = TMInnerPanel.getSearchResult(s, false, false, "");
            }

            doReplaceResult(r, s, false, doUnselect, REPLACE);
        }
    }

    /**
     *Replace all the finding string with the replacing word or string
     * After checking the finding text and replacing text is OK,
     * it will get finding result. If it is a new finding,it will find
     * the new result.At the end,replace all the found result.
     */
    void replaceAllButton_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            String informationString = "Replace what in " + ((this.sourceRadioButton.isSelected()) ? "source?" : "target?");
            JOptionPane.showMessageDialog(this.getRootPane(), informationString, "Warning", JOptionPane.WARNING_MESSAGE);

            return;
        }

        addToFindList(s.what);
        addToReplaceList(getReplaceString());

        if (this.sourceRadioButton.isSelected()) {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain1.tableView.getSelectedRow();
                TMInnerPanel.oldSrcResult = null;
            }
        } else {
            if (TMInnerPanel.initSearchRow == -1) {
                TMInnerPanel.initSearchRow = AlignmentMain.testMain2.tableView.getSelectedRow();
                TMInnerPanel.oldTargetResult = null;
            }
        }

        int currentrow = TMInnerPanel.initSearchRow;

        TMData tmpdata = backend.getTMData();
        boolean tagProtect = backend.getConfig().isTagProtection();

        //prepare for undo
        ArrayList replaceResult = new ArrayList();

        // bug 4736729
        int replacedItemCount = 0;

        if (this.sourceRadioButton.isSelected()) {
            s.forwardFlag = true;

            Vector v = TMInnerPanel.getSearchAllResult(s, true);

            if (v.size() != 0) {
                for (int i = (v.size() - 1); i >= 0; i--) {
                    Result r = (Result)v.elementAt(i);

                    if (canReplace(r, true, tagProtect)) {
                        String temp = (tmpdata).tmsentences[r.rowIndex].getSource();
                        String tempStr = temp.substring(0, r.position) + getReplaceString() + temp.substring(r.position + r.search.what.length());

                        //prepare for undo
                        replaceResult.add(new Object[] {
                                new Integer(r.rowIndex), new Integer(0), new String(temp),
                                new String(tempStr),
                                new Integer(((tmpdata).tmsentences[r.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[r.rowIndex].getTranslationType()),
                                new Integer(((tmpdata).tmsentences[r.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[r.rowIndex].getTranslationType())
                            });
                        (tmpdata).tmsentences[r.rowIndex].setSource(tempStr);

                        //(tmpdata).tmsentences[r.rowIndex].setTranslationType(4);
                        MainFrame.getAnInstance().setBHasModified(true);
                        tmpdata.bTMFlags[r.rowIndex] = true;
                        replacedItemCount++;
                    }
                }

                //undo for replace all
                DocumentUndoableEdit edit = new DocumentUndoableEdit(true, "REPLACE ALL", currentrow, 0, replaceResult);
                MainFrame.undo.addDocumentEdit(edit);

                //------------------------------------
                AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getBounds());
                Toolkit.getDefaultToolkit().beep();

                if (replacedItemCount == v.size()) {
                    JOptionPane.showMessageDialog(this.getRootPane(), v.size() + ((v.size() > 1) ? " items have" : " item has") + " been replaced!", "Replace All", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this.getRootPane(), v.size() + " items found, but only " + replacedItemCount + " replaced, as tag protection is switched on!", "Replace All", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } else { //replace target
            s.forwardFlag = true;

            Vector v = TMInnerPanel.getSearchAllResult(s, false);

            if (v.size() != 0) {
                for (int i = (v.size() - 1); i >= 0; i--) {
                    Result r = (Result)v.elementAt(i);

                    if (canReplace(r, false, tagProtect)) {
                        String temp = (tmpdata).tmsentences[r.rowIndex].getTranslation();
                        String tempStr = temp.substring(0, r.position) + getReplaceString() + temp.substring(r.position + r.search.what.length());

                        //prepare for undo
                        replaceResult.add(new Object[] {
                                new Integer(r.rowIndex), new Integer(0), new String(temp),
                                new String(tempStr),
                                new Integer(((tmpdata).tmsentences[r.rowIndex].getTranslationStatus() * 10) + (tmpdata).tmsentences[r.rowIndex].getTranslationType()),
                                new Integer(((tmpdata).tmsentences[r.rowIndex].getTranslationStatus() * 10) + TMData.TMSentence.USER_TRANSLATION)
                            });

                        (tmpdata).tmsentences[r.rowIndex].setTranslation(tempStr);
                        (tmpdata).tmsentences[r.rowIndex].setTranslationType(TMData.TMSentence.USER_TRANSLATION);
                        MainFrame.getAnInstance().setBHasModified(true);
                        tmpdata.bTMFlags[r.rowIndex] = true;
                        replacedItemCount++;
                    }
                }

                //undo for replace all
                DocumentUndoableEdit edit = new DocumentUndoableEdit(false, "REPLACE ALL", currentrow, 0, replaceResult);
                MainFrame.undo.addDocumentEdit(edit);

                //------------------------------------
                AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getBounds());
                Toolkit.getDefaultToolkit().beep();

                if (replacedItemCount == v.size()) {
                    JOptionPane.showMessageDialog(this.getRootPane(), v.size() + ((v.size() > 1) ? " items have" : " item has") + " been replaced!", "Replace All", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this.getRootPane(), v.size() + " items found, but only " + replacedItemCount + " replaced, as tag protection is switched on!", "Replace All", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    /**
     * Cancel
     * Just hide its parent dialog
     */
    void cancelButton_actionPerformed(ActionEvent e) {
        Container c = this.getParent();

        while (!(c instanceof JDialog)) {
            c = c.getParent();
        }

        c.setVisible(false);

        if (TMInnerPanel.oldTargetResult != null) {
            //PivotTextPane textPane = AlignmentMain.editPanes[TMInnerPanel.oldTargetResult.rowIndex%PivotTextRender1.ROW_COUNT+PivotTextRender1.ROW_COUNT];//.testMain2.getTextPane(oldTargetResult.rowIndex,oldTargetResult.sentenceIndex);
            //if(textPane != null)
            //  textPane.select(TMInnerPanel.oldTargetResult.position,TMInnerPanel.oldTargetResult.position);
        }

        if (TMInnerPanel.oldSrcResult != null) {
            //PivotTextPane textPane = AlignmentMain.editPanes[TMInnerPanel.oldSrcResult.rowIndex%PivotTextRender1.ROW_COUNT];//.testMain2.getTextPane(oldTargetResult.rowIndex,oldTargetResult.sentenceIndex);
            //if(textPane != null)
            //  textPane.select(TMInnerPanel.oldSrcResult.position,TMInnerPanel.oldSrcResult.position);
        }
    }

    /**
     * Help
     */
    void helpButton_actionPerformed(ActionEvent e) {
    }

    void doReplaceResult(Result result, Search s, boolean isSrc, boolean doUnselect, boolean replaceAll) {
        if (result == null) {
            if (isSrc) {
                TMInnerPanel.oldSrcResult = null;
            } else {
                TMInnerPanel.oldTargetResult = null;
            }

            Toolkit.getDefaultToolkit().beep();

            Object[] message = new Object[1];
            String informationString = "Can not find " + s.what + ((isSrc) ? " in source." : " in target.");
            textArea.setSize(200, 50);
            textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;

            String[] options = { "Ok" };
            int r = JOptionPane.showOptionDialog(this.getRootPane(), // the parent that the dialog blocks
                message, // the dialog message array
                "Searching", // the title of the dialog window
                JOptionPane.DEFAULT_OPTION, // option type
                JOptionPane.INFORMATION_MESSAGE, // message type
                null, // optional icon, use null to use the default icon
                options, // options string array, will be made into buttons
                options[0]);

            switch (r) {
            case 0: // ok
                break;
            }
        } else {
            if (isSrc) {
                if (!replaceAll) {
                    // go to the line and make selection
                    TMInnerPanel.doSearchResult(result, s, true);
                }

                TMInnerPanel.oldSrcResult = (Result)result.clone();
            } else {
                if (!replaceAll) {
                    // go to the line and make selection
                    TMInnerPanel.doSearchResult(result, s, false);
                }

                TMInnerPanel.oldTargetResult = (Result)result.clone();
            }
        }
    }

    /**
     * util function
     */
    boolean canReplace(Result r, boolean isSource, boolean tagProtection) {
        TMData tmpdata = backend.getTMData();

        int status = (tmpdata).tmsentences[r.rowIndex].getTranslationStatus();

        if (status == TMData.TMSentence.VERIFIED) {
            return false;
        }

        if (isSource) {
            if (isWriteProtect()) {
                return false;
            }

            PivotText p = new PivotText((tmpdata).tmsentences[r.rowIndex].getSource());

            return p.canReplace(r.position, r.position + r.search.what.length(), tagProtection);
        } else {
            PivotText p = new PivotText((tmpdata).tmsentences[r.rowIndex].getTranslation());

            return p.canReplace(r.position, r.position + r.search.what.length(), tagProtection);
        }
    }

    // bug 4743624  ----------------------------------------
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        //logger.finer("aaaaaaaaaaaaaaaaaa");
    }

    public void keyReleased(KeyEvent e) {
        //logger.finer("bbbbbbbbbbbbbbbbbb");
        if ((e.getKeyCode() == KeyEvent.VK_CUT) || ((e.getKeyCode() == KeyEvent.VK_X) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            String selected = textComponent.getSelectedText();

            if ((selected != null) && (selected.length() > 0)) {
                textComponent.replaceSelection("");

                Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(selected);
                syscb.setContents(selection, null);
            }
        } else if ((e.getKeyCode() == KeyEvent.VK_COPY) || ((e.getKeyCode() == KeyEvent.VK_C) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            String selected = textComponent.getSelectedText();

            if ((selected != null) && (selected.length() > 0)) {
                //textComponent.replaceSelection("");
                Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(selected);
                syscb.setContents(selection, null);
            }
        } else if ((e.getKeyCode() == KeyEvent.VK_PASTE) || ((e.getKeyCode() == KeyEvent.VK_V) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = syscb.getContents(this);

            if (contents == null) {
                return;
            }

            try {
                String text = (String)(contents.getTransferData(DataFlavor.stringFlavor));
                textComponent.replaceSelection(text);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //logger.finer("-----");
        }
    }

    //-------------------------------------------------------------------------------------------
}
