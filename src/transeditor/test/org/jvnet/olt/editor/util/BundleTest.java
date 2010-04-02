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
 * Bundle.java
 *
 * Created on June 23, 2006, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.jvnet.olt.editor.util;

import junit.framework.TestCase;


/**
 *
 * @author boris
 */
public class BundleTest extends TestCase{

    public BundleTest(){
        super();
    }

    public void testGetStringOK() throws Exception{
        Bundle b  = Bundle.getBundle(BundleTest.class);
        assertEquals("abcd",b.getString("1234"));
    }

    public void testGetMissingString() throws Exception{
        //now we want to get back the key, actually and a warning
        Bundle b  = Bundle.getBundle(BundleTest.class);
        assertEquals("XYZ",b.getString("XYZ"));
    }
}
