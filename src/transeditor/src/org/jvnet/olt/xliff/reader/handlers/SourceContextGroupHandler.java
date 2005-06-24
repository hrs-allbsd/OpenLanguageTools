/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SourceContextGroupHandler.java
 *
 * Created on April 19, 2005, 4:45 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/**
 *
 * @author boris
 */
public class SourceContextGroupHandler extends MapHandler {
    /** Creates a new instance of SourceContextGroupHandler */
    public SourceContextGroupHandler(Context ctx) {
        super(ctx, "context-group", "name", "context", "context-type");
    }

    protected void postAction() {
        if ((groupName != null)) {
            ctx.addSourceContext(groupName, map);
        }
    }
}
