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

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.tmci.MessageType;
import java.util.Vector;
import java.util.Enumeration;

/**
 * this code is a derivative (copy) of the DisplayingTokenArrayFactoryVisitor
 * which was used in SunTrans1 - this time though, we're choosing to do different
 * things with the messages (we're not keeping the original formatting
 * in the value, since it's not needed any more)
 *
 * Most of this class is probably really inefficient for the things that we're
 * now doing with it (SunTrans2 integration) and it'd be worth revisiting it at
 * a later date to see if we could implement it a little more cleanly.
 *
 * @author timf
 */
public class ST2DisplayingTokenArrayFactoryVisitor implements MsgFileParserVisitor, MsgFileParserTreeConstants {
    private String m_strCurrentKey;
    private String m_strCurrentComment;
    private String m_strCurrentSet;
    private Vector m_vect;
    private String m_quoteChar;
    private boolean foundQuoteDir;
    private boolean foundQuoteChar;
    
    
    public ST2DisplayingTokenArrayFactoryVisitor() {
        m_vect = new Vector();
        m_strCurrentKey = "";
        m_strCurrentComment = "";
        m_strCurrentSet = "";
    }
    
    
    public Object visit(SimpleNode node, Object data) {
        TokenCell cell = null;
        
        switch(node.getType()) {
            case JJTDEFAULT_SET:
                m_strCurrentSet = "1";
                break;
            case JJTMESSAGE:
                //  Enable "NEW MESSAGE" flagging.
                cell = new TokenCell(TokenCell.MARKER,"start");
                break;
            case JJTQUOTE_DIRECTIVE:
                this.foundQuoteDir=true;
                this.foundQuoteChar=false;
                break;
            case JJTDIRECTIVE_CHAR:
                if(this.foundQuoteDir) {
                    m_quoteChar=node.getNodeData();
                    this.foundQuoteDir=false;
                    this.foundQuoteChar=true;
                }
                break;
            case JJTLC_COMMENT_BLOCK:
                m_strCurrentComment = node.getNodeData();
                break;
            case JJTSET_NUMBER:
                m_strCurrentSet = node.getNodeData();
                cell = new TokenCell(TokenCell.FORMATING, m_strCurrentSet);
                break;
            case JJTMSG_NUMBER:
                cell = new TokenCell(TokenCell.KEY, node.getNodeData());
                m_strCurrentKey = m_strCurrentSet + ":" + node.getNodeData();
                break;
                // Fix Bug 5048890
                //case JJTVALUE_ON_ONE:
            case JJTVALUE:
                // To what extent should I be calling out things as "formatting" and
                // what should be messages... not impressed. - or possibly confused.
                //System.out.println("-->" + node.getNodeData());
                if (this.foundQuoteChar) {
                    //  Stick the quote char in the displaying token list - before message value
                    cell = new TokenCell(TokenCell.FORMATING,m_quoteChar);
                    m_vect.addElement(cell);
                }
                
                cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.MSG_FILE,node.getNodeData(),
                m_strCurrentKey, m_strCurrentComment,
                true);
                
                
                m_vect.addElement(cell);
                
                //  Text is finished. Only formatting left, so insert marker.
                cell = new TokenCell(TokenCell.MARKER,"end text");
                m_vect.addElement(cell);
                
                if (this.foundQuoteChar) {
                    //  Stick quotes in the displaying token list - after message value
                    cell = new TokenCell(TokenCell.FORMATING,m_quoteChar);
                    
                    m_vect.addElement(cell);
                }
                
                //  The message is finished. Again, insert marker.
                cell = new TokenCell(TokenCell.MARKER,"end message");
                
                m_strCurrentComment = "";
                m_strCurrentKey= "";
                break;
            case JJTCOMMENT_BLOCK:
                // first make sure the comment makes it in as a piece of context
                // information that describes the source string
                cell = new TokenCell(TokenCell.CONTEXT,node.getNodeData());
                m_vect.addElement(cell);
                // next make it a normal formatting element
                // by dropping through into default now...
            default:
                if(isNodeDisplaying(node)) {
                    cell = new TokenCell(TokenCell.FORMATING,node.getNodeData());
                }
                break;
        }
        
        if(cell != null) {
            m_vect.addElement(cell);
        }
        
        return data;
    }
    
    
    public TokenCell[] getDisplayingTokens() {
        int iElements = m_vect.size();
        int i = 0;
        //  System.err.println ("m_vect.size is " + iElements );
        TokenCell[] cells = new TokenCell[iElements];
        
        Enumeration myenum = m_vect.elements();
        while(myenum.hasMoreElements() && (i < iElements)){
            TokenCell tmp = (TokenCell) myenum.nextElement();
            cells[i++] = tmp;
        }
        return cells;
    }
    
    private boolean isMessageStart(SimpleNode node) {
        return node.toString().equals("message");
    }
    
    private boolean isNodeDisplaying(SimpleNode node) {
        String str = node.toString();
        return ( str.equals("comment_block") ||
        str.equals("newline") ||
        str.equals("blank_line") ||
        str.equals("white_space_stored") ||
        str.equals("single_white_space_stored") ||
        
        str.equals("msg_number") ||
        str.equals("value") ||
        
        str.equals("directive_comment") ||
        str.equals("directive") ||
        str.equals("set") ||
        str.equals("set_number")
        );
    }
    
}
