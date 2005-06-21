
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser.unittest;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.Enumeration;
import org.jvnet.olt.parsers.POFileParser.*;

public class PrintDisplayingVisitor implements POFileParserVisitor
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
    return ( str.equals("comment_hack") ||
	     str.equals("ws_hack") ||
	     str.equals("newline_hack") ||
	     str.equals("msgid_hack") ||
	     str.equals("msgid_text") ||
	     str.equals("msgstr_hack") ||
	     str.equals("msgstr_text") ||
	     str.equals("domain_hack") ||
	     str.equals("domain_text")
	     );  
  }
}
