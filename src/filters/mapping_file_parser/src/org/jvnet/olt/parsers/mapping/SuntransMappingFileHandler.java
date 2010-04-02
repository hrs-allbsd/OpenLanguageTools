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

