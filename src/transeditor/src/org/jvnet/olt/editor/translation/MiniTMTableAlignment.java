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

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


public class MiniTMTableAlignment extends JPanel implements ListSelectionListener {
    private static final Logger logger = Logger.getLogger(MiniTMTableAlignment.class.getName());
    public final static int ID_TABLE = 1;
    public final static int TM_TABLE = 0;

    //public static int iSentenceCount;
    //public static PivotTextPane[] editPanes = new PivotTextPane[PivotTextRender1.ROW_COUNT*2];
    //public static Color DEFAULT_BACK_GROUND = new Color(250,250,240);
    TableModel dataModel = null;
    ListSelectionModel listSelectionModel = null;
    MiniTMTable tableView = null;
    String[] data = null; //[]
    JScrollPane scrollPane;
    String[] header = null;

    //public static boolean isShowID = false;
    MergeMiniTMTablePanel parent;

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

        public Dimension getIntercellSpacing() {
            return new Dimension(0, 0);
        }
    }

    /**
     * table model
     */
    class MiniTMTableModel implements TableModel {
        public int getRowCount() {
            if (data == null) {
                return 0;
            }

            return data.length;
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int column) {
            Object o = null;

            return data[row];
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

    public MiniTMTableAlignment(MergeMiniTMTablePanel parent, int type) {
        this(type);
        this.parent = parent;
    }

    public MiniTMTableAlignment(int type) {
        super();
        this.setLayout(new BorderLayout());

        if (type == ID_TABLE) {
            header = new String[] { "Translator IDs" };
        } else {
            header = new String[] { "Mini-TMs" };
        }

        showData();
    }

    void showData() {
        dataModel = new MiniTMTableModel();
        tableView = new MiniTMTable(dataModel);

        //tableView.setTableHeader(null);
        tableView.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel = tableView.getSelectionModel();
        listSelectionModel.addListSelectionListener(this);
        tableView.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        tableView.setShowHorizontalLines(false);
        tableView.getTableHeader().setReorderingAllowed(false);

        tableView.setIntercellSpacing(new Dimension(0, -1));

        tableView.setShowGrid(false);

        //TableColumn column = tableView.getColumnModel().getColumn(0);
        //column.setMaxWidth(40);
        //column.setMinWidth(40);
        //column.setPreferredWidth(40);
        //column.setResizable(false);
        //column.setCellRenderer(new MiniTMFlagRender());
        //column2 = tableView.getColumnModel().getColumn(1);
        //column2.setCellRenderer(new MaintainMiniTMRender());
        scrollPane = new JScrollPane(tableView);
        tableView.setRowSelectionAllowed(true);

        this.add("Center", scrollPane);
    }

    public Insets getInsets() {
        return new Insets(10, 5, 5, 10);
    }

    /**
     * ListModelListener
     */
    public void valueChanged(ListSelectionEvent e) {
        int count = tableView.getSelectedRowCount();

        if (count <= 0) {
            if (this == parent.tmTableAlignment) {
                parent.removeButton.setEnabled(false);
            } else {
                parent.moveDownButton.setEnabled(false);
                parent.moveUpButton.setEnabled(false);
            }
        } else if (count > 1) {
            if (this == parent.idTableAlignment) {
                parent.moveDownButton.setEnabled(false);
                parent.moveUpButton.setEnabled(false);
            }
        } else {
            if (this == parent.tmTableAlignment) {
                parent.removeButton.setEnabled(true);
            }

            if (this == parent.idTableAlignment) {
                if (tableView.getSelectedRow() == (tableView.getRowCount() - 1)) {
                    parent.moveDownButton.setEnabled(false);
                } else {
                    parent.moveDownButton.setEnabled(true);
                }

                if (tableView.getSelectedRow() == 0) {
                    parent.moveUpButton.setEnabled(false);
                } else {
                    parent.moveUpButton.setEnabled(true);
                }
            }
        }
    }

    public void moveUp() {
        int index = tableView.getSelectedRow();
        String id = data[index];
        data[index] = data[index - 1];
        data[index - 1] = id;

        tableView.tableChanged(new TableModelEvent(dataModel, index - 1, index));
        tableView.setRowSelectionInterval(index - 1, index - 1);
        tableView.repaint();
    }

    public void moveDown() {
        int index = tableView.getSelectedRow();
        String id = data[index];
        data[index] = data[index + 1];
        data[index + 1] = id;

        tableView.tableChanged(new TableModelEvent(dataModel, index, index + 1));
        tableView.setRowSelectionInterval(index + 1, index + 1);
        tableView.repaint();
    }

    public void remove() {
        int count = tableView.getSelectedRowCount();

        if (count <= 0) {
        } else {
            int cur = -1;

            int oldSegNum = tableView.getRowCount();

            int[] rows = tableView.getSelectedRows();
            Vector v = new Vector();

            for (int i = (rows.length - 1); i >= 0; i--) {
                v.addElement(new Integer(rows[i]));
            }

            if (rows.length == data.length) {
                data = null;
            } else {
                String[] temp = new String[data.length - rows.length];
                int p = 0;

                for (int i = 0; i < data.length; i++) {
                    if (v.contains(new Integer(i))) {
                        continue;
                    } else {
                        temp[p] = data[i];

                        p++;
                    }
                }

                data = temp;

                if ((data.length - rows.length) == 1) {
                    cur = 0;
                } else { // if(rows.length == 1){

                    if (rows[0] >= data.length) {
                        cur = data.length - 1;
                    } else {
                        cur = rows[0];
                    }
                }
            }

            repaintSelf(oldSegNum, cur);
        }
    }

    public void remove(String[] ids, int[] flags) {
        int count = ids.length;

        if (count <= 0) {
        } else {
            int cur = -1;

            int oldSegNum = tableView.getRowCount();

            Vector tt = new Vector();

            for (int ii = 0; ii < ids.length; ii++) {
                if (flags[ii] == 1) {
                    if (data != null) {
                        for (int jj = 0; jj < data.length; jj++) {
                            if (ids[ii].equals(data[jj])) {
                                tt.addElement(new Integer(jj));

                                break;
                            }
                        }
                    }
                }
            }

            //int[] rows = new int[tt.size();
            Vector v = tt;

            //for(int i=(rows.length-1);i>=0;i--) {
            //  v.addElement(new Integer(rows[i]));
            //}
            if (v.size() == 0) {
                return;
            }

            if (v.size() == data.length) {
                data = null;
            } else {
                String[] temp = new String[data.length - v.size()];
                int p = 0;

                for (int i = 0; i < data.length; i++) {
                    if (v.contains(new Integer(i))) {
                        continue;
                    } else {
                        temp[p] = data[i];

                        p++;
                    }
                }

                data = temp;

                if ((data.length - v.size()) == 1) {
                    cur = 0;
                } else { // if(rows.length == 1){

                    int index = ((Integer)v.elementAt(0)).intValue();

                    if (index >= data.length) {
                        cur = data.length - 1;
                    } else {
                        cur = index;
                    }
                }
            }

            repaintSelf(oldSegNum, cur);
        }
    }

    public void append(String name) {
        int oldSegNum = tableView.getRowCount();
        appendOne(name);

        int cur = data.length - 1;
        repaintSelf(oldSegNum, cur);
    }

    private void appendOne(String name) {
        if (data == null) {
            data = new String[1];
            data[0] = name;
        } else {
            String[] temp = new String[data.length + 1];

            for (int i = 0; i < data.length; i++) {
                temp[i] = data[i];
            }

            temp[data.length] = name;

            data = temp;
        }
    }

    //int count = 0;
    public void append(String[] ids) {
        /*ids = new String[2];
        ids[0] = "test"+count/3;
        ids[1] = "test"+(count+1)/3;

        count += 2;*/
        int oldSegNum = tableView.getRowCount();

        if ((ids == null) || (ids.length == 0)) {
            //logger.finer("ids is null");
            return;
        } else {
OUT: 
            for (int i = 0; i < ids.length; i++) {
                String temp = ids[i];

                //logger.finer("id="+temp);
                boolean hasIn = false;

                if (data != null) {
                    for (int j = 0; j < data.length; j++) {
                        if (data[j].equals(temp)) {
                            continue OUT;
                        }
                    }
                }

                appendOne(temp);
            }

            int cur = data.length - 1;

            repaintSelf(oldSegNum, cur);
        }
    }

    public void repaintSelf(int oldSegNum, int cur) {
        tableView.clearSelection();

        int newSegNum = dataModel.getRowCount();

        if (newSegNum != 0) {
            if (newSegNum == oldSegNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, 0, TableModelEvent.UPDATE));
            } else if (newSegNum > oldSegNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldSegNum, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, oldSegNum, newSegNum, 0, TableModelEvent.INSERT));
            } else {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, newSegNum - 1, 0, TableModelEvent.UPDATE));
            }

            tableView.repaint();
        }

        if ((newSegNum > 0) && (cur != -1)) {
            tableView.setRowSelectionInterval(cur, cur);
        }

        tableView.updateUI();
    }

    public void repaintSelf() {
        int oldCur = tableView.getSelectedRow();
        int newSegNum = dataModel.getRowCount();
        tableView.tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
        tableView.repaint();

        //TableColumn column = tableView.getColumnModel().getColumn(0);
        //column.setPreferredWidth(10);
        if (oldCur != -1) {
            tableView.setRowSelectionInterval(oldCur, oldCur);
        }

        tableView.updateUI();
    }
}
