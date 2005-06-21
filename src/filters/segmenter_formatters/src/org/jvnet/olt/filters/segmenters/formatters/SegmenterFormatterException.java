
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SegmenterFormatterException.java
 *
 * Created on July 4, 2002, 5:27 PM
 */

package org.jvnet.olt.filters.segmenters.formatters;

/**
 *
 * @author  timf
 */
public class SegmenterFormatterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SegmenterFormatterException</code> without detail message.
     */
    public SegmenterFormatterException() {
    }
    
    
    /**
     * Constructs an instance of <code>SegmenterFormatterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SegmenterFormatterException(String msg) {
        super(msg);
    }
}
