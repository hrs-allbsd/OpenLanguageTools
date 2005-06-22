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

                if ((state != null) && state.startsWith("x-")) {
                    state = state.substring(2);
                }
            } else {
                postAction();
            }
        }
    }

    protected void postAction() {
        ctx.addTarget(xmlLang, sb.toString(), state);
    }

    public boolean handleSubElements() {
        return true;
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) {
        sb.append(chars, start, length);
    }
}
