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
