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
package org.jvnet.olt.editor.util;

import org.jvnet.olt.editor.format.DefaultFormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.model.PivotBaseElement;


/**
 *
 */
public class BaseElements {
    private static FormatElementExtractor extractor;

    static public String convert(String source) {
        StringBuffer temp = new StringBuffer(0);
        StringBuffer s = new StringBuffer(source);

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '<') {
                temp.append("&lt;");
            } else if (s.charAt(i) == '>') {
                temp.append("&gt;");
            } else {
                temp.append(c);
            }
        }

        return temp.toString();
    }

    //private static String[] strTokens = {"<",">","img","alt=","&",";","\""};
    //private Vector vResult;
    static public String abbreviateContent(String source) {
        Object[] v = extractContent(source);
        StringBuffer temp = new StringBuffer(0);

        for (int i = 0; i < v.length; i++) {
            PivotBaseElement p = (PivotBaseElement)v[i];

            if (p.getFlag()) {
                if (p.getContent().startsWith("<")) {
                    temp.append("&lt;");
                } else if (p.getContent().startsWith("&")) {
                    temp.append("&");
                }
            } else {
                temp.append(convert(p.getContent()));
            }
        }

        return temp.toString();
    }

    /** This method delegates parsing of text to the <CODE>FormatElementExtractor</CODE> and
     * uses the extractor to get the plain text component of the string without
     * any of the formatting.
     */
    static public String pureText(String source) {
        return extractor.extractText(source);
    }

    /** This method delegates parsing of the input string to the <CODE>FormatElementExtractor</CODE>
     * and uses it to produce an array of <CODE>PivotBaseElement</CODE> objects.
     * If no <CODE>FormatElementExtractor</CODE> is set, a default one is created.
     */
    static public PivotBaseElement[] extractContent(String source) {
        //  Guard clause to ensure we have an extractor set. Fixes bug 5019188.
        if (BaseElements.extractor == null) {
            BaseElements.setFormatExtractor(new DefaultFormatElementExtractor());
        }

        //  Delegate the extraction of formatting to the extractor.
        return extractor.extractBaseElements(source);
    }

    public static synchronized void setFormatExtractor(FormatElementExtractor extract) {
        if (extract != null) {
            BaseElements.extractor = extract;
        }
    }

    public static synchronized void setFormatExtractorVariableManager(org.jvnet.olt.format.GlobalVariableManager gvm) {
        if (gvm != null) {
            BaseElements.extractor.setVariableManager(gvm);
        }
    }
}


class Index {
    int start;
    int end;
}
