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
package org.jvnet.olt.editor.translation.preview;

import java.io.InputStream;

import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * User: boris
 * Date: Feb 11, 2005
 * Time: 9:58:05 AM
 */
public class FastFailHanlder extends DefaultHandler {
    private static final Logger logger = Logger.getLogger(FastFailHanlder.class.getName());
    private boolean foundXliff = false;
    private static final int threshold = 100;
    private int count;
    private String srcLang;
    private String tgtLang;
    private FilePreviewPane filePreviewPane;

    public FastFailHanlder(FilePreviewPane filePreviewPane) {
        this.filePreviewPane = filePreviewPane;
    }

    public void startDocument() throws SAXException {
        foundXliff = false;
        count = 0;
        srcLang = null;
        tgtLang = null;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //we need to know if this is xliff file
        if (!foundXliff) {
            if (!"xliff".equals(qName)) {
                filePreviewPane.abort(false);
            }

            foundXliff = true;

            return;
        }

        //been searching for too long
        if (count++ > threshold) {
            filePreviewPane.abort(false);
        }

        if ("file".equals(qName)) {
            srcLang = attributes.getValue("source-language");
            tgtLang = attributes.getValue("target-language");
            filePreviewPane.abort(true);
        }
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (systemId.endsWith("xliff.dtd")) {
            InputStream istream = this.getClass().getResourceAsStream("/dtd/xliff.dtd");

            if (istream != null) {
                InputSource source = new InputSource(istream);

                return source;
            } else {
                logger.finer("DTD InputStream was null!");

                //TODO throw an Exception ??
                return null;
            }
        }

        return null;
    }

    public String getSrcLang() {
        return srcLang;
    }

    public String getTgtLang() {
        return tgtLang;
    }
}
