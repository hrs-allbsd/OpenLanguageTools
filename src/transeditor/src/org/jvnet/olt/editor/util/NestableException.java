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
     * @param aInternalMsg The message for exception.
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
    
    public NestableException(String msg,Throwable aException) {
        super(msg,aException);
    }
    

}
