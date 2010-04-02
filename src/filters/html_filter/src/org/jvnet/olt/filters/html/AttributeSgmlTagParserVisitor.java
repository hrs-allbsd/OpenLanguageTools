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
 * TagNameSgmlTagParserVisitor.java
 *
 * Created on June 27, 2002, 1:23 PM
 */

package org.jvnet.olt.filters.html;

import org.jvnet.olt.parsers.SgmlTagParser.*;

/**
 *
 * @author  timf
 */
public class AttributeSgmlTagParserVisitor implements SgmlTagParserVisitor {
    
    private String tagname;
    private String attribute;
    private String attributeVal;
    private boolean inTag = false;
    private boolean inAttribute = false;
    
    /** Creates a new instance of TagNameSgmlTagParserVisitor */
    public AttributeSgmlTagParserVisitor(String tagname, String attribute) {
        this.tagname = tagname;
        this.attribute = attribute;
        this.attributeVal = "";
    }
    
    public Object visit(org.jvnet.olt.parsers.SgmlTagParser.SimpleNode simpleNode, Object obj) {
        switch (simpleNode.getType()){
            case SgmlTagParserTreeConstants.JJTTAGNAME:
                if (simpleNode.getNodeData().toLowerCase().equals(tagname.toLowerCase())){
                    inTag = true;
                } else {
                    inTag = false;
                    inAttribute = false;
                }
                break;
            case SgmlTagParserTreeConstants.JJTATTNAME:
                if (simpleNode.getNodeData().toLowerCase().equals(attribute.toLowerCase())){
                    inAttribute = true;
                } else inAttribute = false;
                break;
            case SgmlTagParserTreeConstants.JJTQUOTED_VALUE:
                if (inAttribute){
                    attributeVal = simpleNode.getNodeData().substring(1,simpleNode.getNodeData().length()-1);
                    
                }
                break;
            case SgmlTagParserTreeConstants.JJTUNQUOTED_VALUE:
                if (inAttribute){
                    attributeVal = simpleNode.getNodeData();
                    
                }
                break;
        }
        return null;
    }
    
    public String getAttributeValue(){
        if (attributeVal == null)
            attributeVal = "";
        return attributeVal;
    }
}
