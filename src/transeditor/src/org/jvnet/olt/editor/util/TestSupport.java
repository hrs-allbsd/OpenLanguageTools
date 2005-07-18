/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * TestSupport.java
 *
 * Created on February 28, 2005, 8:23 PM
 */
package org.jvnet.olt.editor.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;

import java.net.URL;

import junit.framework.Assert;


/** Set of utility methods to make unit testing simpler
 *
 * @author boris
 */
public class TestSupport {
    /** Creates a new instance of TestSupport */
    private TestSupport() {
    }

    /** compare files line by line.
     *
     *  If files are not identical will throw IllegalStateException.
     */
    static public void compareFiles(File f, File f2) throws IOException {
        LineNumberReader lnr1 = new LineNumberReader(new FileReader(f));
        LineNumberReader lnr2 = new LineNumberReader(new FileReader(f2));

        do {
            String line = lnr1.readLine();
            String line2 = lnr2.readLine();

            if ((line == null) && (line2 == null)) {
                return;
            }

            Assert.assertEquals(line, line2);
        } while (true);
    }

    static public InputStream getResource(String name) throws NullPointerException {
        return TestSupport.class.getClassLoader().getResourceAsStream(name);
    }

    static public File getResourceAsFile(String name) {
        URL url = TestSupport.class.getClassLoader().getResource(name);

        if (url == null) {
            throw new NullPointerException("Resource " + name + " could not be found");
        }

        String s = url.getFile();

        return new File(s);
    }

    /** make file filename in subDir all under pre-defined directory.
     *
     *  Creates direcotry and file under direcotry defined by sys property junit.output.dir
     *  If filename is null returns only the direcotry
     */
    static public File makeFile(String subDir, String filename) throws IllegalStateException {
        String dirPath = System.getProperties().getProperty("junit.output.dir");

        Assert.assertNotNull("junit.output.dir is missing",dirPath);

        File fullDir = new File(dirPath, subDir);
        fullDir.mkdirs();

        return (filename == null) ? fullDir : new File(fullDir, filename);
    }

    /** copy file source to file destination.
     * Both need to be files
     */
    static public void copyFile(File source, File destination) throws IOException {
        FileUtils.copyFiles(source, destination);
    }

    /** find exact line in file.
     *
     *  Tries to find a line in file. Uses LineNumberReader and FileReader with default
     *  encoding.
     *
     *  @see findLine(Reader,String,boolean)
     */
    static public boolean findLine(File f, String xline, boolean doTrim) throws IOException {
        return findLine(new FileReader(f), xline, doTrim);
    }

    /** Tries to find exact line in file.
     *
     * Uses LineNumberReader. If doTrim is specified trims each line before comparing.
     * When first line is found returns true.
     *
     */
    static public boolean findLine(Reader reader, String xline, boolean doTrim) throws IOException {
        LineNumberReader lnr = null;

        try {
            lnr = new LineNumberReader(reader);

            do {
                String line = lnr.readLine();

                if (line == null) {
                    return false;
                }

                if (line.equals(xline)) {
                    return true;
                }

                if (doTrim && line.trim().equals(xline)) {
                    return true;
                }
            } while (true);
        } finally {
            if (lnr != null) {
                lnr.close();
            }
        }
    }
}
