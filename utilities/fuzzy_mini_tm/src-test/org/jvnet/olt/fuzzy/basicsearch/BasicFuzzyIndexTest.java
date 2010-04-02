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

package org.jvnet.olt.fuzzy.basicsearch;

import junit.framework.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import org.jvnet.olt.minitm.*;
import org.jvnet.olt.fuzzy.*;

public class BasicFuzzyIndexTest 
extends TestCase
{
  private MiniTMIndex fuzzyIndex;

  public BasicFuzzyIndexTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      //  Create an empty index
      fuzzyIndex = new BasicFuzzyIndex( new BasicDataStore("/tmp/johnc/foo.bar", true, "sample", "en", "de" ), new PlainTextFormatRemovingStrategy());
      
      //  Add some items to the index.
      fuzzyIndex.insertItem("The cat in the hat.", 1L);
      fuzzyIndex.insertItem("Once upon a time in a galaxy far, far away.", 3L);
      fuzzyIndex.insertItem("There is no delight the equal of dread.", 2L);
      fuzzyIndex.insertItem("There is no spoon.", 4L);
      fuzzyIndex.insertItem("There is no span.", 5L);
      fuzzyIndex.insertItem("The cat on the mat.", 6L);
      fuzzyIndex.insertItem("I love the smell of napalm in the morning.", 7L);
    }
    catch(MiniTMException miniEx) { System.out.println("A very serious setup error has occurred."); }
  } 

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new BasicFuzzyIndexTest("testExactMatches"));
    suite.addTest( new BasicFuzzyIndexTest("testFuzzyMatchesBasic"));
    suite.addTest( new BasicFuzzyIndexTest("testSeventyPCFuzzy"));
  
    //  Other test suites
    suite.addTest( MiniTmExporterTest.suite() );
    suite.addTest( MiniTMTest.suite() );
    suite.addTest( ContentHandlerTest.suite() );
    suite.addTest( StringLengthVariationTest.suite() );
    suite.addTest( SGMLFormattingTest.suite() );
    suite.addTest( GetTranslatorsTest.suite() );
    suite.addTest( StrangeSGMLMatchTest.suite() );
    
    return suite;
  }

  public void testExactMatches()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("The cat in the hat.", 100);
      assertNotNull(result);
      assertEquals(1, result.length);
      try
      {
	assertEquals(1L, result[0].getDataSourceKey());
      }
      catch(ArrayIndexOutOfBoundsException ex) { fail(ex.getMessage()); }

      result = fuzzyIndex.doSearch("There is no spoon.", 100);
      assertNotNull(result);
      assertEquals(1, result.length);
      try
      {
	assertEquals(4L, result[0].getDataSourceKey());
      }
      catch(ArrayIndexOutOfBoundsException ex) { fail(ex.getMessage()); }

      result = fuzzyIndex.doSearch("There is a spoon.", 100);
      assertNull(result);

    }
    catch(MiniTMException miniEx) { fail(miniEx.getMessage()); }
  }

  public void testFuzzyMatchesBasic()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("The cat on the hat.", 85 );
      assertNotNull(result);
      if(result.length > 0)
      {
	for(int t = 0; t < result.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     result[t].getDataSourceKey() + ", " +
			     result[t].getMatchQuality() + " }");
	}
      }
      assertEquals(2, result.length);
      try
      {
	assertEquals(1L, result[0].getDataSourceKey());
      }
      catch(ArrayIndexOutOfBoundsException ex) { fail(ex.getMessage()); }

    }
    catch(MiniTMException miniEx) { fail(miniEx.getMessage()); }
  }

  public void testSeventyPCFuzzy()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("The cat on the hat.", 70 );
      assertNotNull(result);
      if(result.length > 2)
      {
	for(int t = 0; t < result.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     result[t].getDataSourceKey() + ", " +
			     result[t].getMatchQuality() + " }");
	}
      }
      assertEquals(2, result.length);
      try
      {
	assertEquals(1L, result[0].getDataSourceKey());
      }
      catch(ArrayIndexOutOfBoundsException ex) { fail(ex.getMessage()); }

    }
    catch(MiniTMException miniEx) { fail(miniEx.getMessage()); }
  }

}
