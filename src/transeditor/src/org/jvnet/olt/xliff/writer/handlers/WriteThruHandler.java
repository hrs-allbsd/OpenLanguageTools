/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * WriteThruHandler.java
 *
 * Created on April 26, 2005, 3:13 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.io.IOException;

import org.jvnet.olt.xliff.ReaderException;


/**
 *
 * @author boris
 */
public class WriteThruHandler extends BaseHandler {
    private BaseHandler delegate;

    /** Creates a new instance of WriteThruHandler */
    public WriteThruHandler(Context ctx, BaseHandler delegate) {
        super(ctx);

        this.delegate = delegate;
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if (start) {
            writeElement(element, start);

            if (delegate != null) {
                delegate.dispatch(element, start);
            }
        } else {
            if (delegate != null) {
                delegate.dispatch(element, start);
            }

            writeElement(element, start);
        }
    }

    public void dispatchIgnorableChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        writeChars(chars, start, length);

        if (delegate != null) {
            delegate.dispatchIgnorableChars(element, chars, start, length);
        }
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        writeChars(chars, start, length);

        if (delegate != null) {
            delegate.dispatchChars(element, chars, start, length);
        }
    }

    public boolean handleSubElements() {
        return true;
    }
}
