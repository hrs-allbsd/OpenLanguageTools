/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.io.File;
import java.io.FileNotFoundException;

import junit.framework.TestCase;
import org.jvnet.olt.editor.backconv.BackConversionOptions;


/**
 * User: boris
 * Date: Feb 18, 2005
 * Time: 10:25:18 AM
 */
public class JBackConverterExecutorTest extends TestCase {
    JBackConverterExecutor exec;
    File tempFile;

    public JBackConverterExecutorTest() {
    }

    protected void setUp() throws Exception {
        BackConversionOptions opts = new BackConversionOptions();
        opts.setWriteTransStatusToSGML(false);
        exec = new JBackConverterExecutor(opts.createBackConverterProperties(), "UTF-8", false);

        tempFile = File.createTempFile("abc", "def");
    }

    protected void tearDown() throws Exception {
        tempFile.delete();
    }

    public void testSetSource() throws Exception {
        try {
            exec.setSourceFile("/hope/this/file/does/not/exist");
            fail();
        } catch (FileNotFoundException fnfe) {
            //OK
        }

        //don't know how to safely test file I can not read
    }

    public void testConvertFailWhenTargetNotSet() throws Exception {
        exec.setSourceFile(tempFile.getAbsolutePath());
        exec.convert();
    }

    public void testSetTargetWithNoSource() throws Exception {
        try {
            exec.setTargetFile(null);
            fail();
        } catch (IllegalStateException fnfe) {
            //OK
        }
    }

    public void testSetTargetSource() throws Exception {
        try {
            exec.setSourceFile(tempFile.getAbsolutePath());
            exec.setTargetFile(null);

            exec.convert();
        } catch (IllegalArgumentException iae) {
            fail("should pass");
        } catch (Exception e) {
        }
    }

    public void testSetTargetSourceIsFile() throws Exception {
        try {
            exec.setSourceFile(tempFile.getAbsolutePath());
            exec.setTargetFile(tempFile.getAbsolutePath());
            fail();
        } catch (IllegalArgumentException iae) {
            //OK the target may not be a file
        }
    }
}
