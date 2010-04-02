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

import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.*;

import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.editor.util.Bundle;


public class MiniTMFrame extends JPanel {
    private static final Logger logger = Logger.getLogger(MiniTMFrame.class.getName());
    static MiniTMTableFrame jMiniTM = null;
    private JPanel jBtnPanel = new JPanel();
    private JPanel jAttrPanel = new JPanel();
    private JButton jBtnTransfer = new JButton();
    //private JButton jBtnDissolve = new JButton();
    private JScrollPane jScrollPane = new JScrollPane();
    private JTextPane jAttributeText = new JTextPane();
    private BorderLayout borderLayoutAttributes = new BorderLayout();
    private Backend backend;

    private Bundle bundle = Bundle.getBundle(MiniTMFrame.class.getName());

    public MiniTMFrame(Backend backend) {
        this.backend = backend;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(null);

        jMiniTM = new MiniTMTableFrame();

        jBtnTransfer.setText(bundle.getString("Transfer"));
        jBtnTransfer.setBounds(new Rectangle(5, 5, 110, 30));
        jBtnTransfer.setEnabled(false);
        jBtnTransfer.setMaximumSize(new Dimension(110, 30));
        jBtnTransfer.setMinimumSize(new Dimension(110, 30));
        jBtnTransfer.setPreferredSize(new Dimension(110, 30));
        jBtnTransfer.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnTransfer_actionPerformed(e);
                }
            });

