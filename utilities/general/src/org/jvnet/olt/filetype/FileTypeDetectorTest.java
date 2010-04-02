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
///*
// * FileUtilsTest.java
// *
// * Created on March 17, 2006, 10:00 AM
// *
// */
// TODO: remove or move to TEST
//package org.jvnet.olt.filetype;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.FileWriter;
//import java.io.PrintStream;
//import java.io.FileOutputStream;
//import junit.framework.TestCase;
//
//
//
//public class FileTypeDetectorTest extends TestCase {
//
//    File htmlFile = null;
//    File xmlFile = null;
//
//
//    public FileTypeDetectorTest() {
//    }
//
//    protected void setUp() throws Exception {
//        htmlFile = File.createTempFile("fileutil","");
//        xmlFile = File.createTempFile("fileutil","");
//    }
//
//    protected void tearDown() throws Exception {
//        if(htmlFile!=null) {
//            htmlFile.delete();
//        }
//
//        if(xmlFile!=null) {
//            xmlFile.delete();
//        }
//    }
//
//
//    public void testExtensionDetection() throws IOException{
//        assertEquals(FileTypeDetector.detectFileType(new File("htmlfile.html")).intValue(),FileTypeDetector.HTML);
//        assertEquals(FileTypeDetector.detectFileType(new File("manpage.1m")).intValue(),FileTypeDetector.SGML);
//        assertEquals(FileTypeDetector.detectFileType(new File("readme.txt")).intValue(),FileTypeDetector.TEXT);
//        assertEquals(FileTypeDetector.detectFileType(new File("somebook.book")).intValue(),FileTypeDetector.BOOK);
//    }
//
//
//    public void testAdvancedDetection() throws IOException{
//        PrintStream ps = new PrintStream(new FileOutputStream(htmlFile));
//        ps.print("\n<html>\n<body>\n</body>\n</html>\n");
//        ps.close();
//        assertEquals(FileTypeDetector.detectFileType(htmlFile).intValue(),FileTypeDetector.HTML);
//
//        ps = new PrintStream(new FileOutputStream(xmlFile));
//        ps.print("\n<?xml version='1.0' encoding='UTF-8'?>\n<some>\n</some>\n");
//        ps.close();
//        assertEquals(FileTypeDetector.detectFileType(xmlFile).intValue(),FileTypeDetector.XML);
//    }
//
//}
