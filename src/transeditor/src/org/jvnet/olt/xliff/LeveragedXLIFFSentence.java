/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import org.jvnet.olt.editor.util.NestableException;


/**
 * @version 1.2
 */
public class LeveragedXLIFFSentence extends XLIFFSentence {
    private String gMatchQuality = null;

    public LeveragedXLIFFSentence(String aTransUnitId, String aString, String aXMLLang, String aMatchQuality) throws NestableException {
        super(aTransUnitId, aString, aXMLLang);
        gMatchQuality = aMatchQuality;
    }

    public String getMatchQuality() {
        return gMatchQuality;
    }

    public void setMatchQuality(String aMatchQuality) {
        gMatchQuality = aMatchQuality;
    }
}
