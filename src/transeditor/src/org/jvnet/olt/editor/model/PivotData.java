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

import java.text.SimpleDateFormat;

import java.util.*;

import org.jvnet.olt.editor.util.SgmlCharacter;

import org.w3c.dom.*;


/**
 * PivotData
 * Save the common data that used in Pivot file.
 */
public class PivotData {
    /* The name and version of this TMCi Editor */
    public PivotData() {
    }

    String removeQuote(String s) {
        if (s.startsWith("\"")) {
            s = s.substring(1);
        }

        if (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

    String convertSpecialCharacters(String sentence) {
        StringBuffer sbuf = new StringBuffer(sentence);
        int i = 0;

        while (i < sbuf.length()) {
            char ch = sbuf.charAt(i);

            if (SgmlCharacter.isEntityUnicode(ch)) {
                String entity = SgmlCharacter.getEntityName(ch);
                sbuf.replace(i, i + 1, entity);
                i += entity.length();
            }
            //      else if(SgmlCharacter.isLowerASCII(ch)) {
            //        sbuf.setCharAt(i, (char)(ch - '\ue000'));
            //        i ++;
            //      }
            //      else if(SgmlCharacter.isLessThan(ch)) {
            //        sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //        i ++;
            //      }
            //      else if(SgmlCharacter.isGreaterThan(ch)) {
            //        sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //        i ++;
            //      }
            else {
                i++;
            }
        }

        return sbuf.toString();
    }
}
