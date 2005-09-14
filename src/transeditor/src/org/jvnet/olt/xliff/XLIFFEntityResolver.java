/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.io.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jvnet.olt.xliff.handlers.SchemaLocator;

import org.xml.sax.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class XLIFFEntityResolver implements EntityResolver {
    private static final Logger logger = Logger.getLogger(XLIFFEntityResolver.class.getName());
    static private Map schemas = Collections.synchronizedMap(new HashMap());

    static {
        schemas.put(new SchemaLocator("-//XLIFF//DTD XLIFF//EN", "http://www.oasis-open.org/committees/xliff/documents/xliff.dtd"), "/resources/xliff.dtd");
        schemas.put(new SchemaLocator("-//XLIFF//DTD XLIFF//EN", null ), "/resources/xliff.dtd");
        schemas.put(new SchemaLocator(null, "xliff-core-1.1.xsd"), "/resources/xliff-core-1.1.xsd");
        schemas.put(new SchemaLocator(null, "http://www.oasis-open.org/committees/xliff/documents/xliff-core-1.1.xsd"), "/resources/xliff-core-1.1.xsd");
        schemas.put(new SchemaLocator(null, "http://www.w3.org/2001/xml.xsd"), "/resources/xml.xsd");
        schemas.put(new SchemaLocator("-//W3C//DTD XMLSCHEMA 200102//EN", "XMLSchema.dtd"), "/resources/XMLSchema.dtd");
        schemas.put(new SchemaLocator("datatypes", "datatypes.dtd"), "/resources/datatypes.dtd");
        schemas.put(new SchemaLocator(null , "datatypes.dtd"), "/resources/datatypes.dtd");
        
        schemas.put(new SchemaLocator(null, "tmx13.dtd"),"/resources/tmx13.dtd");
        schemas.put(new SchemaLocator("-//LISA OSCAR:1998//DTD for Translation Memory eXchange//EN", "tmx13.dtd"),"/resources/tmx13.dtd");
    }

    static private XLIFFEntityResolver instance = new XLIFFEntityResolver();

    private XLIFFEntityResolver() {
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        logger.finer("Resolving publicId:" + publicId + " systemId:" + systemId);

        //
        // workaround to support SYSTEM declaration 
        //
        if (systemId.endsWith("xliff.dtd") && publicId == null) {
            publicId = "-//XLIFF//DTD XLIFF//EN";
            systemId = "http://www.oasis-open.org/committees/xliff/documents/xliff.dtd";
        }
        
        if(systemId.endsWith("/tmx13.dtd")){
            systemId = "tmx13.dtd";
        }
        
        if (systemId.endsWith("/XMLSchema.dtd")) {
            systemId = "XMLSchema.dtd";
        }

        if (systemId.endsWith("/datatypes.dtd")) {
            systemId = "datatypes.dtd";
        }

        if (systemId.endsWith("/xliff-core-1.1.xsd")) {
            systemId = "xliff-core-1.1.xsd";
        }

        SchemaLocator sl = new SchemaLocator(publicId, systemId);

        if (schemas.containsKey(sl)) {
            String res = (String)schemas.get(sl);

            logger.finer("Resource:" + res);

            InputStream is = this.getClass().getResourceAsStream(res);

            if (is == null) {
                logger.warning("Resource " + res + " could not be found");
            } else {
                return new InputSource(is);
            }
        } else {
            logger.warning("schema not registered. Returning null");
        }

        return null;
    }

    public static XLIFFEntityResolver instance() {
        return instance;
    }

    public SchemaLocator containsSchema(String pubId, String sysId) {
        SchemaLocator sl = new SchemaLocator(pubId, sysId);

        return schemas.containsKey(sl) ? sl : null;
    }
}
