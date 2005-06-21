
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SuntransMappingFileHandler.java
 *
 * Created on June 10, 2004, 18:15 PM
 */
package org.jvnet.olt.parsers.mapping;

import org.xml.sax.*;

/**
 * Receives notification of SuntransMappingFile Parser Events
 * and acts upon those events.
 *
 * @author    Charles Liu
 * @version   June 10, 2004
 * @see       SuntransMappingFileHandlerImpl
 */
public interface SuntransMappingFileHandler extends ContentHandler {

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_suntrans_mapping_file(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_suntrans_mapping_file() throws SAXException;

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_mapping(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_mapping() throws SAXException;

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_source(final Attributes meta) throws SAXException;

    /**
     * An empty element event handling method.
     * @param data             Value or null
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void handle_source(final java.lang.String data, final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_source() throws SAXException;

    /**
     * A container element start event handling method.
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void start_target(final Attributes meta) throws SAXException;

    /**
     * An empty element event handling method.
     * @param data             Value or null
     * @param meta             The attributes and their values of the element.
     * @throws SAXException    Any SAX exception
     */
    public void handle_target(final java.lang.String data, final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws SAXException    Any SAX exception
     */
    public void end_target() throws SAXException;

}

