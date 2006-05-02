/*
 * SOXliffBackException.java
 *
 * Created on April 30, 2006, 6:29 PM
 *
 */

package org.jvnet.olt.soxliff_backconv;

/**
 * Exception throwed by StarOffice Xliff filter
 * 
 * @author michal
 */
public class SOXliffBackException extends Exception {
    
    /**
     * Construct new SOXliffException with detailed message
     *
     * @param description of the cause
     *
     */
    public SOXliffBackException(String message) {
        super(message);
    }
 
    /**
     * Construct new SOXliffException with detailed message and
     * cause of the exception
     *
     * @param description detailed message
     * @param cause of the exception
     */
    public SOXliffBackException(String message,Throwable cause) {
        super(message,cause);
    }
}
