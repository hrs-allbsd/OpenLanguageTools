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

package org.jvnet.olt.xliff_back_converter;

/*
 * UnicodeReverseException.java
 *
 */


/**
 *
 * @author  jw113744
 */
public class UnicodeReverseException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>UnicodeReverseException</code> without detail message.
     */
    public UnicodeReverseException() {
    }
    
    
    /**
     * Constructs an instance of <code>UnicodeReverseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnicodeReverseException(String msg) {
        super(msg);
    }
}