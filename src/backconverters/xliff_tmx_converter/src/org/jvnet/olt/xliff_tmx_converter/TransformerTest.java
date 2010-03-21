//
///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
//TODO: remove or move to testTODO: remove or move to test
//package org.jvnet.olt.xliff_tmx_converter;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import java.io.*;
//
//import junit.framework.*;
///**
// * A class to run and test the <code>XliffToTmxConverter</code>
// *
// * @author Brian Kidney
// * @created August 27, 2002
// */
//public class TransformerTest extends TestCase{
//
//    /*
//     * The Logger to be used
//     */
//    private static Logger logger =
//        Logger.getLogger("org.jvnet.olt.xliff_tmx_converter");
//
//
//    /**
//     * Constructor for the TransformerTest object
//     */
//    public TransformerTest() { }
//
//
//    public void testTransformer() throws Exception{
//        fail("modify this test to work!");
//    }
//    /**
//     * The main method to carry out the testing.
//     *
//     * @param args The arguments entered on the command line.
//     */
//    public static void main(String args[]) {
//
//        if (args.length != 1) {
//            System.out.println(
//                "Usage: <file.xlf>");
//            logger.warning("Incorrect parameters entered");
//            System.exit(1);
//        }
//
//        try {
//            File file = new File(args[0]);
//            String absolutePath = file.getAbsolutePath();
//            String pathToFiles = (absolutePath).substring(0,
//                absolutePath.lastIndexOf(File.separator) + 1);
//            logger.log(Level.FINEST, "pathToFiles = " + pathToFiles);
//
//            String fileName = file.getName();
//            String fileNamePrefix = fileName.substring(0,
//                fileName.lastIndexOf(".") + 1);
//            String outputFileName = fileNamePrefix + "tmx";
//
//            Reader xslFile = new
//                FileReader("resources"+File.separator+"xliff-tmx_MN.xsl");
//            Reader xliffDTD = new BufferedReader(new
//                FileReader("dtd"+File.separator+"xliff.dtd"));
//            Reader testXliffDTD = new BufferedReader(new
//                FileReader("dtd"+File.separator+"xliff.dtd"));
//            XliffToTmxTransformer transformer =
//                new XliffToTmxTransformer(logger, xslFile, xliffDTD, testXliffDTD);
//
//            String xliffFile = args[0];
//            /*FileInputStream fis = new FileInputStream(xliffFile);
//            Reader inputXliffFileTest = new InputStreamReader(new FileInputStream(xliffFile), "UTF8");
//
//            boolean translated = transformer.isFullyTranslated(inputXliffFileTest);
//            System.out.println("isFullyTranslated = " + translated);
//            inputXliffFileTest.close(); */
//
//            Writer output = new OutputStreamWriter(new
//                FileOutputStream(
//                outputFileName), "UTF8");
//            Reader inputXliffFile = new InputStreamReader(new FileInputStream(xliffFile), "UTF8");
//            transformer.doTransform(inputXliffFile, output);
//
//	    System.out.println("Please check the output file:"+outputFileName );
//        } catch (FileNotFoundException ex) {
//            logger.log(Level.SEVERE, "Unable to find file", ex);
//        } catch (IOException ex) {
//            logger.log(Level.SEVERE, "Unable to load XLIFF file", ex);
//        } catch (XliffToTmxTransformerException ex) {
//            logger.log(Level.SEVERE, "Unable to transform XLIFF file ", ex);
//        }
//    }
//
//}
//
