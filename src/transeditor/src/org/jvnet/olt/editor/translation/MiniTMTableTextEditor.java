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

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.undo.*;

import org.jvnet.olt.editor.model.*;


public class MiniTMTableTextEditor extends AbstractCellEditor implements TableCellEditor {
    Vector data;
    int parentRowInitHight = -1;
    int parentRow = -1;
    public MiniTMEditorDelegate delegate;
    protected int clickCountToStart = 1;
    JTable table;
    MiniTMDataPanel p = null;

    protected class MiniTMEditorDelegate implements CaretListener, MouseListener, FocusListener {
        public void focusGained(FocusEvent e) {
            Component[] children = p.getComponents();

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JScrollPane) {
                    if (e.getSource() == ((JScrollPane)children[i]).getViewport().getView()) {
                        ((Component)e.getSource()).setBackground(Color.gray);
                        ((JScrollPane)children[i]).setBackground(Color.gray);
                    }

                    //children[i-1].getParent().setBackground(Color.gray);
                    //children[i-1].setBackground(Color.gray);
                }
            }
        }

        public void focusLost(FocusEvent e) {
            Component[] children = p.getComponents();

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JScrollPane) {
                    if (e.getSource() == ((JScrollPane)children[i]).getViewport().getView()) {
                        ((Component)e.getSource()).setBackground(Color.lightGray);
                        ((JScrollPane)children[i]).setBackground(Color.lightGray);
                    }
                }
            }
        }

        public void caretUpdate(CaretEvent e) {
            //if(!MiniTMTableFrame.needCaretUpdated) return;
            Component[] children = p.getComponents();
            int curHight = 0;

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JScrollPane) {
                    int high = ((JScrollPane)children[i]).getViewport().getView().getPreferredSize().height;
                    children[i - 1].setBounds(0, curHight, 20, high);
                    children[i].setBounds(20, curHight, getInitialWidth(table, 0) - 20, high);
                    curHight += (high + 5);

                    if (i != (children.length - 1)) {
                        curHight += 0;
                    }
                }
            }

            if (parentRowInitHight == 0) {
                return;
            }

            table.setRowHeight(parentRow, curHight + 10);
        }

        /**
         * mouselistener
         */
        public void mouseExited(MouseEvent e) {
            PivotText p1 = new PivotText(((MiniTMTextPane)e.getSource()).getContent(), ((MiniTMTextPane)e.getSource()).elements);
            Component[] children = p.getComponents();
            int index = -1;

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JScrollPane && (((JScrollPane)children[i]).getViewport().getView() == e.getSource())) {
                    index = (i - 1) / 2;

                    break;
                }
            }

            data.removeElementAt(index);
            data.insertElementAt(p1, index);
            p.setData(data);
            ((MiniTMTextPane)e.getSource()).removeCaretListener(delegate);
            ((MiniTMTableTextEditor)table.getCellEditor(parentRow, 0)).stopCellEditing();

            //        table.clearSelection();
        }

        public void mouseEntered(MouseEvent e) {
            ((MiniTMTextPane)e.getSource()).addCaretListener(delegate);
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }
    }

    public MiniTMTableTextEditor(JTable table) {
        super();
        this.table = table;
        delegate = new MiniTMEditorDelegate();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        data = (Vector)value;
        parentRowInitHight = getInitialHeight(table, row);
        parentRow = row;

        //MiniTMTableFrame.needCaretUpdated = true;
        MiniTMDataPanel panel = (MiniTMDataPanel)table.getCellRenderer(row, column).getTableCellRendererComponent(table, value, true, true, row, column);
        Component[] childrens = panel.getComponents();

        for (int i = 0; i < childrens.length; i++) {
            if (childrens[i] instanceof JScrollPane) {
                MiniTMTextPane p1 = (MiniTMTextPane)((JScrollPane)childrens[i]).getViewport().getView();

                //p1.addFocusListener(delegate);
                p1.addCaretListener(delegate);
                p1.addMouseListener(delegate);
            }
        }

        p = panel;

        return panel;
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
        fireEditingStopped();

        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }
}
