
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
import java.util.Hashtable;
import java.io.*;
import org.xml.sax.*;
import org.jvnet.olt.minitm.*;
import javax.xml.parsers.*;

public class MiniTMTest 
extends TestCase
{
  public MiniTMTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      String string =   "<?xml version=\"1.0\" ?>\n" + 
	"<minitm name=\"sample\" srclang=\"en\" tgtlang=\"de\">\n" + 
	"  <entry>\n" + 
	"    <source>The cat in the hat.</source>\n" + 
	"    <translation>A translation &amp; more!</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>The cat on the mat.</source>\n" + 
	"    <translation>Another translation for that.</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>One ring to rule them all, one ring to find them. One ring to bring them all &amp; in darkness bind them.</source>\n" + 
	"    <translation>The Lord of the Rings quote.</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>I taut I taw a putty tat. I did! I did! I did!</source>\n" + 
	"    <translation>Tweety Pie</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>There is no span.</source>\n" + 
	"    <translation>The Bridge builder's nightmare.</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>There is no spoon</source>\n" + 
	"    <translation>Soup is off.</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"</minitm>\n"; 
      File file = new File("/tmp/toread.mtm");
      FileOutputStream ostream = new FileOutputStream(file);
      OutputStreamWriter writer = new OutputStreamWriter(ostream, "UTF8");

      writer.write(string);
      writer.close();
    }  
    catch(Exception ex) { fail(ex.getMessage()); }
 
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new MiniTMTest("testReadingAnExistingTM") );

    return suite;
  }

  //
  //  The tests!
  //
  public void testReadingAnExistingTM()
  {
    try
    {
      MiniTM minitm = new BasicFuzzySearchMiniTM("/tmp/toread.mtm",
						 true,
						 "test",
						 "en",
						 "de");

      
      TMMatch[] matches = minitm.getMatchFor("The cat on the mat.", 70, 5);

      if(matches == null) 
      { 
	System.out.println("Error matches are null"); 
	AlignedSegment[] unitsB = minitm.getAllSegments();
	for(int j = 0; j < unitsB.length; j++)
	{
	  System.out.println("Units: " + j + " { " + 
			     unitsB[j].getDataStoreKey() + ", " +
			     unitsB[j].getSource() + " }");
	}
      }  
	
      assertNotNull(matches);  
      if(matches.length > 0)
      {
	for(int t = 0; t < matches.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     matches[t].getDataSourceKey() + ", " +
			     matches[t].getRatioOfMatch() + " }");
	}
      }  
      assertEquals(2, matches.length);
      
      assertNotNull(matches[0]);
      assertEquals("Another translation for that.", matches[0].getTranslation().toString());
    }
    catch(Exception ex) { fail(ex.getMessage()); }
  }
    
}
