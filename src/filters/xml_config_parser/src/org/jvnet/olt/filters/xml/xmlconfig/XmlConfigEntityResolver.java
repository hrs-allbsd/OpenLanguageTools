
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlConfigEntityResolver.java
 *
 * Created on January 06, 2003, 10:57 AM
 */
package org.jvnet.olt.filters.xml.xmlconfig;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.Reader;
import java.util.logging.*;

/**
 * Resolves the XmlConfig DTD for entities.
 *
 * <p>This XmlConfigEntityResolver ignores the public and url locations for 
 * the DTD as indicated in XmlConfig files and instead gets and resolves the DTD
 * from a local directory.</p>
 *
 * @author    Brian Kidney
 * @version   January 06, 2003
 */
public class XmlConfigEntityResolver implements EntityResolver {

    private static Logger logger =
        Logger.getLogger("org.jvnet.olt.filters.xml.xmlconfig");
    
    /* The XmlConfig DTD */
    private Reader xmlConfigDTD = null;
    
    /* The Location of the XmlConfig DTDs */
    private String xmlConfigDTDLocation; 

    /**
     * Constructor for the XmlConfigEntityResolver object
     *
     */
    public XmlConfigEntityResolver() {

        //BK Need to fix this
        this.xmlConfigDTDLocation = 
            "C:\\work\\xml_filter\\xml_config_parser\\resources\\xml-config.dtd";

    }

    
    /**
     * This method gets the correct XmlConfig dtd (ignoring the one 
     * specified in the header)
     *
     * @param publicId The public Id of the DTD
     * @param systemId The system Id of the DTD
     * @return The correct XmlConfig DTD
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        
        logger.log(Level.FINER, "Start Resolving Entity");
        logger.log(Level.INFO, "SystemId = " + systemId);
        logger.log(Level.FINEST, "PublicId = " + publicId);
       
        return getDTDBySystemId(systemId);           
       
    }
    
    private InputSource getDTDBySystemId(String systemId) {
             
        logger.log(Level.INFO, "PublicID not found");
        
        File file = null;

        /* Open a file containing the DTD, so we can get its filename 
           later     
        */
        try {
            
            file = new File(systemId);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING,
                "Pathname of file to parse is null", e);
            return null;
        }
            
        logger.log(Level.INFO, "file.getName() = " + file.getName());
        if (file.getName().equals("xml-config.dtd")) { 
            logger.log(Level.INFO, "Using xml-config.dtd");
            return new InputSource(xmlConfigDTDLocation);
        } else {
            logger.log(Level.INFO, "Returning Null");
            return null;
        }        
    }

}
