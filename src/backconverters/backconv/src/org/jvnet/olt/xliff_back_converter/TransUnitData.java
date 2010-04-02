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

/*
 * TransUnitData.java
 *
 * Created on 16 February 2004, 14:05
 */

package org.jvnet.olt.xliff_back_converter;

/** This class encapsulates translation status 
 *
 * @author  jc73554
 */
public class TransUnitData {
    
    private String translationStatus;
    
    private StringBuffer textBuffer;
    
    /** Creates a new instance of TransUnitData */
    public TransUnitData() {
        textBuffer = new StringBuffer();
        translationStatus = "untranslated";
    }
    
    /** Gets the text of the trans-unit along with its formatting etc.
     */
    public String getText() {
        return textBuffer.toString();
    }
    
    /** Appends the text provided to the trans-unit buffer.
     */
    public void appendToText(String subString) {
        textBuffer.append(subString);
    }

        
    /** Accessor for the translationStatus property.
     */
    public String getTranslationStatus() {
        return translationStatus;
    }
    
    /** Mutator for the translationStatus property.
     */
    public void setTranslationStatus(String status) {
        //  Note: this is not implemented the best. The change of translation type
        //  is needed to comply with requirements for the output of the back 
        //  conversion.
        if(status.startsWith("non-translated")) {
            translationStatus = "untranslated";
        } else if(status.startsWith("fuzzy")) {
            translationStatus = "fuzzy";            
        } else if(status.startsWith("user")) {
            translationStatus = "user";            
        } else if(status.startsWith("100-Match") || status.startsWith("auto-translated")) {
            translationStatus = "exact";            
        }
    }
    
}
