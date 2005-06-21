
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
package org.jvnet.olt.filters.xml.xmlconfig;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.File;
import java.io.Reader;

/**
 * Resolves the XmlConfig DTD for entities.
 *
 * <p>This XmlConfigResolver ignores the public and url locations for 
 * the DTD as indicated in XmlConfig files and instead gets and resolves 
 * the DTD from a local directory.</p>
 *
 * @author    Brian Kidney
 * @version   August 07, 2003
 */
public class XmlConfigResolver implements EntityResolver {

    
    private Reader xmlConfigDTD;
        
    /**
     * Constructor for the XmlConfigEntityResolver object
     *
     * @param theXmlConfigDTD   The XmlConfig DTD.
     */
    public XmlConfigResolver(Reader theXmlConfigDTD) {     
        this.xmlConfigDTD = theXmlConfigDTD;
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
                
        if (systemId.endsWith("xml-config.dtd")) {
            return new InputSource(xmlConfigDTD);
        } else {            
            return null;
        } 
    }
    

}

