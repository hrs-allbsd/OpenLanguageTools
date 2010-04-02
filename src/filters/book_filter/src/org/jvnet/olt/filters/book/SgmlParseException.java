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
 * SgmlParseException.java
 *
 * Created on March 1, 2005, 3:23 PM
 */

package org.jvnet.olt.filters.book;

/**
 * This is an exception that gets thrown whenever we get a JJTree/JavaCC parse
 * exception. It's of particular interest for sgml files, when we are sometimes
 * asked to parse a bit of a doctype subset by mistake : in those cases, we catch
 * throw this exception to indicate that the content isn't SGML and that the user
 * should probably try another parser.
 * @author timf
 */
public class SgmlParseException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SgmlParseException</code> without detail message.
     */
    public SgmlParseException() {
    }
    
    
    /**
     * Constructs an instance of <code>SgmlParseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SgmlParseException(String msg) {
        super(msg);
    }
}
