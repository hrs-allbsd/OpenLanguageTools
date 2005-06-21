
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

public class TokenCountingVisitor implements ResBundleParserVisitor
{
  private int m_iNumTokens = 0;
  private int m_iTransTokens = 0;

  public Object visit(SimpleNode node, Object data)
  {
    if(node.isDisplayingNode())
    {
      m_iNumTokens++;
      if(node.isTranslatable())
      {
	m_iTransTokens++;
      }
    }
    return null;
  }

  public int getNumTokens()
  {
    return m_iNumTokens;
  }

  public int getTransNumTokens()
  {
    return m_iTransTokens;
  }

}
