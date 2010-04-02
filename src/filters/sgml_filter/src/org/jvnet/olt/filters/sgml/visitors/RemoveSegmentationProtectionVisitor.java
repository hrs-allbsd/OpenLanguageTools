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

/**
 * This visitor replaces all <suntransxmlfilter> tags that have an attribute as specified in the constr.
 * with the value of that attribute. It's used so we can put non-segmentable text into these
 * attributes in order to prevent the segmenter from segmenting the text.
 *
 * For example, the text :
 *
 * Bla <![ "IGNORE" [ This is text in an ignored marked section. This is another segment. ]]> so there
 *
 * has only *one* segment and 3 words, so before segmentation, it becomes :
 *
 * Bla <suntransxmlfilter suntransvalue=\"&lt;![ &quot;IGNORE&quot; This is text
 * in an ignored marked section. This is another segment. ]]&gt;"> so there
 *
 * giving us the segment :
 *
 * Bla &lt;![ "IGNORE" [ This is text in an ignored marked section. This is another segment. ]]&gt; so there
 *
 *
 */

package org.jvnet.olt.filters.sgml.visitors;

import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.io.BasicXMLEntityConversionWriter;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.filters.NonConformantSgmlTagParser.*;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import java.io.StringWriter;
import java.io.StringReader;

/**
 * This is a visitor which removes <suntransxmlfilter> tags, replacing them with
 * the value of the suntransxmlfilter attribute "suntransvalue".
 * @author  timf
 */
public class RemoveSegmentationProtectionVisitor implements NonConformantSgmlDocFragmentParserVisitor, NonConformantSgmlTagParserVisitor {
    
    private boolean inValueAttr= false;
    private String valueStr = "";
    private String attribute = "";
    private StringBuffer text = new StringBuffer();
    private int id=1;
    
    /** Removes segmentation protection, by looking for a suntransxmlfilter tag with the
     * attribute supplied containing the text of the input string
     * @param attribute The name of the attribute containing the text we to return
     */
    public RemoveSegmentationProtectionVisitor(String attribute) {
        this.attribute = attribute;
    }
    
    /** get the string that has been extracted from the input (the string we were
     * protecting)
     * @return the protected string
     */
    public String getRemovedString(){
        String t = text.toString();
        return t;
    }
    
    /** an doc fragment visitor to look for suntransxmlfilter tags
     * @param simpleNode node
     * @param obj obj
     * @return obj
     */
    public Object visit(org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode simpleNode, Object obj) {
        switch (simpleNode.getType()){ 
            case NonConformantSgmlDocFragmentParser.JJTOPEN_TAG:
                valueStr = "";
                if (simpleNode.getTagName().equals("suntransxmlfilter")){
                    // get the value attribute for this one
                    try {
                        NonConformantSgmlTagParser parser = new NonConformantSgmlTagParser(new StringReader(simpleNode.getNodeData()));
                        parser.parse();
                        parser.walkParseTree(this,null);
                        if (valueStr.length() > 0){
                            // no need to write <it> tags here - TagSensitiveSgmlFormatWrappingVisitor does that later on
                            //text.append("<it id=\"a"+id+"\" pos=\"open\">"+valueStr.replaceAll("&quot;","\"")+"</it>");
                            //text.append(valueStr.replaceAll("&quot;","\""));
                            // not efficient
                            StringWriter writer = new StringWriter();
                            BasicXMLEntityConversionWriter wrap = new BasicXMLEntityConversionWriter(writer);
                            StringReader reader = new StringReader(valueStr);
                            int i;
                            while ((i=reader.read()) != -1){
                                wrap.write(i);
                            }                            
                            text.append(writer.toString());
                            /**
                             *
                             * valueStr = valueStr.replaceAll("&quot;","\"");
                             * valueStr = valueStr.replaceAll("&lt;","<");
                             * valueStr = valueStr.replaceAll("&gt;",">");
                             * valueStr = valueStr.replaceAll("&amp;","&");
                             * text.append(valueStr);
                             * id++;
                             */
                        } else {
                            text.append(simpleNode.getNodeData());
                        }
                    } catch (Exception e){
                        System.out.println("ERROR : trying to get value attribute from " + simpleNode.getNodeData());
                        e.printStackTrace();
                    }
                } else {
                    text.append(simpleNode.getNodeData());
                }
                break;
            default:
                text.append(simpleNode.getNodeData());
                
        }
        return null;
    }
    
    
    
    /** a tag visitor to get the attribute value.
     * @param simpleNode node
     * @param obj obj
     * @return obj
     */
    public Object visit(org.jvnet.olt.filters.NonConformantSgmlTagParser.SimpleNode simpleNode, Object obj){
        switch(simpleNode.getType()){
            case NonConformantSgmlTagParserTreeConstants.JJTATTNAME:
                if (simpleNode.getNodeData().equals(this.attribute)){
                    inValueAttr = true;
                } else {
                    inValueAttr = false;
                }
                break;
            case NonConformantSgmlTagParserTreeConstants.JJTQUOTED_VALUE:
                if (inValueAttr){
                    int length = simpleNode.getNodeData().length();
                    valueStr = simpleNode.getNodeData().substring(1,length-1);
                }
                break;
        }
        return null;
    }
    
}
