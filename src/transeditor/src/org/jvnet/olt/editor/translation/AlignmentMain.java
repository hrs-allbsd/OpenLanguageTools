/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;


/**
 * Title:        Test Table<p>
 * Description:  This is a test project,which test the table view for style document.<p>
 */
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
import org.jvnet.olt.editor.util.*;


public class AlignmentMain extends JPanel implements AdjustmentListener {
    private static final Logger logger = Logger.getLogger(AlignmentMain.class.getName());
    public final static int SHOW_ALL_TAGS = 1;
    public final static int SHOW_ABBREVIATE_TAGS = 2;
    public final static int SHOW_PURE_TEXT = 3;
    public final static int showType = 1; //second column
    public static AlignmentMain testMain1;
    public static AlignmentMain testMain2;
    public static JTextLabel[] editPanes = new JTextLabel[PivotTextRender1.ROW_COUNT * 2];
    public static int viewTagsStatus = 1; //1,2,3
    public static int curRow = -1;
    private static ListSelectionListener listSelectListener;
    int iTableType;
    TableModel dataModel = null;
    MyTable tableView = null;
    JScrollPane scrollPane;

    class MyListSelectionListener implements ListSelectionListener {
        /**
         * ListModelListener
         */
        public synchronized void valueChanged(ListSelectionEvent e) {
            //logger.finer.println("valueChanged()......");
            if (tableView.getSelectedRow() == -1) {
                return;
            }

            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            if (e.getSource() instanceof ListSelectionModel) {
                /**
                 * set Color for editing row
                 */
                if ((MainFrame.curTable == tableView) && (tableView == testMain1.tableView)) {
                    JTable table = testMain2.tableView;

                    if (table.isEditing()) {
                        table.getCellEditor(table.getEditingRow(), table.getEditingColumn()).stopCellEditing();
                    }
                }

                if ((MainFrame.curTable == tableView) && (tableView == testMain2.tableView)) {
                    JTable table = testMain1.tableView;

                    if (table.isEditing()) {
                        table.getCellEditor(table.getEditingRow(), table.getEditingColumn()).stopCellEditing();
                    }
                }

                /**
                 * value changed
                 */
                if (tmpdata.getGroupTrack().isShownAsGrouped()) {
                    if ((curRow == tableView.getSelectedRow()) && (e.getValueIsAdjusting() == false)) {
                        return;
                    }

                    if (curRow == tableView.getSelectedRow()) {
                        return;
                    }
                } else {
                    if ((curRow == tableView.getSelectedRow()) && (e.getValueIsAdjusting() == false)) {
                        return;
                    }

                    if (curRow == tableView.getSelectedRow()) {
                        return;
                    }
                }

                curRow = tableView.getSelectedRow();

                //logger.finer.println("--------curRow="+tableView.getSelectedRow()+" number="+tableView.getSelectedRows().length);

                /**
                 * Multi Segment Group
                 */
                String id = tmpdata.tmsentences[curRow].getTransUintID();
                int iRet = tmpdata.getGroupTrack().isTransunitIdInGroup(id);

                if (iRet != -1) {
                    String firstId = tmpdata.getGroupTrack().getFirstIdOfOneGroup(id);
                    int firstrow = tmpdata.getGroupTrack().getRowForId(firstId);
                    int size = tmpdata.getGroupTrack().getSizeOfOneGroup(firstId);

                    if ((tableView.getSelectedRow() == firstrow) && (tableView.getSelectedRows().length == size)) {
                        //logger.finer.println("select row="+tableView.getSelectedRow()+" select number="+tableView.getSelectedRows().length+"---return---"+(tableView==testMain1.tableView)+"---"+e.getValueIsAdjusting());
                        tmpdata.getGroupTrack().setShownAsGrouped(true);
                        testMain2.stopEditing();
                    } else {
                        //logger.finer.println("    curRow="+tableView.getSelectedRow()+"        number="+tableView.getSelectedRows().length+"============"+(tableView==testMain1.tableView));
                        tmpdata.getGroupTrack().setShownAsGrouped(true);

                        //tableView.setRowSelectionInterval(firstrow, firstrow+size-1);
                        //testMain1.stopEditing();
                        //testMain2.stopEditing();
                    }
                } else {
                    tmpdata.getGroupTrack().setShownAsGrouped(false);
                }

                syn(tableView);
                getMatchesContent();
                MainFrame.getAnInstance().setMenuState();

                //logger.finer.println("Adjust end.......");
                //make sure the changes made by renderers are really displayed
                testMain1.tableView.repaint();
                testMain2.tableView.repaint();
            }
        }
    }

    class MyTable extends JTable {
        public MyTable(TableModel t) {
            super(t);
            this.setBorder(null);

            //bug 4761899
            //this.getActionMap().getParent().remove("copy");
            //this.getActionMap().getParent().remove("cut");
            //this.getActionMap().getParent().remove("paste");
            //init actions
            ActionMap am = super.getActionMap().getParent(); //ActionMap from JComponent
            Object[] obj = am.keys();

            for (int i = 0; i < obj.length; i++) {
                if (obj[i].toString().indexOf("scroll") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("cut") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("copy") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("paste") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("selectFirst") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("selectLast") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("selectNext") == 0) {
                    am.remove(obj[i]);
                }

                if (obj[i].toString().indexOf("selectPrevious") == 0) {
                    am.remove(obj[i]);
                }
            }
        }

