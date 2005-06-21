/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * WriterXXTest.java
 *
 * Created on February 28, 2005, 4:58 PM
 */
package org.jvnet.olt.xliff;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.jvnet.olt.editor.util.TestSupport;


/**
 *
 * @author boris
 */
public class SAXWriterTest extends TestCase {
    /** Creates a new instance of WriterXXTest */
    public SAXWriterTest() {
    }

    /*
        public void testX() throws Exception {
            XLIFFSentence s1 = new XLIFFSentence("THE_TEST","en-US","a1");
            XLIFFSentence s2 = new XLIFFSentence("THE_TESTX","en-US","a2");

            File outFile = TestSupport.makeFile("xliff","test3.xlf");
            File inFile = TestSupport.getResourceAsFile("xliff/test3.xlf");
            File result = TestSupport.getResourceAsFile("xliff/test3x_result.xlf");

            SAXWriter wxx = new SAXWriter();

            wxx.saveSourceSentence(s1);
            wxx.saveSourceSentence(s2);

            wxx.write(new FileReader(inFile),new FileWriter(outFile));

            TestSupport.compareFiles(result, inFile);
        }
      */
}
