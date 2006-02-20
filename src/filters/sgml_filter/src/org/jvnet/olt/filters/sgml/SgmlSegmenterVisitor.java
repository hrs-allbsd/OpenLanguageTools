/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlSegmenterVisitor.java
 *
 * Created on January 08 2003
 *
 */
package org.jvnet.olt.filters.sgml;

import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.tagged.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import org.jvnet.olt.filters.sgml.visitors.*;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.format.*;
import java.io.*;
import java.util.*;

/**
 * This class is designed to segment text at a sgml level. It "blocks" chunks of
 * text and then calls the correct plaintext segmenter for that language. Once the segmenter
 * has finished, it then "fixes" tags that may have been broken due to sentence
 * breaks inside tags. For example, the text:
 * <CODE>&lt;b&gt; This is a sentence. So is this.&lt;/b&gt;</CODE> will appear
 * first as the segments :<br>
 * <CODE>&lt;b&gt; This is a sentence.</CODE><br>
 * <CODE>So is this.&lt;/b&gt;</CODE><br><br>
 * and once it's fixed, it will look like  :<br><br>
 * <CODE>&lt;b&gt; This is a sentence.&lt;/b&gt;</CODE><br>
 * <CODE>&lt;b&gt;So is this.&lt;/b&gt;</CODE><br><br>
 *
 * We also have full support for sgml marked sections and xml namespaces. We do this
 * by being a visitor that visits generic "TaggedMarkup" nodes - the implementations
 * being either sgml nodes, or xml nodes. In the case of the xml parse tree, a separate
 * visitor will have already run over the parse tree to fill in complete namespace
 * information for each element encountered.
 *
 * This code is long and complex and I'm not proud of it. It could use a lot of refactoring.
 *
 * @author timf
 */
public class SgmlSegmenterVisitor implements TaggedMarkupVisitor {
    
    // FIXME : there's waaay too many globals here : it might be good
    // to put them in a separate State object, so they're easier to track
    private TagTable tagTable;
    private SegmenterTable segmenterTable;
    
    // Here are the buffers in which we store text that's being
    // built up before segmentation. We have :
    
    // The main StringBuffer - ultimately, everything pours into this.
    // it's where blocks of text are stored just before segmentation
    private StringBuffer buf ;
    
    // a List for storing the nodes that make up an included marked section
    private List includedMarkedSectionNodes;
    // a List for storing the nodes that make up an ignored marked section
    private List ignoredMarkedSectionNodes;
    
    // a buffer for unsegmentable text (eventually wrapped with a <suntransxmlfilter> tag)
    private StringBuffer inlineNoSegBuffer;
    // a buffer for unsegmentable text that we don't wordcount either
    private StringBuffer inlineNoSegNoCountBuffer;
    // a buffer for inline non translatable text (like <comment> in sgml)
    private StringBuffer inlineNoTransBuffer;
    // a buffer where we preserve the layout of the text (like <pre> in html)
    private StringBuffer verbatimBuffer;
    private Stack verbatimStack;
    
    // The segmenter interface
    private SegmenterFacade segmenterFacade;
    // used to translate between the various different parse trees we use
    private NodeTypeConverter converter;
    
    private String language = "";
    
    private List closedOnLastSegmentationRun;
    /* -- The closedOnLastSegmentationRun list are important ---
     * they allow us to remember any tags that were opened and not closed within
     * the last segment/block we've encountered. The list contains
     * NonConformantSgmlDocFragmentParser SimpleNode objects.
     *
     * For example with the Sgml : <b>This is text. This is more text.</b>
     * we need to remember that we're in the context of a <b> tag for the 2nd sentence.
     *
     * Unfortunately it doesn't just count for inline text, for broken input :
     * <b> This is text <td> a block tag </td> This is more text </b>
     * we would get 3 segments (since the block tag is causing the segmentation) but
     * all three segments should be in <b> tags. This list is where we store that information
     */
    
    private GlobalVariableManager gvm;
    private SegmenterFormatter formatter;
    
    private Stack stateStack;
    private int tagState = DEFAULT;
    // our default state
    private static final int DEFAULT = 0;
    // if we're in an element that should not be segmented
    private static final int NONSEGMENTABLE = 1;
    // if we're in an element which should not be counted or segmented
    private static final int NONCOUNTABLEORSEGMENTABLE = 2;
    // if we're in an element which is not translatable (like <comment>)
    private static final int NONTRANSLATABLE = 3;
    // if we're in an element that has verbatim layout
    private static final int VERBATIMLAYOUT = 4;
    
    
    // a configuration flag, to ensure that "dontsegment" is always
    // turned on (useful if the sgml file we're parsing is referenced
    // from a system entity inside a program listing, eg:
    // <programlisting>&my-sample-code.sgm</programlisting> where
    // mysample code is in fact a big wodge of java,c,etc.)
    private boolean treatFileAsSingleSegment = false;
    
    // a string representing the marked section we're in (if any)
    private String markedSectionFlag = "";
    private Stack markedSectionStack;
    
    // is 800 characters okay - should probably do some performance experiments here
    private static final int BUFFSIZE = 800;
    
    // Note these are only *INLINE* tags
    private Stack openTagStack;
    
    // these are *all* the open tags on our stack
    private Stack tagStack;
    
    private Stack nonSegmentableStack;
    private Stack nonSegmentableNonCountableStack;
    private Stack nonTranslatableStack;
    // this List is used to pass namespace information to the lower levels of the segmenter
    // which don't get to see the original SimpleNode
    private List tagList;
    
    
    
    /** Creates a new instance of SgmlSegmenterVisitor
     * @param gvm used to keep state information : typically, which marked sections are INCLUDE or IGNOREd
     * @param converter A converter used to convert the format specific node types of the parse tree
     * we're using into generic TaggedMarkup node types
     * @param formatter the SegmentFormatter used to output the segments
     * @param segmenterTable the segmenterTable that contains information about the file type being parsed.
     * @param table The TagTable that this Visitor uses.
     * @param language The langauge to use for segmentation.
     */
    public SgmlSegmenterVisitor(SegmenterFormatter formatter, SegmenterTable segmenterTable, TagTable table,
            String language, GlobalVariableManager gvm, NodeTypeConverter converter) {
        
        this.tagTable = table;
        this.segmenterTable = segmenterTable;
        this.language = language;
        this.formatter = formatter;
        this.gvm = gvm;
        this.buf = new StringBuffer();
        this.ignoredMarkedSectionNodes = new ArrayList();
        this.includedMarkedSectionNodes = new ArrayList();
        this.verbatimBuffer = new StringBuffer();
        this.markedSectionStack = new Stack();
        
        this.inlineNoSegNoCountBuffer = new StringBuffer();
        this.inlineNoSegBuffer = new StringBuffer();
        this.inlineNoTransBuffer = new StringBuffer();
        
        this.closedOnLastSegmentationRun=new ArrayList();
        // want to add the special suntrans2xmlfilter tag to the segmenter table - it'll require
        // fixing the interface though
        this.tagStack = new Stack();
        this.openTagStack = new Stack();
        this.nonSegmentableNonCountableStack = new Stack();
        this.nonSegmentableStack = new Stack();
        this.nonTranslatableStack = new Stack();
        this.verbatimStack = new Stack();
        
        this.converter = converter;
        this.tagList = new ArrayList();
        this.stateStack = new Stack();
        //System.out.println("Initialising state to ...");
        updateSegmentationState(this.tagState);
    }
    
    public void setTreatFileAsSingleSegment(boolean treatFileAsSingleSegment){
        this.treatFileAsSingleSegment = treatFileAsSingleSegment;
    }
    
