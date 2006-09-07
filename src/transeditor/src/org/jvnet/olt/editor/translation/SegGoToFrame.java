/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class SegGoToFrame extends JDialog {
    JLabel jLabel1 = new JLabel();
    JTextField jTextField1 = new JTextField();
    JButton jBtnOK = new JButton();
    JButton jBtnCancel = new JButton();

    public SegGoToFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jLabel1.setText("Enter new segment number:");
        jLabel1.setBounds(new Rectangle(16, 22, 182, 20));
        this.setResizable(false);
        this.setTitle("Go to segment number");
        this.getContentPane().setLayout(null);
        jTextField1.setBounds(new Rectangle(15, 54, 220, 22));
        jBtnOK.setMnemonic('O');
        jBtnOK.setText("OK");
        jBtnOK.setBounds(new Rectangle(111, 93, 77, 23));
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnOK_actionPerformed(e);
                }
            });
        jBtnCancel.setMnemonic('C');
        jBtnCancel.setText("Cancel");
        jBtnCancel.setBounds(new Rectangle(199, 93, 77, 23));
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCancel_actionPerformed(e);
                }
            });
        this.getContentPane().add(jLabel1, null);
        this.getContentPane().add(jTextField1, null);
        this.getContentPane().add(jBtnCancel, null);
        this.getContentPane().add(jBtnOK, null);
        this.show();
    }

    void jBtnOK_actionPerformed(ActionEvent e) {
        //  int iUserInput = -1;
        // iUserInput = Integer.parseInt(jLabel1.getText());
        // MainFrame.iGotoNumber = iUserInput;
        this.dispose();
    }

    void jBtnCancel_actionPerformed(ActionEvent e) {
        this.dispose();
    }
}
