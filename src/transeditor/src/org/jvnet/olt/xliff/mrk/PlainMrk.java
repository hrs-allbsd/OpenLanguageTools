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
