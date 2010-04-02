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
 * File:           XliffParser.java
 * Date:           August 8, 2002  1:46 PM
 *
 * @author  kb128066
 * @version generated by NetBeans XML module
 */
package org.jvnet.olt.xliff_back_converter;

import org.xml.sax.*;
import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.logging.*;

/**
 * The class reads XML documents according to specified DTD and translates
 * all related events into XliffHandler events. 
 *
 *
 * @author Brian Kidney
 * @version August 8, 2002
 */
public class XliffParser implements ContentHandler {

    public static final int CNST_XLIFF = 0;
    public static final int CNST_FILE = 1;
    public static final int CNST_HEADER = 2;
    public static final int CNST_SKL = 3;
    public static final int CNST_INTERNAL_FILE = 4;
    public static final int CNST_EXTERNAL_FILE = 5;
    public static final int CNST_GLOSSARY = 6;
    public static final int CNST_REFERENCE = 7;
    public static final int CNST_NOTE = 8;
    public static final int CNST_PROP_GROUP = 9;
    public static final int CNST_PROP = 10;
    public static final int CNST_CONTEXT_GROUP = 11;
    public static final int CNST_CONTEXT = 12;
    public static final int CNST_PHASE_GROUP = 13;
    public static final int CNST_PHASE = 14;
    public static final int CNST_COUNT_GROUP = 15;
    public static final int CNST_COUNT = 16;
    public static final int CNST_BODY = 17;
    public static final int CNST_GROUP = 18;
    public static final int CNST_TRANS_UNIT = 19;
    public static final int CNST_SOURCE = 20;
    public static final int CNST_TARGET = 21;
    public static final int CNST_ALT_TRANS = 22;
    public static final int CNST_BIN_UNIT = 23;
    public static final int CNST_BIN_SOURCE = 24;
    public static final int CNST_BIN_TARGET = 25;
    public static final int CNST_G = 26;
    public static final int CNST_X = 27;
    public static final int CNST_BX = 28;
    public static final int CNST_EX = 29;
    public static final int CNST_PH = 30;
    public static final int CNST_BPT = 31;
    public static final int CNST_EPT = 32;
    public static final int CNST_IT = 33;
    public static final int CNST_MRK = 34;
    public static final int CNST_SUB = 35;
    
    private java.lang.StringBuffer buffer;

    private XliffHandler handler;

    private java.util.Stack context;

    private EntityResolver resolver;

    /* Properties required by the Back Converter */
    private BackConverterProperties props;

    /* Logging object */
    private static Logger logger;
    
    private static Reader xliffDTD;

    private HashMap tagMap;

    /**
     * Constructor for the XliffParser object
     *
     * @param handler The Xliff Handler
     * @param resolver The Entity Resolver
     * @param theLogger The logging object (For logging messages).
     * @param theProps Properties required by the Back Converter.
     * @param theXliffDTD The Xliff DTD.
     */
    public XliffParser(final XliffHandler handler, final EntityResolver
                             resolver, Logger theLogger, 
                             BackConverterProperties theProps, 
                             Reader theXliffDTD) {

        this.logger = theLogger;
        this.props = theProps;
        this.handler = handler;
        this.resolver = resolver;
        this.xliffDTD = theXliffDTD;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
        
        setUpMap();
        
    }

    /**
     * Creates a Xliff parser instance.
     *
     * @param handler handler interface implementation (never 
     *        <code>null</code>
     * @param resolver SAX entity resolver implementation or 
     *        <code>null</code>. It is recommended that it could be able to 
     *        resolve at least the DTD.
     */
    public XliffParser(final XliffHandler handler, final EntityResolver
                             resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
        this.props = null;
        this.logger = null;
        
        setUpMap();
    } 

