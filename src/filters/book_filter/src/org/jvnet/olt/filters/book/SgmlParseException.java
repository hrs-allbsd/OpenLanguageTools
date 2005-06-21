
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlParseException.java
 *
 * Created on March 1, 2005, 3:23 PM
 */

package org.jvnet.olt.filters.book;

/**
 * This is an exception that gets thrown whenever we get a JJTree/JavaCC parse
 * exception. It's of particular interest for sgml files, when we are sometimes
 * asked to parse a bit of a doctype subset by mistake : in those cases, we catch
 * throw this exception to indicate that the content isn't SGML and that the user
 * should probably try another parser.
 * @author timf
 */
public class SgmlParseException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SgmlParseException</code> without detail message.
     */
    public SgmlParseException() {
    }
    
    
    /**
     * Constructs an instance of <code>SgmlParseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SgmlParseException(String msg) {
        super(msg);
    }
}
