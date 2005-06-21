
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * Suntrans2tmEntityResolver.java
 *
 * Created on April 27, 2004, 2:07 PM
 */

package org.jvnet.olt.filters.intstaroffice.xliff;
import org.xml.sax.*;
import java.io.FileReader;

/**
 * This is a really simple EntityResolver that doesn't resolve entities at all
 * - it just returns a pointer to /dev/null : we're assuming that the parser
 * doesn't need the dtd since it'll be a non validating parser.
 * @author  timf
 */
public class NullEntityResolver implements EntityResolver{
    

    /** Creates a new instance of Suntrans2tmEntityResolver */
    public NullEntityResolver() {
    }
    
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, java.io.IOException {
        // we don't really care about resolving the dtd, assuming we're getting 
        // proper XML from the portal..
        return new InputSource(new FileReader("/dev/null"));
    }
    
}
