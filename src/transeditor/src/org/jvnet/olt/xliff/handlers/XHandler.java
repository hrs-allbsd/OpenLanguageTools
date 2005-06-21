/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XHandler.java
 *
 * Created on April 21, 2005, 2:45 PM
 *
 */
package org.jvnet.olt.xliff.handlers;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author boris
 */
public class XHandler extends Handler {
    String text;
    String name;
    String ctxName;
    Map m = new HashMap();

    /** Creates a new instance of XHandler */
    public XHandler() {
    }

    public void dispatch(Element element, boolean start) {
        if (element.getQName().equals("context-group")) {
            if (start) {
                ctxName = element.getAttrs().getValue("name");
                m.clear();
            } else {
                System.out.println("Context:" + ctxName);
                System.out.println(m);
            }

            return;
        }

        if (element.getQName().equals("context")) {
            if (start) {
                this.name = element.getAttrs().getValue("context-type");
            } else {
                m.put(this.name, this.text.trim());
                text = "";
            }
        }
    }

    public void dispatch(Element element, String text, boolean ignorable) {
        if (ignorable) {
            return;
        }

        if ("context".equals(element.getQName())) {
            //this.text += text.trim();
        }
    }
}
