
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_back_converter;

/**
 * Exception thrown when a XLIFF back conversion fails.
 *
 * @author Brian Kidney
 * @created 05 September 2002
 */
public class XliffBackConverterException extends org.jvnet.olt.backconv.BackConverterException {

    /**
     * Constructor for the XliffBackConverterException object
     */
    public XliffBackConverterException() { }


    /**
     * Constructor for the XliffBackConverterException object
     *
     * @param msg A message to be included with a 
     * XliffBackConverterException
     */
    public XliffBackConverterException(String msg) {
        super(msg);
    }
}

