/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SafeToExitSemaphore.java
 *
 * Created on 22 November 2003, 18:22
 */
package org.jvnet.olt.editor.translation;


/** This class encapsulates a semaphore flag, which can be raised if
 * it is not safe to exit the application. It provides synchronization to
 * ensure multiple thread behave correctly.
 */
public class SafeToExitSemaphore {
    /** Holds value of property safeToExit. */
    private boolean safeToExit;

    public SafeToExitSemaphore() {
        safeToExit = true;
    }

    /** Getter for property safeToSave.
     * @return Value of property safeToSave.
     *
     */
    public synchronized boolean isSafeToExit() {
        return this.safeToExit;
    }

    /** Setter for property safeToSave.
     * @param safeToSave New value of property safeToSave.
     *
     */
    public synchronized void setSafeToExit(boolean safeToExit) {
        this.safeToExit = safeToExit;
    }

    /** This method raises the semaphore if it is not raised already.
     * @return true is the semaphore not was raised already, i.e., safeToExit.
     */
    public synchronized boolean testAndRaiseSemaphore() {
        if (this.safeToExit) {
            this.safeToExit = false;

            return true;
        } else {
            return false;
        }
    }

    /** This method lowers the semaphore if it has been raised already.
     * @return true is the semaphore was raised already, i.e., !safeToExit
     */
    public synchronized boolean testAndLowerSemaphore() {
        if (this.safeToExit) {
            return false;
        } else {
            this.safeToExit = true;

            return true;
        }
    }
}
