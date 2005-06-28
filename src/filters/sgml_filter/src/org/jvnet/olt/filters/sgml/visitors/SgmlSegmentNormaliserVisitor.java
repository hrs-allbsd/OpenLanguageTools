
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.sgml.visitors;


import org.jvnet.olt.filters.sgml.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.filters.segmenter_facade.*;
import java.io.*;
import java.util.*;
/** This class is designed normlalise tags that during segmentation. In detail,
 * will take a segment that looks like :
 * <code>This is text&lt;b&gt;&lt;/b&gt;&lt;b&gt;&lt;/b&gt;.</code><br>
 * and produce <br>
 *<code>This is text&lt;b&gt;&lt;/b&gt;.</code><br>
 * An empty string will be returned if the input string was simply open and close
 *tags with no content inbetween.
 * @author timf
 */
public class SgmlSegmentNormaliserVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    
    private StringBuffer segment;
    private Stack tagStack;
    private org.jvnet.olt.parsers.tagged.TagTable tagTable;
    private Stack tagStringStack;
    private static final int BUFFSIZE = 20;
    
    /** Creates a new instance of HtmlSegmentNormaliser
     *
     */
    public SgmlSegmentNormaliserVisitor(org.jvnet.olt.parsers.tagged.TagTable tagTable) {
        this.tagTable = tagTable;
        tagStack = new Stack();
        segment = new StringBuffer(BUFFSIZE);
    }
    
    /** This method does the main work of fixing the html input.
     *
     * @param simpleNode The simpleNode that is being visited
     * @param obj An object
     * @return An object
     */
    public Object visit(org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode simpleNode, Object obj) {
        try {
            switch (simpleNode.getType()){
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTCLOSE_TAG:
                    tagStack.push(simpleNode);                
                    break;
                    // these can appear as empty elements in the parse tree -
                    // eg. for <b></b> we have [open_tag=b][sgml_data][tag][close_tag=b]
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTSGML_DATA:
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTTAG:
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                    if (!tagStack.empty()){
                        if (!((SimpleNode)tagStack.peek()).getTagName().equals(simpleNode.getTagName()) ||
                                 simpleNode.hasAttribute() ){
                            // we're doing this if there are attributes in the tag on the stack
                            // (*never* normalise <a name="myanchor"></a>)
                            // they're not equal, so pop the  node off the stack, and write this node.
                            // reverse/clear the stack, then write this node.
                            StringBuffer reverse = new StringBuffer();
                            while (!tagStack.empty()){
                                reverse.insert(0,(((SimpleNode)tagStack.pop()).getNodeData()));
                            }
                            segment.append(reverse.toString());
                            segment.append(simpleNode.getNodeData());
                        } else { // they are equal, so throw away this tag
                            tagStack.pop();
                        }
                    } else {// empty stack - so write this tag
                        segment.append(simpleNode.getNodeData());
                    }
                    break;
                default:
                    //System.out.println ("...." + simpleNode.getNodeData());
                    // reverse out the (open) elements on the stack
                    StringBuffer reverse = new StringBuffer();
                    while (!tagStack.empty()){
                        reverse.insert(0,(((SimpleNode)tagStack.pop()).getNodeData()));
                    }
                    segment.append(reverse.toString());                  
                    segment.append(simpleNode.getNodeData());
                    break;
            }
            
        }catch (java.lang.Exception e){
            e.printStackTrace(); // erk - fixme
        }
        return obj;
    }
    
    public String getSegment(){
        return segment.toString();
    }
    
    
}
