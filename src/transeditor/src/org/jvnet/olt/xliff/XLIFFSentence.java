/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.util.StringTokenizer;
import java.util.Vector;

import org.jvnet.olt.editor.util.NestableException;


public class XLIFFSentence extends XLIFFBasicSentence {
    private String gTransUnitId = null;
    private String gCurrentState = null;
    private String gCurrentStateQualifier = null;

    public XLIFFSentence(String aString, String aXMLLang, String aTransUnitId) {
        super(aString, aXMLLang);
        gTransUnitId = aTransUnitId;
    }

    public XLIFFSentence(String aString, String aXMLLang, String aTransUnitId, String aCurrentState) {
        super(aString, aXMLLang);
        gTransUnitId = aTransUnitId;
        gCurrentState = aCurrentState;
    }

    public XLIFFSentence(String aString, String aXMLLang, String aTransUnitId, String aCurrentState, String aCurrentStateQualifier) {
        this (aString, aXMLLang, aTransUnitId, aCurrentState);
        gCurrentStateQualifier = aCurrentStateQualifier;
    }

    public String getTranslationState() {
        return gCurrentState;
    }

    public void setTranslationState(String aState) {
        gCurrentState = aState;
    }

    public String getTranslationStateQualifier() {
        return gCurrentStateQualifier;
    }

    public void setTranslationStateQualifier(String aStateQualifier) {
        gCurrentStateQualifier = aStateQualifier;
    }

    public String getTransUnitId() {
        return gTransUnitId;
    }

    public void setTransUnitId(String aTransUnitId) {
        gTransUnitId = aTransUnitId;
    }

    public Object clone() {
        return new XLIFFSentence(new String(this.getSentence()), new String(this.getXMLLang()), new String(this.gTransUnitId));
    }
}
