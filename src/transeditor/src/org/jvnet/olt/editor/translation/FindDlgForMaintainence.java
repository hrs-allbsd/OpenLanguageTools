/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;

//import org.w3c.dom.Element;
import org.jvnet.olt.editor.model.*;


/**
 * FindAndReplacePanel
 * JPanel's subcomponent,which will let user search a word or sentence
 * in the current document(Alignment or TM),and replace with other words
 * or sentence after finding.
 */
public class FindDlgForMaintainence extends JPanel {
    private static final Logger logger = Logger.getLogger(FindDlgForMaintainence.class.getName());
    public static boolean REPLACE = false;
    public static boolean REPLACE_ALL = true;
    public static Result result = null;
    public static Result oldResult = null;

    /**
     * layout of text panel
     */

    /*public static Result oldSrcResult = null;
    public static Result srcResult = null;
    public static Result oldTargetResult = null;
    public static Result targetResult = null;*/
    public static JTextPane textArea = new JTextPane();
    public static JScrollPane scr = new JScrollPane(textArea);

    static {
        textArea.setSize(200, 50);
        textArea.setBackground(new Color(204, 204, 204));
        textArea.setEditable(false);
        textArea.setFont(MainFrame.dlgFont);
        scr.setBorder(null);
    }

    public static boolean stop = false;

    /*public static Result targetResult = null;
    public static Result oldTargetResult = null;*/

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
    /**
     * forward radio button
     */
    /**
     * backward radio button
     */
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
    /**
     * layout of this panel
     */
    /**
     * find combobox,the text in which will be found
     */
    JComboBox findComboBox = new JComboBox();

    /**
     * replace combobox,the text in which will be used to replace
     */
    JComboBox replaceComboBox = new JComboBox();
    JDialog parent = null;
    private Backend backend;
    Thread thread = null;
    GridLayout gridLayout2 = new GridLayout();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    /**
     * constructor
     * construct its UI
     */
    public FindDlgForMaintainence(JDialog findDlg, Backend backend) {
        parent = findDlg;
        this.backend = backend;

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
    }

    /**
     * paint its UI
     */
    private void jbInit() throws Exception {
        gridLayout2.setRows(2);
        gridLayout2.setColumns(1);
        titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(SystemColor.inactiveCaptionBorder, 1), "Options");
        titledBorder2 = new TitledBorder(BorderFactory.createLineBorder(SystemColor.inactiveCaptionText, 1), "Search in");
        this.setLayout(null);
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
        findSubPanel.setLayout(gridLayout2);
        textPanel.setLayout(gridBagLayout1);
        findLabel.setHorizontalAlignment(SwingConstants.LEFT);
        findLabel.setText("Text to find:");

        replaceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        replaceLabel.setText("Replace With:");
        optionPanel.setBorder(titledBorder1);
        optionPanel.setLayout(gridLayout3);

        ButtonGroup bg = new ButtonGroup();
        findComboBox.setToolTipText("Enter a word or string to find");
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
        actionPanel.setBounds(new Rectangle(248, 0, 144, 200));
        findSubPanel.setBounds(new Rectangle(4, 0, 244, 200));

        //replaceButton.setEnabled(!MainFrame.bFlagWriteProtection);
        //replaceAllButton.setEnabled(!MainFrame.bFlagWriteProtection);
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
        textPanel.setMinimumSize(new Dimension(181, 76));
        textPanel.setPreferredSize(new Dimension(181, 76));
        findComboBox.setEditable(true);
        replaceComboBox.setEditable(true);

