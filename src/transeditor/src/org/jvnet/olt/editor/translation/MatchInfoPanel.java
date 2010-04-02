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
import org.jvnet.olt.editor.util.Bundle;

import javax.swing.*;
import javax.swing.border.*;


public class MatchInfoPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    Border border1;
    Color defaultColor = null;
    JPanel jPanel1 = new JPanel() {
        public Insets getInsets() {
            return new Insets(0, 2, 0, 2);
        }
    };

    JPanel jPanel2 = new JPanel() {
        public Insets getInsets() {
            return new Insets(0, 4, 0, 2);
        }
    };

    JLabel jLabel1 = new JLabel();
    BorderLayout borderLayout2 = new BorderLayout();
    JLabel jLabelMATCH = new JLabel();
    BorderLayout borderLayout3 = new BorderLayout();
    JLabel MatchStatusInformationLabel = new JLabel();
    JLabel TMInformationLabel = new JLabel();
    JPanel jPanel3 = new JPanel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    BorderLayout borderLayout4 = new BorderLayout();
    private int UNTRANSLATED = 0;
    private int TRANSLATED = 1;
    private int APPROVED = 2;
    private int REJECTED = 3;

    private Bundle bundle = Bundle.getBundle(MatchInfoPanel.class.getName());

    public MatchInfoPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white, new Color(142, 142, 142), new Color(99, 99, 99));
        this.setLayout(borderLayout1);
        this.setPreferredSize(new Dimension(104, 25));

        defaultColor = TMInformationLabel.getForeground();
        jLabel1.setText(bundle.getString("Segment_No:"));
        jPanel1.setLayout(borderLayout2);
        jLabelMATCH.setToolTipText("");
        jLabelMATCH.setText(bundle.getString("Match_Information_:"));
        jPanel2.setLayout(borderLayout3);
        MatchStatusInformationLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        MatchStatusInformationLabel.setMinimumSize(new Dimension(50, 25));
        MatchStatusInformationLabel.setPreferredSize(new Dimension(450, 25));
        TMInformationLabel.setBorder(border1);

        jLabel2.setText(bundle.getString("Status:"));
        jLabel3.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel3.setLayout(borderLayout4);
        jLabel3.setPreferredSize(new Dimension(90, 25));
        this.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(TMInformationLabel, BorderLayout.CENTER);
        jPanel1.add(jLabel1, BorderLayout.WEST);
        this.add(jPanel2, BorderLayout.WEST);
        jPanel2.add(jLabelMATCH, BorderLayout.WEST);
        jPanel2.add(MatchStatusInformationLabel, BorderLayout.CENTER);
        this.add(jPanel3, BorderLayout.EAST);
        jPanel3.add(jLabel3, BorderLayout.CENTER);
        jPanel3.add(jLabel2, BorderLayout.WEST);
    }

    public void setStatus(int iTransStatus) {
        switch (iTransStatus) {
        case 3: //REJECTED:
            jLabel3.setText(bundle.getString("Rejected"));

            break;

        case 2: //ApproveED:
            jLabel3.setText(bundle.getString("Approved"));

            break;

        case 1: //TRANSLATED:
            jLabel3.setText(bundle.getString("Translated"));

            break;

        case 0: //UNTRANSLATED:
            jLabel3.setText(bundle.getString("Untranslated"));

            break;

        default:
            break;
        }
    }

    public void setInformation(String infor) {
        MatchStatusInformationLabel.setText(infor);
    }

    public void setAliNumber(String aliNumber) {
        TMInformationLabel.setText(aliNumber);
    }

    public void setAliGray(boolean gray) {
        if (gray) {
            TMInformationLabel.setForeground(Color.gray);
        } else {
            TMInformationLabel.setForeground(defaultColor);
        }
    }
}
