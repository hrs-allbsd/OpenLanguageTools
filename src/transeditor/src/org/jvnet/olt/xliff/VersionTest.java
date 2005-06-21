/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * VersionTest.java
 *
 * Created on April 25, 2005, 12:33 PM
 *
 */
package org.jvnet.olt.xliff;

import junit.framework.TestCase;


/**
 *
 * @author boris
 */
public class VersionTest extends TestCase {
    /** Creates a new instance of VersionTest */
    public VersionTest() {
    }

    public void testVersion() throws Exception {
        Version v0 = Version.fromString("1.0");
        Version v0x = Version.fromString("1.0");
        Version v1 = Version.fromString("1.1");
        Version v2 = Version.fromString("0.1");

        assertSame(v0, v0x);
        assertNotSame(v0, v1);
        assertNull(v2);
    }
}
