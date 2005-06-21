
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SegmenterFormatterException.java
 *
 * Created on July 4, 2002, 5:27 PM
 */

package org.jvnet.olt.filters.xml.xmlconfig; 


public class ProcessXmlConfigException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ProcessXmlConfigException</code> without detail message.
     */
    public ProcessXmlConfigException() {
    }
    
    
    /**
     * Constructs an instance of <code>ProcessXmlConfigException</code> 
     * with the specified detail message.
     * @param msg the detail message.
     */
    public ProcessXmlConfigException(String msg) {
        super(msg);
    }
}
