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

package org.jvnet.olt.filters.csv;

/**
 *
 * @author timf
 */
public class CsvParserException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>CsvParserException</code> without detail message.
     */
    public CsvParserException() {
    }
    
    
    /**
     * Constructs an instance of <code>CsvParserException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CsvParserException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>CsvParserException</code> with the specified detail message
     * and initial cause.
     * @param msg the detail message.
     * @param t the initial cause of this exception
     */
    public CsvParserException(String msg, Throwable t){
        super(msg);
        this.initCause(t);
    }
    
}
