/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * AltTransContextGroupHandler.java
 *
 * Created on April 19, 2005, 5:11 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/**
 *
 * @author boris
 */
public class AltTransContextGroupHandler extends MapHandler {
    /** Creates a new instance of AltTransContextGroupHandler */
    public AltTransContextGroupHandler(Context ctx) {
        super(ctx, "context-group", "name", "context", "context-type");
    }

    protected void postAction() {
        if ("SunTrans Attributes".equals(groupName)) {
            ctx.addAltTransContext(groupName, map);
        }
    }
}
