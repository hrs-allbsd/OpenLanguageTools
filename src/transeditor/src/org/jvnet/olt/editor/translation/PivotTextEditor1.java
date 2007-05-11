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
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.undo.*;

import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.editor.util.*;


public class PivotTextEditor1 extends AbstractCellEditor implements TableCellEditor {
    private static final Logger logger = Logger.getLogger(PivotTextEditor1.class.getName());
    public static PivotTextPane sourceEditor = null;
    public static PivotTextPane targetEditor = null;
    PivotTextPane editor = null;
    int parentRow = -1;
    int parentRowInitHight = -1;
    String data = null;
    JTable table = null;
    MyEditorDelegate delegate = new MyEditorDelegate();
    protected int clickCountToStart = 1;

    protected class MyEditorDelegate implements KeyListener {
        /**
         * key listener
         */
        public void keyPressed(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            possibleChangeTableHeight(e.getSource());
        }

        private void possibleChangeTableHeight(Object c) {
            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            if ((tmpdata).tmsentences[parentRow].getTranslationType() == TMData.TMSentence.VERIFIED) {
                return;
            }

            PivotTextPane editor = (PivotTextPane)c;
            String temp = null;

            if (table == AlignmentMain.testMain2.tableView) {
                temp = (String)((TargetTableModel)table.getModel()).getEditingValueAt(table.getEditingRow(), table.getEditingColumn());
            } else {
                temp = (String)((SourceTableModel)table.getModel()).getEditingValueAt(table.getEditingRow(), table.getEditingColumn());
            }

            if (editor.getText().equals(temp)) {
                return;
            }

            /**
             * change type of the segment
             */
            if (editor == PivotTextEditor1.targetEditor) {
                (tmpdata).tmsentences[parentRow].setTranslationType(TMData.TMSentence.USER_TRANSLATION);
            }

            table.repaint(table.getCellRect(parentRow, 0, false));
            MainFrame.getAnInstance().setMenuState();

            /**
             * syn two table height
             */
            int w = getInitialWidth(table, 1);

            //int high = (int)editor.getPreferredSize().getHeight()+10;
            int high = (int)editor.getPreferredSize().getHeight();

            boolean otherIsSrc = (table != AlignmentMain.testMain1.tableView);

            table.setValueAt(editor.getText(), parentRow, 1);

            int otherHight = getOtherTablePrefferedHigth(otherIsSrc, parentRow);
            int maxHight = (high > otherHight) ? high : otherHight;

            if (maxHight != table.getRowHeight(parentRow)) {
                AlignmentMain.testMain1.tableView.setRowHeight(parentRow, maxHight);
                AlignmentMain.testMain2.tableView.setRowHeight(parentRow, maxHight);
            }

            editor.setWidth(w);
            editor.setHeight(maxHight);
        }

        public int getOtherTablePrefferedHigth(boolean isSource, int row) {
            int high = 0;

            if (isSource) {
                high = sourceEditor.getHeight();
            } else {
                high = targetEditor.getHeight();
            }

            return high;
        }
    }

    public PivotTextEditor1(JTable table) {
        super();
        this.table = table;
    }

    // Maintenance note - this method is called when switching to a different row
    // in the editor. Can use this to update other UI properties.
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        tmpdata.tmsentences[row].reload();

        if (table == AlignmentMain.testMain1.tableView) {
            data = (String)((SourceTableModel)table.getModel()).getEditingValueAt(row, column); //(String)value;
        } else {
            data = (String)((TargetTableModel)table.getModel()).getEditingValueAt(row, column);
        }

        // logger.finer("{{"+ data +"}}\n"); // debug
        parentRowInitHight = getInitialHeight(table, row);
        parentRow = row;

        MainFrame.curTable = table;
        MainFrame.curRow = parentRow;

        if (sourceEditor == null) {
            sourceEditor = new PivotTextPane(table, row, 0, true);
            sourceEditor.setBorder(null);
        }

        if (targetEditor == null) {
            targetEditor = new PivotTextPane(table, row, 0, false);
            targetEditor.setBorder(null);
        }

        if (table == AlignmentMain.testMain1.tableView) {
            editor = sourceEditor;

            if (((tmpdata).tmsentences[row]).getTranslationStatus() == TMData.TMSentence.VERIFIED) {
                editor.setEditable(false);
            } else {
                //TODO move to listener from radio action
                boolean writeProtect = Backend.instance().getConfig().isBFlagWriteProtection();
                sourceEditor.setEditable(!writeProtect);
            }

            TMInnerPanel.srcResult = null;
            AlignmentMain.testMain2.stopEditing();
        } else {
            editor = targetEditor;
            TMInnerPanel.targetResult = null;

            if (((tmpdata).tmsentences[row]).getTranslationStatus() == TMData.TMSentence.VERIFIED) {
                editor.setEditable(false);
            } else {
                editor.setEditable(true);
            }

            AlignmentMain.testMain1.stopEditing();
        }

        // bug 4745303  it must be done before doing setContent()
        editor.newInsert();
        editor.setTheRowLoacl(row);

        editor.setWidth(getInitialWidth(table, column));
        editor.setSize(getInitialWidth(table, column), parentRowInitHight - 10);
        
        editor.setFont(MainFrame._defaultFont);

        if (table == AlignmentMain.testMain1.tableView) {
            editor.setContent(data, tmpdata.tmsentences[row].getSourceBaseElements());
        } else {
            editor.setContent(data, tmpdata.tmsentences[row].getTranslationBaseElements());
        }

        int preHeight = editor.getPreferredSize().height;
        int maxH = Math.max(parentRowInitHight - 10, preHeight) + 10;

        if (maxH != parentRowInitHight) {
            AlignmentMain.testMain1.tableView.setRowHeight(parentRow, maxH);
            AlignmentMain.testMain2.tableView.setRowHeight(parentRow, maxH);
        }

        editor.setHeight(maxH);
        editor.setBackground(table.getSelectionBackground());

        editor.addKeyListener(delegate);

        //  Refresh the Font in case it has changed.
        editor.setFont(MainFrame.getDefaultFont());

        //TODO move to model selection listener !!!
        // update the SourceContextFrame with some information about this
        // source string.
        String transUnit = tmpdata.tmsentences[row].getTransUintID();
        Map context = tmpdata.getSourceContextTrack().getContext(transUnit);

        if (context != null) {
            MainFrame.jSourceContextFrame.setContextInformation(context);
        } else {
            MainFrame.jSourceContextFrame.setContextInformation(new HashMap());
        }

        return editor;
    }

    private int getInitialWidth(JTable table, int column) {
        return table.getColumnModel().getColumn(column).getWidth();
    }

    private int getInitialHeight(JTable table, int row) {
        return table.getRowHeight(row);
    }

    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    /**
     *  ClickCountToStart controls the number of clicks required to start
     *  editing.
     */
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

            editor.setEditable(false);
        }

        fireEditingStopped();

        return true;
    }

    public PivotTextPane getEditor() {
        return editor;
    }
}
