
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffSubSegmenterException.java
 *
 * Created on October 4, 2004, 3:01 PM
 */

package org.jvnet.olt.filters.xliffsubsegment;

/**
 *
 * @author  timf
 */
public class XliffSubSegmenterException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>XliffSubSegmenterException</code> without detail message.
     */
    public XliffSubSegmenterException() {
    }
    
    /**
     * Creates a new instance of <code>XliffSubSegmenterException</code> with a specified
     * detail message, and a root cause from the given exception.
     */
    public XliffSubSegmenterException(String msg,Exception e){
        super(msg);
        this.initCause(e);
    }
    
    /**
     * Constructs an instance of <code>XliffSubSegmenterException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XliffSubSegmenterException(String msg) {
        super(msg);
    }
}
