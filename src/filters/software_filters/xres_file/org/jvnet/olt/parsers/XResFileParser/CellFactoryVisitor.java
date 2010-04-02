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

package org.jvnet.olt.parsers.XResFileParser;

import java.util.*;

public class CellFactoryVisitor 
implements XResFileParserVisitor, XResFileParserTreeConstants
{
  private Vector m_vectCells = null;
  private String m_currentKey;

  public CellFactoryVisitor()
  {
    m_currentKey = "";
    m_vectCells = new Vector();
  }

  public Object visit(SimpleNode node, Object data)
  {
    if(isDisplayingNode(node))
    {
      //  Create a new cell
      Cell cell = new Cell();
      int iType = node.getNodeType();

      cell.type = iType;
      switch(iType)
      {
      case JJTKEY:
	m_currentKey = node.getNodeData();
	cell.actualstr = node.getNodeData();
	cell.keystr = "";
	break;
      case JJTVALUE:
	cell.actualstr = node.getNodeData();
	cell.keystr = m_currentKey;
	m_currentKey = "";
	break;
      default:
	cell.actualstr = node.getNodeData();
	cell.keystr = "";
	break;
      }
      m_vectCells.addElement(cell);
    }
    return null;
  }

  public Cell[] getCellArray()
  {
    Enumeration myenum = m_vectCells.elements();
    Cell[] cell_array = new Cell[ m_vectCells.size() ];
    for(int i = 0; i < m_vectCells.size(); i++)
    {
      cell_array[i] = (Cell) myenum.nextElement();
    }
    return cell_array;
  }

  public boolean isDisplayingNode(SimpleNode node)
  {
    switch(node.getNodeType())
    {
      
    case JJTCOMMENT:  //  Make use of implicit drop through       
    case JJTEOF_COMMENT:
    case JJTBLANK_LINE:
    case JJTKEY:
    case JJTEQUALS:
    case JJTVALUE:
    case JJTINCLUDE:
    case JJTSLASHNEWLINE_WRAPPER:
    case JJTNEWLINE:
      return true;
    default:
      return false;
    }
  }

}
