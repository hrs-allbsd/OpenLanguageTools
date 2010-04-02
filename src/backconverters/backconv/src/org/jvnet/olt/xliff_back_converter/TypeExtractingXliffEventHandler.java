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
 * TypeExtractingXliffEventHandler.java
 *
 * Created on June 21, 2004, 2:39 PM
 */

package org.jvnet.olt.xliff_back_converter;

/** This class is concerned with determining what the values of attributes in 
 * the 'file' element are, and passing that on to apps.
 * @author  jc73554
 */
public class TypeExtractingXliffEventHandler extends AbstractXliffEventHandler {
    
    private String originalType = "";
    
    /** Creates a new instance of TypeExtractingXliffEventHandler */
    public TypeExtractingXliffEventHandler() {
    }
    
    /** This method is an XML parser event handler for XLIFF 'file' elements. It is
     * fired whenever an opening tag for a 'file' element is encountered.
     */
    public void start_file(org.xml.sax.Attributes meta) {
        //  Get attribute value for "datatype"
        String attrib = meta.getValue("datatype");
        
        if(attrib != null) {
            originalType = attrib;
        }
    }
    
    public String getOriginalType() {
        return originalType;
    }
}
