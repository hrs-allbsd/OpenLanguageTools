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
 * FormatWrapper.java
 *
 * Created on 29 July 2002, 18:25
 */

package org.jvnet.olt.format;

import org.jvnet.olt.format.InvalidFormattingException;

/**
 * This interface provides a way to wrap the formatting of a source
 * string, producing an XLIFF version of that string - wrapping formatting
 * with ept/bpt/it/mrk sections, where required.
 * @author  jc73554
 */
public interface FormatWrapper
{
    
    
    /** Wraps the formatting of the input text, returning the input text
     * in XLIFF form, using ept, bpt, it and other xliff elements. This
     * also takes care of converting less-than greater-than and ampersand
     * characters in the input text to an XML-friendly named character
     * reference instead.
     *
     * @param text the text to be wrapped
     * @throws InvalidFormattingException if some error was encountered while wrapping the formatting
     * @return an xliff version of the input text
     */    
    public String wrapFormatting(String text) throws InvalidFormattingException;
    
}
