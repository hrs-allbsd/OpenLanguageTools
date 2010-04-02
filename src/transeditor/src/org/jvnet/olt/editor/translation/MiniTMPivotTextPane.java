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


//import com.sun.tmci.editor.control.*;
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


public class MiniTMPivotTextPane extends JTextPane implements KeyListener, MouseListener, FocusListener {
    private static final Logger logger = Logger.getLogger(MiniTMPivotTextPane.class.getName());

    //public final static String IN_VISIBLE_CHAR = "";
    public final static String COPY = "COPY";
    public final static String PASTE = "PASTE";
    public final static String CUT = "CUT";
    public final static String DELETE = "DELETE";
    public final static String BACKSPACE = "BACKSPACE";
    public final static String EDIT = "EDIT";

    //static Style normalStyle, tagStyle,selectStyle,unselectStyle,underlineStyle;
    //public static Style insertionStyle, deletionStyle,replaceStyle,changePositionStyle;
    //public static Style whiteSpaceInsertionStyle, whiteSpaceDeletionStyle,whiteSpaceReplaceStyle,whiteSpaceChangePositionStyle;
    //static StyleContext sc;
    //static StyledEditorKit sek;
    //public static Hashtable wordHashes = new Hashtable();
    //public static Vector ignoreWords = new Vector();
    //public static int wordInRowIndex = -1;
    //public static int wordStart = 0;
    //public static int wordEnd = 0;
    //JPopupMenu pop = new JPopupMenu();

    /**
     * edit popup Menu
     */
    public static JPopupMenu popEditMenu = null;
    public static JMenuItem transMenu = null;
    public static JMenuItem verifyMenu = null;
    public static JMenuItem untransMenu = null;
    public static JMenuItem confirmTransMenu = null;
    public static JMenuItem confirmReviewMenu = null;

    //public static String delim = "0123456789 ,.;:?!-_()<>/\\|]}{[+-*&^%$#@~`'\"\r\n";
    public DefaultStyledDocument content;
    public String value = "";
    public PivotBaseElement[] elements;
    int selectIdStart;
    int selectIdEnd;
    boolean initInsert = true;
    boolean needRefresh = false;
    String operation = "";
    int oldDot;
    int oldMark;
    String oldString;
    Vector oldElements;
    int newDot;
    int newMark;
    String newString;
    Vector newElements;
    boolean newInsert = true;
    boolean cutPressed = false;
    boolean pastePressed = false;
    boolean insertable = true;
    boolean forwardDeletable = true;
    boolean backwardDeletable = true;
    boolean keyTyped = false;
    String curCharString = "";
    JTable table;

    //int row;
    int sentenceIndex;
    boolean isSource;
    boolean tagProtection = false;
    int width = 0;
    int height = 0;

    public MiniTMPivotTextPane() {
        super();
    }

    public MiniTMPivotTextPane(JTable table, int row, int sentenceIndex, boolean isSrc) {
        super();
        this.table = table;

        //this.row = row;
        this.sentenceIndex = sentenceIndex;
        this.isSource = isSrc;

        //StyleConstants.setIcon(tagStyle,tagIcon);
        content = new DefaultStyledDocument(PivotTextPane.sc);
        this.setEditorKit(PivotTextPane.sek);
        this.setSelectedTextColor(Color.blue);
        this.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        setStyledDocument(content);
        elements = new PivotBaseElement[0];
        selectIdStart = -1;
        selectIdEnd = -1;
        setEditable(false);

        /*this.addFocusListener(this);
        this.addKeyListener(this);
        this.addMouseListener(this);*/
    }

    public Insets getInsets() {
        return new Insets(0, 0, 0, 2);
    }

    /**
     * mouseListener
     */
    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        select();

