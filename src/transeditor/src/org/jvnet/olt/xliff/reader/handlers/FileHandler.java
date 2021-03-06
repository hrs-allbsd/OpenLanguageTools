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
 * FileHandler.java
 *
 * Created on April 18, 2005, 5:04 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import org.jvnet.olt.editor.format.DefaultFormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractorFactory;
import org.jvnet.olt.editor.format.VariableManagerFactory;
import org.jvnet.olt.editor.util.BaseElements;
import org.jvnet.olt.format.GlobalVariableManager;


/**
 *
 * @author boris
 */
public class FileHandler extends BaseHandler {
    /** Creates a new instance of FileHandler */
    public FileHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) {
        if (start) {
            // Get XLIFF source datatype here.
            String originalDataType = element.getAttrs().getValue("datatype");

            if (originalDataType != null) {
                originalDataType = originalDataType.toUpperCase();
            } else {
                // prevent NullPointerException if XLIFF validation is disabled
                originalDataType = "plaintext";
            }

            ctx.setOriginalDataType(originalDataType);

            //  Add new stuff for setting the Format Extractor
            FormatElementExtractor extractor = null;

            try {
                VariableManagerFactory varManagerFactory = new VariableManagerFactory();
                GlobalVariableManager gvm = varManagerFactory.createVariableManager(originalDataType);

                FormatElementExtractorFactory factory = new FormatElementExtractorFactory();
                extractor = factory.createFormatExtractor(originalDataType, gvm);
            } catch (Exception ex) {
                //  throw new SAXException(ex.getMessage());
                //  We need to be a bit less brittle here. Use a default extractor that
                //  does nothing if we encounter a type we cannot handle.
                logger.throwing(getClass().getName(), "start_file", ex);
                logger.warning("Format will not be highlighted in the Editor's editing window.");
                extractor = new DefaultFormatElementExtractor();
            }

            BaseElements.setFormatExtractor(extractor);

            //  end new format handling code
            ctx.setSourceLanguage(element.getAttrs().getValue("source-language"));
            ctx.setTargetLanguage(element.getAttrs().getValue("target-language"));

            logger.finest(ctx.getSourceLanguage() + " => " + ctx.getTargetLanguage());

            ctx.addFile(element.getAttrs().getValue("original"));
        } else {
            ctx.commitFile();
        }
    }
}
