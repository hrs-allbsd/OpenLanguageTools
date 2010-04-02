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
 * SourceHandler.java
 *
 * Created on April 19, 2005, 11:43 AM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.Stack;

import org.jvnet.olt.xliff.MrkContent;


/**
 *
 * @author boris
 */
public class SourceHandler extends BaseHandler {
    private StringBuffer sb;
    private Stack inlineElementStack;
    private boolean inMark;
    private String xmlLang;

    /** Creates a new instance of SourceHandler */
    public SourceHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        //if("source".equals(element.getQName())){
        if ("source".equals(element.getQName())) {
            if (start) {
                sb = new StringBuffer();
                inlineElementStack = new Stack();
                inMark = false;

                xmlLang = element.getAttrs().getValue("xml:lang");
            } else {
                postAction();
            }
        }

        //if("mrk".equals(element.getQName())){
        if ("mrk".equals(element.getQName())) {
            inMark = start;

            if (start) {
                String mtype = element.getAttrs().getValue("mtype");
                MrkContent mrkContent = new MrkContent(mtype);
                inlineElementStack.push(mrkContent);
            } else {
                //--
                if (!inlineElementStack.isEmpty()) {
                    MrkContent mrkContent = (MrkContent)inlineElementStack.pop();
                    ctx.registerMrk(mrkContent);

                    if (!inlineElementStack.isEmpty()) {
                        MrkContent mrkParent = (MrkContent)inlineElementStack.peek();
                        mrkParent.appendMrkElement(mrkContent);
                    }

                    inlineElementStack.push(mrkContent);
                }
            }
        }
    }

    protected void postAction() {
        ctx.addSource(xmlLang, sb.toString());
    }

    public boolean handleSubElements() {
        return true;
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) {
        String text = new String(chars, start, length);
        sb.append(text);

        if (inMark && !inlineElementStack.isEmpty() && ((text != null) && (text.trim().length() != 0))) {
            MrkContent mrkContent = (MrkContent)inlineElementStack.peek();
            mrkContent.appendText(text);
        }
    }
}
