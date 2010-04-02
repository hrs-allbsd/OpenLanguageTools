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

import java.io.*;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.undo.*;

import org.jvnet.olt.editor.model.*;


public class MiniTMTextRender implements TableCellRenderer {
    private static final Logger logger = Logger.getLogger(MiniTMTextRender.class.getName());
    Color unselectedBackground;
    Color unselectedForeground;

    public MiniTMTextRender() {
        super();
    }

    private MiniTMTableFrame getMiniTMTableFrame(JTable table) {
        Container c = table.getParent();

        while (!(c instanceof MiniTMTableFrame)) {
            c = c.getParent();
        }

        return (MiniTMTableFrame)c;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Vector data = (Vector)value;
        MiniTMDataPanel panel = null;
        panel = getMiniTMTableFrame(table).panels[row];

        if (panel == null) {
            getMiniTMTableFrame(table).panels[row] = new MiniTMDataPanel();
            panel = getMiniTMTableFrame(table).panels[row];
        }

        if ((panel.getData() != null) && panel.getData().equals(data)) {
            Component[] childrens = panel.getComponents();

            for (int i = 0; i < childrens.length; i++) {
                if (isSelected) {
                    if (childrens[i] instanceof JScrollPane) {
                        ((JScrollPane)childrens[i]).getViewport().getView().setForeground(table.getSelectionForeground());
                        ((JScrollPane)childrens[i]).getViewport().getView().setBackground(table.getSelectionBackground());
                    } else {
                        childrens[i].setBackground(table.getSelectionBackground());
                    }

                    panel.setForeground(table.getSelectionForeground());
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    if (childrens[i] instanceof JScrollPane) {
                        ((JScrollPane)childrens[i]).getViewport().getView().setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                        ((JScrollPane)childrens[i]).getViewport().getView().setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                    } else {
                        childrens[i].setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                        childrens[i].setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                    }

                    panel.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                    panel.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                }
            }

            if (panel.getWidth() != getInitialWidth(table, column)) {
                panel.setWidth(getInitialWidth(table, column));
                panel.refresh(table, row, getInitialWidth(table, column));
            }

            highlightDiff(table, row); //new source
            highlightDiff(table, row); //old source

            return panel;
        } else {
            panel.setData(data);
            panel.removeAll();
        }

        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setSize(getInitialWidth(table, column), getInitialHeight(table, row));

        int curHight = 0;

        //logger.finer(data.size());
        for (int i = 0; i < data.size(); i++) {
            MiniTMTextPane p = null;
            p = getMiniTMTableFrame(table).editPanes[row][i];

            if (p == null) {
                p = getMiniTMTableFrame(table).editPanes[row][i] = new MiniTMTextPane();
                p.setEditable(false);
            }

            p.setBorder(null);
            p.setSize(getInitialWidth(table, column) - 20, getInitialHeight(table, row));

            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            int iSelectedRow = AlignmentMain.testMain1.tableView.getSelectedRow();
            Match[] oTemp = tmpdata.tmsentences[iSelectedRow].getMatches(false);
            int iNumSegsInMatchSrc = (oTemp[row]).getSrcSegmentNumber();

            switch (i) {
            case 0:

                //p.setContent(tmpdata.tmsentences[iSelectedRow].getSource(),tmpdata.tmsentences[iSelectedRow].getSourceBaseElements());
                String strFullSourceText = buildFullSourceText(iSelectedRow, iNumSegsInMatchSrc);
                PivotBaseElement[] srcBaseElems = concatenateSourceBaseElementsArray(iSelectedRow, iNumSegsInMatchSrc);
                p.setContent(strFullSourceText, srcBaseElems);

                break;

            case 1:
                p.setContent((oTemp[row]).getLRDS(), tmpdata.tmsentences[iSelectedRow].getMatchesBaseElements()[(3 * row) + 1]);

                break;

            case 2:
                p.setContent((oTemp[row]).getLRDT(), tmpdata.tmsentences[iSelectedRow].getMatchesBaseElements()[(3 * row) + 2]);

                break;

            default:
                break;
            }

            JScrollPane src = new JScrollPane() {
                public Insets getInsets() {
                    return new Insets(0, 0, 0, 0);
                }
            };

            src.setBorder(null);
            src.setViewportView(p);

            JPanel iconPanel = new JPanel();
            iconPanel.setLayout(null);
            iconPanel.setBackground(Color.white);
            iconPanel.setBorder(null);

            JLabel label = new JLabel();

            if (i == 2) {
                SetLabelIcon(MainFrame.strDestFlag, label);
            } else {
                if (i == 1) {
                    SetLabelIcon(MainFrame.strSrcFlag, label);
                } else {
                    //  Test if the current match is a 1:1 or m:n and display an
                    //  appropriate icon.
                    if ((oTemp[row]).getSrcSegmentNumber() == 1) {
                        SetLabelIcon(MainFrame.strImagePath + "gifs/1_1.gif", label);
                    } else {
                        SetLabelIcon(MainFrame.strImagePath + "gifs/m_n.gif", label);
                    }
                }
            }

            iconPanel.add(label);
            label.setBounds(0, 0, 20, 20);
            label.setBorder(null);

            int high = p.getPreferredSize().height;
            iconPanel.setBounds(0, curHight, 20, high);
            src.setBounds(20, curHight, getInitialWidth(table, column) - 20, high); //src.getPreferredSize().width,src.getPreferredSize().height);
            panel.add(iconPanel);
            panel.add(src);
            curHight += (high + 5);

            if (i != (data.size() - 1)) {
                curHight += 0;
            }

            if (isSelected) {
                p.setForeground(table.getSelectionForeground());
                iconPanel.setForeground(table.getSelectionForeground());
                iconPanel.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
                label.setBackground(table.getSelectionBackground());
                panel.setForeground(table.getSelectionForeground());
                panel.setBackground(table.getSelectionBackground());
            } else {
                p.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                iconPanel.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                iconPanel.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                label.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                label.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
                panel.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
                panel.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
            }
        }

        table.setRowHeight(row, (curHight == 0) ? 1 : (curHight + 10));
        panel.setWidth(getInitialWidth(table, column));

        //highlightDiff(table,row);
        //highlightDiff(table,row,1);
        return panel;
    }

