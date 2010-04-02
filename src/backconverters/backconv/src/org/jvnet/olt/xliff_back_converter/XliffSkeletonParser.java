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
 * File:           XliffSkeletonParser.java
 * Date:           August 13, 2002  11:30 AM
 *
 * @author  kb128066
 * @version generated by NetBeans XML module
 */
package org.jvnet.olt.xliff_back_converter;

import org.xml.sax.*;
import java.util.logging.*;
import java.io.Reader;

/**
 * The class reads XML documents according to specified DTD and translates
 * all related events into XliffSkeletonHandler events. <p>
 *
 * @author Brian Kidney
 * @version August 13, 2002
 */
public class XliffSkeletonParser implements ContentHandler {

    private java.lang.StringBuffer buffer;

    private XliffSkeletonHandler handler;

    private java.util.Stack context;

    private EntityResolver resolver;

    /* Properties required by the Back Converter */
    private BackConverterProperties props;

    /* Logging object */
    private static Logger logger;

    /* XLIFF Skeleton DTD */
    private static Reader skeletonDTD;


    /**
     * Constructor for the XliffSkeletonParser object
     *
     * @param handler The Xliff Handler
     * @param resolver The Entity Resolver
     * @param theLogger The logging object (For logging messages).
     * @param theProps Properties required by the Back Converter.
     * @param theSkeletonTD   The Xliff Skeleton DTD.
     */
    public XliffSkeletonParser(final XliffSkeletonHandler handler,
                                     final EntityResolver resolver, Logger theLogger,
                                     BackConverterProperties theProps,
                                     Reader theSkeletonDTD) {

        this.logger = theLogger;
        this.props = theProps;
        this.handler = handler;
        this.resolver = resolver;
        this.skeletonDTD = theSkeletonDTD;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();

    }


    /**
     * Creates a Xliff Skeleton parser instance.
     *
     * @param handler handler interface implementation (never
     *        <code>null</code>
     * @param resolver SAX entity resolver implementation or
     *        <code>null</code>. It is recommended that it could be able to
     *        resolve at least the DTD.
     */
    public XliffSkeletonParser(final XliffSkeletonHandler handler,
                                     final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();

        this.props = null;
        this.logger = null;
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
    public final void startElement(java.lang.String ns, java.lang.String name, java.lang.String qname, Attributes attrs)
        throws SAXException {
        dispatch(true);
        context.push(new Object[]{qname, new org.xml.sax.helpers.AttributesImpl(attrs)});
        if ("tt-xliff-skl".equals(name)) {
            handler.start_tt_xliff_skl(attrs);
        } else if ("header".equals(name)) {
            handler.start_header(attrs);
        } else if ("file".equals(name)) {
            handler.start_file(attrs);
        } else if ("tu-placeholder".equals(name)) {
            handler.handle_tu_placeholder(attrs);
        } else if ("body".equals(name)) {
            handler.start_body(attrs);
        } else if ("formatting".equals(name)) {
            handler.start_formatting(attrs);
        }

        handler.startElement(ns, name, qname, attrs);
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
    public final void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname)
        throws SAXException {
        dispatch(false);
        context.pop();
        if ("tt-xliff-skl".equals(name)) {
            handler.end_tt_xliff_skl();
        } else if ("header".equals(name)) {
            handler.end_header();
        } else if ("file".equals(name)) {
            handler.end_file();
        } else if ("body".equals(name)) {
            handler.end_body();
        } else if ("formatting".equals(name)) {
            handler.end_formatting();
        }
        handler.endElement(ns, name, qname);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param chars The characters from the XML document.
     * @param start The start position in the array.
     * @param len The number of characters to read from the array.
     * @exception SAXException  Any SAXException
     */
    public final void characters(char[] chars, int start, int len) throws SAXException {
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
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
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
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
        handler.processingInstruction(target, data);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param prefix The Namespace prefix being declared.
     * @param uri The Namespace URI the prefix is mapped to.
     * @exception SAXException  Any SAXException
     */
    public final void startPrefixMapping(final java.lang.String prefix, final java.lang.String uri) throws SAXException {
        handler.startPrefixMapping(prefix, uri);
    }


    /**
     * This SAX interface method is implemented by the parser.
     *
     * @param prefix The prefix that was being mapping.
     * @exception SAXException  Any SAXException
     */
    public final void endPrefixMapping(final java.lang.String prefix) throws SAXException {
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
    public final void skippedEntity(java.lang.String name) throws SAXException {
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
        if ("formatting".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }
            handler.handle_formatting(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else if ("xliff-file".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }
            handler.handle_xliff_file(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else {
            //do not care
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
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
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
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
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
    public static void parse(final InputSource input, final XliffSkeletonHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new XliffSkeletonParser(handler, null));
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
    public static void parse(final java.net.URL url, final XliffSkeletonHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }


    /**
     * Description of the Method
     *
     * @param input Description of the Parameter
     * @param recognizer Description of the Parameter
     * @exception SAXException  Any SAXException
     * @exception javax.xml.parsers.ParserConfigurationException Description
     *      of the Exception
     * @exception java.io.IOException Description of the Exception
     */
    private static void parse(final InputSource input, final XliffSkeletonParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
        factory.setValidating(true);//the code was generated according DTD
        factory.setNamespaceAware(true);//the code was generated according DTD
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        parser.setEntityResolver(new XliffSkeletonEntityResolver(
            logger, skeletonDTD));
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

