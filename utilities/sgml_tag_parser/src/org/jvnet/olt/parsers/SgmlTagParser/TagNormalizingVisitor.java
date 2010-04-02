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

package org.jvnet.olt.parsers.SgmlTagParser;

import java.io.Writer;
import java.io.IOException;

public class TagNormalizingVisitor 
implements SgmlTagParserVisitor, SgmlTagParserTreeConstants
{
  private Writer  m_writer;
  private boolean m_boolEmptyTag;

  public TagNormalizingVisitor(Writer writer, boolean boolEmptyTag)
  {
    m_writer = writer;
    m_boolEmptyTag = boolEmptyTag;
  }

  public Object visit(SimpleNode node, Object data)
  {
    try
    {
      if(node.isDisplayingNode())
      {
	String string = node.getNodeData();
	switch(node.getType())
        {
	case JJTTAGNAME:	
	case JJTATTNAME:
	  m_writer.write(string.toLowerCase());
	  break;
	case JJTUNQUOTED_VALUE:
	  m_writer.write("\"" + string + "\"");
	  break;
	case JJTETAG:
	  //  Add a closing '/' if we are dealing with an EMPTY tag
	  if(m_boolEmptyTag) { m_writer.write('/'); }
	  m_writer.write(string);	  
	  break;
	default:
	  m_writer.write(string);
	  break;
	}
      }
    }
    catch(IOException ioEx)
    {
      //  We are in a heap of trouble if we get here.
      ioEx.printStackTrace();
    }
    return data;
  }
}
