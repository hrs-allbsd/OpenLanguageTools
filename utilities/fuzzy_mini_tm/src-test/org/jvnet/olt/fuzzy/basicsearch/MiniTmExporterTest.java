
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.*;
import java.util.*;
import org.jvnet.olt.minitm.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

public class MiniTmExporterTest
extends TestCase
{
    private Hashtable hash;
    private String strMiniTM;
    
    public MiniTmExporterTest(String name)
    {
        super(name);
    }
    
    public void setUp()
    {
        hash = new Hashtable();
        
        try
        {
            IdSequence sequence = new IdSequence();
            strMiniTM =   "<?xml version=\"1.0\" encoding=\"utf8\" ?>\n" +
            "<minitm name=\"sample\" srclang=\"en\" tgtlang=\"de\">\n" +
            "\t<entry>\n" +
            "\t\t<source>The first source segment.</source>\n" +
            "\t\t<translation>A translation &amp; more!</translation>\n" +
            "\t\t<translatorId>johnc</translatorId>\n" +
            "\t</entry>\n" +
            "\t<entry>\n" +
            "\t\t<source>foo &lt; bar</source>\n" +
            "\t\t<translation>bar &gt; foo</translation>\n" +
            "\t\t<translatorId>johnc</translatorId>\n" +
            "\t</entry>\n" +
            "\t<entry>\n" +
            "\t\t<source>Once upon a time, in a galaxy, far, far, away...</source>\n" +
            "\t\t<translation>The starwars quote translation would go here.</translation>\n" +
            "\t\t<translatorId>johnc</translatorId>\n" +
            "\t</entry>\n" +
            "</minitm>\n";
            
            StringReader reader = new StringReader(strMiniTM);
            InputSource is = new InputSource(reader);
            
            MiniTmContentHandler handler = new MiniTmContentHandler(hash, sequence);
            //SAXParserFactory factory = org.apache.xerces.jaxp.SAXParserFactoryImpl.newInstance();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            
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
        
        suite.addTest( new MiniTmExporterTest("testNormalBehaviour") );
        suite.addTest( new MiniTmExporterTest("testNormalRemoteBehaviour") );
        suite.addTest( new MiniTmExporterTest("testMissingPathBehaviour") );
        
        return suite;
    }
    
    //
    //  The tests
    //
    public void testNormalBehaviour()
    {
        String tmFile = "test.mtm";
        
        //  Write out the file.
        MiniTmExporter exporter = new MiniTmExporter();
        try
        {
            exporter.export(hash, tmFile, "sample", "en", "de");
        }
        catch(DataStoreException ex)
        { fail(ex.getMessage()); }
        
        //  Read back and compare
        try
        {
            File fileTm = new File(tmFile);
            FileInputStream istream = new FileInputStream(fileTm);
            InputStreamReader reader = new InputStreamReader(istream, "UTF8");
            
            StringBuffer buffer = new StringBuffer();
            int ch;
            while((ch = reader.read()) != -1)
            {
                buffer.append((char) ch);
            }
            //  Only testing that the string lengths are the same as the
            //  strings can get reordered in the hash.
            assertEquals(strMiniTM.length(), buffer.toString().length());
        }
        catch(IOException ioEx)
        { fail(ioEx.getMessage()); }
        
    }
    
    public void testNormalRemoteBehaviour()
    {
        String tmFile = "/tmp/test.mtm";
        
        //  Write out the file.
        MiniTmExporter exporter = new MiniTmExporter();
        try
        {
            exporter.export(hash, tmFile, "sample", "en", "de");
        }
        catch(DataStoreException ex)
        { fail(ex.getMessage()); }
        
        //  Read back and compare
        try
        {
            File fileTm = new File(tmFile);
            FileInputStream istream = new FileInputStream(fileTm);
            InputStreamReader reader = new InputStreamReader(istream, "UTF8");
            
            StringBuffer buffer = new StringBuffer();
            int ch;
            while((ch = reader.read()) != -1)
            {
                buffer.append((char) ch);
            }
            //  Only testing that the string lengths are the same as the
            //  strings can get reordered in the hash.
            assertEquals(strMiniTM.length(), buffer.toString().length());
        }
        catch(IOException ioEx)
        { fail(ioEx.getMessage()); }
    }
    
    public void testMissingPathBehaviour()
    {
        String tmFile = generateRandomFilePath();
        
        //  Write out the file.
        MiniTmExporter exporter = new MiniTmExporter();
        try
        {
            exporter.export(hash, tmFile, "sample", "en", "de");
        }
        catch(DataStoreException ex)
        { fail(ex.getMessage()); }
        
        //  Read back and compare
        try
        {
            File fileTm = new File(tmFile);
            FileInputStream istream = new FileInputStream(fileTm);
            InputStreamReader reader = new InputStreamReader(istream, "UTF8");
            
            StringBuffer buffer = new StringBuffer();
            int ch;
            while((ch = reader.read()) != -1)
            {
                buffer.append((char) ch);
            }
            //  Only testing that the string lengths are the same as the
            //  strings can get reordered in the hash.
            assertEquals(strMiniTM.length(), buffer.toString().length());
        }
        catch(IOException ioEx)
        { fail(ioEx.getMessage()); }
    }
    
    protected String generateRandomFilePath()
    {
        Random random = new Random();
        
        return ("/tmp/jc" + random.nextInt() + "/test.mtm");
    }
    
}
