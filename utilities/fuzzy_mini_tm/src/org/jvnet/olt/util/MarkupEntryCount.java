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
 * MarkupEntryCounter.java
 *
 * Created on 27 February 2003, 11:04
 */

package org.jvnet.olt.util;

/**
 *
 * @author  jc73554
 */
public class MarkupEntryCount
{
    
    private org.jvnet.olt.parsers.sgmltokens.MarkupEntry markupEntry;
    
    private int count;
    
    /** Creates a new instance of MarkupEntryCounter */
    public MarkupEntryCount(org.jvnet.olt.parsers.sgmltokens.MarkupEntry entry)
    {
        markupEntry = entry;
        count = 1;
    }
    
    public void incrementCount()
    {
        count++;
    }
    
    public int addToCount(int val)
    {
        count += val;
        if(count < 0) { count = 0; }
        return count;
    }
    
    public org.jvnet.olt.parsers.sgmltokens.MarkupEntry getMarkupEntry()
    {
        return markupEntry;
    }
    
    public int getCount()
    {
        return count;
    }
    
}
