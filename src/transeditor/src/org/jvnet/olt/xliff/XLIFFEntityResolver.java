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
        schemas.put(new SchemaLocator("-//XLIFF//DTD XLIFF//EN", "http://www.oasis-open.org/committees/xliff/documents/xliff.dtd"), "/dtd/xliff.dtd");
        schemas.put(new SchemaLocator("-//XLIFF//DTD XLIFF//EN", null ), "/dtd/xliff.dtd");

        schemas.put(new SchemaLocator(null, "xliff-core-1.1.xsd"), "/xsd/xliff-core-1.1.xsd");
        schemas.put(new SchemaLocator("urn:oasis:names:tc:xliff:document:1.1", null), "/xsd/xliff-core-1.1.xsd");
        schemas.put(new SchemaLocator(null, "http://www.oasis-open.org/committees/xliff/documents/xliff-core-1.1.xsd"), "/xsd/xliff-core-1.1.xsd");

        schemas.put(new SchemaLocator(null, "xliff-core-1.2-strict.xsd"), "/xsd/xliff-core-1.2-strict.xsd");
        schemas.put(new SchemaLocator(null, "http://www.oasis-open.org/committees/xliff/documents/xliff-core-1.2-strict.xsd"), "/xsd/xliff-core-1.2-strict.xsd");

        schemas.put(new SchemaLocator("urn:oasis:names:tc:xliff:document:1.2", null), "/xsd/xliff-core-1.2-transitional.xsd");
        schemas.put(new SchemaLocator("urn:oasis:names:tc:xliff:document:1.2", "xliff-core-1.2-transitional.xsd"), "/xsd/xliff-core-1.2-transitional.xsd");
        schemas.put(new SchemaLocator(null, "xliff-core-1.2-transitional.xsd"), "/xsd/xliff-core-1.2-transitional.xsd");
        schemas.put(new SchemaLocator(null, "http://www.oasis-open.org/committees/xliff/documents/xliff-core-1.2-transitional.xsd"), "/xsd/xliff-core-1.2-transitional.xsd");

        schemas.put(new SchemaLocator(null, "http://www.w3.org/2001/xml.xsd"), "/xsd/xml.xsd");

        schemas.put(new SchemaLocator("-//W3C//DTD XMLSCHEMA 200102//EN", "XMLSchema.dtd"), "/dtd/XMLSchema.dtd");
        schemas.put(new SchemaLocator("datatypes", "datatypes.dtd"), "/dtd/datatypes.dtd");
        schemas.put(new SchemaLocator(null , "datatypes.dtd"), "/dtd/datatypes.dtd");
        
        schemas.put(new SchemaLocator(null, "tmx11.dtd"),"/dtd/tmx11.dtd");
        schemas.put(new SchemaLocator(null, "tmx12.dtd"),"/dtd/tmx12.dtd");
        schemas.put(new SchemaLocator(null, "tmx13.dtd"),"/dtd/tmx13.dtd");
        schemas.put(new SchemaLocator(null, "tmx14.dtd"),"/dtd/tmx14.dtd");
		
        schemas.put(new SchemaLocator("-//LISA OSCAR:1998//DTD for Translation Memory eXchange//EN", "tmx13.dtd"),"/dtd/tmx13.dtd");
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
        
        if(systemId.endsWith("/tmx14.dtd")){
            systemId = "tmx14.dtd";
        }
        if(systemId.endsWith("/tmx13.dtd")){
            systemId = "tmx13.dtd";
        }
		
        if(systemId.endsWith("/tmx12.dtd")){
            systemId = "tmx12.dtd";
        }

		if(systemId.endsWith("/tmx11.dtd")){
            systemId = "tmx11.dtd";
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

        if (systemId.endsWith("/xliff-core-1.2-strict.xsd")) {
            systemId = "xliff-core-1.2-strict.xsd";
        }

        if (systemId.endsWith("/xliff-core-1.2-strict.xsd")) {
            systemId = "xliff-core-1.2-strict.xsd";
        }

        if (systemId.endsWith("/xliff-core-1.2-transitional.xsd")) {
            systemId = "xliff-core-1.2-transitional.xsd";
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
            logger.warning("schema not registered. Trying direct file access");
            String res =  "/resources/" + systemId.split("/")[systemId.split("/").length -1];
            InputStream is = this.getClass().getResourceAsStream(res);
            if (is == null) {
                logger.warning("File " + res + " could not be found");
            } else {
                return new InputSource(is);
            }
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
