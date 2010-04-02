/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

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
    if (!node.getNamespaceID().equals("")){
        System.out.println("Namespace ID is "+node.getNamespaceID()+" ****");
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