    /**
     *  This method builds up the source string to display in the match window.
     *  The method pulls together the source text strings for the
     *  source segments starting with the segment at index iSelectedRow and the
     *  following elements. iNumSegsInMatchSrc segments are concatenated in total.
     *  @param iSelectedRow The index of the selected row in the main window. Matches for this row are displayed in the match window.
     *  @param iNumSegsInMatchSrc The number of segments in the match, that corresponds to the rendering component being created.
     *  @return A string representing the full source text.
     */
    protected String buildFullSourceText(int iSelectedRow, int iNumSegsInMatchSrc) {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        boolean boolEndsWithSpace = false;

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        while ((i < iNumSegsInMatchSrc) && ((i + iSelectedRow) < tmpdata.tmsentences.length)) {
            //  Add a space to the end of the previous segment if there isn't one
            //  there already.
            if ((i > 0) && !boolEndsWithSpace) {
                buffer.append(" ");
            }

            buffer.append(tmpdata.tmsentences[(i + iSelectedRow)].getSource());
            boolEndsWithSpace = (tmpdata.tmsentences[(i + iSelectedRow)].getSource()).endsWith(" ");
            i++;
        }

        return buffer.toString();
    }

    /**
     *  This method concatenates the base elements for the various source strings
     *  involved in the match. The method pulls together the base elements for the
     *  source segments starting with the segment at index iSelectedRow and the
     *  following elements. iNumSegsInMatchSrc segments are concatenated in total.
     *  @param iSelectedRow The index of the selected row in the main window. Matches for this row are displayed in the match window.
     *  @param iNumSegsInMatchSrc The number of segments in the match, that corresponds to the rendering component being created.
     *  @return An object array containing all the base elements for all the source segments that are involved in the match.
     */
    protected PivotBaseElement[] concatenateSourceBaseElementsArray(int iSelectedRow, int iNumSegsInMatchSrc) {
        //  Add arrays to a list and then get the list to dump out the whole lot
        //  as an array again. Lots of mucking around with Collections.
        java.util.List list = new LinkedList();

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        int i = 0;

        while ((i < iNumSegsInMatchSrc) && ((i + iSelectedRow) < tmpdata.tmsentences.length)) {
            PivotBaseElement[] baseElems = tmpdata.tmsentences[(i + iSelectedRow)].getSourceBaseElements();

            for (int j = 0; j < baseElems.length; j++) {
                list.add(baseElems[j]);
            }

            i++;
        }

        return (PivotBaseElement[])list.toArray(new PivotBaseElement[0]);
    }

