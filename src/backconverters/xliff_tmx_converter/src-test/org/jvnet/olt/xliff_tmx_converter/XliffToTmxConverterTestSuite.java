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
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_tmx_converter;

import junit.framework.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;

import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import java.util.zip.CRC32;
import org.jvnet.olt.xliff_tmx_converter.XliffToTmxTransformer;
public class XliffToTmxConverterTestSuite
    extends TestCase {
        
    /*
     * The Logger to be used
     */
    private static Logger logger =
        Logger.getLogger("org.jvnet.olt.xliff_tmx_converter");
    
    public XliffToTmxConverterTestSuite(String name) {
        super(name);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
		return new TestSuite(XliffToTmxConverterTestSuite.class);
	}
    
    public void setUp() {   
    }
    
//    public void testTransformer_Output_Xliff () {
//        try {
//
//            Reader xslFile = new
//                FileReader("../transtech-build/resource/xliff-tmx.xsl");
//            XliffToTmxTransformer transformer =
//                new XliffToTmxTransformer(logger, xslFile);
//            File outputFile =
//                new File("../output/XliffTmxConverter/output.tmx");
//            Writer output = new FileWriter(outputFile);
//            Reader inputXliffFile = new FileReader(
//                "../transtech-build-tests/testdocs/output.xlf");
//            transformer.doTransform(inputXliffFile, output);
//            assertNotNull(output);
//
//            output.flush();
//            output.close();
//
//            File testFile = new
//                File("../transtech-build-tests/controldocs/output.tmx");
//
//            assertEquals(getCheckSum(outputFile), getCheckSum(testFile));
//
//        } catch (FileNotFoundException ex) {
//            fail("Transformation failed - Unable to find file " + ex);
//        } catch (IOException ex) {
//            fail("Transformation failed - Unable to load XLIFF file " + ex);
//        } catch (XliffToTmxTransformerException ex) {
//            fail("Transformation failed - Unable to transform XLIFF file " +
//            ex);
//        } catch (Exception ex) {
//            fail("Exception " + ex);
//        }
//    }
    
    private long getCheckSum(File file) throws Exception {
        
        CheckedInputStream inFile = null;
        InputStream fileStream = new FileInputStream(file);
        Checksum cs = new CRC32();
            
        inFile = new CheckedInputStream(fileStream, cs); 
        int data;
        while ( (data = inFile.read()) != -1);
         
        inFile.close();
        
        return inFile.getChecksum().getValue();
    }
    
    
}
