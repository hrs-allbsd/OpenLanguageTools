/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.minitm;

/**
 *@deprecated
 */
public class HashKey {
    public final static long getHashValue(String source) {
        long n = source.length();

        for (long i = n - 1; i >= 0; i--) {
            long k = (long)source.charAt((int)i);
            n = (((n & 1) << 63) | n >>> 1) ^ (k << (i % 32));
        }

        return n;
    }
}
