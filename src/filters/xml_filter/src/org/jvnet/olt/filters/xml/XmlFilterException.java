
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlFilterException.java
 *
 * Created on November 7, 2003, 2:11 PM
 */

package org.jvnet.olt.filters.xml;

/**
 *
 * @author  timf
 */
public class XmlFilterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>XmlFilterException</code> without detail message.
     */
    public XmlFilterException() {
    }
    
    
    /**
     * Constructs an instance of <code>XmlFilterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XmlFilterException(String msg) {
        super(msg);
    }
}