    /**
     * This method sets walks down the sgml parse tree writes the appropriate elements
     * to the Segmenter formatter (blocks, segments, etc.).
     *
     * Internally it works by building up a StringBuffer (buf) of segmentable text,
     * and as soon as it hits a block node (or EOF) it segments the contents of
     * the buffer (using the handleBlockNode() method), writes any segments that
     * result to the SegmenterFormatter and then writes any block level elements
     * and finally clears the pcdata/inline tags buffer making it ready to accept more text.<br><br>
     *
     * Maintenance note - this code is really badly written, sorry about that, but deadlines,
     * deadlines...  A vague excuse is that this stuff is actually really hard, but it would probably
     * look a bit better with some refactoring.
     *
     * @param simpleNode The simpleNode that is being visited
     * @param obj An object
     * @return An object
     */
    public Object visit(TaggedMarkupNode simpleNode, Object obj) {
        try {
            String tagName = simpleNode.getTagName();
            String namespaceID = simpleNode.getNamespaceID();
            String nodeData = simpleNode.getNodeData();
            
            if (treatFileAsSingleSegment){ // this gives me the willies.
                this.tagState = NONCOUNTABLEORSEGMENTABLE;
                switch (this.converter.convert(simpleNode.getType())){
                    case TaggedMarkupNodeConstants.EOF:
                        processEOF(simpleNode);
                        break;
                    default:
                        processPcdata(simpleNode);
                }
            } else {
                switch (this.converter.convert(simpleNode.getType())){
                    
                    case TaggedMarkupNodeConstants.OPEN_TAG:
                        if (!simpleNode.isEmptyTag()){
                            tagStack.push(simpleNode);
                        }
                        processOpenTag(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.CLOSE_TAG:
                        tagStack.pop();
                        processCloseTag(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.PCDATA:
                        processPcdata(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.ENTITY:
                        processEntity(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.EOF:
                        processEOF(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.INT_ENTITY:
                    case TaggedMarkupNodeConstants.DOCTYPE_BEGINNING:
                    /* In the future it could be useful to use this to dynamically
                     * configure the TagTable and SegmenterTable objects.
                     * For the time being, we just write it out as formatting.
                     */
                        buf = handleBlockNode(buf, simpleNode, false);
                        formatter.writeFormatting(simpleNode.getNodeData());
                        break;
                    case TaggedMarkupNodeConstants.COMMENT:
                        processComment(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.CDATA:
                        processCdata(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.MARKED_SECTION_TAG:
                        processMarkedSectTag(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.END_MARKED_SECT:
                        processEndMarkedSect(simpleNode);
                        break;
                        // for entity declarations, we always write text if we're looking
                        // at an INTERNAL text entity. Each entity gets written in it's own
                        // segment.
                    case TaggedMarkupNodeConstants.ENTITY_DECL:
                        processEntityDecl(simpleNode);
                        break;
                        
                    case TaggedMarkupNodeConstants.PROCESSING_INST:
                        processProcessingInst(simpleNode);
                        break;
                    case TaggedMarkupNodeConstants.INTERNAL_SUB_SET_BEGINNING:
                    case TaggedMarkupNodeConstants.INTERNAL_SUB_SET_ENDING:
                    case TaggedMarkupNodeConstants.INTERNAL_SUB_SET_WS_COMMENT:
                    case TaggedMarkupNodeConstants.DOCTYPE_ENDING:
                        this.formatter.writeFormatting(simpleNode.getNodeData());
                        break;
                        // end case statment, no default on purpose.
                }
            }
            
        } catch (java.lang.Exception e){
            // not good exception handling here. Really, the visit method of
            // the doc fragment parser should be coded to throw an exception...
            e.printStackTrace();
            throw new SgmlFilterException(e.getMessage());
        }
        return obj;
    }
    
    
    
    /**
     * Things we do when we hit an open tag
     * @param simpleNode this is a simpleNode that represents the contents of the open tag
     * @throws org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException if there was some problem writing the segment
     * @throws java.io.IOException when some IO problem results
     */
    protected void processOpenTag(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        //System.out.println(nodeData);
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            } else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            Tag tag = new Tag(tagName,namespaceID, simpleNode.getNamespaceMap());
            // add stuff to our tagList, which is only maintained for tags outside the ms.
            this.tagList.add(tag);
            
            if (!tagTable.tagEmpty(tagName,namespaceID) && !simpleNode.isEmptyTag()
            && tagTable.tagMayContainPcdata(tagName,namespaceID)){ // last one experimental
                // since the open tag stack is used to determine if we're inside a block
                // tag, we choose whether to write marked sections or not based on this.
                this.openTagStack.push(tag);
            }
            
            // next, work out what state we should be in, based on our current state
            // and the contents of this tag.
            if (tagTable.tagMayContainPcdata(tagName,namespaceID)) { // inline tags
                switch (tagState){
                    case DEFAULT:
                        // the default state requires a bit of explanation. The idea here is to
                        // deal with cases for entering the other states. However a recent JSP
                        // bug showed that we needed more intelligent processing. Here's what
                        // we do now :
                        // For empty tags, the contents of the tag itself might be significant
                        // or segmentable - eg. <%= blah =%> the entire tag should be protected
                        // potentially - so for each case of inlineNonSegmentable,
                        // inlineNonSegmentableNonWordCount, verbatimLayout and NonTranslatable
                        // we need to decide how to handle the open tag, and transition into a
                        // different tag state. For non-empty tags, it's a bit simpler.
                        // NonSegmentableNonWordCountable
                        
                        /*
                         * This sentence allow to use attributes to decide if to segment/count a tag.
                         * Currently it supports only sgml. We can remove the code if attributes handling
                         * will be added to other tagged markup filters
                         */
                        boolean dontSegmentOrCount = true;
                        if(segmenterTable.getClass().getName().contains("DocbookSegmenterTable")) {
                            Map tagAttributes  = ((org.jvnet.olt.parsers.SgmlDocFragmentParser.SimpleNode)simpleNode).getAttribs();
                            dontSegmentOrCount = ((org.jvnet.olt.filters.sgml.docbook.DocbookSegmenterTable)segmenterTable).dontSegmentOrCountInsideTag(tagName,namespaceID,tagAttributes);
                        } else {
                            dontSegmentOrCount = segmenterTable.dontSegmentOrCountInsideTag(tagName,namespaceID);
                        }
                        
                        if (dontSegmentOrCount) {
                            inlineNoSegNoCountBuffer.append(nodeData);
                            if (!tagTable.tagEmpty(tagName,namespaceID) && !simpleNode.isEmptyTag()){
                                // if it's not an empty tag, we want to protect the entire tag contents
                                tagState = NONCOUNTABLEORSEGMENTABLE;
                                updateSegmentationState(tagState);
                                nonSegmentableNonCountableStack.push(tag);
                            } else if (nonSegmentableNonCountableStack.isEmpty()){
                                // just protect the tag itself - important for JSP files
                                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                                inlineNoSegNoCountBuffer = new StringBuffer();
                            }
                            
                            
                        } // NonSegmentable (eg. <phrase> tags in Docbook)
                        else if (segmenterTable.dontSegmentInsideTag(tagName,namespaceID)){
                            inlineNoSegBuffer.append(nodeData);
                            if (!tagTable.tagEmpty(tagName,namespaceID) && !simpleNode.isEmptyTag()){
                                // if it's not an empty tag, we protect the tag and it's contents
                                tagState = NONSEGMENTABLE;
                                updateSegmentationState(tagState);
                                nonSegmentableStack.push(tag);
                            } else if (nonSegmentableStack.isEmpty()){
                                // just protect the tag itself - important for JSP files
                                buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                                inlineNoSegBuffer = new StringBuffer();
                            }
                            
                        } // NonTranslatable
                        else if (!segmenterTable.containsTranslatableText(tagName,namespaceID)){
                            inlineNoTransBuffer.append(nodeData);
                            if (!tagTable.tagEmpty(tagName,namespaceID) && !simpleNode.isEmptyTag()){
                                tagState = NONTRANSLATABLE;
                                updateSegmentationState(tagState);
                                nonTranslatableStack.push(tag);
                            } else if (nonSegmentableStack.isEmpty()){
                                // just protect the tag itself - important for JSP files
                                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                                inlineNoTransBuffer = new StringBuffer();
                            }
                        }
                        // Verbatim layout (like <pre>, but remember this is for inline tags only!)
                        else if (tagTable.tagForcesVerbatimLayout(tagName,namespaceID)){
                            verbatimBuffer.append(nodeData);
                            if (!tagTable.tagEmpty(tagName,namespaceID) && !simpleNode.isEmptyTag()){
                                verbatimStack.push(tag);
                                tagState = VERBATIMLAYOUT;
                                updateSegmentationState(tagState);
                            } else if (verbatimStack.isEmpty()){
                                // just protect the tag itself - important for JSP files
                                buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"donttranslate"));
                                verbatimBuffer = new StringBuffer();
                            }
                            
                        } // Normal inline tags
                        else {
                            updateSegmentationState(tagState);
                            buf.append(nodeData);
                        }
                        break;
                    case VERBATIMLAYOUT:
                        updateSegmentationState(tagState);
                        verbatimStack.push(tag);
                        verbatimBuffer.append(nodeData);
                        break;
                    case NONTRANSLATABLE:
                        updateSegmentationState(tagState);
                        nonTranslatableStack.push(tag);
                        inlineNoTransBuffer.append(nodeData);
                        break;
                    case NONCOUNTABLEORSEGMENTABLE:
                        updateSegmentationState(tagState);
                        nonSegmentableNonCountableStack.push(tag);
                        inlineNoSegNoCountBuffer.append(nodeData);
                        break;
                    case NONSEGMENTABLE:
                        updateSegmentationState(tagState);
                        nonSegmentableStack.push(tag);
                        inlineNoSegBuffer.append(nodeData);
                        break;
                    default:
                        System.out.println("WARNING : Dropped off the end of a case statement !");
                }
                
            } else { // We've Hit A Block Node ---------------
                /* the aim here, is to determine whether this is a block node
                 * that should have it's contents written to the xliff file
                 * or whether it's a block node that should have *both* it's contents
                 * and it's tags written to the xliff file
                 */
                // Need to implement this
                
                // Depending on which state we're in, we need to empty our buffers
                // and add them into the main buf for segmentation.
                
                // TODO walk back up the tag state, adding buffer contents in
                // order (starting from the first-added element)
                switch (tagState){
                    case DEFAULT:
                        // don't need to do anything here.
                        break;
                    case VERBATIMLAYOUT:
                        buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                        verbatimBuffer = new StringBuffer();
                        break;
                    case NONTRANSLATABLE:
                        formatter.writeFormatting(inlineNoTransBuffer.toString());
                        inlineNoTransBuffer = new StringBuffer();
                        break;
                    case NONCOUNTABLEORSEGMENTABLE:
                        buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                        inlineNoSegNoCountBuffer = new StringBuffer();
                        break;
                    case NONSEGMENTABLE:
                        buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegNoCountBuffer),"dontsegment"));
                        inlineNoSegBuffer = new StringBuffer();
                        break;
                    default:
                        System.out.println("WARNING : Dropped off the end of a case statement !");
                }
                
                
                
                // If we need to put this in a new segment, let's do that
                // if we've got a block tag that needs to be written to the content.xlf
                if (segmenterTable.containsTranslatableAttribute(tagName,namespaceID)) {
                    // previously, we were only writing block tags that were
                    // empty <bla/> - as an experiment, we'll try writing the open
                    // tag to the content.xlf (the close tag goes in the skl file still)
                    int count = SgmlFilterHelper.countSegment(nodeData, segmenterTable, tagTable, language, tagList);
                    // just because the tag may contain translatable elements, doesn't
                    // mean that it actually does. Having done a wordcount of these, we
                    // only write the segment if there's stuff to translate.
                    if (count > 0){
                        // deal with any text in our buffers already
                        buf = handleBlockNode(buf, simpleNode, false);
                        // then write this single block node as a segment
                        writeSegment(formatter, nodeData, count);
                        
                        // see if we need to clear the closedOnLastSegRun
                        // (read comment where this global variable was defined)
                        if (segmenterTable.getBlockLevel(tagName) != Segment.SOFT &&
                                segmenterTable.getBlockLevel(tagName) != Segment.SOFTFLOW){
                            // remove only the tags from here, not the marked sects
                            removeTagsFromList(closedOnLastSegmentationRun);
                        }
                    } else {
                        buf = handleBlockNode(buf, simpleNode, true);
                        tagState = getTagState(tagName, namespaceID);
                    }
                } else { // it's a normal block node
                    buf = handleBlockNode(buf, simpleNode, true);
                }
                // Now, just to check to see if this tag should cause us to
                // change our state, and set it appropriately...
                // Note, that we're rudely *not* putting the tag state on
                // the stack : this is because for, say broken HTML input
                // we're not guaranteed that we'll ever see a close tag.
                // on hitting a close-block-level tag, we always revert back
                // to default state FIXME (that means, this xml :
                // <donttranslate> rubbish <donttranslateeither> more </donttranslate>
                // </donttranslate> will fail, assuming all of those tags
                // are block tags.
                tagState = getTagState(tagName, namespaceID);
                updateSegmentationState(tagState);
                
            } // end of block node
            
        } // end of marked sect.
    }
    
    
    
    protected void processCloseTag(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        //System.out.println(nodeData);
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            } else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        }else {
            // if people are using </>, then it's a shorthand
            // to mean "close the last opened tag" - so we get that
            // off the stack. (the tagName will be empty in this case)
            if (tagName.length() == 0){
                if (openTagStack.size() > 0){
                    Tag tag = (Tag)openTagStack.pop();
                    tagName = tag.getName();
                } else tagName = "";
                
                if (tagName == null){
                    tagName = "";
                }
            } else { // pop off the stack anyway
                if (!openTagStack.empty()){
                    //System.out.println("popped " + openTagStack.peek() +" off stack");
                    openTagStack.pop();
                } //else {System.out.println("not popping when seeing " + tagName + "!!"); }
            }
            
            if (tagTable.tagMayContainPcdata(tagName,namespaceID)){
                switch (this.tagState){
                    case NONSEGMENTABLE:
                        inlineNoSegBuffer.append(nodeData);
                        if (nonSegmentableStack.size() > 0){
                            nonSegmentableStack.pop();
                        }
                        if (nonSegmentableStack.size() == 0){ // if there's nothing on our stack
                            // then we're back at normal segmentable text
                            // System.out.println("Back at normal text again ! " + tagName);
                            buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                            inlineNoSegBuffer = new StringBuffer();
                        }
                        break;
                    case NONCOUNTABLEORSEGMENTABLE: // if we're not segmenting OR counting
                        inlineNoSegNoCountBuffer.append(nodeData);
                        if (nonSegmentableNonCountableStack.size() > 0){
                            nonSegmentableNonCountableStack.pop();
                        }
                        if (nonSegmentableNonCountableStack.size() == 0){ // if there's nothing on our stack
                            // then we're back at normal segmentable text
                            // System.out.println("Back at normal text again ! " + tagName);
                            buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                            inlineNoSegNoCountBuffer = new StringBuffer();
                        }
                        break;
                    case NONTRANSLATABLE: // if we're not translating
                        inlineNoTransBuffer.append(nodeData);
                        if (nonTranslatableStack.size() > 0){
                            nonTranslatableStack.pop();
                        }
                        if (nonTranslatableStack.size() == 0){
                            //System.out.println("Appending non-trans text to buffer");
                            buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(), "donttranslate"));
                            //System.out.println(buf.toString());
                            inlineNoTransBuffer = new StringBuffer();
                        }
                        break;
                    case VERBATIMLAYOUT:
                        verbatimBuffer.append(nodeData);
                        if (verbatimStack.size() > 0){
                            verbatimStack.pop();
                        }
                        if (verbatimStack.empty()){
                            buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                            verbatimBuffer = new StringBuffer();
                        }
                        break;
                    case DEFAULT:
                        buf.append(nodeData);
                        break;
                    default:
                        System.out.println("WARNING - dropped off the end of a case statement !");
                }
                // lastly, go back to our previous state :
                this.tagState =  retrieveSegmentationState();
                
            } else { // we've hit a block node - turn *everything* off
                // in case we haven't closed our various special buffers properly (eg. no close tags)
                // we'll make sure that it's appended into our main buffer - if those buffer are
                // empty anyway, this won't do any harm.
                
                // TODO : Problem -  we're adding these in the wrong order -- need to fix that
                // Similar notes under processMarkedSectTag, processOpenTag
                buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                
                verbatimBuffer = new StringBuffer();
                inlineNoSegBuffer = new StringBuffer();
                inlineNoSegNoCountBuffer = new StringBuffer();
                inlineNoTransBuffer = new StringBuffer();
                
                // back to default state, yes always - we don't support
                // nesting of block-level tags yet. (FIXME)
                // we might want to make this a html-specific action, again
                // we do this in case of broken html input..
                stateStack.clear();
                tagState = DEFAULT;
                updateSegmentationState(tagState);
                
                nonSegmentableStack.clear();
                nonSegmentableNonCountableStack.clear();
                nonTranslatableStack.clear();
                verbatimStack.clear();
                
                buf = handleBlockNode(buf, simpleNode, true);
            }
        }
    }
    
    
    protected void processEntity(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            }  else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            switch (tagState){
                case NONSEGMENTABLE:
                    inlineNoSegBuffer.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
                    break;
                case NONCOUNTABLEORSEGMENTABLE:
                    inlineNoSegNoCountBuffer.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
                    break;
                case NONTRANSLATABLE:
                    inlineNoTransBuffer.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
                    break;
                case VERBATIMLAYOUT:
                    verbatimBuffer.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
                    break;
                case DEFAULT:
                    buf.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
            }
        }
    }
    
    
    protected void processPcdata(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        //System.out.println(nodeData);
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            }  else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            boolean nonTranslatableBlockPcData = nonTranslatableBlockPcData();
            // the question here, is do we write non translatable data inline
            // or do we write it to the skeleton file ?
            if (nonTranslatableBlockPcData && tagState == NONTRANSLATABLE){
                /* no neeed to make a decision here!
                 tagState = NONTRANSLATABLE;
                updateSegmentationState(tagState);*/
                // have to be careful, if we've content in any of our
                // buffers, we need to write that into the main buffer and
                // do segmentation if necessary.
                buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                verbatimBuffer = new StringBuffer();
                inlineNoSegBuffer = new StringBuffer();
                inlineNoSegNoCountBuffer = new StringBuffer();
                inlineNoTransBuffer = new StringBuffer();
                inlineNoTransBuffer.append(nodeData);
                if (buf.length()!=0){
                    doSentenceSegmentation(buf);
                }
                buf = new StringBuffer();
                formatter.writeFormatting(nodeData);
            } else {
                
                switch (tagState){
                    case NONSEGMENTABLE:
                        inlineNoSegBuffer.append(nodeData);
                        break;
                    case NONCOUNTABLEORSEGMENTABLE:
                        inlineNoSegNoCountBuffer.append(nodeData);
                        break;
                    case NONTRANSLATABLE:
                        inlineNoTransBuffer.append(nodeData);
                        break;
                    case VERBATIMLAYOUT:
                        verbatimBuffer.append(nodeData);
                        break;
                    case DEFAULT:
                        // whitespace normalise here
                        buf.append(SgmlFilterHelper.normalise(new StringBuffer(nodeData)));
                        
                        break;
                    default:
                        System.out.println("WARNING : dropped off the end of a case statement!");
                }
            }
        }
    }
    
    protected void processProcessingInst(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        // done the same the way we handle pcdata, except the
        // pi is always written as a non-segmentable piece of text
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            }  else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            boolean atInlineTag = true;
            if (!openTagStack.isEmpty()){
                Tag peeked = (Tag)openTagStack.peek();
                atInlineTag = tagTable.tagMayContainPcdata(peeked.getName(),peeked.getNameSpaceID());
            }
            
            switch (tagState){
                case NONSEGMENTABLE:
                    if (!atInlineTag){
                        formatter.writeFormatting(nodeData);
                    } else {
                        inlineNoSegBuffer.append(nodeData);
                    }
                    break;
                case NONCOUNTABLEORSEGMENTABLE:
                    if (!atInlineTag){
                        formatter.writeFormatting(nodeData);
                    } else {
                        inlineNoSegNoCountBuffer.append(nodeData);
                    }
                    break;
                case NONTRANSLATABLE:
                    if (!atInlineTag){
                        formatter.writeFormatting(nodeData);
                    } else {
                        inlineNoTransBuffer.append(nodeData);
                    }
                    inlineNoTransBuffer.append(SgmlFilterHelper.convertCharacterEntities(nodeData, segmenterTable));
                    break;
                case VERBATIMLAYOUT:
                    verbatimBuffer.append(nodeData);
                    break;
                case DEFAULT:
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(nodeData, "dontsegmentorcount"));
                    break;
                default:
                    System.out.println("WARNING : dropped off the end of a case statement!");
            }
        }
    }
    
    protected void processEntityDecl(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException {
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                includedMarkedSectionNodes.add(simpleNode);
            } else {
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            String[] val = SgmlFilterHelper.parseEntity(simpleNode.getNodeData(), gvm);
            writeEntityDecl(val, simpleNode.getNodeData());
        }
    }
    
