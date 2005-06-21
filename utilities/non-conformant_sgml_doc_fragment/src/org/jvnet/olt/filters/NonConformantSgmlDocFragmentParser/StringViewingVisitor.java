
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser;

public class StringViewingVisitor 
implements NonConformantSgmlDocFragmentParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    System.out.println(node.toString() + " : " + node.getNodeData());
    if (node.isEmptyTag()){
        System.out.println(node.getNodeData() +" is empty !");
    } else {
        System.out.println("not empty");
    }
    return data;
  }
}
