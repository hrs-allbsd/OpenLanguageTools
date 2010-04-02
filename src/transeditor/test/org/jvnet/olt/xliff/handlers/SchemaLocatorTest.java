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
 * SchemaLocatorTest.java
 *
 * Created on June 27, 2005, 3:10 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.xliff.handlers;

import junit.framework.TestCase;

/**
 *
 * @author boris
 */
public class SchemaLocatorTest extends TestCase{

    /** Creates a new instance of SchemaLocatorTest */
    public SchemaLocatorTest() {
    }

    public void testLookupByPublic(){
        SchemaLocator loc = new SchemaLocator("PUBLIC",null);

        SchemaLocator tst1 = new SchemaLocator("PUBLIC",null);
        SchemaLocator tst2 = new SchemaLocator("PUBLIC","SYSTEM");
        SchemaLocator tst3 = new SchemaLocator(null,"SYSTEM");

        assertEquals(tst1,loc);
        assertFalse(tst2.equals(loc));
        assertFalse(tst3.equals(loc));


    }

    public void testLookupBySystem(){
        SchemaLocator loc = new SchemaLocator("PUBLIC","SYSTEM");

        SchemaLocator tst1 = new SchemaLocator("PUBLIC",null);
        SchemaLocator tst2 = new SchemaLocator("PUBLIC","SYSTEM");
        SchemaLocator tst3 = new SchemaLocator(null,"SYSTEM");

        assertFalse(tst1.equals(loc));
        assertEquals(tst2,loc);
        assertFalse(tst3.equals(loc));


    }
}
