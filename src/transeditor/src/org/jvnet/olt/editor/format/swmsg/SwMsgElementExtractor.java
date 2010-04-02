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
 * SwMsgElementExtractor.java
 *
 * Created on 07 June 2004
 */
package org.jvnet.olt.editor.format.swmsg;

import java.io.StringReader;

import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.laxparsers.software.LaxSoftwareMessageParser;
import org.jvnet.olt.laxparsers.software.ParseException;


//import org.jvnet.olt.parsers.tagged.SegmenterTable;
public class SwMsgElementExtractor implements FormatElementExtractor {
    private GlobalVariableManager gvm;

    public SwMsgElementExtractor(GlobalVariableManager gvm) {
        this.gvm = gvm;
    }

    public PivotBaseElement[] extractBaseElements(String text) {
        StringReader reader = new StringReader(text);

        //  Create parser
        LaxSoftwareMessageParser parser = new LaxSoftwareMessageParser(reader);

        //  Parse input text
        try {
            parser.parse();
        } catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();
        }

        //  Create a visitor
        SwMsgBaseElementExtractorVisitor visitor = new SwMsgBaseElementExtractorVisitor(gvm);

        try {
            parser.walkParseTree(visitor, null);
        } catch (Exception ex) {
            //  Do stuff here
            ex.printStackTrace();
        }

        return visitor.getBaseElements();
    }

    public String extractText(String formattedText) {
        StringReader reader = new StringReader(formattedText);

        //  Create parser
        LaxSoftwareMessageParser parser = new LaxSoftwareMessageParser(reader);

        //  Parse input text
        try {
            parser.parse();
        } catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();

            //  Can NOT parse the text, return itself
            return formattedText;
        }

        //  Create a visitor
        SwMsgBaseElementExtractorVisitor visitor = new SwMsgBaseElementExtractorVisitor(gvm);

        try {
            parser.walkParseTree(visitor, null);
        } catch (Exception ex) {
            //  Do stuff here
            ex.printStackTrace();
        }

        return visitor.getMessageData();
    }

    public String markupFormatForDisplay(String formattedText) {
        /*
        StringReader reader = new StringReader(formattedText);

        //  Create parser
        LaxSoftwareMessageParser parser = new LaxSoftwareMessageParser(reader);

        //  Parse input text
        try {
            parser.parse();
        }
        catch (ParseException parseEx) {
            //  Do stuff here
            parseEx.printStackTrace();
        }*/
        return "";
    }

    public void setVariableManager(org.jvnet.olt.format.GlobalVariableManager gvm) {
        //  Do nothing here
    }
}
