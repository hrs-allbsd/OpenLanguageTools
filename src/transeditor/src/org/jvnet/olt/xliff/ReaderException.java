/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ReaderException.java
 *
 * Created on April 22, 2005, 12:52 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class ReaderException extends java.lang.Exception {
    /**
     * Creates a new instance of <code>ReaderException</code> without detail message.
     */
    public ReaderException() {
    }

    /**
     * Constructs an instance of <code>ReaderException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ReaderException(String msg) {
        super(msg);
    }

    public ReaderException(Throwable th) {
        super(th);
    }
}
