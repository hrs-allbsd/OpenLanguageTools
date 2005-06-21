
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.POFileParser;

public class ST2MessageArrayFactoryVisitor implements POFileParserVisitor, POFileParserTreeConstants
{
  private int m_iNumMessages;
  private String m_strDomain;
  private String[][] m_arrMessages;
  private String m_strComment;

  private int m_curmsg = 0;

  private boolean m_encounterGNUPluralMsgid;
  private String m_encounteredGNUMsgidText;
  private boolean m_hasMsgstr;

  public ST2MessageArrayFactoryVisitor(int iNumMessages, String strDefaultDomain)
  {
    m_strDomain = strDefaultDomain;
    m_iNumMessages = iNumMessages;
    // Due to bug 5075472, SoftwareToTmx throws error when msgid_plural == msgid
    // the message array need expand to 5
    // The value of m_arrMessages[i][4], "false" for single msgid, "true" for plural msgid
    // m_arrMessages = new String[m_iNumMessages][4];
    m_arrMessages = new String[m_iNumMessages][5];
    m_strComment = "";

    m_encounterGNUPluralMsgid = false;
    m_encounteredGNUMsgidText = "";
    m_hasMsgstr = false;
  }

  public Object visit(SimpleNode node, Object data)
  {
    int nodeType = node.getType();

    switch (nodeType)
    {
      case JJTMESSAGE:
        m_hasMsgstr = node.isHasMsgStr();

        break;
      case JJTLC_COMMENT_HACK:
        m_strComment = node.getNodeData();
        break;
      case JJTMSGID_TEXT:
        try
        {
          String str = node.getKeyData();
          //data = str.substring(1, str.length() - 1);
          str = str.substring(1, str.length() - 1);

          // Initiate the value for msgid type

          //  Miss msgstr value
          if(!m_hasMsgstr && !str.equals("") && m_curmsg < m_iNumMessages)
          {
            try
            {
              m_arrMessages[m_curmsg][0] = str;
              m_arrMessages[m_curmsg][1] = m_strDomain;
              m_arrMessages[m_curmsg][2] = new String(str);
              m_arrMessages[m_curmsg][3] = m_strComment;
              m_arrMessages[m_curmsg][4] = m_encounterGNUPluralMsgid ? "true" : "false";
            }
            catch (StringIndexOutOfBoundsException exStrIdx)
            {
              exStrIdx.printStackTrace();
            }
            m_curmsg++;
            break;
          }


          if(m_encounterGNUPluralMsgid) {
              m_encounteredGNUMsgidText = str;
              m_arrMessages[m_curmsg + 1][4] = "true";
          }
          else {
              data = str;
              m_arrMessages[m_curmsg][4] = "false";
          }
        }
        catch (StringIndexOutOfBoundsException exStrIdx)
        {
          exStrIdx.printStackTrace();
        }
        break;
      case JJTMSGSTR_UNFORMATTED_TEXT:
        //  Should skip the blank HEADER msgid of GNU .po file
        if ((data != null) && (!data.equals("")) && (m_curmsg < m_iNumMessages))
        {
          try
          {
            String str = node.getNodeData();
            str = str.substring(1, str.length() - 1);
            if(str.equals("")) str = new String(data.toString());
            m_arrMessages[m_curmsg][0] = data.toString();
            m_arrMessages[m_curmsg][1] = m_strDomain;
            m_arrMessages[m_curmsg][2] = str;
            m_arrMessages[m_curmsg][3] = m_strComment;
          }
          catch (StringIndexOutOfBoundsException exStrIdx)
          {
            exStrIdx.printStackTrace();
          }

          m_curmsg++;
        }
        else
        {
          //  Throw some exception here
        }
        m_strComment = "";

        if(m_encounterGNUPluralMsgid)
        {
          m_encounterGNUPluralMsgid = false;
          data = m_encounteredGNUMsgidText;
          m_encounteredGNUMsgidText = "";
        }
        else
        {
          data = null;
        }
        break;
      case JJTDOMAIN_TEXT:
        try
        {
          String str = node.getNodeData();
          m_strDomain = str.substring(1, str.length() - 1);
        }
        catch (StringIndexOutOfBoundsException exStrIdx)
        {
          exStrIdx.printStackTrace();
        }
        break;
      case JJTMSGID_PLURAL_HACK:
        m_encounterGNUPluralMsgid = true;
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
