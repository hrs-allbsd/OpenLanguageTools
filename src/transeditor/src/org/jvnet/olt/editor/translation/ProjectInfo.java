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
 * ProjectInfo.java
 *
 * Created on March 25, 2005, 4:15 PM
 */
package org.jvnet.olt.editor.translation;
import org.jvnet.olt.editor.util.LanguageMappingTable;


/**
 *
 * @author boris
 */
public class ProjectInfo implements Comparable {
    private final String name;
    private final String srcLang;
    private final String tgtLang;

    /** Creates a new instance of ProjectInfo */
    ProjectInfo(String name, String srcLang, String tgtLang) {
        if ((name == null) || (srcLang == null) || (tgtLang == null)) {
            throw new NullPointerException("name/src/tgt lang are null (at least one of them)");
        }

        this.name = name;
        // TODO: remove this fallback for old internal language identifiers
        if ( srcLang.length() == 2 && srcLang.equals(srcLang.toUpperCase()) ) {
            this.srcLang = LanguageMappingTable.getInstance().reverseTranslateLangCode(srcLang);
        } else {
            this.srcLang = srcLang;
        }
        // TODO: remove this fallback for old internal language identifiers
        if ( tgtLang.length() == 2 && tgtLang.equals(tgtLang.toUpperCase()) ) {
            this.tgtLang = LanguageMappingTable.getInstance().reverseTranslateLangCode(tgtLang);
        } else {
            this.tgtLang = tgtLang;
        }

    }

    public String getName() {
        return name;
    }

    public String getSourceLang() {
        return srcLang;
    }

    public String getTargetLang() {
        return tgtLang;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ProjectInfo) {
            ProjectInfo other = (ProjectInfo)obj;

            return name.equals(other.name) && srcLang.equals(other.srcLang) && tgtLang.equals(other.tgtLang);
        }

        return false;
    }

    public int hashCode() {
        return (name.hashCode() * 37) + (srcLang.hashCode() * 7) + (tgtLang.hashCode() * 13);
    }

    public int compareTo(Object o) {
        ProjectInfo other = (ProjectInfo)o;

        int x = compare(name, other.name);

        if (x != 0) {
            return x;
        }

        x = compare(srcLang, other.srcLang);

        if (x != 0) {
            return x;
        }

        x = compare(tgtLang, other.tgtLang);

        if (x != 0) {
            return x;
        }

        return 0;
    }

    private int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}
