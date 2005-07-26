/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SourceHandler.java
 *
 * Created on April 26, 2005, 2:15 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.util.Map;

import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.XLIFFSentence;


/**
 *
 * @author boris
 */
public class SourceHandler extends BaseHandler {
    private Map srcChangeSet;
    private XLIFFSentence currentSntnc;
    private boolean ignoreChars;

    /** Creates a new instance of SourceHandler */
    public SourceHandler(Context ctx) {
        super(ctx);
        this.srcChangeSet = ctx.getSrcChangeSet();
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if ("source".equals(element.getQName())) {
            if (start) {
                String transUnitId = ctx.getCurrentTransId();

                if (srcChangeSet.containsKey(transUnitId)) {
                    currentSntnc = (XLIFFSentence)srcChangeSet.get(transUnitId);
                }

                if (ctx.getVersion().isXLIFF11()) {
                    element.addNamespaceDeclaration(null, Constants.XLIFF_1_1_URI);
                }
            } else {
                srcChangeSet.remove(ctx.getCurrentTransId());
                currentSntnc = null;
            }

            ignoreChars = currentSntnc != null;

            writeElement(element, start);
        }
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if (currentSntnc != null) {
            char[] ch = currentSntnc.getSentence().toCharArray();
            writeChars(ch, 0, ch.length,false );
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

    public boolean handleSubElements() {
        return true;
    }
}
