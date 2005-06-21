/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Note.java
 *
 * Created on April 18, 2005, 5:35 PM
 *
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class Note {
    private String text;

    /** Creates a new instance of Note */
    public Note(String text) {
        this.setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
