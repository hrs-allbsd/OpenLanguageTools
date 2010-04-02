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
            unit.setApproved (element.getAttrs().getValue("approved"));

            ctx.addTransUnit(unit);
        } else {
            ctx.commitTransUnit();
        }
    }
}
