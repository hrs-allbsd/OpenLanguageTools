/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TargetHandler.java
 *
 * Created on April 26, 2005, 2:42 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.Map;

import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.XLIFFSentence;
import org.jvnet.olt.xliff.handlers.Element;

import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
public class TargetHandler extends BaseHandler {
    private Map tgtChangeSet;
    private XLIFFSentence currentSntnc;
    private boolean ignoreChars;

    /** Creates a new instance of TargetHandler */
    public TargetHandler(Context ctx) {
        super(ctx);

        tgtChangeSet = ctx.getTgtChangeSet();
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if ("target".equals(element.getQName())) {
            if (start) {
                String transUnitId = ctx.getCurrentTransId();

                if (tgtChangeSet.containsKey(transUnitId)) {
                    currentSntnc = (XLIFFSentence)tgtChangeSet.get(transUnitId);

                    String state = currentSntnc.getTranslationState();

                    if (ctx.getVersion().isXLIFF11()) {
                        state = "x-" + state;
                    }

                    AttributesImpl attrs = new AttributesImpl(element.getAttrs());
                    setAttributeValue(attrs, "state", state);
                    setAttributeValue(attrs, "xml:lang", ctx.getTargetLang());

                    element = new Element(element.getPrefix(), element.getLocalName(), element.getOriginalQName(), attrs, element.getPath());

                    if (ctx.getVersion().isXLIFF11()) {
                        element.addNamespaceDeclaration(null, Constants.XLIFF_1_1_URI);
                    }
                }
            } else {
                tgtChangeSet.remove(ctx.getCurrentTransId());
                currentSntnc = null;
            }

            ignoreChars = currentSntnc != null;

            writeElement(element, start);
        }
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if (currentSntnc != null) {
            char[] ch = currentSntnc.getSentence().toCharArray();
            writeChars(ch, 0, ch.length, false);
            currentSntnc = null;
        }

        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }

    public void dispatchIgnorableChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws ReaderException {
        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }
}
