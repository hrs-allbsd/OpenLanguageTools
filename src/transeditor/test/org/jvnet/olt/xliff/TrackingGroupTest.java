/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;


/**
 * User: boris
 * Date: Dec 7, 2004
 * Time: 2:17:50 PM
 */
public class TrackingGroupTest extends TestCase {
    List ids1;
    List ids2;
    List ids3;

    public TrackingGroupTest() {
    }

    protected void setUp() throws Exception {
        super.setUp();

        List l = new ArrayList();
        l.add("1");
        l.add("2");
        l.add("3");

        ids1 = Collections.unmodifiableList(l);

        l = new ArrayList();
        l.add("7");

        ids2 = Collections.unmodifiableList(l);

        l = new ArrayList();
        l.add("10");
        l.add("11");

        ids3 = Collections.unmodifiableList(l);
    }

    public void testConstruction() throws Exception {
        TrackingGroup grp = new TrackingGroup();

        grp.addGroup("1", ids1, false);
        grp.addGroup("7", ids2, false);
        grp.addGroup("10", ids3, false);

        assertEquals("1", grp.getFirstIdOfOneGroup("1"));
        assertNull(grp.getFirstIdOfOneGroup("55"));

        assertEquals(3, grp.getSizeOfOneGroup("1"));
        assertEquals(0, grp.getSizeOfOneGroup("55"));
        assertEquals(ids1, grp.getListOfOneGroup("1"));

        assertEquals(0, grp.isTransunitIdInGroup("1"));
        assertEquals(1, grp.isTransunitIdInGroup("2"));
        assertEquals(-1, grp.isTransunitIdInGroup("4"));

        assertEquals("1", grp.getGroupId("3"));
        assertEquals("10", grp.getGroupId("10"));
        assertEquals("10", grp.getGroupId("11"));
        assertNull(grp.getGroupId("55"));

        assertTrue(grp.inTheSameGroup("2", "3"));
        assertTrue(grp.inTheSameGroup("10", "11"));

        assertFalse(grp.inTheSameGroup("1", "11"));
        assertFalse(grp.inTheSameGroup(null, "11"));
        assertFalse(grp.inTheSameGroup(null, null));
    }

    public void testFailure() throws Exception {
        //these tests should mimic failure behaviour
        TrackingGroup grp = new TrackingGroup();
        grp.addGroup(null, ids1, false);

        grp.addGroup(null, null, false);
    }
}
