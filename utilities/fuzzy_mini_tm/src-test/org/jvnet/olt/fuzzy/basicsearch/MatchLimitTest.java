
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
 * MatchLimitTest.java
 * JUnit based test
 *
 * Created on 03 March 2003, 16:00
 */

package org.jvnet.olt.fuzzy.basicsearch;

/**
 * This test suite tests the limit on matches reutrned by the Mini TM. This
 * limit is set by the developer using the mini TM and normally is 5, though
 * in some cases turns out to be one.
 * @author jc73554
 */
public class MatchLimitTest extends junit.framework.TestCase
{
    
    public MatchLimitTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(MatchLimitTest.class);
        return suite;
    }
    
    public void testJustFormatDifference()
    {
        try
        {
            //  Set up mini Tm with two segments
            org.jvnet.olt.minitm.MiniTM tm = new BasicSGMLFuzzySearchMiniTM("foo.mtm", true,"Test TM","en-US", "de-DE");
            org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment("<B>This</B> is a segment.", "<B>This</B> is a segment.","73554");
            tm.addNewSegment(seg);
            
            seg = new org.jvnet.olt.minitm.AlignedSegment("<i>This</i> is a segment.", "<i>This</i> is a segment.","73554");
            tm.addNewSegment(seg );
            tm.saveMiniTmToFile();
            
            //  Search for one of them as 100% match
            org.jvnet.olt.minitm.TMMatch[] matches = tm.getMatchFor("<B>This</B> is a segment.", 100, 1);
           // assertEquals("No match found for '<B>This</B> is a segment.'", 1, matches.length);
        
            //  Search for the other one as 100% match
            matches = tm.getMatchFor("<i>This</i> is a segment.", 100, 1);
            //assertEquals("No match found for '<i>This</i> is a segment.'", 1, matches.length);

        }
        catch(org.jvnet.olt.minitm.MiniTMException exMini)
        {
            fail("exception thrown : " + exMini.getMessage());
        }
    }
    
}
