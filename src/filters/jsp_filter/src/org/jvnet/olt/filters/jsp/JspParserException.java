
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * JspParserException.java
 *
 * Created on December 10, 2003, 3:22 PM
 */

package org.jvnet.olt.filters.jsp;

/**
 *
 * @author  timf
 */
public class JspParserException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>JspParserException</code> without detail message.
     */
    public JspParserException() {
    }
    
    
    /**
     * Constructs an instance of <code>JspParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JspParserException(String msg) {
        super(msg);
    }
}
