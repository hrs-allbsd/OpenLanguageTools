
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.PropsFileParser;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.tmci.MessageType;
import java.util.Vector;
import java.util.Enumeration;

public class ST2DisplayingTokenArrayFactoryVisitor implements PropsFileParserVisitor, PropsFileParserTreeConstants {
    private String m_strCurrentKey;
    private String m_strCurrentComment;
    private Vector m_vect;
    
    public ST2DisplayingTokenArrayFactoryVisitor() {
        m_vect = new Vector();
        m_strCurrentKey = "";
        m_strCurrentComment = "";
    }
    
    
    public Object visit(SimpleNode node, Object data) {
        TokenCell cell = null;
        switch(node.getType()) {
            case JJTKEY:
                m_strCurrentKey = node.getNodeData();
                cell = new TokenCell(TokenCell.KEY,node.getNodeData());
                break;
            case JJTVALUE_NO_LAYOUT:
                String strValue="";
                try {
                    strValue = node.getNodeData();
                    // adding a warning here for empty values
                    if (strValue.length() == 0)
                        System.err.println("Warning ! - key '"+m_strCurrentKey+"' contains an empty value.");
                    
                }
                catch(StringIndexOutOfBoundsException ex) {
                    m_strCurrentComment = "";
                    m_strCurrentKey= "";
                    System.err.println(ex);
                    return data;
                }
                cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.JAVA_PROPS,strValue,
                m_strCurrentKey, m_strCurrentComment,
                true);
                m_vect.addElement(cell);
                
                
                //  Enable "NEW MESSAGE" flagging.
                cell = new TokenCell(TokenCell.MARKER,"end text");
                m_vect.addElement(cell);
                
                
                //  We have stripped the '\n' character of the value
                //  so we'll add it back here.
                if(node.getType() == JJTVALUE_NO_LAYOUT) {
                    cell = new TokenCell(TokenCell.FORMATING,"\n");
                }
                
                
                m_strCurrentComment = "";
                m_strCurrentKey= "";
                break;
            case JJTMESSAGE:
                //case JJTEOF_MESSAGE:
                //  These productions denote the start of
                //  a message.
                //  Enable "NEW MESSAGE" flagging.
                cell = new TokenCell(TokenCell.MARKER,"start");
                break;
            case JJTLC_COMMENT_BLOCK:
                try {
                    m_strCurrentComment = node.getNodeData();
                    cell = new TokenCell(TokenCell.COMMENT,m_strCurrentComment);
                }
                catch(StringIndexOutOfBoundsException ex) {
                    m_strCurrentComment = "";
                    System.err.println(ex);
                    return data;
                }
                break;
            case JJTNORMAL_COMMENT_BLOCK:
                // want to add comments as context information
                // to allow translators to see these in the editor
                cell = new TokenCell(TokenCell.CONTEXT,node.getNodeData());
                m_vect.addElement(cell);
                // also make sure the comment makes it into the skeleton file
                cell = new TokenCell(TokenCell.FORMATING,node.getNodeData());
                break;
                
                /**
                 * case JJTLC_EOF_COMMENT:
                 * try
                 * {
                 * String tmp = node.getNodeData();
                 * m_strCurrentComment = tmp.substring(0,tmp.length() - 1);
                 * cell = new TokenCell(TokenCell.COMMENT,m_strCurrentComment);
                 * }
                 * catch(StringIndexOutOfBoundsException ex)
                 * {
                 * m_strCurrentComment = "";
                 * System.err.println(ex);
                 * return data;
                 * }
                 * break;
                 */
            default:
                if(isNodeDisplaying(node)) {
                    //System.out.println("--"+node.getNodeData());
                    cell = new TokenCell(TokenCell.FORMATING,node.getNodeData());
                }
                break;
        }
        
        //  Add cell if it is not null
        if(cell != null) {
            m_vect.addElement(cell);
        }
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
    
    private boolean isNodeDisplaying(SimpleNode node) {
        String str = node.toString();
        return ( str.equals("key") ||
        str.equals("equals") ||
        str.equals("slashnewline_stored") ||
        str.equals("value_no_layout") ||
        //str.equals("eof_value") ||
        str.equals("normal_comment_block") ||
        //str.equals("normal_eof_comment") ||
        str.equals("blank_line")
        );
    }
    
}
