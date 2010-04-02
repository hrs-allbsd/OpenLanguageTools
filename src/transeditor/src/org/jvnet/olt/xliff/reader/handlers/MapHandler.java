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
 * MapHandler.java
 *
 * Created on April 19, 2005, 1:20 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.HashMap;


/**
 *
 * @author boris
 */
abstract public class MapHandler extends BaseHandler {
    protected HashMap map;
    protected String propGroupTag;
    protected String propTag;
    protected String keyAttrTag;
    protected String propGroupNameAttr;
    protected String groupName;
    private String propName;
    private StringBuffer sb;
    private boolean inAttr;

    /** Creates a new instance of MapHandler */
    public MapHandler(Context ctx, String propGroupTag, String propGroupNameAttr, String propTag, String keyAttrTag) {
        super(ctx);

        this.propGroupTag = propGroupTag;
        this.propTag = propTag;
        this.keyAttrTag = keyAttrTag;
        this.propGroupNameAttr = propGroupNameAttr;
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (propGroupTag.equals(element.getQName())) {
            if (start) {
                groupName = element.getAttrs().getValue(propGroupNameAttr);
                map = new HashMap();
            } else {
                postAction();
            }
        }

        if (propTag.equals(element.getQName())) {
            inAttr = start;

            if (start) {
                propName = element.getAttrs().getValue(keyAttrTag);
                sb = new StringBuffer();
            } else {
                if (propName != null) {
                    map.put(propName, sb.toString());
                }
            }
        }
    }

    abstract protected void postAction();

    public boolean handleSubElements() {
        return true;
    }

    public void dispatchChars(org.jvnet.olt.xliff.handlers.Element element, char[] chars, int start, int length) {
        if (inAttr) {
            sb.append(chars, start, length);
        }
    }
}
