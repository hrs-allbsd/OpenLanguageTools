/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.basic.BasicScrollBarUI.*;
import javax.swing.table.*;

import org.jvnet.olt.editor.model.*;


public class MiniTMAlignmentMain extends JPanel {
    private static final Logger logger = Logger.getLogger(MiniTMAlignmentMain.class.getName());
    public static int iSentenceCount;
    public static MiniTMPivotTextPane[] editPanes = new MiniTMPivotTextPane[PivotTextRender1.ROW_COUNT * 2];

    //public static Color DEFAULT_BACK_GROUND = new Color(250,250,240);
    public final static int SHOW_ALL_TAGS = 1;
    public final static int SHOW_ABBREVIATE_TAGS = 2;
    public final static int SHOW_PURE_TEXT = 3;
    public static int viewTagsStatus = 1; //1,2,3
    public static int curRow = -1;
    public static int showType = 1;
    public static MiniTMAlignmentMain testMain = null;
    public static Object[] data = new Object[0];
    public static boolean bFlag = false;
    public static Vector modifiedSegments = new Vector();

    private boolean  readOnly = true;
    /**
     * cellRender
     */
    public static JPanel NullPanel = new JPanel() {
        public Color getBackground() {
            return Color.lightGray;
        }
    };

    public static Hashtable labelBuf = new Hashtable();

    /**
     * cellEditor
     */
    public static MiniTMPivotTextPane sourceEditor = null;
    public static MiniTMPivotTextPane targetEditor = null;
    TableModel dataModel = null;
    ListSelectionModel listSelectionModel = null;
    MiniTMTable tableView = null;
    JScrollPane scrollPane;
    TableColumn column2;

    class MiniTMTable extends JTable {
        public MiniTMTable(TableModel t) {
            super(t);
            this.setBorder(null);
        }

        public void paint(Graphics g) {
            try {
                super.paint(g);
            } catch (Exception ex) {
            }
        }

        public void setRowSelectionInterval1(int rowStart, int rowEnd) {
            try {
                super.setRowSelectionInterval(rowStart, rowEnd);
                selectRow(rowStart);
            } catch (Exception ex) {
            }
        }

