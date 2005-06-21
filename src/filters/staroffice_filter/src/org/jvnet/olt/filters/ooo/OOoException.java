
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * OOoException.java
 *
 * Created on February 8, 2005, 10:12 AM
 */

package org.jvnet.olt.filters.ooo;

/**
 *
 * @author timf
 */
public class OOoException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>OOoException</code> without detail message.
     */
    public OOoException() {
    }
    
    
    /**
     * Constructs an instance of <code>OOoException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public OOoException(String msg) {
        super(msg);
    }
}
