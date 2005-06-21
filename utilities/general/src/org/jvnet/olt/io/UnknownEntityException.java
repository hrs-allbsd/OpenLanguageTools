
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * UnknownEntityException.java
 *
 * Created on 23 September 2003, 11:47
 */

package org.jvnet.olt.io;

/**
 *
 * @author  jc73554
 */
public class UnknownEntityException extends java.io.IOException {
    
    /** Creates a new instance of UnknownEntityException
     * @param message Message to display with the exception.
     */
    public UnknownEntityException(String message) {
        super(message);
    }
    
    /** Creates a new instance of UnknownEntityException */
    public UnknownEntityException() {
        super();
    }
}