        //showPopup(e);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        select();
    }

    /**
     * focusListener
     */
    public void focusLost(FocusEvent e) {
    }

    public void focusGained(FocusEvent e) {
        select();
    }

    /**
     * keyListener
     */
    public void keyPressed(KeyEvent e) {
        curCharString = "";
        select();

        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            if (backwardDeletable == false) {
                curCharString = DELETE;
                e.consume();

                return;
            } else {
            }
        } else if ((forwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            curCharString = BACKSPACE;
            e.consume();

            return;
        }

        if ((e.getKeyCode() == KeyEvent.VK_COPY) || ((e.getKeyCode() == KeyEvent.VK_C) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            super.copy();
        } else if ((e.getKeyCode() == KeyEvent.VK_CUT) || ((e.getKeyCode() == KeyEvent.VK_X) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            if (!forwardDeletable || !backwardDeletable) {
                e.consume();

                return;
            }
        } else if ((e.getKeyCode() == KeyEvent.VK_PASTE) || ((e.getKeyCode() == KeyEvent.VK_V) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            if (!insertable) {
                e.consume();

                return;
            }
        } else if ((e.getKeyCode() == KeyEvent.VK_HOME) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            MiniTMAlignmentMain.testMain.navigateTo(0);
        } else if ((e.getKeyCode() == KeyEvent.VK_END) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            MiniTMAlignmentMain.testMain.navigateTo(MiniTMAlignmentMain.testMain.tableView.getRowCount() - 1);
        } else if ((e.getKeyCode() == KeyEvent.VK_PAGE_UP) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            MiniTMAlignmentMain.testMain.pageUp();
        } else if ((e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            MiniTMAlignmentMain.testMain.pageDown();
        }
    }

    public void keyReleased(KeyEvent e) {
        if ((backwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_DELETE)) {
            curCharString = "";
            e.consume();

            return;
        } else if ((forwardDeletable == false) && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            curCharString = "";
            e.consume();

            return;
        }

        if ((e.getKeyCode() == KeyEvent.VK_CUT) || ((e.getKeyCode() == KeyEvent.VK_X) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            if (!forwardDeletable || !backwardDeletable) {
                e.consume();

                return;
            }

            super.cut();
        } else if ((e.getKeyCode() == KeyEvent.VK_PASTE) || ((e.getKeyCode() == KeyEvent.VK_V) && (e.getModifiers() == KeyEvent.CTRL_MASK))) {
            if (!insertable) {
                e.consume();

                return;
            }

            super.paste();
        }

        update();
    }

    public void keyTyped(KeyEvent e) {
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

    public void setWidth(int w) {
        width = w;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int h) {
        height = h;
    }

    public int getHeight1() {
        return height;
    }

    public void PivotCopy() {
        super.copy();
    }

    public void PivotPaste() {
        if (!insertable) {
            return;
        }

        super.paste();
    }

    public void PivotCut() {
        if (!forwardDeletable || !backwardDeletable) {
            return;
        }

        super.cut();
    }

    void update() {
        try {
            value = content.getText(0, content.getLength());
            elements = org.jvnet.olt.editor.util.BaseElements.extractContent(value);
            setStyle();
            select();
        } catch (Exception ex) {
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

    void select() {
        int dot = this.getSelectionEnd();
        int mark = this.getSelectionStart();

        insertable = true;
        forwardDeletable = true;
        backwardDeletable = true;

        selectIdStart = getIndexInElements(mark);
        selectIdEnd = getIndexInElements(dot);

        if ((selectIdStart == -1) || (selectIdEnd == -1)) {
            return;
        }

        if (selectIdStart != selectIdEnd) {
            if (tagProtection) { //parent.tagProtection) {

                PivotBaseElement pbe = elements[selectIdStart];

                if (pbe.getFlag()) {
                    insertable = false;
                    forwardDeletable = false;
                    backwardDeletable = false;
                } else {
                    if (selectIdEnd > (selectIdStart + 1)) {
                        insertable = false;
                        forwardDeletable = false;
                        backwardDeletable = false;
                    } else {
                        PivotBaseElement pbe1 = elements[selectIdEnd];

                        if (dot == pbe1.getPositionSite()) {
                            if (PivotTag.betweenIntegratedTag(selectIdStart, elements)) {
                                insertable = false;
                                backwardDeletable = false;
                                forwardDeletable = false;
                            } else {
                                insertable = true;
                                forwardDeletable = true;
                                backwardDeletable = true;
                            }
                        } else {
                            insertable = false;
                            forwardDeletable = false;
                            backwardDeletable = false;
                        }
                    }
                }
            } else {
            }
        } else if (selectIdStart == selectIdEnd) {
            if (tagProtection) { //tagProtection) {

                PivotBaseElement pbe = elements[selectIdStart];

                if (pbe.getFlag()) {
                    if ((mark == dot) && (dot == pbe.getPositionSite())) {
                        if (selectIdStart == 0) {
                            insertable = true;
                            backwardDeletable = false;
                            forwardDeletable = false;
                        } else if (selectIdStart == 1) {
                            PivotBaseElement preE = elements[selectIdStart - 1];

                            if (!preE.getFlag()) {
                                backwardDeletable = false;
                            } else {
                                if (PivotTag.integratedTag(preE.getTagName(), true) && pbe.getTagName().equals("/" + preE.getTagName())) {
                                    insertable = false;
                                    backwardDeletable = false;
                                    forwardDeletable = false;
                                } else {
                                    backwardDeletable = false;
                                    forwardDeletable = false;
                                }
                            }
                        } else {
                            if (PivotTag.betweenIntegratedTag(selectIdStart, elements)) {
                                insertable = false;
                                backwardDeletable = false;
                                forwardDeletable = false;
                            } else {
                                if (PivotTag.betweenIntegratedTag(selectIdStart - 1, elements)) {
                                    insertable = false;
                                    backwardDeletable = false;
                                    forwardDeletable = false;
                                } else {
                                    PivotBaseElement preE = elements[selectIdStart - 1];

                                    if (preE.getFlag()) {
                                        if (PivotTag.integratedTag(preE.getTagName(), true) && pbe.getTagName().equals("/" + preE.getTagName())) {
                                            insertable = false;
                                            backwardDeletable = false;
                                            forwardDeletable = false;
                                        } else {
                                            backwardDeletable = false;
                                            forwardDeletable = false;
                                        }
                                    } else {
                                        backwardDeletable = false;
                                    }
                                }
                            }
                        }
                    } else if ((mark == dot) && (dot == (pbe.getPositionSite() + pbe.getContent().length()))) {
                        insertable = true;
                        forwardDeletable = false;
                        backwardDeletable = false;
                    } else {
                        insertable = false;
                        forwardDeletable = false;
                        backwardDeletable = false;
                    }
                } else { //not tag element

                    if ((selectIdStart != 0) && (selectIdStart != (elements.length - 1))) { //middle element

                        if (PivotTag.betweenIntegratedTag(selectIdStart, elements)) {
                            insertable = false;
                            backwardDeletable = false;
                            forwardDeletable = false;
                        } else {
                            if (mark == dot) {
                                if (mark == pbe.getPositionSite()) {
                                    forwardDeletable = false;
                                }

                                if (dot == (pbe.getPositionSite() + pbe.getContent().length())) {
                                    backwardDeletable = false;
                                }
                            }
                        }
                    } else { // first element or last element

                        if (mark == dot) {
                            if (mark == pbe.getPositionSite()) {
                                forwardDeletable = false;
                            }

                            if (dot == (pbe.getPositionSite() + pbe.getContent().length())) {
                                backwardDeletable = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void select(int start, int end) {
        try {
            content.setCharacterAttributes(start, end - start, PivotTextPane.selectStyle, true);
        } catch (Exception ex) {
        }
    }

    public void select(int start, int end, AttributeSet s) {
        content.setCharacterAttributes(start, end - start, s, true);
    }

    public void unselect(int start, int end) {
        update();
    }

    public void underLine(int start, int end) {
        content.setCharacterAttributes(start, end - start, PivotTextPane.underlineStyle, true);
    }

    int getIndexInElements(int position) {
        int index = -1;
        String temp = "";

        try {
            temp = content.getText(0, position);
        } catch (BadLocationException e) {
            logger.throwing(getClass().getName(), "getIndexInElements", e);
            logger.severe("Exception:" + e);

            //TODO thorw an exception ???
            return index;
        }

        for (int i = 0; i < elements.length; i++) {
            String tt = this.getVisibleStringTo(i);

            if (tt.equals(temp)) {
                if (i != (elements.length - 1)) {
                    PivotBaseElement e = elements[i + 1];

                    if (e.getFlag()) {
                        if (!(e.isVisible())) {
                            continue;
                        } else {
                            index = i + 1;

                            break;
                        }
                    } else {
                        index = i + 1;

                        break;
                    }
                } else {
                    index = i;

                    break;
                }
            } else if (tt.startsWith(temp)) {
                index = i;

                break;
            }
        }

        if (index == -1) {
            return -1;
        }

        if (index == elements.length) {
            index = index - 1;
        }

        if (index == (elements.length - 1)) {
            PivotBaseElement e = elements[index];

            while (e.getFlag()) {
                if (!(e.isVisible())) {
                    index = index - 1;

                    if (index != -1) {
                        e = elements[index];
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        temp = null;

        return index;
    }

    public void setContent(String source) {
        try {
            content.remove(0, content.getLength());
            content.insertString(0, source, PivotTextPane.normalStyle);
        } catch (BadLocationException e) {
            logger.throwing(getClass().getName(), "setContent", e);
            logger.severe("Exception:" + e);

            //TODO throw an Exception ???
        }
    }

    /**
     *    Insert a string to this text pane.
     *  @param source The string that will be inserted.
     */
    public void setContent(String source, PivotBaseElement[] v) {
        StringBuffer s = new StringBuffer();

        if ((v == null) || (v.length == 0)) {
            v = org.jvnet.olt.editor.util.BaseElements.extractContent(source);
        } else {
            for (int i = 0; i < v.length; i++) {
                s.append((v[i]).getContent());
            }

            if (!(s.toString().equals(source))) {
                source = s.toString();
            }
        }

        value = source;
        initInsert = true;
        needRefresh = false;
        newInsert = true;
        insertable = true;
        forwardDeletable = true;
        backwardDeletable = true;
        keyTyped = false;
        cutPressed = false;
        pastePressed = false;
        curCharString = "";

        try {
            content.remove(0, content.getLength());
            this.elements = v;

            content.insertString(0, source, PivotTextPane.normalStyle);
        } catch (BadLocationException e) {
            logger.throwing(getClass().getName(), "setContent", e);
            logger.severe("Exception:" + e);

            //TODO throw an Exception ???
        }

        setStyle();
    }

    String getVisibleStringTo(int indexOfElements) {
        StringBuffer temp = new StringBuffer(0);

        for (int i = 0; i <= indexOfElements; i++) {
            PivotBaseElement e = elements[i];

            if (!e.getFlag() || e.isVisible()) {
                String t = e.getContent();
                temp.append(t);
            }
        }

        return temp.toString();
    }

    void setStyle() {
        int p = 0;

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = elements[i];
            int curLen = e.getContent().length();
            e.setPositionSite(p);

            switch (e.getElemType()) {
            case PivotBaseElement.BPT_ELEM:
            case PivotBaseElement.EPT_ELEM:
            case PivotBaseElement.IT_ELEM:
            case PivotBaseElement.MRK_ELEM:
            case PivotBaseElement.PH_ELEM:
                content.setCharacterAttributes(p, e.getContent().length(), PivotTextPane.tagStyle, true);

                break;

            case PivotBaseElement.TEXT:
                content.setCharacterAttributes(p, e.getContent().length(), PivotTextPane.normalStyle, true);

                break;
            }

            //            if(e.getFlag()) {
            //                Tag tag = e.getTag();
            //                if( tag.isVisible() ) {
            //                    content.setCharacterAttributes(p,curLen,PivotTextPane.tagStyle,true);
            //                }
            //            }else {
            //                if(i != 0 && i != (elements.length-1)) {
            //                    if(PivotTag.betweenIntegratedTag(i,elements)) {
            //                        content.setCharacterAttributes(p,curLen,PivotTextPane.tagStyle,true);
            //                    }else {
            //                        content.setCharacterAttributes(p,curLen,PivotTextPane.normalStyle,true);
            //                    }
            //                }else {
            //                    content.setCharacterAttributes(p,curLen,PivotTextPane.normalStyle,true);
            //                }
            //                
            //                
            //            }
            p += curLen;
        }
    }
}
