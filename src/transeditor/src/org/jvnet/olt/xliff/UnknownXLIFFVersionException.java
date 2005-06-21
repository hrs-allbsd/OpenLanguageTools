/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * UnknownXLIFFVersionException.java
 *
 * Created on April 22, 2005, 12:50 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.xliff;

import org.xml.sax.SAXException;


/**
 *
 * @author boris
 */
public class UnknownXLIFFVersionException extends ReaderException {
    /**
     * Creates a new instance of <code>UnknownXLIFFVersionException</code> without detail message.
     */
    public UnknownXLIFFVersionException() {
    }

    /**
     * Constructs an instance of <code>UnknownXLIFFVersionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownXLIFFVersionException(String msg) {
        super(msg);
    }
}