// simply save the entire marked section, either in the ignored
// buffer, or the included buffer, and deal with it later.
    protected void processMarkedSectTag(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        String markedSectFlag = simpleNode.getMarkedSectFlag();
        
        // if we're already in a marked sect - save in the appropriate bucket.
        if (!markedSectionStack.empty()){
            if (inIncludedMarkedSection()){
                markedSectionStack.push(Boolean.TRUE);
                includedMarkedSectionNodes.add(simpleNode);
            }  else {
                markedSectionStack.push(Boolean.FALSE);
                ignoredMarkedSectionNodes.add(simpleNode);
            }
        } else {
            // otherwise, resolve it, and then store in the appropriate bucket
            if (SgmlFilterHelper.isIncludedMarkedSection(markedSectFlag, gvm)){
                markedSectionStack.push(Boolean.TRUE);
                includedMarkedSectionNodes.add(simpleNode);
            } else {
                markedSectionStack.push(Boolean.FALSE);
                ignoredMarkedSectionNodes.add(simpleNode);
            }
            
            /*if (tagList.size() == 0){ This is a failed fix for 6234207, commented out
                // if we've got text in our buffers, but have no tags open - so in that case, this
                // marked sect will be considered as if it were a block tag, so now's a good time to
                // write any segments we may have accumulated in our buffers.
                // FIXME Note that this is bad, since we don't know what order this stuff arrived in, so
                // this may be a cause of a bug - similar notes under processEof,processOpenTag, processCloseTag
                buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                // non segmentable text probably should be normalised
                buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                if (buf.length() != 0){
                    doSentenceSegmentation(buf);
                }
                buf = new StringBuffer();
                verbatimBuffer = new StringBuffer();
                inlineNoSegBuffer = new StringBuffer();
                inlineNoSegNoCountBuffer = new StringBuffer();
                inlineNoTransBuffer = new StringBuffer();
            }*/
        }
    }
    
    
    protected void processEndMarkedSect(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        
        if (!markedSectionStack.empty()){
            //System.out.println("Testing ms state !! ----- include=" + inIncludedMarkedSection());
            if (inIncludedMarkedSection()){
                markedSectionStack.pop();
                includedMarkedSectionNodes.add(simpleNode);
                if (markedSectionStack.empty()){
                    // deal with the contents of our pre-existing buffers first, before
                    // dealing with the marked sect stuff.
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                    verbatimBuffer = new StringBuffer();
                    inlineNoSegBuffer = new StringBuffer();
                    inlineNoSegNoCountBuffer = new StringBuffer();
                    inlineNoTransBuffer = new StringBuffer();
                    
                    // Now work out whether we need to write ms content to xlf or skeleton,
                    // so we look for the first "interesting thing" inside the marked sect.
                    // and write to the skl or write to the xlf
                    TaggedMarkupNode node = (TaggedMarkupNode)includedMarkedSectionNodes.get(0);
                    ArrayList tmpList = new ArrayList();
                    tmpList.addAll(includedMarkedSectionNodes);
                    includedMarkedSectionNodes.clear();
                    if (nextItemIsBlockTag(tmpList)){
                        // write the beginning marked section stuff to skeleton, and
                        // iterate over the list of items, calling visit() for each node except
                        // the last, which we write as skeleton.
                        formatter.writeFormatting("<![ "+node.getMarkedSectFlag()+" [");
                        for (int i=1; i<tmpList.size()-1;i++){
                            TaggedMarkupNode myNode = (TaggedMarkupNode)tmpList.get(i);
                            //System.out.println("processing " +myNode+": "+ myNode.getNodeData());
                            this.visit(myNode, null);
                        }
                        formatter.writeFormatting("]]>");
                    } else { // next item is inline tag or pcdata
                        buf.append("<![ "+node.getMarkedSectFlag()+" [");
                        for (int i=1; i<tmpList.size()-1;i++){
                            TaggedMarkupNode myNode = (TaggedMarkupNode)tmpList.get(i);
                            //System.out.println("processing inline " +myNode+": "+ myNode.getNodeData());
                            this.visit(myNode, null);
                        }
                        buf.append("]]>");
                    }
                }
            } else { //an ignored ms, similar treatment to the above
                markedSectionStack.pop();
                ignoredMarkedSectionNodes.add(simpleNode);
                if (markedSectionStack.empty()){
                    // deal with any text we have hanging around
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
                    buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
                    verbatimBuffer = new StringBuffer();
                    inlineNoSegBuffer = new StringBuffer();
                    inlineNoSegNoCountBuffer = new StringBuffer();
                    inlineNoTransBuffer = new StringBuffer();
                    
                    
                    
                    //System.out.println("Got IGNORED marked section"+printNodes(ignoredMarkedSectionNodes));
                    if (nextItemIsBlockTag(ignoredMarkedSectionNodes)){
                        // need to segment what we've got in buf so far (fix for 6234207)
                        if (buf.length() != 0){
                            doSentenceSegmentation(buf);
                        }
                        buf = new StringBuffer();
                        verbatimBuffer = new StringBuffer();
                        inlineNoSegBuffer = new StringBuffer();
                        inlineNoSegNoCountBuffer = new StringBuffer();
                        inlineNoTransBuffer = new StringBuffer();
                        // System.out.println("nextItemIsBLockTag retuned true " + printNodes(ignoredMarkedSectionNodes) +" goes to block");
                        // write the beginning marked section stuff to skeleton, and
                        // iterate over the list of items, adding each to our ignored buffer
                        TaggedMarkupNode node = (TaggedMarkupNode)ignoredMarkedSectionNodes.get(0);
                        ArrayList tmpList = new ArrayList();
                        tmpList.addAll(ignoredMarkedSectionNodes);
                        ignoredMarkedSectionNodes.clear();
                        formatter.writeFormatting("<![ "+node.getMarkedSectFlag()+" [");
                        for (int i=1; i<tmpList.size();i++){
                            TaggedMarkupNode myNode = (TaggedMarkupNode)tmpList.get(i);
                            formatter.writeFormatting(myNode.getNodeData());
                        }
                    } else { // next item is inline tag or pcdata
                        StringBuffer ignoredSectionBuffer = new StringBuffer();
                        TaggedMarkupNode node = (TaggedMarkupNode)ignoredMarkedSectionNodes.get(0);
                        ArrayList tmpList = new ArrayList();
                        tmpList.addAll(ignoredMarkedSectionNodes);
                        ignoredMarkedSectionNodes.clear();
                        ignoredSectionBuffer.append("<![ "+node.getMarkedSectFlag()+" [");
                        for (int i=1; i<tmpList.size();i++){
                            TaggedMarkupNode myNode = (TaggedMarkupNode)tmpList.get(i);
                            ignoredSectionBuffer.append(myNode.getNodeData());
                        }
                        buf.append(SgmlFilterHelper.insertSegmentationProtection(ignoredSectionBuffer.toString(), "donttranslate"));
                        ignoredSectionBuffer = new StringBuffer();
                    }
                }
            }
            
            
        } else {
            System.out.println("Marked sect stack is empty");
            System.out.println("This can't happen - We've hit a close MarkedSection when the MS Stack is empty !!!");
            throw new SegmenterFormatterException("CAN'T HAPPEN - Close MS when MS Stack is empty !! - please report a bug to suntrans support.");
        }
    }
    
    /**  Comments in sgml/xml-like files should nearly always be ignored in
     * translatable files and written as formatting into the skeleton files.
     * The one exception is in html files, when dealing with the contents inside
     * &lt;script&gt; elements. In these case, we often see constructions like :
     * &lt;script&gt;
     * &lt;!-- // now follows the actual javascript content :
     * .
     * .
     * --&gt;
     *&lt;/script&gt;
     *This section is specifically for that case, by consulting a method in the SegmenterTable
     *we determine if the element should have it's comments written to the correct translatble buffer,
     *otherwise we write as formatting.
     */
    protected void processComment(TaggedMarkupNode simpleNode) throws SegmenterFormatterException{
        if (tagList.size() > 0 ){
            Tag tag = (Tag)tagList.get(tagList.size()-1);
            if (segmenterTable.includeCommentsInTranslatableSection(tag.getName())) {
                String comment = simpleNode.getNodeData();
                switch (tagState){
                    case NONCOUNTABLEORSEGMENTABLE:
                        inlineNoSegNoCountBuffer.append(comment);
                        break;
                    case NONSEGMENTABLE:
                        inlineNoSegBuffer.append(comment);
                        break;
                    case VERBATIMLAYOUT:
                        verbatimBuffer.append(comment);
                        break;
                    case NONTRANSLATABLE:
                        inlineNoTransBuffer.append(comment);
                        break;
                    case DEFAULT:
                        buf.append(comment);
                        break;
                    default:
                        System.out.println("WARNING - dropped off the end of a case statement in COMMENT !");
                }
                
            } else {
                formatter.writeFormatting(simpleNode.getNodeData());
            }
        } else {
            formatter.writeFormatting(simpleNode.getNodeData());
        }
    }
    
    protected void processEOF(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
        String tagName = simpleNode.getTagName();
        String namespaceID = simpleNode.getNamespaceID();
        String nodeData = simpleNode.getNodeData();
        // at the end of the file - again, do segmentation,
        // then flush the formatter (since we're finished)
        // again, FIXME : don't know what order these are in.
        // For most Sgml, we're probably fine, since this rarely happens. There's
        // a similar comment in processCloseTag, processOpenTag and processMarkedSection
        buf.append(SgmlFilterHelper.insertSegmentationProtection(verbatimBuffer.toString(),"verbatim"));
        // non segmentable text probably should be normalised
        buf.append(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer),"dontsegment"));
        buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(),"dontsegmentorcount"));
        buf.append(SgmlFilterHelper.insertSegmentationProtection(inlineNoTransBuffer.toString(),"donttranslate"));
        
        if (buf.length() != 0){
            doSentenceSegmentation(buf);
        }
        formatter.flush();
        buf = new StringBuffer(BUFFSIZE);
    }
    
    protected void processCdata(TaggedMarkupNode simpleNode) throws SegmenterFormatterException, IOException{
         /* We deal with CDATA by treating it as a section of verbatim layout
          * inline text.
          */
        // deal with whatever stuff we have at the moment to get it out of the way
        switch (tagState){
            case VERBATIMLAYOUT:
                verbatimBuffer = handleBlockNode(verbatimBuffer,
                        simpleNode, false);
                break;
            case NONSEGMENTABLE:
                StringBuffer tmp = new StringBuffer(SgmlFilterHelper.insertSegmentationProtection(SgmlFilterHelper.normalise(inlineNoSegBuffer), "dontsegment"));
                inlineNoSegBuffer = handleBlockNode(tmp, simpleNode, false);
                break;
            case NONCOUNTABLEORSEGMENTABLE:
                StringBuffer non = new StringBuffer(SgmlFilterHelper.insertSegmentationProtection(inlineNoSegNoCountBuffer.toString(), "dontsegmentorcount"));
                inlineNoSegNoCountBuffer = handleBlockNode(non, simpleNode, false);
                break;
            case DEFAULT:
                buf =  handleBlockNode(buf, simpleNode, false);
                break;
            default:
                System.out.println("WARNING - dropped off the end of a case statement !!");
                
        }
        updateSegmentationState(this.tagState);
        tagState = VERBATIMLAYOUT;
        
        formatter.writeFormatting("<![CDATA[");
        String cdata = simpleNode.getNodeData();
        TaggedMarkupNode tmp = simpleNode;
        tmp.setNodeData(SgmlFilterHelper.wrapXMLChars(cdata.substring(9,cdata.length()-3)));
        processPcdata(tmp);
        
        verbatimBuffer = handleBlockNode(verbatimBuffer, simpleNode, false);
        formatter.writeFormatting("]]>");
        tagState = retrieveSegmentationState();
    }
    
    /** This method gets called when we've hit a block node, and want to segment the
     * text we've built up in the input StringBuffer. The contents of the buffer are
     * passed onto the doSentenceSegmentation method for actual segmentation. This
     * method deals with some formatting and segmentation things that are left over
     * (such as controlling the state of the "dontSegment" flag.)
     * @param buf a StringBuffer containing text to be segmented
     * @param simpleNode the simpleNode containing the block tag we've just encountered
     * @throws SgmlFilterException if there was a problem encountered segmenting the contents of the StringBuffer
     * @return a new StringBuffer.
     */
    protected StringBuffer handleBlockNode(StringBuffer buf,
            TaggedMarkupNode simpleNode, boolean writeBlockNode)
            throws SgmlFilterException {
        
        // We've reached a block node - so we need to segment the text that we've
        // built up in our pcdata/inline tags buffer, and then write the appropriate
        // block formatting elements to the SegmenterFormatter.
        if (buf.length() != 0){
            List segmentList;
            try {
                segmentList = doSentenceSegmentation(buf);
            } catch (Throwable e){
                System.err.println("Caught problem segmenting :"+buf.toString()+":");
                throw new SgmlFilterException("Problem caught during handleBlockNode() " + e.getMessage());
                
            }
            // this is awkward - if I get a single whitespace, I'd like to keep
            // that and output it as formatting (rather than as part of the
            // segment) and keep the original character, rather than it's normalised ' ' version
            if (segmentList.size() == 1){
                String seg = (String)segmentList.get(0);
                if (seg.length() > 0){
                    if (Character.isWhitespace(seg.charAt(0)) && seg.length() == 1){
                        try{
                            formatter.writeFormatting(buf.toString());
                        } catch (SegmenterFormatterException e){
                            throw new SgmlFilterException("Problem caught trying to write formatting :"+buf.toString()+":");
                        }
                    }
                }
            }
        } else {
            ;//formatter.writeNonAlignable("<!-- TF  empty segment -->");
        }
        
        buf = new StringBuffer(BUFFSIZE);
        //formatter.writeFormatting("<!-- TF ----------block separator----------- -->");
        try {
            if (writeBlockNode){
                formatter.writeFormatting(simpleNode.getNodeData(), segmenterTable.getBlockLevel(simpleNode.getTagName(), simpleNode.getNamespaceID()));
            }
        } catch (SegmenterFormatterException e){
            throw new SgmlFilterException("Problem caught trying to write formatting :"+simpleNode.getNodeData()+":");
        }
        
        // closedTagsOnLastSegmentationRun determines what tags we "remember" the tag context
        // we propogate to the tags that come after the one we're looking at now - if we've
        // reached a block segment, we may want to just forget about all open inline
        // tags we've seen so far.
        /* eg. for tables, we can forget that we're in a <b> context, but for <p> and
         * <br> tags, we'd like to keep them.
         *
         * <b>This is a bold segment
         *   <table><tr><td>this is not bold</td>
         *          </tr>
         *   </table>
         *   still not bold anymore
         *
         * vs.
         *  <b>This is a bold segment<br>
         *     a new line also bold
         *     <p> This paragraph is bold also
         *
         *
         */
        if (segmenterTable.getBlockLevel(simpleNode.getTagName(), simpleNode.getNamespaceID()) != Segment.SOFT &&
                segmenterTable.getBlockLevel(simpleNode.getTagName(), simpleNode.getNamespaceID()) != Segment.SOFTFLOW){
            // go through the closedOnLastSegmentationRun, and remove all tags
            // don't think we should be forgetting marked sections if we
            // hit a block tag
            removeTagsFromList(closedOnLastSegmentationRun);
        } else {
            //System.out.println("Not emptying ClosedTags on last segmentation Run !!"+ simpleNode.getTagName());
            //System.out.println("Buf is " + buf);
        }
        tagList = new ArrayList();
        return buf;
    }
    
    /** This method actually performs segmentation. It also performs whitespace normalisation
     * if we're not in a verbatimLayout tag.
     * @return A List of segments if applicable. If the text is just whitespace, then we return an empty List.
     * @param buf A string buffer containing the plaintext to be segmented (note that this buf
     * may contain tags)
     * @throws SgmlFilterException if some error was encountered during segmentation
     * @throws SegmenterFormatterException if some error was encountered while writing out segments or formatting using
     * the SegmenterFormatter object currently in use.
     */
    protected List doSentenceSegmentation(StringBuffer buf)
    throws SgmlFilterException, SegmenterFormatterException {
        
        // remove segmentation protection for verbatim layout contents
        buf = new StringBuffer(SgmlFilterHelper.removeSegmentationProtection(buf.toString(), "verbatim"));
        
        if (buf.length() == 1 && (Character.isWhitespace(buf.charAt(0)))){
            ArrayList tmp = new ArrayList();
            tmp.add(new String(
                    Character.toString(buf.charAt(0))
                    ));
            return tmp;
        }
        // I don't think this can ever happen : should throw an exception
        // since it suggests that we've been given nothing to segment, or the
        // normaliser has returned an empty string - either way, it's a bug
        if (buf.length() == 0){
            throw new SgmlFilterException("------ Can't Happen : been given nothing to segment - report a bug ! ----");
        }
        
        List tmpSegments = new ArrayList();
        List fixedSegments = new ArrayList();
        Map formatting = new HashMap();
        StringReader reader = new StringReader(buf.toString());
        segmenterFacade = new SegmenterFacade(reader, language);
        try {
            segmenterFacade.parse();
        } catch (Throwable e){
            throw new SgmlFilterException("Caught an exception doing sentence segmentation " + e.getMessage());
        }
        
        tmpSegments = segmenterFacade.getSegments();
        formatting = segmenterFacade.getFormatting();
        
        /*
         * At this point, I need to "clean up" the segments I've got - that is,
         * if a tag was cut in half, mid segment, I have to fix that, also deal
         * with whitespace at the start and end of the segment writing them as
         * mid-segment-whitespace (write with a special method in segmenterformatter)
         */
        
        Iterator it = tmpSegments.iterator();
        int counter=0;
        // write out the formatting for that segment
        while ( it.hasNext() ){
            if (formatting.containsKey(new Integer(counter))){
                String s = (String)formatting.get(new Integer(counter));
                if (s.length() != 0){
                    formatter.writeMidSegmentFormatting((String)formatting.get(new Integer(counter)));
                }
                // remove that object from the formatting map (since it's been dealt with)
                formatting.remove(new Integer(counter));
                
            }
            // Now Fix the tags - make sure that tags that were closed on the last segmentation
            // run get opened again for this segment (complex stuff)
            String unfixedSegment = (String) it.next();
            String result = fixTags(unfixedSegment, closedOnLastSegmentationRun);
            
            if (result != null){
                // now remove the leading and trailing spaces and write them as mid-segment formatting
                String s = SgmlFilterHelper.getLeadingWhitespace(result);
                if (s.length() != 0){
                    formatter.writeMidSegmentFormatting(s);
                    result = SgmlFilterHelper.stripLeadingWhitespace(result);
                }
                
                String trailing = SgmlFilterHelper.getTrailingWhitespace(result);
                if (trailing.length() != 0){
                    result = SgmlFilterHelper.stripTrailingWhitespace(result);
                }
                // if we've got a segment with just spaces, stripping the leading
                // and trailing spaces can result in an empty segment. if this is
                // the case, we don't write the segment out.
                if (result.length() != 0){
                    fixedSegments.add(result);
                    //System.out.println("Segment: "+result);
                    int count = SgmlFilterHelper.countSegment(unfixedSegment, segmenterTable, tagTable, language, tagList, gvm);
                    writeSegment(formatter, result, count);
                    //formatter.writeSegment(result,count);
                }
                
                if (trailing.length() != 0){
                    formatter.writeMidSegmentFormatting(trailing);
                }
            }
            counter++;
        }
        
        // Have to write out the remaining (if any) elements in the formatting map (pcdata remember!)
        // this can happen if there's formatting after the last segment
        //System.out.println("Remaining formatting :" + formatting.keySet().size());
        it = formatting.keySet().iterator();
        
        while (it.hasNext()){
            Integer i = (Integer) it.next();
            String s = (String)formatting.get(i);
            formatter.writeMidSegmentFormatting(s);
        }
        return fixedSegments;
    }
    
    
    
    /** this method takes a (possibly) broken segment, and makes sure that the tags balance
     * correctly according to the tagTable that it uses. Note that only tags that need to be
     * closed will be closed ( if (!tagTable.tagEmpty(tagname)) // close tag )
     * It also converts any character
     * references to their unicode character values.
     * @see SgmlSegmentFactoryVisitor#
     * @return a String with the correct tagging
     * @param closedMSOnLastSegmentationRun any marked sections that were closed on the last segmentation run (used so we
     * can add them at the front of this segmentation run)
     * @param closedOnLastRun a list of tags that were closed the last time we run this method.
     * @param segment the segment to be fixed
     * @throws SgmlFilterException if some error was enountered while fixing tags.
     */
    protected String fixTags(String segment, List closedOnLastRun) throws SgmlFilterException{
        
        
        StringBuffer result= new StringBuffer();
        StringReader tagFixReader = new StringReader(segment);
        NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(tagFixReader);
        
        try {
            parser = new  NonConformantSgmlDocFragmentParser(tagFixReader);
            parser.parse();
            
            SgmlSegmentCorrectorVisitor sgmlSegmentCorrector =
                    new SgmlSegmentCorrectorVisitor(tagTable,closedOnLastRun, tagList);
            // useful for debugging
            sgmlSegmentCorrector.setValue(result.toString());
            parser.walkParseTree(sgmlSegmentCorrector, null);
            result.append(sgmlSegmentCorrector.getTextAtBeginning());
            result.append(segment);
            result.append(sgmlSegmentCorrector.getTextAtEnd());
	    
            /* for debugging :
             * if (!segment.equals(result.toString())){
             *    System.out.println("fixTags Altered a segment :");
             *    System.out.println("original = " + segment);
             *    System.out.println("Fixed = " + result.toString());
            }*/
	   
            // remove empty tag in the beginning in case tag correction is done 
            if(!result.toString().equals(segment.toString())) {
                StringReader tagNormaliseReader = new StringReader(result.toString());
                parser = new  NonConformantSgmlDocFragmentParser(tagNormaliseReader);
                parser.parse();
                SgmlSegmentNormaliserVisitor normaliser = new SgmlSegmentNormaliserVisitor(tagTable);
                parser.walkParseTree(normaliser, null);
                result = new StringBuffer(normaliser.getSegment());

            }
        } catch (Throwable e){
            e.printStackTrace();
            throw new SgmlFilterException("Exception while correcting tags on "+ segment+ " - "+ e.getMessage());
        }
        return result.toString();
    }
    
    
    
    private void writeEntityDecl(String[] val, String nodeData) throws SegmenterFormatterException{
        if (val == null){
            formatter.writeFormatting(nodeData);
        }else {
            
            String name = val[SgmlFilterHelper.ENTITYNAME];
            String type = val[SgmlFilterHelper.ENTITYTYPE];
            String value = val[SgmlFilterHelper.ENTITYVALUE];
            
            if (type.equals("INTERNAL")){
                // we have a segmentable entity : not actually doing any segmentation here
                // most text entities are just simple phrases - hope that's okay.
                formatter.writeFormatting("<!ENTITY "+name+" \"");
                writeSegment(formatter, value, SgmlFilterHelper.wordCount(value, language));
                //formatter.writeSegment(value, SgmlFilterHelper.wordCount(value, language));
                formatter.writeFormatting("\" >");
            } else {
                formatter.writeFormatting(nodeData);
            }
        }
    }
    
