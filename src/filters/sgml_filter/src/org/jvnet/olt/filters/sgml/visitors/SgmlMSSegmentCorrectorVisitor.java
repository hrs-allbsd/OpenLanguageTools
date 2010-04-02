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

package org.jvnet.olt.filters.sgml.visitors;


import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import java.io.*;
import java.util.*;

/** This class is designed "fix" tags that have been broken during segmentation.
 * @author timf
 */
public class SgmlMSSegmentCorrectorVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    
    private String tag="";
    private StringBuffer tagsAtBeginning;
    private StringBuffer tagsAtEnd;
    private Stack tagStack;
    private Stack tagStringStack;
    private TagTable tagTable;
    private List closedOnLastRun;
    
    private static final int BUFFSIZE = 10;
    
    /** Creates a new instance of HtmlSegmentFactoryVisitor. It requires a list argument that
     * indicates which open-tags were closed on the last run, so these can be marked as open
     * tags for the next time this method is run.
     * eg. <pre>
     * <a href="foo"> This is text. This is more text.</a>
     * results in the segments :
     * <a href="foo"> This is text.
     * This is more text</a>
     *
     * The second time this is run, the closedOnLastRun list will contain the tag <a href="foo">
     * so the 2nd tag can be remade into :
     * <a href="foo">This is more text</a>
     *
     * </pre>
     * @param table The TagTable that this class uses.
     * @param closedOnLastRun The tags that were closed the last time this method was run.
     *
     */
    public SgmlMSSegmentCorrectorVisitor(TagTable tagTable, List closedOnLastRun) {
        tagsAtBeginning = new StringBuffer(BUFFSIZE);
        tagsAtEnd = new StringBuffer(BUFFSIZE);
        tagStack = new Stack();
        this.closedOnLastRun = closedOnLastRun;
        this.tagTable = tagTable;
        Iterator it = closedOnLastRun.iterator();
        
        while (it.hasNext()){
            SimpleNode node = (SimpleNode)it.next();
            tagsAtBeginning.append(node.getNodeData());
            tagStack.push(node);
        }
        this.closedOnLastRun.clear();
        
    }
    
    /** This method does the main work of fixing the html input.
     *
     * @param simpleNode The simpleNode that is being visited
     * @param obj An object
     * @return An object
     */
    public Object visit( SimpleNode simpleNode, Object obj) {
        try {
            switch (simpleNode.getType()){
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTMARKED_SECTION_TAG:
                    //System.out.println ("pushing ms " + simpleNode.getNodeData());
                    tagStack.push(simpleNode);
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTEND_MARKED_SECT:
                    if (!tagStack.empty()){
                        tagStack.pop();
                        //System.out.println ("popping ms");
                    }
                    else { // we have unmatched marked sections in this segment
                        System.out.println("Unmatched marked section " + simpleNode.getNodeData());
                        if (!tagStack.empty())
                            tagStack.pop();
                    }
                    break;
            }            
        }catch (java.lang.Exception e){
            e.printStackTrace(); // erk - fixme
        }
        return null;
    }
    
    public String getTagsAtBeginning(){
        return tagsAtBeginning.toString();
    }
    
    public String getTagsAtEnd(){
        while (!tagStack.empty()){
            // pop off remaining stuff on the tag Stack
            tagsAtEnd.append("]]>");
            this.closedOnLastRun.add(0,tagStack.pop());
        }
        return tagsAtEnd.toString();
    }
    
    
}

