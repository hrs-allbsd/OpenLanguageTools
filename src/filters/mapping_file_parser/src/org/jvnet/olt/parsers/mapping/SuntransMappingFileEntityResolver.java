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
 * SuntransMappingFileEntityResolver.java
 *
 * Created on June 10, 2004, 18:13 PM
 */
package org.jvnet.olt.parsers.mapping;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.Reader;
import java.util.logging.*;

/**
 * Resolves the SuntransMappingFile Entity DTD for entities.
 *
 * <p>This SuntransMappingFileEntityResolver ignores the public and url locations
 * for the DTD as indicated in SuntransMappingFile files and instead gets and resolves
 * the DTD from a local directory.</p>
 *
 * @author    Charles Liu
 * @version   June 10, 2004
 */

public class SuntransMappingFileEntityResolver implements EntityResolver {

    /* SuntransMappingFile DTD */
    private Reader suntransMappingFileDTD;

    /* Logging object */
    private Logger logger = null;

    /**
     * Constructor for the SuntransMappingFileEntityResolver object
     *
     * @param theLogger  The logging object (For logging messages)
     * @param theDTD     SuntransMappingFile DTD
     */
    public SuntransMappingFileEntityResolver(Logger theLogger, Reader theDTD) {

        this.logger = theLogger;
        this.suntransMappingFileDTD = theDTD;
    }

    /**
     * Allow the application to resolve external entities.
     *
     * The Parser will call this method before opening any external entity
     * except the top-level document entity (including the external DTD
     * subset, external entities referenced within the DTD, and external
     * entities referenced within the document element).
     *
     * This method is used to open the entity from a local directory
     *
     * @param publicId          The public identifier of the external entity
     *                          being referenced, or null if none was
     *                          supplied.
     * @param systemId          The system identifier of the external entity
     *                          being referenced.
     * @return                  An InputSource object describing the new
     *                          input source, or null to request that the
     *                          parser open a regular URI connection to the
     *                          system identifier.
     * @exception SAXException  Any SAX exception, possibly wrapping another
     *                          exception.Any SAX exception, possibly
     *                          wrapping another exception.
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

        logger.log(Level.FINEST, "publicId = " + publicId);
        logger.log(Level.FINEST, "SystemId = " + systemId);

        File file = null;
        /* Open a file containing the DTD, so we can get its filename later
         */
        try {
            file = new File(systemId);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Pathname of file to parse is null", e);
            throw new NullPointerException();
        }

        logger.log(Level.FINEST, "File.getName() is \"" + file.getName() + "\"");
        if (file.getName().equals("suntrans-mapping-file.dtd")) {
            logger.log(Level.FINEST, "Using SunTransMappingFile DTD");
            return new InputSource(suntransMappingFileDTD);
        } else {
            logger.log(Level.FINEST, "Returning Null");
            return null;
        }
    }
}

