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
import org.jvnet.olt.editor.util.WordOp;


public class MiniTMTableFrame extends JPanel implements ListSelectionListener {
    public static MiniTMDataPanel[] panels = new MiniTMDataPanel[100];
    public static MiniTMTextPane[][] editPanes = new MiniTMTextPane[100][3];
    static Vector data;

    /**
     * Variable of current selected row number of matches table
     */
    public static int currentRow = -1;

    /**
     * Variable for tracking matches
     */
    public static Match[] matchesObject = null;

    /**
     * Variables for hightlighting differences for matches
     */
    public static WordOp sentenceDiffInst = new WordOp(" \t\b\f\n><.,:?!()", WordOp.EMPTY);
    public static java.util.List diff = null;
    public static java.util.List oldDiff = null;
    public static Object obj = new Object();
    public static boolean needRefresh = false;
    Logger logger = Logger.getLogger(MiniTMTableFrame.class.getName());
    TableModel dataModel = null;
    JTable tableView = null;
    JScrollPane scrollPane;
    String[] header;

    /**
     * SubClass
     */
    class MatchTable extends JTable {
        public MatchTable(TableModel t) {
            super(t);
            this.setBorder(null);
        }

        public void setRowSelectionInterval(int rowStart, int rowEnd) {
            super.setRowSelectionInterval(rowStart, rowEnd);
        }

        public void paint(Graphics g) {
            try {
                super.paint(g);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Constructor
     */
    public MiniTMTableFrame() {
        super();
        this.setLayout(new BorderLayout());

        header = new String[] { "1" };
        data = new Vector();
        showData(header, data);
    }

    public MiniTMTableFrame(Vector v) {
        super();
        this.setLayout(new BorderLayout());

        header = new String[] { "1" };
        data = v;

        showData(header, data);
    }

    private void showData(String[] header1, Vector data1) {
        MainFrame.myMatchstatusBar.setInformation("");
        MainFrame.myMatchstatusBar.setStatus(-1);

        header = header1;
        data = data1;

        dataModel = new MatchTableModel();
        tableView = new MatchTable(dataModel);
        tableView.setTableHeader(null);
        tableView.getSelectionModel().addListSelectionListener(this);
        tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableView.setBackground(MainFrame.DEFAULT_BACK_GROUND);

        for (int i = 0; i < header.length; i++) {
            TableColumn column = tableView.getColumn(header[i]);
            column.setCellRenderer(new MiniTMTextRender());
        }

        scrollPane = new JScrollPane(tableView);
        tableView.setRowSelectionAllowed(true);

        this.add("Center", scrollPane);
    }

    private MiniTMFrame getParentFrame() {
        Container c = getParent();

        while (!(c instanceof MiniTMFrame)) {
            c = c.getParent();
        }

        return (MiniTMFrame)c;
    }

    public void repaintSelf() {
        try {
            if (dataModel.getRowCount() != 0) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, dataModel.getRowCount(), 0, TableModelEvent.UPDATE));
                tableView.setRowSelectionInterval(0, 0);
                tableView.repaint();
            }

            tableView.validate();
            tableView.updateUI();
        } catch (Exception ex) {
            ex.printStackTrace(); //  Don't swallow the exception.
        }
    }

