/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * PlainMrk.java
 *
 * Created on April 21, 2004, 6:06 PM
 */
package org.jvnet.olt.xliff.mrk;


/**
 *
 * @author  jc73554
 */
public class PlainMrk implements MrkItem {
    private String mrkString;

    /** Creates a new instance of PlainMrk */
    public PlainMrk(String mrkString) {
        this.mrkString = mrkString;
    }

    public boolean hasIdentifier() {
        return false;
    }

    public boolean isIgnored() {
        return false;
    }

    public String getMrkString() {
        return mrkString;
    }

    public String getIdentifier() {
        return "";
    }
}
