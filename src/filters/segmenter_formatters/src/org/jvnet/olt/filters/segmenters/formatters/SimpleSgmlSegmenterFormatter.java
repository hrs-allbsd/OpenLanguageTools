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
 * SimpleHtmlSegmentFormatter.java
 *
 * Created on July 2, 2002, 1:59 PM
 */

package org.jvnet.olt.filters.segmenters.formatters;

/**
 *
 * @author  timf
 */
public class SimpleSgmlSegmenterFormatter implements SegmenterFormatter {
    private java.util.List segments;
    private static final boolean DEBUG = true;
    private int total = 0;
    private StringBuffer outputBuf;
    /** Creates a new instance of SimpleHtmlSegmentFormatter */
    public SimpleSgmlSegmenterFormatter(boolean writeOutput) {
        this();
    }
    
    public SimpleSgmlSegmenterFormatter() {
        outputBuf = new StringBuffer();
        this.segments = new java.util.ArrayList();
    }
    
    public void writeFormatting(String formatting, int level) throws SegmenterFormatterException {
        if (DEBUG) {output("<!-- formatting -->");}
        output(formatting);
    }
    
    public void writeMidSegmentFormatting(String formatting) throws SegmenterFormatterException{
        if (DEBUG) {outputln("<!-- ws formatting -->");}
        output(formatting);
    }
    
    public void writeFormatting(String formatting) throws SegmenterFormatterException{
        if (DEBUG){outputln("<!-- formatting -->");}
        output(formatting);
    }
    
    public void writeSegment(String segment, int wordcount) throws SegmenterFormatterException{
        if (DEBUG) {outputln("<!-- seg -->");}
        String output = segment;
        output(output);
        //total+=wordcount;
        //output("Wordcount is " + wordcount);
        //output("Running total is " + total);
        segments.add(segment);
        //output("<!-- end seg -->\n");
    }
    
    public void writeSegment(String segment, String translation, int wordcount) throws SegmenterFormatterException{
        writeSegment(segment, wordcount);
        output("<!-- trans -->"+ translation+"<!-- end trans -->");
    }
    
    
    public void writeMessageId(String messageId) throws SegmenterFormatterException {
        outputln("<!-- messageid -->");
        output(messageId);
        outputln("<!-- end message id -->");
    }
    
    public void writeContext(String context) throws SegmenterFormatterException {
        outputln("<!-- context -->");
        output(context);
        outputln("<!-- end context -->");
    }
    
    public void writeNonAlignable(String text) throws SegmenterFormatterException{
        //outputln("<!-- non-alignable -->");
        output(text);
    }
    
    public void flush() throws SegmenterFormatterException{
        System.out.println(outputBuf.toString());
    }
    
    public java.util.List getSegments(){
        return segments;
    }
    
    private void output(String string){
        outputBuf.append(string);
    }
    
    private void outputln(String string){
        outputBuf.append(string+"\n");
    }
    
    public void writeNote(String note) throws SegmenterFormatterException {
        output(note);
    }
    
}
