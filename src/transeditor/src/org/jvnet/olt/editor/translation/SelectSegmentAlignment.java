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
import javax.swing.event.*;
import javax.swing.table.*;

import org.jvnet.olt.editor.util.Bundle;


public class SelectSegmentAlignment extends JPanel {
    private Bundle bundle = Bundle.getBundle(SelectSegmentAlignment.class.getName());
    public static int iSentenceCount;
    public static MiniTMPivotTextPane[] editPanes = new MiniTMPivotTextPane[PivotTextRender1.ROW_COUNT * 2];

    //public static Color DEFAULT_BACK_GROUND = new Color(250,250,240);
    public final static int SHOW_ALL_TAGS = 1;
    public final static int SHOW_ABBREVIATE_TAGS = 2;
    public final static int SHOW_PURE_TEXT = 3;
    public static int viewTagsStatus = 1; //1,2,3
    public static int curRow = -1;
    public static int showType = 1;
    public static SelectSegmentAlignment testMain = null;
    public static Object[] data = new Object[0];
    public static boolean bFlag = false;

    /**
     * cellRender
     */

    //public static JLabel translator = new JLabel();
    public static JPanel translatorPanel = null;
    TableModel dataModel = null;
    ListSelectionModel listSelectionModel = null;
    SelectMiniTMTable tableView = null;
    JScrollPane scrollPane;
    TableColumn column2;
    Color backColor = Color.red;

    class SelectMiniTMTable extends JTable {
        public SelectMiniTMTable(TableModel t) {
            super(t);
            this.setBorder(null);
        }

        public void paint(Graphics g) {
            try {
                super.paint(g);
            } catch (Exception ex) {
            }
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (direction == 1) { //page down

                return remainH();
            } else { //page up

                Point p = scrollPane.getViewport().getViewPosition();

                if (p.y == 0) {
                    return 0;
                }

                int visibleH = SelectSegmentAlignment.this.getHeight();

                return (visibleH > p.y) ? p.y : visibleH;
            }
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (direction == 1) { //down

                int lastVisibleRow = getLastVisibleRow();

                if (lastVisibleRow == -1) {
                    return 0;
                }

                Rectangle r = getCellRect(lastVisibleRow, showType, true);

                if (visibleRect.contains(r)) {
                    if (lastVisibleRow == (getRowCount() - 1)) {
                        return 0;
                    } else {
                        return (int)getCellRect(lastVisibleRow + 1, showType, true).getHeight();
                    }
                } else {
                    return (int)((r.getY() + r.getHeight()) - visibleRect.getY() - visibleRect.getHeight());
                }
            } else { //up

                int firstVisibleRow = getFirstVisibleRow();
                Rectangle r = getCellRect(firstVisibleRow, showType, true);

                if (visibleRect.contains(r)) {
                    if (firstVisibleRow == 0) {
                        return 0;
                    } else {
                        return (int)getCellRect(firstVisibleRow - 1, showType, true).getHeight();
                    }
                } else {
                    return (int)(visibleRect.getY() - r.getY());
                }
            }
        }
    }

    /**
     * table model
     */
    class SelectMiniTMTableModel implements TableModel {
        String[] header = { bundle.getString("translator"), bundle.getString("source"), bundle.getString("translation") };

        public int getRowCount() {
            if (data == null) {
                return 0;
            }

            return data.length;
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int row, int column) {
            Object o = null;

            if (column == 0) {
                return ((org.jvnet.olt.minitm.TMMatch)data[row]).getTranslatorID();
            } else if (column == 1) {
                return ((org.jvnet.olt.minitm.TMMatch)data[row]).getSource();
            } else if (column == 2) {
                return ((org.jvnet.olt.minitm.TMMatch)data[row]).getTranslation();
            }

            /*if(row == (3*(row/3)+2)) return o;
            else if(row == 3*(row/3)) {//source
              if(column == 0) return MainFrame.sourceLan;
              else
                o = ((org.jvnet.olt.minitm.AlignedSegment)data[row/3]).getSource();
            }else {//translation
              if(column == 0) return MainFrame.targetLan;
              else
                o = ((org.jvnet.olt.minitm.AlignedSegment)data[row/3]).getTranslation();
            }*/
            return o;
        }

        public String getColumnName(int col) {
            return header[col];
        }

        public Class getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
        }

        public void removeTableModelListener(TableModelListener ls) {
        }

