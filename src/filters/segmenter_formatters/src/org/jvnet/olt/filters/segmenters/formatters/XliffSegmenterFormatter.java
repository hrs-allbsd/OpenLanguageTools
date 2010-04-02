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
 * XliffSegmenterFormatter.java
 *
 * Created on July 8, 2002, 11:23 AM
 */

package org.jvnet.olt.filters.segmenters.formatters;


import org.jvnet.olt.filters.segmenters.formatters.XliffContextValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.jvnet.olt.format.*;
import org.jvnet.olt.format.GlobalVariableManager;

import org.jvnet.olt.io.*;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;

import java.io.Writer;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author  timf
 */
public class XliffSegmenterFormatter implements SegmenterFormatter {
    
    private Writer xliffWriter;
    private Writer sklWriter;
    private Map formatting;
    private StringBuffer formattingBuffer;
    private String note;
    private String language;
    private String dataType;
    private String msgid;
    private String context;
    private List    contextList;
    private String sourceFilename;
    
    // the translation that should be written out for this segment
    private String translation = null;
    
    private FormatWrapper wrapper;
    private int transUnitId=0;
    private GlobalVariableManager gvm;
    
    private HashMap m_hashFormatWrappers;
    private HashMap m_hashAvailableWrappers;
    
    
    private TagTable tagTable = null;
    private SegmenterTable segmenterTable = null;
    
    
    /** Creates a new instance of XliffSegmenterFormatter
     *
     *
     * @param dataType what sort of output we're writing (html, sgml, etc.)
     * @param language the source language
     * @param sourceFileName the name of the source file (used during backconversion)
     * @param xliffWriter a writer for the xliff file
     * @param sklWriter a writer for the skeleton file
     * @param gvm used when wrapping segments in it/bpt/ept tags
     * @param wrapper the format wrapper this segmenter formatter uses 
     */
    public XliffSegmenterFormatter(String dataType, String language, String sourceFileName, Writer xliffWriter, Writer sklWriter,
    GlobalVariableManager gvm, FormatWrapper wrapper) throws SegmenterFormatterException {
        try {
            this.xliffWriter = xliffWriter;
            this.sklWriter = sklWriter;
            this.formatting = new HashMap();
            this.formattingBuffer = new StringBuffer();
            this.language = language;
            this.gvm = gvm;
            this.dataType = dataType;
            this.note = "";
            this.msgid= "";
            this.context= "";
            this.wrapper = wrapper;
            // This hash maps from the XLIFF file type, to the correct
            // formatter used for that type
            /* WRONG
             *
            m_hashFormatWrappers = new HashMap();
            m_hashAvailableWrappers = new HashMap();
            m_hashAvailableWrappers.put("HTML", new Integer(HTML));
            m_hashAvailableWrappers.put("SGML", new Integer(SGML));
            m_hashAvailableWrappers.put("XML", new Integer(XML));
            m_hashAvailableWrappers.put("PLAINTEXT", new Integer(PLAINTEXT));
            m_hashAvailableWrappers.put("MSG",new Integer(MSG));
            m_hashAvailableWrappers.put("PO", new Integer(PO));
            m_hashAvailableWrappers.put("JAVA", new Integer(JAVA));
            m_hashAvailableWrappers.put("PROPERTIES",new Integer(PROPERTIES));
            m_hashAvailableWrappers.put("JSP", new Integer(JSP));
            */
             
            /* aren't being used anymore -- see notes under printXLIFFHeader
             * these were being used to get just the filename portion of
             * the absolute paths...
             * File tmp = new File (xliffFileName);
             * xliffFileName = tmp.getName();
             * tmp = new File (sklFileName);
             * sklFileName = tmp.getName();
             */
            printXLIFFHeader(language, sourceFileName);
            this.sourceFilename = sourceFileName;
        }  catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem while writing segment " + e.getMessage());
        }
    }
    
    public XliffSegmenterFormatter(String dataType, String language, String sourceFileName,
    Writer xliffWriter, Writer sklWriter, FormatWrapper wrapper) throws SegmenterFormatterException {
        this(dataType, language, sourceFileName, xliffWriter,sklWriter,null, wrapper);
    }
    
    public void setTagTable(TagTable tagTable){
        this.tagTable = tagTable;
    }
    
    public void setSegmenterTable(SegmenterTable segmenterTable){
        this.segmenterTable = segmenterTable;
    }
    
    
    /** This method gets called upon reaching the end of file marker in the html parser.
     *
     * @throws SegmenterFormatterException if there was some problem encountered during the method.
     *
     */
    public void flush() throws SegmenterFormatterException {
        try {
            xliffWriter.write("</body>\n"+
            "</file>\n"+
            "</xliff>\n");
            // empty any remaining formatting
            if (formattingBuffer.length() > 0){
                sklWriter.write("<formatting>"+formattingBuffer.toString()+
                "</formatting>\n");
                formattingBuffer = new StringBuffer();
            }
            sklWriter.write("</body>\n"+
            "</file>\n"+
            "</tt-xliff-skl>\n");
            xliffWriter.flush();
            sklWriter.flush();
        }  catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem while writing segment " + e.getMessage());
        }
    }
    
    
    /** This method is used to write formatting that doesn't have a "hardness" level,
     * but is still considered formatting (for example the DOCTYPE tag)
     * @param formatting The formatting that is to be written
     * @throws SegmenterFormatterException if there was some problem writing the formatting.
     *
     */
    public void writeFormatting(String formatting) throws SegmenterFormatterException {
        if (formatting.length() > 0){
            try {
                formattingBuffer.append(wrapXMLChars(formatting));
            }  catch (java.io.IOException e){
                throw new SegmenterFormatterException("Problem while writing formatting " + e.getMessage());
            }
        }
    }
    
    /** This method writes formatting that is encountered. This is always "block-level"
     * formatting - such as &lt;p&gt; tags and the like.
     * @param formatting The formatting to be written
     * @param level The level of "hardness" to be written (really this is only important for
     * Segment Alignment programs)
     * @throws SegmenterFormatterException if there was some problem while writing the formatting.
     *
     */
    public void writeFormatting(String formatting, int level) throws SegmenterFormatterException {
        if (formatting.length() > 0){
            try {
                formattingBuffer.append(wrapXMLChars(formatting));
            }  catch (java.io.IOException e){
                throw new SegmenterFormatterException("Problem while writing segment " + e.getMessage());
            }
        }
    }
    
    /** This method writes formatting (usually whitespace) that is encountered
     * in-between segments.
     * @param segment the formatting that is to be written
     * @throws SegmenterFormatterException if there was some problem writing the formatting
     *
     */
    public void writeMidSegmentFormatting(String segment) throws SegmenterFormatterException {
        try {
            // not 100% sure what to do with this.
            sklWriter.write("<formatting type=\"whitespace\">"+wrapXMLChars(segment)+
            "</formatting>\n");
        }  catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem while writing segment " + e.getMessage());
        }
        
    }
    
    /** This method writes text that is "non-alignable" - that is, text within &lt;script&gt;
     * tags, or &lt;style&gt; tags.
     * @param text The text that is to be written.
     * @throws SegmenterFormatterException if there was some problem writing out the text.
     *
     */
    public void writeNonAlignable(String text) throws SegmenterFormatterException {
        if (text.length() > 0){
            try {
                formattingBuffer.append(wrapXMLChars(text));
            }  catch (java.io.IOException e){
                throw new SegmenterFormatterException("Problem while writing non alignable text " + e.getMessage());
            }
        }
    }
    
    /** This method write the segment.
     * @param segment the segment to be written
     * @throws SegmenterFormatterException if there was some problem writing this segment.
     */
    public void writeSegment(String segment, int wordcount) throws SegmenterFormatterException {
        try {
            // write out any buffered formatting
            //System.out.println("segment** _"+segment+"_");
            //System.out.println("Wrapped** _"+wrapTagsAndXMLChars(segment)+"_");
            if (formattingBuffer.length() > 0){
                sklWriter.write("<formatting>"+formattingBuffer.toString()+
                "</formatting>\n");
                formattingBuffer = new StringBuffer();
            }
            
            transUnitId++;
            sklWriter.write("<tu-placeholder id=\"a"+transUnitId+"\"/>");
            xliffWriter.write("<trans-unit id=\"a"+transUnitId+"\">    \n"+
            "       <source>"+wrapTagsAndXMLChars(segment)+"</source>\n");
            
            if (this.translation != null){ // note, this probably needs to be revised
                // no way to set translation state at the moment - we're using
                // a user state for now.
                xliffWriter.write("       <target state=\"user\">"
                        +wrapTagsAndXMLChars(this.translation)+"</target>\n");
            }
            
            if (note.length() != 0){
                xliffWriter.write("       <note>"+this.note+"</note>\n");
                this.note="";
            }
            
            
            xliffWriter.write("       <count-group name=\"word count\">\n"+
            "          <count count-type=\"word count\" unit=\"word\">"+wordcount+"</count>\n"+
            "       </count-group>\n");
            if (msgid.length() != 0){
                // when we move to xliff 1.1 we should have the attribute
                // purpose="match" in the context-group element
                xliffWriter.write("      <context-group name=\"message id\">\n"+
                // for xliff 1.1 we need to have type="record" in here - for now, we use context-type
                "          <context context-type=\"record\">"+msgid+"</context>\n"+
                "      </context-group>\n");
                this.msgid="";
            }
            if(contextList!=null && contextList.size()>0) {
                xliffWriter.write("     <context-group name=\"context info\">\n");
                Iterator it = contextList.iterator();
                while(it.hasNext()) {
                    XliffContextValue context = (XliffContextValue)it.next();
                    xliffWriter.write("          <context context-type=\"" + context.getContextType() + "\">" + context.getContextValue() + "</context>\n");
                }
                xliffWriter.write("      </context-group>\n");
                contextList = null;
            }
            xliffWriter.write("    </trans-unit>\n");
        } catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem while writing segment " + e.getMessage());
        }
        
    }
    
    public void writeSegment(String segment, String translation, int wordcount) throws SegmenterFormatterException {
        this.translation = translation;
        writeSegment(segment, wordcount);
        this.translation = null;
    } 
    
    private void printXLIFFHeader(String srclang, String filename) throws java.io.IOException {
        // actually xliff filename and skl filename aren't being used anymore - it alwyas
        // writes "content.xlf" and "skeleton.skl" as the file names
        xliffWriter.write("<?xml version=\"1.0\" ?>\n"+
        "<!DOCTYPE xliff PUBLIC \"-//XLIFF//DTD XLIFF//EN\"\n"+
        " \"http://www.oasis-open.org/committees/xliff/documents/xliff.dtd\" >\n"+
        "<xliff version=\"1.0\">\n"+
        " <file source-language=\""+wrapXMLChars(srclang)+
        "\" datatype=\""+this.dataType+"\" original=\""+wrapXMLChars(filename)+"\">\n"+
        "  <header><skl>\n"+
        "<external-file href=\"skeleton.skl\" />"+
        "</skl></header>\n"+
        "  <body>\n");
        
        sklWriter.write("<?xml version=\"1.0\" ?>\n"+
        "<!DOCTYPE tt-xliff-skl PUBLIC \"-//SUN-TRANSTECH//DTD XLIFF SKELETON//EN\"\n"+
        " \"http://www.ireland/~timf/tt-xliff-skl.dtd\" >\n"+
        "<tt-xliff-skl version=\"1.0\">\n"+
        " <file source-language=\""+wrapXMLChars(srclang)+
        "\" datatype=\"html\" original=\""+wrapXMLChars(filename)+"\">\n"+
        "  <header><xliff-file href=\"content.xlf\"/></header>\n"+
        "  <body>\n");
        
    }
    
    /**
     * This method takes in characters in the string, and w converts them
     * by trying to pass them through John's XLIFF formatting wrapper (which puts tags inside
     * bpt ept tags correctly. Should this fail for any reason, we default to passing
     * them through JohnC's HTMLEscapeFilterReader. What this does, is
     * convert ampersands, less-than and greater-than characters to an SGML/XML friendly
     * format using the &amp;amp; &amp;lt; and &amp;gt; entities -- not as useful to
     * XLIFF-aware editors, but at least it will always produce valid XML.
     */
    private String wrapTagsAndXMLChars(String string) throws java.io.IOException {
        String output = string;
        try {
            // next, based on the type, we can get a FormatWrapper...
            // wrap the formatting (including any "dontsegment" protected text)
            output = wrapFormatting(output, true);
        } catch (Throwable e){ // in some cases, we can't remove segmentation protection
            e.printStackTrace();
            // since we're not getting valid sgml at all in the first place
            System.err.println("Unable to convert " + string +" to an xliff representation since it's an unsupported format : " + e.getMessage());
            // we still have to put it in an "XML-safe" representation, which all the other
            // format wrappers would have done for us
            output = wrapXMLChars(string);
        }
        return output;
    }
    
    
    /**
     * Do simple XML character wrapping, rather than try to wrap tags
     */
    private String wrapXMLChars(String string) throws java.io.IOException {
        EntityConversionFilterReader conv = new EntityConversionFilterReader(new StringReader(string));
        conv.setEntityMap(ASCIIControlCodeMapFactory.getAsciiControlCodesMap());
        StringWriter writer= new StringWriter();
        int i;
        while ((i = conv.read()) != -1){
            writer.write(i);
        }
        return writer.toString();
    }
    
    // FIXME
    /** The below code is stolen from the TM tool's XliffOutputGenerator - we
     *  should at some point move that code into the xliff utilities I think.
     *  Unfortunately, the tm tool code depends on this code in the filters, so we
     *  can't build that first, and simply use the methods from that class (it
     *  won't have been built yet) - so I'm copying & pasting that code : I know
     *  this is wrong and I apologise for it !
     */
    protected String wrapFormatting(String text , boolean sourceText) throws UnsupportedFormatException, InvalidFormattingException {
        if(text == null) { return ""; }
        
        String wrapped = "";
        try{
            wrapped = this.wrapper.wrapFormatting(text);
        } catch (Throwable e){
            e.printStackTrace();
            // some bad formatting was found (we catch a Throwable here
            // because the blaML formats all use a javacc parser, which
            // throws Errors on getting a lexical err.
            throw new InvalidFormattingException("The formatting for segment _" + text +"_ is invalid");
        }
        //  Return the wrapped string.
        return wrapped;
    }
    
    
    /**
     * This allows us to write notes about segments that get written. Specifically
     * for xliff, we can write &lt;note&gt; elements...
     *
     * @param note the text of the note to write
     */
    public void writeNote(String note) throws SegmenterFormatterException {
        try {
            this.note=wrapXMLChars(note);
        }catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem wrapping " + note +" "+e.getMessage());
        }
    }
    /** This method gets called when writing a message id to the xliff file. Typically
     *  this would be a no-op for segmenter formaters that write documentation files
     *  to xliff, but we're putting this in for the software formats that definitely
     *  do need them.
     *
     *  Note that the message id *must* be written as formatting as well
     *  if it's required to be in the output file - the backconverter does not look
     *  for msgid elements in context-groups in order to back convert the file.
     *
     *  @param msgid the text of the message id
     *  @throws SegmenterFormatterException if there was some problem writing the msg id
     */
    public void writeMessageId(String msgid) throws SegmenterFormatterException {
        try {
            this.msgid=wrapXMLChars(msgid);
        } catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem wrapping " + msgid +" "+e.getMessage());
        }
    }
    
    public void writeContext(String context) throws SegmenterFormatterException {
        try {
            if(contextList == null) {
                contextList = Collections.synchronizedList(new ArrayList());
            }
            contextList.add(new XliffContextValue("record",wrapXMLChars(context)));
        } catch (java.io.IOException e){
            throw new SegmenterFormatterException("Problem wrapping "+ context+ " "+e.getMessage());
        }
    }
    
    public void writeContext(List contextToAdd) throws SegmenterFormatterException {
        if(contextList == null) {
            contextList = Collections.synchronizedList(new ArrayList());
        }
        Iterator it = contextToAdd.iterator();
        while(it.hasNext()) {
            XliffContextValue context = (XliffContextValue)it.next();
            try {
                contextList.add(new XliffContextValue(context.getContextType(),wrapXMLChars(context.getContextValue())));
            } catch(java.io.IOException e) {
                throw new SegmenterFormatterException("Problem wrapping "+ context+ " "+e.getMessage());
            }
        }
    }
    
}
