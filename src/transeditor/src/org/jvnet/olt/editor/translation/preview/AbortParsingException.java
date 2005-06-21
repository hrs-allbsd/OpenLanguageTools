/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation.preview;

import org.xml.sax.SAXException;


/**
 * User: boris
 * Date: Feb 11, 2005
 * Time: 9:57:17 AM
 */
public class AbortParsingException extends SAXException {
    final private boolean success;

    public AbortParsingException(boolean success) {
        super(success ? "success" : "failure");
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
