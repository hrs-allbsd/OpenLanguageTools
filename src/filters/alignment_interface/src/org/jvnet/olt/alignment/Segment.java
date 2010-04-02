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

package org.jvnet.olt.alignment;

import java.util.List;

/**
 *  This is a prototype version of a segmenter class. It could do with
 *  more methods to return formatted strings, unformatted strings,
 *  string lengths, etc.
 */
public interface Segment {
    //  Hard boundary levels
    
    /**
     *  Not a boundary.
     */
    public static final int NOTBOUNDARY = 0;
    
    /**
     *  This represents a cue to the alignment tool, such as a break (&lt;br$gt;) or
     *  horizontal rule (&lt;hr$gt;). The alignment tool is free to ignore it as a
     *  boundary.
     */
    public static final int SOFTFLOW    = 1;  //  E.g., a <br> tag
    
    /**
     *  This represents a paragraph level boundary. The alignment tool should use it
     *  as an aid to alignment but isn't really forced to do so.
     */
    public static final int SOFT        = 2;  //  E.g., a <p> tag
    
    /**
     *  This represents a grouping of paragraphs that should normally be considered
     *  a hard boundary for alignment purposes. An example of this would be a list
     *  item in a list, which may contain a couple of paragraphs
     */
    public static final int HARD        = 3;
    
    /**
     *  This represents a hard boundary that is a very strong cue that this segment
     *  is a boundary to alignment. An example would be the start or end of a list,
     *  an example, or a quote.
     */
    public static final int HARDSUBSECTION = 4;
    
    /**
     *  This represents a hard boundary that should be regarded as an
     *  absolute boundary. The start of a new chapter in a book, etc., would
     *  come under this category.
     */
    public static final int HARDSECTION = 5;  //  E.g., a <sect1> or <sect2> tag
    
    /**
     *  This method returns true if the segment is a hard boundary
     *  segment. Otherwise it returns false.
     */
    public boolean isHardBoundary();
    
    /**
     *  This method describes how "hard" a hard boundary segment. It returns one of
     *  the values described above. If the method isHardBoundary() would return false
     *  for this segment then this method should return NOTBOUNDARY.
     *  @return int The boundary level described by one of the constants above.
     */
    public int getHardBoundaryLevel();
    
    /**
     *  This method returns the pure text of the segment, for alignment
     * purposes. There is also a method to get the formatted version of the
     * segment, for display purposes which is "getFormattedSegmentString"
     */
    public String getSegmentString();
    
    /**
     *  This method sets the text of the segment. Note that this is the text
     *  of the segment for alignment purposes. There is also a method to set
     *  the formatted version of this segment for display purposes which is 
     *  "setFormattedSegmentString"
     */
    public void setSegmentString(String segment);
    
    
    /**
     * This method gets the formatted version of the Segment
     * - that is, including any leading or trailing whitespace
     */
    public String getFormattedSegmentString();
    
    /**
     * this method sets the formatted version of the Segment
     * - that is, including leading or trailing whitespace
     */
    public void setFormattedSegmentString(String formattedString);
    
    /**
     *  This method gets the words contained in the segment.
     *  @return List A list of the words in the segment.
     */
    public List getWords();
    
    
    /**
     *  This method gets the non-words (e.g., "----" or "=====") contained in the segment.
     *  @return List A list of the words in the segment.
     */
    public List getNonWords();
    
    /**
     *  This method gets the inline format codes in the segment.
     *  @return List A list of the format codes (e.g., tags) in the segment.
     */
    public List getFormatting();
    
    /**
     *  This method gets the inline format codes in the segment. We're using
     *  a list of Strings since we often come across numbers like "2.5.1" which
     *  while being numbers, aren't allowed because of the many decimal points !
     *  @return An List of Strings representing the numbers in the segment.
     */
    public List getNumbers();
    
    
    // ...  Possibly more methods.
    
}
