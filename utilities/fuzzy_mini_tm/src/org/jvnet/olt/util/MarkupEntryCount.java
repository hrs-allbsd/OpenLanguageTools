
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
