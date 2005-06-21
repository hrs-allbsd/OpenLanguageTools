
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.backconv;

/**
 * Exception thrown when a XLIFF back conversion fails.
 *
 * @author Brian Kidney
 * @created 05 September 2002
 */
public class BackConverterException extends Exception {

    /**
     * Constructor for the XliffBackConverterException object
     */
    public BackConverterException() { }


    /**
     * Constructor for the XliffBackConverterException object
     *
     * @param msg A message to be included with a 
     * XliffBackConverterException
     */
    public BackConverterException(String msg) {
        super(msg);
    }
}

