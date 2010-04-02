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

package org.jvnet.olt.parsers.POFileParser;

public class CellFactoryVisitor implements POFileParserVisitor
{
  private Cell[] cell_array = null;
  private int cell_index = 0;

  public CellFactoryVisitor(int num_cells)
  {
    cell_array = new Cell[num_cells];
  }

  public Object visit(SimpleNode node, Object data)
  {
    String strNodeType = node.toString();
    Cell cell = new Cell();
    cell.type = strNodeType;
    cell.actualstr = node.getNodeData();
    if(strNodeType.equals("msgid_text"))
    {
      cell.keystr = node.getKeyData();
    }
    cell_array[cell_index++] = cell;
    return null;
  }

  public Cell[] getCellArray()
  {
    return cell_array;
  }

}
