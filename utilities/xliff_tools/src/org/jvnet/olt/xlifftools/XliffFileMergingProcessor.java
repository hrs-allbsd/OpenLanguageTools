
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffFileMergingProcessor.java
 *
 * Created on 14 July 2003, 15:23
 */

package org.jvnet.olt.xlifftools;

import org.jvnet.olt.xliffparser.XliffProcessor;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author  jc73554
 */
public class XliffFileMergingProcessor implements XliffProcessor {
    public static final boolean DEBUG = false;
    
    private boolean m_boolInHeader;
    
    private boolean m_boolWriteHeader;
    
    private Writer m_xliffOutput;
    
    private Stack  m_tagContext;
    
    private Stack  m_tagContextClone;
    
    private boolean m_boolInFooter;
    
    private static final String c_docTypeDecl = "<?xml version=\"1.0\"?>\n<!DOCTYPE xliff PUBLIC \"-//XLIFF//DTD XLIFF//EN\" \"http://www.oasis-open.org/committees/xliff/documents/xliff.dtd\" >";
    
    
    /** Creates a new instance of XliffFileMergingProcessor */
    public XliffFileMergingProcessor(Writer output, boolean writeHeader) {
        m_xliffOutput = output;
        m_boolWriteHeader = writeHeader;
        m_boolInHeader = true;
        
        m_tagContext = new Stack();
    }
    
    public void end_alt_trans() {
        
    }
    
    public void end_bin_source() {
    }
    
    public void end_bin_target() {
    }
    
    public void end_bin_unit() {
    }
    
    public void end_body() {
        m_boolInFooter = true;
        m_tagContextClone = (Stack) m_tagContext.clone();
    }
    
    public void end_bpt() {
    }
    
    public void end_context_group() {
    }
    
    public void end_count_group() {
    }
    
    public void end_ept() {
    }
    
    public void end_file() {
    }
    
    public void end_g() {
    }
    
    public void end_glossary() {
    }
    
    public void end_group() {
    }
    
    public void end_header() {
    }
    
    public void end_it() {
    }
    
    public void end_mrk() {
    }
    
    public void end_ph() {
    }
    
    public void end_phase() {
    }
    
    public void end_phase_group() {
    }
    
    public void end_prop_group() {
    }
    
    public void end_reference() {
    }
    
    public void end_skl() {
    }
    
    public void end_source() {
    }
    
    public void end_sub() {
    }
    
    public void end_target() {
    }
    
    public void end_trans_unit() {
    }
    
    public void end_xliff() {
    }
    
    public void handle_bpt(java.lang.String data, Attributes meta) {
    }
    
    public void handle_bx(Attributes meta) {
    }
    
    public void handle_context(java.lang.String data, Attributes meta) {
    }
    
    public void handle_count(java.lang.String data, Attributes meta) {
    }
    
    public void handle_ept(java.lang.String data, Attributes meta) {
    }
    
    public void handle_ex(Attributes meta) {
    }
    
    public void handle_external_file(Attributes meta) {
    }
    
    public void handle_g(java.lang.String data, Attributes meta) {
    }
    
    public void handle_internal_file(java.lang.String data, Attributes meta) {
    }
    
    public void handle_it(java.lang.String data, Attributes meta) {
    }
    
    public void handle_mrk(java.lang.String data, Attributes meta) {
    }
    
    public void handle_note(java.lang.String data, Attributes meta) {
    }
    
    public void handle_ph(java.lang.String data, Attributes meta) {
    }
    
    public void handle_prop(java.lang.String data, Attributes meta) {
    }
    
    public void handle_source(java.lang.String data, Attributes meta) {
    }
    
    public void handle_sub(java.lang.String data, Attributes meta) {
    }
    
    public void handle_target(java.lang.String data, Attributes meta) {
    }
    
    public void handle_x(Attributes meta) {
    }
    
    public void start_alt_trans(Attributes meta) {
    }
    
    public void start_bin_source(Attributes meta) {
    }
    
    public void start_bin_target(Attributes meta) {
    }
    
    public void start_bin_unit(Attributes meta) {
    }
    
    public void start_body(Attributes meta) {
    }
    
    public void start_bpt(Attributes meta) {
    }
    
    public void start_context_group(Attributes meta) {
    }
    
    public void start_count_group(Attributes meta) {
    }
    
    public void start_ept(Attributes meta) {
    }
    
    public void start_file(Attributes meta) {
    }
    
    public void start_g(Attributes meta) {
    }
    
    public void start_glossary(Attributes meta) {
    }
    
    public void start_group(Attributes meta) {
        m_boolInHeader = false;
    }
    
    public void start_header(Attributes meta) {
    }
    
    public void start_it(Attributes meta) {
    }
    
    public void start_mrk(Attributes meta) {
    }
    
