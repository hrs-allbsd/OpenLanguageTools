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
 * WriterXXTest.java
 *
 * Created on February 28, 2005, 4:58 PM
 */
package org.jvnet.olt.xliff;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.editor.translation.Configuration;

import org.jvnet.olt.util.TestSupport;

/**
 *
 * @author boris
 */
public class SAXWriterTest extends TestCase {
    /** Creates a new instance of WriterXXTest */
    public SAXWriterTest() {
        // try to initialize backend
        try {
            File home = new File(System.getProperty("user.home") + File.separator + ".xliffeditor" + File.separator);
            File minitmDir = new File(home, "mini-tm");
            Configuration cfg = new Configuration(home, minitmDir);
            Backend backend = Backend.instance(cfg);
        } catch (Exception e) {};
    }


        public void testX() throws Exception {
            XLIFFSentence s1 = new XLIFFSentence("THE_TEST in Japanese","ja-JP","a1(0)");
            XLIFFSentence s2 = new XLIFFSentence("THE_TESTX","en-US","a2(0)");

            File outFile = TestSupport.makeFile("xliff","test3.xlf");
            File inFile = TestSupport.getResourceAsFile("xliff/test3.xlf");
            File resultFile = TestSupport.getResourceAsFile("xliff/test3_result.xlf");

            SAXWriter wxx = new SAXWriter(Version.XLIFF_1_0);

            wxx.saveTargetLanguageCode("ja-JP");
            s1.setTranslationState("approved");
            s1.setTranslationStateQualifier("100-Match");
            wxx.saveTargetSentence(s1);
            wxx.saveSourceSentence(s2);

            FileWriter fw = new FileWriter(outFile);
            wxx.write(new FileReader(inFile),fw, true);
            fw.flush();
            fw.close();

            TestSupport.compareFiles(outFile, resultFile);
        }

}
