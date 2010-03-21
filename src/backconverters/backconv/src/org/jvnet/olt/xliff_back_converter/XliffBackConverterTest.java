///*
// * XliffBackConverterTest.java
// *
// * Created on April 24, 2006, 5:58 PM
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//TODO: remove or move to test
//package org.jvnet.olt.xliff_back_converter;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//import org.jvnet.olt.util.TestSupport;
//import junit.framework.TestCase;
//import org.jvnet.olt.backconv.BackConverter;
//
///**
// *
// * @author boris
// */
//public class XliffBackConverterTest extends TestCase{
//
//    /** Creates a new instance of XliffBackConverterTest */
//    public XliffBackConverterTest() {
//    }
//
//
//
//    public void testConvertSGML1() throws Exception {
//        String[] files = new String[]{
//
//            "test-data/properties/props2.properties.xlz", "test-data/properties/props2.properties",
//            "test-data/xml/xml1.xml.xlz", "test-data/xml/xml1.xml",
//            "test-data/sgml/sgml2.sgm.xlz", "test-data/sgml/sgml2.sgm",
//            "test-data/sgml/sgml1.sgm.xlz", "test-data/sgml/sgml1.sgm",
//            "test-data/po/po2.po.xlz", "test-data/po/po2.po",
//            "test-data/po/po1.po.xlz", "test-data/po/po1.po",
//            "test-data/html/html1.html.xlz", "test-data/html/html1.html",
//            "test-data/properties/props1.properties.xlz", "test-data/properties/props1.properties",
//
//        };
//
//        for (int i = 0; i < files.length; i+=2) {
//            File xlz = TestSupport.getResourceAsFile(files[i]);
//            File backFile = TestSupport.getResourceAsFile(files[i+1]);
//
//            File outDir = TestSupport.makeFile("result/",null);
//
//            BackConverterProperties props = new BackConverterProperties();
//            props.setBooleanProperty(BackConverterProperties.PROP_GEN_OVERWRITE_FILES,true);
//
//            System.out.println("i:"+i+" "+xlz+" "+backFile+" "+outDir);
//
//
//            BackConverter bc = new BackConverter(props);
//
//            bc.backConvert(xlz,outDir.getAbsolutePath(),false,"UTF-8");
//
//
//            String xFile = xlz.getName();
//
//            if(xFile.endsWith(".xlz")){
//                File resultFile = new File(outDir,xFile.substring(0,xFile.length()-4));
//                TestSupport.compareFiles(backFile,resultFile,true);
//            } else
//                fail("the source .xlz file name does NOT end with .xlz");
//        }
//
//        //TestSupport.compareFile()
//    }
//
//    public void testOpenOfficeFiles() throws Exception {
//        String[] files = new String[]{
//            "test-data/ooo/SETUP_GUIDE.odt.xlz", "test-data/ooo/content.xml",
//        };
//
//        for (int i = 0; i < files.length; i+=2) {
//            File xlz = TestSupport.getResourceAsFile(files[i]);
//            File backFile = TestSupport.getResourceAsFile(files[i+1]);
//
//            File outDir = TestSupport.makeFile("result/",null);
//
//            BackConverterProperties props = new BackConverterProperties();
//            props.setBooleanProperty(BackConverterProperties.PROP_GEN_OVERWRITE_FILES,true);
//
//            System.out.println("i:"+i+" "+xlz+" "+backFile+" "+outDir);
//
//
//            BackConverter bc = new BackConverter(props);
//
//            bc.backConvert(xlz,outDir.getAbsolutePath(),false,"UTF-8");
//
//
//            String xFile = xlz.getName();
//
//            if(xFile.endsWith(".xlz")){
//                File resultFile = new File(outDir,xFile.substring(0,xFile.length()-4));
//                File resultXMLFile = new File(resultFile.getParentFile(),"content.xml");
//
//                ZipInputStream zis = null;
//                try{
//                    zis = new ZipInputStream(new FileInputStream(resultFile));
//
//                    ZipEntry e = null;
//                    do{
//                        e = zis.getNextEntry();
//
//                        if(e == null || "content.xml".equals(e.getName()))
//                            break;
//                    }
//                    while(true);
//
//                    assertNotNull(e);
//
//                    FileOutputStream fos = new FileOutputStream(resultXMLFile);
//
//                    byte[] buffer = new byte[3072];
//                    try{
//                        do{
//                            int read = zis.read(buffer);
//                            if(  read == -1)
//                                break;
//                            fos.write(buffer,0,read);
//                        }
//                        while(true);
//                    }
//                    finally {
//                        fos.close();
//                    }
//
//                    TestSupport.compareFiles(backFile,resultXMLFile,true);
//                } finally {
//                    if(zis != null)
//                        zis.close();
//                }
//            } else
//                fail("the source .xlz file name does NOT end with .xlz");
//        }
//    }
//}
