/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.lang.*;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;


class JAutoSaveDlg extends JDialog {
    private static final Logger logger = Logger.getLogger(JAutoSaveDlg.class.getName());
    protected JFrame m_owner;
    protected String[] vInterval = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    protected Vector vInter;
    JPanel p1 = new JPanel(new BorderLayout());
    JPanel pc1 = new JPanel(new BorderLayout());
    JPanel pf = new JPanel();
    JPanel jPanel1 = new JPanel();
    JButton jBtnOK = new JButton();
    JButton jBtnCancel = new JButton();
    JComboBox jCbInterval = new JComboBox(vInterval);
    JLabel jLabel1 = new JLabel();
    JCheckBox jCkEnable = new JCheckBox();
    private boolean bTempFile;
    Border border1;
    TitledBorder titledBorder1;
    JLabel jLabel2 = new JLabel();
    private boolean canceled;
    private boolean bEnable;
    private int interval;

    public JAutoSaveDlg(JFrame owner, boolean enabled, int interval) {
        super(owner, "Autosave Property Setting", false);
        m_owner = owner;
        this.bEnable = enabled;

        this.interval = interval;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        //jPanel3.setBorder(new TitledBorder(new EtchedBorder(),"Enable/Disable Autosave Function"));
        border1 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(142, 142, 142)), "Interval of Autosave Function as:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.black);
        jBtnOK.setMnemonic('O');
        jBtnOK.setSelected(true);
        jBtnOK.setText("OK");
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnOK_actionPerformed(e);
                }
            });
        jBtnCancel.setMnemonic('C');
        jBtnCancel.setText("Cancel");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCancel_actionPerformed(e);
                }
            });

        JPanel po = new JPanel(new GridLayout(2, 2, 8, 2));

        //po.setBorder(new TitledBorder(new EtchedBorder(),"Autosave Options"));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("  Minutes");
        jLabel1.setBounds(new Rectangle(204, 78, 60, 27));

        jCbInterval.setMaximumSize(new Dimension(150, 27));
        jCbInterval.setMinimumSize(new Dimension(150, 27));
        jCbInterval.setPreferredSize(new Dimension(80, 27));
        jCbInterval.setEditable(true);
        jCbInterval.setBounds(new Rectangle(137, 78, 64, 27));
        this.setResizable(false);
        jCkEnable.setText("Enable Autosave Function");
        jCkEnable.setBounds(new Rectangle(17, 22, 210, 25));
        jCkEnable.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCkEnable_actionPerformed(e);
                }
            });
        po.setLayout(null);
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Autosave file every");
        jLabel2.setBounds(new Rectangle(19, 76, 116, 31));
        pc1.add(jPanel1, BorderLayout.SOUTH);
        jPanel1.add(jBtnOK, null);
        jPanel1.add(jBtnCancel, null);
        pc1.add(po, BorderLayout.CENTER);
        po.add(jCkEnable, null);
        po.add(jCbInterval, null);
        po.add(jLabel1, null);
        po.add(jLabel2, null);
        p1.add(pc1, BorderLayout.CENTER);
        getContentPane().add(p1, BorderLayout.CENTER);

        if (bEnable) {
            jCkEnable.setSelected(true);
            jCbInterval.setEnabled(true);
            jLabel1.setEnabled(true);
        } else {
            jCbInterval.setEnabled(false);
            jLabel1.setEnabled(false);
        }

        //    logger.finer(Integer.toString(this.iInterval));
        if (this.interval > 10) {
            ((JTextField)jCbInterval.getEditor().getEditorComponent()).setText(Integer.toString(this.interval));
        } else {
            jCbInterval.setSelectedIndex(interval - 1);
        }

        Rectangle rc = m_owner.getBounds();
        this.setBounds((int)rc.getX() + (int)((rc.width - 370) / 2), (int)rc.getY() + ((int)(rc.height - 210) / 2), 370, 210);
        this.setResizable(false);

        //this.setVisible(true);
    }

    void jBtnOK_actionPerformed(ActionEvent e) {
        bEnable = this.jCkEnable.isSelected();

        String str = ((JTextField)jCbInterval.getEditor().getEditorComponent()).getText();

        if ((str == null) || (str.length() == 0)) {
            return;
        }

        if (!validateInput(str)) {
            return;
        }

        this.setVisible(false);
    }

    void jBtnCancel_actionPerformed(ActionEvent e) {
        this.setVisible(false);

        this.canceled = true;
    }

    void jCkEnable_actionPerformed(ActionEvent e) {
        bEnable = jCkEnable.isSelected();

        jCbInterval.setEnabled(bEnable);
        jLabel1.setEnabled(bEnable);
        jLabel2.setEnabled(bEnable);

        if (bEnable) {
            String str = ((JTextField)jCbInterval.getEditor().getEditorComponent()).getText();
            interval = Integer.parseInt(str);
        }
    }

    private boolean validateInput(String strInput) {
        try {
            int interval = Integer.parseInt(strInput);

            if ((interval <= 0) || (interval > 60)) {
                String strErrorMsg = "Please enter the auto save interval in minutes between 1 and 60.";
                JOptionPane.showMessageDialog(this, strErrorMsg, "Error Input", JOptionPane.OK_OPTION);

                return false;
            }

            this.interval = interval;

            return true;
        } catch (Exception ex) {
            String strErrorMsg = "The autosave interval must be a number between 1 and 60.";
            JOptionPane.showMessageDialog(this, strErrorMsg, "Error Input", JOptionPane.OK_OPTION);

            return false;
        }
    }

    public int getInterval() {
        return interval;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public boolean isAutoSaveEnabled() {
        return bEnable;
    }
}
