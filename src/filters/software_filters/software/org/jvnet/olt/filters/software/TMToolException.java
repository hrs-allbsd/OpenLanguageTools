
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TMToolException.java
 *
 * Created on March 7, 2003, 3:19 PM
 */

package org.jvnet.olt.filters.software;

/**
 *
 * @author  timf
 */
public class TMToolException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>TMToolException</code> without detail message.
     */
    public TMToolException() {
    }
    
    
    /**
     * Constructs an instance of <code>TMToolException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TMToolException(String msg) {
        super(msg);
    }
}
