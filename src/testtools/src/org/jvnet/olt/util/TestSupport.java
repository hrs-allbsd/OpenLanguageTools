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
package org.jvnet.olt.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;

import java.net.URL;

import junit.framework.Assert;
import org.jvnet.olt.util.FileUtils;


/** Set of utility methods to make unit testing simpler
 *
 * @author boris
 */
public class TestSupport {
    /** Creates a new instance of TestSupport */
    private TestSupport() {
    }

    /** compare files line by line.
     *  Whitespace is *not* ignored
     */
    static public void compareFiles(File f, File f2) throws IOException {
        compareFiles(f,f2,false);
    }
    /** compare files line by line.
     *
     *  If files are not identical will throw IllegalStateException.
     *  If ignoreWhiteSpace is true, lines are trimmed before they're compared
     */
    static public void compareFiles(File f, File f2,boolean ignoreWhiteSpace) throws IOException {
        LineNumberReader lnr1 = new LineNumberReader(new FileReader(f));
        LineNumberReader lnr2 = new LineNumberReader(new FileReader(f2));

        int ln = 0;
        do {
            ++ln;

            String line = lnr1.readLine();
            String line2 = lnr2.readLine();


            if ((line == null) && (line2 == null)) {
                return;
            }
            if(ignoreWhiteSpace){
                line = line.trim();
                line2 = line2.trim();
            }

            Assert.assertEquals(f+" vs. "+f2+" line: "+ln,line, line2);
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
        Object prop = System.getProperties();
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

    static public void catFile(File f) throws IOException{
	Reader r = new FileReader(f);
	try{
	    LineNumberReader lnr = new LineNumberReader(r);

	    int ln = 0;
	    do{
		String line = lnr.readLine();
		if(line == null)
		    break;
		System.out.println(++ln+":"+line);
	    }
	    while(true);
	}
	finally {
	    if(r != null)
		r.close();
	}
    }
}
