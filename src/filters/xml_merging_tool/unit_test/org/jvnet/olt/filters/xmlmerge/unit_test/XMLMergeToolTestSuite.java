
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XMLMergeToolTest.java
 * JUnit based test
 *
 * Created on 2004年4月9日, 下午6:47
 */

package org.jvnet.olt.filters.xmlmerge.unit_test;

import junit.framework.*;
import org.jvnet.olt.filters.xmlmerge.unit_test.mergeByElementID.MergeByElementIDTest;
import org.jvnet.olt.filters.xmlmerge.unit_test.mergeBySameOrder.MergeBySameOrderTest;
import org.jvnet.olt.filters.xmlmerge.unit_test.mergeBySourceID.MergeBySourceIDTest;
import org.jvnet.olt.filters.xmlmerge.unit_test.mergeXLIFF.MergeXLIFFTest;

/**
 *
 * @author sssfff
 */
public class XMLMergeToolTestSuite extends TestCase {
    
    public XMLMergeToolTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest( MergeByElementIDTest.suite() );
        suite.addTest( MergeBySameOrderTest.suite() );
        suite.addTest( MergeBySourceIDTest.suite() );
        suite.addTest( MergeXLIFFTest.suite() );
        return suite;
    }
//    public static void main(String []args){
//        junit.textui.TestRunner.run(suite());
//    }
        
}
