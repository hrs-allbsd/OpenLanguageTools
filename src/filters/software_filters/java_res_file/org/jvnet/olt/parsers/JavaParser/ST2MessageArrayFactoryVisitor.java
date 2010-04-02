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

package org.jvnet.olt.parsers.JavaParser;


public class ST2MessageArrayFactoryVisitor implements ResBundleParserVisitor, ResBundleParserTreeConstants {
    private int m_iNumMessages;
    private String[][] m_arrMessages;
    private String m_strDomain;
    private String m_currComment;
    private int m_curmsg = 0;
    
    public ST2MessageArrayFactoryVisitor(int iNumMessages, String strDefaultDomain) {
        m_strDomain = strDefaultDomain;
        m_iNumMessages = iNumMessages;
        m_arrMessages = new String[m_iNumMessages][4];
        m_currComment = "";
    }
    
    public Object visit(SimpleNode node, Object data) {
        switch(node.getType()) {
            case JJTLC_COMMENT_HACK:
                m_currComment = node.getNodeData();
                break;
            case JJTKEY_NO_LAYOUT:
                try {
                    data = node.getNodeData();                    
                }
                catch(StringIndexOutOfBoundsException exStrIdx) {
                    exStrIdx.printStackTrace();
                }
                break;
            case JJTVALUE_NO_LAYOUT:
            case JJTCOMPLEX_VALUE_WITH_LAYOUT:
                if((data != null) && (m_curmsg < m_iNumMessages)) {
                    try {
                        String str = node.getNodeData();
                        m_arrMessages[m_curmsg][0] = data.toString();
                        m_arrMessages[m_curmsg][1] = m_strDomain;                        
                        m_arrMessages[m_curmsg][2] = str;
                        m_arrMessages[m_curmsg][3] = m_currComment;
                    }
                    catch(StringIndexOutOfBoundsException exStrIdx) {
                        exStrIdx.printStackTrace();
                    }
                    m_currComment = "";
                    m_curmsg++;
                }
                else {
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
    
    public String[][] generateMessageArray() {
        return m_arrMessages;
    }
}
