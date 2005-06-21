/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * InvalidFormatTypeException.java
 *
 * Created on 06 January 2004, 15:44
 */
package org.jvnet.olt.editor.format;


/**
 *
 * @author  jc73554
 */
public class InvalidFormatTypeException extends java.lang.Exception {
    /**
     * Creates a new instance of <code>InvalidFormatTypeException</code> without detail message.
     */
    public InvalidFormatTypeException() {
    }

    /**
     * Constructs an instance of <code>InvalidFormatTypeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidFormatTypeException(String msg) {
        super(msg);
    }
}
