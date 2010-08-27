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
 * DefaultTagTable.java
 *
 * Created on May 17, 2004, 5:33 PM
 */

package org.jvnet.olt.parsers.tagged;

/**
 * A default class that implements TagTable, providing "sane" values
 * as defaults.
 * @author  timf
 */
public class DefaultTagTable implements TagTable {
     
    /** Creates a new instance of DefaultTagTable */
    public DefaultTagTable() {
    }
    
    /**
     * is empty ? = false
     * @param tagName
     * @return false.
     */
    public boolean tagEmpty(String tagName) {
        return false;
    }
    
    /**
     * @return false.
     */
    public boolean tagEmpty(String tagName, String namespaceID) {
        return tagEmpty(tagName);
    }
    
    /**
     * forces verbatim layout ? = false
     */
    public boolean tagForcesVerbatimLayout(String tagName) {
        return false;
    }
    
    /**
     * @return false.
     */
    public boolean tagForcesVerbatimLayout(String tagName, String namespaceID) {
        return tagForcesVerbatimLayout(tagName);
    }
    
    /**
     * may contain pcdata ? = true
     */
    public boolean tagMayContainPcdata(String tagName) {
        return true;
    }
    
    /**
     * @return true.
     */
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}
