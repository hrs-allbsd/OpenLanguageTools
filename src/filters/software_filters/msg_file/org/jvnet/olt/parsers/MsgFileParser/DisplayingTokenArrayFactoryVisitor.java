
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.tmci.MessageType;
import java.util.Vector;
import java.util.Enumeration;

public class DisplayingTokenArrayFactoryVisitor 
implements MsgFileParserVisitor, MsgFileParserTreeConstants
{ 
    private String m_strCurrentKey;
    private String m_strCurrentComment;
    private String m_strCurrentSet;
    private Vector m_vect;
    private String m_quoteChar;
    private boolean foundQuoteDir;
    private boolean foundQuoteChar;

    
    public DisplayingTokenArrayFactoryVisitor()
    {
	m_vect = new Vector();
	m_strCurrentKey = "";
	m_strCurrentComment = "";
	m_strCurrentSet = "";
    }
    
    
    public Object visit(SimpleNode node, Object data)    
    {
	TokenCell cell = null;

	switch(node.getType())
	{
	case JJTDEFAULT_SET:
	  m_strCurrentSet = "1";
	  break;
	case JJTMESSAGE:
	  //  Enable "NEW MESSAGE" flagging.
	  cell = new TokenCell(TokenCell.MARKER,"start"); 
	  break;
	case JJTQUOTE_DIRECTIVE:
	  this.foundQuoteDir=true;
	  this.foundQuoteChar=false;
	  break;
	case JJTDIRECTIVE_CHAR:
	  if(this.foundQuoteDir)
	  {
	    m_quoteChar=node.getNodeData();
      	    this.foundQuoteDir=false;
	    this.foundQuoteChar=true;
	  }
	  break;
	case JJTLC_COMMENT_BLOCK:
	  m_strCurrentComment = node.getNodeData();
	  break;
	case JJTSET_NUMBER:
	  m_strCurrentSet = node.getNodeData();
	  cell = new TokenCell(TokenCell.FORMATING, m_strCurrentSet);
	  break;
	case JJTMSG_NUMBER:
	  cell = new TokenCell(TokenCell.KEY, node.getNodeData());
	  m_strCurrentKey = m_strCurrentSet + ":" + node.getNodeData();
	  break;
	case JJTVALUE:
	  // To what extent should I be calling out things as "formatting" and
	  // what should be messages... not impressed. - or possibly confused.
	    
	  if (this.foundQuoteChar)
	  {
	    //  Stick the quote char in the displaying token list - before message value
	    cell = new TokenCell(TokenCell.FORMATING,m_quoteChar);
	    m_vect.addElement(cell);
	  }
	  
	  cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.MSG_FILE,node.getNodeData(),
				      m_strCurrentKey, m_strCurrentComment,
				      true);


	  m_vect.addElement(cell);
		
	  //  Text is finished. Only formatting left, so insert marker.
	  cell = new TokenCell(TokenCell.MARKER,"end text");
	  m_vect.addElement(cell);

	  if (this.foundQuoteChar)
	  {
	    //  Stick quotes in the displaying token list - after message value
	    cell = new TokenCell(TokenCell.FORMATING,m_quoteChar);
	    
	    m_vect.addElement(cell);
	  }

	  //  The message is finished. Again, insert marker.
	  cell = new TokenCell(TokenCell.MARKER,"end message");
			      
	  m_strCurrentComment = "";
	  m_strCurrentKey= "";
	  break;
	default:
	  if(isNodeDisplaying(node))
	  {
	    cell = new TokenCell(TokenCell.FORMATING,node.getNodeData());	    
	  }
	  break;
	}
	
	if(cell != null)
	{
	  m_vect.addElement(cell);
	}

	return data;
    }
	
    
    public TokenCell[] getDisplayingTokens()
    {
	int iElements = m_vect.size();
	int i = 0;
	//  System.err.println ("m_vect.size is " + iElements );
	TokenCell[] cells = new TokenCell[iElements];
	
	Enumeration myenum = m_vect.elements();
	while(myenum.hasMoreElements() && (i < iElements)){
	    TokenCell tmp = (TokenCell) myenum.nextElement();
	    cells[i++] = tmp;
	}
	return cells;
    }
    
    private boolean isMessageStart(SimpleNode node)
    {
	return node.toString().equals("message");
    }
    
    private boolean isNodeDisplaying(SimpleNode node) {
	String str = node.toString();    
	return ( str.equals("comment_block") ||
	     str.equals("newline") ||
	     str.equals("blank_line") ||
	     str.equals("white_space_stored") ||
	     str.equals("single_white_space_stored") ||
	     
	     str.equals("msg_number") ||
	     str.equals("value") ||
	     
	     str.equals("directive_comment") ||
	     str.equals("directive") ||
	     str.equals("set") ||
	     str.equals("set_number")
		 );  
    }
    
}
