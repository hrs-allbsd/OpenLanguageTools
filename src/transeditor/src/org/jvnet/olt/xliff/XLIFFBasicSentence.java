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
