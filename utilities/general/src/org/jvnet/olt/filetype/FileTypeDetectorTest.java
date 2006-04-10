/*
 * FileUtilsTest.java
 *
 * Created on March 17, 2006, 10:00 AM
 *
 */

package org.jvnet.olt.filetype;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintStream;
import junit.framework.TestCase;



public class FileTypeDetectorTest extends TestCase {
    
    File htmlFile = null;
    File xmlFile = null;
    
    
    public FileTypeDetectorTest() {
    }
    
    protected void setUp() throws Exception {
        htmlFile = File.createTempFile("fileutil","");
        xmlFile = File.createTempFile("fileutil","");
    }
    
    protected void tearDown() throws Exception {
        if(htmlFile!=null) {
            htmlFile.delete();
        }
        
        if(xmlFile!=null) {
            xmlFile.delete();
        }
    }
    
    
    public void testExtensionDetection() throws IOException{
        assertEquals(FileTypeDetector.detectFileType(new File("htmlfile.html")).intValue(),FileTypeDetector.HTML);
        assertEquals(FileTypeDetector.detectFileType(new File("manpage.1m")).intValue(),FileTypeDetector.SGML);
        assertEquals(FileTypeDetector.detectFileType(new File("readme.txt")).intValue(),FileTypeDetector.TEXT);
        assertEquals(FileTypeDetector.detectFileType(new File("somebook.book")).intValue(),FileTypeDetector.BOOK);
    }

    
    public void testAdvancedDetection() throws IOException{
        PrintStream ps = new PrintStream(htmlFile);
        ps.print("\n<html>\n<body>\n</body>\n</html>\n");
        ps.close();
        assertEquals(FileTypeDetector.detectFileType(htmlFile).intValue(),FileTypeDetector.HTML);
        
        ps = new PrintStream(xmlFile);
        ps.print("\n<?xml version='1.0' encoding='UTF-8'?>\n<some>\n</some>\n");
        ps.close();
        assertEquals(FileTypeDetector.detectFileType(xmlFile).intValue(),FileTypeDetector.XML);
    }
    
}
