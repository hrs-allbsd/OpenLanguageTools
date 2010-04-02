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

/**
 * Exception thrown when a XLIFF back conversion fails.
 *
 * @author Brian Kidney
 * @created 05 September 2002
 */
public class XliffBackConverterException extends org.jvnet.olt.backconv.BackConverterException {

    /**
     * Constructor for the XliffBackConverterException object
     */
    public XliffBackConverterException() { }


    /**
     * Constructor for the XliffBackConverterException object
     *
     * @param msg A message to be included with a 
     * XliffBackConverterException
     */
    public XliffBackConverterException(String msg) {
        super(msg);
    }
}

