
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
import java.io.*;
import org.xml.sax.*;
import org.jvnet.olt.minitm.*;
import javax.xml.parsers.*;

public class SGMLFormattingTest 
extends TestCase
{
  public SGMLFormattingTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new SGMLFormattingTest("testFormatIndepenentRetrieval") );
    suite.addTest( new SGMLFormattingTest("testRetrievalWithDifferentFormatting") );

    return suite;
  }

  
  public void setUp()
  {
    try
    {
      String string =   "<?xml version=\"1.0\" ?>\n" + 
	"<minitm name=\"SGML tags MiniTM\" srclang=\"en\" tgtlang=\"de\">\n" +
	"  <entry>\n" + 
	"    <source>This is a &lt;b&gt;string&lt;/b&gt; with &lt;a href=\"formatting\"&gt;formatting&lt;/a&gt;.</source>\n" + 
	"    <translation>This is a &lt;b&gt;translated string&lt;/b&gt; with &lt;a href=\"formatting\"&gt;formatting&lt;/a&gt;</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>&lt;guimenu&gt;&lt;guimenuitem&gt;Mostly &lt;/guimenuitem&gt; &lt;emphasis&gt;formatting.&lt;emphasis&gt;&lt;/guimenu&gt;</source>\n" +
	"    <translation>&lt;guimenu&gt;&lt;guimenuitem&gt;Translated &lt;/guimenuitem&gt; &lt;emphasis&gt;formatting.&lt;emphasis&gt;&lt;/guimenu&gt;</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"  <entry>\n" + 
	"    <source>A string with the tinest little &lt;b&gt;bit&lt;/b&gt; of formatting embedded in it.</source>\n" + 
	"    <translation>A  translated string with the tinest little &lt;u&gt;bit&lt;/u&gt; of formatting embedded in it.</translation>\n" + 
	"    <translatorId>johnc</translatorId>\n" + 
	"  </entry>\n" + 
	"</minitm>\n";
      
      File file = new File("sgml-tags.mtm");
      FileOutputStream ostream = new FileOutputStream(file);
      OutputStreamWriter writer = new OutputStreamWriter(ostream, "UTF8");

      writer.write(string);
      writer.close();      
    }
    catch(Exception ex) { fail(ex.getMessage()); }
  }
  

  public void testFormatIndepenentRetrieval()
  {
    try
    {
      MiniTM minitm = new BasicSGMLFuzzySearchMiniTM("sgml-tags.mtm",
						 true,
						 "SGML MiniTM",
						 "en",
						 "de");

      TMMatch[] matches = minitm.getMatchFor("This is a string with formatting.", 70, 5);
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
      assertEquals(1, matches.length);
      assertEquals(89, matches[0].getRatioOfMatch());
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }

  
  public void testRetrievalWithDifferentFormatting()
  {
    try
    {
      MiniTM minitm = new BasicSGMLFuzzySearchMiniTM("sgml-tags.mtm",
						 true,
						 "SGML MiniTM",
						 "en",
						 "de");

      TMMatch[] matches = minitm.getMatchFor("<guimenu><guimenuitem>This is a <emphasis>string</emphasis> with formatting.</guimenuitem></guimenu>", 70, 5);
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
      assertEquals(1, matches.length);
      assertEquals(77, matches[0].getRatioOfMatch());
    }
    catch(Exception ex) { fail("Exception thrown:" + ex.getMessage()); }
  }



}