    public void highlightDiff(JTable table, int row) {
        if (MiniTMTableFrame.diff == null) {
            return;
        }

        MiniTMTextPane newSrcPane = getMiniTMTableFrame(table).editPanes[row][0];
        MiniTMTextPane oldSrcPane = getMiniTMTableFrame(table).editPanes[row][1];

        if (row != MiniTMTableFrame.currentRow) {
            try {
                newSrcPane.setStyle();
                oldSrcPane.setStyle();
            } catch (Exception ex) {
                newSrcPane.elements = org.jvnet.olt.editor.util.BaseElements.extractContent(newSrcPane.getText());
                newSrcPane.setStyle();

                oldSrcPane.elements = org.jvnet.olt.editor.util.BaseElements.extractContent(oldSrcPane.getText());
                oldSrcPane.setStyle();
            }

            return;
        }

        //if(!MiniTMTableFrame.needRefresh) return;
        //logger.finer("repaint---row="+row);
        synchronized (MiniTMTableFrame.obj) {
            Iterator it = MiniTMTableFrame.diff.iterator();

            while (it.hasNext()) {
                org.jvnet.olt.editor.util.WordOp op = (org.jvnet.olt.editor.util.WordOp)it.next();
                int type = op.getType();
                int srcStart;
                int srcEnd;
                int tgtStart;
                int tgtEnd;

                switch (type) {
                case org.jvnet.olt.editor.util.WordOp.CHANGE:
                    srcStart = op.getNewPos();
                    srcEnd = srcStart + op.getNewWord().length();

                    if (op.getNewWord().equals("\n") || op.getNewWord().equals(" ") || op.getNewWord().equals("\t")) {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.whiteSpaceReplaceStyle);
                    } else {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.replaceStyle);
                    }

                    tgtStart = op.getOldPos();
                    tgtEnd = tgtStart + op.getOldWord().length();

                    if (op.getOldWord().equals("\n") || op.getOldWord().equals(" ") || op.getOldWord().equals("\t")) {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.whiteSpaceReplaceStyle);
                    } else {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.replaceStyle);
                    }

                    break;

                case org.jvnet.olt.editor.util.WordOp.DELETE:
                    tgtStart = op.getOldPos();
                    tgtEnd = tgtStart + op.getOldWord().length();

                    if (op.getOldWord().equals("\n") || op.getOldWord().equals(" ") || op.getOldWord().equals("\t")) {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.whiteSpaceDeletionStyle);
                    } else {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.deletionStyle);
                    }

                    break;

                case org.jvnet.olt.editor.util.WordOp.INSERT:
                    srcStart = op.getNewPos();
                    srcEnd = srcStart + op.getNewWord().length();

                    if (op.getNewWord().equals("\n") || op.getNewWord().equals(" ") || op.getNewWord().equals("\t")) {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.whiteSpaceInsertionStyle);
                    } else {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.insertionStyle);
                    }

                    break;

                case org.jvnet.olt.editor.util.WordOp.MOVE:
                    srcStart = op.getNewPos();
                    srcEnd = srcStart + op.getNewWord().length();

                    if (op.getNewWord().equals("\n") || op.getNewWord().equals(" ") || op.getNewWord().equals("\t")) {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.whiteSpaceChangePositionStyle);
                    } else {
                        newSrcPane.select(srcStart, srcEnd, PivotTextPane.changePositionStyle);
                    }

                    tgtStart = op.getOldPos();
                    tgtEnd = tgtStart + op.getOldWord().length();

                    if (op.getOldWord().equals("\n") || op.getOldWord().equals(" ") || op.getOldWord().equals("\t")) {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.whiteSpaceChangePositionStyle);
                    } else {
                        oldSrcPane.select(tgtStart, tgtEnd, PivotTextPane.changePositionStyle);
                    }

                    break;
                }

                ;
            }

            MiniTMTableFrame.needRefresh = false;
        }

        ;
    }

    private int getInitialWidth(JTable table, int column) {
        TableColumn tc = table.getColumn(table.getColumnName(column));

        return tc.getWidth();
    }

    private int getInitialHeight(JTable table, int row) {
        return table.getRowHeight(row);
    }

    public void SetLabelIcon(String strGifName, JLabel jlabel) {
        ///logger.finer(strGifName);
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(strGifName));
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(18, 12, Image.SCALE_SMOOTH));
        jlabel.setIcon(imageIcon);
    }
}
