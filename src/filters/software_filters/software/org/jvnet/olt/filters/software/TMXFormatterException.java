
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TMXFormatterException.java
 *
 * Created on November 9, 2003, 12:08 PM
 */

package org.jvnet.olt.filters.software;

/**
 *
 * @author  timf
 */
public class TMXFormatterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>TMXFormatterException</code> without detail message.
     */
    public TMXFormatterException() {
    }
    
    
    /**
     * Constructs an instance of <code>TMXFormatterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TMXFormatterException(String msg) {
        super(msg);
    }
}
