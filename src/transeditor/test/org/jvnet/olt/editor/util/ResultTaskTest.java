/*
 * TaskTest.java
 *
 * Created on July 18, 2005, 2:57 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.editor.util;

import junit.framework.TestCase;


/**
 *
 * @author boris
 */
public class ResultTaskTest extends TestCase {
    String x = "X";

    /** Creates a new instance of TaskTest */
    public ResultTaskTest() {
    }

    public void testRunningTask() throws Exception {
        final StringBuffer sb = new StringBuffer();

        ResultTask t = new ResultTask() {
            protected Object execute() {
                sb.append("hello");

                return x;
            }
        };

        t.start();

        Thread.currentThread().sleep(2000);

        assertEquals("hello", sb.toString());
        assertEquals(ResultTask.STATE_FINISHED, t.getState());
        assertSame(x, t.result());
    }

    public void testBlocking() throws Exception {
        final StringBuffer sb = new StringBuffer();

        final Object lock = new Object();

        final ResultTask tsk = new ResultTask() {
            protected Object execute() {
                sb.append("0");

                try {
                    sb.append("1");
                    System.out.println("1");

                    synchronized (lock) {
                        lock.wait();
                    }

                    System.out.println("2");
                    sb.append("2");
                } catch (InterruptedException e) {
                }

                return x;
            }
        };

        Thread thr = new Thread(new Runnable() {
                public void run() {
                    sb.append("3");
                    System.out.println("3");

                    tsk.result();
                    System.out.println("4");

                    sb.append("4");
                }
            });

        tsk.start();
        thr.start();

        Thread.currentThread().sleep(1000);

        synchronized (lock) {
            lock.notifyAll();
        }

        Thread.currentThread().sleep(1000);

        assertEquals(ResultTask.STATE_FINISHED, tsk.getState());
        assertEquals("01324", sb.toString());
    }

    public void testDoubleStart() throws Exception {
        final Object lock = new Object();

        System.out.println("Bla");

        ResultTask tsk = new ResultTask() {
            protected Object execute() {
                synchronized (lock) {
                    try {
                        System.out.println("1");
                        lock.wait();
                        System.out.println("2");
                    } catch (InterruptedException e) {
                    }
                }

                return null;
            }
        };

        System.out.println("3");
        tsk.start();

        Thread.currentThread().sleep(1000);

        try {
            System.out.println("4");
            tsk.start();
            System.out.println("5");
            fail();
        } catch (IllegalStateException ise) {
            //this is ok
        }
    }
}
