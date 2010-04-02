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

package org.jvnet.olt.parsers.POFileParser;

public class MessageArrayFactoryVisitor 
implements POFileParserVisitor,POFileParserTreeConstants
{
  private int m_iNumMessages;
  private String m_strDomain;
  private String[][] m_arrMessages;
  private String m_strComment;

  private int m_curmsg = 0;

  public MessageArrayFactoryVisitor(int iNumMessages,String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_iNumMessages = iNumMessages;
    m_arrMessages = new String[m_iNumMessages][4];
    m_strComment = "";
  }

  public Object visit(SimpleNode node, Object data)
  {
    int nodeType = node.getType();

    switch(nodeType)
    {
    case JJTLC_COMMENT_HACK:
      m_strComment = node.getNodeData();
      break;
    case JJTMSGID_TEXT:
      try
      {
	String str = node.getKeyData();
	data = str.substring(1,str.length() - 1);
      }
      catch(StringIndexOutOfBoundsException exStrIdx) 
      {
	exStrIdx.printStackTrace(); 
      }
      break;
    case JJTMSGSTR_TEXT:
      if((data != null) && (m_curmsg < m_iNumMessages))
      {
	try
	{
	  String str = node.getNodeData();
	  m_arrMessages[m_curmsg][0] = data.toString();
	  m_arrMessages[m_curmsg][1] = m_strDomain;
	  m_arrMessages[m_curmsg][2] = str.substring(1,str.length() - 1);
	  m_arrMessages[m_curmsg][3] = m_strComment;
	}
	catch(StringIndexOutOfBoundsException exStrIdx) 
	{
	  exStrIdx.printStackTrace(); 
	}
      }
      else
      {
	//  Throw some exception here
      }
      m_strComment = "";
      m_curmsg++;
      data = null;
      break;
    case JJTDOMAIN_TEXT:
      try
      {
	String str = node.getNodeData();
	m_strDomain = str.substring(1,str.length() - 1);
      }
      catch(StringIndexOutOfBoundsException exStrIdx) 
      {
	exStrIdx.printStackTrace(); 
      }
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
