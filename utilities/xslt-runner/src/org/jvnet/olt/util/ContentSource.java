
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ContentSource.java
 *
 * Created on June 16, 2004, 2:28 PM
 */

package org.jvnet.olt.util;

import java.io.IOException;
import java.io.Reader;

/** This interface represents a readable resource, a source of content. It 
 * encapsulates all the messiness of having to create a java.io.Reader from
 * some source, repeatedly, be that source a file, a resource path in a jar, 
 * a URL, etc.
 * @author  jc73554
 */
public interface ContentSource {
    
    public Reader getReader() throws IOException;
    
}
