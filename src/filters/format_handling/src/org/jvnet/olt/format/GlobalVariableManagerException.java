
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * GlobalVariableManagerException.java
 *
 * Created on May 6, 2003, 1:20 PM
 */

package org.jvnet.olt.format;

/**
 *
 * @author  timf
 */
public class GlobalVariableManagerException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>GlobalVariableManagerException</code> without detail message.
     * This exception can be thrown in case of any problem with resolving variables or other
     * exceptional circumstances.
     */
    public GlobalVariableManagerException() {
    }
    
    
    /**
     * Constructs an instance of <code>GlobalVariableManagerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public GlobalVariableManagerException(String msg) {
        super(msg);
    }
}
