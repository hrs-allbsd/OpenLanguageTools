
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * AlignmentHtmlFormatter.java
 *
 * Created on July 2, 2002, 1:59 PM
 */

package org.jvnet.olt.filters.html;
import org.jvnet.olt.alignment.Segment;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;


/** This HtmlSegmentFormatter gets used when we want to create Segment[] objects for
 * the alignment program for Klemens.
 * @author timf
 */
public class AlignmentHtmlSegmenterFormatter implements SegmenterFormatter {
    private java.util.List segmentList;
    private SegmentImpl seg;
    private StringBuffer formattedString;
    private String language;
    private boolean segmentSeen = false;
    
    /** Creates a new instance of AlignmentHtmlSegmentFormatter
     *
     * @param language This is the language that we're using (needed to supply correct word statistics
     * to the alignment program (so lang-specific abbreviations are recognised))
     */
    public AlignmentHtmlSegmenterFormatter(String language) {
        segmentList = new java.util.ArrayList();
        formattedString = new StringBuffer();
        this.language = language;
        seg = new SegmentImpl();
    }
    
    /** Write a block-level formatting Segment.
     * @param formatting This is the formatting that we're writing - this can be ignored for
     * the purposes of alignment. For debug purposes, it's being written
     * to the Segment.setFormattedSegmentString.
     * @param level This is the "hardness" level of the block formatting Segment that we're
     * writing. These levels are the static variables in the Segment interface.
     */
    public void writeFormatting(String formatting, int level) throws SegmenterFormatterException{
        if (segmentSeen){
            seg.setFormattedSegmentString(formattedString.toString());
            addCurrentSegment();
            segmentSeen = false;
        }
        seg.setHardBoundaryLevel(level);
        // remember, the alignment program doesn't use the formatted segment string
        // it's just for display/debugging purposes.
        seg.setFormattedSegmentString(formatting);
        addCurrentSegment();
    }
    
    /** Write mid-segment formatting : typically these are just whitespace characters
     * that get written to the formattedSegmentString section of the Segment object.
     * @param midformatting The formatting that we're writing.
     *
     */
    public void writeMidSegmentFormatting(String midformatting) throws SegmenterFormatterException{
        formattedString.append(midformatting);
        if (segmentSeen) {
            seg.setFormattedSegmentString(formattedString.toString());
            addCurrentSegment();
            segmentSeen = false;
        }
    }
    
    
    /** This method writes formatting at a default level. It is not implemented for the
     * AlignmentHtmlSegmenterFormatter (results in a noop)
     * @param formatting The formatting to be written.
     *
     */
    public void writeFormatting(String formatting) throws SegmenterFormatterException{
        // not needed for alignment purposes
        ;
    }
    
    /** This writes the "pure" text of the segment to the Segment object.
     *
     * @param segment The text of the segment we're writing.
     */
    public void writeSegment(String segment, int wordcount ) throws SegmenterFormatterException {
        // in the case of two segments together, without mid-segment formatting
        // or blocktags in between, we should write a segment if we've seen one.
        if (segmentSeen){
            seg.setFormattedSegmentString(formattedString.toString());
            addCurrentSegment();
        }
        seg.setSegmentString(segment);
        formattedString.append(segment);
        seg.setHardBoundaryLevel(org.jvnet.olt.alignment.Segment.NOTBOUNDARY);
        segmentSeen = true;
    }
    
    /**
     * This is a no-op really for the AlignmentSegmenterFormatter : it merely
     * calls the two arg version of this method
     */
    public void writeSegment(String segment, String translation, int wordcount) throws SegmenterFormatterException {
       writeSegment(segment, wordcount); 
    }
    
    /** This is intended to write non Alignable text - things like &gt;script&lt; tags
     * and the like. It is not implemented for this class (since we don't want to align
     * that text !) and results in a noop.
     * @param text The non-alignable text to be written.
     */
    public void writeNonAlignable(String text) throws SegmenterFormatterException{
        // not needed for alignment purposes
        // System.out.println(text)
        ;
    }
    
    public void writeMessageId(String msgid) throws SegmenterFormatterException {
     // this is a no-op : not needed for this class
        ;
    }
    
    private void addCurrentSegment() throws SegmenterFormatterException {
        // calculate stats on the current segment
        try {
            // in some special cases, you can get a \n at the end of a Segment
            // usually this isn't anything to worry about but here, it will result
            // in a final piece of pcdata *if* there is a tag just before the /n
            // so we're removing that here.
            String text = seg.getSegmentString();//.trim();
            // only bother doing this if we've got a non-empty text segment
            if (!seg.isHardBoundary() || !text.equals("")) {
                java.io.StringReader reader = new java.io.StringReader(text);
                org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser parser = new  org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser(reader);
                parser.parse();
                HtmlStatsVisitor htmlStats = new HtmlStatsVisitor(language);
                parser.walkParseTree(htmlStats, null);
                seg.setWords(htmlStats.getWords());
                seg.setNumbers(htmlStats.getNumbers());
                seg.setFormatting(htmlStats.getTags());
            }
        } catch (java.lang.Exception e){ // Nasty.
            throw new SegmenterFormatterException("Caught Exception during addCurrentSegment " + e.getMessage() +
            "\nCurrent segment is " + seg.getFormattedSegmentString());
        }
        
        segmentList.add(seg);
        seg = new SegmentImpl();
        formattedString = new StringBuffer();
        
        
    }
    
    
    
    /** This returns a List of segments to the client - providing the main purpose of
     * this method.
     * @return A List of Segment objects.
     */
    public java.util.List getSegmentList() throws SegmenterFormatterException{
        return segmentList;
        
    }
    
    /** This method gets called by the HtmlSegmentFactoryVisitor on reaching the
     * end of the file - it's an indication that the formatted shuold clean up
     * and finish nicely. (Important if you're parsing html that doesn't have any
     * mid-segment whitespace, block tags : in fact, is just plaintext !
     */
    public void flush() throws SegmenterFormatterException{
        if (segmentSeen){
            seg.setFormattedSegmentString(formattedString.toString());
            addCurrentSegment();
        }
    }
    
    public void writeNote(String note) throws SegmenterFormatterException {
    } 
    
    public void writeContext(String context) throws SegmenterFormatterException {
    } 
    
}
