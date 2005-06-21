
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.laxparsers.software;

import java.util.*;
import java.io.StringReader;
public class StringViewingVisitor implements LaxSoftwareMessageParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    System.out.println(node.toString() + " : " + node.getNodeData());


    return data;
  }
}
