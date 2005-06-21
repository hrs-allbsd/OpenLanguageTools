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
 */
public class NestableException extends Exception {
    private String gInternalErrorMsg = "Open Language Tools Exception";
    private String gUserErrorMsg = "";
    private Throwable gPreviousThrowable = null;

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
        gInternalErrorMsg = new String(aInternalMsg);
    }

    /** Create an exception with a specified internal message and user
     * visible message
     * @param aMsg The message for exception
     */
    public NestableException(String aInternalMsg, String aUserMsg) {
        super(aInternalMsg);
        gInternalErrorMsg = new String(aInternalMsg);
        gUserErrorMsg = new String(aUserMsg);
    }

    /** Create an exception based on a given exception
     * @param aException The exception to create this exception from
     */
    public NestableException(Throwable aException) {
        gPreviousThrowable = aException;
    }

    /** Creates a NestableException with a given internal message from a given excpetion
     * @param aInternalMsg The message for exception
     * @param aException The exception to create the NestableException from
     */
    public NestableException(String aInternalMsg, Throwable aException) {
        super(aInternalMsg);
        gInternalErrorMsg = new String(aInternalMsg);
        gPreviousThrowable = aException;
    }

    /** Creates a NestableException with given internal and user messages from a given
     * excpetion
     * @param aInternalMsg The message for exception
     * @param aException The exception to create the NestableException from
     */
    public NestableException(String aInternalMsg, String aUserMsg, Throwable aException) {
        super(aInternalMsg);
        gInternalErrorMsg = new String(aInternalMsg);
        gUserErrorMsg = new String(aUserMsg);
        gPreviousThrowable = aException;
    }

    /** Overridden method to display chained exceptions
     */
    public void printStackTrace() {
        super.printStackTrace();

        if (gPreviousThrowable != null) {
            gPreviousThrowable.printStackTrace();
        }
    }

    /** Overridden method to displayed chained stack trace
     * @param aPrintStream The PrintStream to print the stack trace on
     */
    public void printStackTrace(PrintStream aPrintStream) {
        super.printStackTrace(aPrintStream);

        if (gPreviousThrowable != null) {
            gPreviousThrowable.printStackTrace(aPrintStream);
        }
    }

    /** Overridden method to displayed chained stack trace
     * @param aPrintWriter The PrintWriter to print the stack trace on
     */
    public void printStackTrace(PrintWriter aPrintWriter) {
        super.printStackTrace(aPrintWriter);

        if (gPreviousThrowable != null) {
            gPreviousThrowable.printStackTrace(aPrintWriter);
        }
    }

    public String toString() {
        if (gPreviousThrowable != null) {
            return gInternalErrorMsg + " <- " + gPreviousThrowable.toString();
        }

        return gInternalErrorMsg;
    }

    /** Get the message this exception was created with
     * @return  the message this exception was created with or default string */
    public String getMessage() {
        return toString();
    }

    /** Get the message this exception was created with. An alias for getMessage
     * @return  the message this exception was created with or default string */
    public String getInternalMessage() {
        return toString();
    }

    public NestableException setUserMessage(String aMsg) {
        if (aMsg == null) {
            gUserErrorMsg = "";
        } else {
            gUserErrorMsg = new String(aMsg);
        }

        return this;
    }

    /** Get the user messages "trace" this exception was created with if any
     * @return  the message this exception was created with or default string */
    public String getUserMessageTrace() {
        String msg = null;

        if ((gPreviousThrowable != null) && gPreviousThrowable instanceof NestableException) {
            msg = ((NestableException)gPreviousThrowable).getUserMessageTrace();

            if (msg.equals("")) {
                return gUserErrorMsg;
            } else {
                if (gUserErrorMsg.equals("")) {
                    return msg;
                } else {
                    return gUserErrorMsg + "\nbecause " + msg;
                }
            }
        } else {
            return gUserErrorMsg;
        }
    }
}
