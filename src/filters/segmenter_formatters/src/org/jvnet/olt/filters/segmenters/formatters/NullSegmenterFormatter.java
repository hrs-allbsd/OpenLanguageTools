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

/*
 * NullSegmenterFormatter.java
 *
 * Created on May 27, 2003, 11:53 AM
 */

package org.jvnet.olt.filters.segmenters.formatters;

/**
 *
 * @author  timf
 */
public class NullSegmenterFormatter implements SegmenterFormatter {
    
    /** Creates a new instance of NullSegmenterFormatter */
    public NullSegmenterFormatter() {
    }
    
    /** This method gets called upon reaching the end of file marker in the html parser.
     *
     * @throws SegmenterFormatterException if there was some problem encountered during the method.
     *
     *
     */
    public void flush() throws SegmenterFormatterException {
    }
    
    /** This method is used to write formatting that doesn't have a "hardness" level,
     * but is still considered formatting (for example the DOCTYPE tag)
     * @param formatting The formatting that is to be written
     * @throws SegmenterFormatterException if there was some problem writing the formatting.
     *
     *
     */
    public void writeFormatting(String formatting) throws SegmenterFormatterException {
    }
    
    /** This method writes formatting that is encountered. This is always "block-level"
     * formatting - such as &lt;p&gt; tags and the like.
     * @param formatting The formatting to be written
     * @param level The level of "hardness" to be written (really this is only important for
     * Segment Alignment programs)
     * @throws SegmenterFormatterException if there was some problem while writing the formatting.
     *
     *
     */
    public void writeFormatting(String formatting, int level) throws SegmenterFormatterException {
    }
    
    /** This method writes formatting (usually whitespace) that is encountered
     * in-between segments.
     * @param segment the formatting that is to be written
     * @throws SegmenterFormatterException if there was some problem writing the formatting
     *
     *
     */
    public void writeMidSegmentFormatting(String segment) throws SegmenterFormatterException {
    }
    
    /** This method writes text that is "non-alignable" - that is, text within &lt;script&gt;
     * tags, or &lt;style&gt; tags.
     * @param text The text that is to be written.
     * @throws SegmenterFormatterException if there was some problem writing out the text.
     *
     *
     */
    public void writeNonAlignable(String text) throws SegmenterFormatterException {
    }
    
    /** This method write the segment.
     * @param segment the segment to be written
     * @param wordcount the word count of the segment to be written.
     * @throws SegmenterFormatterException if there was some problem writing this segment.
     *
     */
    public void writeSegment(String segment, int wordcount) throws SegmenterFormatterException {
    }
    
    public void writeSegment(String segment, String translation, int wordcount) throws SegmenterFormatterException {    
    }
    
    
     /**
     * This allows us to write notes about segments that get written. Specifically
     * for xliff, we can write &lt;note&gt; elements...
     *
     * @param note the text of the note to write
     */
    public void writeNote(String note) throws SegmenterFormatterException {
    }
    
    public void writeMessageId(String msgid) throws SegmenterFormatterException {
     // this is a no-op : not needed for this class 
        ;
    }
    
    public void writeContext(String context) throws SegmenterFormatterException {
     // this is a no-op : not needed for this class 
        ;
    }
}
