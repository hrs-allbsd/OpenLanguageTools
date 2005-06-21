
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

public class TreeViewingVisitor implements ResBundleParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    if((node.getNodeData()).equals(""))
    {
      System.out.print(node.toString() + " :\tNon-displaying\n");
    }
    else
    {
      System.out.print(node.toString() + " :\tDisplaying\n");      
    }
    return data;
  }
}