        this.add(findSubPanel, null);
        findSubPanel.add(textPanel, null);
        textPanel.add(findComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(7, 0, 0, 0), 37, 11));
        textPanel.add(replaceLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 8, 0), 5, 17));
        textPanel.add(replaceComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(11, 0, 8, 0), 37, 11));
        textPanel.add(findLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 0, 0), 13, 14));
        findSubPanel.add(optionPanel, null);
        optionPanel.add(caseOption, null);
        optionPanel.add(searchDirectionOption, null);

        this.add(actionPanel, null);
        actionPanel.add(findButton, null);
        actionPanel.add(replaceButton, null);
        actionPanel.add(replaceAllButton, null);
        actionPanel.add(cancelButton, null);
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
        parent.getGlassPane().setCursor(Cursor.getDefaultCursor());
        parent.getGlassPane().setVisible(false);
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

        MiniTMAlignmentMain.testMain.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            //Object[]    message = new Object[1];
            String informationString = "Find what?";

            /*textArea.setSize(200,50);
            textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;*/
            String message = informationString;

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

            return;
        }

        addToFindList(s.what);

        Result r = getSearchResult(s, true, false, "");
        doSearchResult(r, s, true);

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
        if (!disableGUI()) {
            return;
        }

        MiniTMAlignmentMain.testMain.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            //Object[]    message = new Object[1];
            String informationString = "Replace what?";

            /*textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;*/
            String message = informationString;

            String[] options = { "Ok" };

            int r = JOptionPane.showOptionDialog(this, // the parent that the dialog blocks
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

            return;
        }

        addToFindList(s.what);
        addToReplaceList((String)replaceComboBox.getSelectedItem());

        boolean tagProtect = backend.getConfig().isTagProtection();

        Result r = null;
        boolean doUnselect = true;

        if (oldResult == null) {
            r = getSearchResult(s, true, false, "");
        } else if (oldResult.search.equals(s) && canReplace(oldResult, true, tagProtect)) {
            if (oldResult.sentenceIndex == 0) {
                String temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex / 3]).getSource();
                String tempStr = temp.substring(0, oldResult.position) + getReplaceString() + temp.substring(oldResult.position + oldResult.search.what.length());

                //MiniTMAlignmentMain.data[oldResult.rowIndex/3] = new org.jvnet.olt.minitm.AlignedSegment(tempStr,((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex/3]).getTranslation(),((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex/3]).getTranslatorID());
                MiniTMAlignmentMain.testMain.tableView.setValueAt(tempStr, oldResult.rowIndex, 1);
            } else {
                String temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex / 3]).getTranslation();
                String tempStr = temp.substring(0, oldResult.position) + getReplaceString() + temp.substring(oldResult.position + oldResult.search.what.length());

                //MiniTMAlignmentMain.data[oldResult.rowIndex/3] = new org.jvnet.olt.minitm.AlignedSegment(((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex/3]).getSource(),tempStr,((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[oldResult.rowIndex/3]).getTranslatorID());
                MiniTMAlignmentMain.testMain.tableView.setValueAt(tempStr, oldResult.rowIndex, 1);
            }

            r = getSearchResult(s, true, true, getReplaceString());

            //MiniTMAlignmentMain.testMain.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(oldResult.rowIndex,0,true));
            MiniTMAlignmentMain.testMain.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(oldResult.rowIndex, 1, false));
        } else {
            r = getSearchResult(s, true, false, "");
        }

        doReplaceResult(r, s, true, doUnselect, REPLACE);
        stopWhile();
    }

    /**
     *Replace all the finding string with the replacing word or string
     * After checking the finding text and replacing text is OK,
     * it will get finding result. If it is a new finding,it will find
     * the new result.At the end,replace all the found result.
     */
    void replaceAllButton_actionPerformed(ActionEvent e) {
        if (!disableGUI()) {
            return;
        }

        MiniTMAlignmentMain.testMain.stopEditing();

        Search s = checkSearch();

        if (s == null) {
            Toolkit.getDefaultToolkit().beep();

            //Object[]    message = new Object[1];
            String informationString = "Replace what?";

            /*textArea.setSize(200,50);
            textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;*/
            String message = informationString;

            String[] options = { "Ok" };

            int r = JOptionPane.showOptionDialog(this, // the parent that the dialog blocks
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
        addToReplaceList(getReplaceString());

        boolean tagProtect = backend.getConfig().isBFlagTagProtection();

        //s.forwardFlag = true;
        Vector v = getSearchAllResult(s);

        if (v.size() != 0) {
            for (int i = (v.size() - 1); i >= 0; i--) {
                Result r = (Result)v.elementAt(i);

                if (canReplace(r, true, tagProtect)) {
                    if (r.sentenceIndex == 0) {
                        String temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex / 3]).getSource();
                        String tempStr = temp.substring(0, r.position) + getReplaceString() + temp.substring(r.position + r.search.what.length());

                        //MiniTMAlignmentMain.data[r.rowIndex/3] = new org.jvnet.olt.minitm.AlignedSegment(tempStr,((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex/3]).getTranslation(),((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex/3]).getTranslatorID());
                        MiniTMAlignmentMain.testMain.tableView.setValueAt(tempStr, r.rowIndex, 1);
                    } else {
                        String temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex / 3]).getTranslation();
                        String tempStr = temp.substring(0, r.position) + getReplaceString() + temp.substring(r.position + r.search.what.length());

                        //MiniTMAlignmentMain.data[r.rowIndex/3] = new org.jvnet.olt.minitm.AlignedSegment(((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex/3]).getSource(),tempStr,((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex/3]).getTranslatorID());
                        MiniTMAlignmentMain.testMain.tableView.setValueAt(tempStr, r.rowIndex, 1);
                    }
                }
            }

            MiniTMAlignmentMain.testMain.tableView.repaint(MiniTMAlignmentMain.testMain.tableView.getBounds());
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, v.size() + ((v.size() > 1) ? " occurrences have" : " occurrence has") + " been replaced!", "Replace All", JOptionPane.PLAIN_MESSAGE);
        }

        stopWhile();
    }

    Vector getSearchAllResult(Search s) {
        Vector v = new Vector();

        oldResult = null;
        result = null;

        do {
            if (oldResult == null) {
                int startRowIndex = MiniTMAlignmentMain.testMain.tableView.getSelectedRow();
                int startSentenceIndex = ((startRowIndex - (3 * (startRowIndex / 3))) == 1) ? 1 : 0;
                startRowIndex = startRowIndex / 3;

                int startPosition = getCurrentPosition(s, true);

                if (startRowIndex == -1) {
                    startRowIndex = 0;
                }

                result = search(MiniTMAlignmentMain.data, startRowIndex, startSentenceIndex, startPosition, s);
            } else { //based on previous result

                int startRowIndex = oldResult.rowIndex;
                int startSentenceIndex = ((startRowIndex - (3 * (startRowIndex / 3))) == 1) ? 1 : 0;
                startRowIndex = oldResult.rowIndex / 3;

                int startPosition = -1;

                startPosition = oldResult.position + oldResult.search.what.length();
                result = search(MiniTMAlignmentMain.data, startRowIndex, startSentenceIndex, startPosition, s);
            }

            if (result != null) {
                v.addElement(result);
                oldResult = (Result)result.clone();
            }
        } while (result != null);

        oldResult = null;

        return v;
    }

    void doSearchResult(Result result, Search s, boolean isSrc) {
        if (result == null) {
            boolean noOne = true;

            if (oldResult != null) {
                noOne = false;
            }

            Toolkit.getDefaultToolkit().beep();

            //Object[]    message = new Object[1];
            String informationString = "Finished searching the file.";

            if (noOne) {
                informationString = "The search item was not found in the Mini-TM file.";
            }

            /*textArea.setText(informationString);

            scr.setSize(new Dimension(textArea.getPreferredSize()));

            message[0] = scr;*/
            String message = informationString;

            String[] options = { "Ok" };

            int r = JOptionPane.showOptionDialog(this, //AlignmentMain.frame,                             // the parent that the dialog blocks
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
            if (oldResult != null) {
                if (oldResult.sentenceIndex == 0) {
                    MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[(oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(oldSrcResult.rowIndex,oldSrcResult.sentenceIndex);

                    if (textPane != null) {
                        textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                    }
                } else {
                    MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[((oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT) + PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(oldSrcResult.rowIndex,oldSrcResult.sentenceIndex);

                    if (textPane != null) {
                        textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                    }
                }
            }

            MiniTMAlignmentMain.testMain.navigateTo(result.rowIndex);
            oldResult = (Result)result.clone();

            MiniTMAlignmentMain.testMain.tableView.repaint(MiniTMAlignmentMain.testMain.tableView.getCellRect(result.rowIndex, 1, false));
        }
    }

    Result getSearchResult(Search s, boolean isSource, boolean replace, String replaceStr) {
        //if(isSource) {
        if (oldResult == null) {
            int startRowIndex = MiniTMAlignmentMain.testMain.tableView.getSelectedRow();
            int startSentenceIndex = ((startRowIndex - (3 * (startRowIndex / 3))) == 1) ? 1 : 0;
            startRowIndex = startRowIndex / 3;

            int startPosition = getCurrentPosition(s, true);

            //int startSentenceIndex = getCurrentSentenceIndex(s,true);
            if (startRowIndex == -1) {
                startRowIndex = 0;
            }

            result = search(MiniTMAlignmentMain.data, startRowIndex, startSentenceIndex, startPosition, s);
        } else { //based on previous result

            if (oldResult.rowIndex == MiniTMAlignmentMain.testMain.tableView.getSelectedRow()) {
                int startRowIndex = oldResult.rowIndex;
                int startSentenceIndex = ((startRowIndex - (3 * (startRowIndex / 3))) == 1) ? 1 : 0;
                startRowIndex = oldResult.rowIndex / 3;

                int startPosition = -1;

                //int startSentenceIndex = oldResult.sentenceIndex;
                if (oldResult.search.equals(s)) {
                    if (s.forwardFlag) {
                        if (replace) {
                            startPosition = oldResult.position + replaceStr.length();
                        } else {
                            startPosition = oldResult.position + oldResult.search.what.length();
                        }
                    } else {
                        startPosition = oldResult.position - 1;
                    }
                } else {
                    startPosition = getCurrentPosition(s, true);
                }

                result = search(MiniTMAlignmentMain.data, startRowIndex, startSentenceIndex, startPosition, s);
            } else {
                int startRowIndex = MiniTMAlignmentMain.testMain.tableView.getSelectedRow();
                int startSentenceIndex = ((startRowIndex - (3 * (startRowIndex / 3))) == 1) ? 1 : 0;
                startRowIndex = (startRowIndex == -1) ? 0 : startRowIndex;
                startRowIndex = startRowIndex / 3;

                int startPosition = getCurrentPosition(s, true);

                //int startSentenceIndex = getCurrentSentenceIndex(s,true);
                if (startRowIndex == -1) {
                    startRowIndex = 0;
                }

                result = search(MiniTMAlignmentMain.data, startRowIndex, startSentenceIndex, startPosition, s);
            }
        }

        //logger.finer("row="+result.rowIndex+" position="+result.position);
        return result;
    }

    int getCurrentPosition(Search s, boolean isSrc) {
        return s.forwardFlag ? 0 : Integer.MAX_VALUE;
    }

    int getCurrentSentenceIndex(Search s, boolean isSrc) {
        return 0;
    }

    private Result search(Object[] alignments, int startRow, int startSentence, int startPosition, Search s) {
        if (alignments == null) {
            return null;
        } else {
            if (s.forwardFlag) { //forward

                for (int i = startRow; i < alignments.length; i++) {
                    if (i != startRow) {
                        startSentence = 0;
                        startPosition = 0;
                    }

                    org.jvnet.olt.minitm.AlignedSegment ali = (org.jvnet.olt.minitm.AlignedSegment)alignments[i];

                    if (ali != null) {
                        if (startSentence == 0) {
                            String p = ali.getSource();
                            int old = startPosition;

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.indexOf(s.what, startPosition);

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            } else {
                                int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            }

                            startPosition = 0;
                            p = ali.getTranslation();

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.indexOf(s.what, startPosition);

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            } else {
                                int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            }
                        } else if (startSentence == 1) {
                            String p = ali.getTranslation();

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.indexOf(s.what, startPosition);

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            } else {
                                int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            }
                        }
                    }
                }

                if ((startRow != 0) && (oldResult == null)) {
                    String[] options = { "Yes", "No" };

                    int r = JOptionPane.showOptionDialog(this, //AlignmentMain.frame,                             // the parent that the dialog blocks
                        "The editor has searched to the end of the file. \nDo you want to continue searching from the start of the file?", // the dialog message array
                        "Searching", // the title of the dialog window
                        JOptionPane.DEFAULT_OPTION, // option type
                        JOptionPane.INFORMATION_MESSAGE, // message type
                        null, // optional icon, use null to use the default icon
                        options, // options string array, will be made into buttons
                        options[0]);

                    switch (r) {
                    case 0: // yes

                        for (int i = 0; i < startRow; i++) {
                            startSentence = 0;
                            startPosition = 0;

                            org.jvnet.olt.minitm.AlignedSegment ali = (org.jvnet.olt.minitm.AlignedSegment)alignments[i];

                            if (ali != null) {
                                if (startSentence == 0) {
                                    String p = ali.getSource();
                                    int old = startPosition;

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.indexOf(s.what, startPosition);

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    } else {
                                        int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    }

                                    startPosition = 0;
                                    p = ali.getTranslation();

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.indexOf(s.what, startPosition);

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    } else {
                                        int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    }
                                } else if (startSentence == 1) {
                                    String p = ali.getTranslation();

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.indexOf(s.what, startPosition);

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    } else {
                                        int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    }
                }
            } else { //backward

                for (int i = startRow; i > -1; i--) {
                    org.jvnet.olt.minitm.AlignedSegment ali = (org.jvnet.olt.minitm.AlignedSegment)alignments[i];

                    if (ali != null) {
                        if (i != startRow) {
                            startSentence = 1;
                            startPosition = ali.getTranslation().length() - 1;
                        }

                        if (startSentence == 0) {
                            String p = ali.getSource();
                            int old = startPosition;

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            } else {
                                int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            }
                        } else if (startSentence == 1) {
                            String p = ali.getTranslation();

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            } else {
                                int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                if (index != -1) {
                                    return new Result(s, (3 * i) + 1, 1, index);
                                }
                            }

                            p = ali.getSource();
                            startPosition = p.length();

                            if (startPosition < 0) {
                                startPosition = 0;
                            }

                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }

                            if (s.caseFlag) {
                                int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            } else {
                                int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                if (index != -1) {
                                    return new Result(s, 3 * i, 0, index);
                                }
                            }
                        }
                    }
                }

                if ((startRow != (alignments.length - 1)) && (oldResult == null)) {
                    String[] options = { "Yes", "No" };

                    int r = JOptionPane.showOptionDialog(this, //AlignmentMain.frame,                             // the parent that the dialog blocks
                        "The editor has searched to the start of the file. \nDo you want to continue searching from the end of the file?", // the dialog message array
                        "Searching", // the title of the dialog window
                        JOptionPane.DEFAULT_OPTION, // option type
                        JOptionPane.INFORMATION_MESSAGE, // message type
                        null, // optional icon, use null to use the default icon
                        options, // options string array, will be made into buttons
                        options[0]);

                    switch (r) {
                    case 0: // yes

                        for (int i = alignments.length - 1; i > startRow; i--) {
                            org.jvnet.olt.minitm.AlignedSegment ali = (org.jvnet.olt.minitm.AlignedSegment)alignments[i];

                            if (ali != null) {
                                startSentence = 1;
                                startPosition = ali.getTranslation().length() - 1;

                                if (startSentence == 0) {
                                    String p = ali.getSource();
                                    int old = startPosition;

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    } else {
                                        int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    }
                                } else if (startSentence == 1) {
                                    String p = ali.getTranslation();

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    } else {
                                        int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                        if (index != -1) {
                                            return new Result(s, (3 * i) + 1, 1, index);
                                        }
                                    }

                                    p = ali.getSource();
                                    startPosition = p.length();

                                    if (startPosition < 0) {
                                        startPosition = 0;
                                    }

                                    if (startPosition > p.length()) {
                                        startPosition = p.length();
                                    }

                                    if (s.caseFlag) {
                                        int index = p.substring(0, startPosition).lastIndexOf(s.what);

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    } else {
                                        int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());

                                        if (index != -1) {
                                            return new Result(s, 3 * i, 0, index);
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    }
                }
            }
        }

        return null;
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

        if (oldResult != null) {
            MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[((oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT) + PivotTextRender1.ROW_COUNT]; //.testMain2.getTextPane(oldTargetResult.rowIndex,oldTargetResult.sentenceIndex);

            if (textPane != null) {
                textPane.select(oldResult.position, oldResult.position);
            }
        }
    }

    /**
     * Help
     */
    void helpButton_actionPerformed(ActionEvent e) {
    }

    void doReplaceResult(Result result, Search s, boolean isSrc, boolean doUnselect, boolean replaceAll) {
        if (result == null) {
            if (doUnselect && !replaceAll) {
                if (oldResult != null) {
                    if (oldResult.sentenceIndex == 0) {
                        MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[(oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(TMInnerPanel.oldSrcResult.rowIndex,TMInnerPanel.oldSrcResult.sentenceIndex);

                        if (textPane != null) {
                            textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                        }
                    } else {
                        MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[((oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT) + PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(TMInnerPanel.oldSrcResult.rowIndex,TMInnerPanel.oldSrcResult.sentenceIndex);

                        if (textPane != null) {
                            textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                        }
                    }
                }
            }

            oldResult = null;

            Toolkit.getDefaultToolkit().beep();

            //Object[]    message = new Object[1];
            String informationString = "Can not find " + s.what + ((isSrc) ? " in source." : " in target.");

            /*textArea.setSize(200,50);
            textArea.setText(informationString);
            scr.setSize(new Dimension(textArea.getPreferredSize()));
            message[0] = scr;*/
            String message = informationString;

            String[] options = { "Ok" };
            int r = JOptionPane.showOptionDialog(this, // the parent that the dialog blocks
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
            if (doUnselect && !replaceAll) {
                if (oldResult != null) {
                    if (oldResult.sentenceIndex == 0) {
                        MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[(oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(TMInnerPanel.oldSrcResult.rowIndex,TMInnerPanel.oldSrcResult.sentenceIndex);

                        if (textPane != null) {
                            textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                        }
                    } else {
                        MiniTMPivotTextPane textPane = MiniTMAlignmentMain.editPanes[((oldResult.rowIndex / 3) % PivotTextRender1.ROW_COUNT) + PivotTextRender1.ROW_COUNT]; //testMain1.getTextPane(TMInnerPanel.oldSrcResult.rowIndex,TMInnerPanel.oldSrcResult.sentenceIndex);

                        if (textPane != null) {
                            textPane.unselect(oldResult.position, oldResult.position + oldResult.search.what.length());
                        }
                    }
                }
            }

            if (!replaceAll) {
                MiniTMAlignmentMain.testMain.navigateTo(result.rowIndex);
                MiniTMAlignmentMain.testMain.tableView.repaint(MiniTMAlignmentMain.testMain.tableView.getCellRect(result.rowIndex, 1, false));
            }

            oldResult = (Result)result.clone();
        }
    }

    /**
     * util function
     */
    boolean canReplace(Result r, boolean isSource, boolean tagProtection) {
        String temp = null;
        PivotText p = null;

        if (r.sentenceIndex == 0) {
            temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex / 3]).getSource();
        } else {
            temp = ((org.jvnet.olt.minitm.AlignedSegment)MiniTMAlignmentMain.data[r.rowIndex / 3]).getTranslation();
        }

        p = new PivotText(temp);

        boolean flag = p.canReplace(r.position, r.position + r.search.what.length(), tagProtection);

        temp = null;
        p = null;

        return flag;
    }
}
