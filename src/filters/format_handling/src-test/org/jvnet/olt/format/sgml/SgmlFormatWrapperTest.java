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

/*
 * SgmlFormatWrapperTest.java
 * JUnit based test
 *
 * Created on 30 July 2002, 15:59
 */

package org.jvnet.olt.format.sgml;

import junit.framework.*;
import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;

/**
 *
 * @author jc73554
 */
public class SgmlFormatWrapperTest 
extends TestCase
{
    private GlobalVariableManager m_entityManager;
    
    public SgmlFormatWrapperTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        
        suite.addTest(new SgmlFormatWrapperTest("testPlainFormatting"));
        suite.addTest(new SgmlFormatWrapperTest("testCommentAndPiFormatting"));
        suite.addTest(new SgmlFormatWrapperTest("testEntityHandling"));
        suite.addTest(new SgmlFormatWrapperTest("testIncludedMarkedSectFormatting"));
        suite.addTest(new SgmlFormatWrapperTest("testIgnoredMarkedSectFormatting"));
        suite.addTest(new SgmlFormatWrapperTest("testCDataFormatting"));
        
        return suite;
    }

    public void setUp()
    { 
        m_entityManager = new EntityManager();
       try {
        m_entityManager.setVariable("foo.bar", "INCLUDE","PARAMETER");
        m_entityManager.setVariable("wibble", "IGNORE","PARAMETER");
        m_entityManager.setVariable("narf", "%wibble;","PARAMETER");    
       } catch (GlobalVariableManagerException e){
           System.out.println("FATAL : Error trying to setup test " + e.getMessage());
       }
    }

    //  Helper method 
    protected void runTest(String sgml, String expected)
    {
        SgmlFormatWrapper wrapper = new SgmlFormatWrapper(m_entityManager);
        try
        {
            String actual = wrapper.wrapFormatting(sgml);
            assertEquals(expected, actual);
        }
        catch(InvalidFormattingException exInvalid)
        {
            //exInvalid.printStackTrace();
            fail("InvalidFormattingException thrown : " + exInvalid.getMessage());
        }
    }
    
    
    /** Test of wrapFormatting method, of class org.jvnet.olt.format.sgml.SgmlFormatWrapper. */
    public void testPlainFormatting()
    {
        String sgml = "This <b>is</b> a piece of <i>formatted</i> text.";
        String expected = "This <bpt id=\"1\">&lt;b&gt;</bpt>is<ept id=\"1\">&lt;/b&gt;</ept> a piece of <bpt id=\"2\">&lt;i&gt;</bpt>formatted<ept id=\"2\">&lt;/i&gt;</ept> text.";
        
        runTest(sgml, expected);
    }
    
    public void testCDataFormatting()
    {
        String sgml = "Text with  a <![CDATA[CDATA ]]>section.";
        String expected = "Text with  a <it id=\"1\" pos=\"open\">&lt;![CDATA[CDATA ]]&gt;</it>section.";
        
        runTest(sgml, expected);
    }

    public void testIncludedMarkedSectFormatting()
    {
        String sgml = "<![ %foo.bar; [This text <b>is</b> included. ]]>";
        String expected = "<bpt id=\"1\">&lt;![ %foo.bar; [</bpt>This text <bpt id=\"2\">&lt;b&gt;</bpt>is<ept id=\"1\">&lt;/b&gt;</ept> included. <ept id=\"2\">]]&gt;</ept>";
        
        runTest(sgml, expected);
    }

    public void testIgnoredMarkedSectFormatting()
    {
        String sgml = "<![ %narf; [This text <b>is</b> included. ]]>";
        String expected = "<mrk mtype=\"protect\">&lt;![ %narf; [This text &lt;b&gt;is&lt;/b&gt; included. ]]&gt;</mrk>";
        
        runTest(sgml, expected);
    }
    
    public void testEntityHandling()
    {
        String sgml = "A string with &amp; entities.";
        String expected = "A string with &amp;amp; entities.";
        
        runTest(sgml, expected);
    }

    
    public void testCommentAndPiFormatting()
    {
        String sgml = "<!-- a comment --> This is some text <?Boo a pi ?> with comments and PIs.";
        String expected = "<it id=\"1\" pos=\"open\">&lt;!-- a comment --&gt;</it> This is some text <it id=\"2\" pos=\"open\">&lt;?Boo a pi ?&gt;</it> with comments and PIs.";
        
        runTest(sgml, expected);
    }
    
    
}
