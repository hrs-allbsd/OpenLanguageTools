/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * AltTransUnit.java
 *
 * Created on February 25, 2005, 11:57 AM
 */
package org.jvnet.olt.xliff;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.olt.editor.model.Match;
import org.jvnet.olt.editor.model.MatchAttributes;


/**
 *
 * @author boris
 */
public class AltTransUnit {
    XLIFFBasicSentence source;
    XLIFFBasicSentence target;
    MatchAttributes attrs;
    String matchType;
    String matchQuality = "";
    String formatDiffInfo = "";
    String origin = "";

    /** Creates a new instance of AltTransUnit */
    public AltTransUnit(String matchQuality) {
        this.matchQuality = matchQuality;
    }

    public AltTransUnit(String matchQuality, String origin) {
        this ( matchQuality);
        this.origin = origin;
    }

    public XLIFFBasicSentence getSource() {
        return source;
    }

    public void setSource(XLIFFBasicSentence sntnc) {
        this.source = sntnc;
    }

    public XLIFFBasicSentence getTarget() {
        return target;
    }

    public void setTarget(XLIFFBasicSentence sntnc) {
        this.target = sntnc;
    }

    public void setMatchAttributes(MatchAttributes attrs) {
        this.attrs = attrs;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public void setFormatDiffInfo(String formatDiffInfo) {
        this.formatDiffInfo = (formatDiffInfo == null) ? "0" : formatDiffInfo;
    }

    public Match makeMatch() {
        Match match = null;

        if (matchType == null) {
            match = new org.jvnet.olt.editor.model.SingleSegmentMatch(source, target, formatDiffInfo, matchQuality, origin);
        } else {
            match = new org.jvnet.olt.editor.model.MultiSegmentMatch(source, target, formatDiffInfo, matchQuality, matchType);
        }

        match.setMatchAttributes(attrs);

        return match;
    }
}
