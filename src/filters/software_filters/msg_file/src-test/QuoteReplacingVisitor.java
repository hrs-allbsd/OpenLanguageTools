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
