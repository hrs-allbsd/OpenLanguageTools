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
package org.jvnet.olt.editor.model;

public class Tag {
    String tagname;
    String level;
    String reserve;
    String editable;
    String endtag;
    boolean isEndtag;
    boolean visible;

    public Tag(String tagname, String level, String reserve, String editable, String endtag, boolean isEndtag, String visible) {
        this.tagname = tagname;
        this.level = level;
        this.reserve = reserve;
        this.editable = editable;
        this.endtag = endtag;
        this.isEndtag = isEndtag;
        this.visible = visible.equals("Y") ? true : false;
    }

    public String getTagname() {
        return tagname.toUpperCase();
    }

    public String getLevel() {
        return level.toUpperCase();
    }

    public String getReserve() {
        return reserve.toUpperCase();
    }

    public String getEditable() {
        return editable.toUpperCase();
    }

    public String getEndtag() {
        return endtag.toUpperCase();
    }

    public boolean isEndtag() {
        return isEndtag;
    }

    public boolean isVisible() {
        return visible;
    }
}
