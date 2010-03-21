///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
///*
// * HandlerTestBase.java
// *
// * Created on April 19, 2005, 3:46 PM
// *
// */
//TODO: remove or move to test
//package org.jvnet.olt.xliff.reader.handlers;
//
//import java.io.Reader;
//import java.io.StringReader;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import junit.framework.TestCase;
//
//import org.jvnet.olt.editor.translation.Constants;
//import org.jvnet.olt.xliff.handlers.ParserX;
//
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//
//
///**
// *
// * @author boris
// */
//public class HandlerTestBase extends TestCase {
//    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
//    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
//
//    /** Creates a new instance of HandlerTestBase */
//    public HandlerTestBase() {
//    }
//
//    private Reader makeReader(String xml) {
//        return new StringReader(xml);
//    }
//
//    protected void parse(String xml, ParserX parser) throws Exception {
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        factory.setValidating(false); //the code was generated according DTD
//        factory.setNamespaceAware(true); //the code was generated according DTD
//
//        SAXParser saxParser = factory.newSAXParser();
//        saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
//
//        XMLReader rdr = saxParser.getXMLReader();
//        rdr.setContentHandler(parser);
//
//        Reader r = makeReader(xml);
//        rdr.parse(new InputSource(r));
//    }
//}
