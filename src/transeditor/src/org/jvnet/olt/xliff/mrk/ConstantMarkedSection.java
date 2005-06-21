/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ConstantMarkedSection.java
 *
 * Created on April 21, 2004, 6:06 PM
 */
package org.jvnet.olt.xliff.mrk;


/**
 *
 * @author  jc73554
 */
public class ConstantMarkedSection implements MrkItem {
    private String constant;
    private String mrkString;

    /** Creates a new instance of ConstantMarkedSection */
    public ConstantMarkedSection(String mrkString, String constant) {
        this.mrkString = mrkString;
        this.constant = constant;
    }

    public boolean hasIdentifier() {
        return false;
    }

    public boolean isIgnored() {
        return (constant.equals("IGNORE"));
    }

    public String getMrkString() {
        return mrkString;
    }

    public String getIdentifier() {
        return constant;
    }
}
