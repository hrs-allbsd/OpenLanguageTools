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
package org.jvnet.olt.editor.minitm;

/**
 * @deprecated
 */

public class TypeConversion {
    public static final boolean equals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        } else {
            for (int i = 0; i < a.length; ++i)
                if (a[i] != b[i]) {
                    return false;
                }

            return true;
        }
    }

    public static final void zero(byte[] a, int a0, int l) {
        fill((byte)0, a, a0, l);
    }

    public static final void fill(byte a, byte[] b, int b0, int l) {
        for (int i = 0; i < l; ++i)
            b[b0 + i] = a;
    }

    public static final void intToBytes(int a, byte[] b, int b0) {
        b[b0] = (byte)(a >> 24);
        b[b0 + 1] = (byte)(a >> 16);
        b[b0 + 2] = (byte)(a >> 8);
        b[b0 + 3] = (byte)(a);
    }

    public static final void intsToBytes(int[] a, int a0, int l, byte[] b, int b0) {
        for (int i = 0; i < l; ++i)
            intToBytes(a[a0 + i], b, b0 + (i << 2));
    }

    public static final int bytesToInt(byte[] a, int a0) {
        return (a[a0] << 24) + ((a[a0 + 1] & 0xff) << 16) + ((a[a0 + 2] & 0xff) << 8) + (a[a0 + 3] & 0xff);
    }

    public static final void bytesToInts(byte[] a, int a0, int[] b, int b0, int l) {
        for (int i = 0; i < l; ++i)
            b[b0 + i] = bytesToInt(a, a0 + (i << 2));
    }

    public static final void longToBytes(long a, byte[] b, int b0) {
        b[b0] = (byte)(a >> 56);
        b[b0 + 1] = (byte)(a >> 48);
        b[b0 + 2] = (byte)(a >> 40);
        b[b0 + 3] = (byte)(a >> 32);
        b[b0 + 4] = (byte)(a >> 24);
        b[b0 + 5] = (byte)(a >> 16);
        b[b0 + 6] = (byte)(a >> 8);
        b[b0 + 7] = (byte)(a);
    }

    public static final long bytesToLong(byte[] a, int a0) {
        return ((a[a0] & 0xffL) << 56) | ((a[a0 + 1] & 0xffL) << 48) | ((a[a0 + 2] & 0xffL) << 40) | ((a[a0 + 3] & 0xffL) << 32) | ((a[a0 + 4] & 0xffL) << 24) | ((a[a0 + 5] & 0xffL) << 16) | ((a[a0 + 6] & 0xffL) << 8) | (a[a0 + 7] & 0xffL);
    }

    public static final String bytesToHex(byte[] a) {
        StringBuffer s = new StringBuffer();

        for (int i = 0; i < a.length; ++i) {
            s.append(Character.forDigit((a[i] >> 4) & 0xf, 16));
            s.append(Character.forDigit(a[i] & 0xf, 16));
        }

        return s.toString();
    }

    public static final String longToHex(long a) {
        byte[] b = new byte[8];
        longToBytes(a, b, 0);

        return bytesToHex(b);
    }
}
