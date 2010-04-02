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
 * Created on April 26, 2005, 2:13 PM
 *
 */
package org.jvnet.olt.xliff.writer.handlers;

import java.io.IOException;
import java.util.logging.Logger;

import org.jvnet.olt.xliff.ReaderException;
import org.jvnet.olt.xliff.handlers.Element;
import org.jvnet.olt.xliff.handlers.Handler;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 *
 * @author boris
 */
public class FileHandler extends BaseHandler {
    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());
    String targetLanguage;

    public FileHandler(Context ctx) {
        super(ctx);
    }

    public void dispatch(org.jvnet.olt.xliff.handlers.Element element, boolean start) throws ReaderException {
        Element e1 = element;

        if (start && "file".equals(element.getLocalName())) {
            Attributes attrs = element.getAttrs();
            ctx.addFile(attrs.getValue("original"));

            targetLanguage = ctx.getTargetLang();
            logger.finer("Target language="+targetLanguage);
            
            if ("file".equals(element.getLocalName()) && (targetLanguage == null || !targetLanguage.equals(attrs.getValue("target-language"))) ) {
                AttributesImpl impl = new AttributesImpl(attrs);
                setAttributeValue(impl, "target-language", targetLanguage);

                e1 = new Element(element.getPrefix(), element.getLocalName(), element.getOriginalQName(), impl, element.getPath());
            }
        }

        writeElement(e1, start);
    }

    public void dispatchIgnorableChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public void dispatchChars(Element element, char[] chars, int start, int length) throws ReaderException {
        writeChars(chars, start, length);
    }

    public boolean handleSubElements() {
        return true;
    }
}
