
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PlaintextParserException.java
 *
 * Created on September 16, 2002, 3:29 PM
 */

package org.jvnet.olt.filters.plaintext;

/**
 *
 * @author  timf
 */
public class PlaintextParserException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>PlaintextParserException</code> without detail message.
     */
    public PlaintextParserException() {
    }
    
    
    /**
     * Constructs an instance of <code>PlaintextParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PlaintextParserException(String msg) {
        super(msg);
    }
}
