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

package org.jvnet.olt.parsers.PropsFileParser;

import java.util.Vector;
import java.util.Enumeration;

public class CommentHarvestingVisitor 
implements PropsFileParserVisitor, PropsFileParserTreeConstants
{
  private Vector m_vect;
  
  public CommentHarvestingVisitor()
  {
    m_vect = new Vector();
  }

  public Object visit(SimpleNode node, Object data)
  {
    switch(node.getType())
    {
    case JJTLC_COMMENT_BLOCK:
    case JJTNORMAL_COMMENT_BLOCK:
    case JJTTMC_COMMENT_BLOCK:
      m_vect.addElement(node.getNodeData());
      break;
    default:
      //  Consign everything else to /dev/null
      break;
    }
    return data;
  }

  public String[] getHarvestedComments()
  {
    int iElements = m_vect.size();
    int i = 0;

    String[] array = new String[iElements];

    Enumeration myenum = m_vect.elements();
    while(myenum.hasMoreElements() && (i < iElements))
    {
      array[i++] = (String) myenum.nextElement();
    }
    return array;
  }

}
