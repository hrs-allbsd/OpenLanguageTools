
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
import java.util.Arrays;
import org.jvnet.olt.minitm.*;
import org.jvnet.olt.fuzzy.*;

public class MoreSGMLMatchTests 
extends TestCase
{
  private BasicFuzzySearchMiniTM minitm;

  public MoreSGMLMatchTests(String name)
  {
    super(name);
  }
  public void setUp()
  {
    try
    {
      minitm = new BasicFuzzySearchMiniTM("weird-sgml.mtm",
					      true,
					      "SGML MiniTM",
					      "en",
					      "de");
      
    }
    catch(Exception ex) { fail(ex.getMessage());}
  }

  public void tearDown()
  {
    minitm = null;
    File file = new File("weird-sgml.mtm");
    if(file.exists()) { file.delete(); }
    
  }
  public void testMatchQuality()
  {
    try
    {
      AlignedSegment as = new AlignedSegment("What's Changed in the Solaris 9 Operating Environment highlights and describes the new features of the <trademark>Solaris</trademark> 8 operating environment.","The Translation", "johnc");

      minitm.addNewSegment(as);

      TMMatch[] matches = minitm.getMatchFor("The Solaris operating environment runs on three types of hardware, or platforms - SPARC, IA and <trademark>MAC</trademark>.", 60, 5);
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
      
    }
    catch(Exception ex) { fail(ex.getMessage());}
  }

  public void testMatchQualityB()
  {
    try
    {
      AlignedSegment as = new AlignedSegment("Contact the IHV directly to get support for these controllers, which have been certified using a third-party driver.","The Translation", "johnc");

      minitm.addNewSegment(as);

      TMMatch[] matches = minitm.getMatchFor("PXE network boot is available only for devices that implement the Intel Preboot Execution Environment specification.", 70, 5);
      if(matches.length > 0)
      {
	for(int t = 0; t < matches.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     matches[t].getDataSourceKey() + ", " +
			     matches[t].getRatioOfMatch() + " }");
	}
      }
      assertEquals(0, matches.length);
      
    }
    catch(Exception ex) { fail(ex.getMessage());}
  }
  public void testMatchQualityC()
  {
    try
    {
      AlignedSegment as = new AlignedSegment("IA based systems that use the Intel Pentium Pro and subsequently released Intel CPUs can address up to 32 Gbytes of memory. ","The Translation", "johnc");

      minitm.addNewSegment(as);

      TMMatch[] matches = minitm.getMatchFor("PXE network boot is available only for devices that implement the Intel Preboot Execution Environment specification. ", 70, 5);
      if(matches.length > 0)
      {
	for(int t = 0; t < matches.length; t++)
	{
	  System.out.println("Result " + t + " { " + 
			     matches[t].getDataSourceKey() + ", " +
			     matches[t].getRatioOfMatch() + " }");
	}
      }
      assertEquals(0, matches.length);
      
    }
    catch(Exception ex) { fail(ex.getMessage());}
  }

}
