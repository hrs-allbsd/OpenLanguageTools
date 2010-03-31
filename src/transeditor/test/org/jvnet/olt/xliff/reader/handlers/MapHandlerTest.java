/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * MapHandlerTest.java
 *
 * Created on April 19, 2005, 3:16 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.Map;

import org.jvnet.olt.xliff.Version;
import org.jvnet.olt.xliff.handlers.ParserX;


/**
 *
 * @author boris
 */
public class MapHandlerTest extends HandlerTestBase {
    class TestMapHandler extends MapHandler {
        TestMapHandler(Context ctx, String propGroupTag, String propGroupNameAttr, String propTag, String propTagNameAttr) {
            super(ctx, propGroupTag, propGroupNameAttr, propTag, propTagNameAttr);
        }

        protected void postAction() {
        }

        Map getMap() {
            return map;
        }

        String getName() {
            return groupName;
        }
    }

    /** Creates a new instance of MapHandlerTest */
    public MapHandlerTest() {
    }

    public void testMapHandler() throws Exception {
        Context ctx = new Context(Version.XLIFF_1_0);
        TestMapHandler handler = new TestMapHandler(ctx, "prop-group", "name", "prop", "name");

        ParserX px = new ParserX();
        px.addHandler("/x/prop-group", handler);

        parse("<?xml version='1.0'?>\n" + "<x>" + "  <prop-group name='g1'>" + "    <prop name='p1'>v1</prop>" + "    <prop name='p2'>v2</prop>" + "  </prop-group>" + "</x>", px);

        Map m = handler.getMap();
        assertEquals("g1", handler.getName());
        assertEquals("v1", m.get("p1"));
        assertEquals("v2", m.get("p2"));
    }
}
