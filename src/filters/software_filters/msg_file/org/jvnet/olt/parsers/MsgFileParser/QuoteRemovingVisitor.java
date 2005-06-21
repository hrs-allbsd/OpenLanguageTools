
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

public class QuoteRemovingVisitor implements MsgFileParserVisitor {
    protected boolean foundQuoteDir=false;
    protected boolean foundQuoteChar=false;
    protected char[] directiveChar={'x'};
    
    
    public Object visit(SimpleNode node, Object data) {
        
        if (node.toString().equals("quote_directive")){
            // System.out.println ("Found a quote directive");
            this.foundQuoteDir=true;
        }
        
        if (node.toString().equals("directive_char") && this.foundQuoteDir){
            // System.out.println ("Found a quote character too");
            directiveChar=node.getNodeData().toCharArray();
            this.foundQuoteDir=true;
            this.foundQuoteChar=true;
        }
        
        if (node.toString().equals("quote_directive") && this.foundQuoteChar){
            //  System.out.println ("Found a quote directive : resetting old QuoteChar flag");
            this.foundQuoteChar=false;
            this.foundQuoteDir=true;
        }
        
        if ((node.toString().equals("value") || node.toString().equals("value_on_one"))&&
        this.foundQuoteChar){
            
            System.out.println("removing quotes from a message : "+node.getNodeData());
            
            
            // System.out.println (this.directiveChar[0]);
            String tmp_str=node.getNodeData();
            
            int length=tmp_str.length();
            if (length > 0){
                if ((tmp_str.charAt(0)==this.directiveChar[0]) && (tmp_str.charAt(length-1)==this.directiveChar[0])) {
                    node.setNodeData(tmp_str.substring(1,length-1));
                }
                
                else {
                    System.err.println("Warning! - Incorrectly quoted string : " + node.getNodeData() );
                }
            } else { System.out.println("empty string !"); }
        }
        
        // for SunTrans2, we're interested in the value_on_many production, since it allows us
        // to get the entire text of the message easily. In order to get useful stuff from
        // the value_on_many, we may need to prune a quoting character off the end.
        if (node.toString().equals("value_on_many") && this.foundQuoteChar){
            System.out.println("removing quotes from a value on many : "+node.getNodeData());
            
            String tmp_str=node.getNodeData();
            int length=tmp_str.length();
            // value on many strings can only ever have a quote character at the end
            // (and being more specific, only at the last value_on_many node in the
            //  message)
            if ((tmp_str.charAt(length-1)==this.directiveChar[0])) {
                node.setNodeData(tmp_str.substring(0,length-1));
            }
        }
        
        if (node.toString().equals("value_before_many") && this.foundQuoteChar){
            System.out.println("Removing beginning quotes from a value before many : " +  node.getNodeData());
            String tmp_str=node.getNodeData();
            int length=tmp_str.length();
            // value before many strings can only ever have a quote character at the beginning
            if ((tmp_str.charAt(0)==this.directiveChar[0])) {
                node.setNodeData(tmp_str.substring(1,length));
            }
        }
        return data;
    }
}
