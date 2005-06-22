/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * AltTransHandler.java
 *
 * Created on April 19, 2005, 5:58 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;


/**
 *
 * @author boris
 */
public class AltTransHandler extends BaseHandler {
    String matchQuality;

    /** Creates a new instance of AltTransHandler */
    public AltTransHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            matchQuality = element.getAttrs().getValue("match-quality");

            if (matchQuality == null) {
                matchQuality = "0";
            }

            ctx.addAltTrans(matchQuality);
        } else {
            ctx.commitAltTrans();
        }
    }
}