        public Dimension getIntercellSpacing() {
            return new Dimension(0, 0);
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (direction == 1) { //page down

                return remainH();
            } else { //page up

                Point p = scrollPane.getViewport().getViewPosition();

                if (p.y == 0) {
                    return 0;
                }

                int visibleH = MiniTMAlignmentMain.this.getHeight();

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
    class MiniTMTableModel implements TableModel {
        String[] header = { "FLAG", "Content" };

        public int getRowCount() {
            if (data == null) {
                return 0;
            }

            return 3 * data.length;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int column) {
            Object o = null;

            if (row == ((3 * (row / 3)) + 2)) {
                return o;
            } else if (row == (3 * (row / 3))) { //source

                if (column == 0) {
                    return Backend.instance().getProject().getSrcLang();
                } else {
                    o = ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getSource();
                }
            } else { //translation

                if (column == 0) {
                    return Backend.instance().getProject().getTgtLang();
                } else {
                    o = ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslation();
                }
            }

            return o;
        }

        public String getColumnName(int col) {
            return header[col];
        }

        public Class getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return (col == 1) && !(row == ((3 * (row / 3)) + 2));
            
        }

        public void setValueAt(Object value, int row, int col) {
            if(readOnly)
                return ;
            
            if (col == 0) {
                return;
            }

            if (row == ((3 * (row / 3)) + 2)) {
                return;
            } else if (row == (3 * (row / 3))) { //source

                if (((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getSource().equals((String)value)) {
                    return;
                }

                String temp = String.valueOf(row / 3);

                if (!modifiedSegments.contains(temp)) {
                    modifiedSegments.addElement(temp);

                    //
                    org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment((String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslation(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), 0L - ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                    data[row / 3] = null; //for gc
                    data[row / 3] = seg;
                } else {
                    if (((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey() > 0L) {
                        //data[row/3] = null;//for gc
                        org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment((String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslation(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), 0L - ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                        data[row / 3] = null; //for gc
                        data[row / 3] = seg;
                    } else {
                        //data[row/3] = null;//for gc
                        org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment((String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslation(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                        data[row / 3] = null; //for gc
                        data[row / 3] = seg;
                    }
                }
            } else { //translation

                if (((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslation().equals((String)value)) {
                    return;
                }

                String temp = String.valueOf(row / 3);

                if (!modifiedSegments.contains(temp)) {
                    modifiedSegments.addElement(temp);

                    //data[row/3] = null;//for gc
                    org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment(((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getSource(), (String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                    data[row / 3] = null; //for gc
                    data[row / 3] = seg;
                } else {
                    if (((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey() > 0L) {
                        //data[row/3] = null;//for gc
                        org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment(((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getSource(), (String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                        data[row / 3] = null; //for gc
                        data[row / 3] = seg;
                    } else {
                        //data[row/3] = null;//for gc
                        org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment(((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getSource(), (String)value, ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getTranslatorID(), ((org.jvnet.olt.minitm.AlignedSegment)data[row / 3]).getDataStoreKey());
                        data[row / 3] = null; //for gc
                        data[row / 3] = seg;
                    }
                }
            }
        }

        public void removeTableModelListener(TableModelListener ls) {
        }

        public void addTableModelListener(TableModelListener ls) {
        }
    } //end of table model

    class MiniTMFlagRender implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row == ((3 * (row / 3)) + 2)) {
                return NullPanel;
            }

            if (value == null) {
                return NullPanel;
            }

            JPanel panel = new JPanel();
            panel.setLayout(null);

            String lan = (String)value;

            JLabel label = null;

            if (labelBuf.get(lan) == null) {
                //bug 4758110
                //ImageIcon imageIcon = new ImageIcon(getClass().getResource(org.jvnet.olt.editor.util.Languages.getFlagPath(lan)));
                ImageIcon imageIcon = null;

                try {
                    imageIcon = new ImageIcon(getClass().getResource(org.jvnet.olt.editor.util.Languages.getFlagPath(lan)));
                } catch (Exception e) {
                    logger.throwing(getClass().getName(), "getTableCellRendererComponent", e);
                    logger.severe("Exception:" + e);
                }

                //          ImageIcon imageIcon = new ImageIcon(getClass().getResource(org.jvnet.olt.editor.util.Languages.getFlagPath(lan)));
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(18, 12, Image.SCALE_SMOOTH));
                label = new JLabel(imageIcon);
                label.setBounds(0, 0, 20, 20);
                panel.add(label);
                labelBuf.put(lan, label);
            } else {
                label = (JLabel)labelBuf.get(lan);
                label.setBounds(0, 0, 20, 20);
                panel.add(label);
                labelBuf.put(lan, label);
            }

            panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);

            return panel;
        }
    }

    class MaintainMiniTMRender implements TableCellRenderer {
        public static final int hb = 2;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row == ((3 * (row / 3)) + 2)) {
                if (row == (table.getRowCount() - 1)) {
                    if (table.getRowHeight(row) != 1) {
                        table.setRowHeight(row, 1);
                    }
                } else {
                    if (table.getRowHeight(row) != 5) {
                        table.setRowHeight(row, 5);
                    }
                }

                return NullPanel;
            }

            if (value == null) {
                return NullPanel;
            }

            MiniTMPivotTextPane p = null;
            String content = (String)value;

            int w = getInitialWidth(table, column);
            int h = table.getRowHeight(row);

            if (row == (3 * (row / 3))) { //source

                int row1 = row / 3;

                if (editPanes[row1 % (PivotTextRender1.ROW_COUNT)] == null) { //init
                    editPanes[row1 % (PivotTextRender1.ROW_COUNT)] = new MiniTMPivotTextPane(table, row1 % (PivotTextRender1.ROW_COUNT), 0, true);
                    p = editPanes[row1 % (PivotTextRender1.ROW_COUNT)];
                    p.setBorder(null);                    
                } else {
                    p = editPanes[row1 % (PivotTextRender1.ROW_COUNT)];

                    if ((table.getEditingRow() == row) && (table.getEditingColumn() == column) && p.getText().equals(content)) {
                        if (w == p.getWidth()) {
                            if (h != (p.getHeight1() + hb)) {
                                table.setRowHeight(row, (int)p.getHeight1() + hb);
                                p.setContent(content, null);
                            }

                            selectForFound(table, row, p);

                            return p;
                        }
                    }

                    if (p.getText().equals(content)) { //data no changed

                        if (w == p.getWidth()) {
                            if (h != (p.getHeight1() + hb)) {
                                table.setRowHeight(row, (int)p.getHeight1() + hb);
                                p.setContent(content, null);
                            }

                            selectForFound(table, row, p);

                            return p;
                        }
                    }
                }
            } else { //target

                int row1 = row / 3;

                if (editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT] == null) { //init
                    editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT] = new MiniTMPivotTextPane(table, row1 % (PivotTextRender1.ROW_COUNT), 0, false);
                    p = editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT];
                    p.setBorder(null);
                } else {
                    p = editPanes[(row1 % (PivotTextRender1.ROW_COUNT)) + PivotTextRender1.ROW_COUNT];

                    if ((table.getEditingRow() == row) && (table.getEditingColumn() == column) && p.getText().equals(content)) {
                        if (w == p.getWidth()) {
                            if (h != (p.getHeight1() + hb)) {
                                table.setRowHeight(row, (int)p.getHeight1() + hb);
                                p.setContent(content, null);
                            }

                            selectForFound(table, row, p);

                            return p;
                        }
                    }

                    if (p.getText().equals(content)) { //data no changed

                        if (w == p.getWidth()) {
                            if (h != (p.getHeight1() + hb)) {
                                table.setRowHeight(row, (int)p.getHeight1() + hb);
                                p.setContent(content, null);
                            }

                            selectForFound(table, row, p);

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
            table.setRowHeight(row, curHight);
            selectForFound(table, row, p);

            return p;
        }

        private void selectForFound(JTable table, int row, MiniTMPivotTextPane pane) {
            Result r = FindDlgForMaintainence.result;

            if (r != null) {
                if (r.rowIndex == row) {
                    pane.select(r.position, r.position + r.search.what.length());
                }
            } else if (FindDlgForMaintainence.oldResult != null) {
                if (FindDlgForMaintainence.oldResult.rowIndex == row) {
                    pane.setStyle();
                    FindDlgForMaintainence.oldResult = null;
                }
            }
        }

        private int getInitialWidth(JTable table, int column) {
            TableColumn tc = table.getColumn(table.getColumnName(column));

            return tc.getWidth();
        }

        private int getInitialHeight(JTable table, int row) {
            return table.getRowHeight(row);
        }
    } //end of cellRender

    class MaintainMiniTMEditor extends AbstractCellEditor implements TableCellEditor {
        String data;
        int parentRowInitHight = -1;
        int parentRow = -1;
        public MyEditorDelegate delegate;
        protected int clickCountToStart = 1;
        JTable table;
        MiniTMPivotTextPane editor = null;

        protected class MyEditorDelegate implements KeyListener {
            public void keyPressed(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                possibleChangeTableHeight(e.getSource());
            }

            private void possibleChangeTableHeight(Object c) {
                MiniTMPivotTextPane editor = (MiniTMPivotTextPane)c;
                String temp = (String)table.getValueAt(table.getEditingRow(), table.getEditingColumn());

                if (editor.getText().equals(temp)) {
                    return;
                }

                int w = getInitialWidth(table, 1);
                int high = (int)editor.getPreferredSize().getHeight() + MaintainMiniTMRender.hb;
                table.setRowHeight(parentRow, high);
                editor.setWidth(w);
                editor.setHeight(high - MaintainMiniTMRender.hb);

                table.setValueAt(editor.getText(), parentRow, 1);
            }
        }

        public MaintainMiniTMEditor(JTable table) {
            super();
            this.table = table;
            delegate = new MyEditorDelegate();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            data = (String)value;

            parentRowInitHight = getInitialHeight(table, row);
            parentRow = row;

            if (row == (3 * (row / 3))) { //source

                if (sourceEditor == null) {
                    sourceEditor = new MiniTMPivotTextPane(table, row, 0, true);
                    sourceEditor.setBorder(null);
                }

                editor = sourceEditor;

                //TMInnerPanel.srcResult = null;
            } else {
                if (targetEditor == null) {
                    targetEditor = new MiniTMPivotTextPane(table, row, 0, false);
                    targetEditor.setBorder(null);
                }

                editor = targetEditor;
            }

            editor.setWidth(getInitialWidth(table, column));

            editor.setSize(getInitialWidth(table, column), parentRowInitHight - MaintainMiniTMRender.hb);
            editor.setContent(data, null);

            editor.addKeyListener(delegate);
            editor.addKeyListener(editor);
            editor.addMouseListener(editor);
            editor.addFocusListener(editor);
            editor.setEditable(!readOnly);

            return editor;
        }

        private int getInitialWidth(JTable table, int column) {
            TableColumn tc = table.getColumn(table.getColumnName(column));

            return tc.getWidth();
        }

        private int getInitialHeight(JTable table, int row) {
            return table.getRowHeight(row);
        }

        public void setClickCountToStart(int count) {
            clickCountToStart = count;
        }

        public int getClickCountToStart() {
            return clickCountToStart;
        }

        public Object getCellEditorValue() {
            return data;
        }

        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
            }

            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            if (editor != null) {
                data = editor.getText();

                if (delegate != null) {
                    editor.removeKeyListener(delegate);
                }

                editor.removeKeyListener(editor);
                editor.removeMouseListener(editor);
                editor.removeFocusListener(editor);
                editor.setEditable(false);
            }

            fireEditingStopped();

            return true;
        }
    } //end of editor

    public MiniTMAlignmentMain() {
        super();
        this.setLayout(new BorderLayout());
        testMain = this;

        showData();
    }

    void showData() {
        dataModel = new MiniTMTableModel();
        tableView = new MiniTMTable(dataModel);
        tableView.setTableHeader(null);
        tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableView.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        tableView.setShowHorizontalLines(false);

        tableView.setIntercellSpacing(new Dimension(0, -1));

        tableView.setShowGrid(false);

        TableColumn column = tableView.getColumnModel().getColumn(0);
        column.setMaxWidth(20);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setResizable(false);

        column.setCellRenderer(new MiniTMFlagRender());

        column2 = tableView.getColumnModel().getColumn(1);

        column2.setCellRenderer(new MaintainMiniTMRender());
        column2.setCellEditor(new MaintainMiniTMEditor(tableView));

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
            if (newSegNum == oldSegNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, showType, TableModelEvent.UPDATE));
            } else if (newSegNum > oldSegNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, showType, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, oldSegNum, newSegNum, 0, TableModelEvent.INSERT));
                tableView.tableChanged(new TableModelEvent(dataModel, oldSegNum, newSegNum, showType, TableModelEvent.INSERT));
            } else {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum - 1, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum - 1, showType, TableModelEvent.UPDATE));
            }

            tableView.repaint();
        }

        if (newSegNum > 0) {
            tableView.setRowSelectionInterval(0, 0);
        }

        tableView.updateUI();
    }

    public void repaintSelf() {
        int newSegNum = dataModel.getRowCount();

        if (newSegNum != 0) {
            tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum, 0, TableModelEvent.UPDATE));
            tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum, showType, TableModelEvent.UPDATE));
            tableView.repaint();
        }

        tableView.updateUI();
    }

    void stopEditing() {
        if (tableView.isEditing()) {
            tableView.getCellEditor(tableView.getEditingRow(), tableView.getEditingColumn()).stopCellEditing();
            tableView.repaint(tableView.getCellRect(tableView.getEditingRow(), tableView.getEditingColumn(), false));
        }
    }

    void startEditing() {
        int row = tableView.getSelectedRow();

        if (row != -1) {
            if (sourceEditor != null) {
                tableView.editCellAt(row, 1);
                sourceEditor.requestFocus();
                sourceEditor.setCaretPosition(0);
            }

            if (targetEditor != null) {
                tableView.editCellAt(row, 1);
                targetEditor.requestFocus();
                targetEditor.setCaretPosition(0);
            }
        }
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

    public void selectRow(int row) {
        int curSelectIndex = tableView.getSelectedRow();

        if (curSelectIndex != -1) {
            if (tableView.isEditing() && (row != curSelectIndex)) {
                JTable table = tableView;
                table.getCellEditor(curSelectIndex, table.getEditingColumn()).stopCellEditing();
            } else if (tableView.isEditing() && (row == curSelectIndex)) {
                JTable table = tableView;
                table.getCellEditor(curSelectIndex, showType).stopCellEditing();
            }
        }

        Rectangle r1 = tableView.getCellRect(row, showType, true);
        tableView.scrollRectToVisible(r1);
        r1 = tableView.getCellRect(row, showType, true);
        tableView.scrollRectToVisible(r1);

        Rectangle rr = tableView.getVisibleRect();
        Rectangle r = tableView.getCellRect(row, showType, true);

        if (!rr.contains(r)) {
            if (rr.getY() < r.getY()) {
                Point p = scrollPane.getViewport().getViewPosition();
                p.y = p.y + (int)((r.getY() + r.getHeight()) - rr.getY() - rr.getHeight());
                scrollPane.getViewport().setViewPosition(p);
            } else {
                Point p = scrollPane.getViewport().getViewPosition();
                p.y = p.y + (int)(r.getY() - rr.getY());
                scrollPane.getViewport().setViewPosition(p);
            }
        }

        tableView.setRowSelectionInterval(row, row);
    }

    /*public void next() {
        int curSelectIndex = tableView.getSelectedRow();
        if(curSelectIndex >= (tableView.getRowCount()-1) ){
          return;
        }else {
          selectRow(curSelectIndex+1);
        }
    }

    public void previous() {
        int curSelectIndex = tableView.getSelectedRow();

        if(curSelectIndex <= 0 )
        {
          return;
        }
        else {
          selectRow(curSelectIndex-1);
        }
    }*/
    public void navigateTo(int index) {
        if ((tableView.getRowCount() > index) && (index > -1)) {
            selectRow(index);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    

    public void setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        for(int i = 0; editPanes != null && i < editPanes.length; i++)
            if(editPanes[i] != null)
                editPanes[i].setEditable(!readOnly);
    }
}
