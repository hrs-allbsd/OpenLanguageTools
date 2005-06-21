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
    public static final Version XLIFF_1_0 = new Version();
    public static final Version XLIFF_1_1 = new Version();
    static private Map versions = new HashMap();

    static {
        versions.put("1.0", XLIFF_1_0);
        versions.put("1.1", XLIFF_1_1);
    }

    /** Creates a new instance of Version */
    private Version() {
    }

    public boolean isXLIFF10() {
        return this == XLIFF_1_0;
    }

    public boolean isXLIFF11() {
        return this == XLIFF_1_1;
    }

    synchronized static public Version fromString(String version) {
        return (version == null) ? null : (Version)versions.get(version);
    }
}
