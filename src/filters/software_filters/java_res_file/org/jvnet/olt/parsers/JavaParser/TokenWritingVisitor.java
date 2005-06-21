
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

import java.io.*;

public class TokenWritingVisitor implements ResBundleParserVisitor
{
  PrintStream output;

  public TokenWritingVisitor(PrintStream stream)
  {
    output = stream;
  }

  public Object visit(SimpleNode node, Object data)
  {
    if(node.isDisplayingNode())
    {
      System.out.println(node.toString() + " :\t" + node.getNodeData());
    }
    return null;
  }
}
