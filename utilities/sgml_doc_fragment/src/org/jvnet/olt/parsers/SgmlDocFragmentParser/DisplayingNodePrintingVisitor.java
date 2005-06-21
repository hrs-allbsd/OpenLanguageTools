
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.io.Writer;
import java.io.IOException;

public class DisplayingNodePrintingVisitor 
implements SgmlDocFragmentParserVisitor
{
  private Writer m_writer;

  public DisplayingNodePrintingVisitor(Writer writer)
  {
    //  Guard clause
    if(writer == null) 
      { throw new IllegalArgumentException("The writer passed to the Visitor was \"null\""); }

    m_writer = writer;
  }


  public Object visit(SimpleNode node, Object data)
  {
    try
    {
      if(node.isDisplayingNode())
      {
	m_writer.write(node.getNodeData());
      }
    }
    catch(IOException ioEx)
    {
      ioEx.printStackTrace();
    }
    return data;
  }
}

