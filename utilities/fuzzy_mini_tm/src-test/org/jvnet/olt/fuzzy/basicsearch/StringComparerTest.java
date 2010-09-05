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
 * StringComparerTest.java
 * JUnit based test
 *
 * Created on 27 February 2003, 14:32
 */

package org.jvnet.olt.fuzzy.basicsearch;

import junit.framework.*;
import org.jvnet.olt.index.StringComparer;

/**
 *
 * @author jc73554
 */
public class StringComparerTest extends TestCase
{
    
    public StringComparerTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(StringComparerTest.class);
        
        return suite;
    }
    
    /** Test of compareStrings method, of class org.jvnet.olt.fuzzy.basicsearch.StringComparer. */
    public void testCompareStrings()
    {
        runStringComparisonTest("A string.", "string", 3);
        runStringComparisonTest("A string.", "A string.", 0);
        runStringComparisonTest("", "A string.", 9);
        runStringComparisonTest("A string.", "", 9);
    }
    
    /** Test of calculatePercentMatch method, of class org.jvnet.olt.fuzzy.basicsearch.StringComparer. */
    public void testCalculatePercentMatch()
    {
        runPercentMatchTest("A string.", "string", 60);
        runPercentMatchTest("A string.", "A string.", 100);
        runPercentMatchTest("", "A string.", 0);
        runPercentMatchTest("A string.", "", 0);
    }
    
    //  Helper methods
    private void runStringComparisonTest(String query, String ref, int val)
    {
        int diff = StringComparer.compareStrings(query, ref, true);
        if(diff != val)
        {
            fail("The difference between strings \"" + query + "\" and \"" + ref + "\" should have been: " + val + " but was: " + diff);
        }
    }

    private void runPercentMatchTest(String query, String ref, int val)
    {
        int diff = (int) StringComparer.calculatePercentMatch(query, ref);
        if(diff != val)
        {
            fail("The percent match between strings \"" + query + "\" and \"" + ref + "\" should have been: " + val + " but was: " + diff);
        }
    }
 
}
