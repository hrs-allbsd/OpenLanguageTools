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
package org.jvnet.olt.xliff;

import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


/**
 * The class reads XML documents according to specified DTD and
 * translates all related events into Handler events.
 * <p>Usage sample:
 * <pre>
 *    XParser parser = new XParser(...);
 *    parser.parse(new InputSource("..."));
 * </pre>
 * <p><b>Warning:</b> the class is machine generated. DO NOT MODIFY</p>
 */
class XParser implements ContentHandler {
    private static final Logger logger = Logger.getLogger(XParser.class.getName());
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private java.lang.StringBuffer buffer;
    private Handler handler;
    private java.util.Stack context;
    private XLIFFEntityResolver resolver;

    /**
     * Creates a parser instance.
     * @param handler handler interface implementation (never <code>null</code>
     * @param resolver SAX entity resolver implementation or <code>null</code>.
     * It is recommended that it could be able to resolve at least the DTD. */
    public XParser(final Handler handler, final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = XLIFFEntityResolver.instance();
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }

    /**
     * Receives notification of a skipped entity.
     * A warning message is printed.
     */
    public void skippedEntity(String name) throws SAXException {
        logger.warning("Skipped Entity: " + name);
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void startDocument() throws SAXException {
        handler.startDocument();
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void endDocument() throws SAXException {
        handler.endDocument();
    }

    /**
     * Begins the scope of a prefix-URI Namespace mapping.
     * The mapping is saved to a hashtable
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    /**
     * Ends the scope of a prefix-URI mapping.
     * The mapping is removed from the hashtable.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public void startElement(String uri, String localName, String name, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[] { name, new org.xml.sax.helpers.AttributesImpl(attrs) });

        if ("mrk".equals(name)) {
            handler.start_mrk(attrs);
        } else if ("bin-unit".equals(name)) {
            handler.start_bin_unit(attrs);
        } else if ("sub".equals(name)) {
            handler.start_sub(attrs);
        } else if ("file".equals(name)) {
            handler.start_file(attrs);
        } else if ("target".equals(name)) {
            handler.start_target(attrs);
        } else if ("xliff".equals(name)) {
            handler.start_xliff(attrs);
        } else if ("bx".equals(name)) {
            handler.handle_bx(attrs);
        } else if ("prop-group".equals(name)) {
            handler.start_prop_group(attrs);
        } else if ("skl".equals(name)) {
            handler.start_skl(attrs);
        } else if ("trans-unit".equals(name)) {
            handler.start_trans_unit(attrs);
        } else if ("phase".equals(name)) {
            handler.start_phase(attrs);
        } else if ("body".equals(name)) {
            handler.start_body(attrs);
        } else if ("group".equals(name)) {
            handler.start_group(attrs);
        } else if ("ept".equals(name)) {
            handler.start_ept(attrs);
        } else if ("header".equals(name)) {
            handler.start_header(attrs);
        } else if ("ph".equals(name)) {
            handler.start_ph(attrs);
        } else if ("g".equals(name)) {
            handler.start_g(attrs);
        } else if ("ex".equals(name)) {
            handler.handle_ex(attrs);
        } else if ("phase-group".equals(name)) {
            handler.start_phase_group(attrs);
        } else if ("external-file".equals(name)) {
            handler.handle_external_file(attrs);
        } else if ("alt-trans".equals(name)) {
            handler.start_alt_trans(attrs);
        } else if ("context-group".equals(name)) {
            handler.start_context_group(attrs);
        } else if ("bpt".equals(name)) {
            handler.start_bpt(attrs);
        } else if ("it".equals(name)) {
            handler.start_it(attrs);
        } else if ("count-group".equals(name)) {
            handler.start_count_group(attrs);
        } else if ("bin-source".equals(name)) {
            handler.start_bin_source(attrs);
        } else if ("glossary".equals(name)) {
            handler.start_glossary(attrs);
        } else if ("source".equals(name)) {
            handler.start_source(attrs);
        } else if ("x".equals(name)) {
            handler.handle_x(attrs);
        } else if ("bin-target".equals(name)) {
            handler.start_bin_target(attrs);
        } else if ("reference".equals(name)) {
            handler.start_reference(attrs);
        } else if ("note".equals(name)) {
            handler.start_note(attrs);
        } else if ("prop".equals(name)) {
            handler.start_prop(attrs);
        }

        handler.startElement(uri, localName, name, attrs);
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public void endElement(String uri, String localName, String name) throws SAXException {
        dispatch(false);
        context.pop();

        if ("mrk".equals(name)) {
            handler.end_mrk();
        } else if ("bin-unit".equals(name)) {
            handler.end_bin_unit();
        } else if ("sub".equals(name)) {
            handler.end_sub();
        } else if ("file".equals(name)) {
            handler.end_file();
        } else if ("target".equals(name)) {
            handler.end_target();
        } else if ("xliff".equals(name)) {
            handler.end_xliff();
        } else if ("prop-group".equals(name)) {
            handler.end_prop_group();
        } else if ("skl".equals(name)) {
            handler.end_skl();
        } else if ("trans-unit".equals(name)) {
            handler.end_trans_unit();
        } else if ("phase".equals(name)) {
            handler.end_phase();
        } else if ("body".equals(name)) {
            handler.end_body();
        } else if ("group".equals(name)) {
            handler.end_group();
        } else if ("ept".equals(name)) {
            handler.end_ept();
        } else if ("header".equals(name)) {
            handler.end_header();
        } else if ("ph".equals(name)) {
            handler.end_ph();
        } else if ("g".equals(name)) {
            handler.end_g();
        } else if ("phase-group".equals(name)) {
            handler.end_phase_group();
        } else if ("alt-trans".equals(name)) {
            handler.end_alt_trans();
        } else if ("context-group".equals(name)) {
            handler.end_context_group();
        } else if ("bpt".equals(name)) {
            handler.end_bpt();
        } else if ("it".equals(name)) {
            handler.end_it();
        } else if ("count-group".equals(name)) {
            handler.end_count_group();
        } else if ("bin-source".equals(name)) {
            handler.end_bin_source();
        } else if ("glossary".equals(name)) {
            handler.end_glossary();
        } else if ("source".equals(name)) {
            handler.end_source();
        } else if ("bin-target".equals(name)) {
            handler.end_bin_target();
        } else if ("reference".equals(name)) {
            handler.end_reference();
        } else if ("note".equals(name)) {
            handler.end_note();
        } else if ("prop".equals(name)) {
            handler.end_prop();
        }

        handler.endElement(uri, localName, name);
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void characters(char[] chars, int start, int len) throws SAXException {
        buffer.append(chars, start, len);
        handler.characters(chars, start, len);
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
        handler.ignorableWhitespace(chars, start, len);
    }

    /**
     * This SAX interface method is implemented by the parser.
     */
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
        handler.processingInstruction(target, data);
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && (buffer.length() == 0)) {
            return; //skip it
        }

        Object[] ctx = (Object[])context.peek();
        String here = (String)ctx[0];
        Attributes attrs = (Attributes)ctx[1];

        if ("mrk".equals(here)) {
            handler.handle_mrk((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("sub".equals(here)) {
            handler.handle_sub((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("target".equals(here)) {
            handler.handle_target((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("internal-file".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }

            handler.handle_internal_file((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("context".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }

            handler.handle_context((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("prop".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }

            handler.handle_prop((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("ept".equals(here)) {
            handler.handle_ept((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("ph".equals(here)) {
            handler.handle_ph((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("g".equals(here)) {
            handler.handle_g((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("note".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }

            handler.handle_note((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("bpt".equals(here)) {
            handler.handle_bpt((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("it".equals(here)) {
            handler.handle_it((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("source".equals(here)) {
            handler.handle_source((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else if ("count".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }

            handler.handle_count((buffer.length() == 0) ? null : buffer.toString(), attrs);
        } else {
            //do not care
        }

        buffer.delete(0, buffer.length());
    }

    /**
     * The recognizer entry method taking an InputSource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     */
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, this);
    }

    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     */
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }

    /**
     * The recognizer entry method taking an Inputsource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     */
    public static void parse(final InputSource input, final Handler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new XParser(handler, null));
    }

    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     */
    public static void parse(final java.net.URL url, final Handler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final InputSource input, final XParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        try {
            javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
            factory.setValidating(true); //the code was generated according DTD
            factory.setNamespaceAware(true); //the code was generated according DTD

            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            XMLReader parser = saxParser.getXMLReader();

            parser.setContentHandler(recognizer);
            parser.setErrorHandler(recognizer.getDefaultErrorHandler());
            parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

            if (recognizer.resolver != null) {
                parser.setEntityResolver(recognizer.resolver);
            }

            parser.parse(input);
        } catch (SAXException e) {
            logger.throwing(XParser.class.getName(), "parse(InputSource,XParser)", e);
            throw e;
        }
    }

    /**
     * Creates default error handler used by this parser.
     * @return org.xml.sax.ErrorHandler implementation
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
                public void error(SAXParseException ex) throws SAXException {
                    if (context.isEmpty()) {
                        logger.severe("Missing DOCTYPE.");
                    }

                    throw ex;
                }

                public void fatalError(SAXParseException ex) throws SAXException {
                    throw ex;
                }

                public void warning(SAXParseException ex) throws SAXException {
                    logger.warning("warning exception while parsing:" + ex);
                }
            };
    }
}
