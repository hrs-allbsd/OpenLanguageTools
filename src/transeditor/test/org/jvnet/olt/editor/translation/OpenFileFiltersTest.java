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
    File correctFile1 = new File("/a/b/c/x.xlf");
    File correctFile2 = new File("/a/b/c/y.xliff");
    File correctFile3 = new File("/a/b/c/z.xlz");

    File dir = new File("/a/b/c/");
    File noExt1 = new File("/a/b/c/c");
    File noExt2 = new File("/a/b/c/c.");

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
        assertTrue(f.accept(new File("/ab/c/x.xliff")));
    }

    public void testXLZFilter() throws Exception {
        FileFilter f = OpenFileFilters.XLZ_FILTER;

        assertFalse(f.accept(null));

        //accept files and directories;
        assertTrue(f.accept(new File(".")));
        assertTrue(f.accept(new File("x.xlz")));
        assertTrue(f.accept(new File("/ab/c/x.xlz")));
    }


    public void testGetExtension() throws Exception {

        assertEquals("xlf",OpenFileFilters.getExtension(correctFile1));
        assertEquals("xliff",OpenFileFilters.getExtension(correctFile2));
        assertEquals("xlz",OpenFileFilters.getExtension(correctFile3));

        assertNull(OpenFileFilters.getExtension(dir));
        assertNull(OpenFileFilters.getExtension(noExt1));
        assertNull(OpenFileFilters.getExtension(noExt2));
    }

    public void testGetKnownExtensions() throws Exception {
        assertTrue(OpenFileFilters.isFileNameXLF(correctFile1));
        assertTrue(OpenFileFilters.isFileNameXLF(correctFile2));

        assertFalse(OpenFileFilters.isFileNameXLF(dir));


        assertTrue(OpenFileFilters.isFileNameXLZ(correctFile3));
        assertFalse(OpenFileFilters.isFileNameXLZ(dir));
    }
}
