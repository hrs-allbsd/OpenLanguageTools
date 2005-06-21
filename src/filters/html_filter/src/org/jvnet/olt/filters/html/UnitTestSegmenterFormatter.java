
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SimpleHtmlSegmentFormatter.java
 *
 * Created on July 2, 2002, 1:59 PM
 */

package org.jvnet.olt.filters.html;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;

/**
 *
 * @author  timf
 */
public class UnitTestSegmenterFormatter implements SegmenterFormatter {
    StringBuffer buf;
    
    /** Creates a new instance of SimpleHtmlSegmentFormatter */
    public UnitTestSegmenterFormatter() {
        this.buf = new StringBuffer();
    }
    
    public void writeFormatting(String formatting, int level) throws SegmenterFormatterException {
        buf.append(formatting);
    }
    
    public void writeMidSegmentFormatting(String formatting) throws SegmenterFormatterException{
        buf.append("_"+formatting+"_");
    }
    
    public void writeFormatting(String formatting) throws SegmenterFormatterException{
        buf.append(formatting);
    }
    
    public void writeSegment(String segment, int wordcount) throws SegmenterFormatterException{
        buf.append("<!-- seg -->");
        buf.append(segment);
        buf.append("<!-- end seg -->");
    }
    
    /**
     * This is a no-op for this class : it merely calls the two-arg version of
     * this method
     */
    public void writeSegment(String segment, String translation, int wordcount) throws SegmenterFormatterException{
       writeSegment(segment, wordcount);
    }
    
    
    public void writeNonAlignable(String text) throws SegmenterFormatterException{
        buf.append(text);
    }

    public void writeMessageId(String msgid) throws SegmenterFormatterException {
     // this is a no-op : not needed for this class 
        ;
    }
    
    public void writeNote(String note) throws SegmenterFormatterException {
    }
    
    public void writeContext(String context) throws SegmenterFormatterException {
    }
    
    public void flush() throws SegmenterFormatterException{
        ;
    }
    
    public String getString(){
        return buf.toString();
    }
    
}
