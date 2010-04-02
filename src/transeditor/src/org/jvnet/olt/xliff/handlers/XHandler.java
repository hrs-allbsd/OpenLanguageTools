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
