
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xml.xmlconfig;

import org.xml.sax.*;

public class XmlLookupDTDHandlerImpl implements DTDHandler {
    
    public void notationDecl(String name, String publicId, String systemId) 
        throws org.xml.sax.SAXException {
            System.out.println("notationDecl");
            System.out.println("name = " + name);
            System.out.println("publicId = " + publicId);
            System.out.println("systemId = " + systemId);
    }
    
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws org.xml.sax.SAXException {
        System.out.println("unparsedEntityDecl");
        System.out.println("name = " + name);
        System.out.println("publicId = " + publicId);
        System.out.println("systemId = " + systemId);
        System.out.println("notationName = " + notationName);
    }
    
}
