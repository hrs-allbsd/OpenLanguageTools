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
 * Version.java
 *
 * Created on April 22, 2005, 12:40 PM
 *
 */
package org.jvnet.olt.xliff;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author boris
 */
public class Version {
    public static final Version XLIFF_1_0 = new Version("1.0");
    public static final Version XLIFF_1_1 = new Version("1.1");
    public static final Version XLIFF_1_2 = new Version("1.2");
    public static final Version XLIFF_1_2_strict = new Version("1.2_strict");
    public static final Version XLIFF_1_2_transitional = new Version("1.2_transitional");
    private String versionString;
    static private Map versions = new HashMap();


    static {
        versions.put("1.0", XLIFF_1_0);
        versions.put("1.1", XLIFF_1_1);
        versions.put("1.2", XLIFF_1_2);
        versions.put("1.2_strict", XLIFF_1_2_strict);
        versions.put("1.2_transitional", XLIFF_1_2_transitional);
    }

    /** Creates a new instance of Version */
    private Version() {
    }

    private Version(String version) {
        versionString=version;
    }

    public boolean isXLIFF10() {
        return this == XLIFF_1_0;
    }

    public boolean isXLIFF11() {
        return this == XLIFF_1_1;
    }

    public boolean isXLIFF12() {
        return (this == XLIFF_1_2) || isXLIFF12_strict() || isXLIFF12_transitional();
    }

    public boolean isXLIFF12_strict() {
        return this == XLIFF_1_2_strict;
    }

    public boolean isXLIFF12_transitional() {
        return this == XLIFF_1_2_transitional;
    }

    synchronized static public Version fromString(String version) {
        return (version == null) ? null : (Version)versions.get(version);
    }

    public boolean isEqual( Version v) {
        return ( this.equals(v) ||
                 this.isXLIFF12() && v.isXLIFF12()
               );
    }

    @Override
    public String toString() {
        return (versionString == null) ? null : versionString;
    }

}