    private void setUpMap() {
        
        tagMap = new HashMap(36);
        tagMap.put("xliff", new Integer(CNST_XLIFF) );
        tagMap.put("file", new Integer(CNST_FILE) );
        tagMap.put("header", new Integer(CNST_HEADER) );
        tagMap.put("skl", new Integer(CNST_SKL) );
        tagMap.put("internal-file", new Integer(CNST_INTERNAL_FILE) );
        tagMap.put("external-file", new Integer(CNST_EXTERNAL_FILE) );
        tagMap.put("glossary", new Integer(CNST_GLOSSARY) );
        tagMap.put("reference", new Integer(CNST_REFERENCE) );
        tagMap.put("note", new Integer(CNST_NOTE) );
        tagMap.put("prop-group", new Integer(CNST_PROP_GROUP) );
        tagMap.put("prop", new Integer(CNST_PROP) );
        tagMap.put("context-group", new Integer(CNST_CONTEXT_GROUP) );
        tagMap.put("context", new Integer(CNST_CONTEXT) );
        tagMap.put("phase-group", new Integer(CNST_PHASE_GROUP) );
        tagMap.put("phase", new Integer(CNST_PHASE) );
        tagMap.put("count-group", new Integer(CNST_COUNT_GROUP) );
        tagMap.put("count", new Integer(CNST_COUNT) );
        tagMap.put("body", new Integer(CNST_BODY) );
        tagMap.put("group", new Integer(CNST_GROUP) );
        tagMap.put("trans-unit", new Integer(CNST_TRANS_UNIT) );
        tagMap.put("source", new Integer(CNST_SOURCE) );
        tagMap.put("target", new Integer(CNST_TARGET) );
        tagMap.put("alt-trans", new Integer(CNST_ALT_TRANS) );
        tagMap.put("bin-unit", new Integer(CNST_BIN_UNIT) );
        tagMap.put("bin-source", new Integer(CNST_BIN_SOURCE) );
        tagMap.put("bin-target", new Integer(CNST_BIN_TARGET) );
        tagMap.put("g", new Integer(CNST_G) );
        tagMap.put("x", new Integer(CNST_X) );
        tagMap.put("bx", new Integer(CNST_BX) );
        tagMap.put("ex", new Integer(CNST_EX) );
        tagMap.put("ph", new Integer(CNST_PH) );
        tagMap.put("bpt", new Integer(CNST_BPT) );
        tagMap.put("ept", new Integer(CNST_EPT) );
        tagMap.put("it", new Integer(CNST_IT) );
        tagMap.put("mrk", new Integer(CNST_MRK) );
    }

    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param locator The new documentLocator value
     */
    public final void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @exception SAXException  Any SAXException
     */
    public final void startDocument() throws SAXException {
        handler.startDocument();
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @exception SAXException  Any SAXException
     */
    public final void endDocument() throws SAXException {
        handler.endDocument();
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param ns The Namespace URI, or the empty string if the element has 
     * no Namespace URI or if Namespace processing is not being performed.
     * @param name The local name (without prefix), or the empty 
     * string if Namespace processing is not being performed.
     * @param qname The qualified name (with prefix), or the empty string if 
     * qualified names are not available.
     * @param attrs The attributes attached to the element. If there are no 
     * attributes, it shall be an empty Attributes object.
     * @exception SAXException  Any SAXException
     */
    public final void startElement(java.lang.String ns, java.lang.String 
    name, java.lang.String qname, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[]{qname, new 
            org.xml.sax.helpers.AttributesImpl(attrs)});
        
        handler.startElement(ns, name, qname, attrs);
        
        int iType = ((Integer) tagMap.get(qname)).intValue();
        
        switch(iType)
        {
            case CNST_XLIFF:
                handler.start_xliff(attrs);
                break;
            case CNST_FILE:
                handler.start_file(attrs);
                break;
            case CNST_HEADER:
                handler.start_header(attrs);
                break;
            case CNST_SKL:
                handler.start_skl(attrs);
                break;
            case CNST_INTERNAL_FILE:
                break;
            case CNST_EXTERNAL_FILE:
                break;
            case CNST_GLOSSARY:
                handler.start_glossary(attrs);
                break;
            case CNST_REFERENCE:
                handler.start_reference(attrs);
                break;
            case CNST_NOTE:
                break;
            case CNST_PROP_GROUP:
                handler.start_prop_group(attrs);
                break;
            case CNST_PROP:
                break;
            case CNST_CONTEXT_GROUP:
                handler.start_context_group(attrs);
                break;
            case CNST_CONTEXT:
                break;
            case CNST_PHASE_GROUP:
                handler.start_phase_group(attrs);
                break;
            case CNST_PHASE:
                handler.start_phase(attrs);
                break;
            case CNST_COUNT_GROUP:
                handler.start_count_group(attrs);
                break;
            case CNST_COUNT:
                break;
            case CNST_BODY:
                handler.start_body(attrs);
                break;
            case CNST_GROUP:
                handler.start_group(attrs);
                break;
            case CNST_TRANS_UNIT:
                handler.start_trans_unit(attrs);
                break;
            case CNST_SOURCE:
                handler.start_source(attrs);
                break;
            case CNST_TARGET:
                handler.start_target(attrs);
                break;
            case CNST_ALT_TRANS:
                handler.start_alt_trans(attrs);
                break;
            case CNST_BIN_UNIT:
                handler.start_bin_unit(attrs);
                break;
            case CNST_BIN_SOURCE:
                handler.start_bin_source(attrs);
                break;
            case CNST_BIN_TARGET:
                handler.start_bin_target(attrs);
                break;
            case CNST_G:
                handler.start_g(attrs);
                break;
            case CNST_X:
                break;
            case CNST_BX:
                break;
            case CNST_EX:
                break;
            case CNST_PH:
                handler.start_ph(attrs);
                break;
            case CNST_BPT:
                handler.start_bpt(attrs);
                break;
            case CNST_EPT:
                handler.start_ept(attrs);
                break;
            case CNST_IT:
                handler.start_it(attrs);
                break;
            case CNST_MRK:
                handler.start_mrk(attrs);
                break;
            case CNST_SUB:
                handler.start_sub(attrs);
                break;
        }
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param ns The Namespace URI, or the empty string if the element has 
     * no Namespace URI or if Namespace processing is not being performed.
     * @param name The local name (without prefix), or the empty 
     * string if Namespace processing is not being performed.
     * @param qname The qualified name (with prefix), or the empty string if 
     * qualified names are not available.
     * @exception SAXException  Any SAXException
     */
    public final void endElement(java.lang.String ns, java.lang.String name, 
    java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();
        
        int iType = ((Integer) tagMap.get(qname)).intValue();
        handler.endElement(ns, name, qname);
        
        switch(iType)
        {
            case CNST_XLIFF:
                handler.end_xliff();
                break;
            case CNST_FILE:
                handler.end_file();
                break;
            case CNST_HEADER:
                handler.end_header();
                break;
            case CNST_SKL:
                handler.end_skl();
                break;
            case CNST_INTERNAL_FILE:
                break;
            case CNST_EXTERNAL_FILE:
                break;
            case CNST_GLOSSARY:
                handler.end_glossary();
                break;
            case CNST_REFERENCE:
                handler.end_reference();
                break;
            case CNST_NOTE:
                break;
            case CNST_PROP_GROUP:
                handler.end_prop_group();
                break;
            case CNST_PROP:
                break;
            case CNST_CONTEXT_GROUP:
                handler.end_context_group();
                break;
            case CNST_CONTEXT:
                break;
            case CNST_PHASE_GROUP:
                handler.end_phase_group();
                break;
            case CNST_PHASE:
                handler.end_phase();
                break;
            case CNST_COUNT_GROUP:
                handler.end_count_group();
                break;
            case CNST_COUNT:
                break;
            case CNST_BODY:
                handler.end_body();
                break;
            case CNST_GROUP:
                handler.end_group();
                break;
            case CNST_TRANS_UNIT:
                handler.end_trans_unit();
                break;
            case CNST_SOURCE:
                handler.end_source();
                break;
            case CNST_TARGET:
                handler.end_target();
                break;
            case CNST_ALT_TRANS:
                handler.end_alt_trans();
                break;
            case CNST_BIN_UNIT:
                handler.end_bin_unit();
                break;
            case CNST_BIN_SOURCE:
                handler.end_bin_source();
                break;
            case CNST_BIN_TARGET:
                handler.end_bin_target();
                break;
            case CNST_G:
                handler.end_g();
                break;
            case CNST_X:
                break;
            case CNST_BX:
                break;
            case CNST_EX:
                break;
            case CNST_PH:
                handler.end_ph();
                break;
            case CNST_BPT:
                handler.end_bpt();
                break;
            case CNST_EPT:
                handler.end_ept();
                break;
            case CNST_IT:
                handler.end_it();
                break;
            case CNST_MRK:
                handler.end_mrk();
                break;
            case CNST_SUB:
                handler.end_sub();
                break;      
        }                    
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param chars The characters from the XML document.
     * @param start The start position in the array.
     * @param len The number of characters to read from the array.
     * @exception SAXException  Any SAXException
     */
    public final void characters(char[] chars, int start, int len) 
        throws SAXException {
        buffer.append(chars, start, len);
        handler.characters(chars, start, len);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param chars The characters from the XML document.
     * @param start The start position in the array.
     * @param len The number of characters to read from the array.
     * @exception SAXException  Any SAXException
     */
    public final void ignorableWhitespace(char[] chars, int start, int len) 
        throws SAXException {
        handler.ignorableWhitespace(chars, start, len);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param target The processing instruction target.
     * @param data The processing instruction data, or null if none was 
     * supplied. The data does not include any whitespace separating it from 
     * the target.
     * @exception SAXException  Any SAXException
     */
    public final void processingInstruction(java.lang.String target, 
        java.lang.String data) throws SAXException {
        handler.processingInstruction(target, data);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param prefix The Namespace prefix being declared.
     * @param uri The Namespace URI the prefix is mapped to.
     * @exception SAXException  Any SAXException
     */
    public final void startPrefixMapping(final java.lang.String prefix, 
        final java.lang.String uri) throws SAXException {
        handler.startPrefixMapping(prefix, uri);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param prefix The prefix that was being mapping.
     * @exception SAXException  Any SAXException
     */
    public final void endPrefixMapping(final java.lang.String prefix) 
        throws SAXException {
        handler.endPrefixMapping(prefix);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param name The name of the skipped entity. If it is a parameter 
     * entity, the name will begin with '%', and if it is the external DTD 
     * subset, it will be the string "[dtd]".
     * @exception SAXException  Any SAXException
     */
    public final void skippedEntity(java.lang.String name) 
        throws SAXException {
        handler.skippedEntity(name);
    }


    /**
     * Dispatch method
     *
     * @param fireOnlyIfMixed Boolean
     * @exception SAXException  Any SAXException
     */
    private void dispatch(final boolean fireOnlyIfMixed) 
        throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) {
            return;
        }//skip it

        Object[] ctx = (Object[]) context.peek();
        String here = (String) ctx[0];
        Attributes attrs = (Attributes) ctx[1];
        
        int iType = ((Integer) tagMap.get(here)).intValue();
        
        switch(iType)
        {
            case CNST_SOURCE:
                handler.handle_source(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_TARGET:
                handler.handle_target(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_G:
                handler.handle_g(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_PH:
                handler.handle_ph(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_BPT:
                handler.handle_bpt(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_EPT:
                handler.handle_ept(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_IT:
                handler.handle_it(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_MRK:
                handler.handle_mrk(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_SUB:
                handler.handle_sub(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_INTERNAL_FILE:
                handler.handle_internal_file(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_NOTE:
                handler.handle_note(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_PROP:
                handler.handle_prop(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_CONTEXT:
                handler.handle_context(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_COUNT:
                handler.handle_count(buffer.length() == 0 ? null : buffer.toString(), attrs);
                break;
            case CNST_EXTERNAL_FILE:
                handler.handle_external_file(attrs);
                break;
            case CNST_X:
                handler.handle_x(attrs);
                break;
            case CNST_BX:
                handler.handle_bx(attrs);
                break;
            case CNST_EX:
                handler.handle_ex(attrs);
                break;
        }
        buffer.delete(0, buffer.length());
    }


    /**
     * The recognizer entry method taking an InputSource.
     *
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a
     *      DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     *      satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the
     *      implementation can not be instantiated.
     */
    public void parse(final InputSource input) throws SAXException, 
        javax.xml.parsers.ParserConfigurationException, 
        java.io.IOException {
        parse(input, this);
    }


    /**
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a
     *      DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     *      satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the
     *      implementation can not be instantiated.
     */
    public void parse(final java.net.URL url) throws SAXException, 
        javax.xml.parsers.ParserConfigurationException, 
        java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }


    /**
     * The recognizer entry method taking an Inputsource.
     *
     * @param input InputSource to be parsed.
     * @param handler Description of the Parameter
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a
     *      DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     *      satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the
     *      implementation can not be instantiated.
     */
    public static void parse(final InputSource input, 
        final XliffHandler handler) throws SAXException, 
        javax.xml.parsers.ParserConfigurationException, 
        java.io.IOException {
        parse(input, new XliffParser(handler, null));
    }


    /**
     * The recognizer entry method taking a URL.
     *
     * @param url URL source to be parsed.
     * @param handler Description of the Parameter
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a
     *      DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser
     *      satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the
     *      implementation can not be instantiated.
     */
    public static void parse(final java.net.URL url, 
        final XliffHandler handler) throws SAXException, 
        javax.xml.parsers.ParserConfigurationException, 
        java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }


    /**
     * Parses the incoming Xliff file
     *
     * @param input Description of the Parameter
     * @param recognizer Description of the Parameter
     * @exception SAXException  Any SAXException
     * @exception javax.xml.parsers.ParserConfigurationException Description
     *      of the Exception
     * @exception java.io.IOException Description of the Exception
     */
    private static void parse(final InputSource input, 
        final XliffParser recognizer) throws SAXException, 
        javax.xml.parsers.ParserConfigurationException, 
        java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = 
            javax.xml.parsers.SAXParserFactory.newInstance();
        factory.setValidating(true);//the code was generated according DTD
        factory.setNamespaceAware(true);//the code was generated according DTD
       // factory.setFeature(
       // "http://xml.org/sax/features/external-general-entities", true);
       // factory.setFeature(
       // "http://xml.org/sax/features/external-parameter-entities", true);
        
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        XliffEntityResolver xliffEntityResolver =
            new XliffEntityResolver(logger, xliffDTD);
        parser.setEntityResolver(xliffEntityResolver);
        parser.parse(input);
    }


    /**
     * Creates default error handler used by this parser.
     *
     * @return org.xml.sax.ErrorHandler implementation
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return
            new ErrorHandler() {
                public void error(SAXParseException ex) 
                    throws SAXException {
                    if (context.isEmpty()) {
                        System.err.println("Missing DOCTYPE.");
                    }
                    throw ex;
                }


                public void fatalError(SAXParseException ex) 
                    throws SAXException {
                    throw ex;
                }


                public void warning(SAXParseException ex) 
                    throws SAXException {
                    // ignore
                }
            };
    }

}

