
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

public class StringViewingVisitor implements POFileParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    System.out.println(node.toString() + " : " + node.getNodeData());
    if(node.toString().equals("message"))
      System.out.println(node.toString() + "-->" + node.isHasMsgStr());
    return data;
  }
}
