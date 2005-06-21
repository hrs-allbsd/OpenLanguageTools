
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
 * SgmlFormatElementExtractorTest.java
 * JUnit based test
 *
 * Created on 09 January 2004, 13:57
 */

package org.jvnet.olt.editor.format.sgml;

import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.editor.format.FormatElementExtractor;
import java.io.StringReader;
import org.jvnet.olt.laxparsers.sgml.ParseException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;
import junit.framework.*;

/**
 *
 * @author jc73554
 */
public class SgmlFormatElementExtractorTest extends TestCase {
    
    public SgmlFormatElementExtractorTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(SgmlFormatElementExtractorTest.class);
        return suite;
    }
    
    protected void runTestArray(String testString, int[] expectedTypes, String[] expectedStrings) {
        GlobalVariableManager gvm = new EntityManager();
        SgmlFormatElementExtractor extractor = new SgmlFormatElementExtractor(gvm);
        
        PivotBaseElement[] elements = extractor.extractBaseElements(testString);
        if(expectedStrings.length != elements.length) {
            for(int i = 0; i < elements.length; i++) {
                System.out.println(elements[i].getContent());
            }
        }
        
        assertEquals("Number of base elements is different than expected", expectedTypes.length,elements.length);
        assertEquals("Number of base elements is different than expected", expectedStrings.length,elements.length);
        
        for(int i = 0; i < elements.length; i++) {
            assertEquals("Base element ("+i+") has incorrect type.", expectedTypes[i], elements[i].getElemType());
            assertEquals("Base element ("+i+") has string incorrect.", expectedStrings[i], elements[i].getContent() );
        }
    }
    
    public void testEmptyString() {
        String testString = "";
        int[] expectedTypes = { };
        String[] expectedStrings = { };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    
    public void testIgnoredMarkedSection() {
        String testString = "<![ IGNORE [ This is a string in an <b>ignored</b> marked section. ]]>";
        int[] expectedTypes = { PivotBaseElement.MRK_ELEM };
        String[] expectedStrings = { "<![ IGNORE [ This is a string in an <b>ignored</b> marked section. ]]>" };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testNestedIgnoredMarkedSection() {
        String testString = "<![ IGNORE [ This is a string in an <![INCLUDE[ignored]]> marked section. ]]>";
        int[] expectedTypes = { PivotBaseElement.MRK_ELEM };
        String[] expectedStrings = { "<![ IGNORE [ This is a string in an <![INCLUDE[ignored]]> marked section. ]]>" };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    
    public void testIncludedMarkedSection() {
        String testString = "<![ INCLUDE [ This is a string in an <b>included</b> marked section. ]]>";
        int[] expectedTypes = { PivotBaseElement.BPT_ELEM,
        PivotBaseElement.TEXT,
        PivotBaseElement.BPT_ELEM,
        PivotBaseElement.TEXT,
        PivotBaseElement.EPT_ELEM,
        PivotBaseElement.TEXT,
        PivotBaseElement.EPT_ELEM
        };
        
        String[] expectedStrings = { "<![ INCLUDE [",
        " This is a string in an ",
        "<b>",
        "included",
        "</b>",
        " marked section. ",
        "]]>"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testEntities() {
        String testString = "This is a string with &ent; entities. &foo";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.PH_ELEM,
        PivotBaseElement.TEXT,
        PivotBaseElement.PH_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "&ent;",
        " entities. ",
        "&foo"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testTags() {
        String testString = "This is a string with <tag> tags. </tag>";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.BPT_ELEM,
        PivotBaseElement.TEXT,
        PivotBaseElement.EPT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<tag>",
        " tags. ",
        "</tag>"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testBrokenTags() {
        String testString = "This is a string with <tag  broken tags. </tag";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.BPT_ELEM,
        PivotBaseElement.EPT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<tag  broken tags. ",
        "</tag"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    
    public void testCdataSections() {
        String testString = "This is a string with <![CDATA[ section. ]]>";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.IT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<![CDATA[ section. ]]>",
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testBrokenComment() {
        String testString = "This is a string with <!-- a broken comment";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.IT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<!-- a broken comment"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testComment() {
        String testString = "This is a string with <!-- a comment -->";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.IT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<!-- a comment -->"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
    public void testPI() {
        String testString = "This is a string with <?PI pi text>";
        int[] expectedTypes = { PivotBaseElement.TEXT,
        PivotBaseElement.IT_ELEM
        };
        
        String[] expectedStrings = { "This is a string with ",
        "<?PI pi text>"
        };
        
        runTestArray(testString, expectedTypes, expectedStrings);
    }
    
//    public void testTagWithTagInAttribute() {
//        String testString = "<tag attribute=\"<tag in_value>\">";
//        int[] expectedTypes = { PivotBaseElement.BPT_ELEM
//        };
//        
//        String[] expectedStrings = { "<tag attribute=\"<tag in_value>\">"
//        };
//        
//        runTestArray(testString, expectedTypes, expectedStrings);
//    }
     
}
