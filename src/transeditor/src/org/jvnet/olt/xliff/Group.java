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
 * Group.java
 *
 * Created on April 19, 2005, 10:46 AM
 *
 */
package org.jvnet.olt.xliff;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author boris
 */

//TODO change id to object containg the file as well
public class Group {
    private final String id;
    private Map attrs;
    private List trUnits = new LinkedList();

    /** Creates a new instance of Group */
    public Group(String id, Map attrs) {
        this.id = id;
        this.attrs = attrs;
    }

    public void addTransUnit(String trId) {
        trUnits.add(trId);
    }

    public List getUnitIds() {
        return Collections.unmodifiableList(trUnits);
    }

    public Map getAttributeMap() {
        return Collections.unmodifiableMap(attrs);
    }
}
