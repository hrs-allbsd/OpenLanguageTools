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

import java.util.List;


/**
 * <p>Title: Open Language Tools XLIFF Translation Editor</p>
 * <p>Description: This class is the definition of a tag element of XLIFF file, and it is used in the feature of Update Tags.</p>
 * @version 1.0
 */
public class ContentTag {
    public static final int COMPARE_TAGS_EQUAL = 0;
    public static final int COMPARE_TAGS_EQUAL_ATTRIBUTES_DIFF = 1;
    public static final int COMPARE_TAGS_NOT_EQUAL = -1;
    
    
    
    public String strTagName;
    public int iIndex;
    public int iPos;
    public String strText;
    public String strAttribute;
    public boolean bFlag = false; // true = self-closed tag, false = non self-closed tag

    /**
     * This constructor will construct an instance of ContentTag
     * @param strTagNameInput. <p> it keeps the name of a tag. </p>
     * @param iIndexInput <p> it keeps the order of which appear in a segment. For Example, <A> this is a <IMG href="a.gif"> text </IMG></A>, the iIndexInput of Tag A is 0, which IMG is 1.</p>
     * @param iPosInput <p> it keeps the start postion of a tag appeared in a segment. </p>
     * @param strTextInput <p> the whole text of a Tag. </p>
     */
    public ContentTag(String strTagNameInput, int iIndexInput, int iPosInput, String strTextInput) {
        this.strTagName = strTagNameInput;
        this.iIndex = iIndexInput;
        this.iPos = iPosInput;
        this.strText = strTextInput;
        this.strAttribute = strText.substring(strText.indexOf(strTagName) + strTagName.length(), strText.length() - 1);
        this.strAttribute = this.strAttribute.trim();

        if (strAttribute.endsWith("/") == true) {
            bFlag = true;
            strAttribute = strAttribute.substring(0, strAttribute.length() - 1);
        }
    }

    /**
     * compare whether two contentTags are the same.
     * @param ctInput. <p>ContentTag to be compared </p>
     * @return <p> true--- completely the same, that is, except the case difference, the tags is quite the same.
     * false ---- not the same, include different tag ans different attibute factors. </P>
     *
     */
    public int compare(ContentTag ctInput) {
        if (ctInput == null) {
            return -1;
        }

        if (this.strTagName.compareToIgnoreCase(ctInput.strTagName) == 0) {
            if (strAttribute.compareToIgnoreCase(ctInput.strAttribute) == 0) {
                return 0; //completely same
            } else {
                return 1; //tag same, but attrbute different;
            }
        } else {
            return -1;
        }
    }

    public String toString() {
	return "ContentTag: name:"+strTagName+" idx:"+iIndex+" pos:"+iPos+" text:"+strText+" attr:"+strAttribute;
    }
    
    
}