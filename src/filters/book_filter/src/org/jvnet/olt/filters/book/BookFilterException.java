
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BookFilterException.java
 *
 * Created on November 2, 2004, 4:37 PM
 */

package org.jvnet.olt.filters.book;

/**
 *
 * @author  timf
 */
public class BookFilterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>BookFilterException</code> without detail message.
     */
    public BookFilterException() {
    }
    
    
    /**
     * Constructs an instance of <code>BookFilterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BookFilterException(String msg) {
        super(msg);
    }
}
