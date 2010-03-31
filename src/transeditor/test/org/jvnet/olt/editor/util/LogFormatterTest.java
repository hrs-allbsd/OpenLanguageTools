/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.io.StringReader;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import junit.framework.TestCase;
import org.jvnet.olt.util.TestSupport;


/*
 * LogFormatterTest.java
 *
 * Created on March 21, 2005, 7:42 PM
 */

/**
 *
 * @author boris
 */
public class LogFormatterTest extends TestCase {
    /** Creates a new instance of LogFormatterTest */
    public LogFormatterTest() {
    }

    public void testNormalize() throws Exception {
        LogFormatter f = new LogFormatter();

        String s = f.normalize(null, 3);

        assertNotNull(s);
        assertEquals("   ", s);

        s = f.normalize("abc", 10);

        assertNotNull(s);
        assertEquals("abc       ", s);

        s = f.normalize("abcdef", 3);

        assertNotNull(s);
        assertEquals("def", s);
    }

    public void testEnsurePad() throws Exception {
        LogFormatter lf = new LogFormatter();

        assertEquals("   ", lf.ensurePad(3));
        assertEquals("    ", lf.ensurePad(4));
        assertEquals("   ", lf.ensurePad(3));
        assertEquals("     ", lf.ensurePad(5));
        assertEquals("", lf.ensurePad(0));
        assertNull(lf.ensurePad(-5));
    }

    private Calendar makeCal() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1980);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DATE, 21);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.AM_PM, Calendar.PM);
        cal.set(Calendar.MINUTE, 49);
        cal.set(Calendar.SECOND, 53);

        return cal;
    }

    public void testSimpleRecord() throws Exception {
        LogFormatter f = new LogFormatter();

        f.setMaxLevelWidth(5);
        f.setMaxLoggerWidth(10);

        Calendar cal = makeCal();

        LogRecord record = new LogRecord(Level.INFO, "Message!");
        record.setMillis(cal.getTimeInMillis());
        record.setLoggerName("LOGGER");

        String s = f.format(record);

        System.out.println("s:" + s);
        System.out.println("x:" + "1980/04/21 22:49:53 INFO  LOGGER     Message!\n");
        System.out.println(cal.getTime());

        assertEquals("1980/04/21 22:49:53 INFO  LOGGER     Message!\n", s);
    }

    public void testThrows() throws Exception {
        Calendar cal = makeCal();
        LogFormatter f = new LogFormatter();
        f.setMaxLevelWidth(8);
        f.setMaxLoggerWidth(10);

        LogRecord record = new LogRecord(Level.WARNING, "STuff!");

        record.setMillis(cal.getTimeInMillis());
        record.setLoggerName(getClass().getName());

        Throwable e = null;

        try {
            throw new Exception("ABC");
        } catch (Throwable t) {
            e = t;
        }

        record.setThrown(e);

        String s = f.format(record);

        //System.out.println("s:"+s);
        StringReader sr = new StringReader(s);

        assertTrue(TestSupport.findLine(sr, "java.lang.Exception: ABC", true));
    }

    public void testParams() throws Exception {
        Calendar cal = makeCal();
        LogFormatter f = new LogFormatter();
        f.setMaxLevelWidth(8);
        f.setMaxLoggerWidth(10);

        LogRecord record = new LogRecord(Level.WARNING, "STuff!");

        String s = f.format(record);

        System.out.println("s:" + s);
    }
}
