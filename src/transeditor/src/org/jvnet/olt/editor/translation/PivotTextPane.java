/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;

import org.jvnet.olt.editor.model.*;


/**
 * The base component for rendering a text as the Pivot format.
 */
public class PivotTextPane extends JTextPane implements DocumentListener, KeyListener, MouseListener, FocusListener {
    private static final Logger logger = Logger.getLogger(PivotTextPane.class.getName());
    public final static String IN_VISIBLE_CHAR = "";
    public final static String BACKSPACE = "BACKSPACE";
    public final static String CUT = "CUT";
    public final static String COPY = "COPY";
    public final static String DELETE = "DELETE";
    public final static String EDIT = "EDIT";
    public final static String PASTE = "PASTE";

    /**
     * The tokens for seperating the word when doing the spellcheck.
     */
    public static String delim = "0123456789 ,.;:?!-_()<>/\\|]}{[+-*&^%$#@~`'\"\r\n";
    public static Hashtable wordHashes = new Hashtable();
    public static Vector ignoreWords = new Vector();
    public static int wordInRowIndex = -1;
    public static int wordStart = 0;
    public static int wordEnd = 0;
    static StyledEditorKit sek;
    static StyleContext sc;
    public static Style normalStyle;
    public static Style tagStyle;
    public static Style selectStyle;
    public static Style selectStyle2;
    public static Style unselectStyle;
    public static Style underlineStyle;
    public static Style insertionStyle;
    public static Style deletionStyle;
    public static Style replaceStyle;
    public static Style changePositionStyle;
    public static Style whiteSpaceInsertionStyle;
    public static Style whiteSpaceDeletionStyle;
    public static Style whiteSpaceReplaceStyle;
    public static Style whiteSpaceChangePositionStyle;
    public static JPopupMenu popEditMenu = null;
    public static JMenuItem transMenu = null;
    public static JMenuItem approveMenu = null;
    public static JMenuItem rejectMenu = null;
    public static JMenuItem untransMenu = null;
    public static JMenuItem confirmTransMenu = null;
    public static JMenuItem confirmReviewMenu = null;
    public static JMenuItem confirmRejectMenu = null;

    // bug 4744603
    //--------------------------------------
    private static Keymap thisKeymap = null;

    /**
     * Init Styles
     */
    static {
        sc = new StyleContext();

        Style def = sc.getStyle(StyleContext.DEFAULT_STYLE);

        //StyleConstants.setFontSize(def,14);
        //StyleConstants.setFontFamily(def,"Dialog");
        normalStyle = sc.addStyle("normal", def);
        StyleConstants.setForeground(normalStyle, new Color(0, 0, 0));

        tagStyle = sc.addStyle("tag", def);
        StyleConstants.setForeground(tagStyle, new Color(255, 0, 0));

        selectStyle = sc.addStyle("select", def);
        StyleConstants.setBackground(selectStyle, Color.lightGray);

        // selection for search
        selectStyle2 = sc.addStyle("select2", def);
        StyleConstants.setForeground(selectStyle2, Color.blue);

        unselectStyle = sc.addStyle("unselect", def);
        StyleConstants.setBackground(unselectStyle, Color.lightGray);

        underlineStyle = sc.addStyle("underline", def);
        StyleConstants.setUnderline(underlineStyle, true);
        StyleConstants.setForeground(underlineStyle, Color.red);

        insertionStyle = sc.addStyle("insert", def);
        StyleConstants.setForeground(insertionStyle, Color.blue);

        deletionStyle = sc.addStyle("delete", def);
        StyleConstants.setForeground(deletionStyle, Color.gray);

        replaceStyle = sc.addStyle("replace", def);
        StyleConstants.setForeground(replaceStyle, Color.white);

        changePositionStyle = sc.addStyle("change", def);
        StyleConstants.setForeground(changePositionStyle, new Color(255, 100, 200));

        whiteSpaceInsertionStyle = sc.addStyle("whitespaceinsertion", def);
        StyleConstants.setBackground(whiteSpaceInsertionStyle, Color.blue);

        whiteSpaceDeletionStyle = sc.addStyle("whitespacedeletion", def);
        StyleConstants.setBackground(whiteSpaceDeletionStyle, Color.gray);

        whiteSpaceReplaceStyle = sc.addStyle("whitespacereplace", def);
        StyleConstants.setBackground(whiteSpaceReplaceStyle, Color.white);

        whiteSpaceChangePositionStyle = sc.addStyle("whitespacechange", def);
        StyleConstants.setBackground(whiteSpaceChangePositionStyle, Color.white);

        sek = new StyledEditorKit();

        Action[] actions = sek.getActions();

        for (int i = 0; i < actions.length; i++) {
            if (actions[i].toString().toLowerCase().indexOf("copyaction") != -1) {
                actions[i].setEnabled(false);
            }

            if (actions[i].toString().toLowerCase().indexOf("cutaction") != -1) {
                actions[i].setEnabled(false);
            }

            if (actions[i].toString().toLowerCase().indexOf("pasteaction") != -1) {
                actions[i].setEnabled(false);
            }

            if (actions[i].toString().toLowerCase().indexOf("beginaction") != -1) {
                actions[i].setEnabled(false);
            }

            if (actions[i].toString().toLowerCase().indexOf("endaction") != -1) {
                actions[i].setEnabled(false);
            }
        }
    }

