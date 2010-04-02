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
 * XLIFFFastFailParserTest.java
 *
 * Created on April 25, 2005, 12:39 PM
 *
 */
package org.jvnet.olt.xliff;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 *
 * @author boris
 */
public class XLIFFFastFailParserTest extends TestCase {
    XMLReader xmlReader;
    XLIFFFastFailParser ffp;

    /** Creates a new instance of XLIFFFastFailParserTest */
    public XLIFFFastFailParserTest() {
    }

    protected void setUp() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        SAXParser saxParser = factory.newSAXParser();

        //saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
        xmlReader = saxParser.getXMLReader();
    }

    void parse(String xml) throws SAXException, IOException {
        xmlReader.parse(new InputSource(new StringReader(xml)));
    }

    XLIFFFastFailParser setUpParser(int type) {
        ffp = new XLIFFFastFailParser(type);

        xmlReader.setContentHandler(ffp);
        xmlReader.setErrorHandler(ffp);
        xmlReader.setEntityResolver(ffp);

        return ffp;
    }

    public void testWrongSearchType() throws Exception {
        try {
            setUpParser(3);
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testFailOnXLIFF() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_VERSION);
            parse("<?xml version='1.0'?>\n" + "<a><b/></a>");

            fail();
        } catch (XLIFFFastFailParser.FailureException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, fe.getStatus());
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, ffp.getStatus());
            assertNull(ffp.getVersion());
        }
    }

    public void testFailOnXLIFFMissingVersion() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_VERSION);

            parse("<?xml version='1.0'?>\n" + "<xliff></xliff>");

            fail();
        } catch (XLIFFFastFailParser.FailureException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, fe.getStatus());
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, ffp.getStatus());
            assertNull(ffp.getVersion());
        }
    }

    public void testFailOnXLIFFEmptyVersion() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_VERSION);

            parse("<?xml version='1.0'?>\n" + "<xliff version=''></xliff>");

            fail();
        } catch (XLIFFFastFailParser.FailureException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, fe.getStatus());
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, ffp.getStatus());
            assertNull(ffp.getVersion());
        }
    }

    public void testFailOnXLIFFWrongVersion() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_VERSION);

            parse("<?xml version='1.0'?>\n" + "<xliff version='.0'></xliff>");

            fail();
        } catch (XLIFFFastFailParser.FailureException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, fe.getStatus());
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_XLIFF, ffp.getStatus());
            assertNull(ffp.getVersion());
        }
    }

    public void testVersionSuccess() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_VERSION);

            parse("<?xml version='1.0'?>\n" + "<xliff version='1.0'>" + "<file sourceLanguage='en-US' targetLanguage='ja-JP'/>" + "</xliff>");

            fail();
        } catch (XLIFFFastFailParser.SuccessException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_SUCCESS, ffp.getStatus());
            assertTrue(ffp.getVersion().isXLIFF10());

            assertNull(ffp.getSourceLanguage());
            assertNull(ffp.getTargetLanguage());
        }
    }

    //Testing langs search below
    public void testFailOnTrashHold() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_LANGS);
            ffp.setElementsTrashHold(1);

            parse("<?xml version='1.0'?>\n" + "<xliff version='1.0'><a/><a/><b/></xliff>");

            fail();
        } catch (XLIFFFastFailParser.FailureException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_TRESHHOLD, fe.getStatus());
            assertEquals(XLIFFFastFailParser.STATUS_FAILED_ON_TRESHHOLD, ffp.getStatus());
            assertTrue(ffp.getVersion().isXLIFF10());
        }
    }

    public void testLangsSuccess() throws Exception {
        try {
            setUpParser(XLIFFFastFailParser.SEARCH_TYPE_LANGS);

            parse("<?xml version='1.0'?>\n" + "<xliff version='1.0'>" + "<file source-language='en-US' target-language='ja-JP'/>" + "</xliff>");

            fail();
        } catch (XLIFFFastFailParser.SuccessException fe) {
            assertEquals(XLIFFFastFailParser.STATUS_SUCCESS, ffp.getStatus());
            assertTrue(ffp.getVersion().isXLIFF10());

            assertEquals("en-US", ffp.getSourceLanguage());
            assertEquals("ja-JP", ffp.getTargetLanguage());
        }
    }

    protected void tearDown() throws Exception {
        ffp = null;
        xmlReader = null;
    }
}
