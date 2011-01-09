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

import javax.swing.*;
import javax.swing.table.*;

import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.editor.util.*;


public class PivotTextRender1 implements TableCellRenderer {
    public static final int ROW_COUNT = 25;
    private PivotTextPane srcPivotPane;
    private PivotTextPane tgtPivotPane;

    public PivotTextRender1() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int iHeight;
        int iWidth;
        boolean returnDirectly = false;

        String data = (String)value;
        iWidth = table.getColumnModel().getColumn(column).getWidth();

        if (isSelected) {
            boolean isSrc = (table == AlignmentMain.testMain1.tableView);

            if (isSrc) {
                if ((srcPivotPane == null)) {
                    srcPivotPane = buildPivotTextPane(table, iWidth, data, row, true);
                }

                adjustSizeAndText(srcPivotPane, iWidth, table, row, isSrc, data);

                return srcPivotPane;
            } else {
                if ((tgtPivotPane == null)) {
                    tgtPivotPane = buildPivotTextPane(table, iWidth, data, row, false);
                }

                adjustSizeAndText(tgtPivotPane, iWidth, table, row, isSrc, data);

                return tgtPivotPane;
            }
        }

        JTextLabel p = null;

        if (table == AlignmentMain.testMain1.tableView) { //source table

            if (AlignmentMain.editPanes[row % (ROW_COUNT)] == null) { //init
                AlignmentMain.editPanes[row % (ROW_COUNT)] = new JTextLabel(data, iWidth, isSelected, table.getSelectionBackground(), table.getBackground());
                p = AlignmentMain.editPanes[row % (ROW_COUNT)];
                p.setBorder(null);
            } else {
                p = AlignmentMain.editPanes[row % (ROW_COUNT)];

                if (p.getText().equals(data)) { //data no changed

                    if (iWidth == p.getWidth()) {
                        int maxH = getMaxHeight(table, row, column, p.getHeight());
                        table.setRowHeight(row, maxH);
                        returnDirectly = true;

                        //setColor(p,isSelected);
                        //return p;
                    }
                }
            }
        } else { //target table

            if (AlignmentMain.editPanes[(row % (ROW_COUNT)) + ROW_COUNT] == null) { //init
                AlignmentMain.editPanes[(row % (ROW_COUNT)) + ROW_COUNT] = new JTextLabel(data, iWidth, isSelected, table.getSelectionBackground(), table.getBackground());
                p = AlignmentMain.editPanes[(row % (ROW_COUNT)) + ROW_COUNT];
                p.setBorder(null);
            } else {
                p = AlignmentMain.editPanes[(row % (ROW_COUNT)) + ROW_COUNT];

                if (p.getText().equals(data)) { //data no changed

                    if (iWidth == p.getWidth()) {
                        int maxH = getMaxHeight(table, row, column, p.getHeight());
                        table.setRowHeight(row, maxH);
                        returnDirectly = true;

                        //setColor(p,isSelected);
                        //return p;
                    }
                }
            }
        }

        if (!returnDirectly) {
            p.setWidth(iWidth);
            p.setContent(data);

            int curHight = p.getPreferredSize().height;

            if (!isSelected) {
                setRowHeight(table, row, column, curHight);
            }

            iHeight = p.getHeight();

            if (iHeight != table.getRowHeight(row)) {
                p.setHeight(table.getRowHeight(row));
            }
        }

