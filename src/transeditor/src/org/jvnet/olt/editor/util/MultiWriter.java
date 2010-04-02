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
 * MultiWriter.java
 *
 * Created on March 8, 2005, 3:33 PM
 */
package org.jvnet.olt.editor.util;

import java.io.Writer;


/** MultiWriter writes into multiple writers.
 *
 * If any operation on any of the writers fails an exception
 * is thrown this class does not attempt to finish the operation
 * on the rest of the writers.
 *
 * @author boris
 */
public class MultiWriter extends Writer {
    private Writer[] targets;

    /** Creates a new instance of MultiWriter */
    public MultiWriter(Writer[] writers) {
        this.targets = (writers == null) ? new Writer[] {  } : writers;
    }

    public void write(String str) throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].write(str);
    }

    public void write(char[] cbuf) throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].write(cbuf);
    }

    public void write(char[] cbuf, int off, int len) throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].write(cbuf, off, len);
    }

    public void write(String str, int off, int len) throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].write(str, off, len);
    }

    public void write(int c) throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].write(c);
    }

    public void flush() throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].flush();
    }

    public void close() throws java.io.IOException {
        for (int i = 0; i < targets.length; i++)
            targets[i].close();
    }
}