    public void start_ph(Attributes meta) {
    }
    
    public void start_phase(Attributes meta) {
    }
    
    public void start_phase_group(Attributes meta) {
    }
    
    public void start_prop_group(Attributes meta) {
    }
    
    public void start_reference(Attributes meta) {
    }
    
    public void start_skl(Attributes meta) {
    }
    
    public void start_source(Attributes meta) {
    }
    
    public void start_sub(Attributes meta) {
    }
    
    public void start_target(Attributes meta) {
    }
    
    public void start_trans_unit(Attributes meta) {
        m_boolInHeader = false;
    }
    
    public void start_xliff(Attributes meta) {
    }
    
    public void characters(char[] values, int param, int param2) throws SAXException {
        // Wrap characters &, <, and >
        StringWriter stringWriter = new StringWriter();
        
        for(int i = param; i < (param + param2); i++) {
            switch((int)values[i]) {
                case (int) '&':
                    stringWriter.write("&amp;");
                    break;
                case (int) '<':
                    stringWriter.write("&lt;");
                    break;
                case (int) '>':
                    stringWriter.write("&gt;");
                    break;
                default:
                    stringWriter.write(values[i]);
                    break;
            }
        }
        writeText(stringWriter.toString());
        
    }
    
    public void endDocument() throws SAXException {
    }
    
    public void endElement(String str, String str1, String str2) throws SAXException {
        if(DEBUG) { System.out.println("end_" + str2 + "():"); }
        try {
            this.m_tagContext.pop();
            // System.out.println("Stack size after pop = " + m_tagContext.size());
        }
        catch(EmptyStackException emptyEx) {
            emptyEx.printStackTrace();
            throw new SAXException(emptyEx);
        }
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("</");
        buffer.append(str2);
        buffer.append(">");
        
        writeText(buffer.toString());
        
    }
    
    public void endPrefixMapping(String str) throws SAXException {
    }
    
    public void ignorableWhitespace(char[] values, int param, int param2) throws SAXException {
        writeText(values, param, param2);
    }
    
    public void processingInstruction(String str, String str1) throws SAXException {
    }
    
    public void setDocumentLocator(Locator locator) {
    }
    
    public void skippedEntity(String str) throws SAXException {
    }
    
    public void startDocument() throws SAXException {
        writeText(c_docTypeDecl);
    }
    
    public void startElement(String str, String str1, String str2, Attributes attributes) throws SAXException {
        if(DEBUG) { System.out.println("start_" + str2 + "():"); }
        //  push to stack
        m_tagContext.push(str2);
        //System.out.println("Stack size after push = " + m_tagContext.size());
        
        //  Process the text and write to certain area
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("<");
        buffer.append(str2);
        appendAttributes(buffer, attributes);
        buffer.append(">");
        
        writeText(buffer.toString());
        
    }
    
    public void startPrefixMapping(String str, String str1) throws SAXException {
    }
    
    protected void writeText(String text) throws SAXException {
        if((!m_boolInHeader || m_boolWriteHeader) && !m_boolInFooter) {
            try {
                //  Write to the output file.
                m_xliffOutput.write(text);
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
                throw new SAXException(ioEx);
            }
        }
    }
    protected void writeText(char[] values, int start, int length) throws SAXException {
        if((!m_boolInHeader || m_boolWriteHeader) && !m_boolInFooter) {
            try {
                //  Write to the output file instead.
                m_xliffOutput.write(values, start, length);
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
                throw new SAXException(ioEx);
            }
        }
    }
    
    /** This method takes an Attributes object, and writes the attribute name/value
     * pairs to a string buffer.
     * @param buffer The StringBuffer to write the attribute name/value pairs to.
     * @param attributes The Attributes object containing the tags attribute name/value pairs.
     */
    protected void appendAttributes(StringBuffer buffer, Attributes attributes) {
        //  Guard clause
        if((attributes == null) || (buffer == null))
        { return; }
        
        //  Iterate over the attributes and append each one to the string
        int numAttribs = attributes.getLength();
        for(int i = 0; i<numAttribs; i++) {
            buffer.append(" ");
            buffer.append(attributes.getQName(i));
            buffer.append("=\"");
            buffer.append(attributes.getValue(i));
            buffer.append("\"");
        }
    }
    
    public void closeOutDocument() throws IOException {
        try {
            while(!m_tagContextClone.empty()) {
                String tagName = (String) m_tagContextClone.pop();
                //  Do indentation stuff here, eventually.
                m_xliffOutput.write("</" + tagName + ">\n");
            }
            m_xliffOutput.flush();
            m_xliffOutput.close();
        }
        catch(EmptyStackException emptyEx) {
            emptyEx.printStackTrace();
            throw new IOException(emptyEx.getMessage());
        }
        
    }
}
