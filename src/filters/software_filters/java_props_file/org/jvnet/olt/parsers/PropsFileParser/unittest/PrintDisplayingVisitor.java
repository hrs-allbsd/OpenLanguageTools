
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser.unittest;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.Enumeration;
import org.jvnet.olt.parsers.PropsFileParser.*;

public class PrintDisplayingVisitor implements PropsFileParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    if(isNodeDisplaying(node))
    {
      System.out.print(node.getNodeData());
    }
    return data;
  }

  private boolean isNodeDisplaying(SimpleNode node)
  {
    String str = node.toString();    
    return ( str.equals("key") ||
	     str.equals("value") ||
	     str.equals("eof_value") ||
	     str.equals("comment") ||
	     str.equals("eof_comment") ||
	     str.equals("equals") ||
	     str.equals("blank_line")
	     );  
  }

}
