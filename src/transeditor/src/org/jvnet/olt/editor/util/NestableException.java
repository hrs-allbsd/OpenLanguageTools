/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.io.PrintStream;
import java.io.PrintWriter;


/*
 * timf: I'm suspicious of this class, can't we use the newer exception
 * initCause(Throwable t) method ?
 *
 * boris_steiner: modified to approximate to Exception semantics; further
 * this class will be renamed to ApplicationException to serve as base
 * for all app exceptions
 */
public class NestableException extends Exception {
    /** Creates a NestableException without any message
     */
    public NestableException() {
        super();
    }

    /** Create an exception with a specified internal message
     * @param aMsg The message for exception
     */
    public NestableException(String aInternalMsg) {
        super(aInternalMsg);
    }


    /** Create an exception based on a given exception
     * @param aException The exception to create this exception from
     */
    public NestableException(Throwable aException) {
        super(aException);
    }

}
