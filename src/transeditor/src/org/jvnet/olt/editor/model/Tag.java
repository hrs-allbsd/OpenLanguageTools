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
