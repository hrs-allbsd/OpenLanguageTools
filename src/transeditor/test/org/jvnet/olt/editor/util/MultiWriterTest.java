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
 * MultiWriterTest.java
 *
 * Created on March 10, 2005, 10:20 AM
 */
package org.jvnet.olt.editor.util;

import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;


/** Tests multiwriter -- writer that writes into multiple out writers
 *
 * @author boris
 */
public class MultiWriterTest extends TestCase {
    class FakeWriter extends Writer {
        boolean wasClose;
        boolean wasFlush;
        StringBuffer sb = new StringBuffer();

        public void write(String str) throws java.io.IOException {
            sb.append(str);
        }

        public void write(char[] cbuf) throws java.io.IOException {
            sb.append(cbuf);
        }

        public void write(char[] cbuf, int off, int len) throws java.io.IOException {
            sb.append(cbuf, off, len);
        }

        public void write(String str, int off, int len) throws java.io.IOException {
            sb.append(str.substring(off, off + len));
        }

        public void write(int c) throws java.io.IOException {
            sb.append((char)c);
        }

        public void flush() throws java.io.IOException {
            wasFlush = true;
        }

        public void close() throws java.io.IOException {
            wasClose = true;
        }

        public String toString() {
            return sb.toString();
        }
    }

    /** Creates a new instance of MultiWriterTest */
    public MultiWriterTest() {
    }

    public void testWriteString() throws Exception {
        StringWriter sw1 = new StringWriter();
        StringWriter sw2 = new StringWriter();

        MultiWriter mw = new MultiWriter(new Writer[] { sw1, sw2 });
        mw.write("abcdef");
        mw.close();

        System.out.println("'" + sw1.toString() + "'");

        assertEquals("x", "abcdef", sw1.toString());
        assertEquals("x", "abcdef", sw2.toString());
    }

    public void testFlushAndClose() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        mw.write("abc");
        mw.close();
        mw.flush();

        assertTrue(fw1.wasFlush);
        assertTrue(fw1.wasClose);
        assertTrue(fw2.wasFlush);
        assertTrue(fw2.wasClose);
    }

    public void testWriteCharArray() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        char[] x = new char[] { 'x', 'y', 'z' };

        mw.write(x);
        mw.flush();
        mw.close();

        assertEquals("xyz", fw1.toString());
        assertEquals("xyz", fw2.toString());
    }

    public void testWriteCharArrayBounds() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        char[] x = new char[] { 'x', 'y', 'z', '@' };

        mw.write(x, 1, 2);
        mw.flush();
        mw.close();

        assertEquals("yz", fw1.toString());
        assertEquals("yz", fw2.toString());
    }

    public void testWriteStringWBounds() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        mw.write("ijklmn", 1, 2);
        mw.flush();
        mw.close();

        assertEquals("jk", fw1.toString());
        assertEquals("jk", fw2.toString());
    }

    public void testWriteOneChar() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        mw.write('a');
        mw.write('b');
        mw.flush();
        mw.close();

        assertEquals("ab", fw1.toString());
        assertEquals("ab", fw2.toString());
    }

    public void testBigFile() throws Exception {
        FakeWriter fw1 = new FakeWriter();
        FakeWriter fw2 = new FakeWriter();
        MultiWriter mw = new MultiWriter(new Writer[] { fw1, fw2 });

        final int COUNT = 20000;
        StringBuffer sb = new StringBuffer(COUNT);

        int x = 0;

        do {
            sb.append(++x);
            mw.write(x + "");
        } while (sb.length() < COUNT);

        assertEquals(sb.toString(), fw1.toString());
        assertEquals(sb.toString(), fw2.toString());
    }
}
