
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

import java.util.HashMap;
import org.jvnet.olt.parsers.sgmltokens.MarkupEntry;
import org.jvnet.olt.util.MarkupEntryCount;

/**
 *  This class is a convenience class to allow users to calculate what
 *  degree of matching is to be expected between two strings. By default,
 *  this class assumes it is gettting SGML marked up text, and strips out
 *  such markup.
 */
public class MatchQualityCalculator
{
    public static final int ATTRIBUTED_TAG_PENALTY = 5;
    public static final int PLAIN_TAG_PENALTY = 2;
    
    public static int calculateMatchQuality(String queryString, String referenceString) throws org.jvnet.olt.minitm.MiniTMException
    {
        org.jvnet.olt.minitm.FormatRemovingStrategy fr = new org.jvnet.olt.minitm.SGMLFormatRemovingStrategy();
        return calculateMatchQuality(queryString, referenceString, fr);
    }
    
    public static int calculateMatchQuality(String queryString, String referenceString, org.jvnet.olt.minitm.FormatRemovingStrategy fr) throws org.jvnet.olt.minitm.MiniTMException
    {
        
        float matchQuality = calculatePercentDiff(queryString, referenceString, fr);
        
        matchQuality -= calculateFormatPenalty(queryString, referenceString, fr);
        
        if(matchQuality < 0)
        { matchQuality = 0; }
        
        return ((int) matchQuality);
    }
    
    protected static float calculatePercentDiff(String queryString, String referenceString, org.jvnet.olt.minitm.FormatRemovingStrategy fr) throws org.jvnet.olt.minitm.MiniTMException
    {
        String unformattedQueryStr = fr.removeFormatting( queryString );
        String unformattedReferenceStr = fr.removeFormatting( referenceString );
        
        float percentDiff = org.jvnet.olt.index.StringComparer.calculatePercentMatch(unformattedQueryStr,unformattedReferenceStr);
        
        if(percentDiff < 0) { percentDiff = 0; }
        return percentDiff;
    }
    
    public static int calculateFormatPenalty(String queryString, String referenceString, org.jvnet.olt.minitm.FormatRemovingStrategy fr) throws org.jvnet.olt.minitm.MiniTMException
    {
        HashMap queryMap = buildMapFromList(fr.extractFormatting(queryString));
        HashMap refMap= buildMapFromList(fr.extractFormatting(referenceString));
        
        int formatDiff = 0;
        
        //  get iterator on the keys of query map
        java.util.Iterator iterQuery = queryMap.keySet().iterator();
        MarkupEntry entry = null;
        MarkupEntryCount queryCount = null;
        MarkupEntryCount refCount = null;
        
        //  iterate over query map keys
        while(iterQuery.hasNext())
        {
            entry = (MarkupEntry) iterQuery.next();
            int penalty = PLAIN_TAG_PENALTY;  //  Set default and override if needed
            if(entry.hasAttributes()) { penalty = ATTRIBUTED_TAG_PENALTY; }
            if(refMap.containsKey(entry))
            {
                //  Apply penalty for differences in number of a given tag. If
                //  the number of tags is the same in both cases, then this
                //  penalty will be zero.
                queryCount = (MarkupEntryCount) queryMap.get(entry);               
                refCount = (MarkupEntryCount) refMap.get(entry);
                
                formatDiff += ( Math.abs(queryCount.getCount() - refCount.getCount() ) * penalty);
                
                //  Penalty has been applied for ref map entry, so we need to
                //  remove the entry to ensure it isn't applied again.
                 refMap.remove(entry);
            }
            else
            {
                //  Apply penalty for each occurrence of the tag.
                queryCount = (MarkupEntryCount) queryMap.get(entry);
                formatDiff += ( queryCount.getCount() * penalty);
            }
        }
        //  iterate over the remaining keys in ref map
        java.util.Iterator iterRef = refMap.keySet().iterator();
        while(iterRef.hasNext())
        {
            entry = (MarkupEntry) iterRef.next();
            
            int penalty = PLAIN_TAG_PENALTY;  //  Set default and override if needed
            if(entry.hasAttributes()) { penalty = ATTRIBUTED_TAG_PENALTY; }
            
            refCount = (MarkupEntryCount) refMap.get(entry);

            formatDiff += ( refCount.getCount() * penalty);   
        } 
        
        return formatDiff;
    }
    
    /**
     *  This method takes a list of markup entry objects and builds a HashMap
     *  from it. The HashMap contains MarkupEntryCount objects and between the
     *  two of them they simulate an indexed multi-set.
     */
    protected static HashMap buildMapFromList(java.util.List markupList)
    {
        HashMap map = new HashMap();
        if(markupList == null) { return map; }
        java.util.Iterator iterator = markupList.iterator();
        while(iterator.hasNext())
        {
            //  Check if entry is in the HashMap
            MarkupEntry entry = (MarkupEntry) iterator.next();
            if(map.containsKey(entry))
            {
                MarkupEntryCount entryCount = (MarkupEntryCount) map.get(entry);
                entryCount.incrementCount();
            }
            else
            {
                MarkupEntryCount newCount = new MarkupEntryCount(entry);
                map.put(entry,newCount);
            }
        }
        return map;
    }
    
}
