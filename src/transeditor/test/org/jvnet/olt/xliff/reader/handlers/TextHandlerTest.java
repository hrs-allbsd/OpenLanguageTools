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
 * TextHandlerTest.java
 *
 * Created on April 19, 2005, 3:48 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import org.jvnet.olt.xliff.Version;
import org.jvnet.olt.xliff.handlers.ParserX;


/**
 *
 * @author boris
 */
public class TextHandlerTest extends HandlerTestBase {
    class TestTextHandler extends TextHandler {
        public TestTextHandler(Context ctx) {
            super(ctx);
        }

        protected void postAction() {
        }

        String getText() {
            return text.toString();
        }
    }

    /** Creates a new instance of TextHandlerTest */
    public TextHandlerTest() {
    }

    public void testTextHandler() throws Exception {
        TestTextHandler hndlr = new TestTextHandler(new Context(Version.XLIFF_1_0));

        ParserX px = new ParserX();
        px.addHandler("/x/y/", hndlr);

        parse("<?xml version='1.0'?>\n" + "<x>" + "  <y>ABC</y>" + "</x>", px);

        assertEquals("ABC", hndlr.getText());
    }
}
