
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BookException.java
 *
 * Created on October 7, 2003, 2:40 PM
 */

package org.jvnet.olt.filters.book;

/**
 *
 * @author  timf
 */
public class BookException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>BookException</code> without detail message.
     */
    public BookException() {
    }
    
    
    /**
     * Constructs an instance of <code>BookException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BookException(String msg) {
        super(msg);
    }
}
