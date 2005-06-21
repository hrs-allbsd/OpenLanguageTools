
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlParserException.java
 *
 * Created on July 4, 2002, 5:12 PM
 */

package org.jvnet.olt.filters.sgml;

/**
 *
 * @author  timf
 */
public class SgmlFilterException extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>HtmlParserException</code> without detail message.
     */
    public SgmlFilterException() {
    }
    
    
    /**
     * Constructs an instance of <code>HtmlParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SgmlFilterException(String msg) {
        super(msg);
    }
}
