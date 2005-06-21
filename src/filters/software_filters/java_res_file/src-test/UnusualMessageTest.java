
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.JavaParser;
import org.jvnet.olt.tmci.TokenCell;
import junit.framework.*;
import java.io.StringReader;


public class UnusualMessageTest 
extends TestCase
implements ResBundleParserTreeConstants
{
  private TokenCell[] tokencells;

  public UnusualMessageTest(String name)
  {
    super(name);
  }

  public void setUp()
  {
    try
    {
      //  Put a sample .java file in a string
      String string = 
	/* START TEST FILE */
	"package com.sun.wizards;\n"+
"import java.util.*;\n"+
"public class WizardResources extends ListResourceBundle\n"+
"{\n"+
"  public static final int MSG_HOST_NOT_VALID = 1;\n"+
"  public Object[][] getContents() { return contents; }\n"+
"  static final Object[][] contents =\n"+
"  { \n"+
"    { \"MSG15\", \"Enter Hostname:\"},\n"+
"    { \"MSG16\", \"The Wizard cannot be started on this display.  On Solaris,\" +\n"+
"		System.getProperty(\"line.separator\") +\n"+
"               \"you must allow the wizard access to the display.  \" +\n"+
"               System.getProperty(\"line.separator\") +\n"+
"		\"Try typing \\\"xhost +\\\" in another window.\"},\n"+
"    { \"MSG17\", \"Close\" },\n"+
"  };\n"+
	  "}";

      StringReader reader = new StringReader(string);
      ResBundleParser parser = new ResBundleParser(reader);
            
      parser.parse();

      DisplayingTokenArrayFactoryVisitor factory = new DisplayingTokenArrayFactoryVisitor();
      parser.walkParseTree(factory, "");

      tokencells = factory.getDisplayingTokens();
    }
    catch(Exception ex)
    {
      fail("Parse of the test file failed!" + ex);
    }
  }

  public void tearDown()
  {
    tokencells = null;
  }


  public void testUnusualMessage()
  {


      assertEquals("\"MSG16\"",tokencells[82].getText());
      assertEquals(",",tokencells[83].getText());
      assertEquals("end code",tokencells[86].getText());
      assertEquals("\"The Wizard cannot be started on this display.  On Solaris,\"", tokencells[87].getText());
      assertEquals("+", tokencells[89].getText());
      assertEquals("System.getProperty", tokencells[91].getText());
      assertEquals("(",tokencells[92].getText());
      assertEquals("\"line.separator\"",tokencells[93].getText());
      assertEquals(")",tokencells[94].getText());
      assertEquals("+",tokencells[96].getText());
      assertEquals("\"you must allow the wizard access to the display.  \"",tokencells[98].getText());
      assertEquals("+",tokencells[100].getText());
      assertEquals("System.getProperty",tokencells[102].getText());
      assertEquals("(",tokencells[103].getText());
      assertEquals("\"line.separator\"",tokencells[104].getText());
      assertEquals(")",tokencells[105].getText());
      assertEquals("+",tokencells[107].getText());
      assertEquals("\"Try typing \\\"xhost +\\\" in another window.\"",tokencells[109].getText());
      assertEquals("}",tokencells[110].getText());
      assertEquals(",",tokencells[111].getText());
     

  }


}

