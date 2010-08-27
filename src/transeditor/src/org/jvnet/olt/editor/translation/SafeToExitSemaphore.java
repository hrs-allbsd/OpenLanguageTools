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

    /** Getter for property safeToExit.
     * @return Value of property safeToExit.
     *
     */
    public synchronized boolean isSafeToExit() {
        return this.safeToExit;
    }

    /** Setter for property safeToExit.
     * @param safeToExit New value of property safeToExit.
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
