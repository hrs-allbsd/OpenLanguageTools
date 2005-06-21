
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XmlTagParser;

public class StringViewingVisitor 
implements XmlTagParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    System.out.println(node.toString() + " : " + node.getNodeData()); 
    
    if(!node.getPrefix().equals("")) {
        System.out.println("*** Prefix is " + node.getPrefix() + " ***");
    }
    
    return data;
  }
}
