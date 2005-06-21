
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.Enumeration;

public class DisplayingTokenArrayFactoryVisitor 
implements POFileParserVisitor
{
  private String m_strCurrentKey;
  private String m_strCurrentComment;
  private Vector m_vect;
  
  public DisplayingTokenArrayFactoryVisitor()
  {
    m_vect = new Vector();
    m_strCurrentKey = "";
    m_strCurrentComment = "";
  }


  public Object visit(SimpleNode node, Object data)
  {
    TokenCell cell = null;
    String str = node.toString();
      //System.out.println("visiting node ..."+str);

    if(isNodeDisplaying(node) || isMessageStart(node))
    {
      if(str.equals("msgid_text"))
      {
	//  Remove the quotes from the key.
	m_strCurrentKey = node.getKeyData();
	m_strCurrentKey = m_strCurrentKey.substring(1,m_strCurrentKey.length() - 1);
	cell = new TokenCell(TokenCell.KEY,node.getNodeData()); 
      }
      else 
      if(isMessageStart(node))
      {
	//  Enable "NEW MESSAGE" flagging.
	cell = new TokenCell(TokenCell.MARKER,"start"); 
      }
      else 
      if(str.equals("msgstr_text"))
      {
	String temp =  node.getNodeData();
	temp = temp.substring(1,temp.length() - 1);  //  Remove quotes
	                                             //  from the message.

	//  Stick quotes in the displaying token list - before message value
	cell = new TokenCell(TokenCell.FORMATING,"\"");
	m_vect.addElement(cell);

	cell = new MessageTokenCell(TokenCell.MESSAGE, temp,
				    m_strCurrentKey, m_strCurrentComment,
				    true);
	m_vect.addElement(cell);

	//  Stick quotes in the displaying token list - after message value
	cell = new TokenCell(TokenCell.FORMATING,"\"");
	m_vect.addElement(cell);

	//  Enable "NEW MESSAGE" flagging.
	cell = new TokenCell(TokenCell.MARKER,"end text"); 

	m_strCurrentComment = "";
	m_strCurrentKey= "";
      }
      else
      if(str.equals("domain_hack"))
      {
	cell = new TokenCell(TokenCell.DOMAIN_KEY_WORD,node.getNodeData()); 
      }
      else
      if(str.equals("domain_text"))
      {
	String temp =  node.getNodeData();
	temp = temp.substring(1,temp.length() - 1);  //  Remove quotes
	                                             //  from the domain id.

	//  Add opening quotes to the token list
	cell = new TokenCell(TokenCell.FORMATING,"\"");
	m_vect.addElement(cell);

	cell = new TokenCell(TokenCell.DOMAIN,temp); 
	m_vect.addElement(cell);

	//  Add opening quotes to the token list
	cell = new TokenCell(TokenCell.FORMATING,"\"");
      }
      else      //  Only associate LC comments with messages.
	if( str.equals("lc_comment_hack"))  
      {
	m_strCurrentComment = node.getNodeData();
        System.out.println("comment " + m_strCurrentComment);
	cell = new TokenCell(TokenCell.COMMENT,node.getNodeData()); 
      }
      else
      {
	cell = new TokenCell(TokenCell.FORMATING,node.getNodeData()); 	
      }
      m_vect.addElement(cell);
    }
    return data;
  }
    
  public TokenCell[] getDisplayingTokens()
  {
    int iElements = m_vect.size();
    int i = 0;

    TokenCell[] cells = new TokenCell[iElements];

    Enumeration myenum = m_vect.elements();
    while(myenum.hasMoreElements() && (i < iElements))
    {
      cells[i++] = (TokenCell) myenum.nextElement();
    }
    return cells;
  }

  private boolean isMessageStart(SimpleNode node)
  {
    return node.toString().equals("message");
  }

  private boolean isNodeDisplaying(SimpleNode node)
  {
    String str = node.toString();    
    return ( str.equals("comment_hack") ||
             str.equals("lc_comment_hack") ||
	     str.equals("ws_hack") ||
	     str.equals("newline_hack") ||
	     str.equals("msgid_hack") ||
	     str.equals("msgid_text") ||
	     str.equals("msgstr_hack") ||
	     str.equals("msgstr_text") ||
	     str.equals("domain_hack") ||
	     str.equals("domain_text")
	     );  
  }

  private boolean isNodeComment(SimpleNode node)
  {
    String str = node.toString();    
    return ( str.equals("comment_hack") ||
	     str.equals("lc_commet_hack")
	     );  
  }


}
