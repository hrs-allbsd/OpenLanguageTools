
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
