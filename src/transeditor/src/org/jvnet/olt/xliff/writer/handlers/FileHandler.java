/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * FileHandler.java
 *
 * Created on April 26, 2005, 2:13 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.io.IOException;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.handlers.Element;
import org.jvnet.olt.xliff.handlers.Handler;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
public class FileHandler extends BaseHandler {
    String targetLanguage;

    public FileHandler(Context ctx) {
        super(ctx);

        targetLanguage = ctx.getTargetLang();
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws ReaderException {
        Element e1 = element;

        if (start) {
            Attributes attrs = element.getAttrs();

            if ("file".equals(element.getLocalName()) && !targetLanguage.equals(attrs.getValue("target-language"))) {
                AttributesImpl impl = new AttributesImpl(attrs);
                setAttributeValue(impl, "target-language", targetLanguage);

                e1 = new Element(element.getPrefix(), element.getLocalName(), element.getOriginalQName(), impl, element.getPath());
            }
        }

        writeElement(e1, start);
    }

    public void dispatchIgnorableChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public void dispatchChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public boolean handleSubElements() {
        return true;
    }
}
