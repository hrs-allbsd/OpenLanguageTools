
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

public class ST2MessageArrayFactoryVisitor implements MsgFileParserVisitor, MsgFileParserTreeConstants
{

  private int m_iNumMessages;
  private String m_strDomain;
  private String m_currentSet;
  private String m_currentComment;
  private String[][] m_arrMessages;

  //  Start the counter at -1. The first message will bump it to 0 in time
  //  for the value to arrive.
  private int m_curmsg = -1;

  //  A varible indicates if the current msgid matches on msg string
  private boolean m_hasmessage = false;

  public ST2MessageArrayFactoryVisitor(int iNumMessages, String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_iNumMessages = iNumMessages;
    m_arrMessages = new String[m_iNumMessages][4];
    m_currentComment = "";
  }

  // This is used by the alignment tool - need to populate m_arrMessages with
  // values from the parse tree, according to the node type.

  // "Id"     == msg id arrMessages[][0]  is combination of setid:msgid value
  // "Domain" == set id arrMessages[][1]  is cat name : gets passed in as "defaultDomain"
  // "value"  == value  arrMessages[][2]  duh.
  // "comment"  == value  arrMessages[][3]  The LC comment associated with the message.

  // Also there's no need for the substring(1,thing.length() -1) since I've already
  // removed quotes using the QuoteRemovingVistor code inbuilt into the parser.

  public Object visit(SimpleNode node, Object data)
  {
    switch (node.getType())
    {
      case JJTMESSAGE:
        m_hasmessage = node.isHasMessage();
        if (m_hasmessage)
        {
          m_curmsg++;
        }
        break;
      case JJTMSG_NUMBER:
        if (m_hasmessage)
        {
          m_arrMessages[m_curmsg][0] = m_currentSet + ":" + node.getNodeData();
          m_arrMessages[m_curmsg][1] = m_strDomain;
          m_arrMessages[m_curmsg][2] = ""; //  Initialize this in case there
          //  is no value.
          m_arrMessages[m_curmsg][3] = m_currentComment;
          m_currentComment = ""; //  Reset the comment.
        }
        break;
        // Fix Bug 5048890
        //case JJTVALUE_ON_ONE:
      case JJTVALUE:
        if (m_hasmessage)
        {
          m_arrMessages[m_curmsg][2] = node.getNodeData(); //  Set the value.
        }
        break;
      case JJTDEFAULT_SET:
        m_currentSet = "1";
        break;
      case JJTSET_NUMBER:
        m_currentSet = node.getNodeData();
        break;
      case JJTLC_COMMENT_BLOCK:
        m_currentComment = node.getNodeData();
        break;
      default:
        break;
    }
    return data;
  }

  public String[][] generateMessageArray()
  {
    return m_arrMessages;
  }
}