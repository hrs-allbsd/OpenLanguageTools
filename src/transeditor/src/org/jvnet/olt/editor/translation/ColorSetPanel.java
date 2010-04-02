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

import javax.swing.*;


public class ColorSetPanel extends JPanel {
    String[] petStrings = { "Tag", "Segment", "Term" };
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanelTop = new JPanel();
    JLabel jLabelWhichWindow = new JLabel();
    JComboBox jComboBox1 = new JComboBox(petStrings);
    JPanel jPanelColorChooser = new JPanel();
    JColorChooser jColorChooser1 = new JColorChooser();

    public ColorSetPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jLabelWhichWindow.setText("Select the window which font color to be set:");
        jComboBox1.setSelectedIndex(2);
        jComboBox1.setMinimumSize(new Dimension(80, 20));
        jComboBox1.setPreferredSize(new Dimension(80, 20));
        jPanelTop.setBorder(BorderFactory.createEtchedBorder());
        jPanelColorChooser.setBorder(BorderFactory.createEtchedBorder());
        this.setMinimumSize(new Dimension(443, 392));
        this.setPreferredSize(new Dimension(443, 392));
        this.add(jPanelTop, BorderLayout.NORTH);
        jPanelTop.add(jLabelWhichWindow, null);
        jPanelTop.add(jComboBox1, null);
        this.add(jPanelColorChooser, BorderLayout.CENTER);
        jPanelColorChooser.add(jColorChooser1, null);
    }
}
