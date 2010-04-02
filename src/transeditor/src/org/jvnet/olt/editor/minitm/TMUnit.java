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
package org.jvnet.olt.editor.minitm;


/**
 * <p>Title: Open Language Tools XLIFF Translation Editor</p>
 * <p>Description: This class is the definition of TMUnit, which is an item saved/read in/from MiniTM file. </p>
 * @deprecated
 */
public class TMUnit {
    private String source;
    private String target;

    //private long hashvalue;

    /**
     * Constructor
     */
    public TMUnit(String source) {
        this.source = source;

        //this.hashvalue = HashKey.getHashValue(source);
        this.target = null;
    }

    public TMUnit(String source, String target) {
        this.source = source;

        //this.hashvalue = HashKey.getHashValue(source);
        this.target = target;
    }

    /**
     * Return object which is the key value
     *
     * @return the key value
     */
    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public void setSource(String s) {
        source = s;
    }

    public void setTarget(String s) {
        target = s;
    }

    /**
     */
    public boolean equals(TMUnit aunit) {
        String v = aunit.getSource();

        if (source.equals(v)) {
            return true;
        } else {
            return false;
        }
    }
}
