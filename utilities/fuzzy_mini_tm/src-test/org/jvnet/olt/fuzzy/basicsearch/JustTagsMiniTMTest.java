
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
 * JustTagsMiniTMTest.java
 * JUnit based test
 *
 * Created on 27 February 2003, 16:41
 */

package org.jvnet.olt.fuzzy.basicsearch;

/**
 *
 * @author jc73554
 */
public class JustTagsMiniTMTest extends junit.framework.TestCase
{
    
    public JustTagsMiniTMTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(JustTagsMiniTMTest.class);
        return suite;
    }
    
    //  Test methods below.
    public void testLookupForJustTags()
    {
        try
        {
            //  create new minitm
            BasicSGMLFuzzySearchMiniTM minitm =
            new BasicSGMLFuzzySearchMiniTM("foo.xlz", true, "barja", "en-US","ja-JP");
            
            //  put a string in it that consists of just tags
            org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment( "<a href=\"http://www.sun.com/products/\"><img src=\"/im/ar_lg_red_d_pad.gif\" width=\"24\" height=\"21\" border=\"0\" alt=\"\" /></a><a href=\"http://www.sun.com/products/\"><img src=\"/im/tx_lg_products_services.gif\" width=\"146\" height=\"21\" border=\"0\" alt=\"Products and Services\" /></a>", "<a href=\"http://www.sun.com/products/\"><img src=\"/im/ar_lg_red_d_pad.gif\" width=\"24\" height=\"21\" border=\"0\" alt=\"\" /></a><a href=\"http://www.sun.com/products/\"><img src=\"/im/tx_lg_products_services.gif\" width=\"146\" height=\"21\" border=\"0\" alt=\"Toys and Tinkering\" /></a>", "73554");
            minitm.addNewSegment(seg);
            minitm.saveMiniTmToFile();
            
            //  look for this string
            org.jvnet.olt.minitm.TMMatch[] mathches = minitm.getMatchFor("<a href=\"http://www.sun.com/products/\"><img src=\"/im/ar_lg_red_d_pad.gif\" width=\"24\" height=\"21\" border=\"0\" alt=\"\" /></a><a href=\"http://www.sun.com/products/\"><img src=\"/im/tx_lg_products_services.gif\" width=\"146\" height=\"21\" border=\"0\" alt=\"Products and Services\" /></a>", 75, 5);
            
            
            
            //  assert that it was found
            assertEquals("Incorrect no. matches found. ", 3, mathches.length);
        }
        catch(org.jvnet.olt.minitm.MiniTMException ex)
        {
            ex.printStackTrace();
            fail("Exception thrown");
        }
    }
    
    public void testEmptySeg()
    {
        try
        {
            //  create new minitm
            BasicSGMLFuzzySearchMiniTM minitm =
            new BasicSGMLFuzzySearchMiniTM("foob.mtm", true, "barjab", "en-US","ja-JP");
            
            //  put a string in it that consists of just tags
            org.jvnet.olt.minitm.AlignedSegment seg = new org.jvnet.olt.minitm.AlignedSegment("","", "73554");
            minitm.addNewSegment(seg);
            
            org.jvnet.olt.minitm.AlignedSegment segb = new org.jvnet.olt.minitm.AlignedSegment( "<a href=\"http://www.sun.com/products/\"><img src=\"/im/ar_lg_red_d_pad.gif\" width=\"24\" height=\"21\" border=\"0\" alt=\"\" /></a><a href=\"http://www.sun.com/products/\"><img src=\"/im/tx_lg_products_services.gif\" width=\"146\" height=\"21\" border=\"0\" alt=\"Products and Services\" /></a>", "<a href=\"http://www.sun.com/products/\"><img src=\"/im/ar_lg_red_d_pad.gif\" width=\"24\" height=\"21\" border=\"0\" alt=\"\" /></a><a href=\"http://www.sun.com/products/\"><img src=\"/im/tx_lg_products_services.gif\" width=\"146\" height=\"21\" border=\"0\" alt=\"Toys and Tinkering\" /></a>", "73554");
            minitm.addNewSegment(segb);
            minitm.saveMiniTmToFile();
            
            //  look for this string
            org.jvnet.olt.minitm.TMMatch[] mathches = minitm.getMatchFor("", 75, 5);
            
            
            //  assert that it was found
            assertEquals("Incorrect no. matches found. ", 3, mathches.length);
        }
        catch(org.jvnet.olt.minitm.MiniTMException ex)
        {
            ex.printStackTrace();
            fail("Exception thrown");
        }
    }
    
    
}
