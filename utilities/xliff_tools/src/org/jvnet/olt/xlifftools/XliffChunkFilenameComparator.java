
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
