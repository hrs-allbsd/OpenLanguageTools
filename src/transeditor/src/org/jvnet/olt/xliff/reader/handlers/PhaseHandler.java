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