    /**
     * edit popup Menu
     */
    JPopupMenu pop = new JPopupMenu();

    //--------------------------------------

    /**
     * Variables Control Insert/Backspace/Delete
     */
    private boolean newInsert = true;
    private boolean insertable = true;
    private boolean forwardDeletable = true;
    private boolean backwardDeletable = true;
    private String curCharString = "";

    /**
     * Variables for current TextPane
     */
    private DefaultStyledDocument content;
    private String value = "";
    private PivotBaseElement[] elements;
    private JTable table;
    private int rowLocal;
    private int sentenceIndex;
    private boolean isSource;
    private int width = 0;
    private int height = 0;

    /**
     * Other Variables
     */
    private int selectIdStart;
    private int selectIdEnd;
    private boolean initInsert = true;
    private boolean needRefresh = false;

    /**
     * Variables for Undo/Redo
     */
    private int oldttype;
    private int newttype;
    private String oldString;
    private String newString;
    private String operation = "";

    /**
     *  Variables just to flag when will trigger the document listener
     */
    private boolean duringSetcontent = false;
    private boolean contentModified = false;

    //define the Control+PageDown action
    class ActionCtrlPageDown extends TextAction {
        public ActionCtrlPageDown(String str) {
            super(str);
        }

        public void actionPerformed(ActionEvent e) {
            if (MainFrame.curTable == AlignmentMain.testMain1.tableView) {
                AlignmentMain.testMain1.pageDown();
            } else if (MainFrame.curTable == AlignmentMain.testMain2.tableView) {
                AlignmentMain.testMain2.pageDown();
            }
        }
    }

    //define the Control+PageUp action
    class ActionCtrlPageUp extends TextAction {
        public ActionCtrlPageUp(String str) {
            super(str);
        }

        public void actionPerformed(ActionEvent e) {
            if (MainFrame.curTable == AlignmentMain.testMain1.tableView) {
                AlignmentMain.testMain1.pageUp();
            } else if (MainFrame.curTable == AlignmentMain.testMain2.tableView) {
                AlignmentMain.testMain2.pageUp();
            }
        }
    }

    class PopupMenuListener extends JMenuItem implements ActionListener {
        PivotTextPane parent;
        String title;
        int wordStart;
        int len;

        PopupMenuListener(PivotTextPane parent, String title, int p, int l) {
            super(title);
            this.parent = parent;
            this.title = title;
            wordStart = p;
            len = l;

            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int iSrcTableSel = AlignmentMain.testMain1.tableView.getSelectedRow();

            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            if ((iSrcTableSel == -1)) {
                return;
            }

            if ((tmpdata).tmsentences[iSrcTableSel].getTranslationStatus() == TMData.TMSentence.VERIFIED) {
                return;
            }

            String old = parent.getText();
            String newStr = old.substring(0, wordStart) + title + old.substring(wordStart + len);

            parent.setContent(newStr, org.jvnet.olt.editor.util.BaseElements.extractContent(newStr));

            //logger.finer("PopupMenuListner.actionPreformed start...");
            parent.updateTMType(TMData.TMSentence.USER_TRANSLATION);

            //logger.finer("PopupMenuListner.actionPreformed Finish...");
        }
    }

