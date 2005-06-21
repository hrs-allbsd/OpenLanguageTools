/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ConfigurationException.java
 *
 * Created on March 24, 2005, 11:19 AM
 */
package org.jvnet.olt.editor.translation;


/**
 *
 * @author boris
 */
public class ConfigurationException extends java.lang.Exception {
    /**
     * Creates a new instance of <code>ConfigurationException</code> without detail message.
     */
    public ConfigurationException() {
    }

    /**
     * Constructs an instance of <code>ConfigurationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(Throwable th) {
        super(th);
    }

    public ConfigurationException(String cause, Throwable th) {
        super(cause, th);
    }
}
