
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

package org.jvnet.olt.fuzzy;

import junit.framework.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

public class FuzzyIndexEntryTest 
extends TestCase
{
  private FuzzyIndexEntry entry1;
  private FuzzyIndexEntry entry2;
  private FuzzyIndexEntry entry3;
  private FuzzyIndexEntry entry4;
  private FuzzyIndexEntry entry5;
  private FuzzyIndexEntry entry6;

  public FuzzyIndexEntryTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      entry1 = new FuzzyIndexEntry("The cat in the hat.", 1L);
      entry2 = new FuzzyIndexEntry("The cat on the mat.", 2L);
      entry3 = new FuzzyIndexEntry("One ring to rule them all, one ring to find them. One ring to bring them all and in darkness bind them.", 3L);
      entry4 = new FuzzyIndexEntry("I taut I taw a putty tat. I did! I did! I did!", 4L);
      entry5 = new FuzzyIndexEntry("There is no span.", 5L);
      entry6 = new FuzzyIndexEntry("There is no spoon", 6L);


    }
    catch(FuzzyKeyException ex) { System.out.println(ex.getMessage()); }
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new FuzzyIndexEntryTest("testManhattanVectorLength"));
    suite.addTest( new FuzzyIndexEntryTest("testManhattanDistances"));
    //  suite.addTest( new FuzzyIndexEntryTest("testDumpVectors"));
   
    return suite;
  }


  public void testManhattanVectorLength()
  {
    assertEquals(19, (long) entry1.manhattanVectorLength());
    assertEquals(19, (long)  entry2.manhattanVectorLength());
    assertEquals(103, (long)  entry3.manhattanVectorLength());
    assertEquals(46, (long)  entry4.manhattanVectorLength());
  }

  public void testManhattanDistances()
  {
    if(entry1.manhattanDistance(entry2) > 12)
    {
      System.out.println("Entry 1 {" + entry1.dumpIndexVector() + "}");
      System.out.println("Entry 2 {" + entry2.dumpIndexVector() + "}");
    }
    assertEquals( new Boolean(entry1.manhattanDistance(entry2) < 12), new Boolean(true));

  }
}
