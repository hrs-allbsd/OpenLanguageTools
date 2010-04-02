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

import javax.swing.*;


public class ColorSetFrame extends JFrame {
    JPanel jPanel2 = new JPanel();
    JPanel jPanel1 = new ColorSetPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JButton jBtnApply = new JButton();
    JButton jBtnCancel = new JButton();
    JButton jBtnOK = new JButton();
    FlowLayout flowLayout1 = new FlowLayout();

    public ColorSetFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                }
            });
        this.setResizable(false);
        this.setTitle("Color Setting Dialog");
        this.getContentPane().setLayout(borderLayout1);
        jBtnApply.setMaximumSize(new Dimension(75, 29));
        jBtnApply.setMinimumSize(new Dimension(75, 29));
        jBtnApply.setPreferredSize(new Dimension(75, 29));
        jBtnApply.setMnemonic('A');
        jBtnApply.setText("Apply");
        jBtnApply.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnApply_actionPerformed(e);
                }
            });
        jBtnCancel.setMnemonic('C');
        jBtnCancel.setText("Cancel");
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCancel_actionPerformed(e);
                }
            });
        jBtnOK.setMaximumSize(new Dimension(75, 29));
        jBtnOK.setMinimumSize(new Dimension(75, 29));
        jBtnOK.setPreferredSize(new Dimension(75, 29));
        jBtnOK.setMnemonic('O');
        jBtnOK.setText("OK");
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnOK_actionPerformed(e);
                }
            });
        jPanel2.setLayout(flowLayout1);
        jPanel2.setMinimumSize(new Dimension(219, 36));
        jPanel2.setPreferredSize(new Dimension(219, 36));
        jPanel1.setMinimumSize(new Dimension(443, 393));
        jPanel1.setPreferredSize(new Dimension(443, 393));
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
        jPanel2.add(jBtnOK, null);
        jPanel2.add(jBtnCancel, null);
        jPanel2.add(jBtnApply, null);

        setSize(450, 470);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
    }

    void jBtnApply_actionPerformed(ActionEvent e) {
        //do something,then directly quit.
        this.dispose();
    }

    void jBtnCancel_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    void jBtnOK_actionPerformed(ActionEvent e) {
        //confirm user something,if yes ,do something, then quit;if no,nothing to do.
        int n = JOptionPane.showConfirmDialog(this, "You have made some change on the fonts setting\n" + "Do you want to save?", "An Inane Question", JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) { //do some save working.
            this.dispose();
        } else {
            return;
        }
    }
}
