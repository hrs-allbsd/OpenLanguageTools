/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

public class SpecialStrIndex {
    int index = -1;
    int len = 0;

    public SpecialStrIndex(int index, int len) {
        this.index = index;
        this.len = len;
    }

    public int index() {
        return index;
    }

    public void set(int index, int len) {
        this.index = index;
        this.len = len;
    }

    public int length() {
        return len;
    }
}
