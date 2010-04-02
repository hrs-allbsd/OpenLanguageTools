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
 * XliffEntityResolver.java
 *
 * Created on August 14, 2002, 10:57 AM
 */
package org.jvnet.olt.xliff_tmx_converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.File;
import java.io.Reader;

/**
 * Resolves the XLIFF DTD for entities.
 *
 * <p>This XliffEntityResolver ignores the public and url locations for 
 * the DTD as indicated in XLIFF files and instead gets and resolves the DTD
 * from a local directory.</p>
 *
 * @author    Brian Kidney
 * @version   August 14, 2002
 */
public class XliffEntityResolver implements EntityResolver {

    /* Properties required by the Back Converter */
   // private BackConverterProperties props = null;

    /* Logging object */
    private Logger logger = null;
    
    private Reader xliffDTD;
        
    /**
     * Constructor for the XliffEntityResolver object
     *
     * @param theLogger  The logging object (For logging messages).
     * @param theXliffDTD   The Xliff DTD.
     */
    public XliffEntityResolver(Logger theLogger, Reader theXliffDTD) {

        this.logger = theLogger;
        logger.log(Level.FINEST, "XliffEntityResolver constructor created");
        
        this.xliffDTD = theXliffDTD;

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
    public InputSource resolveEntity(String publicId, String systemId)
         throws SAXException {
        
        logger.log(Level.FINEST, "SystemId = " + systemId);

        File file = null;

        /* Open a file containing the DTD, so we can get its filename later     
         */
        try {
            file = new File(systemId);
        } catch (NullPointerException e) {
            logger.log(Level.FINE,
                "Pathname of file to parse is null", e);
            throw new NullPointerException();
        }
        
        logger.log(Level.FINEST, "File.getName() is " + file.getName());
        if (file.getName().equals("xliff.dtd")) {
            logger.log(Level.FINEST, "Using XLIFFDTD");
            return new InputSource(xliffDTD);
        } else {
            logger.log(Level.FINEST, "Returning Null");
            return null;
        }

    }

}

