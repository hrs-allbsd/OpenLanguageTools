
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

package org.jvnet.olt.filters.html;

/**
 *
 * @author  timf
 */
public class HtmlParserException extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>HtmlParserException</code> without detail message.
     */
    public HtmlParserException() {
    }
    
    
    /**
     * Constructs an instance of <code>HtmlParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public HtmlParserException(String msg) {
        super(msg);
    }
}
