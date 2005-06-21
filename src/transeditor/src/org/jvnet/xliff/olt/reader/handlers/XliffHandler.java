/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XliffHandler.java
 *
 * Created on April 21, 2005, 3:28 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.Version;


/**
 *
 * @author boris
 */
public class XliffHandler extends BaseHandler {
    /** Creates a new instance of XliffHandler */
    public XliffHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws ReaderException {
        if (start) {
            Version v = Version.fromString(element.getAttrs().getValue("version"));

            ctx.addXLIFF(v);
        } else {
            ctx.commitXLIFF();
        }
    }
}