        public void paint(Graphics g) {
            try {
                super.paint(g);
            } catch (Exception ex) {
            }
        }

        public int getAbsolutHeight(Rectangle aRect) {
            Container parent;
            int dy = getY();

            for (parent = getParent(); !(parent == null); parent = parent.getParent()) {
                Rectangle bounds = parent.getBounds();

                dy += bounds.y;
            }

            return aRect.y + dy;
        }

        public void setRowSelectionInterval(int rowStart, int rowEnd) {
            try {
                super.setRowSelectionInterval(rowStart, rowEnd);

                if ((this == MainFrame.curTable) && (TMInnerPanel.targetResult == null) && (TMInnerPanel.srcResult == null) && (PivotTextPane.wordInRowIndex == -1)) {
                    editCellAt(rowStart, 1);

                    if (this == testMain2.tableView) {
                        PivotTextEditor1.targetEditor.requestFocus();
                        PivotTextEditor1.targetEditor.setCaretPosition(0);
                    } else {
                        Backend backend = Backend.instance();
                        boolean writeProtect = backend.getConfig().isBFlagWriteProtection();

                        if (writeProtect) {
                            PivotTextEditor1.sourceEditor.requestFocus();
                            PivotTextEditor1.sourceEditor.setCaretPosition(0);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.warning("Exception:"+ex);
            }
        }

        public Dimension getIntercellSpacing() {
            return new Dimension(0, 0);
        }

        public Point getToolTipLocation1(MouseEvent e) {
            return e.getPoint();
        }

        public String getToolTipText11(MouseEvent e) {
            Point p = e.getPoint();
            int row = this.rowAtPoint(p);
            int column = this.columnAtPoint(p);

            if ((row == -1) || (column != 0)) {
                return null;
            } else {
                if (this == testMain1.tableView) { //source

                    //int column = this.columnAtPoint(p);
                    //if(column == -1) return null;
                    //else {
                    int status = ((Integer)getValueAt(row, 0)).intValue();

                    switch (status) {
                    case TMData.TMSentence.UNTRANSLATED:
                        return "untranslated";

                    case TMData.TMSentence.TRANSLATED:
                        return "translated";

                    case TMData.TMSentence.VERIFIED:
                        return "verified";

                    default:
                        return null;
                    }

                    //}
                } else { //target

                    //int column = this.columnAtPoint(p);
                    //if(column == -1) return null;
                    //else {
                    int type = ((Integer)getValueAt(row, 0)).intValue();

                    switch (type) {
                    case TMData.TMSentence.AUTO_TRANSLATION:
                        return "auto-translation";

                    case TMData.TMSentence.EXACT_TRANSLATION:
                        return "exact-translation";

                    case TMData.TMSentence.FUZZY_TRANSLATION:
                        return "fuzzy-translation";

                    //case TMData.TMSentence.MACHINE_TRANSLATION:
                    //return "Machine translation";
                    case TMData.TMSentence.UNKNOWN_TRANSLATION:
                        return "unknown translation";

                    case TMData.TMSentence.USER_TRANSLATION:
                        return "user-Translation";

                    default:
                        return null;
                    }
                }
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

                //int visibleH = this.getHeight();
                int visibleH = scrollPane.getVisibleRect().height;

                return (p.y > visibleH) ? visibleH : p.y;
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
                } else if (visibleRect.getY() == r.getY()) {
                    // bug 4712009
                    if (firstVisibleRow == 0) {
                        return 0;
                    }

                    return (int)getCellRect(firstVisibleRow - 1, showType, true).getHeight();
                } else {
                    return (int)(visibleRect.getY() - r.getY());
                }
            }
        }
    }

    public AlignmentMain(int iTableType) {
        super();
        this.setLayout(new BorderLayout());
        this.iTableType = iTableType;
        listSelectListener = new MyListSelectionListener();
        showData();
    }

    void showData() {
        if (iTableType == 0) {
            dataModel = new SourceTableModel();
        } else {
            dataModel = new TargetTableModel();
        }

        tableView = new MyTable(dataModel);

        tableView.getSelectionModel().addListSelectionListener(listSelectListener);
        tableView.setTableHeader(null);
        tableView.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tableView.setBackground(MainFrame.DEFAULT_BACK_GROUND);

        tableView.setShowHorizontalLines(false);

        tableView.setIntercellSpacing(new Dimension(0, -1));
        tableView.setRowSelectionAllowed(true);

        tableView.setShowGrid(false);

        TableColumn column = tableView.getColumnModel().getColumn(0);

        if (this.iTableType == 0) { //source table
            column.setMaxWidth(20);
            column.setMinWidth(20);
            column.setPreferredWidth(20);
        } else { //target table
            column.setMaxWidth(20);
            column.setMinWidth(20);
            column.setPreferredWidth(20);
        }

        column.setResizable(false);

        if (iTableType == 0) { //source
            column.setCellRenderer(new SourceStatusRender());
            column.setCellEditor(new SourceStatusEditor());
        } else {
            column.setCellRenderer(new TranslationTypeRender());
            column.setCellEditor(new TranslationTypeEditor());
        }

        TableColumn column2 = tableView.getColumnModel().getColumn(1);

        /*column3 = tableView.getColumnModel().getColumn(2);
        column4 = tableView.getColumnModel().getColumn(3);

        if(showType == 1) {
          setVisibleColumn(column2,true,0);
          setVisibleColumn(column3,false,0);
          setVisibleColumn(column4,false,0);
        }else if(showType == 2) {
          setVisibleColumn(column2,false,0);
          setVisibleColumn(column3,true,0);
          setVisibleColumn(column4,false,0);
        }else if(showType == 3) {
          setVisibleColumn(column2,false,0);
          setVisibleColumn(column3,false,0);
          setVisibleColumn(column4,true,0);
        }*/
        column2.setCellRenderer(new PivotTextRender1());

        /// commented by tony wu, for nonsense
        //if(iTableType == 0) {//source
        column2.setCellEditor(new PivotTextEditor1(tableView));

        //  } else {
        //  column2.setCellEditor(new PivotTextEditor1(tableView));
        //}
        /*column3.setMaxWidth(0);
        column3.setMinWidth(0);
        column3.setPreferredWidth(0);

        column4.setMaxWidth(0);
        column4.setMinWidth(0);
        column4.setPreferredWidth(0);*/
        scrollPane = new JScrollPane(tableView);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        boolean synScroll = Backend.instance().getConfig().isBFlagSynScrolling();

        if (synScroll) {
            scrollPane.getVerticalScrollBar().addAdjustmentListener(this);
        }

        this.add("Center", scrollPane);
    }

    public JTable getTableView() {
        return tableView;
    }

    public void addAdjustmentListener() {
        scrollPane.getVerticalScrollBar().addAdjustmentListener(this);
    }

    public void removeAdjustmentListener() {
        scrollPane.getVerticalScrollBar().removeAdjustmentListener(this);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        synScroll(tableView);
    }

    public void typeChanged(int type) {
        stopEditing();
        viewTagsStatus = type;
        repaintSelf();
    }

    private void setVisibleColumn(TableColumn column, boolean visible, int w) {
        if (visible) {
            column.setCellRenderer(new PivotTextRender1());

            //if(iTableType == 0) {//source
            column.setCellEditor(new PivotTextEditor1(tableView));

            //} else {
            // column.setCellEditor(new PivotTextEditor1(tableView));
            //}
            if (w != 0) {
                column.setPreferredWidth(w);
            }
        } else {
            //column.setMaxWidth(0);
            //column.setMinWidth(0);
            column.setWidth(0);
        }
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

                //tableView.tableChanged(new TableModelEvent(dataModel,newSegNum,oldSegNum,0,TableModelEvent.DELETE));
                //tableView.tableChanged(new TableModelEvent(dataModel,newSegNum,oldSegNum,1,TableModelEvent.DELETE));
            }
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

    public void refreshIcon() {
        int row = tableView.getSelectedRow();

        if (row == -1) {
            return;
        }

        //logger.finer.println(tableView.getValueAt(row,0));
        tableView.tableChanged(new TableModelEvent(dataModel, row, row, 0, TableModelEvent.UPDATE));
        tableView.getCellRenderer(row, 0).getTableCellRendererComponent(tableView, tableView.getValueAt(row, 0), true, false, row, 0);
        tableView.repaint(tableView.getCellRect(row, 0, true));
    }

    void stopEditing() {
        if (tableView.isEditing()) {
            tableView.getCellEditor(tableView.getEditingRow(), tableView.getEditingColumn()).stopCellEditing();
            tableView.repaint(tableView.getCellRect(tableView.getEditingRow(), tableView.getEditingColumn(), false));
        }
    }

    void startEditing() {
        int row = tableView.getSelectedRow();

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        if (row != -1) {
            if (PivotTextEditor1.sourceEditor != null) {
                boolean writable = false;

                //(tmpdata).tmsentences[row])
                if (((tmpdata).tmsentences[row]).getTranslationStatus() == TMData.TMSentence.VERIFIED) {
                } else {
                    writable = backend.getConfig().isBFlagWriteProtection(); //!MainFrame.bFlagWriteProtection;
                }

                if (writable) {
                    tableView.editCellAt(row, 1);
                    PivotTextEditor1.sourceEditor.requestFocus();
                    PivotTextEditor1.sourceEditor.setCaretPosition(0);
                }
            }

            if (PivotTextEditor1.targetEditor != null) {
                tableView.editCellAt(row, 1);
                PivotTextEditor1.targetEditor.requestFocus();
                PivotTextEditor1.targetEditor.setCaretPosition(0);
            }
        }
    }

    public void synScroll(JTable t) {
        int row = getFirstVisibleRow();

        if (row == -1) {
            return;
        }

        boolean x = Backend.instance().getConfig().getBSelectFlag(2);

        if (x == false) { //horizontal syn

            if (t == testMain1.tableView) {
                int high1 = testMain1.tableView.getAbsolutHeight(testMain1.tableView.getCellRect(row, showType, true));
                Rectangle rr = testMain2.tableView.getCellRect(row, showType, true);
                int high2 = testMain2.tableView.getAbsolutHeight(rr);

                while (high2 != high1) {
                    Point p = testMain2.scrollPane.getViewport().getViewPosition();
                    p.y = p.y + (high2 - high1);
                    testMain2.scrollPane.getViewport().setViewPosition(p);
                    testMain2.tableView.repaint();

                    rr = testMain2.tableView.getCellRect(row, showType, true);
                    high2 = testMain2.tableView.getAbsolutHeight(rr);
                }
            } else if (t == testMain2.tableView) {
                int high2 = testMain2.tableView.getAbsolutHeight(testMain2.tableView.getCellRect(row, showType, true));
                Rectangle rr = testMain1.tableView.getCellRect(row, showType, true);
                int high1 = testMain1.tableView.getAbsolutHeight(rr);

                while (high1 != high2) {
                    Point p = testMain1.scrollPane.getViewport().getViewPosition();
                    p.y = p.y + (high1 - high2);
                    testMain1.scrollPane.getViewport().setViewPosition(p);
                    testMain1.tableView.repaint();

                    rr = testMain1.tableView.getCellRect(row, showType, true);
                    high1 = testMain1.tableView.getAbsolutHeight(rr);
                }
            }
        } else { //Vertical

            if (t == testMain1.tableView) {
                int high1 = (int)testMain1.tableView.getCellRect(row, showType, true).getY();
                Rectangle rr = testMain2.tableView.getCellRect(row, showType, true);
                int high2 = (int)rr.getY();
                Point p1 = testMain1.scrollPane.getViewport().getViewPosition();
                high1 = high1 - (int)p1.getY();

                Point p2 = testMain2.scrollPane.getViewport().getViewPosition();
                high2 = high2 - (int)p2.getY();

                while (high1 != high2) {
                    p2.y = p2.y + (high2 - high1);
                    testMain2.scrollPane.getViewport().setViewPosition(p2);
                    testMain2.tableView.repaint();

                    rr = testMain2.tableView.getCellRect(row, showType, true);
                    high2 = (int)rr.getY();
                    p2 = testMain2.scrollPane.getViewport().getViewPosition();
                    high2 = high2 - (int)p2.getY();
                }
            } else if (t == testMain2.tableView) {
                int high2 = (int)testMain2.tableView.getCellRect(row, showType, true).getY();
                Rectangle rr = testMain1.tableView.getCellRect(row, showType, true);
                int high1 = (int)rr.getY();
                Point p1 = testMain1.scrollPane.getViewport().getViewPosition();
                high1 = high1 - (int)p1.getY();

                Point p2 = testMain2.scrollPane.getViewport().getViewPosition();
                high2 = high2 - (int)p2.getY();

                while (high2 != high1) {
                    p1.y = p1.y + (high1 - high2);
                    testMain1.scrollPane.getViewport().setViewPosition(p1);
                    testMain1.tableView.repaint();

                    rr = testMain1.tableView.getCellRect(row, showType, true);
                    high1 = (int)rr.getY();
                    p1 = testMain1.scrollPane.getViewport().getViewPosition();
                    high1 = high1 - (int)p1.getY();
                }
            }
        }
    }

    private static void syncHoriz(AlignmentMain source, AlignmentMain target, int row, int selectedNumber) {
        int high1 = source.tableView.getAbsolutHeight(source.tableView.getCellRect(row, showType, true));
        Rectangle rr = target.tableView.getCellRect(row, showType, true);
        int high2 = target.tableView.getAbsolutHeight(rr);

        while (high2 != high1) {
            Point p = target.scrollPane.getViewport().getViewPosition();
            p.y = p.y + (high2 - high1);
            target.scrollPane.getViewport().setViewPosition(p);
            target.tableView.repaint();

            rr = target.tableView.getCellRect(row, showType, true);
            high2 = target.tableView.getAbsolutHeight(rr);
        }

        int curSelectIndex = target.tableView.getSelectedRow();

        if (curSelectIndex != -1) {
            if ((curSelectIndex != row) && target.tableView.isEditing()) {
                // ???
                JTable table = target.tableView;
            }
        }

        target.tableView.setRowSelectionInterval(row, (row + selectedNumber) - 1);
    }

    private static void syncVert(AlignmentMain source, AlignmentMain target, int row, int selectedNumber) {
        int high1 = (int)source.tableView.getCellRect(row, showType, true).getY();
        Rectangle rr = target.tableView.getCellRect(row, showType, true);
        int high2 = (int)rr.getY();
        Point p1 = source.scrollPane.getViewport().getViewPosition();
        high1 = high1 - (int)p1.getY();

        Point p2 = target.scrollPane.getViewport().getViewPosition();
        high2 = high2 - (int)p2.getY();

        while (high1 != high2) {
            p2.y = p2.y + (high2 - high1);
            target.scrollPane.getViewport().setViewPosition(p2);
            target.tableView.repaint();

            rr = target.tableView.getCellRect(row, showType, true);
            high2 = (int)rr.getY();
            p2 = target.scrollPane.getViewport().getViewPosition();
            high2 = high2 - (int)p2.getY();
        }

        int curSelectIndex = target.tableView.getSelectedRow();

        if (curSelectIndex != -1) {
            if ((curSelectIndex != row) && target.tableView.isEditing()) {
                // ????
                JTable table = target.tableView;
            }
        }

        target.tableView.setRowSelectionInterval(row, (row + selectedNumber) - 1);
    }

    public static void syn(JTable t) {
        int row = t.getSelectedRow();

        if (row == -1) {
            return;
        }

        int selectedNumber = t.getSelectedRows().length;

        Rectangle r1 = testMain1.tableView.getCellRect(row, showType, true);
        testMain1.tableView.scrollRectToVisible(r1);
        r1 = testMain1.tableView.getCellRect(row, showType, true);
        testMain1.tableView.scrollRectToVisible(r1);

        Rectangle r2 = testMain2.tableView.getCellRect(row, showType, true);
        testMain2.tableView.scrollRectToVisible(r2);
        r2 = testMain2.tableView.getCellRect(row, showType, true);
        testMain2.tableView.scrollRectToVisible(r2);

        //TODO replace with something better than '2'
        boolean x = Backend.instance().getConfig().getBSelectFlag(2);

        if (x == false) { //horizontal syn

            if (t == testMain1.tableView) {
                syncHoriz(testMain1, testMain2, row, selectedNumber);
            } else if (t == testMain2.tableView) { // Could it be anything else than testMain1 or testMain2 ??
                syncHoriz(testMain2, testMain1, row, selectedNumber);
            }
        } else { //Vertical

            if (t == testMain1.tableView) {
                syncVert(testMain1, testMain2, row, selectedNumber);
            } else if (t == testMain2.tableView) { // Could it be anything else than testMain1 or testMain2 ??
                syncVert(testMain2, testMain1, row, selectedNumber);
            }
        }

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        // refresh the status bar
        MainFrame.myMatchstatusBar.setAliNumber("   " + (row + 1) + "/" + tmpdata.getSize());
    }

    public void getMatchesContent() {
        //logger.finer.println(this.iTableType+ " getMatchesContent()......");
        try {
            int row = tableView.getSelectedRow();

            if (row == -1) {
                return;
            }

            if (testMain1.tableView.getSelectedRow() != testMain2.tableView.getSelectedRow()) {
                return;
            }

            int oldNum = (MiniTMTableFrame.data == null) ? 0 : MiniTMTableFrame.data.size();

            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            //refresh the status bar
            MainFrame.myMatchstatusBar.setInformation("");
            MainFrame.myMatchstatusBar.setStatus(((tmpdata).tmsentences[row]).getTranslationStatus());

            //prepare for the matches
            //  Fix for bug 5030091, call 'getMatches()' with 'true' as argument
            //  rather than 'false'
            Match[] mTemp = ((tmpdata).tmsentences[row]).getMatches(true);

            //if(mTemp != null) { logger.finer.println("Num matches:" + mTemp.length);}
            if (MiniTMTableFrame.matchesObject == mTemp) {
                if (!tmpdata.getGroupTrack().isShownAsGrouped()) {
                    (MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).handleWhenSelected();
                }

                MiniTMFrame.jMiniTM.ShowMatchInfo();

                return;
            } else {
                MiniTMTableFrame.matchesObject = mTemp;
            }

            //logger.finer.println(this.iTableType+ " getMatchesContent()......");
            if ((mTemp != null) && (mTemp.length != 0)) {
                //bug 4761883
                if (!MainFrame.getAnInstance().isBHasModified()) { // if the segment is auto translated.

                    if ((tmpdata).tmsentences[row].isAutoTranslated() && ((tmpdata).tmsentences[row].getTranslationStatus() == TMData.TMSentence.TRANSLATED) && ((tmpdata).tmsentences[row].getTranslationType() == TMData.TMSentence.AUTO_TRANSLATION)) {
                        MainFrame.getAnInstance().setBHasModified(true);
                    }
                }

                //logger.finer.println("isAutoTranslated="+((tmpdata).tmsentences[row]).isAutoTranslated());
                if ((tmpdata).tmsentences[row].getTranslationType() == TMData.TMSentence.AUTO_TRANSLATION) {
                    testMain2.tableView.tableChanged(new TableModelEvent(testMain2.tableView.getModel(), row, row, showType, TableModelEvent.UPDATE));
                }

                if (MiniTMTableFrame.data == null) {
                    MiniTMTableFrame.data = new Vector();
                } else {
                    MiniTMTableFrame.data.clear();
                }

                String strToInsert = null;

                for (int j = 0; j < mTemp.length; j++) {
                    Vector vMatchTemp = new Vector();

                    strToInsert = (String)testMain1.tableView.getValueAt(row, 1);
                    vMatchTemp.addElement(new PivotText(strToInsert, ((tmpdata).tmsentences[row]).getMatchesBaseElements()[3 * j]));
                    strToInsert = (mTemp[j]).getLRDS();
                    vMatchTemp.addElement(new PivotText(strToInsert, ((tmpdata).tmsentences[row]).getMatchesBaseElements()[(3 * j) + 1]));
                    strToInsert = (mTemp[j]).getLRDT();
                    vMatchTemp.addElement(new PivotText(strToInsert, ((tmpdata).tmsentences[row]).getMatchesBaseElements()[(3 * j) + 2]));

                    MiniTMTableFrame.data.addElement(vMatchTemp);

                    //logger.finer.println("Matches in table:" + MiniTMTableFrame.data.size());
                }

                MiniTMTableFrame.currentRow = -1;
                MiniTMFrame.jMiniTM.repaintSelf(1);
            } else { // Has no matches
                MiniTMTableFrame.currentRow = -1;
                MiniTMTableFrame.data = new Vector();
                MiniTMFrame.jMiniTM.repaintSelf(oldNum);
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    private int getFirstVisibleRow() {
        Point p = scrollPane.getViewport().getViewPosition();

        return tableView.rowAtPoint(p);
    }

    private int getLastVisibleRow() {
        Point p = scrollPane.getViewport().getViewPosition();
        int visibleH = scrollPane.getVisibleRect().height;
        p.y += visibleH;

        return tableView.rowAtPoint(p);
    }

    private int remainH() {
        int lastVisibleRow = getLastVisibleRow();
        Point p = scrollPane.getViewport().getViewPosition();

        //int visibleH = this.getHeight();
        int visibleH = scrollPane.getVisibleRect().height;
        Rectangle lastVisibleRowRect = tableView.getCellRect(lastVisibleRow, showType, true);
        Rectangle lastRowRect = tableView.getCellRect(tableView.getRowCount() - 1, showType, true);
        int h = (int)((lastRowRect.getY() + lastRowRect.getHeight()) - p.y - visibleH);

        return (h > visibleH) ? visibleH : h;
    }

    // bug 4744603 ------------------------------------------------------------------------
    public void pageUp() {
        Point p = scrollPane.getViewport().getViewPosition();

        if (p.y == 0) {
            return;
        }

        int visibleH = scrollPane.getVisibleRect().height;
        int min = (p.y > visibleH) ? visibleH : p.y;
        p.y = p.y - min;

        int row = tableView.rowAtPoint(p);
        Rectangle rect = tableView.getCellRect(row, showType, true);
        p.y = rect.y;
        scrollPane.getViewport().setViewPosition(p);

        if (tableView == AlignmentMain.testMain1.tableView) {
            AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);
        } else if (tableView == AlignmentMain.testMain2.tableView) {
            AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
        }
    }

    public void pageDown() {
        int row;
        int crow;

        if (tableView.getSelectedRow() == (tableView.getRowCount() - 1)) {
            return;
        }

        int h = remainH();
        int visibleH = scrollPane.getVisibleRect().height;
        Point p = scrollPane.getViewport().getViewPosition();
        crow = tableView.rowAtPoint(p);

        Rectangle curRect = tableView.getCellRect(crow, showType, true);

        if (curRect.height > visibleH) {
            if (p.y == curRect.y) {
                p.y = p.y + curRect.height + 1; //goto the next row
            } else {
                p.y = tableView.getHeight() - 1; //goto the last row
            }
        } else {
            if (h >= visibleH) {
                p.y = p.y + h; //goto the next Height of the scrollpane
            } else {
                p.y = tableView.getHeight() - 1; //reach the bottom
            }
        }

        row = tableView.rowAtPoint(p);

        Rectangle rect = tableView.getCellRect(row, showType, true);
        p.y = rect.y;
        scrollPane.getViewport().setViewPosition(p);

        if (tableView == AlignmentMain.testMain1.tableView) {
            AlignmentMain.testMain1.tableView.setRowSelectionInterval(row, row);
        } else if (tableView == AlignmentMain.testMain2.tableView) {
            AlignmentMain.testMain2.tableView.setRowSelectionInterval(row, row);
        }
    }

    // ------------------------------------------------------------------------
    public void next() {
        int curSelectIndex = tableView.getSelectedRow();

        if (curSelectIndex >= (tableView.getRowCount() - 1)) {
            return;
        } else {
            navigateTo(curSelectIndex + 1);
        }
    }

    public void previous() {
        int curSelectIndex = tableView.getSelectedRow();

        if (curSelectIndex <= 0) {
            return;
        } else {
            navigateTo(curSelectIndex - 1);
        }
    }

    public void navigateTo(int index) {
        if (index == tableView.getSelectedRow()) {
            return;
        }

        if ((index >= tableView.getRowCount()) || (index < 0)) {
            Toolkit.getDefaultToolkit().beep();

            return;
        }

        // bug 4713925
        // need to call setRowSelectionInterval() to adjust height of row
        tableView.setRowSelectionInterval(index, index);

        selectRow(index);

        if (tableView == testMain1.tableView) {
            syn(testMain1.tableView);
            syn(testMain2.tableView);
        } else {
            syn(testMain2.tableView);
            syn(testMain1.tableView);
        }
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

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        //tableView.setRowSelectionInterval(row,row);
        // refresh the status bar
        MainFrame.myMatchstatusBar.setAliNumber("   " + (row + 1) + "/" + tmpdata.getSize());
    }

    // method to get PivotTextPane for source
    public static PivotTextPane getSrcEditPane(int row) {
        PivotTextEditor1 editor = (PivotTextEditor1)testMain1.tableView.getCellEditor(row, 1);

        if (editor != null) {
            return (PivotTextPane)editor.getEditor();
        }

        return null;
    }

    // method to get PivotTextPane for target
    public static PivotTextPane getTargetEditPane(int row) {
        PivotTextEditor1 editor = (PivotTextEditor1)testMain2.tableView.getCellEditor(row, 1);

        if (editor != null) {
            return (PivotTextPane)editor.getEditor();
        }

        return null;
    }
}


class SourceTableModel implements TableModel {
    Backend backend = Backend.instance();
    String[] header = { "Status", "Full Content", "Abbreviation Content", "Pure Text" };

    /**
     * implements TableModel
     */
    public int getRowCount() {
        TMData tmpdata = backend.getTMData();

        if ((tmpdata == null) || (tmpdata.tmsentences == null)) {
            return 0;
        }

        return tmpdata.tmsentences.length;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getEditingValueAt(int row, int column) {
        Object o = null;
        TMData tmpdata = backend.getTMData();

        if (column == 0) { //status
            o = new Integer(tmpdata.tmsentences[row].getTranslationStatus());
        } else if (column == 1) { //content
            o = tmpdata.tmsentences[row].getSource();
        }

        return o;
    }

    public Object getValueAt(int row, int column) {
        TMData tmpdata = backend.getTMData();

        Object o = null;

        if (column == 0) { //status
            o = new Integer(tmpdata.tmsentences[row].getTranslationStatus());
        } else if (column == 1) { //Full content

            if (AlignmentMain.viewTagsStatus == 1) {
                o = tmpdata.tmsentences[row].getSource();
            } else if (AlignmentMain.viewTagsStatus == 2) {
                o = org.jvnet.olt.editor.util.BaseElements.abbreviateContent(tmpdata.tmsentences[row].getSource());
            } else if (AlignmentMain.viewTagsStatus == 3) {
                o = org.jvnet.olt.editor.util.BaseElements.pureText(tmpdata.tmsentences[row].getSource());
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
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        TMData tmpdata = backend.getTMData();

        boolean writeProtect = backend.getConfig().isBFlagWriteProtection();

        if (writeProtect) {
            return;
        }

        if ((col == 1) && (tmpdata != null)) {
            tmpdata.tmsentences[row].setSource((String)value);
        }
    }

    /**
     * tableModelListener
     */
    public void removeTableModelListener(TableModelListener ls) {
    }

    public void addTableModelListener(TableModelListener ls) {
    }
}


class TargetTableModel implements TableModel {
    Backend backend = Backend.instance();
    String[] header = { "Status", "Full Content", "Abbreviation Content", "Pure Text" };

    /**
     * implements TableModel
     */
    public int getRowCount() {
        TMData tmpdata = backend.getTMData();

        if ((tmpdata == null) || (tmpdata.tmsentences == null)) {
            return 0;
        }

        return tmpdata.tmsentences.length;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getEditingValueAt(int row, int column) {
        TMData tmpdata = backend.getTMData();
        Object o = null;

        if (column == 0) { //type
            o = new Integer(tmpdata.tmsentences[row].getTransCombinedType());
        } else if (column == 1) { //content
            o = tmpdata.tmsentences[row].getTranslation();
        }

        return o;
    }

    public Object getValueAt(int row, int column) {
        Object o = null;
        TMData tmpdata = backend.getTMData();

        if (column == 0) { //type
            o = new Integer(tmpdata.tmsentences[row].getTransCombinedType());
        } else if (column == 1) { //content

            //o = tmpdata.tmsentences[row].getTranslation();
            if (AlignmentMain.viewTagsStatus == 1) {
                o = tmpdata.tmsentences[row].getTranslation();
            } else if (AlignmentMain.viewTagsStatus == 2) {
                o = org.jvnet.olt.editor.util.BaseElements.abbreviateContent(tmpdata.tmsentences[row].getTranslation());
            } else if (AlignmentMain.viewTagsStatus == 3) {
                o = org.jvnet.olt.editor.util.BaseElements.pureText(tmpdata.tmsentences[row].getTranslation());
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
        return true;
    } //col==1&&tmpdata.tmsentences[row].getTranslationStatus()!=2;}

    public void setValueAt(Object value, int row, int col) {
        TMData tmpdata = backend.getTMData();

        if ((col == 1) && (tmpdata != null)) {
            tmpdata.tmsentences[row].setTranslation((String)value);
        }
    }

    /**
     * tableModelListener
     */
    public void removeTableModelListener(TableModelListener ls) {
    }

    public void addTableModelListener(TableModelListener ls) {
    }
}


class SourceStatusRender implements TableCellRenderer {
    Backend backend = Backend.instance();
    public final IconPanel[] statusPanel = {
        new IconPanel(true, 0), new IconPanel(true, 1), new IconPanel(true, 2),
        new IconPanel(true, 3)
    };

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int status = ((Integer)value).intValue();

        TMData tmpdata = backend.getTMData();

        JPanel panel = statusPanel[status];

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            if (tmpdata.getGroupTrack().isShownAsGrouped()) {
                String id = tmpdata.tmsentences[row].getTransUintID();
                int iRet = tmpdata.getGroupTrack().isTransunitIdInGroup(id);

                if (iRet != -1) {
                    int size = tmpdata.getGroupTrack().getSizeOfOneGroup(id);
                    String selId = tmpdata.tmsentences[table.getSelectedRow()].getTransUintID();

                    boolean sameGroup = tmpdata.getGroupTrack().inTheSameGroup(id, selId);

                    //          if(row > table.getSelectedRow() && row < (table.getSelectedRow()+size))
                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size)) && sameGroup) {
                        panel.setBackground(table.getSelectionBackground());
                    } else {
                        panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                    }
                } else {
                    panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                }
            } else {
                if ((MiniTMTableFrame.matchesObject.length > 0) && (MiniTMTableFrame.currentRow != -1) && ((MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber() > 1)) {
                    int size = (MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber();

                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size))) {
                        panel.setBackground(table.getSelectionBackground());
                    } else {
                        panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                    }
                } else {
                    panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                }
            }
        }

        return panel;
    }
}


class SourceStatusEditor extends AbstractCellEditor implements TableCellEditor {
    public final IconPanel[] statusPanel = {
        new IconPanel(true, 0), new IconPanel(true, 1), new IconPanel(true, 2),
        new IconPanel(true, 3)
    };

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        MainFrame.curTable = table;

        int status = ((Integer)value).intValue();

        JPanel panel = statusPanel[status];
        panel.setBackground(table.getSelectionBackground());

        return panel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}


class TranslationTypeRender implements TableCellRenderer {
    Backend backend = Backend.instance();
    public final IconPanel[] typePanel = {
        new IconPanel(false, 0, 0), new IconPanel(false, 0, 1), new IconPanel(false, 0, 2),
        new IconPanel(false, 0, 3), new IconPanel(false, 0, 4), new IconPanel(false, 0, 5),
        new IconPanel(false, 1, 0), new IconPanel(false, 1, 1), new IconPanel(false, 1, 2),
        new IconPanel(false, 1, 3), new IconPanel(false, 1, 4), new IconPanel(false, 1, 5)
    };

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int type = ((Integer)value).intValue();

        JPanel panel = typePanel[(type / 10 * 6) + (type % 10)];

        TMData tmpdata = backend.getTMData();

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            if (tmpdata.getGroupTrack().isShownAsGrouped()) {
                String id = tmpdata.tmsentences[row].getTransUintID();
                int iRet = tmpdata.getGroupTrack().isTransunitIdInGroup(id);

                if (iRet != -1) {
                    int size = tmpdata.getGroupTrack().getSizeOfOneGroup(id);

                    String selId = tmpdata.tmsentences[table.getSelectedRow()].getTransUintID();

                    boolean sameGroup = tmpdata.getGroupTrack().inTheSameGroup(id, selId);

                    //if(row > table.getSelectedRow() && row < (table.getSelectedRow()+size))
                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size)) && sameGroup) {
                        panel.setBackground(table.getSelectionBackground());
                    } else {
                        panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                    }
                } else {
                    panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                }
            } else {
                if ((MiniTMTableFrame.matchesObject.length > 0) && (MiniTMTableFrame.currentRow != -1) && ((MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber() > 1)) {
                    int size = (MiniTMTableFrame.matchesObject[MiniTMTableFrame.currentRow]).getSrcSegmentNumber();

                    if ((row > table.getSelectedRow()) && (row < (table.getSelectedRow() + size))) {
                        panel.setBackground(table.getSelectionBackground());
                    } else {
                        panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                    }
                } else {
                    panel.setBackground(MainFrame.DEFAULT_BACK_GROUND);
                }
            }
        }

