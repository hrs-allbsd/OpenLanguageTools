
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software.moz_dtd;
import org.jvnet.olt.parsers.DTDDocFragmentParser.*;

public class ST2MessageArrayFactoryVisitor implements DTDDocFragmentParserVisitor, org.jvnet.olt.parsers.DTDDocFragmentParser.DTDDocFragmentParserTreeConstants {
    private int m_iNumMessages;
    private String m_strDomain;
    private String m_strCurrentKey;
    private String[][] m_arrMessages;
    private String m_comment;
    private int m_curmsg = 0;
    
    
  /* Due to build problems, we should be using the MessageArrayKey class to
   * manage these constants, but aren't. Need to fix that.
   *public static final int KEY = 0;
    public static final int DOMAIN = 1;
    public static final int STRING = 2;
    public static final int COMMENT = 3;
   * No need for plurals, not supported in Moz dtd message files
    public static final int PLURAL = 4;
   */
    //  A varible indicates if the current msgid matches on msg string
    private boolean m_hasmessage = false;
    private boolean encounteredNonMozillaMessageStringDTD = false;
    
    public ST2MessageArrayFactoryVisitor(int iNumMessages) {
        m_strDomain = "";
        m_strCurrentKey = new String("");
        m_comment = new String("");
        m_iNumMessages = iNumMessages;
        m_arrMessages = new String[m_iNumMessages][4];
    }
    
    public Object visit(SimpleNode node, Object data) {
        switch(node.getType()){
            case JJTENTITY_DECL_NAME:
                // System.out.println("key " + node.getNodeData());
                m_strCurrentKey = node.getNodeData();                
                break;
            case JJTENTITY_DECL_VALUE:
                // System.out.println("val "+ node.getNodeData());
                m_arrMessages[m_curmsg][0] = m_strCurrentKey;
                m_arrMessages[m_curmsg][1] = "";
                m_arrMessages[m_curmsg][2] =  replaceXMLChars(node.getNodeData().substring(1,node.getNodeData().length()-1));
                m_arrMessages[m_curmsg][3] = "";
                m_curmsg++;
                break;
            // check to see if there are any tokens in the tree that would suggest
            // we haven't parsed a mozilla dtd file.
            case JJTNDATA_ENTITY_DECL:
            case JJTPARAMETER_ENTITY_DECL:
            case JJTNOTATION_DECL:
            case JJTINT_ENTITY:
                this.encounteredNonMozillaMessageStringDTD = true;
                break;    
            default:
                break;
        }
        return data;
    }
    
    public String[][] generateMessageArray() {
        return m_arrMessages;
    }
    
    public boolean encounteredNonMozillaMessageStringDTD(){
        return this.encounteredNonMozillaMessageStringDTD;    
    }
    
    private String replaceXMLChars(String str){
        String result = str;
        result = result.replaceAll("&","&amp;");
        result = result.replaceAll("<","&lt;");
        result = result.replaceAll(">","&gt;");
        return result;
    }
    
}
