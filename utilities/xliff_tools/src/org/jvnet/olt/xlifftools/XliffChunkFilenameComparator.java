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
 * XliffChunkFilenameComparator.java
 *
 * Created on 22 August 2003, 15:14
 */

package org.jvnet.olt.xlifftools;

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author  jc73554
 */
public class XliffChunkFilenameComparator implements Comparator
{
    
    /** Creates a new instance of XliffChunkFilenameComparator */
    public XliffChunkFilenameComparator()
    {
    }
    
    /**
     */
    public int compare(Object o1, Object o2)
    {
        //  Guard clause: ensure that we are comparing Files.
        if( !( (o1 instanceof File) && (o2 instanceof File) ) )
        {
            throw new ClassCastException("Objects being compared are not File objects");
        }
        
        File f1 = (File) o1;
        File f2 = (File) o2;
        
        //  If the files are equal then we are done: return 0
        if(f1.equals(f2)) { return 0; }
        
        //  Get file names.
        String name1 = f1.getName();
        String name2 = f2.getName();
        
        //  If the files names are equal then we are done: return 0
        if(name1.equals(name2)) { return 0; }
        
        //  Break file names into their respective bits, so that the differences
        //  can be ordered.
        try
        {
            TokenizedFileName tfn1 = new TokenizedFileName(name1);
            TokenizedFileName tfn2 = new TokenizedFileName(name2);
            return tfn1.compareTo(tfn2);
        }
        catch(FilenameFormatException formatEx)
        {
            formatEx.printStackTrace();
            return name1.compareTo(name2);
        }
    }
}
