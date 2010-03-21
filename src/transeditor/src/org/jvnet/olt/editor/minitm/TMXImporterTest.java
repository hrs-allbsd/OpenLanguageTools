///*
// * TMXImporterTest.java
// *
// * Created on September 12, 2005, 5:09 PM
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//TODO: remove or move to test
//package org.jvnet.olt.editor.minitm;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.InputStream;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.sax.SAXSource;
//import javax.xml.transform.stream.StreamResult;
//import junit.framework.TestCase;
//import org.jvnet.olt.util.TestSupport;
//import org.jvnet.olt.xliff.XLIFFEntityResolver;
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.XMLReaderFactory;
///**
// *
// * @author boris
// */
//public class TMXImporterTest extends TestCase {
//
//    /** Creates a new instance of TMXImporterTest */
//    public TMXImporterTest() {
//    }
//
//    //High level view if the test passes at all...
//    public void testSanity() throws Exception{
//        File mtmFile = TestSupport.makeFile("tmximport","1.mtm");
//        File tmxFile = TestSupport.getResourceAsFile("tmximport/1.tmx");
//
//        TMXImporter imp = new TMXImporter();
//        imp.setSrcLang("en-US");
//        imp.setTgtLang("zh-CN");
//        imp.setSrcLangShort("US");
//        imp.setTgtLangShort("ZH");
//        imp.setTranslatorId("abcdef");
//
//        imp.convertTMX2MTM(tmxFile,mtmFile);
//
//	TestSupport.catFile(mtmFile);
//
//        assertTrue(TestSupport.findLine(mtmFile, "<minitm name=\"temp\" srclang=\"US\" tgtlang=\"ZH\">", true));
//        assertTrue(TestSupport.findLine(mtmFile, "<source>ABCDEF</source>", true));
//        assertTrue(TestSupport.findLine(mtmFile, "<translation>012345</translation>", true));
//        assertTrue(TestSupport.findLine(mtmFile, "<translatorId>abcdef</translatorId>", true));
//    }
//
//    public void testRawTransform() throws Exception{
//	File tmxFile = TestSupport.getResourceAsFile("tmximport/2.tmx");
//	File outFile = TestSupport.makeFile("tmximport","rawOut.xml");
//
//	InputStream is = TMXImporter.class.getClassLoader().getResourceAsStream("resources/tmx2mtm.xsl");
//
//	/*SAXParserFactory pf = SAXParserFactory.newInstance();
//	pf.setValidating(true);
//	pf.setNamespaceAware(true);
//	 */
//	XMLReader reader = XMLReaderFactory.createXMLReader();
//	reader.setEntityResolver(XLIFFEntityResolver.instance());
//
//	TransformerFactory tf = TransformerFactory.newInstance();
//	Transformer tr = tf.newTransformer(new SAXSource(new InputSource(is)));
//	tr.setParameter("srcLang","en-US");
//	tr.setParameter("tgtLang","zh-CN");
//	tr.setParameter("srcLangShort","US");
//	tr.setParameter("tgtLangShort","ZH");
//
//
//	tr.transform(new SAXSource(reader,new InputSource(new FileReader(tmxFile))),new StreamResult(new FileWriter(outFile)));
//
//
//    }
//
//}
