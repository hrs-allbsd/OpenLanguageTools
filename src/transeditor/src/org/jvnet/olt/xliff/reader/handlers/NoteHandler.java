/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * NoteHandler.java
 *
 * Created on April 18, 2005, 5:46 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import org.jvnet.olt.xliff.Note;


/** Handles not element.
 *
 *  Pushes header, trans-unit or alt-trans-unit note to the context.
 *
 * @author boris
 */
public class NoteHandler extends TextHandler {
    public static final int NOTE_HEADER = 0;
    public static final int NOTE_TRANS_UNIT = 1;
    public static final int NOTE_ALT_TRANS_UNIT = 2;
    final private int type;

    /** Creates a new instance of NoteHandler */
    public NoteHandler(Context ctx, int type) {
        super(ctx);
        this.type = type;

        if ((type < 0) || (type > 2)) {
            throw new IllegalStateException("Wrong note type:" + type);
        }
    }

    protected void postAction() {
        //do not create empty notes
        if ( text.toString().length() > 0) {
            Note n = new Note(text.toString());

            switch (type) {
            case NOTE_HEADER:
                ctx.addHeaderNote(n);

                break;

            case NOTE_TRANS_UNIT:
                ctx.addTransUnitNote(n);

                break;

            case NOTE_ALT_TRANS_UNIT:
                ctx.addAltTransUnitNote(n);

                break;
            }
        }
    }
}
