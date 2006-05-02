/*
 * SOXliffException.java
 *
 * Created on April 30, 2006, 6:29 PM
 *
 */

package org.jvnet.olt.filters.soxliff;

/**
 * Exception throwed by StarOffice Xliff filter
 * 
 * @author michal
 */
public class SOXliffException extends Exception {
    
    /**
     * Construct new SOXliffException with detailed message
     *
     * @param description of the cause
     *
     */
    public SOXliffException(String message) {
        super(message);
    }
 
    /**
     * Construct new SOXliffException with detailed message and
     * cause of the exception
     *
     * @param description detailed message
     * @param cause of the exception
     */
    public SOXliffException(String message,Throwable cause) {
        super(message,cause);
    }
}
