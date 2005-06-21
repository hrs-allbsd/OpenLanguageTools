
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import java.util.Vector;
import java.util.Enumeration;

public class CommentHarvestingVisitor 
implements PropsFileParserVisitor, PropsFileParserTreeConstants
{
  private Vector m_vect;
  
  public CommentHarvestingVisitor()
  {
    m_vect = new Vector();
  }

  public Object visit(SimpleNode node, Object data)
  {
    switch(node.getType())
    {
    case JJTLC_COMMENT_BLOCK:
    case JJTNORMAL_COMMENT_BLOCK:
    case JJTTMC_COMMENT_BLOCK:
      m_vect.addElement(node.getNodeData());
      break;
    default:
      //  Consign everything else to /dev/null
      break;
    }
    return data;
  }

  public String[] getHarvestedComments()
  {
    int iElements = m_vect.size();
    int i = 0;

    String[] array = new String[iElements];

    Enumeration myenum = m_vect.elements();
    while(myenum.hasMoreElements() && (i < iElements))
    {
      array[i++] = (String) myenum.nextElement();
    }
    return array;
  }

}
