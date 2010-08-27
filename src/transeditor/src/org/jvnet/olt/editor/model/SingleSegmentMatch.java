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
package org.jvnet.olt.editor.model;

import org.jvnet.olt.editor.util.Bundle;
import java.util.logging.Logger;

import org.jvnet.olt.editor.translation.AlignmentMain;

/**
 * <p>Title: Single Segment Match</p>
 */
import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.editor.translation.DocumentUndoableEdit;
import org.jvnet.olt.editor.translation.MainFrame;
import org.jvnet.olt.minitm.TMUnit;
import org.jvnet.olt.xliff.*;


public class SingleSegmentMatch extends Match {
    private static final Logger logger = Logger.getLogger(SingleSegmentMatch.class.getName());
    private static final String matchStyle = org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/model/SingleSegmentMatch").getString("1:1");
    Bundle bundle = Bundle.getBundle(SingleSegmentMatch.class.getName());
    
    public SingleSegmentMatch(TMUnit unit, int iMatchQualityInput, int iFormatDiffInput) {
        super(unit, iMatchQualityInput, iFormatDiffInput);
    }

    public SingleSegmentMatch(XLIFFBasicSentence xlfSrcInput, XLIFFBasicSentence xlfTgtInput, String aMatchQualityInput, String aFormatDiffInput, String origin) {
        super(xlfSrcInput, xlfTgtInput, aFormatDiffInput, aMatchQualityInput, origin);
    }

    public String getMatchStyle() {
        return matchStyle;
    }

    public int getSrcSegmentNumber() {
        return 1;
    }

    public void handleWhenSelected() {
        //logger.finer("it is single segment match");
        int currentSelectRow = AlignmentMain.testMain1.curRow;
        AlignmentMain.testMain1.getTableView().setRowSelectionInterval(currentSelectRow, currentSelectRow);
        AlignmentMain.testMain2.getTableView().setRowSelectionInterval(currentSelectRow, currentSelectRow);
    }

    /**
     * @param iSentenceID the row number of the table.
     */
    public void handleAsAppliedMatch(int iSentenceID, int iTransType, int iApplyType) {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        TMData.TMSentence tms = tmpdata.tmsentences[iSentenceID];
        
        handleAsAppliedMatch(tms,iTransType,iApplyType);
    }

    /**
     * @param iTransType   the translation type ("EXACT_TRANSLATION","AUTO_TRANSLATION","FUZZY_TRANSLATION")
     * @param iApplyType   the apply type ("EXACTAUTOMATCH","AUTOPROPAGATE","TRANSFER")
     */
    public void handleAsAppliedMatch(TMData.TMSentence tms, int iTransType, int iApplyType) {
        String oldString = null;
        String newString = null;
        int oldType = 0;
        int newType = 0;

        /**
         * For undo if using "TRANSFER"
         */
        if (iApplyType == TRANSFER) {
            oldString = tms.getTranslation();
            oldType = (tms.getTranslationStatus() * 10) + tms.getTranslationType();
        }

        boolean updateTags = Backend.instance().getConfig().isBFlagTagUpdate();
        /**
         * Apply match
         */
        if (iMatchQuality >= 100) {
            tms.setTranslation(this.getMatchTranslation());
            tms.setAutoTranslated(true);
        } else {
            SimpleSentence s = new SimpleSentence(tms.getSource(), this.getLRDS(), this.getLRDT(), updateTags);
            tms.setTranslation(s.getValue());
            tms.setAutoTranslated(false);
        }

        tms.setTranslationStatus(TMData.TMSentence.TRANSLATED);
        tms.setTranslationType(iTransType);

        /**
         * for undo if using "TRANSFER"
         */
        if (iApplyType == TRANSFER) {
            newString = tms.getTranslation();
            newType = (tms.getTranslationStatus() * 10) + tms.getTranslationType();

            DocumentUndoableEdit edit = new DocumentUndoableEdit(false, org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/model/SingleSegmentMatch").getString("Transfer_") + getMatchStyle(), tms.getSentenceID(), 0, oldString, newString, oldType, newType);
            MainFrame.undo.addDocumentEdit(edit);
        }
    }
}
