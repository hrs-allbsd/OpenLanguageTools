/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * NoteHandler.java
 *
 * Created on April 26, 2005, 4:03 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.io.IOException;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.TrackingComments;


/**
 *
 * @author boris
 */
public class NoteHandler extends BaseHandler {
    private boolean header;
    private String note;
    private boolean ignoreChars;

    /** Creates a new instance of NoteHandler */
    public NoteHandler(Context ctx, boolean header) {
        super(ctx);
        this.header = header;
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if (start) {
            TrackingComments tc = ctx.getTrackingComments();

            String key = ctx.getCurrentTransId();

            if (header) {
                key = "header";
            }

            if (tc.isCommentModified(key)) {
                note = tc.getComment(key);
            }

            ignoreChars = note != null;
        } else {
            note = null;

            TrackingComments tc = ctx.getTrackingComments();
            tc.setCommentModified(header ? "header" : ctx.getCurrentTransId(), false);
        }

        writeElement(element, start);
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if (note != null) {
            char[] ch = note.toCharArray();
            writeChars(ch, 0, ch.length);
            note = null;
        }

        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }

    public void dispatchIgnorableChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if (!ignoreChars) {
            writeChars(chars, start, length);
        }
    }
}
