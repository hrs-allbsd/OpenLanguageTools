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
 * VersionTest.java
 *
 * Created on April 25, 2005, 12:33 PM
 *
 */
package org.jvnet.olt.xliff;

import junit.framework.TestCase;
import org.jvnet.olt.xliff.Version;


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