    public void repaintSelf(int oldNum) {
        tableView.clearSelection();

        int newNum = dataModel.getRowCount();

        if (newNum != 0) {
            if (newNum == oldNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldNum, 0, TableModelEvent.UPDATE));
            } else if (newNum > oldNum) {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldNum, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, 0, oldNum, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, oldNum, newNum, 0, TableModelEvent.INSERT));
                tableView.tableChanged(new TableModelEvent(dataModel, oldNum, newNum, 0, TableModelEvent.INSERT));
            } else {
                tableView.tableChanged(new TableModelEvent(dataModel, 0, newNum - 1, 0, TableModelEvent.UPDATE));
                tableView.tableChanged(new TableModelEvent(dataModel, 0, newNum - 1, 0, TableModelEvent.UPDATE));
            }

            tableView.setRowSelectionInterval(0, 0);
            tableView.repaint();
        }

        tableView.updateUI();
    }

    /**
     * ListModelListener
     */
    public void valueChanged(ListSelectionEvent e) {
        //logger.finer("valueChanged().....");
        if (tableView.getSelectedRow() == -1) {
            this.getParentFrame().setButtonStatus(false);
            this.getParentFrame().setButtonEnable(false);

            //
            ShowMatchInfo();

            //
            return;
        }

        if (e.getSource() instanceof ListSelectionModel) {
            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            boolean shownAsGroup = (tmpdata == null) ? false : tmpdata.getGroupTrack().isShownAsGrouped();

            // timf: why would we want to comment out this stuff when we're
            // showing as a group ?
            //this.getParentFrame().setButtonStatus(shownAsGroup);
            //this.getParentFrame().setButtonEnable(matchesObject.length > 0 && !shownAsGroup);
            this.getParentFrame().setButtonEnable(matchesObject.length > 0);

            if (currentRow == tableView.getSelectedRow()) {
                return;
            }

            currentRow = tableView.getSelectedRow();
            this.getParentFrame().setButtonText("Transfer" + " " + (matchesObject[currentRow]).getMatchStyle());

            // timf : again why do we want to treat groups differently ?
            //if(shownAsGroup) {
            //    this.getParentFrame().setButtonEnable(false);
            //}
            //else {
            (matchesObject[currentRow]).handleWhenSelected();

            //}
            ShowMatchInfo();
            highlighting();
        }
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
        //  Maintenance note: the method is a copy of the one in MiniTMTextRenderer.
        //  It shouldn't be here. In fact the highlighting() method that follows it
        //  should also be somewhere else. They are here because I haven't found
        //  exactly how to move the highlighting() method to MiniTMTextRenderer
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

    public void highlighting() {
        int iCurRow = tableView.getSelectedRow();
        int iCurSel = AlignmentMain.testMain1.tableView.getSelectedRow();

        if ((iCurSel == -1) || (iCurRow == -1)) {
            return;
        }

        String strDest = (matchesObject[iCurRow]).getLRDS();
        int iNumSegsInMatchSrc = (matchesObject[iCurRow]).getSrcSegmentNumber();

        //String strSrc = MainFrame.tmpdata.tmsentences[iCurSel].getSource();
        String strSrc = buildFullSourceText(iCurSel, iNumSegsInMatchSrc);

        synchronized (obj) {
            currentRow = iCurRow;
            diff = null;
            diff = sentenceDiffInst.classifyChanges(strDest, strSrc);

            oldDiff = diff.subList(0, diff.size());
            needRefresh = true;
        }

        ;

        /*
        logger.finer("<============================================================>");
        Iterator it = diff.iterator();
        while (it.hasNext()){
          WordOp op = (WordOp)it.next();
          logger.finer(op);
        }
         */

        //end of demo data
        if ((diff != null) && (diff.size() != 0)) {
            tableView.repaint(tableView.getCellRect(iCurRow, 0, false));
        }
    }

    public void ShowMatchInfo() {
        //logger.finer("ShowMatchInfo().....");
        int iCurRow = tableView.getSelectedRow();
        int iCurSel = AlignmentMain.testMain1.tableView.getSelectedRow();

        if ((iCurRow == -1) || (iCurSel == -1)) {
            //  Set a null match info to clear the attribute pane. Fixes bug
            //  4925883
            this.getParentFrame().setMatchAttributeInfo(null);

            return;
        }

        if ((matchesObject == null) || (matchesObject.length == 0)) {
            //  Set a null match info to clear the attribute pane. Fixes bug
            //  4925883
            this.getParentFrame().setMatchAttributeInfo(null);

            return;
        }

        Match match = (matchesObject[iCurRow]);

        //logger.finer(match.getMatchAttributes());
        if (match.getMatchAttributes() == null) {
            this.getParentFrame().setMatchAttributeInfo(null);
        } else {
            this.getParentFrame().setMatchAttributeInfo(match.getMatchAttributes().getAttributesList());
        }

        String strToSet = match.getMatchInformation();

        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        int iTransStatus = (tmpdata).tmsentences[iCurSel].getTranslationStatus();
        MainFrame.myMatchstatusBar.setInformation(strToSet);
        MainFrame.myMatchstatusBar.setStatus(iTransStatus);
    }
}


class MatchTableModel implements TableModel {
    /**
     * implements TableModel
     */
    public int getRowCount() {
        if (MiniTMTableFrame.data == null) {
            return 0;
        }

        return MiniTMTableFrame.data.size();
    }

    public int getColumnCount() {
        return 1;
    }

    public Object getValueAt(int row, int column) {
        Vector p = (Vector)MiniTMTableFrame.data.elementAt(row);

        return p;
    }

    public String getColumnName(int col) {
        return "1";
    }

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
    }

    /**
     * tableModelListener
     */
    public void removeTableModelListener(TableModelListener ls) {
    }

    public void addTableModelListener(TableModelListener ls) {
    }
}
