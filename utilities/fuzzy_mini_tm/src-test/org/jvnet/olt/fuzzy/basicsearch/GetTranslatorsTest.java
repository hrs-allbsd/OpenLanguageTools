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
import java.util.Arrays;
import java.io.*;
import org.jvnet.olt.minitm.*;

public class GetTranslatorsTest 
extends TestCase
{
  public GetTranslatorsTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new GetTranslatorsTest("testTranslatorIdRetrieval") );
    suite.addTest( new GetTranslatorsTest("testRetrievalFromEmptyMiniTM") );
    suite.addTest( new GetTranslatorsTest("testRetrievalFromMiniTM") );
    suite.addTest( new GetTranslatorsTest("testRetrievalFromSGMLMiniTM") );
    suite.addTest( new GetTranslatorsTest("testEmptyMiniTM") );

    return suite;
  }

  public void setUp()
  {
    try
    {
      String string =   "<?xml version=\"1.0\" ?>\n" + 
	"<minitm name=\"SGML tags MiniTM\" srclang=\"en\" tgtlang=\"de\">\n" +
	"  <entry>\n" + 
	"    <source>foo</source>\n" + 
	"    <translation>bar</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>left</source>\n" + 
	"    <translation>right</translation>\n" + 
	"    <translatorId>cujo</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>yin</source>\n" + 
	"    <translation>yang</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>black</source>\n" + 
	"    <translation>white</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>night</source>\n" + 
	"    <translation>day</translation>\n" + 
	"    <translatorId>cujo</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>up</source>\n" + 
	"    <translation>down</translation>\n" + 
	"    <translatorId>cenobyte</translatorId>\n" + 
	"  </entry>\n" + 
	"</minitm>\n";


      File file = new File("multi-id.mtm");
      FileOutputStream ostream = new FileOutputStream(file);
      OutputStreamWriter writer = new OutputStreamWriter(ostream, "UTF8");

      writer.write(string);
      writer.close();      
    }
    catch(Exception ex) { fail(ex.getMessage()); }
  }

  public void tearDown()
  {
    File multiId = new File("multi-id.mtm");
    if(multiId.exists())
    {
      multiId.delete();
    }
    
    File empty = new File("empty.mtm");
    if(empty.exists())
    {
      empty.delete();
    }
  }

  public void testTranslatorIdRetrieval()
  {
    try
    {
      BasicDataStore ds = new BasicDataStore("multi-id.mtm", false, "IDs", "en", "de");
      String[] array = ds.getAllTranslatorIDs();
      assertEquals(3, array.length);
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }

  public void testRetrievalFromEmptyMiniTM()
  {
    try
    {
      BasicDataStore ds = new BasicDataStore("empty.mtm", true, "empty", "en", "de");
      String[] array = ds.getAllTranslatorIDs();
      assertEquals(0, array.length);
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }

  public void testRetrievalFromSGMLMiniTM()
  {
    try
    {
      BasicSGMLFuzzySearchMiniTM minitm = new
	BasicSGMLFuzzySearchMiniTM("multi-id.mtm", false, "IDs", "en", "de");

      String[] array = minitm.getAllTranslatorIDs();
      assertEquals(3, array.length);
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }

  public void testRetrievalFromMiniTM()
  {
    try
    {
      BasicFuzzySearchMiniTM minitm = new
	BasicFuzzySearchMiniTM("multi-id.mtm", false, "IDs", "en", "de");

      String[] array = minitm.getAllTranslatorIDs();
      assertEquals(3, array.length);
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }

 
  public void testEmptyMiniTM()
  {
    try
    {
      BasicDataStore ds = new BasicDataStore("empty.mtm", true, "empty", "en", "de");
      
      AlignedSegment as1 = new AlignedSegment("foo","bar","johnc");
      ds.insertItem(as1);

      AlignedSegment as2 = new AlignedSegment("wibble","wobble","mr.x");
      ds.insertItem(as2);
      
      String[] array = ds.getAllTranslatorIDs();
      assertEquals(2, array.length);
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }
 

}
