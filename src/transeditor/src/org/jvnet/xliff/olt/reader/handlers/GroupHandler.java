/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * GroupHandler.java
 *
 * Created on April 19, 2005, 10:07 AM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.olt.xliff.Group;


/** Handles group element under body element.
 *
 * Handles groups of depth 1 under body. Nested groups are not supported.
 *
 * @author boris
 */
public class GroupHandler extends BaseHandler {
    /** Creates a new instance of GroupHandler */
    public GroupHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            String id = element.getAttrs().getValue("id");

            if ((id != null) && (id.trim().length() > 0)) {
                Map attributesMap = new HashMap();
                int numberOfAttributes = element.getAttrs().getLength();

                for (int i = 0; i < numberOfAttributes; i++) {
                    attributesMap.put(element.getAttrs().getQName(i), element.getAttrs().getValue(i));
                }

                Group group = new Group(id, attributesMap);
                ctx.addGroup(group);
            }
        } else {
            ctx.commitGroup();
        }
    }
}
