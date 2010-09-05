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
import org.jvnet.olt.index.BasicDataStore;

public class StringLengthVariationTest 
extends TestCase
{
  private MiniTMIndex fuzzyIndex;

  public StringLengthVariationTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      //  Create an empty index
      fuzzyIndex = new BasicFuzzyIndex( new BasicDataStore("/tmp/johnc/foo.bar", true, "sample", "en", "de" ), new PlainTextFormatRemovingStrategy() );
      
      //  Add some items to the index.
      fuzzyIndex.insertItem("The cat in the hat.", 1L);
      fuzzyIndex.insertItem("Once upon a time in a galaxy far, far away.", 2L);
      fuzzyIndex.insertItem("There is no delight the equal of dread.", 3L);
      fuzzyIndex.insertItem("The cat on the mat.", 4L);
      fuzzyIndex.insertItem("I love the smell of napalm in the morning.", 5L);
      fuzzyIndex.insertItem("Character picker.", 5L);
   }
    catch(MiniTMException miniEx) { System.out.println("A very serious setup error has occurred."); }
  } 

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new StringLengthVariationTest("testShortQuery"));
    suite.addTest( new StringLengthVariationTest("testLongQuery"));
    suite.addTest( new StringLengthVariationTest("testLargeOverlap"));

    return suite;
  }


  public void testShortQuery()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("Short.", 70);
      assertNull("False matches found.",result);

    }
    catch(Exception ex) { fail(ex.getMessage()); }
    
  }

  public void testLongQuery()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("This is a rather long query string that contains the words, 'galaxy', 'cat', 'dread', which occur inside some of the strings in the fuzzy index, and are included to provoke false matches.", 70);
      assertNull("False matches found.",result);

    }
    catch(Exception ex) { fail(ex.getMessage()); }
  }

  public void testLargeOverlap()
  {
    SearchResult[] result;
    try
    {
      result = fuzzyIndex.doSearch("Character picker help.", 60);
      
      if(result.length > 0)
      {
	for(int t = 0; t < result.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     result[t].getDataSourceKey() + ", " +
			     result[t].getMatchQuality() + " }");
	}
      }
      assertNotNull("Died on 'help' string.",result);

      result = fuzzyIndex.doSearch("Character Picker applet as it appears in the panel.", 70);
      assertNull("Matched on 'error message' string.",result);

      result = fuzzyIndex.doSearch("To add <application>Character Picker</application> to a panel open the <guimenu>Global Menu</guimenu> from the panel, then choose<menuchoice> <guimenu>Applets</guimenu> <guisubmenu>Utility</guisubmenu> <guimenuitem>Character Picker</guimenuitem> </menuchoice>.", 70);
      assertNull("Matched on 'Doc help' string.",result);
      
    }
    catch(Exception ex) { fail(ex.getMessage()); }
  
  }
}
