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
 * Counter.java
 *
 * Created on 27 January 2004, 12:10
 */
package org.jvnet.olt.editor.model;


/**
 *
 * @author  jc73554
 */
public class Counter {
    private int count;

    /** Creates a new instance of Counter */
    public Counter() {
        count = 0;
    }

    public Counter(int start) {
        count = start;
    }

    public int increment() {
        return (count++);
    }

    public int decrement() {
        return (count--);
    }

    public int getCount() {
        return count;
    }
}
