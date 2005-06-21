
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SuntransMappingFileHandlerImpl.java
 *
 * Created on June 10, 2004, 18:15 PM
 */

package org.jvnet.olt.parsers.mapping;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.xml.sax.*;

/**
 * Receives notification of SuntransMappingFile Parser Events
 * and acts upon those events.
 *
 * <p><code>SuntransMappingFileHandlerImpl</code> is an implementation of the
 * <code>SuntransMappingFileHandler</code> interface. It receives notification of
 * SuntransMappingFile Parser Events from the SuntransMappingFile Parser and
 * generate the mapping data</p>
 *
 * @author    Charles Liu
 * @version   June 10, 2004
 */

public class SuntransMappingFileHandlerImpl implements SuntransMappingFileHandler {

    protected Logger logger;

    /* a Map object to contain the filename mappings */
    protected Map mappings;

    /* variables to contain the vulues */
    protected String source = "";
    protected String target = "";

    /**
     * Constructor for the SuntransMappingFileHandlerImpl object
     *
     * @param theLogger      The logging object (For logging messages).
     * @param theMappings    The map object to contain filename mapping
     */
    public SuntransMappingFileHandlerImpl(Logger theLogger, Map theMappings) {
        logger = theLogger;
        mappings = theMappings;
    }

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_suntrans_mapping_file(final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "start_suntrans_mapping_file(): " + meta);
    }

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_suntrans_mapping_file() throws SAXException {
        logger.log(Level.FINEST, "end_suntrans_mapping_file()");
    }

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_mapping(final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "start_mapping(): " + meta);
    }

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_mapping() throws SAXException {
        logger.log(Level.FINEST, "end_mapping()");

        //  Recording the current mapping
        if (!source.equals("") && !target.equals(""))
            mappings.put(source, target);
    }

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_source(final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "start_source(): " + meta);
        //  Init the variable
        source = "";
    }

    /**
     * An empty element event handling method.
     * @param data             Value or null
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void handle_source(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "handle_source(): " + data);

        source = data;
    }

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_source() throws SAXException {
        logger.log(Level.FINEST, "end_source()");
    }

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_target(final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "start_target(): " + meta);
        //  Init the variable
        target = "";
    }

    /**
     * An empty element event handling method.
     * @param data             Value or null
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void handle_target(final java.lang.String data, final Attributes meta) throws SAXException {
        logger.log(Level.FINEST, "handle_target(): " + data);

        target = data;
    }

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_target() throws SAXException {
        logger.log(Level.FINEST, "end_target()");
    }

    /**
     * The CDATA of an element
     *
     * @param values            The characters from the XML document.
     * @param param             The start position in the array.
     * @param param2            The number of characters to read from the
     *                          array.
     * @exception SAXException  Any SAXException
     */
    public void characters(char[] values, int param, int param2) throws SAXException {

    }

    /**
     * An event handler indicating the end of the document.
     *
     * @exception SAXException  Any SAXException
     */
    public void endDocument() throws SAXException {

    }

    /**
     * An event handler indicating the end of an element.
     *
     * @param str               The Namespace URI, or the empty string if
     *                          the element has no Namespace URI or if
     *                          Namespace processing is not being performed.
     * @param str1              The local name (without prefix), or the
     *                          empty string if Namespace processing is not
     *                          being performed.
     * @param str2              The qualified name (with prefix), or the
     *                          empty string if qualified names are not
     *                          available.
     * @exception SAXException  Any SAXException
     */
    public void endElement(String str, String str1, String str2) throws SAXException {

    }

    /**
     * An event handler indicating the end of a prefix mapping.
     *
     * @param str               The Namespace URI, or the empty string if
     *                          the element has no Namespace URI or if
     *                          Namespace processing is not being performed.
     * @exception SAXException  Any SAXException
     */
    public void endPrefixMapping(String str) throws SAXException {

    }

    /**
     * An event handler indicating tignorable white space.
     *
     * @param values            The characters from the XML document.
     * @param param             The start position in the array.
     * @param param2            The number of characters to read from the
     *                          array.
     * @exception SAXException  Any SAXException
     */
    public void ignorableWhitespace(char[] values, int param, int param2) throws SAXException {

    }

    /**
     * An event handler indicating processing instruction.
     *
     * @param str               The Namespace URI, or the empty string if
     *                          the element has no Namespace URI or if
     *                          Namespace processing is not being performed.
     * @param str1              The local name (without prefix), or the
     *                          empty string if Namespace processing is not
     *                          being performed.
     * @exception SAXException  Any SAXException
     */
    public void processingInstruction(String str, String str1) throws SAXException {

    }

    /**
     * Sets the documentLocator attribute of the SuntransMappingFileHandlerImpl object
     *
     * @param locator  The new documentLocator value
     */
    public void setDocumentLocator(org.xml.sax.Locator locator) {

    }

    /**
     * An event handler indicating  a Skipped Entry
     *
     * @param str               The Namespace URI, or the empty string if
     *                          the element has no Namespace URI or if
     *                          Namespace processing is not being performed.
     * @exception SAXException  Any SAXException
     */
    public void skippedEntity(String str) throws SAXException {

    }

    /**
     * An event handler indicating the start of a document.
     *
     * @exception SAXException  Any SAXException
     */
    public void startDocument() throws SAXException {

    }

    /**
     * An event handler indicating the start of a  element.
     *
     * @param str               The Namespace URI, or the empty string if
     *                          the element has no Namespace URI or if
     *                          Namespace processing is not being performed.
     * @param str1              The local name (without prefix), or the
     *                          empty string if Namespace processing is not
     *                          being performed.
     * @param str2              The qualified name (with prefix), or the
     *                          empty string if qualified names are not
     *                          available.
     * @param attributes        The attributes attached to the element. If
     *                          there are no attributes, it shall be an
     *                          empty Attributes object.
     * @exception SAXException  Any SAXException
     */
    public void startElement(String str, String str1, String str2, org.xml.sax.Attributes attributes) throws SAXException {

    }

    /**
     * An event handler indicating the start of a prefix mapping.
     *
     * @param str               The Namespace prefix being declared
     * @param str1              The Namespace URI the prefix is mapped to.
     * @exception SAXException  Any SAXException
     */
    public void startPrefixMapping(String str, String str1) throws SAXException {

    }
}



