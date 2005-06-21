
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
import java.util.Hashtable;
import java.io.*;
import org.xml.sax.*;
import org.jvnet.olt.minitm.TMUnit;
import javax.xml.parsers.*;

public class ContentHandlerTest
extends TestCase
{
    private Hashtable hash;
    
    public ContentHandlerTest(String name)
    {
        super(name);
    }
    
    public void setUp()
    {
        
        hash = new Hashtable();
        
        try
        {
            IdSequence sequence = new IdSequence();
            
            String string =   "<?xml version=\"1.0\" ?>\n" +
            //	"<!DOCTYPE minitm SYSTEM \"minitm.dtd\" >\n" +
            "<minitm name=\"sample\">\n" +
            "  <entry>\n" +
            "    <source>The first source segment.</source>\n" +
            "    <translation>Le premier translation.</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "  <entry>\n" +
            "    <source>foo</source>\n" +
            "    <translation>bar</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "  <entry>\n" +
            "    <source>Once upon a time, in a galaxy, far, far, away...</source>\n" +
            "    <translation>The starwars quote translation would go here.</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "</minitm>\n";
            StringReader reader = new StringReader(string);
            InputSource is = new InputSource(reader);
            
            MiniTmContentHandler handler = new MiniTmContentHandler(hash, sequence);
            
            SAXParserFactory factory = org.apache.xerces.jaxp.SAXParserFactoryImpl.newInstance();
            
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            
            factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
            XMLReader parser = factory.newSAXParser().getXMLReader();
            
            parser.setContentHandler(handler);
            parser.setErrorHandler(handler);
            
            parser.parse(is);
        }
        catch(Exception ex)
        { fail(ex.getMessage()); }
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        
        suite.addTest( new ContentHandlerTest("testIdOne") );
        suite.addTest( new ContentHandlerTest("testIdTwo") );
        suite.addTest( new ContentHandlerTest("testIdThree") );
        
        suite.addTest( new CharEntityTest("testEntityConversion") );
        
        return suite;
    }
    
    //
    //  The tests!
    //
    public void testIdOne()
    {
        TMUnit unit = (TMUnit) hash.get( new Long(1L));
        assertNotNull(unit);
        assertEquals("The first source segment.", unit.getSource());
        assertEquals("Le premier translation.", unit.getTranslation());
        assertEquals("johnc", unit.getTranslatorID());
    }
    
    public void testIdTwo()
    {
        TMUnit unit = (TMUnit) hash.get( new Long(2L));
        assertNotNull(unit);
        assertEquals("foo", unit.getSource());
        assertEquals("bar", unit.getTranslation());
        assertEquals("johnc", unit.getTranslatorID());
    }
    
    public void testIdThree()
    {
        TMUnit unit = (TMUnit) hash.get( new Long(3L));
        assertNotNull(unit);
        assertEquals("Once upon a time, in a galaxy, far, far, away...", unit.getSource());
        assertEquals("The starwars quote translation would go here.", unit.getTranslation());
        assertEquals("johnc", unit.getTranslatorID());
    }
    
}
