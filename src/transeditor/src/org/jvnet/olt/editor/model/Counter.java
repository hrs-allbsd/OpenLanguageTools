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
