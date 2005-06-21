/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * MrkContent.java
 *
 * Created on April 23, 2004, 3:04 PM
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author  jc73554
 */
public class MrkContent {
    private String mtype;
    private StringBuffer content;

    /** Creates a new instance of MrkContent */
    public MrkContent(String mtype) {
        this.mtype = mtype;
        content = new StringBuffer();
    }

    public void appendText(String text) {
        content.append(text);
    }

    public void appendMrkElement(MrkContent element) {
        appendText(element.getContent());
    }

    public String getMtype() {
        return mtype;
    }

    public String getContent() {
        return content.toString();
    }
}
