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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


/**
 * @deprecated
 */
public class MyFileChooser extends JFileChooser {
    public static String[] strProjectName = { "Solaris_UR5" };
    public static JTextField jTf;
    public static String strProName;
    JList m_Entries;
    ListSelectionModel listSelectionModel;
    TabListCellRenderer renderer;
    protected MainFrame m_MainFrame;

    class TMOpenFilter extends javax.swing.filechooser.FileFilter {
        String tmExtension = "tm";

        public boolean accept(File f) {
            if (f != null) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = getExtension(f);

                if ((extension != null) && extension.equals(tmExtension)) {
                    return true;
                }

                ;
            }

            return false;
        }

        public String getExtension(File f) {
            if (f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');

                if ((i > 0) && (i < (filename.length() - 1))) {
                    return filename.substring(i + 1).toLowerCase();
                }

                ;
            }

            return null;
        }

        public String getDescription() {
            return "Translation Memory Files";
        }
    }

    public MyFileChooser(MainFrame m_Parent) {
        m_MainFrame = m_Parent;

        //    super();
        //ShowMyFileChooser();
    }

    public String getSelectedProjectName() {
        return strProName;
    }

    public int ShowMyFileChooser() {
        this.addChoosableFileFilter(new TMOpenFilter());
        this.setSelectedFile(new File("*.tm"));

        this.setDialogType(JFileChooser.OPEN_DIALOG);
        this.setMultiSelectionEnabled(false);

        //  this.setFileView(new MyFileChooserExtendView());
        this.setPreferredSize(new Dimension(500, 400));
        m_Entries = new JList(strProjectName);
        listSelectionModel = m_Entries.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        m_Entries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        renderer = new TabListCellRenderer();
        renderer.setTabs(new int[] { 240, 300, 360 });
        m_Entries.setCellRenderer(renderer);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new TitledBorder("Select a Project ")), new EmptyBorder(2, 2, 2, 2)));

        JScrollPane ps = new JScrollPane(m_Entries);
        JPanel j = new JPanel() {
            public Insets getInsets() {
                return new Insets(2, 2, 2, 2);
            }
        };

        j.setLayout(new BorderLayout());

        JLabel jProName = new JLabel("Project       Name:");
        j.add(jProName, BorderLayout.WEST);
        jTf = new JTextField();
        jTf.setText("Solaris_UR5");
        j.add(jTf, BorderLayout.CENTER);
        p.add(j, BorderLayout.NORTH);

        JPanel jButtomPanel = new JPanel() {
            public Insets getInsets() {
                return new Insets(2, 0, 2, 2);
            }
        };

        JPanel jLeftPanel = new JPanel() {
            public Insets getInsets() {
                return new Insets(2, 0, 2, 2);
            }
        };

        JLabel jAvaiPro = new JLabel("Available Project:");
        jLeftPanel.setLayout(new BorderLayout());
        jButtomPanel.setLayout(new BorderLayout());
        jLeftPanel.add(jAvaiPro, BorderLayout.NORTH);
        jButtomPanel.add(jLeftPanel, BorderLayout.WEST);
        jButtomPanel.add(ps, BorderLayout.CENTER);
        p.add(jButtomPanel, BorderLayout.CENTER);

        //this.add(p);
        //this.validate();
        return this.showDialog(m_MainFrame, "Open");

        //    if(this.showDialog(this,"Open") != JFileChooser.APPROVE_OPTION)
        //  return JFileChooser.CANCEL_OPTION ;
        //else return JFileChooser.APPROVE_OPTION;
    }
}


class TabListCellRenderer extends JLabel implements ListCellRenderer {
    protected static Border m_noFocusBorder;
    protected FontMetrics m_fm = null;
    protected Insets m_insets = new Insets(5, 5, 5, 5);
    protected int m_defaultTab = 50;
    protected int[] m_tabs = null;

    public TabListCellRenderer() {
        super();
        m_noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        setOpaque(true);
        setBorder(m_noFocusBorder);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : m_noFocusBorder);

        return this;
    }

    public void setDefaultTab(int defaultTab) {
        m_defaultTab = defaultTab;
    }

    public int getDefaultTab() {
        return m_defaultTab;
    }

    public void setTabs(int[] tabs) {
        m_tabs = tabs;
    }

    public int[] getTabs() {
        return m_tabs;
    }

    public int getTab(int index) {
        if (m_tabs == null) {
            return m_defaultTab * index;
        }

        int len = m_tabs.length;

        if ((index >= 0) && (index < len)) {
            return m_tabs[index];
        }

        return m_tabs[len - 1] + (m_defaultTab * (index - len + 1));
    }

    public void paint(Graphics g) {
        m_fm = g.getFontMetrics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        g.setFont(getFont());
        m_insets = getInsets();

        int x = m_insets.left;
        int y = m_insets.top + m_fm.getAscent();
        StringTokenizer st = new StringTokenizer(getText(), "\t");

        while (st.hasMoreTokens()) {
            String sNext = st.nextToken();
            g.drawString(sNext, x, y);
            x += m_fm.stringWidth(sNext);

            if (!st.hasMoreTokens()) {
                break;
            }

            int index = 0;

            while (x >= getTab(index))
                index++;

            x = getTab(index);
        }
    }
}


class SharedListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        int firstIndex = e.getFirstIndex();
        int lastIndex = e.getLastIndex();

        if (firstIndex == -1) {
            return;
        }

        if (lsm.isSelectionEmpty()) {
            return;
        } else {
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();

            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    MyFileChooser.jTf.setText("");
                    MyFileChooser.jTf.setText(MyFileChooser.strProjectName[i]);
                    MyFileChooser.strProName = MyFileChooser.strProjectName[i];
                }
            }
        }
    }
}
