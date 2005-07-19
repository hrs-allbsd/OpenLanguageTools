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
public class CsvToXliffException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>CsvToXliffException</code> without detail message.
     */
    public CsvToXliffException() {
    }
    
    
    /**
     * Constructs an instance of <code>CsvToXliffException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CsvToXliffException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>CsvToXliffException</code> with the specified detail message
     * and initial cause.
     * @param msg the detail message.
     * @param t the initial cause of this exception
     */
    public CsvToXliffException(String msg, Throwable t){
        super(msg);
        this.initCause(t);
    }
}