        public void addTableModelListener(TableModelListener ls) {
        }
    } //end of table model

    class SelectMiniTMTranslatorRender extends JLabel implements TableCellRenderer {
        protected boolean selected;
        Color selectedBackgroundColor = null;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String lan = ((StringBuffer)value).toString();

            if (selectedBackgroundColor == null) {
                selectedBackgroundColor = table.getSelectionBackground();
            }

            //JLabel label = null;
            if (lan != null) {
                setText(lan);
            }

            this.selected = isSelected;

            return this;
        }

        public int getHorizontalAlignment() {
            return JLabel.CENTER;
        }

        public void paint(Graphics g) {
            Color bColor;

            if (selected) {
                bColor = selectedBackgroundColor;
            } else if (getParent() != null) {
                bColor = getParent().getBackground();
            } else {
                bColor = getBackground();
            }

            g.setColor(bColor);
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            super.paint(g);
        }
    }

    class SelectSegmentMiniTMRender implements TableCellRenderer {
        public static final int hb = 2;
        int w;
        int h;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            MiniTMPivotTextPane p = null;
            String content = ((StringBuffer)value).toString();
            w = getInitialWidth(table, column);
            h = table.getRowHeight(row);

            if (column == 1) { //source

                int row1 = row;

                if (editPanes[row1 % (PivotTextRender1.ROW_COUNT)] == null) { //init
                    editPanes[row1 % (PivotTextRender1.ROW_COUNT)] = new MiniTMPivotTextPane(table, row1 % (PivotTextRender1.ROW_COUNT), 0, true);
                    p = editPanes[row1 % (PivotTextRender1.ROW_COUNT)];
                    p.setBorder(null);
                } else {
                    p = editPanes[row1 % (PivotTextRender1.ROW_COUNT)];

                    if (p.getText().equals(content)) { //data no changed

                        if (w == p.getWidth()) {
                            int maxH = getMaxHeight(table, row, column, p.getHeight1() + hb);

                            if ((maxH) != h) {
                                table.setRowHeight(row, maxH);
                            }

                            setColor(table, p, isSelected);

                            //p.setForeground(backColor);
                            return p;
                        }
                    }
                }
            } else { //target

                int row1 = row;

                if (editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT] == null) { //init
                    editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT] = new MiniTMPivotTextPane(table, row1 % (PivotTextRender1.ROW_COUNT), 0, false);
                    p = editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT];
                    p.setBorder(null);
                } else {
                    p = editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT];

                    if (p.getText().equals(content)) { //data no changed

                        if (w == p.getWidth()) {
                            int maxH = getMaxHeight(table, row, column, p.getHeight1() + hb);

                            if ((maxH) != h) {
                                table.setRowHeight(row, maxH);
                            }

                            /*if(h != p.getHeight1()+hb) {

                              table.setRowHeight(row,(int)p.getHeight1()+hb);
                              p.setContent(content,null);
                            }*/
                            setColor(table, p, isSelected);

                            //p.setForeground(backColor);
                            return p;
                        }
                    }
                }
            }

            p.setSize(getInitialWidth(table, column), getInitialHeight(table, row));
            p.setContent(content, null);
            p.setWidth(w);
            p.setHeight(p.getPreferredSize().height);

            int curHight = p.getPreferredSize().height + hb;
            setRowHeight(table, row, column, curHight);

            //p.setForeground(backColor);
            return p;
        }

        private int getInitialWidth(JTable table, int column) {
            TableColumn tc = table.getColumn(table.getColumnName(column));

            return tc.getWidth();
        }

        private int getInitialHeight(JTable table, int row) {
            return table.getRowHeight(row);
        }

        private void setColor(JTable table, MiniTMPivotTextPane p, boolean isSelected) {
            if (isSelected) {
                p.setBackground(table.getSelectionBackground());
            } else {
                p.setBackground(table.getBackground());
            }
        }

        public int getMaxHeight(JTable table, int row, int column, int curHight) {
            int otherHight = getOtherTablePreferredHeight((column == 1) ? false : true, row, column) + hb;

            int maxHight = (curHight > otherHight) ? curHight : otherHight;

            return maxHight;
        }

        private void setRowHeight(JTable table, int row, int column, int curHight) {
            int otherHight = getOtherTablePreferredHeight((column == 1) ? false : true, row, column) + hb;

            int maxHight = (curHight > otherHight) ? curHight : otherHight;

            if (table.getRowHeight(row) != maxHight) {
                table.setRowHeight(row, maxHight);
            }
        }

        public int getOtherTablePreferredHeight(boolean isSource, int row, int column) {
            int high = 0;

            if (isSource) {
                MiniTMPivotTextPane p = editPanes[row % (PivotTextRender1.ROW_COUNT)];
                String temp = null;
                temp = ((StringBuffer)tableView.getValueAt(row, 1)).toString();

                if (p == null) {
                    p = new MiniTMPivotTextPane(tableView, row % (PivotTextRender1.ROW_COUNT), 0, true);
                    p.setBorder(null);
                } else {
                    if (p.getText().equals(temp)) {
                        return p.getHeight1();
                    }
                }

                p.setSize(getInitialWidth(tableView, column), tableView.getRowHeight(row));
                p.setContent(temp, null);
                p.setWidth(w);
                high = p.getPreferredSize().height;
                p.setHeight(high);
            } else {
                int row1 = (row % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT;
                MiniTMPivotTextPane p = editPanes[row1];
                String temp = null;
                temp = ((StringBuffer)tableView.getValueAt(row, 2)).toString();

                if (p == null) {
                    p = new MiniTMPivotTextPane(tableView, row % (PivotTextRender1.ROW_COUNT), 0, false);
                    p.setBorder(null);
                } else {
                    if (p.getText().equals(temp)) {
                        return p.getHeight1();
                    }
                }

                p.setSize(getInitialWidth(tableView, column), tableView.getRowHeight(row));

                p.setWidth(w);
                p.setContent(temp, null);

                high = p.getPreferredSize().height;
                p.setHeight(high);
            }

            return high;
        }
    } //end of cellRender

    public SelectSegmentAlignment() {
        super();
        this.setLayout(new BorderLayout());
        testMain = this;

        showData();
    }

    void showData() {
        dataModel = new SelectMiniTMTableModel();
        tableView = new SelectMiniTMTable(dataModel);

        tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableView.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        tableView.setShowHorizontalLines(true);
        tableView.setShowVerticalLines(true);

        //tableView.setIntercellSpacing(new Dimension(2,-2));
        //tableView.setShowGrid(true);
        TableColumn column = tableView.getColumnModel().getColumn(0);
        column.setMaxWidth(100);
        column.setMinWidth(20);
        column.setPreferredWidth(80);

        //column.setResizable(false);
        tableView.getTableHeader().setReorderingAllowed(false);
        column.setCellRenderer(new SelectMiniTMTranslatorRender());

        column2 = tableView.getColumnModel().getColumn(1);

        SelectSegmentMiniTMRender render = new SelectSegmentMiniTMRender();
        column2.setCellRenderer(render);
        tableView.getColumnModel().getColumn(2).setCellRenderer(render);

        scrollPane = new JScrollPane(tableView);
        tableView.setRowSelectionAllowed(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add("Center", scrollPane);
    }

    public void repaintSelf(int oldSegNum) {
        curRow = -1;
        tableView.clearSelection();

        int newSegNum = dataModel.getRowCount();

        if (newSegNum != 0) {
            tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum - 1));

            tableView.repaint();
        }

        if (newSegNum > 0) {
            tableView.setRowSelectionInterval(0, 0);
        }

        tableView.invalidate();
        tableView.updateUI();
    }

    public void repaintSelf() {
        int newSegNum = dataModel.getRowCount();

        if (newSegNum != 0) {
            tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum - 1));

            tableView.repaint();
        }

        tableView.updateUI();
    }

    private int getFirstVisibleRow() {
        Point p = scrollPane.getViewport().getViewPosition();

        return tableView.rowAtPoint(p);
    }

    private int getLastVisibleRow() {
        Point p = scrollPane.getViewport().getViewPosition();
        int visibleH = (int)getHeight();
        p.y += visibleH;

        return tableView.rowAtPoint(p);
    }

    public void pageUp() {
        Point p = scrollPane.getViewport().getViewPosition();

        if (p.y == 0) {
            return;
        }

        int visibleH = this.getHeight();
        int min = (visibleH > p.y) ? p.y : visibleH;
        p.y = p.y - min;
        scrollPane.getViewport().setViewPosition(p);
        tableView.repaint();
    }

    private int remainH() {
        int lastVisibleRow = getLastVisibleRow();
        Point p = scrollPane.getViewport().getViewPosition();
        int visibleH = this.getHeight();

        Rectangle lastVisibleRowRect = tableView.getCellRect(lastVisibleRow, showType, true);
        Rectangle lastRowRect = tableView.getCellRect(tableView.getRowCount() - 1, showType, true);
        int h = (int)((lastRowRect.getY() + lastRowRect.getHeight()) - p.y - visibleH);

        if (h > visibleH) {
            return visibleH;
        }

        return h;
    }

    public void pageDown() {
        Rectangle visibleRect = tableView.getVisibleRect();
        Rectangle lastRowRect = tableView.getCellRect(tableView.getRowCount() - 1, showType, true);

        if (visibleRect.contains(lastRowRect)) {
            return;
        }

        int visibleH = this.getHeight();
        int h = remainH();

        Point p = scrollPane.getViewport().getViewPosition();
        int min = (visibleH > h) ? h : visibleH;
        p.y = p.y + min;
        scrollPane.getViewport().setViewPosition(p);
        tableView.repaint();
    }
}
