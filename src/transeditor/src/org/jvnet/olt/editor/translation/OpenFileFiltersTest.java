/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import junit.framework.TestCase;


/*
 * OpenFileFiltersTest.java
 *
 * Created on March 22, 2005, 3:03 PM
 */

/**
 *
 * @author boris
 */
public class OpenFileFiltersTest extends TestCase {
    /** Creates a new instance of OpenFileFiltersTest */
    public OpenFileFiltersTest() {
    }

    public void testXLFFilter() throws Exception {
        FileFilter f = OpenFileFilters.XLF_FILTER;

        assertFalse(f.accept(null));

        //accept files and directories;
        assertTrue(f.accept(new File(".")));
        assertTrue(f.accept(new File("x.xlf")));
        assertTrue(f.accept(new File("/ab/c/x.xlf")));
    }

    public void testXLZFilter() throws Exception {
        FileFilter f = OpenFileFilters.XLZ_FILTER;

        assertFalse(f.accept(null));

        //accept files and directories;
        assertTrue(f.accept(new File(".")));
        assertTrue(f.accept(new File("x.xlz")));
        assertTrue(f.accept(new File("/ab/c/x.xlz")));
    }
}
