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
 * XMLDumperTest.java
 *
 * Created on March 4, 2005, 11:18 PM
 */
package org.jvnet.olt.xliff;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.jvnet.olt.xliff.handlers.Element;

import org.xml.sax.helpers.AttributesImpl;


/** Tests XMLDumper
 *
 * @author boris
 */
public class XMLDumperTest extends TestCase {
    StringWriter w;
    XMLDumper dumper;
    Element elem;
    AttributesImpl attrs;

    /** Creates a new instance of XMLDumperTest */
    public XMLDumperTest() {
    }

    protected void setUp() throws Exception {
        w = new StringWriter();
        dumper = new XMLDumper(w);

        attrs = new AttributesImpl();
        attrs.addAttribute("", "a", "y:a", null, "b");
        elem = new Element("x", "a", "y:a", attrs, "/a");
    }

    public void testNullElement() throws Exception {
        try {
            dumper.writeElement(null, true);
            fail();
        } catch (NullPointerException npe) {
        }
    }

    public void testSimpleElement() throws Exception {
        Element e1 = new Element(null, "tag", "tag", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeElement(e1, false);

        w.close();

        System.out.println("w:" + w.toString());
        assertEquals("<tag></tag>", w.toString());
    }

    public void testSimepleNSElement() throws Exception {
        Element e1 = new Element("x", "tag", "y:tag", null, "/");
        dumper.writeElement(e1, true);
        dumper.writeElement(e1, false);

        System.out.println("w:" + w.toString());
        assertEquals("<y:tag></y:tag>", w.toString());
    }

    public void testSimpleTextElement() throws Exception {
        Element e1 = new Element(null, "tag", "tag", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeChars("text".toCharArray(), 0, 4);
        dumper.writeElement(e1, false);

        assertEquals("<tag>text</tag>", w.toString());
    }

    public void testMixedElements() throws Exception {
        Element e1 = new Element(null, "tag", "tag", null, "/");
        Element e2 = new Element(null, "tag2", "tag2", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeChars("text".toCharArray(), 0, 4);
        dumper.writeElement(e2, true);
        dumper.writeChars("text2".toCharArray(), 0, 5);
        dumper.writeElement(e2, false);
        dumper.writeElement(e1, false);

        assertEquals("<tag>text<tag2>text2</tag2></tag>", w.toString());
    }

    public void testEscaping() throws Exception {
        /* < > & must be escaped to &lt; &gt; &amp */
        Element e1 = new Element(null, "tag", "tag", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeChars("<>&<suck>".toCharArray(), 0, 9);
        dumper.writeElement(e1, false);

        assertEquals("<tag>&lt;&gt;&amp;&lt;suck&gt;</tag>", w.toString());
    }

    public void testNoEscaping() throws Exception {
        /* test avoiding of escapes */
        Element e1 = new Element(null, "tag", "tag", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeChars("<>&<suck>&gt;".toCharArray(), 0, 13, false);
        dumper.writeElement(e1, false);

        assertEquals("<tag><>&<suck>&gt;</tag>", w.toString());
    }

    public void testNamespaces() throws Exception {
        Element e1 = new Element("ns1", "tag", "ns2:tag", null, "/");
        Element e2 = new Element("ns1", "tag2", "ns2:tag2", null, "/");

        dumper.writeElement(e1, true);
        dumper.writeElement(e2, true);
        dumper.writeElement(e2, false);
        dumper.writeElement(e1, false);

        System.out.println(w.toString());

        assertEquals("<ns2:tag><ns2:tag2></ns2:tag2></ns2:tag>", w.toString());
    }

    public void testAttributes() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "", "a:a1", "", "a11");
        attrs.addAttribute("", "", "b:b1", "", "b11");

        Element e1 = new Element(null, "tag", "tag", attrs, "/");

        dumper.writeElement(e1, true);

        assertEquals("<tag a:a1=\"a11\" b:b1=\"b11\">", w.toString());
    }

    public void testNamespaceAndAttributes() throws Exception {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "", "a:a1", "", "a11");
        attrs.addAttribute("", "", "b:b1", "", "b11");

        Element e1 = new Element(null, "tag", "tag", attrs, "/");
        e1.addNamespaceDeclaration(null, "abc:cde");
        e1.addNamespaceDeclaration("xyz", "zyx:xyz");

        dumper.writeElement(e1, true);

        assertEquals("<tag xmlns=\"abc:cde\" xmlns:xyz=\"zyx:xyz\" a:a1=\"a11\" b:b1=\"b11\">", w.toString());
    }
}
