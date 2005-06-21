
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.MsgFileParser;

public class QuoteReplacingVisitor 
implements MsgFileParserVisitor
{
    protected boolean foundQuoteDir=false;
    protected boolean foundQuoteChar=false;
    protected char[] directiveChar={'x'};
    
    
    public Object visit(SimpleNode node, Object data) {
	
	if (node.toString().equals("quote_directive")){
	    this.foundQuoteDir=true;
	}
	
	if (node.toString().equals("directive_char") && this.foundQuoteDir){
	    directiveChar=node.getNodeData().toCharArray();
	    this.foundQuoteDir=true;
	    this.foundQuoteChar=true;
	}

	if (node.toString().equals("quote_directive") && this.foundQuoteChar){
	    this.foundQuoteChar=false;
	    this.foundQuoteDir=true;
	}
	
	if (node.toString().equals("value") && this.foundQuoteChar){

	    String tmp_str=node.getNodeData();
	    int length=tmp_str.length();
	    
	    node.setNodeData(directiveChar[0] + tmp_str + directiveChar[0]);
	}
	
	return data;
    }
}
