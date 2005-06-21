
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SpecificBackConverterException.java
 *
 * Created on August 1, 2003, 12:26 PM
 */

package org.jvnet.olt.xliff_back_converter;

/**
 *
 * @author  timf
 */
public class SpecificBackConverterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SpecificBackConverterException</code> without detail message.
     */
    public SpecificBackConverterException() {
    }
    
    
    /**
     * Constructs an instance of <code>SpecificBackConverterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SpecificBackConverterException(String msg) {
        super(msg);
    }
}
