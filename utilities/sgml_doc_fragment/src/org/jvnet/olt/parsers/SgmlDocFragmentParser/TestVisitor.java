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

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.util.Map;
import java.util.Iterator;

/**
 * Visitor used by junit framework to test the SgmlDocFragmentParser
 */
public class TestVisitor implements SgmlDocFragmentParserVisitor
{

  StringBuffer result = null;
  
  public TestVisitor() {
      result = new StringBuffer();
  }
    
  public Object visit(SimpleNode node, Object data)
  {
    if("".equals(node.getTagName())) {
        return data;
    }
    
    result.append("Name: ").append(node.getTagName()).append("\n");
    Map map = node.getAttribs();
    
    result.append("Attribs: ");
    Iterator it = map.keySet().iterator();
    while(it.hasNext()) {
        String name = (String)it.next();
        String value = (String)map.get(name);
        result.append(" ").append(name).append("=").append(value);
    }
    result.append("\n");
    
    result.append("Data: ").append(node.getNodeData()).append("\n");
    return data;
  }
  
  public String getResult() {
      return result.toString();
  }
  
}
