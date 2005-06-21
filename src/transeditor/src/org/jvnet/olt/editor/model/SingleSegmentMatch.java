/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

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
    private static final String matchStyle = "1:1";

    public SingleSegmentMatch(TMUnit unit, int iMatchQualityInput, int iFormatDiffInput) {
        super(unit, iMatchQualityInput, iFormatDiffInput);
    }

    public SingleSegmentMatch(XLIFFBasicSentence xlfSrcInput, XLIFFBasicSentence xlfTgtInput, String aMatchQualityInput, String aFormatDiffInput) {
        super(xlfSrcInput, xlfTgtInput, aFormatDiffInput, aMatchQualityInput);
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

    public void handleAsAppliedMatch(int iSentenceID, int iTransType, int iApplyType) {
        Backend backend = Backend.instance();
        TMData tmpdata = backend.getTMData();

        TMData.TMSentence tms = tmpdata.tmsentences[iSentenceID];
    }

    /**
     * @param iSentenceID  the row number of the table
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

        /**
         * Apply match
         */
        if (iMatchQuality >= 100) {
            tms.setTranslation(this.getMatchTranslation());
            tms.setAutoTranslated(true);
        } else {
            SimpleSentence s = new SimpleSentence(tms.getSource(), this.getLRDS(), this.getLRDT());
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

            DocumentUndoableEdit edit = new DocumentUndoableEdit(false, "Transfer " + getMatchStyle(), tms.getSentenceID(), 0, oldString, newString, oldType, newType);
            MainFrame.undo.addDocumentEdit(edit);
        }
    }
}
