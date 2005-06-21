/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * FileUtilsTest.java
 *
 * Created on March 4, 2005, 2:41 PM
 */
package org.jvnet.olt.editor.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import junit.framework.TestCase;


/**
 *
 * @author boris
 */
public class FileUtilsTest extends TestCase {
    File srcFile;
    File tgtFile;

    /** Creates a new instance of FileUtilsTest */
    public FileUtilsTest() {
    }

    protected void setUp() throws Exception {
        srcFile = TestSupport.makeFile("fileUtils", "testCopyStreamToFile.txt");
        tgtFile = TestSupport.makeFile("fileUtils", "testCopyStreamToFile_dest.txt");
    }

    protected void tearDown() throws Exception {
        /*

        if(srcFile != null && srcFile.exists())
            srcFile.delete();
        if(tgtFile != null && tgtFile.exists())
            tgtFile.delete();
        */
    }

    public void testCopyStreamToFile() throws Exception {
        FileWriter fw = new FileWriter(srcFile);

        try {
            int dummy = 0;

            for (int line = 0; line < 1000; line++) {
                while ((++dummy % 50) != 0)
                    fw.write("" + dummy);

                fw.write("\n");
            }

            fw.close();

            FileUtils.copyStreamToFile(new FileReader(srcFile), tgtFile);

            TestSupport.compareFiles(srcFile, tgtFile);
        } finally {
            if (fw != null) {
                fw.close();
            }
        }
    }
}
