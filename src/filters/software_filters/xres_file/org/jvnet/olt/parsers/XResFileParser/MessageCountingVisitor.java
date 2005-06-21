
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XResFileParser;

public class MessageCountingVisitor implements XResFileParserVisitor
{
  private int m_iMessageCount = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.toString().equals("message") || node.toString().equals("eof_message")) {  m_iMessageCount++; }
    return data;
  }

  public int getMessageCount()
  {
    return m_iMessageCount;
  }

}