// don't know if we need this anymore...
    private void removeTagsFromList(List nodeList){
        Iterator it = nodeList.iterator();
        while (it.hasNext()){
            org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode node =
                    (org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.SimpleNode)it.next();
            
            if (node.getType() == NonConformantSgmlDocFragmentParserTreeConstants.JJTOPEN_TAG){
                //System.out.println("Removing " +node.getNodeData());
                it.remove();
                //} else { System.out.println("Not removing "+node.getNodeData());
            }
        }
    }
    
    /** returns true if we haven't collected any text in our buffers
     * - don't know if we need this anymore
     */
    private boolean emptyBuffersP(){
        if (buf.length() == 0 &&
                inlineNoSegBuffer.length() == 0 &&
                inlineNoTransBuffer.length() == 0){
            return true;
        } else {
            return false;
        }
    }
    
    private String printNodes(List nodes){
        Iterator it = nodes.iterator();
        StringBuffer foo = new StringBuffer();
        while (it.hasNext()){
            TaggedMarkupNode node = (TaggedMarkupNode)it.next();
            foo.append(node.getNodeData());
        }
        return foo.toString();
    }
    
    private String printNodesVerbose(List nodes){
        Iterator it = nodes.iterator();
        StringBuffer foo = new StringBuffer();
        while (it.hasNext()){
            TaggedMarkupNode node = (TaggedMarkupNode)it.next();
            foo.append("\t"+node.toString()+": "+node.getNodeData()+"\n");
        }
        return foo.toString();
    }
    
    private boolean inIncludedMarkedSection(){
        // we're in an included marked section
        // if we walk up the stack (from first-in to the last-in)
        // and find nothing but include tags ("True"s on the stack)
        // this allows us to see perhaps a case where we have
        //
        // <![ INCL [ <!IGN <![INCL X [ ]]> ]]> ]]>
        //
        // and we are at position X
        
        Boolean result = Boolean.TRUE;
        for (int i=0; i<markedSectionStack.size();i++){
            Boolean item = (Boolean)markedSectionStack.get(i);
            if (item.booleanValue() == false){
                result = Boolean.FALSE;
            }
        }
        /*if (result.booleanValue()){
            System.out.println("Now in included marked section");
        }else {
            System.out.println("Now in ignored marked section");
        }*/
        return result.booleanValue();
    }
    
    /** this code runs over a list of nodes, and determines if
     * the next "interesting" item is a block tag or not. This is needed when
     * looking at SGML marked sections. For example, given the fragment of text :
     * <![[ %foo; [ <para> so there ]]>
     *
     * It's clear, that the marked section contains a block of sgml, and should
     * be segmented accordingly (resolve %foo; if it's "IGNORE" put the entire
     * marked section into the skeleton file, otherwise put the beginning of the
     * marked section into the skeleton file, and treat the contents of the sect
     * as translatable, putting the end of the marked section also in the skl.
     *
     * It becomes a bit more hazy with things like :
     * <![[ %foo; [ &entityref; &entityref; ]]>
     *
     * Do we go to the trouble of resolving each entity and parsing them to
     * determine if they contain blocks or not ? Tough call to make. For now,
     * we ignore entities as being interesting, and only look at tags, cdata
     * and pcdata.
     * @param nodeList a List of TaggedMarkupNodes containing the contents of the marked section
     * @return true if the next item is a block tag, false otherwise
     *
     */
    private boolean nextItemIsBlockTag(List nodeList){
        boolean found = false;
        boolean isBlockTag = false;
        int i=0;
        while (!found && i<nodeList.size()){
            TaggedMarkupNode node = (TaggedMarkupNode)nodeList.get(i);
            i++;
            switch (this.converter.convert(node.getType())){
                case TaggedMarkupNodeConstants.OPEN_TAG:
                    found = true;
                    String tagName = node.getTagName();
                    if (tagTable.tagMayContainPcdata(tagName)){
                        //System.out.println("looking at inline tag "+tagName+" - not block");
                        isBlockTag = false;
                    } else { isBlockTag = true; }//System.out.println("Looking at block Tag!");}
                    break;
                case TaggedMarkupNodeConstants.PCDATA:
                case TaggedMarkupNodeConstants.CDATA:
                    String str = SgmlFilterHelper.normalise(new StringBuffer(node.getNodeData()));
                    if (!str.equals(" ")){
                        found = true;
                        isBlockTag = false;
                    }
                    break;
                    // not sure what to do here, for the time being we'll
                    // not make any decisions based on these.
                case TaggedMarkupNodeConstants.MARKED_SECTION_TAG:
                case TaggedMarkupNodeConstants.END_MARKED_SECT:
                default :
                    // we can't decide based on this node, so we continue
                    break;
            }
        }
        //System.out.println("returning "+isBlockTag);
        return isBlockTag;
    }
    
    /**
     * This is a delegate to remove segmentation protection from the input
     * string so we can write the segment without any tags that we've added
     * along the way to prevent certain parts of the string from being segmented
     * (for example, stuff in <code> ... <code> tags would confuse the segmenter)
     *
     * @param formatter The segmenter formatter that ultimately gets used to write the segment
     * @param segment The segment we want to write
     * @param count The wordcount of the segment
     *
     */
    private void writeSegment(SegmenterFormatter formatter, String segment, int count ) throws SegmenterFormatterException {
        String s = segment;
        try {
            s = SgmlFilterHelper.removeSegmentationProtection(s, "dontsegment");//, gvm);
            s = SgmlFilterHelper.removeSegmentationProtection(s, "dontsegmentorcount");
            s = SgmlFilterHelper.removeSegmentationProtection(s, "donttranslate");
        } catch (Throwable e){
            // we can live without this working - it sometimes fails on files
            // or any file format where we're allowed to write ><>><<
            System.out.println("Caught exception trying to remove segmentation protection from _"+segment+"_ : " + e.getMessage());
            ;
        }
        formatter.writeSegment(s, count);
    }
    
    /**
     * this walks up the stack of open tags, and determines if we're
     * inside a translatable element. We return on the first non-translatable
     * tag we find, or the first block level tag - whichever comes first. The
     * segmenter table implementations need to decide whether unknown tags
     * default to translatable or non-translatable contents.
     */
    private boolean nonTranslatableBlockPcData(){
        boolean translatable = segmenterTable.pcdataTranslatableByDefault();
        
        if (tagStack.size() > 0){
            for (int i=tagStack.size()-1; i!=-1;i--){
                TaggedMarkupNode top = (TaggedMarkupNode)tagStack.get(i);
                String tag = top.getTagName();
                String ns = top.getNamespaceID();
                
                // we're only interested in pcdata contained in block tags
                // if we see an inline tag, then the answer is immediately "no"
                if (tagTable.tagMayContainPcdata(tag,ns)){
                    return false;
                }
                if (!segmenterTable.containsTranslatableText(tag, ns)){
                    // stop here & make a decision
                    return false;
                }
            }
            return translatable;
        } else {
            return translatable;
        }
    }
    
    /** Manages access to our current state : that is, whether the current
     * text is treated as normal inline content, non-segmentable content,
     * non-translatable, verbatim layout, non-segmentable non-wordcountable ,etc.
     * It's implemented using a simple stack
     */
    private void updateSegmentationState(int state){
        stateStack.push(new Integer(state));
        //System.out.println("Moved to state "+printState(state));
    }
    
    /** returns a new segmentation state - see the comments under
     * updateSegmentationState(int state) for more details.
     */
    private int retrieveSegmentationState(){
        Integer i = new Integer(DEFAULT);
        if (!stateStack.empty()){
            stateStack.pop();
            
            if (!stateStack.empty()){
                i = (Integer)stateStack.peek();
                //System.out.println("Back to state " +printState(i.intValue()));
            } else {
                System.out.println("WARNING - state stack is empty, moving to DEFAULT state !");
            }
        } else {
            System.out.println("WARNING - state stack was empty, moving to DEFAULT state !");
        }
        return i.intValue();
    }
    
    private String printState(int i){
        String stateName = "DEFAULT";
        switch(i){
            case DEFAULT:
                break;
            case NONSEGMENTABLE:
                stateName = "NONSEGMENTABLE";
                break;
            case NONCOUNTABLEORSEGMENTABLE:
                stateName = "NONCOUNTABLEORSEGMENTABLE";
                break;
            case NONTRANSLATABLE:
                stateName = "NONTRANSLATABLE";
                break;
            case VERBATIMLAYOUT:
                stateName = "VERBATIMLAYOUT";
                break;
            default:
                System.out.println("WARNING : Dropped off the end of a case statement in printState ");
        }
        return stateName;
    }
    
    private int getTagState(String tagName, String namespaceID){
        int stateVal = DEFAULT;
        if (tagTable.tagForcesVerbatimLayout(tagName, namespaceID)){
            stateVal = VERBATIMLAYOUT;
        } else if (segmenterTable.dontSegmentInsideTag(tagName, namespaceID)){
            stateVal = NONSEGMENTABLE;
        } else if (segmenterTable.dontSegmentOrCountInsideTag(tagName, namespaceID)){
            stateVal = NONCOUNTABLEORSEGMENTABLE;
        } else if (!segmenterTable.containsTranslatableText(tagName, namespaceID)){
            stateVal = NONTRANSLATABLE;
        }
        return stateVal;
    }
}
