
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_tmx_converter;

import org.xml.sax.SAXException;

/**
 * Exception thrown when a XLIFF to TMX transformation fails.
 *
 * @author Brian Kidney
 * @created 27 August 2002
 */
public class XliffTargetNotFoundException extends SAXException {

    /**
     * Constructor for the XliffTargetNotFoundException object
     */
    public XliffTargetNotFoundException() { 
        super("Translated target element not found in a trans-unit, or the translation has been rejected in review.");
    }


    /**
     * Constructor for the XliffTargetNotFoundException object
     *
     * @param msg A message to be included with a 
     *        XliffTargetNotFoundException
     */
    public XliffTargetNotFoundException(String msg) {
        super(msg);
    }
}

