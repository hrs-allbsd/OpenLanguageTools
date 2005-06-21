
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xml.xmlconfig;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.*;

public class XmlLookupResolver implements EntityResolver {
    
    private XmlIdentifier xmlIdentifier;
    
    private String dummyDTDFile;
    
    public XmlLookupResolver(XmlIdentifier xmlIdentifier, String dummyDTDFile) {
        this.xmlIdentifier = xmlIdentifier;
        this.dummyDTDFile = dummyDTDFile;
    }
    
    public InputSource resolveEntity(String publicId, String systemId) {
        
        xmlIdentifier.setPublicID(publicId);
        if (xmlIdentifier.getSystemID() == ""){
            /*
             * For cases where we're looking at a URL to the dtd,
             * we use the entire system id. Otherwise, we return just
             * the filename portion (otherwise, the xml parser returns
             * the full path to the filename, eg : <!DOCTYPE foo SYSTEM "bla.dtd">
             * will return "/apps/foo/bla/appserver/applications/TMSystem_1/..../bla.dtd"
             * and we won't find the config file that matches that (since the config file
             * will most likely just say "bla.dtd"
             */
            if (systemId.startsWith("http://")){
                xmlIdentifier.setSystemID(systemId);
            } else {
                int beginIndex = systemId.lastIndexOf(File.separator);
                String systemIdSubString = systemId.substring(beginIndex + 1);
                xmlIdentifier.setSystemID(systemIdSubString);
            }
            
        }
        try {
            Reader reader = new FileReader(new File(dummyDTDFile));
            return new InputSource(reader);
        } catch(FileNotFoundException ex) {
            return null;
        }
        
        
        
    }
}
