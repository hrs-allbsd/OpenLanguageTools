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
