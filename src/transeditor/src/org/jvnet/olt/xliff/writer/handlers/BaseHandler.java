/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * BaseHandler.java
 *
 * Created on April 26, 2005, 2:31 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.io.IOException;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.XMLDumper;
import org.jvnet.olt.xliff.handlers.Element;
import org.jvnet.olt.xliff.handlers.Handler;

import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
abstract public class BaseHandler extends Handler {
    final protected Context ctx;
    final private XMLDumper dumper;

    /** Creates a new instance of BaseHandler */
    public BaseHandler(Context ctx) {
        this.ctx = ctx;
        this.dumper = ctx.getDumper();
    }

    protected void writeElement(Element element, boolean start) throws ReaderException {
        try {
            dumper.writeElement(element, start);
        } catch (IOException ioe) {
            throw new ReaderException(ioe);
        }
    }

    protected void writeChars(char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length, true);
    }

    protected void writeChars(char[] chars, int start, int length, boolean escapeHTML) throws ReaderException {
        try {
            dumper.writeChars(chars, start, length, escapeHTML);
        } catch (IOException ioe) {
            throw new ReaderException(ioe);
        }
    }

    protected void setAttributeValue(AttributesImpl attrs, String attrName, String attrValue) {
        int idx = attrs.getIndex(attrName);

        if (idx == -1) {
            attrs.addAttribute(null, attrName, attrName, null, attrValue);
        } else {
            attrs.setValue(idx, attrValue);
        }
    }
}
