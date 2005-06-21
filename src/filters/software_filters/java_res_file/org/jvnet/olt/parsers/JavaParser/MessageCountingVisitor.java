
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

public class MessageCountingVisitor implements ResBundleParserVisitor
{
  private int m_iMessageCount = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.toString().equals("quoted_text_value")) {  m_iMessageCount++; }
    return data;
  }

  public int getMessageCount()
  {
    return m_iMessageCount;
  }

}
