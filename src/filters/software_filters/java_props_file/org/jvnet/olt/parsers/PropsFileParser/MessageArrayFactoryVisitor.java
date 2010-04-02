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

package org.jvnet.olt.parsers.PropsFileParser;

public class MessageArrayFactoryVisitor
implements PropsFileParserVisitor, PropsFileParserTreeConstants
{
  private int m_iNumMessages;
  private String m_strDomain;
  private String m_strCurrentKey;
  private String[][] m_arrMessages;
  private String m_comment;
  private int m_curmsg = 0;

  public MessageArrayFactoryVisitor(int iNumMessages,String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_strCurrentKey = new String ("");
    m_comment = new String ("");
    m_iNumMessages = iNumMessages;
    m_arrMessages = new String[m_iNumMessages][4];
  }

    public Object visit(SimpleNode node, Object data)
    {
      switch(node.getType())
      {
      case JJTKEY:
        try
        {
          data = node.getNodeData();
          m_strCurrentKey = data.toString();
        }
        catch(StringIndexOutOfBoundsException exStrIdx)
        {
          exStrIdx.printStackTrace();
        }
        break;
      case JJTVALUE:
      //case JJTEOF_VALUE:
        if((data != null) && (m_curmsg < m_iNumMessages))
        {
          try
          {

            // write some cleverness in here similar to comment/eof_comment

            String strValue = data.toString();

            m_arrMessages[m_curmsg][0] = strValue;
            m_arrMessages[m_curmsg][1] = m_strDomain;
            String str=node.getNodeData();
            m_arrMessages[m_curmsg][2] = str.substring(0,str.length()-1);
            m_arrMessages[m_curmsg][3] = m_comment;
            m_comment = "";  //  Comment used: reset to empty.
            if (m_arrMessages[m_curmsg][2].length() == 0)
            System.err.println ("Warning ! - key '"+m_strCurrentKey+"' contains an empty value.");


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
        m_curmsg++;
        data = null;
        break;
      case JJTLC_COMMENT_BLOCK:
      //case JJTLC_EOF_COMMENT:
        String str = node.getNodeData();
        m_comment = str.substring(0,str.length() -1);
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
