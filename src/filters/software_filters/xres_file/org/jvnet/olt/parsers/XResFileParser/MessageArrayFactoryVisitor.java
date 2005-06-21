
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XResFileParser;

public class MessageArrayFactoryVisitor implements XResFileParserVisitor
{
  private int m_iNumMessages;
  private String m_strDomain;
  private String[][] m_arrMessages;
  private String m_comment;
  private int m_curmsg = 0;

  public MessageArrayFactoryVisitor(int iNumMessages,String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_comment = new String ("");
    m_iNumMessages = iNumMessages;
    m_arrMessages = new String[m_iNumMessages][4];
  }
    
    public Object visit(SimpleNode node, Object data)
    {
      String strNodeType = node.toString();
    
    if(strNodeType.equals("key"))
	{
	try {
	  data = node.getNodeData();
	}
	catch(StringIndexOutOfBoundsException exStrIdx) {exStrIdx.printStackTrace();}
    }
    else if(strNodeType.equals("value") || strNodeType.equals("eof_value")){
	if((data != null) && (m_curmsg < m_iNumMessages)){
	    try{// write some cleverness in here similar to comment/eof_comment 			      
		m_arrMessages[m_curmsg][0] = data.toString();
		m_arrMessages[m_curmsg][1] = m_strDomain;
		String str=node.getNodeData();
		if (strNodeType.equals("value"))
		    m_arrMessages[m_curmsg][2] = str;
		else  // we're dealing with an eof_value
		    m_arrMessages[m_curmsg][2] = str;
		m_arrMessages[m_curmsg][3] = m_comment;
	    }
	    catch(StringIndexOutOfBoundsException exStrIdx){exStrIdx.printStackTrace();}
	}
	else {
	    //  Throw some exception here
	}
	m_curmsg++;
	data = null;
    }
    else if(strNodeType.equals("domain_text")){
	try {
	    String str = node.getNodeData();
	    m_strDomain = str.substring(1,str.length() - 2);
	}
	catch(StringIndexOutOfBoundsException exStrIdx) {exStrIdx.printStackTrace();}
    }
    else if(strNodeType.equals("comment") || strNodeType.equals("eof_comment")){
	
	//     This bit is a little mad - I need to know where the comment goes in relation
	//     to a message string. That is, either before or after a message. Ouch.
	//      Currently only comments are going in for immediately before the message
	
	m_comment = node.getNodeData();
	if (strNodeType.equals("eof_comment")){
	    String str = node.getNodeData();
	    m_comment = str.substring(0,str.length() -1);
	}
    }
    
    return data;
    }
    
    public String[][] generateMessageArray()
    {
	return m_arrMessages;
    }
}
