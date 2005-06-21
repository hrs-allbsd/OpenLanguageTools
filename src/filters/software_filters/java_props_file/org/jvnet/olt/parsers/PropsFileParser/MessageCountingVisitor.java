
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

public class MessageCountingVisitor implements PropsFileParserVisitor
{
  private int m_iMessageCount = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.toString().equals("message") && node.isHasMessage()) {  m_iMessageCount++; }
    return data;
  }

  public int getMessageCount()
  {
    return m_iMessageCount;
  }

}
