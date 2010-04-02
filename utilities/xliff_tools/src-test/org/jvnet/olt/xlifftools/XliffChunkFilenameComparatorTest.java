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
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffChunkFilenameComparatorTest.java
 * JUnit based test
 *
 * Created on 25 August 2003, 15:15
 */

package org.jvnet.olt.xlifftools;

import java.io.File;
import java.util.Comparator;
import junit.framework.*;

/**
 *
 * @author jc73554
 */
public class XliffChunkFilenameComparatorTest extends TestCase
{
    
    public XliffChunkFilenameComparatorTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(XliffChunkFilenameComparatorTest.class);
        return suite;
    }
    
    /** Test of compare method, of class org.jvnet.olt.xlifftools.XliffChunkFilenameComparator. */
    public void testCompare()
    {
        System.out.println("testCompare");
        XliffChunkFilenameComparator comparator = new XliffChunkFilenameComparator();
        
        File a = new File("file_chunk_2.xlz");
        File b = new File("file_chunk_1.xlz");
        File c = new File("file_chunk_17.xlz");
        File d = new File("file_chunk_107.xlz");

        //  Do the tests
        assertEquals(comparator.compare(a,b), 1);
        assertEquals(comparator.compare(c,b), 1);
        assertEquals(comparator.compare(c,a), 1);
        assertEquals(comparator.compare(d,c), 1);
        assertEquals(comparator.compare(a,a), 0);        
    }  
    
}
