
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * LoadableResourceContentSource.java
 *
 * Created on June 16, 2004, 2:33 PM
 */

package org.jvnet.olt.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/** This class represents a ContentSource created from  
 * @author  jc73554
 */
public class LoadableResourceContentSource implements ContentSource {
    
    private String resourcePath;
    
    /** Creates a new instance of LoadableResourceContentSource */
    public LoadableResourceContentSource(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public Reader getReader() throws IOException {
        try {
            ClassLoader loader = this.getClass().getClassLoader();
            
            InputStream stream = loader.getResourceAsStream(resourcePath);
            if(stream == null) {
                throw new IOException("The resource pointed to by '" + resourcePath + "' could not be found.");
            }
            
            return new InputStreamReader(stream);
        }
        catch(SecurityException secEx) {
            throw new IOException("Unable to get ClassLoader: " + secEx.getMessage());
        }
    }
    
}
