
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.software.moz_dtd;

import org.jvnet.olt.tmci.TokenCell;
import org.jvnet.olt.tmci.MessageTokenCell;
import org.jvnet.olt.tmci.MessageType;
import org.jvnet.olt.parsers.DTDDocFragmentParser.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;


/**
 * This was thrown together in a hurry to try to support Mozilla .dtd files
 * -- it's a bit messy, but it works.
 * @author timf
 */
public class ST2DisplayingTokenArrayFactoryVisitor implements DTDDocFragmentParserVisitor, org.jvnet.olt.parsers.DTDDocFragmentParser.DTDDocFragmentParserTreeConstants {
    private String key;
    private String comment;
    private Vector m_vect;
    private List m_list;
    // we want to keep track of any suspicious productions that would
    // indicate that we've parsed a DTD file that doesn't appear to be
    // a Mozilla DTD one (in particular, if we encounter any of the other
    // entity declaration types definied in the DTDDocFragmentParser
    private boolean encounteredNonMozillaMessageStringDTD = false;
    
    public ST2DisplayingTokenArrayFactoryVisitor() {
        m_vect = new Vector();
        key = "";
        comment = "";
        
        m_list = new ArrayList();
    }
    
    
    public Object visit(SimpleNode node, Object data) {
        TokenCell cell = null;
        
        switch(node.getType()) {
            
            // check to see if there are any tokens in the tree that would suggest
            // we haven't parsed a mozilla dtd file.
            case JJTNDATA_ENTITY_DECL:
            case JJTPARAMETER_ENTITY_DECL:
            case JJTNOTATION_DECL:
            case JJTINT_ENTITY:
                this.encounteredNonMozillaMessageStringDTD = true;
                break;
            case JJTENTITY_DECL_NAME:
                //  add the key
                this.key = replaceXMLChars(node.getNodeData());
                cell = new TokenCell(TokenCell.KEY, this.key);
                m_vect.addElement(cell);
                // add the key also as formatting
                cell = new TokenCell(TokenCell.FORMATING, this.key);
                break;
                
            case JJTENTITY_DECL_VALUE:
                String value = replaceXMLChars(node.getNodeData().substring(1, node.getNodeData().length() - 1));
                
                // need to put formatting quotes around the message value
                cell = new TokenCell(TokenCell.FORMATING, "\"");
                m_vect.addElement(cell);
                cell = new MessageTokenCell(TokenCell.MESSAGE, MessageType.MOZDTD_FILE, value,
                        this.key, this.comment, true);
                cell.setReLayoutAllowed(false);
                m_vect.addElement(cell);
                // need to put formatting quotes around the message value
                cell = new TokenCell(TokenCell.FORMATING, "\"");
                this.comment = "";
                this.key = "";
                break;
                
            case JJTINTERNAL_SUB_SET_COMMENT:
                this.comment+=replaceXMLChars(node.getNodeData());
                // since we have comment tokens here, we need to make sure
                // they don't break our xliff. In other message types,
                // we escape the XML characters before they reach the
                // parser, but since these are DTD files, we can't do that.
                cell = new TokenCell(TokenCell.CONTEXT, this.comment);
                m_vect.addElement(cell);
                // now, we drop through to the default state, so the comment gets
                // written as formatting too...
            default:
                if(isNodeDisplaying(node)) {
                    cell = new TokenCell(TokenCell.FORMATING, replaceXMLChars(node.getNodeData()));
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
        
        TokenCell[] cells = new TokenCell[iElements];
        
        Enumeration myenum = m_vect.elements();
        while(myenum.hasMoreElements() && (i < iElements)) {
            cells[i++] = (TokenCell) myenum.nextElement();
        }
        return cells;
    }
    
    
    private boolean isNodeDisplaying(SimpleNode node) {
        String str = node.toString();
        return (str.equals("internal_sub_set_comment") ||
                str.equals("internal_sub_set_ws") ||
                str.equals("entity_decl_begin") ||
                str.equals("entity_decl_name") ||
                str.equals("entity_decl_space") ||
                str.equals("entity_decl_value") ||
                str.equals("entity_decl_close") ||
                str.equals("notation_decl") ||
                str.equals("element_decl") ||
                str.equals("sgml_data") ||
                str.equals("cdata") ||
                str.equals("int_entity") ||
                str.equals("pcdata")
                );
    }
    
    private boolean isNodeComment(SimpleNode node) {
        String str = node.toString();
        return (str.equals("internal_sub_set_comment"));
    }
    
    private String replaceXMLChars(String str){
        String result = str;
        result = result.replaceAll("&","&amp;");
        result = result.replaceAll("<","&lt;");
        result = result.replaceAll(">","&gt;");
        return result;
    }
    
    public boolean encounteredNonMozillaMessageStringDTD(){
        return this.encounteredNonMozillaMessageStringDTD;    
    }
    
}
