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

import java.util.*;

public class SkipListFactoryVisitor
implements MsgFileParserVisitor
{
  private Vector m_skiplist;
  long countNodes;

  public SkipListFactoryVisitor()
  {
    m_skiplist = new Vector();
    countNodes = 0;
  }

  public Object visit(SimpleNode node, Object data)
  {
    m_skiplist.addElement(node);
    countNodes++;
    
    return data;
  }
  
  public SimpleNode[] getSkipList()
  {
    SimpleNode[] array = new SimpleNode[m_skiplist.size()];
    
    Enumeration myenum = m_skiplist.elements();
    int i = 0;

    while((myenum.hasMoreElements()) && (i < m_skiplist.size()))
    {
      array[i++] = (SimpleNode) myenum.nextElement();
    }

    return array;
  }

}
