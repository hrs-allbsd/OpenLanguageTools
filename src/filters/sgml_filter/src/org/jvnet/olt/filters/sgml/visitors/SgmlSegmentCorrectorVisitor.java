
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
 * It copes with either tags or marked sections, hopefully.
 * @author timf
 */
public class SgmlSegmentCorrectorVisitor implements NonConformantSgmlDocFragmentParserVisitor {
    
    private StringBuffer tagsAtBeginning;
    private StringBuffer tagsAtEnd;
    private Stack tagStack;
    private Stack tagStringStack;
    private org.jvnet.olt.parsers.tagged.TagTable tagTable;
    private List closedOnLastRun;
    private List tagList;
    private int index = 0;
    
    
    // used in debugging
    private String value ="";
    
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
    public SgmlSegmentCorrectorVisitor(TagTable tagTable, List closedOnLastRun, List tagList) {
        tagsAtBeginning = new StringBuffer(BUFFSIZE);
        tagsAtEnd = new StringBuffer(BUFFSIZE);
        tagStack = new Stack();
        this.closedOnLastRun = closedOnLastRun;
        this.tagTable = tagTable;
        this.tagList = tagList;
        Iterator it = closedOnLastRun.iterator();
        // System.out.println("-0-0---=------");
        while (it.hasNext()){
            
            SimpleNode node = (SimpleNode)it.next();
            //System.out.println("adding tags " + node.getNodeData());
            tagsAtBeginning.append(node.getNodeData());
            tagStack.push(node);
        }
        //        System.out.println("-0-0---=------");
        this.closedOnLastRun.clear();
        
    }
    
    public void setValue(String value){
        //this.value = value;
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
                
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                    // don't want to do anything for these special tags...
                    if (simpleNode.getTagName().equals("suntransxmlfilter")){
                        index --;
                    } else {
                        Tag tag = null;
                        if (tagList.size() == 0 || index > tagList.size()-1){
                            System.out.println("Hit open tag called " + simpleNode.getTagName());
                            System.out.println("WUh Wuh Wuh!");
                            tag = new Tag(simpleNode.getTagName(),"Unknown Namespace !",null);
                        } else {
                            tag = (Tag)tagList.get(index);
                        }
                        //System.out.println ("hit open tag");
                        if (!tagTable.tagEmpty(simpleNode.getTagName(),tag.getNameSpaceID()) && !simpleNode.isEmptyTag() ){
                            //System.out.println ("pushing " + simpleNode.getNodeData());
                            tagStack.push(simpleNode);
                        } else {
                            //System.out.println("Not pushing " + simpleNode.getNodeData());
                        }
                    }
                    index++;
                    break;
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTCLOSE_TAG:
                    if (!tagStack.empty()){
                        if (((SimpleNode)tagStack.peek()).getTagName().equals(simpleNode.getTagName()) &&
                        ((SimpleNode)tagStack.peek()).getPrefix().equals(simpleNode.getPrefix())
                        ){
                            tagStack.pop();
                            //System.out.println ("popping" + simpleNode.getTagName());
                            
                        }
                        else { // we have unmatched tags in this segment
                            System.out.println("Unmatched Tag " + simpleNode.getNodeData());
                            System.out.println(value);
                            tagStack.pop();
                        }
                    }
                    break;
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
            throw new java.lang.RuntimeException("Caught exception in visitor ! " + e.getMessage());
        }
        return null;
    }
    
    public String getTextAtBeginning(){
        return tagsAtBeginning.toString();
    }
    
    public String getTextAtEnd(){
        while (!tagStack.empty()){
            // pop off remaining stuff on the tag Stack
            SimpleNode peekedNode = (SimpleNode)tagStack.peek();
            switch (peekedNode.getType()){
                // close the opened marked section
                case  NonConformantSgmlDocFragmentParserTreeConstants.JJTMARKED_SECTION_TAG:
                    tagsAtEnd.append("]]>");
                    this.closedOnLastRun.add(0,tagStack.pop());
                    break;
                // close the tag
                case NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG:
                    String prefix = ((SimpleNode)tagStack.peek()).getPrefix();
                    tagsAtEnd.append("</");
                    if (prefix.length() != 0){
                        tagsAtEnd.append(prefix);
                    }
                    tagsAtEnd.append(((SimpleNode)tagStack.peek()).getTagName());
                    tagsAtEnd.append(">");
                    this.closedOnLastRun.add(0,tagStack.pop());
                    break;
            }
        }
        return tagsAtEnd.toString();
    }
    
}