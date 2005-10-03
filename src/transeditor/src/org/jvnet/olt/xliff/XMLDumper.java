/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XMLDumper.java
 *
 * Created on February 28, 2005, 4:54 PM
 */
package org.jvnet.olt.xliff;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.Iterator;
import java.util.Set;

import org.jvnet.olt.io.*;
import org.jvnet.olt.xliff.handlers.Element;

import org.xml.sax.Attributes;


/**
 *
 * @author boris
 */
public class XMLDumper {
    private java.io.Writer os;
    private int depth;
    private char[] buffer;

    public XMLDumper(java.io.Writer os) {
        this.os = os;

        buffer = new char[3072];
    }

    public void writeElement(Element element, boolean start) throws IOException {
        if (start) {
            os.write("<");
            os.write(element.getOriginalQName());

            Set s = element.getNamespacesDeclarations();

            if (!s.isEmpty()) {
                for (Iterator i = s.iterator(); i.hasNext();) {
                    os.write(" ");

                    Element.Namespace ns = (Element.Namespace)i.next();
                    os.write(ns.toString());
                }
            }

            Attributes attrs = element.getAttrs();

            for (int i = 0; i < attrs.getLength(); i++) {
                os.write(" ");

                String attr = attrs.getQName(i);
                String value = attrs.getValue(i);

                //String ns = attrs.getLocalName(i);
                //if(ns != null && ns.trim().length() > 0)
                //    os.write(ns+":");
                os.write(attr + "=\"" + value + "\"");
            }

            os.write(">");
        } else {
            os.write("</");
            os.write(element.getOriginalQName());
            os.write(">");
        }
    }

    public void writeChars(char[] c, int start, int length) throws IOException {
        writeChars(c, start, length, true);
    }

    public void writeChars(char[] c, int start, int length, boolean escapeHTML) throws IOException {
        if (escapeHTML) {
            CharArrayReader car = new CharArrayReader(c, start, length);
            java.io.Reader r = new HTMLEscapeFilterReader(car);

            do {
                int len = r.read(buffer);

                if (len == -1) {
                    break;
                }

                os.write(buffer, 0, len);
            } while (true);
        } else {
            os.write(c, start, length);
        }
    }

    /*

        void writeProcessingInstruction(String target, String data) throws IOException {
            os.write("<?" + target + " " + data + "?>");
        }
    */
}
