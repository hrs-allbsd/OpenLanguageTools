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
