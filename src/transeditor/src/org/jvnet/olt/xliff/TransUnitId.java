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
 * TransUnitId.java
 *
 * Created on February 25, 2005, 11:36 AM
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class TransUnitId {
    final private String id;
    final private TransFile file;

    /** Creates a new instance of TransUnitId */
    public TransUnitId(String id, TransFile file) {
        this.id = id;
        this.file = file;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TransUnitId) {
            TransUnitId other = (TransUnitId)obj;

            return id.equals(other.id) && file.equals(other.file);
        }

        return false;
    }

    public String toString() {
        return "ID:" + id + " (" + file.toString() + ")";
    }

    public int hashCode() {
        return (id.hashCode() * 37) + (file.hashCode() * 13);
    }

    public String getStrId() {
        return id + "(" + file.getUniqueId() + ")" ;
    }
}
