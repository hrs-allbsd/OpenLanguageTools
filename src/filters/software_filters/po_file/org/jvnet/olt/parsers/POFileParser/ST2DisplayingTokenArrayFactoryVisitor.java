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

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * this code is a derivative (copy) of the DisplayingTokenArrayFactoryVisitor
 * which was used in SunTrans1 - this time though, we're choosing to do different
 * things with the message strings and keys (not keeping the original formatting
 * in the msgstr, since it's not needed any more)
 *
 * Most of this class is probably really inefficient for the things that we're
 * now doing with it (SunTrans2 integration) and it'd be worth revisiting it at
 * a later date to see if we could implement it a little more cleanly.
 *
 * @author timf
 */
public class ST2DisplayingTokenArrayFactoryVisitor implements POFileParserVisitor, POFileParserTreeConstants
{
  private String m_strCurrentKey;
  private String m_strCurrentComment;
  private Vector m_vect;

  //  Key designed for "msgid_plural" in GNU PO file
  private String m_strCurrentKeyPlural;
  //  Flag of encounter GNU msgid_plural
  private boolean m_encounterGNUPluralMsgid;
  //  Collection of the status of message() production
  private ArrayList m_list;

  public ST2DisplayingTokenArrayFactoryVisitor()
  {
    m_vect = new Vector();
    m_strCurrentKey = "";
    m_strCurrentComment = "";

    m_strCurrentKeyPlural = "";
    m_encounterGNUPluralMsgid = false;

    m_list = new ArrayList();
  }


  public Object visit(SimpleNode node, Object data)
  {
    TokenCell cell = null;

    switch(node.getType())
    {
    case JJTMESSAGE:
      //  Enable "NEW MESSAGE" flagging.
      cell = new TokenCell(TokenCell.MARKER, "start");
      //  Record the msgstr status for current message
      m_list.add(new Boolean(node.isHasMsgStr()));

      break;
    case JJTMSGID_TEXT:
      //  Remove the quotes from the key.
      String keytext = node.getKeyData();
      if(!m_encounterGNUPluralMsgid)
      {
        m_strCurrentKey = keytext.substring(1, keytext.length() - 1);
        m_strCurrentKeyPlural = "";
      }
      else
      {
        m_strCurrentKeyPlural = keytext.substring(1, keytext.length() - 1);
      }
      cell = new TokenCell(TokenCell.KEY, node.getNodeData());
      break;
    case JJTMSGSTR_TEXT:
      //  HEADER msgid of GNU PO file if the string of "msgid" is "",
      //  then the msgstr_text production should be as the format part
      if(m_strCurrentKey.equals(""))
      {
        cell = new TokenCell(TokenCell.FORMATING, node.getNodeData());
      }
      break;
    case JJTMSGSTR_UNFORMATTED_TEXT:
      if(!m_encounterGNUPluralMsgid && m_strCurrentKey.equals(""))
      {
        //  HEADER msgid of GNU PO file
        //  The msgstr should not be translatable
        break;
      }

      String unformatedMsgString = node.getNodeData();
      //  Remove quotes from the message.
      unformatedMsgString = unformatedMsgString.substring(1, unformatedMsgString.length() - 1);
      if(unformatedMsgString.equals(""))
        unformatedMsgString = m_strCurrentKey;

      //  Stick quotes in the displaying token list - before message value.
      cell = new TokenCell(TokenCell.FORMATING, "\"");
      m_vect.addElement(cell);

      // This is different than SunTrans1 - here, we're using
      // the message key as the translatable part - not the message
      // string as obtained from the message file.
      cell = new MessageTokenCell(TokenCell.MESSAGE, m_strCurrentKey,
                                  m_strCurrentKey, m_strCurrentComment,
                                  true);
      cell.setTranslation(unformatedMsgString);
      m_vect.addElement(cell);
      

      //  Stick quotes in the displaying token list - after message value
      cell = new TokenCell(TokenCell.FORMATING, "\"");
      m_vect.addElement(cell);

      //  Enable "NEW MESSAGE" flagging.
      cell = new TokenCell(TokenCell.MARKER, "end text");

      m_strCurrentComment = "";
      m_strCurrentKey = "";

      if(m_encounterGNUPluralMsgid)
      {
        m_encounterGNUPluralMsgid = false;
        m_strCurrentKey = m_strCurrentKeyPlural;
        m_strCurrentKeyPlural = "";
      }

      break;
    case JJTMSGSTR_HACK:
    case JJTMSGSTR_PLURAL_HACK:
      cell = new TokenCell(TokenCell.FORMATING, node.getNodeData());
      //  Handle with the current message if it has no msgstr
      if(m_list.size() > 0 && !((Boolean)m_list.get(m_list.size()-1)).booleanValue())
      {
        m_vect.addElement(cell);

        cell = new TokenCell(TokenCell.FORMATING, " ");
        m_vect.addElement(cell);

        cell = new TokenCell(TokenCell.FORMATING, "\"");
        m_vect.addElement(cell);

        cell = new MessageTokenCell(TokenCell.MESSAGE, m_strCurrentKey,
                                    m_strCurrentKey, m_strCurrentComment,
                                    true);
        m_vect.addElement(cell);

        cell = new TokenCell(TokenCell.FORMATING, "\"");
        m_vect.addElement(cell);

        //  Enable "NEW MESSAGE" flagging.
        cell = new TokenCell(TokenCell.MARKER, "end text");

        m_strCurrentComment = "";
        m_strCurrentKey = "";

        if (m_encounterGNUPluralMsgid)
        {
          m_encounterGNUPluralMsgid = false;
          m_strCurrentKey = m_strCurrentKeyPlural;
          m_strCurrentKeyPlural = "";
        }
      }
      break;

    case JJTDOMAIN_HACK:
      cell = new TokenCell(TokenCell.DOMAIN_KEY_WORD, node.getNodeData());
      break;
    case JJTDOMAIN_TEXT:
      String domaintext = node.getNodeData();
      domaintext = domaintext.substring(1, domaintext.length() - 1); //  Remove quotes
      //  from the domain id.

      //  Add opening quotes to the token list
      cell = new TokenCell(TokenCell.FORMATING, "\"");
      m_vect.addElement(cell);

      cell = new TokenCell(TokenCell.DOMAIN, domaintext);
      m_vect.addElement(cell);

      //  Add opening quotes to the token list
      cell = new TokenCell(TokenCell.FORMATING, "\"");
      break;
    case JJTLC_COMMENT_HACK:
      m_strCurrentComment = node.getNodeData();
      cell = new TokenCell(TokenCell.COMMENT, node.getNodeData());
      break;
      // make sure we save the comment describing the next message id
      // which gets stored as a piece source-string context information
    case JJTCOMMENT_HACK:
        cell = new TokenCell(TokenCell.CONTEXT, node.getNodeData());
        m_vect.addElement(cell);
        // now, we drop through to the default state, so the comment gets
        // written as formatting too...
    default:
      if(isNodeDisplaying(node))
      {
        cell = new TokenCell(TokenCell.FORMATING, node.getNodeData());
      }

      if(node.getType() == JJTMSGID_PLURAL_HACK)
      {
        m_encounterGNUPluralMsgid = true;
      }
      break;
    }

    if(cell != null)
    {
      m_vect.addElement(cell);
    }

    /*
    String str = node.toString();
    if(isNodeDisplaying(node) || isMessageStart(node))
    {
      if(str.equals("msgid_text"))
      {
         //  Remove the quotes from the key.
         m_strCurrentKey = node.getKeyData();
         m_strCurrentKey = m_strCurrentKey.substring(1, m_strCurrentKey.length() - 1);
         cell = new TokenCell(TokenCell.KEY, node.getNodeData());
      }
      else
      if (isMessageStart(node))
      {
        //  Enable "NEW MESSAGE" flagging.
        cell = new TokenCell(TokenCell.MARKER, "start");
      }
      else
      if(str.equals("msgstr_unformatted_text"))
      {
        String temp = node.getNodeData();
        temp = temp.substring(1, temp.length() - 1); //  Remove quotes
        //  from the message.

        //  Stick quotes in the displaying token list - before message value.
        cell = new TokenCell(TokenCell.FORMATING, "\"");
        m_vect.addElement(cell);

        // This is different than SunTrans1 - here, we're using
        // the message key as the translatable part - not the message
        // string as obtained from the message file.
        cell = new MessageTokenCell(TokenCell.MESSAGE, temp,
                                    m_strCurrentKey, m_strCurrentComment,
                                    true);
        m_vect.addElement(cell);

        //  Stick quotes in the displaying token list - after message value
        cell = new TokenCell(TokenCell.FORMATING, "\"");
        m_vect.addElement(cell);

        //  Enable "NEW MESSAGE" flagging.
        cell = new TokenCell(TokenCell.MARKER, "end text");

        m_strCurrentComment = "";
        m_strCurrentKey = "";
      }
      else
      if(str.equals("domain_hack"))
      {
        cell = new TokenCell(TokenCell.DOMAIN_KEY_WORD, node.getNodeData());
      }
      else
      if(str.equals("domain_text"))
      {
        String temp = node.getNodeData();
        temp = temp.substring(1, temp.length() - 1); //  Remove quotes
        //  from the domain id.

        //  Add opening quotes to the token list
        cell = new TokenCell(TokenCell.FORMATING, "\"");
        m_vect.addElement(cell);

        cell = new TokenCell(TokenCell.DOMAIN, temp);
        m_vect.addElement(cell);

        //  Add opening quotes to the token list
        cell = new TokenCell(TokenCell.FORMATING, "\"");
      }
      else      //  Only associate LC comments with messages.
      if (str.equals("lc_comment_hack"))
      {
        m_strCurrentComment = node.getNodeData();
        cell = new TokenCell(TokenCell.COMMENT, node.getNodeData());
      }
      else
      {
        cell = new TokenCell(TokenCell.FORMATING, node.getNodeData());
      }
      m_vect.addElement(cell);
    }*/

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
    return (str.equals("comment_hack") ||
            str.equals("lc_comment_hack") ||
            str.equals("ws_hack") ||
            str.equals("newline_hack") ||
            str.equals("msgid_hack") ||
            str.equals("msgid_text") ||
            str.equals("msgstr_hack") ||
            // plural "msgid" and "msgstr"
            str.equals("msgid_plural_hack") ||
            str.equals("msgstr_plural_hack") ||
            // we're not using the formatted version
            // of the msgstr anymore !
            // str.equals("msgstr_text") ||
            str.equals("msgstr_unformatted_text") ||
            str.equals("domain_hack") ||
            str.equals("domain_text")
            );
  }

  private boolean isNodeComment(SimpleNode node)
  {
    String str = node.toString();
    return (str.equals("comment_hack") ||
            str.equals("lc_commet_hack")
            );
  }

  public boolean hasMsgstrsForAllMessages() {
    int countTrue = 0;
    int countFalse = 0;
    for(int i=0; i<m_list.size(); i++) {
      if(((Boolean)m_list.get(i)).booleanValue())
        countTrue ++;
      else
        countFalse ++;
    }

    return countTrue == m_list.size();
  }
}
