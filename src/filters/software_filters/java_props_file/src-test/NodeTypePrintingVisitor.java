
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import java.io.Writer;
import java.io.IOException;


public class NodeTypePrintingVisitor 
implements PropsFileParserVisitor
{
  private Writer m_writer;
  private int iNodeCount;

  public NodeTypePrintingVisitor(Writer writer)
  {
    m_writer = writer;
    iNodeCount = 1;
  }

  
  public Object visit(SimpleNode node, Object data)
  {
    try
    {
      m_writer.write(iNodeCount++ + ":" + node.toString() + "\n");
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
    }
    return data;
  }

}
