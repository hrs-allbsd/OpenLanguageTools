
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser.unittest;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.Enumeration;
import org.jvnet.olt.parsers.JavaParser.*;

public class PrintDisplayingVisitor implements ResBundleParserVisitor
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
    return (str.equals("assign_hack") ||
	    str.equals("class_hack") ||
	    str.equals("comma_hack") ||
	    str.equals("formal_comment_hack") ||
	    str.equals("import_hack") ||
	    str.equals("lcurly_hack") ||
	    str.equals("lparen_hack") ||
	    str.equals("multi_line_comment_hack") ||
	    str.equals("object_hack") ||
	    str.equals("other_chars_hack") ||
	    str.equals("package_hack") ||
	    str.equals("quoted_text_value") ||
	    str.equals("rcurly_hack") ||
	    str.equals("rparen_hack") ||
	    str.equals("semicolon_hack") ||
	    str.equals("single_line_comment_hack") ||
	    str.equals("string_literal_hack") ||
	    str.equals("ws_hack") ||
	    str.equals("eof_hack")
	     ); 

    //
    //  Note:  Keys are not included in here as they are 
    //         made up from a number of other nodes. There
    //         is the ability to pass keys to the translatable
    //         TokenCell.
    //
 
  }
}
