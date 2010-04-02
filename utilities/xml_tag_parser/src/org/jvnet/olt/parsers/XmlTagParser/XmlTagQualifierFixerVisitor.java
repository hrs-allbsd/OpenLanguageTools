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

package org.jvnet.olt.parsers.XmlTagParser;

import java.util.Map;

public class XmlTagQualifierFixerVisitor 
implements XmlTagParserVisitor
{
  
  private Map namespaceMap;
  private String lastTagName = "";
  private String defaultNamespace = "";
  private boolean defaultNamespacePresent = false;
  
  public XmlTagQualifierFixerVisitor(Map theNamespaceMap, 
    String defaultNamespace) {
      this.namespaceMap = theNamespaceMap;
      this.defaultNamespace = defaultNamespace;
  }

  public Object visit(SimpleNode node, Object data)
  {        
    if(node.getType() == XmlTagParserTreeConstants.JJTATTNAME && node.getPrefix().equals("xmlns:")) {
        lastTagName = node.getNodeData();
    }
    
    if(node.getType() == XmlTagParserTreeConstants.JJTATTNAME &&  
        node.getNodeData().equals("xmlns")) {
        defaultNamespacePresent = true;
    }
        
    if(node.getType() == XmlTagParserTreeConstants.JJTQUOTED_VALUE && !(lastTagName.equals(""))){
        
        namespaceMap.put(lastTagName, XmlFilterHelper.stripDoubleQuotes(node.getNodeData()));
        lastTagName = "";
        
    }
    
    if(node.getType() == XmlTagParserTreeConstants.JJTQUOTED_VALUE && 
        defaultNamespacePresent){
        defaultNamespace = 
            XmlFilterHelper.stripDoubleQuotes(node.getNodeData());
        defaultNamespacePresent = false;
    }
        
    if(node.getType() == XmlTagParserTreeConstants.JJTQUOTED_VALUE && !node.getPrefix().equals("")) {
        
        int endString = node.getPrefix().lastIndexOf(":");
        String prefix = node.getPrefix().substring(0, endString);        
        String namespaceID = XmlFilterHelper.stripDoubleQuotes(
            (String) namespaceMap.get(prefix));        
        node.setNamespaceID(namespaceID);
        
    }
        
    
    return data;
  }
  
  public String getDefaultNamespace() {
      return defaultNamespace;
  }
  
}
