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
