
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

package org.jvnet.olt.format;

import junit.framework.*;
import org.jvnet.olt.format.sgml.FormatExtractionTest;
import org.jvnet.olt.format.sgml.FormatComparisonTest;
import org.jvnet.olt.format.sgml.SgmlFormatWrapperTest;
import org.jvnet.olt.format.xml.XmlFormatWrapperTest;

public class FormattingTestSuite
extends TestCase
{

  public FormattingTestSuite(String name)
  {
    super(name);
  }

  public static void main(java.lang.String[] args)
  {
     junit.textui.TestRunner.run(suite());
  }

  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    
    suite.addTest( FormatExtractionTest.suite() );
    suite.addTest( FormatComparisonTest.suite() );
    suite.addTest( SgmlFormatWrapperTest.suite() );
 
    return suite;
  }
}
