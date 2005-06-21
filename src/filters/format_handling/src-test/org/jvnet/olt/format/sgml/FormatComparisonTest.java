
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

package org.jvnet.olt.format.sgml;

import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.UnsupportedFormatException;
import org.jvnet.olt.format.FormatComparer;
import org.jvnet.olt.format.GlobalVariableManager;
import junit.framework.*;


public class FormatComparisonTest
extends TestCase
{
    private GlobalVariableManager m_entityManager;

  public FormatComparisonTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    m_entityManager = new EntityManager();
  }

  public void testCaseFolding()
  {
    String str1 = "<emphasis>An emphasized string</emphasis>.";
    String str2 = "<EMPHASIS>An unemphasized string</EMPHASIS>.";
    
    runTestCase(str1, str2, 0);
  }


  public void testBasicComparison()
  {
    String str1 = "<emphasis>An emphasized string</emphasis>.";
    String str2 = "An unemphasized string.";
    
    runTestCase(str1, str2, 4);
  }

  public void testMultiInstanceFormat()
  {
    String str1 = "<b>Bold text</b> and plain <b>text</b>.";
    String str2 = "<b>An</b> unemphasized string.";

    runTestCase(str1, str2, 4);
  }

  public void testMajorDifferences()
  {
    String str1 = "<b><i>Bold</i> text</b> and <a href=\"\"><img src=\"foo.gif\" alt=\"<img src=\"foo.gif\">\" >plain</a> <b>text</b>.";
    String str2 = "<b>An</b> <u>unemphasized</u> string.";

    runTestCase(str1, str2, 24);
  }

  public void testCDataFormat()
  {
    String str1 = "<b>Bold text</b> and plain text and a <![CDATA[ section containing <b> tags and <i> tags ]]>.";
    String str2 = "<b>An</b> unemphasized string.";

    runTestCase(str1, str2, 5);
  }

  public void runTestCase(String str1, String str2, int expectedPenalty)
  {
    FormatComparer comparer = new FormatComparer();

    try
    {
      int penalty = comparer.compareFormats(str1, "SGML", str2, "SGML", m_entityManager);

      assertEquals("Format penalty incorrect", expectedPenalty, penalty);
    }
    catch(InvalidFormattingException exInvalid) { fail("InvalidFormattingException " + exInvalid.getMessage()); }
    catch(UnsupportedFormatException exUnsupp) { fail("UnsupportedFormatException: " + exUnsupp.getMessage()); }

  }


  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new FormatComparisonTest("testBasicComparison"));
    suite.addTest( new FormatComparisonTest("testMultiInstanceFormat"));
    suite.addTest( new FormatComparisonTest("testMajorDifferences"));
    suite.addTest( new FormatComparisonTest("testCDataFormat"));
    suite.addTest( new FormatComparisonTest("testCaseFolding"));
    
    return suite;   
  }
}
