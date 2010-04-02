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
 * HeaderHandler.java
 *
 * Created on April 26, 2005, 8:09 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.TrackingComments;
import org.jvnet.olt.xliff.handlers.Element;


/**
 *
 * @author boris
 */
public class HeaderHandler extends BaseHandler {
    /** Creates a new instance of HeaderHandler */
    public HeaderHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws org.jvnet.olt.xliff.ReaderException {
        if (!start && "header".equals(element.getQName())) {
            TrackingComments tc = ctx.getTrackingComments();

            if (tc.isCommentModified("header")) {
                saveComment();
            }
        }

        writeElement(element, start);
    }

    private void saveComment() throws ReaderException {
        TrackingComments tc = ctx.getTrackingComments();
        String note = tc.getComment("header");

        Element e = new Element(null, "note", "note", null, "/");

        writeElement(e, true);
        writeChars(note.toCharArray(), 0, note.length());
        writeElement(e, false);

        tc.setCommentModified("header", false);
    }

    public void dispatchIgnorableChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        writeChars(chars, start, length);
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) throws org.jvnet.olt.xliff.ReaderException {
        writeChars(chars, start, length);
    }

    public boolean handleSubElements() {
        return true;
    }
}
