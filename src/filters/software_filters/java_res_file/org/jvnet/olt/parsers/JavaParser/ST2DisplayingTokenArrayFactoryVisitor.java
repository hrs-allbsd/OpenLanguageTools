
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

public class ST2DisplayingTokenArrayFactoryVisitor implements ResBundleParserVisitor, ResBundleParserTreeConstants {
    private String m_strCurrentKey;
    private String m_strCurrentComment;
    private Vector m_vect;

    //  Implement the comment for EN Source Message
    private int m_indexOfComment = 0;
    private final String TMC_EN_MESSAGE = "@TMC@ EN MESSAGE";

    public ST2DisplayingTokenArrayFactoryVisitor() {
        m_vect = new Vector();
        m_strCurrentKey = "";
        m_strCurrentComment = "";
        boolean relayoutMessageP = true;
    }


    public Object visit(SimpleNode node, Object data) {
        TokenCell cell = null;
        String temp;

        switch(node.getType()) {
            case JJTKEY_NO_LAYOUT:
                //  Pass key information along
                m_strCurrentKey = node.getNodeData();
                break;
            // Fix bug 4982560, backconversion of .java adds src language text alive, not commented
            case JJTQUOTED_TEXT_VALUE:
                //  Value of quoted_text_value() production should not be treated as formating string
                break;
            case JJTVALUE_NO_LAYOUT:
                temp = node.getNodeData();

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

                //  Implement the comment for EN Source Message
                String msgCommentBegin = "/* " + TMC_EN_MESSAGE + " {";
                String msgCommentKey =  "\"" + m_strCurrentKey + "\"";
                String msgCommentComma = ", ";
                String msgCommentText = "\"" + temp + "\"";
                String msgCommentEnd = "}, */\n        ";
                String msgComment = msgCommentBegin + msgCommentKey + msgCommentComma + msgCommentText + msgCommentEnd;
                if(!existMsgComment(m_vect, msgCommentBegin + msgCommentKey + msgCommentComma)) {
                  cell = new TokenCell(TokenCell.FORMATING, msgComment);
                  m_vect.add(m_indexOfComment, cell);
                }

                //  Enable "NEW MESSAGE" flagging.
                cell = new TokenCell(TokenCell.MARKER, "end text");

                m_strCurrentKey = "";
                m_strCurrentComment = "";
                break;
            case JJTCOMPLEX_VALUE_WITH_LAYOUT:
                cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.JAVA_RES, node.getNodeData(),
                                            m_strCurrentKey, m_strCurrentComment,
                                            false);
                // for complex values, we store the exact message values with layout eg.
                // stuff like :
                /* { "weird.value", new Integer(42) + " is the meaning of "+
                 *                  + "life on today's date, " + new Date() },
                 */
                // so in these cases, we don't allow code later on to re-layout these messages
                cell.setReLayoutAllowed(false);
                m_vect.addElement(cell);

                //  Enable "Check this strange stuff" flagging.
                cell = new TokenCell(TokenCell.MARKER, "end code");

                m_strCurrentKey = "";
                m_strCurrentComment = "";
                break;
            case JJTRES_LINE:
                //  Implement the comment for EN Source Message
                m_indexOfComment = m_vect.size();

                cell = new TokenCell(TokenCell.MARKER, "start");
                break;
                // for all comments, we want to write a comment node, but
                // also write non-lc comments as formatting nodes
            case JJTSINGLE_LINE_COMMENT_HACK:
            case JJTFORMAL_COMMENT_HACK:
            case JJTMULTI_LINE_COMMENT_HACK:
                // first add the comment as a context item
                // (so we can display it to translators if necessary)
                cell = new TokenCell(TokenCell.CONTEXT,node.getNodeData());
                m_vect.addElement(cell);
                
                cell = new TokenCell(TokenCell.FORMATING,node.getNodeData());
                m_vect.addElement(cell);
                // yes, we want to drop through....
                
                if(node.getNodeData().indexOf(TMC_EN_MESSAGE) > 0) {
                  cell = null;
                  break;
                }

            case JJTLC_COMMENT_HACK:
                m_strCurrentComment = node.getNodeData();
                cell = new TokenCell(TokenCell.COMMENT, node.getNodeData());
                break;
            default:
                if(isNodeDisplaying(node)) {
                    cell = new TokenCell(TokenCell.FORMATING, node.getNodeData());
                    //System.out.println("-->"+node.toString()+"\n==>"+node.getNodeData());
                }
                break;
        }
        if(cell != null) {
            m_vect.addElement(cell);
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
        return data;
    }

    public TokenCell[] getDisplayingTokens() {
        int iElements = m_vect.size();
        int i = 0;

        TokenCell[] cells = new TokenCell[iElements];

        Enumeration myenum = m_vect.elements();
        while(myenum.hasMoreElements() && (i < iElements)) {
            cells[i++] = (TokenCell) myenum.nextElement();
        }
        return cells;
    }


    private boolean isNodeTranslatable(SimpleNode node) {
        String str = node.toString();
        return (str.equals("value_no_layout") || str.equals("complex_value_with_layout"));
    }

    private boolean isNodeKey(SimpleNode node) {
        String str = node.toString();
        return ( str.equals("key_no_layout") ) ;
    }

    private boolean isMessageStartCue(SimpleNode node) {
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

    private boolean isNodeDisplaying(SimpleNode node) {
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
        str.equals("complex_value_with_layout") ||
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

    //  Implement the comment for EN Source Message
    private boolean existMsgComment(Vector vect, String str) {
      for(int i=0; i<vect.size(); i++) {
        TokenCell cell = (TokenCell) vect.get(i);
        if(cell.getType() != TokenCell.FORMATING || cell.getText().indexOf(TMC_EN_MESSAGE) < 0)
          continue;
        if(cell.getText().startsWith(str))
          return true;
      }
      return false;
    }
}
