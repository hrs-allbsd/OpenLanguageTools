/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Formatter;


/*
 * LogFormatter.java
 *
 * Created on March 21, 2005, 6:00 PM
 */

/**
 *
 * @author boris
 */
public class LogFormatter extends Formatter {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");

    /** Creates a new instance of LogFormatter */
    private int maxLoggerWidth = 40;
    private int maxLevelWidth = 10;
    private ArrayList pads = new ArrayList(Math.max(maxLoggerWidth, maxLevelWidth));

    public LogFormatter() {
        super();
    }

    public void setMaxLoggerWidth(int width) {
        this.maxLoggerWidth = (width < 0) ? 0 : width;
    }

    public void setMaxLevelWidth(int width) {
        this.maxLevelWidth = (width < 0) ? 0 : width;
    }

    public String formatMessage(java.util.logging.LogRecord logRecord) {
        return format(logRecord);
    }

    public String format(java.util.logging.LogRecord logRecord) {
        StringBuffer line = new StringBuffer();

        synchronized (sdf) {
            try {
                line.append(sdf.format(new Date(logRecord.getMillis())));
            } catch (IllegalArgumentException iae) {
                line.append("<INVALID DATE>");
            }
        }

        line.append(" ");
        line.append(normalize(logRecord.getLevel().getName(), maxLevelWidth));
        line.append(" ");
        line.append(normalize(logRecord.getLoggerName(), maxLoggerWidth));
        line.append(" ");
        line.append(logRecord.getMessage());

        if (logRecord.getParameters() != null) {
            line.append(";Parameters: ");

            Object[] objs = logRecord.getParameters();

            for (int i = 0; i < objs.length; i++)
                line.append(objs[i]).append(",");
        }

        if (logRecord.getThrown() != null) {
            StringWriter sw = new StringWriter();
            Throwable th = logRecord.getThrown();
            th.printStackTrace(new PrintWriter(sw));

            line.append("\n").append(sw.toString());
        }

        return line.append("\n").toString();
    }

    public String getTail(java.util.logging.Handler handler) {
        return "";
    }

    public String getHead(java.util.logging.Handler handler) {
        return "";
    }

    String normalize(String s, int maxLen) {
        synchronized (pads) {
            if ((s == null) || (maxLen == 0)) {
                s = "";
            }

            int len = s.length();

            if (maxLen > len) {
                String pad = ensurePad(maxLen - len);

                return s + pad;
            } else {
                return s.substring(len - maxLen);
            }
        }
    }

    String ensurePad(int size) {
        if (size <= 0) {
            return (size < 0) ? null : "";
        }

        if (pads.size() <= size) {
            pads.ensureCapacity(size);

            for (int i = pads.size(); i <= size; i++)
                pads.add(null);
        }

        String p = (String)pads.get(size);

        if (p == null) {
            StringBuffer pad = new StringBuffer(size);

            for (int i = 0; i < size; i++)
                pad.append(' ');

            p = pad.toString();
            pads.set(size, p);
        }

        return p;
    }
}
