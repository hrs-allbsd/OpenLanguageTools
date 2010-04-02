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
 * MatchQualityCalculatorTest.java
 * JUnit based test
 *
 * Created on 25 February 2003, 18:38
 */

package org.jvnet.olt.fuzzy;

import junit.framework.*;
import org.jvnet.olt.minitm.MiniTMException;
import org.jvnet.olt.minitm.MiniTMIndexException;
import org.jvnet.olt.minitm.SGMLFormatRemovingStrategy;
import org.jvnet.olt.minitm.FormatRemovingStrategy;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import org.jvnet.olt.minitm.StringCleaningAdapter;
import org.jvnet.olt.parsers.sgmltokens.MarkupEntry;
import org.jvnet.olt.fuzzy.basicsearch.StringComparer;

/**
 *
 * @author jc73554
 */
public class MatchQualityCalculatorTest extends TestCase
{
    
    public MatchQualityCalculatorTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(MatchQualityCalculatorTest.class);
        
        suite.addTestSuite(org.jvnet.olt.fuzzy.basicsearch.StringComparerTest.class);
        suite.addTest(org.jvnet.olt.fuzzy.basicsearch.MatchLimitTest.suite());
        
        return suite;
    }
    
    /** Test of calculateMatchQuality method, of class org.jvnet.olt.fuzzy.MatchQualityCalculator. */
    public void testCalculateMatchQuality()
    {
        runMatchTest("<b>A string</b>","<b>A string</b>",100);
        runMatchTest("<b>A string","<b>A string</b>",98);
        runMatchTest("<b>","<b></b>",98);
        runMatchTest("<b>foo","<b></b>",0);
        runMatchTest("<b>","<b>foo</b>",0);
        runMatchTest("<b>A string</i>","<b>A string</b>",96);
        runMatchTest("<b>The longer string with a few bits added.</b>","<b>A longer string with a few bits added.</b>",92);
        runMatchTest("<b>A longer string with a few bits added.</b>","<b>The longer string with a few bits added.</b>",92);
        runMatchTest("zh<b>A string.</b>","<b>A string.</b>",80);
        runMatchTest("A string","A string",100);
        runMatchTest("A string","<b>A string</b>",96);
        runMatchTest("<B>A string</b>","A string",96);
    }
        
    /** Test of calculateFormatPenalty method, of class org.jvnet.olt.fuzzy.MatchQualityCalculator. */
    public void testCalculateFormatPenalty()
    {
        runFormatDiffTest("<b>A string</b>","<b>A string</b>",0);
        runFormatDiffTest("<B>A string</b>","<b>A string</B>",0);
        runFormatDiffTest("<b></b>","<b></b>",0);
        runFormatDiffTest("<b><b><b></b>","<b></b>",4);
        runFormatDiffTest("<b><b><b></b>","<b><a href=\"foo\"></b>",9);
        runFormatDiffTest("A string","A string",0);
        runFormatDiffTest("A string","<b>A string</B>",4);
        runFormatDiffTest("<B>A string</b>","A string",4);
    }
    
    //  Helper methods.
    private void runMatchTest(String query, String ref, int val)
    {
        try
        {
            FormatRemovingStrategy fr = new SGMLFormatRemovingStrategy();
            int matchQual = MatchQualityCalculator.calculateMatchQuality(query,ref,fr);
            if(matchQual != val)
            {
                fail("Match quality between strings \"" + query + "\" and \"" + ref + "\" should have been: " + val + " but was: " + matchQual);
            }
        }
        catch(MiniTMException ex)
        {
            fail("exception thrown: " + ex.getMessage());
        }
        catch(NullPointerException exNull)
        {
            fail("exception thrown: testing strings '" +query+ "' and '" + ref+"'");
        }
        
    }
    
    private void runFormatDiffTest(String query, String ref, int val)
    {
       try
        {
            FormatRemovingStrategy fr = new SGMLFormatRemovingStrategy();
            int formatDiff = MatchQualityCalculator.calculateFormatPenalty(query,ref,fr);
            if(formatDiff != val)
            {
                fail("The format difference between strings \"" + query + "\" and \"" + ref + "\" should have been: " + val + " but was: " + formatDiff);
            }
        }
        catch(MiniTMException ex)
        {
            fail("exception thrown: " + ex.getMessage());
        }
        catch(NullPointerException exNull)
        {
            fail("exception thrown: testing strings '" +query+ "' and '" + ref +"'");
        }
    }
        
    
}
