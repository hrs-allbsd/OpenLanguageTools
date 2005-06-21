
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XResFileParser;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.Enumeration;

public class DisplayingTokenArrayFactoryVisitor 
implements XResFileParserVisitor
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

    if(isNodeDisplaying(node) || isMessageStart(node))
    {
      if(str.equals("key"))
      {
	m_strCurrentKey = node.getNodeData();
	System.out.println ("key: " + m_strCurrentKey);
	cell = new TokenCell(TokenCell.KEY,node.getNodeData()); 
      }
      else 
      if(isMessageStart(node))
      {
	//  Enable "NEW MESSAGE" flagging.
	cell = new TokenCell(TokenCell.MARKER,"start"); 		
      }
      else 
      if(str.equals("value") || str.equals("eof_value"))
      {

	  String strValue = node.getNodeData();
	  
	  cell = new MessageTokenCell(TokenCell.MESSAGE, strValue,
				      m_strCurrentKey, m_strCurrentComment,
				      true);
	  m_vect.addElement(cell);

	  //  Enable "NEW MESSAGE" flagging.
	  cell = new TokenCell(TokenCell.MARKER,"end text");
	  m_strCurrentComment = "";
	  m_strCurrentKey= "";
      }
      else
      if( str.equals("comment") || str.equals("eof_comment"))
      {
	try
	{
	  if(str.equals("eof_comment"))
	  {
	    m_strCurrentComment = node.getNodeData();
	  }
	  else
	  { 
	    m_strCurrentComment = node.getNodeData();
	  }
   	  System.out.println ("comment: "+ m_strCurrentComment);
	  cell = new TokenCell(TokenCell.COMMENT,m_strCurrentComment); 
	}
	catch(StringIndexOutOfBoundsException ex)
	{
	  m_strCurrentComment = "";
	  System.err.println(ex);
	  return data;
	}
      }
      else
      {
	  System.out.println ("formatting: " +node.getNodeData() +node.toString());
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
    return (node.toString().equals("message") || node.toString().equals("eof_message"));
  }

  private boolean isNodeDisplaying(SimpleNode node)
  {
    String str = node.toString();    
    return ( str.equals("comment") ||
	     str.equals("storing_white_space") ||
	     str.equals("newline") ||
	     str.equals("key") ||
	     str.equals("equals") ||
	     str.equals("value") ||
	     str.equals("comment") ||
	     str.equals("include")
	     );  
  }

}
