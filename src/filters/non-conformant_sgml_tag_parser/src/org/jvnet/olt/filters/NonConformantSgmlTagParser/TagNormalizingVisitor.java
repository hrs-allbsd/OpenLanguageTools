
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.NonConformantSgmlTagParser;

import java.io.Writer;
import java.io.IOException;

public class TagNormalizingVisitor 
implements NonConformantSgmlTagParserVisitor, NonConformantSgmlTagParserTreeConstants
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
