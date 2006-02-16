/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ParseTest.java
 * JUnit based test
 *
 * Created on 18 September 2002, 13:32
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.io.IOException;
import java.io.StringReader;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.textui.TestRunner;

/**
 *
 * @author jc73554
 */
public class ParseTest extends TestCase {
    
    public ParseTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ParseTest.class);
        return suite;
    }
    
    /**
     * Parse source with testVisitor
     * @return result of parse process
     */
    private String parse(String source) {
        String result = "";
        
        try {
            StringReader reader = new StringReader(source);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            TestVisitor testVisitor = new TestVisitor();
            parser.parse();
            parser.walkParseTree(testVisitor, null);
            result = testVisitor.getResult();
        } catch(ParseException e) {
            fail("Parsing Error: " + e.getMessage());
        } catch(Exception e) {
            fail("Parsing Error: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * Test empty string
     */
    public void testEmpty() {
        String sgml = "";
        String result = "";
        
        String parseResult = parse(sgml);
        assertEquals(result,parseResult);
    }
    
    /**
     * Test one tag with simle attribute
     */
    public void testSimpleAttr() {
        String sgml = "<para attr1=\"1\">";
        String result = "Name: para\n" + 
                        "Attribs:  attr1=\"1\"\n" +
                        "Data: <para attr1=\"1\">\n";
        
        String parseResult = parse(sgml);
        assertEquals(result,parseResult);
    }
    
    /**
     * Test one tag with more attributes
     */
    public void testMoreAttribs() {
        String sgml = "<para attr1=\"1\" attr2='2' attr3=3>";
        String result = "Name: para\n" +
                        "Attribs:  attr3=3 attr2='2' attr1=\"1\"\n" +
                        "Data: <para attr1=\"1\" attr2='2' attr3=3>\n";

        
        String parseResult = parse(sgml);
        assertEquals(result,parseResult);
    }
    
    /**
     * Test more tags
     */
    public void testMoreTags() {
        String sgml = "<para>\n" +
                      "<para attr=\"1\">Just a text</para>\n" +
                      "</para>\n";
        String result = "Name: para\n" +
                        "Attribs: \n" +
                        "Data: <para>\n" +
                        "Name: para\n" +
                        "Attribs:  attr=\"1\"\n" +
                        "Data: <para attr=\"1\">\n" +
                        "Name: para\n" +
                        "Attribs: \n" +
                        "Data: </para>\n" +
                        "Name: para\n" +
                        "Attribs: \n" +
                        "Data: </para>\n";
        
        String parseResult = parse(sgml);
        assertEquals(result,parseResult);
    }
    
    /**
     * Pretty comple example
     */
    public void testComplexExample() {
        String sgml =   "<para>\n" +
                        "<para attr=\"1\">Just a text</para>\n" +
                        "<para attr1 = \"attr attr\" att2 ='attr attr attr' att3=attr>\n" +
                        "attr\n" +
                        "</para>\n" +
                        "</para>\n";
        String result = "Name: para\n" +
                        "Attribs: \n" +
                        "Data: <para>\n" +
                        "Name: para\n" +
                        "Attribs:  attr=\"1\"\n" +
                        "Data: <para attr=\"1\">\n" +
                        "Name: para\n" +
                        "Attribs: \n" +
                        "Data: </para>\n" +
                        "Name: para\n" +
                        "Attribs:  attr1=\"attr attr\" att3=attr att2='attr attr attr'\n" +
                        "Data: <para attr1 = \"attr attr\" att2 ='attr attr attr' att3=attr>\n" +
                        "Name: para\n" +
                        "Attribs: \n" +
                        "Data: </para>\n" +
                        "Name: para\n" +
                        "Attribs: \n" +
                        "Data: </para>\n";
        
        String parseResult = parse(sgml);
        assertEquals(result,parseResult);
    }
    
}
