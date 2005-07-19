/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.csv;

/**
 *
 * @author timf
 */
public class CsvParserException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>CsvParserException</code> without detail message.
     */
    public CsvParserException() {
    }
    
    
    /**
     * Constructs an instance of <code>CsvParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CsvParserException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>CsvParserException</code> with the specified detail message
     * and initial cause.
     * @param msg the detail message.
     * @param t the initial cause of this exception
     */
    public CsvParserException(String msg, Throwable t){
        super(msg);
        this.initCause(t);
    }
    
}
