/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ParserXTest.java
 * JUnit based test
 *
 * Created on April 18, 2005, 12:51 PM
 */
package org.jvnet.olt.xliff.handlers;

import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


/**
 *
 * @author boris
 */
public class ParserXTest extends junit.framework.TestCase {
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    public ParserXTest(String testName) {
        super(testName);
    }

    private Reader makeReader(String xml) {
        return new StringReader(xml);
    }

    private void parse(String xml, ParserX parser) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false); //the code was generated according DTD
        factory.setNamespaceAware(true); //the code was generated according DTD

        SAXParser saxParser = factory.newSAXParser();
        saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

        XMLReader rdr = saxParser.getXMLReader();
        rdr.setContentHandler(parser);

        Reader r = makeReader(xml);
        rdr.parse(new InputSource(r));
    }

    public void testSimpleHandler() throws Exception {
        ParserX p = new ParserX();

        final List elems = new ArrayList();
        final int[] startsEnds = new int[2];

        p.addHandler("/a", new Handler() {
                public void dispatch(Element element, boolean start) {
                    elems.add(element);
                    startsEnds[start ? 0 : 1]++;
                }

                public boolean handleSubElements() {
                    return true;
                }
            });

        parse("<?xml version='1.0'?>\n<a><b/></a>", p);

        System.out.println("Elems:" + elems);
        assertTrue(!elems.isEmpty());
        assertEquals(4, elems.size());
        assertEquals(2, startsEnds[0]);
        assertEquals(2, startsEnds[1]);
    }

    public void testNamespaceHandler() throws Exception {
        ParserX p = new ParserX();

        final List elems = new ArrayList();
        final int[] startsEnds = new int[2];

        Map prefixes = new HashMap();
        prefixes.put("aURI", "x");
        prefixes.put("bURI", "y");
        p.setPrefixMap(prefixes);

        p.addHandler("/x:a", new Handler() {
                public void dispatch(Element element, boolean start) {
                    elems.add(element);
                    startsEnds[start ? 0 : 1]++;
                }

                public boolean handleSubElements() {
                    return true;
                }
            });

        String xml = "<?xml version='1.0'?>\n" + "<a xmlns='aURI' xmlns:b='bURI'>" + "   <b:b/>" + "</a>";

        parse(xml, p);

        System.out.println("Elems:" + elems);
        assertTrue(!elems.isEmpty());
        assertEquals(4, elems.size());
        assertEquals(2, startsEnds[0]);
        assertEquals(2, startsEnds[1]);
    }

    public void testMultipleNamespaceHandlers() throws Exception {
        ParserX p = new ParserX();

        final List elems = new ArrayList();
        final List elems2 = new ArrayList();
        final StringBuffer texts = new StringBuffer();
        final int[] startsEnds = new int[2];

        Map prefixes = new HashMap();
        prefixes.put("aURI", "x");
        prefixes.put("bURI", "y");
        p.setPrefixMap(prefixes);

        p.addHandler("/x:a", new Handler() {
                public void dispatch(Element element, boolean start) {
                    elems.add(element);
                    startsEnds[start ? 0 : 1]++;
                }
            });
        p.addHandler("/x:a/y:b", new Handler() {
                StringBuffer b;
                Element last;

                public void dispatch(Element element, boolean start) {
                    elems2.add(element);
                    startsEnds[start ? 0 : 1]++;
                }

                public void dispatchChars(Element element, char[] chars, int from, int lenght) {
                    texts.append(chars, from, lenght);
                }
            });

        String xml = "<?xml version='1.0'?>\n" + "<a xmlns='aURI' xmlns:b='bURI'>" + "   <b:b>HELLO</b:b>" + "</a>";

        parse(xml, p);

        System.out.println("Elems:" + elems);
        assertTrue(!elems.isEmpty());
        assertTrue(!elems2.isEmpty());
        assertEquals(2, elems.size());
        assertEquals(2, elems2.size());
        assertEquals(2, startsEnds[0]);
        assertEquals(2, startsEnds[1]);
        assertEquals("HELLO", texts.toString());
    }
}
