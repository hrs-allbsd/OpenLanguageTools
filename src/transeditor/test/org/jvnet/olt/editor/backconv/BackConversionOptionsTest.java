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
 * BackConversionOptionsTest.java
 *
 * Created on September 1, 2005, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/* TODO: remove or move to unit-tests */
package org.jvnet.olt.editor.backconv;

import java.util.HashMap;
import junit.framework.TestCase;
import org.jvnet.olt.xliff_back_converter.BackConverterProperties;

/**
 *
 * @author boris
 */
public class BackConversionOptionsTest extends TestCase{

    /** Creates a new instance of BackConversionOptionsTest */
    public BackConversionOptionsTest() {
    }

    private void testProps(BackConversionOptions opts,boolean value){
        opts.setGenerateTMX(value);
        opts.setOverwriteFiles(value);
        opts.setPreferXLZNames(value);
        opts.setWriteTransStatusToSGML(value);
        assertEquals(value,opts.isGenerateTMX());
        assertEquals(value,opts.isOverwriteFiles());
        assertEquals(value,opts.isPreferXLZNames());
        assertEquals(value,opts.isWriteTransStatusToSGML());
    }

    public void testSimpleProperties() throws Exception {
        BackConversionOptions opts = new BackConversionOptions();

        testProps(opts,true);
        testProps(opts,false);

        opts.setEncoding("XXX");
        HashMap m1 = new HashMap();
        HashMap m2 = new HashMap();
        opts.setUnicode2entityExcludeMap(m1);
        opts.setUnicode2entityIncludeMap(m2);

        assertEquals("XXX",opts.getEncoding());
        assertSame(m1,opts.getUnicode2entityExcludeMap());
        assertSame(m2,opts.getUnicode2entityIncludeMap());
    }

    public void testGeneratedBackconverterPropsTest() throws Exception{
        BackConversionOptions opts = new BackConversionOptions();

        opts.setEncoding("XXX");
        opts.setPreferXLZNames(true);
        opts.setWriteTransStatusToSGML(true);
        opts.setOverwriteFiles(true);

        HashMap m1 = new HashMap(){
            {
                put("x","y");
                put("1","2");
            }
        };
        HashMap m2 = new HashMap(){
            {
                put("a","b");
                put("c","d");
            }
        };

        opts.setUnicode2entityExcludeMap(m1);
        opts.setUnicode2entityIncludeMap(m2);

        BackConverterProperties props = opts.createBackConverterProperties();

        assertEquals("XXX",props.getProperty(BackConverterProperties.PROP_GEN_FILE_ENCODING));
        assertEquals(true,props.getBooleanProperty(BackConverterProperties.PROP_GEN_PREFER_XLZ_NAME));
        assertEquals(true,props.getBooleanProperty(BackConverterProperties.PROP_SGML_WRITE_TRANS_STATUS));
        assertEquals(true,props.getBooleanProperty(BackConverterProperties.PROP_GEN_OVERWRITE_FILES));

        assertTrue(m1.equals(props.getSGMLUnicode2EntityExcludeMap()));
        assertTrue(m2.equals(props.getSGMLUnicode2EntityIncludeMap()));
    }
}
