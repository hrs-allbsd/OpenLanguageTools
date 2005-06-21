
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import java.util.*;

public class SkipListFactoryVisitor
implements MsgFileParserVisitor
{
  private Vector m_skiplist;
  long countNodes;

  public SkipListFactoryVisitor()
  {
    m_skiplist = new Vector();
    countNodes = 0;
  }

  public Object visit(SimpleNode node, Object data)
  {
    m_skiplist.addElement(node);
    countNodes++;
    
    return data;
  }
  
  public SimpleNode[] getSkipList()
  {
    SimpleNode[] array = new SimpleNode[m_skiplist.size()];
    
    Enumeration myenum = m_skiplist.elements();
    int i = 0;

    while((myenum.hasMoreElements()) && (i < m_skiplist.size()))
    {
      array[i++] = (SimpleNode) myenum.nextElement();
    }

    return array;
  }

}
