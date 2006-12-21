/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;
import org.jvnet.olt.editor.util.Bundle;

import java.util.Vector;
import java.util.regex.Matcher;

import javax.swing.*;
import javax.swing.text.*;

import org.jvnet.olt.editor.model.*;


public class TMInnerPanel extends JPanel implements KeyListener, FocusListener {
    private static Bundle bundle = Bundle.getBundle(TMInnerPanel.class.getName());
    public static Result srcResult = null;
    public static Result oldSrcResult = null;
    public static Result targetResult = null;
    public static Result oldTargetResult = null;
    
    //bug 4763116
    public static int initSearchRow = -1;
    int iLocation;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jToolBarPanel = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JCheckBox jCheckBox1 = new JCheckBox();
    JCheckBox jCheckBox2 = new JCheckBox();
    BorderLayout borderLayout3 = new BorderLayout();
    JLabel jLabel1 = new JLabel();
    JButton jButton1 = new JButton();
    JTextField jTextField1 = new JTextField();
    private JTextPane textArea = new JTextPane();
    private JScrollPane scr = new JScrollPane(textArea);
    private Backend backend;
    
    public TMInnerPanel(int iLocation, Backend backend) {
        super();
        this.iLocation = iLocation;
        
        if (iLocation == 0) {
            AlignmentMain.testMain1 = new AlignmentMain(0);
        } else {
            AlignmentMain.testMain2 = new AlignmentMain(1);
        }
        
        this.backend = backend;
        
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        textArea.setSize(200, 50);
        textArea.setBackground(new Color(204, 204, 204));
        textArea.setEditable(false);
        textArea.setFont(MainFrame.dlgFont);
        scr.setBorder(null);
        
        this.setLayout(borderLayout1);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setMinimumSize(new Dimension(200, 35));
        this.setPreferredSize(new Dimension(200, 35));
        jToolBarPanel.setBorder(BorderFactory.createEtchedBorder());
        jToolBarPanel.setMinimumSize(new Dimension(267, 20));
        jToolBarPanel.setPreferredSize(new Dimension(327, 30));
        jToolBarPanel.setLayout(borderLayout2);
        jPanel2.setLayout(gridLayout1);
        gridLayout1.setColumns(2);
        jCheckBox1.setText(bundle.getString("Match_Case"));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCheckBox1_actionPerformed(e);
                }
            });
        jCheckBox2.setText(bundle.getString("Down"));
        jCheckBox2.setSelected(true);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jCheckBox2_actionPerformed(e);
            }
        });
        jPanel1.setLayout(borderLayout3);
        
        jButton1.setMaximumSize(new Dimension(30, 27));
        jButton1.setMinimumSize(new Dimension(25, 25));
        jButton1.setPreferredSize(new Dimension(25, 25));
        jButton1.setToolTipText(bundle.getString("Search"));
        jButton1.setIcon(MainFrame.ip.imageFind);
        jButton1.addActionListener(new TMInnerPanel_jButton1_actionAdapter(this));
        borderLayout1.setHgap(2);
        borderLayout1.setVgap(2);
        this.add(jToolBarPanel, BorderLayout.NORTH);
        jToolBarPanel.add(jPanel1, BorderLayout.CENTER);
        
        jPanel1.add(jLabel1, BorderLayout.WEST);
        jPanel1.add(jButton1, BorderLayout.EAST);
        
        JPanel p = new JPanel() {
            public Insets getInsets() {
                return new Insets(2, 2, 2, 2);
            }
        };
        
        p.setLayout(new BorderLayout());
        jTextField1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1.doClick();
                jTextField1.requestFocus();
            }
        });
        p.add(jTextField1);
        jPanel1.add(p, BorderLayout.CENTER);
        jToolBarPanel.add(jPanel2, BorderLayout.EAST);
        jPanel2.add(jCheckBox1, null);
        jPanel2.add(jCheckBox2, null);
        
        if (iLocation == 0) {
            this.add(AlignmentMain.testMain1, BorderLayout.CENTER);
        } else {
            this.add(AlignmentMain.testMain2, BorderLayout.CENTER);
        }
        
        this.setVisible(true);
        ((JTextField)jTextField1).addKeyListener(this);
        ((JTextField)jTextField1).addFocusListener(this);
    }
    
    public Insets getInsets() {
        return new Insets(2, 2, 2, 2);
    }
    
    public void setNationFlag(String strNationFlagGif) {
        jLabel1.setIcon(new ImageIcon(getClass().getResource(strNationFlagGif)));
    }
    
    public void setFlagTips(String strNation) {
        if (iLocation == 0) {
            jLabel1.setToolTipText(bundle.getString("Source_Language:_") + strNation);
        } else {
            jLabel1.setToolTipText(bundle.getString("Target_Language:_") + strNation);
        }
    }
    
    /**
     * construct a search object
     */
    Search checkSearch() {
        String searchString = jTextField1.getText();
        
        if ((searchString == null) || searchString.equals("")) {
            return null;
        }
        
        boolean caseFlag = jCheckBox1.isSelected();
        boolean forwardFlag = jCheckBox2.isSelected();
        
        return new Search(searchString, caseFlag, forwardFlag);
    }
    
    /**
     * start to search the string
     */
    void jButton1_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();
        
        //TODO Just a question: What for God's sake do we search for when there's no file OPEN ???
        if (!backend.hasCurrentFile()) {
            return;
        }
        
        Search s = checkSearch();
        
        if (s == null) {
            String informationString = "";
            Toolkit.getDefaultToolkit().beep();
            
            if (iLocation == 0) {
                informationString = new String(bundle.getString("Find_what_in_source?"));
            } else {
                informationString = new String(bundle.getString("Find_what_in_target?"));
            }
            
            String message = informationString;

            String[] options = { bundle.getString("Ok") };

            int r = JOptionPane.showOptionDialog(getRootPane(), // the parent that the dialog blocks
                message, // the dialog message array
                bundle.getString("Warning"), // the title of the dialog window
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
        
        //bug 4763116++
        if (iLocation == 0) {
            if (initSearchRow == -1) {
                initSearchRow = AlignmentMain.testMain1.tableView.getSelectedRow();
                oldSrcResult = null;
            }
            
            Result r = getSearchResult(s, true, false, "");
            doSearchResult(r, s, true);
        } else {
            if (initSearchRow == -1) {
                initSearchRow = AlignmentMain.testMain2.tableView.getSelectedRow();
                oldTargetResult = null;
            }
            
            Result r = getSearchResult(s, false, false, "");
            doSearchResult(r, s, false);
        }
    }
    
    /**
     * From a curtain position,try to search the next occurrence of a string.
     * This method will be reused by FindAndReplace class.
     */
    static Result getSearchResult(Search s, boolean isSource, boolean replace, String replaceStr) {
        if (isSource) {
            if (oldSrcResult == null) {
                int startRowIndex = AlignmentMain.testMain1.tableView.getSelectedRow();
                int startPosition = getCurrentPosition(s, true);
                int startSentenceIndex = getCurrentSentenceIndex(s, true);
                
                if (startRowIndex == -1) {
                    startRowIndex = 0;
                }
                
                //initSearchRow  = startRowIndex;
                srcResult = search(true, startRowIndex, startSentenceIndex, startPosition, s);
            } else { //based on previous result
                
                if (oldSrcResult.rowIndex == AlignmentMain.testMain1.tableView.getSelectedRow()) {
                    int startRowIndex = oldSrcResult.rowIndex;
                    int startPosition = -1;
                    int startSentenceIndex = oldSrcResult.sentenceIndex;
                    
                    if (oldSrcResult.search.equals(s)) {
                        if (s.forwardFlag) {
                            if (replace) {
                                startPosition = oldSrcResult.position + replaceStr.length();
                            } else {
                                startPosition = oldSrcResult.position + oldSrcResult.search.what.length();
                            }
                        } else {
                            startPosition = oldSrcResult.position - 1;
                        }
                    } else {
                        startPosition = getCurrentPosition(s, true);
                    }
                    
                    srcResult = search(true, startRowIndex, startSentenceIndex, startPosition, s);
                } else {
                    int startRowIndex = AlignmentMain.testMain1.tableView.getSelectedRow();
                    startRowIndex = (startRowIndex == -1) ? 0 : startRowIndex;
                    
                    int startPosition = getCurrentPosition(s, true);
                    int startSentenceIndex = getCurrentSentenceIndex(s, true);
                    
                    if (startRowIndex == -1) {
                        startRowIndex = 0;
                    }
                    
                    //initSearchRow  = startRowIndex;
                    srcResult = search(true, startRowIndex, startSentenceIndex, startPosition, s);
                }
            }
            
            return srcResult;
        } else { //is Target
            
            if (oldTargetResult == null) {
                int startRowIndex = AlignmentMain.testMain2.tableView.getSelectedRow();
                int startPosition = getCurrentPosition(s, false);
                int startSentenceIndex = getCurrentSentenceIndex(s, false);
                
                if (startRowIndex == -1) {
                    startRowIndex = 0;
                }
                
                //initSearchRow  = startRowIndex;
                targetResult = search(false, startRowIndex, startSentenceIndex, startPosition, s);
            } else { //based on previous result
                
                if (oldTargetResult.rowIndex == AlignmentMain.testMain2.tableView.getSelectedRow()) {
                    int startRowIndex = oldTargetResult.rowIndex;
                    int startPosition = -1;
                    int startSentenceIndex = oldTargetResult.sentenceIndex;
                    
                    if (oldTargetResult.search.equals(s)) {
                        if (s.forwardFlag) {
                            if (replace) {
                                startPosition = oldTargetResult.position + replaceStr.length();
                            } else {
                                startPosition = oldTargetResult.position + oldTargetResult.search.what.length();
                            }
                        } else {
                            startPosition = oldTargetResult.position - 1;
                        }
                    } else {
                        startPosition = getCurrentPosition(s, false);
                    }
                    
                    targetResult = search(false, startRowIndex, startSentenceIndex, startPosition, s);
                } else {
                    int startRowIndex = AlignmentMain.testMain2.tableView.getSelectedRow();
                    startRowIndex = (startRowIndex == -1) ? 0 : startRowIndex;
                    
                    int startPosition = getCurrentPosition(s, false);
                    int startSentenceIndex = getCurrentSentenceIndex(s, false);
                    
                    if (startRowIndex == -1) {
                        startRowIndex = 0;
                    }
                    
                    //initSearchRow  = startRowIndex;
                    targetResult = search(false, startRowIndex, startSentenceIndex, startPosition, s);
                }
            }
            
            return targetResult;
        }
    }
    
    /**
     * search all of the occurrence of a string in the target content and return a set.
     * This method will be reused by FindAndReplace class.
     */
    static Vector getSearchAllResult(Search s, boolean isSource) {
        Vector v = new Vector();
        
        if (isSource) {
            oldSrcResult = null;
            srcResult = null;
            
            do {
                if (oldSrcResult == null) {
                    int startRowIndex = AlignmentMain.testMain1.tableView.getSelectedRow();
                    int startPosition = getCurrentPosition(s, true);
                    int startSentenceIndex = getCurrentSentenceIndex(s, true);
                    
                    if (startRowIndex == -1) {
                        startRowIndex = 0;
                    }
                    
                    srcResult = search(true, startRowIndex, startSentenceIndex, startPosition, s);
                } else { //based on previous result
                    
                    int startRowIndex = oldSrcResult.rowIndex;
                    int startPosition = -1;
                    int startSentenceIndex = 0;
                    
                    startPosition = oldSrcResult.position + oldSrcResult.search.what.length();
                    srcResult = search(true, startRowIndex, startSentenceIndex, startPosition, s);
                }
                
                if (srcResult != null) {
                    v.addElement(srcResult);
                    oldSrcResult = (Result)srcResult.clone();
                }
            } while (srcResult != null);
            
            oldSrcResult = null;
        } else { //is Target
            oldTargetResult = null;
            targetResult = null;
            
            do {
                if (oldTargetResult == null) {
                    int startRowIndex = AlignmentMain.testMain2.tableView.getSelectedRow();
                    ;
                    
                    int startPosition = getCurrentPosition(s, false);
                    ;
                    
                    int startSentenceIndex = getCurrentSentenceIndex(s, false);
                    
                    if (startRowIndex == -1) {
                        startRowIndex = 0;
                    }
                    
                    targetResult = search(false, startRowIndex, startSentenceIndex, startPosition, s);
                } else { //based on previous result
                    
                    int startRowIndex = oldTargetResult.rowIndex;
                    int startPosition = -1;
                    int startSentenceIndex = 0;
                    
                    startPosition = oldTargetResult.position + oldTargetResult.search.what.length();
                    targetResult = search(false, startRowIndex, startSentenceIndex, startPosition, s);
                }
                
                if (targetResult != null) {
                    v.addElement(targetResult);
                    oldTargetResult = (Result)targetResult.clone();
                }
            } while (targetResult != null);
            
            oldTargetResult = null;
        }
        
        return v;
    }
    
    /**
     * After doing a search for a string in the backgroud,
     * try to highlight it in the foreground.
     */
    static void doSearchResult(Result result, Search s, boolean isSrc) {
        if (result == null) {
            boolean noOne = true;
            
            if (isSrc) {
                if (oldSrcResult != null) {
                    noOne = false;
                }
            } else {
                if (oldTargetResult != null) {
                    noOne = false;
                }
            }
            
            Toolkit.getDefaultToolkit().beep();

            String informationString = bundle.getString("Finished_searching_the_file.");

            if (noOne) {
                if (isSrc)
                    informationString = bundle.getString("The_search_item_was_not_found_Source");
                else
                    informationString = bundle.getString("The_search_item_was_not_found_Target");
            }
            
            String message = informationString;

            String[] options = { bundle.getString("Ok") };

            int r = JOptionPane.showOptionDialog(AlignmentMain.testMain1.getRootPane(), //AlignmentMain.frame,                             // the parent that the dialog blocks
                message, // the dialog message array
                bundle.getString("Searching"), // the title of the dialog window
                JOptionPane.DEFAULT_OPTION, // option type
                JOptionPane.INFORMATION_MESSAGE, // message type
                null, // optional icon, use null to use the default icon
                options, // options string array, will be made into buttons
                options[0]);

            switch (r) {
                case 0: // ok
                    initSearchRow = -1;
                    
                    break;
            }
        } else {
            if (isSrc) {
                oldSrcResult = (Result)result.clone();
                
                // make selection - bug 4728266
                AlignmentMain.testMain1.startEditing();
                AlignmentMain.testMain1.navigateTo(result.rowIndex);
                
                PivotTextPane textPane = AlignmentMain.getSrcEditPane(result.rowIndex);
                
                if (textPane != null) {
                    textPane.select2(result.position, result.position + result.search.what.length());
                    textPane.select(result.position, result.position + result.search.what.length());
                }
            } else {
                oldTargetResult = (Result)result.clone();
                
                // make selection - bug 4728266
                AlignmentMain.testMain2.startEditing();
                AlignmentMain.testMain2.navigateTo(result.rowIndex);
                
                PivotTextPane textPane = AlignmentMain.getTargetEditPane(result.rowIndex);
                
                if (textPane != null) {
                    textPane.select2(result.position, result.position + result.search.what.length());
                    textPane.select(result.position, result.position + result.search.what.length());
                }
            }
        }
    }
    
    /**
     * a base search function,which will try to find a result based on a curtain position.
     */
    private static Result search(boolean isSrc, int startRow, int startSentence, int startPosition, Search s) {
        Result result = null;
        
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();
        
        TMData.TMSentence[] alignments = tmpdata.tmsentences;
        
        if (alignments == null) {
            return null;
        } else {
            if (s.forwardFlag) { //forward
                
                if (initSearchRow <= startRow) {
                    for (int i = startRow; i < alignments.length; i++) {
                        if (i != startRow) {
                            startSentence = 0;
                            startPosition = 0;
                        }
                        
                        TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                        
                        if (ali != null) {
                            String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                            
                            if (startPosition < 0) {
                                startPosition = 0;
                            }
                            
                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }
                            
                            if(s.matchWords){
                                Matcher m = s.getMatcher(p);
                                if(m.find(startPosition)){
                                    result = new Result(s, i, 0, m.start());
                                    
                                    break;
                                }
                            } else{
                                if (s.caseFlag) {
                                    int index = p.indexOf(s.what, startPosition);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                } else{
                                    int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                }
                            }
                            
                        }
                    }
                    
                    if (result != null) {
                        return result;
                    }
                    
                    //System.out.println("==============================");
                    //System.out.println("search.startRow = "+ startRow);
                    //System.out.println("search.oldResult = "+ (oldResult == null));
                    //if(startRow != 0 && oldResult == null) {
                    boolean resume = false;
                    
                    if ((initSearchRow != 0) && (result == null)) {
                        String[] options = { bundle.getString("Yes"), bundle.getString("No") };

                        Toolkit.getDefaultToolkit().beep();
                        
                        int r = JOptionPane.showOptionDialog(AlignmentMain.testMain1.getRootPane(), //AlignmentMain.frame,                             // the parent that the dialog blocks
                            bundle.getString("The_editor_has_finished_searching_to_the_end_of_the_file._Do_you_want_to_continue_searching_from_the_top_of_the_file?"), // the dialog message array
                            bundle.getString("Searching"), // the title of the dialog window
                            JOptionPane.DEFAULT_OPTION, // option type
                            JOptionPane.INFORMATION_MESSAGE, // message type
                            null, // optional icon, use null to use the default icon
                            options, // options string array, will be made into buttons
                            options[0]);

                        switch (r) {
                            case 0: // yes
                                resume = true;
                                
                                break;
                                
                            case 1: //no
                                break;
                        }
                    }
                    
                    if (resume) {
                        for (int i = 0; i < initSearchRow; i++) {
                            //              System.out.println("i="+i);
                            startSentence = 0;
                            startPosition = 0;
                            
                            TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                            
                            if (ali != null) {
                                String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                                
                                if (startPosition < 0) {
                                    startPosition = 0;
                                }
                                
                                if (startPosition > p.length()) {
                                    startPosition = p.length();
                                }
                                if(s.matchWords){
                                    Matcher m = s.getMatcher(p);
                                    if(m.find(startPosition)){
                                        result = new Result(s, i, 0, m.start());
                                        
                                        break;
                                    }
                                } else{
                                    if (s.caseFlag) {
                                        int index = p.indexOf(s.what, startPosition);
                                        
                                        if (index != -1) {
                                            result = new Result(s, i, 0, index);
                                            
                                            break;
                                        }
                                    } else {
                                        int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);
                                        
                                        if (index != -1) {
                                            result = new Result(s, i, 0, index);
                                            
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = startRow; i < initSearchRow; i++) {
                        if (i != startRow) {
                            startSentence = 0;
                            startPosition = 0;
                        }
                        
                        TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                        
                        if (ali != null) {
                            String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                            
                            if (startPosition < 0) {
                                startPosition = 0;
                            }
                            
                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }
                            
                            if(s.matchWords){
                                Matcher m = s.getMatcher(p);
                                if(m.find(startPosition)){
                                    result = new Result(s, i, 0, m.start());
                                    
                                    break;
                                }
                            } else{
                                if (s.caseFlag) {
                                    int index = p.indexOf(s.what, startPosition);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                } else {
                                    int index = p.toLowerCase().indexOf(s.what.toLowerCase(), startPosition);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                
                //return result;
            } else { //backward
                
                //System.out.println("==============================");
                //System.out.println("initSearchRow = "+ initSearchRow);
                //System.out.println("search.startRow = "+ startRow);
                if (initSearchRow >= startRow) {
                    for (int i = startRow; i > -1; i--) {
                        TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                        
                        if (ali != null) {
                            if (i != startRow) {
                                startSentence = 0;
                                startPosition = ((isSrc) ? ali.getSource() : ali.getTranslation()).length();
                            }
                            
                            String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                            
                            if (startPosition < 0) {
                                startPosition = 0;
                            }
                            
                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }
                            
                            if(s.matchWords){
                                String x = p.substring(0, startPosition);
                                
                                int lastStart = -1;
                                do{
                                    Matcher m = s.getMatcher(x);
                                    if(!m.find())
                                        break;
                                    
                                    if(lastStart == -1)
                                        lastStart = m.start();
                                    else
                                        lastStart += m.start();
                                    
                                    x = x.substring(m.end());
                                }
                                while(true);
                                
                                if(lastStart != -1)
                                    result = new Result(s, i, 0, lastStart);
                            } else{
                                if (s.caseFlag) {
                                    int index = p.substring(0, startPosition).lastIndexOf(s.what);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                } else {
                                    int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (result != null) {
                        return result;
                    }
                    
                    boolean resume = false;
                    
                    if ((initSearchRow != (alignments.length - 1)) && (result == null)) {
                        String[] options = { bundle.getString("Yes"), bundle.getString("No") };

                        Toolkit.getDefaultToolkit().beep();
                        
                        int r = JOptionPane.showOptionDialog(AlignmentMain.testMain1.getRootPane(), //AlignmentMain.frame,                             // the parent that the dialog blocks
                            bundle.getString("The_editor_has_finished_searching_to_the_top_of_the_file._Do_you_want_to_continue_searching_from_the_end_of_the_file?"), // the dialog message array
                            bundle.getString("Searching"), // the title of the dialog window
                            JOptionPane.DEFAULT_OPTION, // option type
                            JOptionPane.INFORMATION_MESSAGE, // message type
                            null, // optional icon, use null to use the default icon
                            options, // options string array, will be made into buttons
                            options[0]);

                        switch (r) {
                            case 0: // yes
                                resume = true;
                                
                                break;
                                
                            case 1: //no
                                break;
                        }
                    }
                    
                    if (resume) {
                        for (int i = alignments.length - 1; i > initSearchRow; i--) {
                            TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                            startSentence = 0;
                            startPosition = ((isSrc) ? ali.getSource() : ali.getTranslation()).length();
                            
                            if (ali != null) {
                                String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                                
                                if (startPosition < 0) {
                                    startPosition = 0;
                                }
                                
                                if (startPosition > p.length()) {
                                    startPosition = p.length();
                                }
                                
                                if(s.matchWords){
                                    String x = p.substring(0, startPosition);
                                    
                                    int lastStart = -1;
                                    do{
                                        Matcher m = s.getMatcher(x);
                                        if(!m.find())
                                            break;
                                        
                                        if(lastStart == -1)
                                            lastStart = m.start();
                                        else
                                            lastStart += m.start();
                                        
                                        x = x.substring(m.end());
                                    }
                                    while(true);
                                    
                                    if(lastStart != -1)
                                        result = new Result(s, i, 0, lastStart);
                                } else{
                                    if (s.caseFlag) {
                                        int index = p.substring(0, startPosition).lastIndexOf(s.what);
                                        
                                        if (index != -1) {
                                            result = new Result(s, i, 0, index);
                                            
                                            break;
                                        }
                                    } else {
                                        int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());
                                        
                                        if (index != -1) {
                                            result = new Result(s, i, 0, index);
                                            
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = startRow; i > initSearchRow; i--) {
                        TMData.TMSentence ali = (TMData.TMSentence)alignments[i];
                        
                        if (i != startRow) {
                            startSentence = 0;
                            startPosition = ((isSrc) ? ali.getSource() : ali.getTranslation()).length();
                        }
                        
                        if (ali != null) {
                            String p = (isSrc) ? ali.getSource() : ali.getTranslation();
                            
                            if (startPosition < 0) {
                                startPosition = 0;
                            }
                            
                            if (startPosition > p.length()) {
                                startPosition = p.length();
                            }
                            if(s.matchWords){
                                String x = p.substring(0, startPosition);
                                
                                int lastStart = -1;
                                do{
                                    Matcher m = s.getMatcher(x);
                                    if(!m.find())
                                        break;
                                    
                                    if(lastStart == -1)
                                        lastStart = m.start();
                                    else
                                        lastStart += m.start();
                                    
                                    x = x.substring(m.end());
                                }
                                while(true);
                                
                                if(lastStart != -1)
                                    result = new Result(s, i, 0, lastStart);
                            } else{
                                
                                if (s.caseFlag) {
                                    int index = p.substring(0, startPosition).lastIndexOf(s.what);
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                } else {
                                    int index = p.substring(0, startPosition).toLowerCase().lastIndexOf(s.what.toLowerCase());
                                    
                                    if (index != -1) {
                                        result = new Result(s, i, 0, index);
                                        
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            //return result;
        }
        
        return result;
    }
    
    public void focusLost(FocusEvent e) {
        //MainFrame.activeComponent = null;
    }
    
    public void focusGained(FocusEvent e) {
        MainFrame.activeComponent = ((JTextComponent)jTextField1);
    }
    
    static int getCurrentPosition(Search s, boolean isSrc) {
        return s.forwardFlag ? 0 : Integer.MAX_VALUE;
    }
    
    static int getCurrentSentenceIndex(Search s, boolean isSrc) {
        return 0;
    }
    
    void jCheckBox1_actionPerformed(ActionEvent e) {
    }
    
    void jCheckBox2_actionPerformed(ActionEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CUT) //||
            //(e.getKeyCode() == KeyEvent.VK_X && e.getModifiers() == KeyEvent.CTRL_MASK)) {
        {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            textComponent.cut();
            
            /*      String selected = textComponent.getSelectedText();
                  if(selected != null && selected.length() > 0 ) {
                    textComponent.replaceSelection("");
                    Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection = new StringSelection(selected);
                    syscb.setContents(selection,null);
                  }*/
        } else if (e.getKeyCode() == KeyEvent.VK_COPY) //||
            //                 (e.getKeyCode() == KeyEvent.VK_C && e.getModifiers() == KeyEvent.CTRL_MASK)) {
        {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            textComponent.copy();
            
            /*      String selected = textComponent.getSelectedText();
                  if(selected != null && selected.length() > 0 ) {
                    //textComponent.replaceSelection("");
                    Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection = new StringSelection(selected);
                    syscb.setContents(selection,null);
                  }*/
        } else if (e.getKeyCode() == KeyEvent.VK_PASTE) //||
            //(e.getKeyCode() == KeyEvent.VK_V && e.getModifiers() == KeyEvent.CTRL_MASK)) {
        {
            JTextComponent textComponent = (JTextComponent)(e.getSource());
            textComponent.paste();
            
            /*      Clipboard syscb = Toolkit.getDefaultToolkit().getSystemClipboard();
                  Transferable contents = syscb.getContents(this);
                  if(contents == null) return;
                  try {
                    String text = (String)(contents.getTransferData(DataFlavor.stringFlavor));
                    textComponent.replaceSelection(text);
                    //System.out.println("TMInnerPanel");
                  }catch(Exception exception) {
             
                  }*/
        }
    }
}


class TMInnerPanel_jButton1_actionAdapter implements java.awt.event.ActionListener {
    TMInnerPanel adaptee;
    
    TMInnerPanel_jButton1_actionAdapter(TMInnerPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}
