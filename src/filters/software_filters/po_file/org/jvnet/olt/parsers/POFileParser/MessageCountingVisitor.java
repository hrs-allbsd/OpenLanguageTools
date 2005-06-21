
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

public class MessageCountingVisitor implements POFileParserVisitor
{
  private int m_iMessageCount = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.toString().equals("message") || node.toString().equals("msgid_plural_hack")) {  m_iMessageCount++; }

    //  Should not count the blank HEADER msgid of GNU .po
    if(node.toString().equals("msgid_text") && node.getKeyData().equals("\"\"")) { m_iMessageCount--; }

    return data;
  }

  public int getMessageCount()
  {
    return m_iMessageCount;
  }

}
