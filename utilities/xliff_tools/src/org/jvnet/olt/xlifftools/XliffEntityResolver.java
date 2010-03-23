
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffEntityResolver.java
 *
 * Created on 08 August 2003, 17:19
 */

package org.jvnet.olt.xlifftools;

import java.io.File;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.InputStream;

/**
 *
 * @author  jc73554
 */
public class XliffEntityResolver implements EntityResolver
{
    
    /** Creates a new instance of XliffEntityResolver */
    public XliffEntityResolver()
    {
    }
    
    public InputSource resolveEntity(String publicId, String systemId)
    {
        if(systemId.endsWith("xliff.dtd"))
        {
            InputStream istream = this.getClass().getResourceAsStream("/dtd/xliff.dtd");
            if(istream != null)
            {
                InputSource source = new InputSource(istream);
                return source;
            }
            else
            {
                System.err.println("DTD InputStream was null!");
                return null;
            }
        }
        else
        {
            //  We only resolve XLIFF SYSTEM ids!
            return null;
        }

    }
    
}
