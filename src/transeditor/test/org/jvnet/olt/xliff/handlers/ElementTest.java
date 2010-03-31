/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff.handlers;

import java.util.Iterator;
import java.util.Set;

import junit.framework.*;

import org.xml.sax.helpers.AttributesImpl;


/*
 * ElementTest.java
 * JUnit based test
 *
 * Created on April 18, 2005, 11:19 AM
 */

/**
 *
 * @author boris
 */
public class ElementTest extends TestCase {
    public ElementTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ElementTest.class);

        return suite;
    }

    public void testProperties() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("u1", "l1", "q1", "t1", "value");

        Element e1 = new Element("prefix", "local", "qName", attrs, "/abc");

        assertEquals("prefix", e1.getPrefix());
        assertEquals("local", e1.getLocalName());
        assertEquals("prefix:local", e1.getQName());
        assertEquals("qName", e1.getOriginalQName());
        assertEquals("/abc", e1.getPath());

        Element e2 = new Element(null, "local", "qName", attrs, "/abc");
        assertEquals("local", e2.getQName());
    }

    private void testForNulls(Element e) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("u1", "l1", "q1", "t1", "value");

        String nullStr = null;

        Element e1 = new Element(nullStr, "l1", "q1", attrs, "/");

        try {
            e1 = new Element("", nullStr, "q1", attrs, "/");
            fail();
        } catch (NullPointerException npe) {
        }

        try {
            e1 = new Element("", "", nullStr, attrs, "/");
            fail();
        } catch (NullPointerException npe) {
        }

        e1 = new Element("", "", "", null, "");

        try {
            e1 = new Element("", "", "", attrs, null);
            fail();
        } catch (NullPointerException npe) {
        }
    }

    public void testEquals() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        Element e1 = new Element("prefix", "local", "qName", attrs, "/abc");
        Element e2 = new Element("prefix", "local", "qName", attrs, "/abc");
        Element e3 = new Element("", "local", "qName", attrs, "/abc");

        assertEquals(e1, e1);

        //equality is identity
        //assertEquals(e1, e2);
        assertFalse(e1.equals(e2));
        assertFalse(e1.equals(e3));

        assertFalse(e1.equals(Element.ROOT_ELEMENT));
        assertEquals(Element.ROOT_ELEMENT, Element.ROOT_ELEMENT);
    }

    public void testRoot() throws Exception {
        Element r = Element.ROOT_ELEMENT;

        assertNotNull(r.toString());
    }

    public void testNamespacesDeclsNoPrefix() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        Element e1 = new Element("prefix", "local", "qName", attrs, "/abc");

        e1.addNamespaceDeclaration(null, "abc:cde:fgi");

        Set s = e1.getNamespacesDeclarations();

        assertFalse(s.isEmpty());
        assertEquals(1, s.size());

        Element.Namespace ns = (Element.Namespace)e1.getNamespacesDeclarations().iterator().next();

        assertNull(ns.getPrefix());
        assertEquals("abc:cde:fgi", ns.getURI());

        assertEquals("xmlns=\"abc:cde:fgi\"", ns.toString());

        //returns non modifiable set so this MUST fail
        try {
            s.clear();
            fail();
        } catch (UnsupportedOperationException uoe) {
            //ok
        }
    }

    public void testNamespacesDeclsWithPrefix() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        Element e1 = new Element("prefix", "local", "qName", attrs, "/abc");

        e1.addNamespaceDeclaration("xyz", "abc:cde:fgi");

        Set s = e1.getNamespacesDeclarations();

        assertFalse(s.isEmpty());
        assertEquals(1, s.size());

        Element.Namespace ns = (Element.Namespace)e1.getNamespacesDeclarations().iterator().next();

        assertEquals("xyz", ns.getPrefix());
        assertEquals("abc:cde:fgi", ns.getURI());

        assertEquals("xmlns:xyz=\"abc:cde:fgi\"", ns.toString());
    }

    public void testNSOrder() throws Exception {
        Element e1 = new Element("prefix", "local", "qName", null, "/abc");
        e1.addNamespaceDeclaration(null, "abc");
        e1.addNamespaceDeclaration("cde", "abc");

        Iterator i = e1.getNamespacesDeclarations().iterator();
        Element.Namespace ns1 = (Element.Namespace)i.next();
        Element.Namespace ns2 = (Element.Namespace)i.next();

        assertNull(ns1.getPrefix());
        assertEquals("cde", ns2.getPrefix());
    }
}
