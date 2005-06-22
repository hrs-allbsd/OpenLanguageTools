/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TransUnitHandler.java
 *
 * Created on April 19, 2005, 11:03 AM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import org.jvnet.olt.xliff.TransUnit;
import org.jvnet.olt.xliff.TransUnitId;


/**
 *
 * @author boris
 */
public class TransUnitHandler extends BaseHandler {
    TransUnit unit;

    /** Creates a new instance of TransUnitHandler */
    public TransUnitHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            String id = element.getAttrs().getValue("id");
            TransUnitId tuId = ctx.createTransUnitKey(id);

            unit = new TransUnit(tuId);

            ctx.addTransUnit(unit);
        } else {
            ctx.commitTransUnit();
        }
    }
}
