
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software.moz_dtd;
import org.jvnet.olt.parsers.DTDDocFragmentParser.DTDDocFragmentParserVisitor;
import org.jvnet.olt.parsers.DTDDocFragmentParser.DTDDocFragmentParserTreeConstants;
import org.jvnet.olt.parsers.DTDDocFragmentParser.SimpleNode;

public class MessageCountingVisitor implements DTDDocFragmentParserVisitor, DTDDocFragmentParserTreeConstants
{
  private int m_iMessageCount = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.toString().equals("entity_decl_name")) {  m_iMessageCount++; }
    return data;
  }

  public int getMessageCount()
  {
    return m_iMessageCount;
  }

}
