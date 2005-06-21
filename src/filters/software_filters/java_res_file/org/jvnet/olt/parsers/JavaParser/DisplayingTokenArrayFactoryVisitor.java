
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.tmci.MessageType;
import java.util.Vector;
import java.util.Enumeration;

public class DisplayingTokenArrayFactoryVisitor 
implements ResBundleParserVisitor, ResBundleParserTreeConstants
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
    String temp;

    switch(node.getType())
    {
    case JJTSIMPLE_KEY:
      //  Pass key information along
      m_strCurrentKey = node.getNodeData();
      break;
    case JJTQUOTED_TEXT_KEY:
      //  Pass key information along
      temp =  node.getNodeData();
      m_strCurrentKey = temp.substring(1,temp.length() - 1); //  Remove quotes
      break;
    case JJTQUOTED_TEXT_VALUE:
      temp =  node.getNodeData();
      temp = temp.substring(1,temp.length() - 1);  //  Remove quotes
                                                   //  from the message.
      
      //  Stick quotes in the displaying token list - before message value
      cell = new TokenCell(TokenCell.FORMATING,"\"");
      m_vect.addElement(cell);
      
      //  Put message in the displaying token list
      cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.JAVA_RES,temp,
				  m_strCurrentKey, m_strCurrentComment,
				  true);
      m_vect.addElement(cell);
      
      //  Stick quotes in the displaying token list - after message value
      cell = new TokenCell(TokenCell.FORMATING,"\"");
      m_vect.addElement(cell);
      
      //  Enable "NEW MESSAGE" flagging.
      cell = new TokenCell(TokenCell.MARKER, "end text");	 
      
      m_strCurrentKey = "";
      m_strCurrentComment = "";
      break;
    case JJTSIMPLE_VALUE:
      cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.JAVA_RES, node.getNodeData(),
				  m_strCurrentKey, m_strCurrentComment,
				  false);
      m_vect.addElement(cell);
      
      //  Enable "Check this strange stuff" flagging.
      cell = new TokenCell(TokenCell.MARKER, "end code");	 
      
      m_strCurrentKey = "";
      m_strCurrentComment = "";
      break;
    case JJTRES_LINE:
      cell = new TokenCell(TokenCell.MARKER, "start");	       
      break;
    case JJTLC_COMMENT_HACK:
      m_strCurrentComment = node.getNodeData(); 
      cell = new TokenCell(TokenCell.COMMENT,
			     node.getNodeData());	 
      break;
    default:
      if(isNodeDisplaying(node))
      {
      	cell = new TokenCell(TokenCell.FORMATING,
			     node.getNodeData());
      }	 	
      break;
    }

    /*
    if(isNodeKey(node))
    {
      //  Pass key information along
      String temp =  node.getNodeData();
      m_strCurrentKey = temp.substring(1,temp.length() - 1);  //  Remove quotes
    }
    else
    if(isNodeDisplaying(node) || isMessageStartCue(node))
    {
      String strNodeType = node.toString();
      TokenCell cell = null;

      //  Mark quoted_string_values types as translatable
      if(strNodeType.equals("quoted_text_value"))   // Translatable value
      {
	String temp =  node.getNodeData();
	temp = temp.substring(1,temp.length() - 1);  //  Remove quotes
	                                             //  from the message.

	//  Stick quotes in the displaying token list - before message value
	cell = new TokenCell(TokenCell.FORMATING,"\"");
	m_vect.addElement(cell);

	//  Put message in the displaying token list
	cell = new MessageTokenCell(TokenCell.MESSAGE, temp,
				    m_strCurrentKey, m_strCurrentComment,
				    true);
	m_vect.addElement(cell);

	//  Stick quotes in the displaying token list - after message value
	cell = new TokenCell(TokenCell.FORMATING,"\"");
	m_vect.addElement(cell);

	//  Enable "NEW MESSAGE" flagging.
	cell = new TokenCell(TokenCell.MARKER, "end text");	 

	m_strCurrentKey = "";
	m_strCurrentComment = "";
      }
      else 
	if(strNodeType.equals("simple_value"))  //  Odd value 
      {
	cell = new MessageTokenCell(TokenCell.MESSAGE, node.getNodeData(),
				    m_strCurrentKey, m_strCurrentComment,
				    false);
	m_vect.addElement(cell);

	//  Enable "Check this strange stuff" flagging.
	cell = new TokenCell(TokenCell.MARKER, "end code");	 

	m_strCurrentKey = "";
	m_strCurrentComment = "";
      }      
      else 
	if(isNodeComment(node)) 
      {
	m_strCurrentComment = node.getNodeData(); 
	cell = new TokenCell(TokenCell.COMMENT,
			     node.getNodeData());	 
      }
      else 
	if(isMessageStartCue(node)) 
      {
	//  Put in a marker to enable flagging.
	cell = new TokenCell(TokenCell.MARKER, "start");	 
      }
      else 
      {
	cell = new TokenCell(TokenCell.FORMATING,
			     node.getNodeData());	 	
      }
      m_vect.addElement(cell);
    }
    */
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

    TokenCell[] cells = new TokenCell[iElements];

    Enumeration myenum = m_vect.elements();
    while(myenum.hasMoreElements() && (i < iElements))
    {
      cells[i++] = (TokenCell) myenum.nextElement();
    }
    return cells;
  }


  private boolean isNodeTranslatable(SimpleNode node)
  {
    String str = node.toString();
    return str.equals("quoted_text_value");
  }

  private boolean isNodeKey(SimpleNode node)
  {
    String str = node.toString();
    return ( str.equals("simple_key") || str.equals("quoted_text_key") ) ;
  }

  private boolean isMessageStartCue(SimpleNode node)
  {
    return ( node.toString().equals("res_line") ) ;
  }

  /*
  private boolean isNodeComment(SimpleNode node)
  {
    String str = node.toString();
    return ( str.equals("formal_comment_hack") ||
	     str.equals("multi_line_comment_hack") ||
	     str.equals("single_line_comment_hack") );
  }
  */

  private boolean isNodeDisplaying(SimpleNode node)
  {
    String str = node.toString();    
    return (str.equals("assign_hack") ||
	    str.equals("class_hack") ||
	    str.equals("comma_hack") ||
	    str.equals("formal_comment_hack") ||
	    str.equals("import_hack") ||
	    str.equals("lcurly_hack") ||
	    str.equals("lparen_hack") ||
	    str.equals("multi_line_comment_hack") ||
	    str.equals("object_hack") ||
	    str.equals("other_chars_hack") ||
	    str.equals("package_hack") ||
	    str.equals("quoted_text_value") ||
	    str.equals("rcurly_hack") ||
	    str.equals("rparen_hack") ||
	    str.equals("semicolon_hack") ||
	    str.equals("single_line_comment_hack") ||
	    str.equals("string_literal_hack") ||
	    str.equals("ws_hack") ||
	    str.equals("eof_hack") ||
	    str.equals("plus_hack") // for unusal messages, plus_hack 
	                            // gets used : elsewhere we use the token <PLUS>
	     ); 

    //
    //  Note:  Keys are not included in here as they are 
    //         made up from a number of other nodes. There
    //         is the ability to pass keys to the translatable
    //         TokenCell.
    //
 
  }


}