    //org.jvnet.olt.editor.translation.TableView oldSrcTableView = null;
    //org.jvnet.olt.editor.translation.TableView newSrcTableView = null;
    //org.jvnet.olt.editor.translation.TableView oldTargetTableView = null;
    //org.jvnet.olt.editor.translation.TableView newTargetTableView = null;
    //TODO This is never used !!!    
    private PivotTextPane() {
        super();

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PivotTextPane(JTable table, int row, int sentenceIndex, boolean isSrc) {
        super();
        this.table = table;
        this.rowLocal = row;
        this.sentenceIndex = sentenceIndex;
        this.isSource = isSrc;

        content = new DefaultStyledDocument(sc);
        this.setEditorKit(sek);
        this.setSelectedTextColor(Color.blue);
        this.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        setStyledDocument(content);
        elements = new PivotBaseElement[0];
        selectIdStart = -1;
        selectIdEnd = -1;
        setEditable(false);

        //bug 4744603
        //---------------------------------------------------------
        thisKeymap = this.getKeymap();
        resetPivotTextPaneKeymp();

        //---------------------------------------------------------
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addFocusListener(this);
        this.content.addDocumentListener(this);
    }

    public void sizePivotTextPane(int iWidth, int iRowHeight) {
        //  Size it correctly
        this.setWidth(iWidth);

        int curHight = this.getPreferredSize().height;

        int iHeight = this.getHeight();

        if (iHeight != iRowHeight) {
            this.setHeight(iRowHeight);
        }
    }

    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public void resetPivotTextPaneKeymp() {
        Keymap map = JTextComponent.addKeymap("myMap", thisKeymap);

        //KeyStroke ks1 = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,InputEvent.CTRL_MASK);
        KeyStroke ks1 = ShortcutsBuilder.getMenuItemShortcut("Navigation", "Page Down");
        Action pagedown = new ActionCtrlPageDown("pagedown");
        map.addActionForKeyStroke(ks1, pagedown);

        //KeyStroke ks2 = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,InputEvent.CTRL_MASK);
        KeyStroke ks2 = ShortcutsBuilder.getMenuItemShortcut("Navigation", "Page Up");
        Action pageup = new ActionCtrlPageUp("pageup");
        map.addActionForKeyStroke(ks2, pageup);

        setKeymap(map);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * mouseListener
     */
    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        // bug 4714740
        setMainFrameClipEnable();

        if (((e.getButton() == e.BUTTON2) || (e.getButton() == e.BUTTON3))) {
            //      logger.finer("show popup in mousereleased...");
            select();
            showPopup(e);
            repaint();

            return;
        } else {
            select();
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (((e.getButton() == e.BUTTON2) || (e.getButton() == e.BUTTON3))) {
            //logger.finer("show popup in mousepressed...");
            newInsert();
            showPopup(e);

            return;
        } else {
        }
    }

    public void mouseClicked(MouseEvent e) {
        // bug 4714740
        if ((e.getButton() == e.BUTTON2) || (e.getButton() == e.BUTTON3)) {
            //logger.finer("show popup in mouseclicked...");
            select();
            showPopup(e);

            return;
        }

        if (e.getClickCount() == 2) {
            setMainFrameClipEnable();
        }

        select();
    }

    /**
     * DocumentListener listener
     */
    public void changedUpdate(DocumentEvent e) {
    }

    public void removeUpdate(DocumentEvent e) {
        if (duringSetcontent) {
            return;
        }

        if (!contentModified) {
            contentModified = true;
        }

        // logger.finer("Row local is "+rowLocal);
        //TODO add DocumentListener to update MainFrame automatically
        if (!MainFrame.getAnInstance().isBHasModified()) {
            MainFrame.getAnInstance().setBHasModified(true);
        }

        Backend backend = Backend.instance();
        backend.getTMData().bMiniTMFlags[rowLocal] = true;

        //if(!MainFrame.tmpdata.bMiniTMFlags[rowLocal]) MainFrame.tmpdata.bMiniTMFlags[rowLocal] = true;
        update();
        select();
    }

    public void insertUpdate(DocumentEvent e) {
        if (duringSetcontent) {
            return;
        }

        if (!contentModified) {
            contentModified = true;
        }

        //TODO add DocumentListener to update MainFrame automatically
        if (!MainFrame.getAnInstance().isBHasModified()) {
            MainFrame.getAnInstance().setBHasModified(true);
        }

        Backend backend = Backend.instance();
        backend.getTMData().bMiniTMFlags[rowLocal] = true;

        //if(!MainFrame.tmpdata.bMiniTMFlags[rowLocal]) MainFrame.tmpdata.bMiniTMFlags[rowLocal] = true;
        update();
        select();
    }

    /**
     * focusListener
     */
    public void focusGained(FocusEvent e) {
        MainFrame.activeComponent = this;
        select();
    }

    public void focusLost(FocusEvent e) {
        newInsert();

        //logger.finer("set new value.........."+contentModified);
        if (contentModified) {
            //logger.finer("set new value..........");
            String newValue = getText();

            //TODO add listener to set directly in Backend
            Backend backend = Backend.instance();

            if (table == AlignmentMain.testMain1.tableView) {
                backend.getTMData().tmsentences[rowLocal].setSource(newValue);
            } else {
                backend.getTMData().tmsentences[rowLocal].setTranslation(newValue);
            }
        }
    }

    /**
     * keyListener
     */
    public void keyPressed(KeyEvent e) {
        curCharString = "";
        select();

        if(e.getKeyCode() == KeyEvent.VK_ENTER && !insertable){
            e.consume();
        }
        
        
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            if (backwardDeletable == false) {
                curCharString = DELETE;
                e.consume();

                return;
            } else {
                if (newInsert) {
                    setOldRecord();
                    this.operation = EDIT;
                    newInsert = false;
                }
            }
        } else if ((forwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            curCharString = BACKSPACE;
            e.consume();

            return;
        }

        // bug 4714740 4761899
        KeyStroke ks = ShortcutsBuilder.getMenuItemShortcut("Edit", "Cut");

        if (MainFrame.canCut && ((e.getKeyCode() == KeyEvent.VK_CUT) || ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))))) {
            if (!forwardDeletable || !backwardDeletable) {
                e.consume();

                return;
            }

            newInsert();
            setOldRecord();
            this.operation = CUT;
        }

        ks = ShortcutsBuilder.getMenuItemShortcut("Edit", "Paste");

