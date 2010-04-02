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

package org.jvnet.olt.parsers.MsgFileParser;

import java.io.*;

public class DisplayingNodePrintingVisitor
implements MsgFileParserVisitor
{
  private Writer m_writer;

  public DisplayingNodePrintingVisitor(Writer writer)
  {
    m_writer = writer;
  }

  public Object visit(SimpleNode node, Object data)
  {
    try
    {
      if(isNodeDisplaying(node))
      {
	m_writer.write(node.getNodeData());
      }
    }
    catch(IOException ex)
    {
      System.err.println("Problem writing out text.");
    }
    return data;
  }


  private boolean isNodeDisplaying(SimpleNode node) 
  {
    String str = node.toString();    
    return ( str.equals("comment_block") ||
	     str.equals("newline") ||
	     str.equals("blank_line") ||
	     str.equals("white_space_stored") ||
	     str.equals("single_white_space_stored") ||
	     
	     str.equals("msg_number") ||
	     str.equals("value") ||
	     
	     str.equals("directive_comment") ||
	     str.equals("directive") ||
	     str.equals("set") ||
	     str.equals("set_number")
	     );  
  }
}
