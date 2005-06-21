/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import org.jvnet.olt.editor.util.NestableException;


public class XLIFFBasicSentence {
    private String gSentence = null;
    private String gXMLLang = null;

    public XLIFFBasicSentence(String aString, String aXMLLang) {
        gSentence = aString;
        gXMLLang = aXMLLang;
    }

    public String getSentence() {
        return gSentence;
    }

    public void setSentence(String aSentence) throws NestableException {
        if (aSentence == null) {
            throw new NestableException("Can not create a null sentence");
        }

        gSentence = aSentence;
    }

    public String getVisibleSentence() {
        return gSentence;
    }

    public String getXMLLang() {
        return gXMLLang;
    }

    public void setXMLLang(String aXMLLang) {
        gXMLLang = aXMLLang;
    }

    public String toString() {
        return getSentence();
    }
}
