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

package org.jvnet.olt.parsers.XmlDocFragmentParser;

import org.jvnet.olt.parsers.XmlTagParser.*;
import java.io.*;
import java.util.*;

public class XmlDocFragmentQualifierFixerVisitor 
implements XmlDocFragmentParserVisitor {

    private Map namespaceMap = new NamespaceMap();
  
    private Stack namespaceStack = new Stack();
    
    private Stack defaultNamespaceStack = new Stack();
    
    private String defaultNamespace = "";
  
    public Object visit(SimpleNode node, Object data) {       
      
        
    
    if(node.getType() == XmlDocFragmentParserTreeConstants.JJTCLOSE_TAG) {
        if(!namespaceStack.empty()) { 
            namespaceMap = new HashMap((Map) namespaceStack.pop());
        }  
        
        if(!defaultNamespaceStack.empty()) {
            defaultNamespace = (String) defaultNamespaceStack.pop();
        }
        /* XmlFilterHelper.printMap(namespaceMap); */
        node.setNamespaceMap(namespaceMap);
        /* XmlFilterHelper.printStack(namespaceStack); */
    }
        
    if(node.getType() == XmlDocFragmentParserTreeConstants.JJTOPEN_TAG) {
        try {   
            
            if(!namespaceStack.empty()) { 
                namespaceMap = new HashMap((Map) namespaceStack.peek());
            } 
            
            if(!defaultNamespaceStack.empty()) {
                defaultNamespace = (String) defaultNamespaceStack.peek();
            }
        
            Reader reader = new StringReader(node.getNodeData());
         
            XmlTagParser parser = new  XmlTagParser(reader);
            parser.parse();
            XmlTagQualifierFixerVisitor stringView = new XmlTagQualifierFixerVisitor(namespaceMap, defaultNamespace);            
            parser.walkParseTree(stringView, null);
            defaultNamespace = stringView.getDefaultNamespace();
            defaultNamespaceStack.push(defaultNamespace);
            node.setNamespaceMap(namespaceMap);
             
            if(!node.isEmptyTag()) {   
                /* XmlFilterHelper.printMap(namespaceMap); */
                    
                Map namespaceMapCopy = new HashMap(namespaceMap);
                namespaceStack.push(namespaceMapCopy); 
                /* XmlFilterHelper.printStack(namespaceStack); */
                                
            }
        } 
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
    }

    if(!node.getPrefix().equals("")) {
        
        int endString = node.getPrefix().lastIndexOf(":");
        String prefix = node.getPrefix().substring(0, endString);        
        String namespaceID = XmlFilterHelper.stripDoubleQuotes(
            (String) namespaceMap.get(prefix));        
        node.setNamespaceID(namespaceID);
        
    } else if(!defaultNamespace.equals("")) {     
        node.setNamespaceID(defaultNamespace);
        
    }
    
    return data;
  }
   
  
}