        if (MainFrame.canPaste && ((e.getKeyCode() == KeyEvent.VK_PASTE) || ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))))) {
            //logger.finer("PivotTextPane Paste");
            //if(!this.hasFocus()) {e.consume();return;}
            if (!insertable) {
                e.consume();

                return;
            }

            newInsert();
            setOldRecord();
            this.operation = PASTE;
        }

        ks = ShortcutsBuilder.getMenuItemShortcut("Edit", "Undo");

        if ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))) {
            newInsert();
        }

        ks = ShortcutsBuilder.getMenuItemShortcut("Edit", "Redo");

        if ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))) {
            newInsert();
        }

        ks = ShortcutsBuilder.getMenuItemShortcut("Navigation", "Go To Top");

        if ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))) {
            newInsert();
            AlignmentMain.testMain1.navigateTo(0);
        }

        ks = ShortcutsBuilder.getMenuItemShortcut("Navigation", "Go To Bottom");

        if ((e.getKeyCode() == ks.getKeyCode()) && KeyEvent.getKeyModifiersText(e.getModifiers()).equals(KeyEvent.getKeyModifiersText(ks.getModifiers()))) {
            newInsert();
            AlignmentMain.testMain1.navigateTo(AlignmentMain.testMain1.tableView.getRowCount() - 1);
        }

        /* Ctrl+PageUp and Ctrl+PageDown */
        /*else if(e.getKeyCode() == KeyEvent.VK_PAGE_UP && e.getModifiers() == KeyEvent.CTRL_MASK) {
            newInsert();
            AlignmentMain.testMain1.pageUp();
            AlignmentMain.testMain2.pageUp();
          }else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN && e.getModifiers() == KeyEvent.CTRL_MASK) {
            newInsert();
            AlignmentMain.testMain1.pageDown();
            AlignmentMain.testMain2.pageDown();
          }*/
        if (e.getKeyCode() == KeyEvent.VK_UNDO) {
            newInsert();
            MainFrame.undo.undo();
        } else if (e.getKeyCode() == KeyEvent.VK_AGAIN) {
            newInsert();
            MainFrame.undo.redo();
        } else if (cusorMoved(e)) {
            newInsert();
        } else if ((e.getModifiers() == KeyEvent.CTRL_MASK) || (e.getModifiers() == KeyEvent.ALT_MASK)) {
            dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_EXITED, 0, 0, 5, -5, 1, false));
        }
    }

    public void setMainFramePasteEnable() {
        Container container = this.getParent();

        while (!(container instanceof MainFrame)) {
            container = container.getParent();
        }

        //TODO change to something more readable
        Backend backend = Backend.instance();
        boolean writeProtection = backend.getConfig().isBFlagWriteProtection();
        boolean tagProtection = backend.getConfig().isTagProtection();

        if ((tagProtection && !insertable) || ((table == AlignmentMain.testMain1.tableView) && writeProtection)) {
            ((MainFrame)container).setClipPaste(false);
        } else {
            ((MainFrame)container).setClipPaste(true);
        }
    }

    public void setMainFrameClipEnable() {
        Container container = this.getParent();

        while (!(container instanceof MainFrame)) {
            container = container.getParent();
        }

        String seleStr = this.getSelectedText();

        //TODO change to something more readable
        Backend backend = Backend.instance();
        boolean writeProtection = backend.getConfig().isBFlagWriteProtection();
        boolean tagProtection = backend.getConfig().isTagProtection();

        if ((seleStr == null) || (seleStr.length() == 0)) {
            ((MainFrame)container).setClipCopy(false);
            ((MainFrame)container).setClipCut(false);
        } else {
            ((MainFrame)container).setClipCopy(true);

            if (table == AlignmentMain.testMain1.tableView) {
                if (writeProtection) {
                    ((MainFrame)container).setClipCut(false);
                } else {
                    if (tagProtection && !insertable) {
                        ((MainFrame)container).setClipCut(false);
                    } else {
                        ((MainFrame)container).setClipCut(true);
                    }
                }
            } else {
                if (tagProtection && !insertable) {
                    ((MainFrame)container).setClipCut(false);
                } else {
                    ((MainFrame)container).setClipCut(true);
                }
            }
        }

        setMainFramePasteEnable();
    }

    // ------------------------------------------------------------------------------
    public void keyReleased(KeyEvent e) {
        // bug 4714740
        setMainFrameClipEnable();

        //long startRelease = System.currentTimeMillis();
        boolean need_update = false; // TCS

        if ((backwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_DELETE)) {
            curCharString = "";
            e.consume();

            return;
        } else if ((forwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            curCharString = "";
            e.consume();

            return;
        }

        //note the functionality of the shortcuts of the clipboard in JTextPane component
        //because of the prioty of the shortcuts of the menuitems (copy,cut,paste) is higher
        if ((e.getKeyCode() == KeyEvent.VK_CONTROL) || (e.getKeyCode() == KeyEvent.VK_SHIFT) || (e.getKeyCode() == KeyEvent.VK_ALT)) {
            e.consume();

            return;
        } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_DOWN)) {
            e.consume();

            return;
        }

        /* check num of elements has been changed by entered character */
        int prev = 0;
        int after = 0;

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        if (need_update == false) {
            if (table == AlignmentMain.testMain1.tableView) {
                prev = (tmpdata).tmsentences[table.getSelectedRow()].getSourceBaseElements().length;
            } else {
                prev = (tmpdata).tmsentences[table.getSelectedRow()].getTranslationBaseElements().length;
            }
        }

        update();
        setStyle();

        if (need_update == false) {
            if (table == AlignmentMain.testMain1.tableView) {
                after = (tmpdata).tmsentences[table.getSelectedRow()].getSourceBaseElements().length;
            } else {
                after = (tmpdata).tmsentences[table.getSelectedRow()].getTranslationBaseElements().length;
            }

            if (prev != after) {
                need_update = true;
            }
        }
    }

    public void keyTyped(KeyEvent e) {        
        if (this.table == AlignmentMain.testMain2.tableView) { //Target

            if (isRowApproved()) {
                return;
            }

            if (insertable == false) {
                e.consume();

                return;
            }

            if ((backwardDeletable == false) && curCharString.equals(DELETE)) {
                e.consume();

                return;
            }

            if ((forwardDeletable == false) && curCharString.equals(BACKSPACE)) {
                e.consume();

                return;
            }

            if ((e.getModifiers() == 0) || (e.getModifiers() == KeyEvent.SHIFT_MASK)) {
                if (newInsert) {
                    setOldRecord();
                    this.operation = EDIT;
                    newInsert = false;
                }

                updateTMType(TMData.TMSentence.USER_TRANSLATION);
            }
        } else { //Source

            Backend backend = Backend.instance();
            boolean writeProtect = backend.getConfig().isBFlagWriteProtection();

            if (writeProtect) {
                return;
            }

            if (isRowApproved()) {
                return;
            }

            if (insertable == false) {
                e.consume();

                return;
            }

            if ((backwardDeletable == false) && curCharString.equals(DELETE)) {
                e.consume();

                return;
            }

            if ((forwardDeletable == false) && curCharString.equals(BACKSPACE)) {
                e.consume();

                return;
            }

            if ((e.getModifiers() == 0) || (e.getModifiers() == KeyEvent.SHIFT_MASK)) {
                if (newInsert) {
                    setOldRecord();
                    this.operation = EDIT;
                    newInsert = false;
                }
            }
        }
    }

    private boolean cusorMoved(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_HOME) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_END) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            return true;
        }

        return false;
    }

    public void setWidth(int theWidth) {
        width = theWidth;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int theHeight) {
        height = theHeight;
        this.setSize(new Dimension(this.width, this.height));
    }

    public int getHeight() {
        return height;
    }

    public void PivotCopy() {
        newInsert();
        super.copy();

        // bug 4714740
        setMainFrameClipEnable();
    }

    public void PivotPaste() {
        if (isRowApproved()) {
            return;
        }

        if (!insertable) {
            return;
        }

        newInsert();
        setOldRecord();
        super.paste();
        setNewRecord();

        this.operation = PASTE;

        DocumentUndoableEdit edit = new DocumentUndoableEdit(isSource, operation, rowLocal, sentenceIndex, oldString, newString, oldttype, newttype);
        MainFrame.undo.addDocumentEdit(edit);

        if (this.table == AlignmentMain.testMain2.tableView) {
            updateTMType(TMData.TMSentence.USER_TRANSLATION);
        }

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        if (!tmpdata.bTMFlags[rowLocal]) {
            tmpdata.bTMFlags[rowLocal] = true;
        }

        update();
        setStyle();
    }

    protected boolean isRowApproved() {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        int transStatus = tmpdata.tmsentences[rowLocal].getTranslationStatus();

        return ((transStatus == TMData.TMSentence.VERIFIED) || (transStatus == TMData.TMSentence.APPROVED));
    }

    protected boolean inWriteProtectedSource() {
        Backend backend = Backend.instance();
        boolean writeProtect = backend.getConfig().isBFlagWriteProtection();

        return (isSource && writeProtect);
    }

    public void PivotCut() {
        if (isRowApproved()) {
            return;
        }

        if (inWriteProtectedSource()) {
            return;
        }

        if (!forwardDeletable || !backwardDeletable || !this.insertable) {
            return;
        }

        newInsert();

        setOldRecord();
        super.cut();
        setNewRecord();

        this.operation = CUT;

        DocumentUndoableEdit edit = new DocumentUndoableEdit(isSource, operation, rowLocal, sentenceIndex, oldString, newString, oldttype, newttype);
        MainFrame.undo.addDocumentEdit(edit);

        if (this.table == AlignmentMain.testMain2.tableView) {
            updateTMType(TMData.TMSentence.USER_TRANSLATION);
        }

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        if (!tmpdata.bTMFlags[rowLocal]) {
            tmpdata.bTMFlags[rowLocal] = true;
        }

        update();
        setStyle();

        // bug 4714740
        setMainFrameClipEnable();
    }

    void update() {
        try {
            value = content.getText(0, content.getLength());
            elements = org.jvnet.olt.editor.util.BaseElements.extractContent(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getText() {
        try {
            return content.getText(0, content.getLength());
        } catch (Exception ex) {
            return null;
        }
    }

    public String getContent() {
        return getText();
    }

    void setOldRecord() {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        oldString = this.getText();
        oldttype = (tmpdata.tmsentences[rowLocal].getTranslationStatus() * 10) + tmpdata.tmsentences[rowLocal].getTranslationType();
    }

    void setNewRecord() {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        newString = this.getText();
        newttype = (tmpdata.tmsentences[rowLocal].getTranslationStatus() * 10) + TMData.TMSentence.USER_TRANSLATION;
    }

    void newInsert() {
        if (!newInsert) {
            setNewRecord();

            DocumentUndoableEdit edit = new DocumentUndoableEdit(isSource, operation, rowLocal, sentenceIndex, oldString, newString, oldttype, newttype);
            MainFrame.undo.addDocumentEdit(edit);
        }

        this.operation = "";
        newInsert = true;
    }

    // bug 4745303
    public void setTheRowLoacl(int theRow) {
        this.rowLocal = theRow;
    }

    //  Start new control variable setting code

    /** This badly named method evaluates whether the user can type into the text
     * field at the current caret position or with the current selection.
     */
    void select() {
        //TODO change to something more readable
        Backend backend = Backend.instance();
        boolean tagProtection = backend.getConfig().isTagProtection();

        int dot = this.getSelectionEnd();
        int mark = this.getSelectionStart();

        if (!tagProtection) {
            //  No tag protection, so the user can do whatever they want.
            insertable = true;
            forwardDeletable = true;
            backwardDeletable = true;

            return;
        }

        if (dot == mark) { //  Working on the basis of caret placement not selection
            evaluateCaretPosition(mark);
        } else { //  Working on the basis of selection
            evaluateSelectionPosition(mark, dot);
        }
    }

    /** This method sets the control variables, based on the position of the caret.
     */
    protected void evaluateCaretPosition(int pos) {
        //  Determine the following: if inside a protected element, if just
        //  before a protected element, and if just after a protected element.
        boolean insideProtected = isCaretInProtectedElem(elements, pos);
        boolean beforeProtected = isCaretBeforeProtectedElem(elements, pos);
        boolean afterProtected = isCaretAfterProtectedElem(elements, pos);

        insertable = !insideProtected;
        forwardDeletable = !(insideProtected || afterProtected);
        backwardDeletable = !(insideProtected || beforeProtected);
    }

    protected boolean isCaretInProtectedElem(PivotBaseElement[] pbes, int caretPos) {
        if ((pbes == null) || (pbes.length == 0)) {
            return false;
        }

        for (int i = 0; i < pbes.length; i++) {
            int elemStart = pbes[i].getPositionSite();
            int elemEnd = (pbes[i].getPositionSite() + pbes[i].getVisibleLength()) - 1; //  First position plus n-1 more

            if ((caretPos > elemStart) && (caretPos <= elemEnd)) {
                return pbes[i].isMarkup();
            }
        }

        return false;
    }

    protected boolean isCaretBeforeProtectedElem(PivotBaseElement[] pbes, int caretPos) {
        if ((pbes == null) || (pbes.length == 0)) {
            return false;
        }

        for (int i = 0; i < pbes.length; i++) {
            int elemStart = pbes[i].getPositionSite();

            if ((caretPos == elemStart)) {
                return pbes[i].isMarkup();
            }
        }

        return false;
    }

    protected boolean isCaretAfterProtectedElem(PivotBaseElement[] pbes, int caretPos) {
        if ((pbes == null) || (pbes.length == 0)) {
            return false;
        }

        for (int i = 0; i < pbes.length; i++) {
            int elemEnd = (pbes[i].getPositionSite() + pbes[i].getVisibleLength()) - 1; //  First position plus n-1 more

            if ((caretPos == (elemEnd + 1))) {
                return pbes[i].isMarkup();
            }
        }

        return false;
    }

    /** This method sets the control variable, based on the selection. If the
     * selection does intersect or contain a protected element, then the control
     * variable are set to false.
     */
    protected void evaluateSelectionPosition(int selStart, int selEnd) {
        //  Get the elements that overlap with the selection
        java.util.List intersectingElems = elementsIntersectingSelection(elements, selStart, selEnd);

        //  Test each of these elements to see if any are not Text elements.
        java.util.Iterator iterator = intersectingElems.iterator();
        boolean boolContainsProtected = false;

        while (iterator.hasNext() && !boolContainsProtected) {
            PivotBaseElement pbe = (PivotBaseElement)iterator.next();
            boolContainsProtected = pbe.isMarkup();
        }

        if (boolContainsProtected) {
            insertable = false;
            forwardDeletable = false;
            backwardDeletable = false;
        } else {
            insertable = true;
            forwardDeletable = true;
            backwardDeletable = true;
        }
    }

    /** This method gets the list of elements that intersect with a selection
     * defined by its start and end positions.
     */
    protected java.util.List elementsIntersectingSelection(PivotBaseElement[] pbes, int selStart, int selEnd) {
        java.util.List intersectList = new java.util.LinkedList();

        if ((pbes == null) || (pbes.length == 0)) {
            return intersectList;
        }

        for (int i = 0; i < pbes.length; i++) {
            int elemStart = pbes[i].getPositionSite();
            int elemEnd = (pbes[i].getPositionSite() + pbes[i].getVisibleLength()) - 1; //  First position plus n-1 more

            boolean boolContained = ((elemStart >= selStart) && (elemEnd < selEnd));
            boolean boolIntersect = (((elemStart >= selStart) && (elemStart < selEnd)) || ((elemEnd >= selStart) && (elemEnd < selEnd)));

            if (boolContained || boolIntersect) {
                intersectList.add(pbes[i]);
            }
        }

        return intersectList;
    }

    //  End new control variable setting code
    public void select(int start, int end, AttributeSet s) {
        content.setCharacterAttributes(start, end - start, s, true);
    }

    // for make selection at finding
    // it seems that select() does not make selection until
    // this gets focus. So doing select2() and select() to
    // display selection when find at Find dialog
    public void select2(int start, int end) {
        content.setCharacterAttributes(start, end - start, selectStyle2, true);
    }

    public void unselect(int start, int end) {
        //logger.finer("unSelect");
        update();
    }

    public void underLine(int start, int end) {
        content.setCharacterAttributes(start, end - start, underlineStyle, true);
    }

    public void addWordSuggestion(String word, Vector suggestion) {
        wordHashes.put(word.toLowerCase(), suggestion);
    }

    public static Vector getWordSuggestion(String word) {
        Object o = wordHashes.get(word.toLowerCase());

        if (o == null) {
            return null;
        } else {
            return (Vector)o;
        }
    }

    public static void addToIgnoreTable(String word) {
        ignoreWords.addElement(word.toLowerCase());
    }

    public static String getWordFromIgnoreTable(String word) {
        if (ignoreWords.contains(word.toLowerCase())) {
            return word;
        } else {
            return null;
        }
    }

    void removeWordSuggestion(String word) {
        if (wordHashes.contains(word.toLowerCase())) {
            wordHashes.remove(word.toLowerCase());
        }
    }

    int getIndexInElements(int position) {
        int len = 0;

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement elm = elements[i];
            len += elm.getVisibleLength();

            if (len > position) {
                return i;
            }
        }

        return elements.length - 1;
    }

    public void resetContent() {
        this.value = null;
        this.elements = null;
    }

    /**
     *    Insert a string to this text pane.
     *  @param source The string that will be inserted.
     */
    public void setContent(String source, PivotBaseElement[] v) {
        initInsert = true;
        needRefresh = false;
        insertable = true;
        forwardDeletable = true;
        backwardDeletable = true;
        curCharString = "";

        duringSetcontent = true;

        value = source;

        //logger.finer("vInput.size = "+v.length);
        try {
            content.remove(0, content.getLength());

            // logger.finer("Finish content remove");
            this.elements = v;

            //logger.finer("this.elements.size = "+elements.length);
            content.insertString(0, source, normalStyle);

            //logger.finer("Finish content insert");
            setStyle();

            //logger.finer("Finish content setStyle");
        } catch (BadLocationException e) {
            logger.throwing(getClass().getName(), "setContent", e);
            logger.severe("Exception:" + e);

            //TODO throw Exception ???
        }

        contentModified = false;
        duringSetcontent = false;
    }

    void setStyle() {
        int p = 0;

        try {
            //logger.finer("this.elements.size = "+elements.length);
            for (int i = 0; i < elements.length; i++) {
                PivotBaseElement e = elements[i];
                int curLen = e.getContent().length();

                //logger.finer("p= "+p+" Element="+e.getContent()+" curLen="+curLen);
                e.setPositionSite(p);

                switch (e.getElemType()) {
                case PivotBaseElement.BPT_ELEM:
                case PivotBaseElement.EPT_ELEM:
                case PivotBaseElement.IT_ELEM:
                case PivotBaseElement.MRK_ELEM:
                case PivotBaseElement.PH_ELEM:
                    content.setCharacterAttributes(p, e.getContent().length(), tagStyle, true);

                    break;

                case PivotBaseElement.TEXT:
                    content.setCharacterAttributes(p, e.getContent().length(), normalStyle, true);

                    break;
                }

                p += curLen;
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    public void insertString(int iStyle, int iPos, String str) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();

        //Integer nInt = new Integer(str.substring(0, 1));
        switch (iStyle) {
        case 1:
            StyleConstants.setForeground(attrs, Color.red);

            break;

        case 2:
            StyleConstants.setForeground(attrs, Color.black);

            break;

            //and so forth
        }

        //String strLine = str.substring(1) + "\n";
        try {
            content.insertString(iPos, str, attrs); //prepends
        } catch (BadLocationException be) {
            be.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setFont(MainFrame.getDefaultFont());
    }

    public static void updateTMType(int type) {
        int row = AlignmentMain.testMain1.tableView.getSelectedRow();

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        (tmpdata).tmsentences[row].setTranslationType(type);
        AlignmentMain.testMain2.tableView.repaint();

        MainFrame.getAnInstance().setMenuState();
    }

    public static boolean updateTMStatus(Container c, int status) {
        int row = AlignmentMain.testMain1.tableView.getSelectedRow();

        /**
         * setEnable for cell editor
         */
        if (AlignmentMain.testMain2.tableView.isEditing() && (AlignmentMain.testMain2.tableView.getEditingColumn() == 1)) {
            if ((status == TMData.TMSentence.VERIFIED) || (status == TMData.TMSentence.APPROVED)) {
                PivotTextEditor1.targetEditor.setEditable(false);
            } else {
                PivotTextEditor1.targetEditor.setEditable(true);
            }
        }

        MainFrame.getAnInstance().setBHasModified(true);

        //MainFrame.tmpdata.bTMFlags[row] = true;
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();
        tmpdata.tmsentences[row].setTranslationStatus(status);

        //AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(row,0,true));
        AlignmentMain.testMain1.tableView.repaint();

        if (status == TMData.TMSentence.TRANSLATED) {
            Container f = AlignmentMain.testMain1.tableView.getParent();

            while (!(f instanceof MainFrame))
                f = f.getParent();

            ((MainFrame)f).jBtnUpdateMiniTM_actionPerformed(null);
        }

        MainFrame.getAnInstance().setMenuState();

        return true;
    }

    void showPopup(MouseEvent e) {
        //  Maintenance note: This method used to hold a heap of stuff for word 
        //  lookups. This has been removed as it didn't seem to work anyway, and 
        //  it was causing problems. 
        select();
        showEditMenu(e);
    }

    public static void showEditMenu(MouseEvent e) {
        if (popEditMenu == null) {
            popEditMenu = new JPopupMenu();

            JMenu markMenu = new JMenu("Mark Segment As");
            popEditMenu.add(markMenu);
            transMenu = new PopupEditListener("Translated", 0);
            markMenu.add(transMenu);
            approveMenu = new PopupEditListener("Approved", 1);
            markMenu.add(approveMenu);
            rejectMenu = new PopupEditListener("Rejected", 2);
            markMenu.add(rejectMenu);
            untransMenu = new PopupEditListener("Untranslated", 3);
            markMenu.add(untransMenu);
            popEditMenu.addSeparator();

            confirmTransMenu = new PopupEditListener("Confirm and Translate Next", 4);
            popEditMenu.add(confirmTransMenu);
            confirmReviewMenu = new PopupEditListener("Approve and go to next translated", 5);
            popEditMenu.add(confirmReviewMenu);
            confirmRejectMenu = new PopupEditListener("Reject and go to next translated", 6);
            popEditMenu.add(confirmRejectMenu);
        }

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        int row = AlignmentMain.testMain1.tableView.getSelectedRow();
        int iStatus = tmpdata.tmsentences[row].getTranslationStatus();

        switch (iStatus) {
        case TMData.TMSentence.UNTRANSLATED:
            transMenu.setEnabled(true);
            confirmTransMenu.setEnabled(true);
            confirmReviewMenu.setEnabled(false);
            confirmRejectMenu.setEnabled(false);
            untransMenu.setEnabled(false);
            approveMenu.setEnabled(false);
            rejectMenu.setEnabled(false);

            break;

        case TMData.TMSentence.TRANSLATED:
            transMenu.setEnabled(false);
            confirmTransMenu.setEnabled(false);
            confirmReviewMenu.setEnabled(true);
            confirmRejectMenu.setEnabled(true);

            int targetType = (tmpdata).tmsentences[row].getTranslationType();

            if (targetType == TMData.TMSentence.EXACT_TRANSLATION) {
                untransMenu.setEnabled(false);
            } else {
                untransMenu.setEnabled(true);
            }

            approveMenu.setEnabled(true);
            rejectMenu.setEnabled(true);

            break;

        // case TMData.TMSentence.VERIFIED:
        case TMData.TMSentence.APPROVED:
        case TMData.TMSentence.REJECTED:
            transMenu.setEnabled(true);
            confirmTransMenu.setEnabled(false);
            confirmReviewMenu.setEnabled(true);
            confirmRejectMenu.setEnabled(true);
            untransMenu.setEnabled(false);
            approveMenu.setEnabled(true);
            rejectMenu.setEnabled(true);

            break;
        }

        popEditMenu.show((Component)e.getSource(), e.getX(), e.getY());
    }

    public void setFont(java.awt.Font font) {
        super.setFont(font);
    }
}


class PopupEditListener extends JMenuItem implements ActionListener {
    String title;
    int index;

    PopupEditListener(String title, int index) {
        super(title);
        this.title = title;
        this.index = index;

        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        int row = AlignmentMain.testMain1.tableView.getSelectedRow();

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        switch (index) {
        case 0: //trans
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.TRANSLATED);

            break;

        case 1: //Approve
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.APPROVED);

            break;

        case 2: //Reject
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.REJECTED);

            break;

        case 3: //untrans
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.UNTRANSLATED);

            break;

        case 4: //confirm trans

            boolean success = PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.TRANSLATED);

            if (success) {
                int nextTMIndex = (tmpdata).getIndexOfTM(row, 1, 0);

                // bug 4713261
                if (nextTMIndex == -1) {
                    nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0);
                }

                boolean hasAutoTranslated = false;

                do {
                    if (nextTMIndex == -1) {
                        break;
                    }

                    hasAutoTranslated = (MainFrame.updateStatus(nextTMIndex) == 1) ? true : false;
                    MainFrame.navigateTo(nextTMIndex);

                    if (hasAutoTranslated) {
                        nextTMIndex = (tmpdata).getIndexOfTM(nextTMIndex, 1, 0);

                        // bug 4713261
                        if (nextTMIndex == -1) {
                            nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0);
                        }

                        continue;
                    }

                    hasAutoTranslated = (tmpdata).tmsentences[nextTMIndex].isAutoTranslated();

                    // bug 4713261
                    if (hasAutoTranslated) {
                        nextTMIndex = (tmpdata).getIndexOfTM(nextTMIndex, 1, 0);

                        if (nextTMIndex == -1) {
                            nextTMIndex = (tmpdata).getIndexOfTM(-1, 1, 0);
                        }
                    }

                    //if(hasAutoTranslated) nextTMIndex = (tmpdata).getIndexOfTM(nextTMIndex,1,0);
                } while (hasAutoTranslated);
            }

            break;

        case 5: //confirm approve
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.APPROVED);

            int nextTMIndex = (tmpdata).getIndexOfTM(row, 5, 0); //  Go to next translated
            AlignmentMain.testMain2.navigateTo(nextTMIndex);

            break;

        case 6: //confirm reject
            PivotTextPane.updateTMStatus((Container)AlignmentMain.testMain1, TMData.TMSentence.REJECTED);

            int nextTMIndex2 = (tmpdata).getIndexOfTM(row, 5, 0); //  Go to next translated
            AlignmentMain.testMain2.navigateTo(nextTMIndex2);

            break;
        }

        ;
    }
}
