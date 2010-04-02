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
 * TransFile.java
 *
 * Created on February 25, 2005, 11:36 AM
 */
package org.jvnet.olt.xliff;


/**
 *
 * @author boris
 */
public class TransFile {
    private final String fileName;
    private final long uniqueId;

    //TODO add data type, languages etc ...

    /** Creates a new instance of TransFile */
    public TransFile(String fileName, long serialId) {
        this.fileName = fileName;
        uniqueId = serialId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TransFile) {
            TransFile other = (TransFile)obj;

            return other.uniqueId == uniqueId;
        }

        return false;
    }

    public String toString() {
        return "File:" + fileName + " (id:" + uniqueId + ")";
    }

    public int hashCode() {
        return (int)(uniqueId * 37);
    }

    public long getUniqueId () {
        return uniqueId;
    }

}
