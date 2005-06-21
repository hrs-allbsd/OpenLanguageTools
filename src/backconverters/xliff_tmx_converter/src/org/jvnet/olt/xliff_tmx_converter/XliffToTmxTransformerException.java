
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_tmx_converter;

/**
 * Exception thrown when a XLIFF to TMX transformation fails.
 *
 * @author Brian Kidney
 * @created 27 August 2002
 */
public class XliffToTmxTransformerException extends Exception {

    /**
     * Constructor for the XliffToTmxTransformerException object
     */
    public XliffToTmxTransformerException() { }


    /**
     * Constructor for the XliffToTmxTransformerException object
     *
     * @param msg A message to be included with a 
     *        XliffToTmxTransformerException
     */
    public XliffToTmxTransformerException(String msg) {
        super(msg);
    }
}

