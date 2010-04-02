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
 * SOXliffBackException.java
 *
 * Created on April 30, 2006, 6:29 PM
 *
 */

package org.jvnet.olt.soxliff_backconv;

/**
 * Exception throwed by StarOffice Xliff filter
 * 
 * @author michal
 */
public class SOXliffBackException extends Exception {
    
    /**
     * Construct new SOXliffException with detailed message
     *
     * @param description of the cause
     *
     */
    public SOXliffBackException(String message) {
        super(message);
    }
 
    /**
     * Construct new SOXliffException with detailed message and
     * cause of the exception
     *
     * @param description detailed message
     * @param cause of the exception
     */
    public SOXliffBackException(String message,Throwable cause) {
        super(message,cause);
    }
}