        if (isSelected) {
            setColor(p, isSelected);
        } else {
            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            if (tmpdata.getGroupTrack().isShownAsGrouped()) {
                String id = tmpdata.tmsentences[row].getTransUintID();
                int iRet = tmpdata.getGroupTrack().isTransunitIdInGroup(id);

                if (iRet != -1) {
                    int size = tmpdata.getGroupTrack().getSizeOfOneGroup(id);

                    String selId = tmpdata.tmsentences[table.getSelectedRow()].getTransUintID();
                    boolean sameGroup = tmpdata.getGroupTrack().inTheSameGroup(id, selId);

                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size)) && sameGroup) {
                        setColor(p, true);
                    } else {
                        setColor(p, false);
                    }
                } else {
                    setColor(p, false);
                }
            } else {
                if ((MiniTMTableFrame.matchesObject.length > 0) && (MiniTMTableFrame.currentRow != -1) && ((MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber() > 1)) {
                    int size = (MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber();

                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size))) {
                        setColor(p, true);
                    } else {
                        setColor(p, false);
                    }
                } else {
                    setColor(p, false);
                }
            }
        }

        return p;
    }

    private void setColor(JTextLabel p, boolean isSelected) {
        p.setSelectStatus(isSelected);
    }

    /**
     *  This method builds a PivotTextPane object to render the
     */
    protected PivotTextPane buildPivotTextPane(JTable table, int iWidth, String data, int row, boolean isSrc) {
        //  Create it
        PivotTextPane p = new PivotTextPane(table, row, 0, isSrc);
        p.setBorder(null);

        return p;
    }

    protected void adjustSizeAndText(PivotTextPane p, int iWidth, JTable table, int row, boolean isSrc, String data) {
        p.sizePivotTextPane(iWidth, table.getRowHeight(row));
        p.setBackground(table.getSelectionBackground());

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        //  Fill it with data
        if (isSrc) {
            p.setContent(data, tmpdata.tmsentences[row].getSourceBaseElements());
        } else {
            p.setContent(data, tmpdata.tmsentences[row].getTranslationBaseElements());
        }
    }

    public int getMaxHeight(JTable table, int row, int column, int curHight) {
        JTable otherTable = AlignmentMain.testMain1.tableView;

        int otherHight = getOtherTablePreferredHeight((otherTable == table) ? false : true, row, column); //+10;

        int maxHight = (curHight > otherHight) ? curHight : otherHight;

        return maxHight;
    }

    private void setRowHeight(JTable table, int row, int column, int curHight) {
        JTable otherTable = AlignmentMain.testMain1.tableView;

        int otherHight = getOtherTablePreferredHeight((otherTable == table) ? false : true, row, column); //+10;

        int maxHight = (curHight > otherHight) ? curHight : otherHight;

        if (table.getRowHeight(row) != maxHight) {
            table.setRowHeight(row, maxHight);

            if (table == AlignmentMain.testMain1.tableView) {
                AlignmentMain.testMain2.tableView.setRowHeight(row, maxHight);
            } else {
                AlignmentMain.testMain1.tableView.setRowHeight(row, maxHight);
            }
        }
    }

    public int getOtherTablePreferredHeight(boolean isSource, int row, int column) {
        int high = 0;

        if (isSource) {
            JTable table = AlignmentMain.testMain1.tableView;
            JTextLabel p = AlignmentMain.editPanes[row % (ROW_COUNT)];
            String temp = null;

            if (table.isEditing() && (table.getEditingColumn() == column) && (table.getEditingRow() == row)) {
                temp = (String)((SourceTableModel)table.getModel()).getEditingValueAt(row, column);
            } else {
                temp = (String)table.getValueAt(row, column);
            }

            if (p == null) {
                p = new JTextLabel(table.getColumnModel().getColumn(column).getWidth());
                p.setBorder(null);
            } else {
                if (p.getText().equals(temp)) {
                    return p.getHeight();
                }
            }

            p.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));
            p.setContent(temp);
        } else {
            int row1 = (row % (ROW_COUNT)) + ROW_COUNT;
            JTable table = AlignmentMain.testMain2.tableView;
            JTextLabel p = AlignmentMain.editPanes[row1];
            String temp = null;

            if (table.isEditing() && (table.getEditingColumn() == column) && (table.getEditingRow() == row)) {
                temp = (String)((TargetTableModel)table.getModel()).getEditingValueAt(row, column);
            } else {
                temp = (String)table.getValueAt(row, column);
            }

            if (p == null) {
                p = new JTextLabel(table.getColumnModel().getColumn(column).getWidth());
                p.setBorder(null);
            } else {
                if (p.getText().equals(temp)) {
                    return p.getHeight();
                }
            }

            p.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));

            p.setWidth(table.getColumnModel().getColumn(column).getWidth());
            p.setContent(temp);

            high = p.getPreferredSize().height;

            //      p.setHeight(high);   Don't ever uncomment this line if you want the performance to be good.
        }

        return high;
    }
}
