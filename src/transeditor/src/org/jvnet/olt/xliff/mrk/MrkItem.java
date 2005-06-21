/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * MrkItem.java
 *
 * Created on April 21, 2004, 6:07 PM
 */
package org.jvnet.olt.xliff.mrk;


/**
 *
 * @author  jc73554
 */
public interface MrkItem {
    public boolean hasIdentifier();

    public boolean isIgnored();

    /** Getter for property mrkString.
     * @return Value of property mrkString.
     */
    public String getMrkString();

    public String getIdentifier();
}
