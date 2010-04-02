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

package org.jvnet.olt.minitm;

import junit.framework.*;
import java.util.List;
import java.util.ListIterator;
import org.jvnet.olt.parsers.sgmltokens.MarkupEntry;

public class TextExtractionTest
extends TestCase
{
  
  public TextExtractionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest( new TextExtractionTest("testPlainTextInput") );
    suite.addTest( new TextExtractionTest("testMarkedUpInput") );
    suite.addTest( new TextExtractionTest("testMarkupHandling") );
    suite.addTest( new TextExtractionTest("testMarkedSection") );

    return suite;
  }

  public void testPlainTextInput()
  {
    try
    {
      String string = "This is a plain text string without markup.";
      StringCleaningAdapter cleaner = new StringCleaningAdapter(string);
      
      String output = cleaner.getPlainText();
      assertEquals(string, output);
    }
    catch(MiniTMException ex){ fail(ex.getMessage()); }
  }

  public void testMarkedUpInput()
  {
    try
    {
      String string = "This is a <b>marked up</b> text <a href=\"string reference\">string</a> with tags.";
      StringCleaningAdapter cleaner = new StringCleaningAdapter(string);
      
      String output = cleaner.getPlainText();
      assertEquals("This is a marked up text string with tags.", output);
    }
    catch(MiniTMException ex){ fail(ex.getMessage()); }
  }

  public void testMarkupHandling()
  {
    try
    {
      String string = "This is a <b>marked up</b> text <a href=\"string reference\">string</a> with tags.";
      StringCleaningAdapter cleaner = new StringCleaningAdapter(string);
      
      List markupList = cleaner.getMarkup();
      if(markupList.size() != 4)
      {
	ListIterator iterator = markupList.listIterator();
	while(iterator.hasNext())
	{
	  MarkupEntry mue = (MarkupEntry) iterator.next();
	  System.out.println(mue.getMarkupText());
	}
      }
      assertEquals(4, markupList.size());
      
      MarkupEntry entry = (MarkupEntry) markupList.get(2);
      
      assertEquals("<a href=\"string reference\">", entry.getMarkupText());
      assertEquals(new Boolean(entry.hasAttributes()), new Boolean(true));
    }
    catch(MiniTMException ex){ fail(ex.getMessage()); }
  }

  public void testMarkedSection()
  {
    try
    {
       String string = "This string <sunw_format type=\"ignore\"><![%foo;[has]]></sunw_format><sunw_format type=\"include\"><![%bar;[had]]></sunw_format> marked sections in it.";
      StringCleaningAdapter cleaner = new StringCleaningAdapter(string);

      String output = cleaner.getPlainText();
      assertEquals("This string had marked sections in it.", output);     
    }
    catch(MiniTMException ex){ fail(ex.getMessage()); }     
  }
}
