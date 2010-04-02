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

import java.util.Vector;
import java.util.logging.Logger;

import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.editor.util.BaseElements;
import org.jvnet.olt.editor.util.SgmlCharacter;

public class SimpleSentence {
    private static final Logger logger = Logger.getLogger(SimpleSentence.class.getName());
    String value = "";
    Backend backend;

    //    PivotBaseElement[] baseElements;
    public SimpleSentence(String s) {
        value = convertSpecialCharacters(s);
    }

    public SimpleSentence(String s, String matchSrc, String matchT, boolean updateTags) {
        value = SimpleSentenceHelper.formatTranslation(s, matchSrc, matchT,updateTags);
	logger.finest("SimpleSentence");
	logger.finest("s       :"+s);
	logger.finest("matchSrc:"+matchSrc);
	logger.finest("matchT  :"+matchT);
	logger.finest("value   :"+value);
    }
 

    
    
    public String convertSpecialCharacters(String sentence) {
        StringBuffer sbuf = new StringBuffer(sentence);
        int i = 0;

        while (i < sbuf.length()) {
            char ch = sbuf.charAt(i);
            if (SgmlCharacter.isEntityUnicode(ch)) {

                //skip quotes to make sure they display as quotes
		if(ch == '\u201D' || //rdquo
                   ch == '\u201C' || //ldquo
                   ch == '\u2018' || //lsquo
                   ch == '\u2019' || //rsquo
                   ch == '\u201E' ){   //bdquo
                    i++;
                    continue;
                }		
		
                String entity = SgmlCharacter.getEntityName(ch);
                sbuf.replace(i, i + 1, entity);
                i += entity.length();
            }
            //            else if(SgmlCharacter.isLowerASCII(ch)) {
            //                sbuf.setCharAt(i, (char)(ch - '\ue000'));
            //                i ++;
            //            }
            //            else if(SgmlCharacter.isLessThan(ch)) {
            //                sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //                i ++;
            //            }
            //            else if(SgmlCharacter.isGreaterThan(ch)) {
            //                sbuf.setCharAt(i, (char)(ch  - '\u3d00'));
            //                i ++;
            //            }
            else {
                i++;
            }
        }

        return sbuf.toString();
    }

    public PivotBaseElement[] getBaseElementsObject() {
        //        return baseElements;  //  Try the lazy instantiation thing.
        return BaseElements.extractContent(value);
    }

    //    public void setBaseElementsObject(PivotBaseElement[] oInput) {
    //        baseElements = oInput;
    //    }
    public String getValue() {
        return value;
    }

    public void setValue(String s) {
        value = s;
    }

    public void setValue(String s, Vector v) {
        value = s;
    }
    
    public String toString(){
	return "SimpleSentence:"+value;
    }
}
