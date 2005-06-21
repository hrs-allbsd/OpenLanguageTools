
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
