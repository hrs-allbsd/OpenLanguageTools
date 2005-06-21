
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XmlDocFragmentParser;

import org.jvnet.olt.parsers.XmlTagParser.*;
import java.io.*;

public class StringViewingVisitor 
implements XmlDocFragmentParserVisitor
{

  public Object visit(SimpleNode node, Object data)
  {
    System.out.println(node.toString() + " : " + node.getNodeData());
    
    System.out.println("type = " + node.getType());
    
    if(node.isEmptyTag()) {
        System.out.println("*** Element is empty tag ***");
    }
    
    if(!node.getPrefix().equals("")) {
        System.out.println("*** Prefix is " + node.getPrefix() + " ***");
    }
    
    if(node.getType() == 23) {
        try {
        
        Reader reader = new StringReader(node.getNodeData());
     
        XmlTagParser parser = new  XmlTagParser(reader);
        parser.parse();
        org.jvnet.olt.parsers.XmlTagParser.StringViewingVisitor stringView = new org.jvnet.olt.parsers.XmlTagParser.StringViewingVisitor();
        parser.walkParseTree(stringView, null);
        
        
        } 
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
    }


    return data;
  }
}
