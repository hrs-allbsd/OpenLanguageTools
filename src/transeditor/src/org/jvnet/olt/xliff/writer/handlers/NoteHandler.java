/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
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
    private boolean remove;

    /** Creates a new instance of NoteHandler */
    public NoteHandler(Context ctx, boolean header) {
        super(ctx);
        this.header = header;
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if (start) {
            remove = false;
            
            TrackingComments tc = ctx.getTrackingComments();

            String key = ctx.getCurrentTransId().getStrId();

            if (header) {
                key = "header";
            }

            if (tc.isCommentModified(key)) {
                note = tc.getComment(key);
                remove = note == null;
            }
            
            ignoreChars = note != null;
        } else {
            // if note content has not been written yet - write it and set to null
            if ( note != null) {
                char[] ch = note.toCharArray();
                writeChars(ch, 0, ch.length);
                note = null;
            }

            TrackingComments tc = ctx.getTrackingComments();
            tc.setCommentModified(header ? "header" : ctx.getCurrentTransId().getStrId(), false);
        }

        if(!remove)
            writeElement(element, start);
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        if(remove)
            return;

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