/*
        jBtnDissolve.setText("Untransfer");
        jBtnDissolve.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnDissolve_actionPerformed(e);
                }
            });
        jBtnDissolve.setBounds(new Rectangle(5, 45, 110, 30));
        jBtnDissolve.setEnabled(false);
        jBtnPanel.add(jBtnDissolve, null);
*/
        jBtnPanel.setLayout(null);
        jBtnPanel.setMinimumSize(new Dimension(120, 100));
        jBtnPanel.setPreferredSize(new Dimension(120, 100));
        jBtnPanel.add(jBtnTransfer, null);

        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = jAttributeText.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = jAttributeText.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
        s = jAttributeText.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        jAttributeText.setOpaque(false);
        jAttributeText.setBackground(new Color(212, 208, 200));
        jAttributeText.setEditable(false);

        jScrollPane.getViewport().add(jAttributeText, null);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        jAttrPanel.setBorder(BorderFactory.createEtchedBorder());
        jAttrPanel.setPreferredSize(new Dimension(600, 48));
        jAttrPanel.setLayout(borderLayoutAttributes);
        jAttrPanel.add(jScrollPane, BorderLayout.CENTER);

        this.add(jMiniTM, BorderLayout.CENTER);
        this.add(jBtnPanel, BorderLayout.EAST);
        this.add(jAttrPanel, BorderLayout.SOUTH);
    }

    public void setButtonEnable(boolean bEnabled) {
        jBtnTransfer.setEnabled(bEnabled);
    }

    //TODO remove this method
    public void setButtonStatus(boolean isShownAsGroup) {
        // jBtnDissolve.setEnabled(isShownAsGroup);
    }

    public void setButtonText(String strInput) {
        jBtnTransfer.setText(strInput);
    }

    void jBtnTransfer_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();

        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();

        if (iSrcTableSel == -1) {
            return;
        }

        TMData tmpdata = backend.getTMData();

        if ((tmpdata).tmsentences[iSrcTableSel].getTranslationStatus() == TMData.TMSentence.VERIFIED) {
            return;
        }

        int iSelRow = jMiniTM.tableView.getSelectedRow();

        if (iSelRow == -1) {
            return;
        }

        /**
         * apply the match
         */
        Match theMatch = jMiniTM.matchesObject[iSelRow];
        int iMatchQuality = theMatch.getMatchQuality();
        int iTransType = (iMatchQuality >= 100) ? TMData.TMSentence.EXACT_TRANSLATION : TMData.TMSentence.FUZZY_TRANSLATION;

        if (theMatch instanceof MultiSegmentMatch) {
            Toolkit.getDefaultToolkit().beep();

            if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(MainFrame.getAnInstance(), bundle.getString("This_match_is_a_M:N_match._Are_you_sure_you_want_to_transfer_it?"), bundle.getString("Transfer"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                return;
            }
        }

        theMatch.handleAsAppliedMatch(iSrcTableSel, iTransType, Match.TRANSFER);

        /**
         * Set button's status
         */
        // Timf : I've no idea why we'd want to prevent grouping from
        // affecting the way we do transfers
        //boolean shownAsGroup = tmpdata.getGroupTrack().isShownAsGrouped();
        //setButtonEnable(!shownAsGroup);
        //setButtonStatus(shownAsGroup);
        setButtonEnable(false);
        setButtonStatus(true);

        /**
         * Update the flags
         */
        MainFrame.getAnInstance().setBHasModified(true);

        /**
         * repaint the table
         */
        for (int i = 0; i < theMatch.getSrcSegmentNumber(); i++) {
            AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(iSrcTableSel + i, 0, true)); //icon
            AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel + i, 0, true)); //icon
            AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel + i, AlignmentMain.testMain2.showType, true));
        }

        theMatch.handleWhenSelected();

        AlignmentMain.testMain2.startEditing();
    }

    void jBtnDissolve_actionPerformed(ActionEvent e) {
        AlignmentMain.testMain1.stopEditing();
        AlignmentMain.testMain2.stopEditing();
        int iSrcTableSel = AlignmentMain.testMain2.tableView.getSelectedRow();
        if(iSrcTableSel == -1) return;

        TMData tmpdata = backend.getTMData();
        if((tmpdata).tmsentences[iSrcTableSel].getTranslationStatus() == TMData.TMSentence.VERIFIED) {
            return;
        }

        int iSelRow = jMiniTM.tableView.getSelectedRow() ;
        if(iSelRow == -1) return;
        /**
         * apply the match
         */
        Match theMatch = jMiniTM.matchesObject[iSelRow];
        int iMatchQuality = theMatch.getMatchQuality();
        int iTransType = iMatchQuality >=100 ? TMData.TMSentence.EXACT_TRANSLATION : TMData.TMSentence.FUZZY_TRANSLATION;

        if(theMatch instanceof MultiSegmentMatch) {
            Toolkit.getDefaultToolkit().beep();
            if(JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(MainFrame.getAnInstance(),bundle.getString("This_match_is_a_M:N_match._Are_you_sure_you_want_to_transfer_it?"),bundle.getString("Transfer"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE))
                return;
        }

        theMatch.handleAsAppliedMatch(iSrcTableSel, iTransType, Match.TRANSFER);

        /**
         * Set button's status
         */
        // Timf : I've no idea why we'd want to prevent grouping from
        // affecting the way we do transfers
        //boolean shownAsGroup = tmpdata.getGroupTrack().isShownAsGrouped();
        //setButtonEnable(!shownAsGroup);
        //setButtonStatus(shownAsGroup);
        setButtonEnable(false);
        setButtonStatus(true);

        /**
         * Update the flags
         */
        MainFrame.getAnInstance().setBHasModified(true);
        /**
         * repaint the table
         */
        for(int i=0;i<theMatch.getSrcSegmentNumber();i++) {
            AlignmentMain.testMain1.tableView.repaint(AlignmentMain.testMain1.tableView.getCellRect(iSrcTableSel+i,0,true));//icon
            AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel+i,0,true));//icon
            AlignmentMain.testMain2.tableView.repaint(AlignmentMain.testMain2.tableView.getCellRect(iSrcTableSel+i,AlignmentMain.testMain2.showType,true));
        }
        AlignmentMain.testMain2.startEditing();

    }

    public void setMatchAttributeInfo(java.util.ArrayList aList) {
        try {
            Document doc = this.jAttributeText.getDocument();
            doc.remove(0, doc.getLength());

            if (aList == null) {
                return;
            }

            for (int i = 0; i < aList.size(); i++) {
                String strContent = (String)aList.get(i);

                if ((i % 2) == 0) {
                    doc.insertString(doc.getLength(), strContent, jAttributeText.getStyle("bold"));
                } else {
                    doc.insertString(doc.getLength(), strContent, jAttributeText.getStyle("regular"));
                }
            }

            //logger.finer(doc.getText(0,doc.getLength()));
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
    }

    public void setAttributePanelVisible(boolean bInput) {
        this.jAttrPanel.setVisible(bInput);
    }
}
