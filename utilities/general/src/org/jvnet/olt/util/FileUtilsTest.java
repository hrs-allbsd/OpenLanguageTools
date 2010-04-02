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
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
///*
// * FileUtilsTest.java
// *
// * Created on March 4, 2005, 2:41 PM
// */
// TODO: remove or move to TEST
//package org.jvnet.olt.util;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//
//import junit.framework.TestCase;
//
//
///**
// *
// * @author boris
// */
//public class FileUtilsTest extends TestCase {
//    File srcFile;
//    File tgtFile;
//
//    /** Creates a new instance of FileUtilsTest */
//    public FileUtilsTest() {
//    }
//
//    protected void setUp() throws Exception {
//        srcFile = TestSupport.makeFile("fileUtils", "testCopyStreamToFile.txt");
//        tgtFile = TestSupport.makeFile("fileUtils", "testCopyStreamToFile_dest.txt");
//    }
//
//    protected void tearDown() throws Exception {
//        /*
//
//        if(srcFile != null && srcFile.exists())
//            srcFile.delete();
//        if(tgtFile != null && tgtFile.exists())
//            tgtFile.delete();
//        */
//    }
//
//    public void testCopyStreamToFile() throws Exception {
//        FileWriter fw = new FileWriter(srcFile);
//
//        try {
//            int dummy = 0;
//
//            for (int line = 0; line < 1000; line++) {
//                while ((++dummy % 50) != 0)
//                    fw.write("" + dummy);
//
//                fw.write("\n");
//            }
//
//            fw.close();
//
//            FileUtils.copyStreamToFile(new FileReader(srcFile), tgtFile);
//
//            TestSupport.compareFiles(srcFile, tgtFile);
//        } finally {
//            if (fw != null) {
//                fw.close();
//            }
//        }
//    }
//}
