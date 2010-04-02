/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

public class MessageArrayFactoryVisitor
implements MsgFileParserVisitor, MsgFileParserTreeConstants
{
    
    private int m_iNumMessages;
    private String m_strDomain;
    private String m_currentSet;
    private String m_currentComment;
    private String[][] m_arrMessages;
 
    //  Start the counter at -1. The first message will bump it to 0 in time
    //  for the value to arrive.
    private int m_curmsg = -1;
  
    public MessageArrayFactoryVisitor(int iNumMessages,String strDefaultDomain)
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
    switch(node.getType())
    {
    case JJTMSG_NUMBER:
      m_curmsg++;
      m_arrMessages[m_curmsg][0] = m_currentSet + ":" +  node.getNodeData();
      m_arrMessages[m_curmsg][1] = m_strDomain;
      m_arrMessages[m_curmsg][2] = "";   //  Initialize this in case there 
                                         //  is no value.
      m_arrMessages[m_curmsg][3] = m_currentComment;
      m_currentComment = "";  //  Reset the comment.
      break;
    case JJTVALUE:
      m_arrMessages[m_curmsg][2] = node.getNodeData();   //  Set the value.
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
    
  public String[][] generateMessageArray() {
    return m_arrMessages;
  }
}
