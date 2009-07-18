/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.jvnet.olt.editor.model.*;


public class MiniTMDataPanel extends JPanel {
    Vector data = null;
    int width = -1;

    public MiniTMDataPanel() {
        super();
        this.setBorder(null);
    }

    public void setData(Vector d) {
        data = d;
    }

    public synchronized Vector getData() {
        return data;
    }

    public void setWidth(int w) {
        width = w;
    }

    public int getWidth() {
        return width;
    }

    public void refresh(JTable table, int row, int width) {
        try {
            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            Component[] children = getComponents();
            int curHight = 0;

            //logger.finer("children.length = "+ children.length);
            int currentRow = 0;

            for (int i = 0; i < children.length; i++) {
                if (children[i] instanceof JScrollPane) {
                    MiniTMTextPane textPane = (MiniTMTextPane)((JScrollPane)children[i]).getViewport().getView();
                    textPane.setSize(width - 20, (int)textPane.getSize().getHeight());

                    Match[] oTemp = tmpdata.tmsentences[AlignmentMain.testMain1.tableView.getSelectedRow()].getMatches(false);

                    switch (currentRow) {
                    case 0:
                        textPane.setContent(tmpdata.tmsentences[AlignmentMain.testMain1.tableView.getSelectedRow()].getSource(), tmpdata.tmsentences[AlignmentMain.testMain1.tableView.getSelectedRow()].getSourceBaseElements());

                        break;

                    case 1:
                        textPane.setContent(oTemp[row].getLRDS(), tmpdata.tmsentences[AlignmentMain.testMain1.tableView.getSelectedRow()].getMatchesBaseElements()[(3 * row) + 1]);

                        break;

                    case 2:
                        textPane.setContent(oTemp[row].getLRDT(), tmpdata.tmsentences[AlignmentMain.testMain1.tableView.getSelectedRow()].getMatchesBaseElements()[(3 * row) + 2]);

                        break;

                    default:
                        break;
                    }

                    int high = textPane.getPreferredSize().height;
                    children[i - 1].setBounds(0, curHight, 20, high);
                    children[i].setBounds(20, curHight, width - 20, high);
                    curHight += (high + 5);

                    if (i != (children.length - 1)) {
                        curHight += 0;
                    }

                    currentRow++;
                }
            }

            table.setRowHeight(row, curHight + 10);
        } catch (Exception ex) {
        }
    }
}