        return panel;
    }
}


class TranslationTypeEditor extends AbstractCellEditor implements TableCellEditor {
    public final IconPanel[] typePanel = {
        new IconPanel(false, 0, 0), new IconPanel(false, 0, 1), new IconPanel(false, 0, 2),
        new IconPanel(false, 0, 3), new IconPanel(false, 0, 4), new IconPanel(false, 0, 5),
        new IconPanel(false, 1, 0), new IconPanel(false, 1, 1), new IconPanel(false, 1, 2),
        new IconPanel(false, 1, 3), new IconPanel(false, 1, 4), new IconPanel(false, 1, 5)
    };

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        MainFrame.curTable = table;

        int type = ((Integer)value).intValue();

        JPanel panel = typePanel[(type / 10 * 6) + (type % 10)];

        panel.setBackground(table.getSelectionBackground());

        return panel;
    }

    public Object getCellEditorValue() {
        return "";
    }
}


class IconPanel extends JPanel {
    public final MouseAdapter dbclick = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if ((e.getButton() == e.BUTTON1) && (e.getClickCount() == 2)) {
                MainFrame.getAnInstance().jMenuEditComment_actionPerformed(null);
                AlignmentMain.testMain2.startEditing();
            }
        }
    };

    public final MouseAdapter ml = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                //logger.finer.println("isPopupTrigger");
                PivotTextPane.showEditMenu(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                PivotTextPane.showEditMenu(e);
            }
        }
    };

    public IconPanel(boolean isStatus, int index) {
        super();
        setLayout(null);

        JLabel label = null;

        if (isStatus) {
            label = new JLabel("", MainFrame.ip.statusIcon[index], SwingConstants.LEFT);
        } else {
            label = new JLabel("", MainFrame.ip.typeIcon[index], SwingConstants.LEFT);
        }

        label.setOpaque(true);
        add(label);
        label.setBounds(0, 0, 16, 16);
        this.addMouseListener(ml);
    }

    public IconPanel(boolean isStatus, int commented, int index) {
        super();
        setLayout(null);

        JLabel label = null;
        JLabel commentLabel = null;

        if (commented == 0) {
            if (isStatus) {
                label = new JLabel("", MainFrame.ip.statusIcon[index], SwingConstants.LEFT);
            } else {
                label = new JLabel("", MainFrame.ip.typeIcon[index], SwingConstants.LEFT);
            }

            label.setOpaque(true);
            label.setBounds(0, 0, 16, 16);
            add(label);
        } else if (commented == 1) {
            if (isStatus) {
                label = new JLabel("", MainFrame.ip.statusIcon[index], SwingConstants.LEFT);
            } else {
                label = new JLabel("", MainFrame.ip.typeIcon[index], SwingConstants.LEFT);
                commentLabel = new JLabel("", MainFrame.ip.imageComment, SwingConstants.LEFT);
            }

            label.setOpaque(true);
            label.setBounds(0, 0, 16, 16);
            commentLabel.setOpaque(true);
            commentLabel.setBounds(0, 17, 16, 16);

            commentLabel.addMouseListener(dbclick);
            add(label);
            add(commentLabel);
        }
    }
}
