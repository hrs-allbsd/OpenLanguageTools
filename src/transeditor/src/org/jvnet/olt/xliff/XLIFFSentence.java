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
