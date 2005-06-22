/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TextHandler.java
 *
 * Created on April 19, 2005, 11:18 AM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/**
 *
 * @author boris
 */
abstract public class TextHandler extends BaseHandler {
    protected StringBuffer text;

    /** Creates a new instance of TextHandler */
    public TextHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            text = new StringBuffer();
        } else {
            postAction();
        }
    }

    abstract protected void postAction();

    public boolean handleSubElements() {
        return false;
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) {
        text.append(chars, start, length);
    }
}
