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

/**
 * This visitor normalises everything except <suntransxmlfilter> tags
 * using JohnC's whitespace normalising function. It needs to use the
 * NonConformantSgmlDocFragment parser, since we could be asked to
 * normalise input that's not necessarily valid sgml (text containing
 * a marked section open tag, but no marked section closing tag)
 * 
 */

package org.jvnet.olt.filters.sgml.visitors;


import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import java.io.StringReader;


public class TextNormalisingVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    
    private boolean inValueAttr= false;
    private String valueStr = "";
    private StringBuffer text = new StringBuffer();
    private int id=1;
    

    
    /** get the string that has been extracted from the input (the string we were
     * protecting)
     * @return the protected string
     */    
    public String getText(){
        String t = text.toString();
        return t;
    }
    
    /** an doc fragment visitor to look for suntransxmlfilter tags, and not
     * normalizing those, but normalising everything else.
     *
     * @param simpleNode node
     * @param obj obj
     * @return obj
     */    
    public Object visit(org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode simpleNode, Object obj) {
        switch (simpleNode.getType()){
            case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                valueStr = "";
                if (simpleNode.getTagName().equals("suntransxmlfilter")){
                    text.append(simpleNode.getNodeData());                   
                } else {
                    text.append(SgmlFilterHelper.sgmlNormalise(new StringBuffer(simpleNode.getNodeData())));
                }
                break;
            default:
                text.append(SgmlFilterHelper.sgmlNormalise(new StringBuffer(simpleNode.getNodeData())));
                
        }
        return null;
    }
    
    
    
 
    
}
