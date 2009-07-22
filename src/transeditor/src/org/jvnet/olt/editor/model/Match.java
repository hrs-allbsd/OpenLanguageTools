/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.text.MessageFormat;
import org.jvnet.olt.editor.util.Bundle;
import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.minitm.TMUnit;
import org.jvnet.olt.xliff.*;


public abstract class Match {
    public static final int SYSTEM_TM = 1;
    public static final int MINI_TM = 2;
    public static final int EXACT_MATCH = 1;
    public static final int FUZZY_MATCH = 2;
    public static final int EXACTAUTOMATCH = 1; // Exact match applied by build() method in TMSentence
    public static final int AUTOPROPAGATE = 2; // Exact match applied by auto propagate
    public static final int TRANSFER = 3; // Any type match applied by using Transfer operation
    protected int iMatchQuality;
    protected int iFormatDiff;
    protected int iTMType;
    protected SimpleSentence lrds; // source in TM
    protected SimpleSentence lrdt; // target in TM
    protected MatchAttributes matchAttr = null;

    private Bundle rb = Bundle.getBundle(Match.class.getName());
    
    public Match(TMUnit unit, int iMatchQualityInput, int iFormatDiffInput) {
        iMatchQuality = iMatchQualityInput;
        iFormatDiff = iFormatDiffInput;
        iTMType = MINI_TM;
        lrds = new SimpleSentence(unit.getSource().toString());
        lrdt = new SimpleSentence(unit.getTranslation().toString());
    }

    public Match(XLIFFBasicSentence xlfSrcInput, XLIFFBasicSentence xlfTgtInput, String aMatchQualityInput, String aFormatDiffInput) {
        try {
            iMatchQuality = Integer.parseInt(aMatchQualityInput);
        } catch (NumberFormatException ex) {
            iMatchQuality = 0;
        }
        iFormatDiff = Integer.parseInt(aFormatDiffInput);
        iTMType = SYSTEM_TM;

        //xlfSrcInput.setVisibleSentence();
        //xlfTgtInput.setVisibleSentence();
        lrds = new SimpleSentence(xlfSrcInput.getVisibleSentence());
        lrdt = new SimpleSentence(xlfTgtInput.getVisibleSentence());
    }

    public int getTMType() {
        return iTMType;
    }

    public int getMatchType() {
        if (iMatchQuality >= 100) {
            return EXACT_MATCH;
        } else {
            return FUZZY_MATCH;
        }
    }

    public int getMatchQuality() {
        return iMatchQuality;
    }

    public String getLRDS() {
        return lrds.getValue();
    }

    public String getLRDT() {
        return lrdt.getValue();
    }

    public String getMatchTranslation() {
        return lrdt.getValue();
    }

    public String getMatchInformation() {
        String matchAttr = null;

        
        
        
        if (iTMType == SYSTEM_TM) {
            matchAttr = MessageFormat.format(rb.getString("TM_;_Format_Difference:_{0}%;_Quality:_{1}%"),iFormatDiff,iMatchQuality);
        } else if (iTMType == MINI_TM) {
            Backend backend = Backend.instance();
            TMData tmpdata = backend.getTMData();

            matchAttr = MessageFormat.format(rb.getString("Mini-TM:_{0};_Format_Difference:_{1}%;_Quality:_{2}%"),tmpdata.getTransProject().getMiniTM().getName(),iFormatDiff,iMatchQuality);
        }

        return matchAttr;
    }

    public void setMatchAttributes(MatchAttributes maInput) {
        matchAttr = maInput;
    }

    public MatchAttributes getMatchAttributes() {
        return matchAttr;
    }

    public boolean existMatch(String strSource, String strTarget) {
        if (getLRDS().toLowerCase().equals(strSource.toLowerCase()) && getLRDT().toLowerCase().equals(strTarget.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Implement by subClass, return "1:1" or "M:N"
     */
    public abstract String getMatchStyle();

    /**
     * Implement by subClass, return the segment number of current match
     */
    public abstract int getSrcSegmentNumber();

    /**
     * Implement by subClass, select different numbers of segment while the current match gains focus.
     */
    public abstract void handleWhenSelected();

    /**
     * Implement by subClass, apply the current match
     * @param iSentenceID  the row number of the table
     * @param iTransType   the translation type ("EXACT_TRANSLATION","AUTO_TRANSLATION","FUZZY_TRANSLATION")
     * @param iApplyType   the apply type ("EXACTAUTOMATCH","AUTOPROPAGATE","TRANSFER")
     */
    public abstract void handleAsAppliedMatch(int iSentenceID, int iTransType, int iApplyType);

    //Helper mehtod to avoid touching MainFrame.tmpdata
    public abstract void handleAsAppliedMatch(TMData.TMSentence sentence, int iTransType, int iApplyType);
}
