/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * PropHandler.java
 *
 * Created on April 18, 2005, 6:12 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/** Handles only prop-group and prop elements under alt-trans element.
 *
 * It handles only something like this:
 * &lt;prop-group name="format-penalty"&gt;<br>
 *    &lt;prop name="format-diff-penalty"&gt;X&lt;/prop&gt;<br>
 * &lt;/prop-group&gt;<br>
 * or
 * &lt;prop-group name="match"&gt;<br>
 *    &lt;prop &gt;X&lt;/prop&gt;<br>
 * &lt;/prop-group&gt;<br>
 *
 * @author boris
 */
public class PropGroupHandler extends MapHandler {
    /** Creates a new instance of PropHandler */
    public PropGroupHandler(Context ctx) {
        super(ctx, "prop-group", "name", "prop", "prop-type");
    }

    protected void postAction() {
        if ("format penalty".equals(groupName)) {
            ctx.setFormatDiffInfo((String)map.get("format-diff-penalty"));
        } else if ("match".equals(groupName)) {
            ctx.setMatchType((String)map.get("matchtype"));
        }
    }
}
