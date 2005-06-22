/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * PhaseHandler.java
 *
 * Created on April 18, 2005, 5:39 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/** Handles phase tag.
 *
 * Only implemented for for single &lt;phase&gt; tag in &lt;phase-group&gt;
 *
 * @author boris
 */
public class PhaseHandler extends BaseHandler {
    /** Creates a new instance of PhaseHandler */
    public PhaseHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            ctx.setProcessName(element.getAttrs().getValue("process-name"));
            ctx.setPhaseName(element.getAttrs().getValue("phase-name"));

            String tool = element.getAttrs().getValue("tool");

            if (tool != null) {
                ctx.setToolName(tool);
            }
        }
    }
}
