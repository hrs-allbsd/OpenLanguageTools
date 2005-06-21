
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlBackConversionCommandException.java
 *
 * Created on February 10, 2005, 11:24 AM
 */

package org.jvnet.olt.xliff_back_converter.format.xml;

/**
 *
 * @author timf
 */
public class XmlBackConversionCommandException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>XmlBackConversionCommandException</code> without detail message.
     */
    public XmlBackConversionCommandException() {
    }
    
    
    /**
     * Constructs an instance of <code>XmlBackConversionCommandException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XmlBackConversionCommandException(String msg) {
        super(msg);
    }
}
