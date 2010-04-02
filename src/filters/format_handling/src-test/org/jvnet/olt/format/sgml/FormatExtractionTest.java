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

package org.jvnet.olt.format.sgml;

import java.util.HashMap;
import junit.framework.*;
import java.io.StringReader;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.FormatItem;


public class FormatExtractionTest
extends TestCase {
    private GlobalVariableManager m_entityManager;
    
    public FormatExtractionTest(String name) {
        super(name);
    }
    
    public void setUp() {
        m_entityManager = new EntityManager();
        try {
            m_entityManager.setVariable("foo.bar", "INCLUDE","PARAMETER");
            m_entityManager.setVariable("wibble", "IGNORE","PARAMETER");
            m_entityManager.setVariable("narf", "%wibble;","PARAMETER");
        } catch (GlobalVariableManagerException e){
            System.err.println("FATAL : Error trying to setup test " + e.getMessage());
        }
    }
    
    public void testSgmlFormatExtraction() {
        //  Create an SgmlFormatExtractor
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "<b><a href=\"\">A link</a> with some text</b>, and also an image <img src=\"foo.svg\" alt=\"<img src=\"foo.svg\">\">";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", formats.size(), 5);
            
            FormatItem item = (FormatItem) formats.get("<B>");
            assertNotNull("Format, '<B>', not detected.", item);
            assertEquals("Penalty on '<B>' not correct.", 2, item.getPenalty());
            
            item = (FormatItem) formats.get("<IMG SRC=\"FOO.SVG\" ALT=\"<IMG SRC=\"FOO.SVG\">\">");
            assertNotNull("Image tag not detected correctly.", item);
            assertEquals("Penalty on '<IMG>' not correct.", 5, item.getPenalty());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
  /*
   *  This test always fails at the moment due to the fact that the case folding
   *  in tags for the comparison methods, folds the case of the entire tag,
   *  including attribute values. It should not really do this, but the case
   *  where tags differ only in attribute values is to rare to justify fixing
   *  at the moment.
   */
    public void testSGMLAttributeValueHandling() {
        //  Create an SgmlFormatExtractor
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "An image <img src=\"foo.svg\" alt=\"<img src=\"foo.svg\">\">";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", formats.size(), 1);
            
            FormatItem item = (FormatItem) formats.get("<IMG SRC=\"foo.svg\" ALT=\"<img src=\"foo.svg\">\">\">");
            assertNotNull("Attribute values in the <IMG> tag are not handled correctly.", item);
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testCDataHandling() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![CDATA[CDATA section. ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 1, formats.size());
            
            FormatItem item = (FormatItem) formats.get("<![CDATA[CDATA section. ]]>");
            assertNotNull(item);
            assertEquals("Penalty on the CDATA section is incorrect.", 5, item.getPenalty());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
        
    }
    
    public void testMarkedSectionHandling1() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![ %foo.bar; [ This is a marked section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 2, formats.size());
            
            
            
            FormatItem item = (FormatItem) formats.get("<![ %foo.bar; [");
            assertNotNull(item);
            assertEquals("Penalty on the Marked section is incorrect.", 2, item.getPenalty());
            
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testMarkedSectionHandling2() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![ %wibble; [ This is a marked section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 1, formats.size());
            
            
            
            FormatItem item = (FormatItem) formats.get("<![ %wibble; [ This is a marked section ]]>");
            assertNotNull(item);
            assertEquals("Penalty on the Marked section is incorrect.", 5, item.getPenalty());
            
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testMarkedSectionHandling3() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![ %narf; [ This is a marked section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 1, formats.size());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    public void testMarkedSectionHandling4() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![INCLUDE[ This is a marked section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 2, formats.size());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testMarkedSectionHandling5() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![IGNORE[ This is a marked section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 1, formats.size());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testMarkedSectionHandling6() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![IGNORE[ This is a <bold>marked</bold> section ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 1, formats.size());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public void testNestedMarkedSectionHandling() {
        SgmlFormatExtractor extractor = new SgmlFormatExtractor();
        String sgml = "Some text followed by <![INCLUDE[ <![IGNORE[ This is <![INCLUDE[a <bold>marked</bold>]]> section ]]> ]]>.";
        
        try {
            //  Pass it a test string
            HashMap formats = extractor.getFormatting(sgml, m_entityManager);
            
            //  Check the hash that is returned.
            assertEquals("Number of formats detected was incorrect.", 3, formats.size());
        }
        catch(InvalidFormattingException ex) {
            fail("Parsing of the string failed");
        }
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTest( new FormatExtractionTest("testSgmlFormatExtraction"));
        suite.addTest( new FormatExtractionTest("testSGMLAttributeValueHandling"));
        suite.addTest( new FormatExtractionTest("testCDataHandling"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling1"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling2"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling3"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling4"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling5"));
        suite.addTest( new FormatExtractionTest("testMarkedSectionHandling6"));
        suite.addTest( new FormatExtractionTest("testNestedMarkedSectionHandling"));
        
        return suite;
    }
}
