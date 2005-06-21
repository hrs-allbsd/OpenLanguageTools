
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;


public class MessageArrayFactoryVisitor 
implements ResBundleParserVisitor, ResBundleParserTreeConstants
{
  private int m_iNumMessages;
  private String[][] m_arrMessages;
  private String m_strDomain;
  private String m_currComment;
  private int m_curmsg = 0;

  public MessageArrayFactoryVisitor(int iNumMessages,String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_iNumMessages = iNumMessages;
    m_arrMessages = new String[m_iNumMessages][4];
    m_currComment = "";
  }

  public Object visit(SimpleNode node, Object data)
  {
    switch(node.getType())
    {
    case JJTLC_COMMENT_HACK:
      m_currComment = node.getNodeData();
      break;
    case JJTQUOTED_TEXT_KEY:
      try
      {
	String str = node.getNodeData();
	data = str.substring(1,str.length() - 1);
      }
      catch(StringIndexOutOfBoundsException exStrIdx) 
      {
	exStrIdx.printStackTrace(); 
      }     
      break;
    case JJTSIMPLE_KEY:
      data = node.getNodeData();
      break;
    case JJTQUOTED_TEXT_VALUE:
      if((data != null) && (m_curmsg < m_iNumMessages))
      {
	try
	{
	  String str = node.getNodeData();
	  m_arrMessages[m_curmsg][0] = data.toString();
	  m_arrMessages[m_curmsg][1] = m_strDomain;
	  m_arrMessages[m_curmsg][2] = str.substring(1,str.length() - 1);
	  m_arrMessages[m_curmsg][3] = m_currComment;
	}
	catch(StringIndexOutOfBoundsException exStrIdx) 
	{
	  exStrIdx.printStackTrace(); 
	}
	m_currComment = "";
	m_curmsg++;
      }
      else
      {
	//  Throw some exception here
      }
      data = null;
      break;
    default:
      break;
    }

    /*
    String strNodeType = node.toString();

    if(strNodeType.equals("quoted_text_key"))
    {
      try
      {
	String str = node.getNodeData();
	data = str.substring(1,str.length() - 1);
      }
      catch(StringIndexOutOfBoundsException exStrIdx) 
      {
	exStrIdx.printStackTrace(); 
      }
    }
    else
      if(strNodeType.equals("simple_key"))
    {
      data = node.getNodeData();
    }
    else 
      if(strNodeType.equals("quoted_text_value"))
    {
      if((data != null) && (m_curmsg < m_iNumMessages))
      {
	try
	{
	  String str = node.getNodeData();
	  m_arrMessages[m_curmsg][0] = data.toString();
	  m_arrMessages[m_curmsg][1] = m_strDomain;
	  m_arrMessages[m_curmsg][2] = str.substring(1,str.length() - 1);
	}
	catch(StringIndexOutOfBoundsException exStrIdx) 
	{
	  exStrIdx.printStackTrace(); 
	}
	m_curmsg++;
      }
      else
      {
	//  Throw some exception here
      }
      data = null;
    }
    */
    return data;
  }

  public String[][] generateMessageArray()
  {
    return m_arrMessages;
  }
}
