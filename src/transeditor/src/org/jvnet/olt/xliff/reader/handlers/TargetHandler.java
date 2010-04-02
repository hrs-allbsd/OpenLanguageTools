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
 * TargetHandler.java
 *
 * Created on April 19, 2005, 12:51 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/**
 *
 * @author boris
 */
public class TargetHandler extends BaseHandler {
    StringBuffer sb;
    String state;
    String stateQualifier;
    String xmlLang;

    /** Creates a new instance of TargetHandler */
    public TargetHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        //if("target".equals(element.getQName())){
        if ("target".equals(element.getQName())) {
            if (start) {
                sb = new StringBuffer();
                state = element.getAttrs().getValue("state");
                xmlLang = element.getAttrs().getValue("xml:lang");
                stateQualifier = element.getAttrs().getValue("state-qualifier");

                if ( this.ctx.getVersion().isXLIFF10() ) {
                    // old XLIFF 1.0 combined state
                    if ((state != null) && state.startsWith("x-")) {
                        state = state.substring(2);
                        stateQualifier = null;
                    }
                } else if ( (state != null) ) {
                    if ( state.startsWith("x-")) {
                        if ( state.substring(2).contains(":") ) {
                            //try to split the old combined state
                            stateQualifier=state.substring(2).split(":")[0];
                            state=state.substring(2).split(":")[1];
                        }
                    }
                }
            } else {
                postAction();
            }
        }
    }

    protected void postAction() {
        ctx.addTarget(xmlLang, sb.toString(), state, stateQualifier);
    }

    public boolean handleSubElements() {
        return true;
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) {
        sb.append(chars, start, length);
    }
}
