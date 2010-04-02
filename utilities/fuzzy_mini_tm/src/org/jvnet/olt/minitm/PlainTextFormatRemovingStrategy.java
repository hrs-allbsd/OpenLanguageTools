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

package org.jvnet.olt.minitm;

/**
 *  This class removed formatting from strings that don't have
 *  formatting, that is, all the text in the strings are assumed
 *  to be character data. The removeFormatting function is an
 *  identity mapping in this case.
 */
public class PlainTextFormatRemovingStrategy
implements FormatRemovingStrategy
{
    public String removeFormatting(String string)
    throws MiniTMException
    {
        return string;
    }
    
    public java.util.List extractFormatting(String string) throws MiniTMException
    {
        return new java.util.LinkedList();
    }   
}
