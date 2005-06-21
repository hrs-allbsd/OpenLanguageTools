
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ApproximateMatchIndex.java
 *
 * Created on May 25, 2004, 5:23 PM
 */

package org.jvnet.olt.index;

import org.jvnet.olt.index.StringComparer;
import org.jvnet.olt.minitm.MiniTMIndex;
import org.jvnet.olt.minitm.MiniTMException;
import org.jvnet.olt.minitm.MiniTMIndexException;
import org.jvnet.olt.minitm.SearchResult;
import org.jvnet.olt.minitm.TMUnit;
import org.jvnet.olt.minitm.DataStore;
import org.jvnet.olt.minitm.FormatRemovingStrategy;
import java.util.*;

/** This class implements the an index for the mini TM function.
 *
 * @author  jc73554
 */
public class ApproximateMatchIndex implements MiniTMIndex {
    
    /* Implementation note: this index uses a map of sets to hold the entries
     */
    private Map indexEntries;
    
    /** Creates a new instance of ApproximateMatchIndex */
    public ApproximateMatchIndex() {
        indexEntries = new HashMap();
    }

    public void openDataStore(DataStore store, FormatRemovingStrategy fr)  throws MiniTMException {
        String plainText;
        
        TMUnit[] tmunitArray = store.getAllTMs();

        for(int i = 0; i < tmunitArray.length; i++) {
            plainText = fr.removeFormatting((tmunitArray[i].getSource()).toString());
            insertItem(plainText, tmunitArray[i].getDataSourceKey());
        }
    }
    
    public synchronized SearchResult[] doSearch(String queryString, int matchThreshold) throws MiniTMIndexException {
        //  calculate length range
        int queryStringLen = queryString.length();
        int upperLen = (int)(queryStringLen * ((100.0 + (100.0 - matchThreshold))/100.0) );
        int lowerLen = (int)(queryStringLen * ((100.0 - (100.0 - matchThreshold))/100.0) );
        
        //  create a list to hold the search results
        List searchResultList = new LinkedList();
                
        Set keys = indexEntries.keySet();
        Iterator iterator = keys.iterator();
        while(iterator.hasNext()) {
            String key = (String) iterator.next();
            
            //  test to see if key in the range where a match is possible
            if((key.length() <= upperLen) && (key.length() >= lowerLen)) {  
                //  Do the comparison
                float matchPercent = StringComparer.calculatePercentMatch(queryString, key);
                
                if( matchPercent >= matchThreshold ) {
                    Set itemSet = (Set) indexEntries.get(key);
                    addItemsToSearchResults(searchResultList, itemSet, (int) matchPercent);
                }
            }
        }
        //  Sort the list
        Collections.sort(searchResultList, new org.jvnet.olt.minitm.SearchResultComparator());

        //  convert the list to a SearchResult array
        return ((SearchResult[]) searchResultList.toArray(new SearchResult[0]));
    }
    
    public synchronized boolean inIndex(String queryString) {
        return indexEntries.containsKey(queryString);
    }
    
    public synchronized void insertItem(String toIndex, long dataStoreKey) throws MiniTMIndexException {
        IndexEntry entry = new IndexEntry(toIndex, dataStoreKey);
        
        //  Add to the map
        if(inIndex(toIndex)) {
            //  retrieve the Set from the map
            Set itemSet = (Set) indexEntries.get(toIndex);
            
            //  guard clause
            if(itemSet == null) {
                throw new MiniTMIndexException("Index corrupted. The itemSet is missing.");
            }
            //  insert the entry
            itemSet.add(entry);
        } else {
            //  create a Set
            Set itemSet = new TreeSet();
            itemSet.add(entry);
            
            //  add the Set to the map.
            indexEntries.put(toIndex, itemSet);
        }
    }
    
    public synchronized boolean removeItem(String toRemove, long dataSourceKey) {
        if(!inIndex(toRemove)) {
            return false;  //  Not there, so we couldn't remove it.
        }
        Set itemSet = (Set) indexEntries.get(toRemove);
        
        IndexEntry entry = new IndexEntry(toRemove, dataSourceKey);
        boolean itemRemoved = itemSet.remove(entry);
        
        //  if the set is empty then remove the map entry
        if(itemSet.isEmpty()) {
            indexEntries.remove(toRemove);
        }        
        return itemRemoved;
    }
       
    private void addItemsToSearchResults(List searchResultsList, Set itemSet, int matchQuality) {
        Iterator iterator = itemSet.iterator();
        while(iterator.hasNext()) {
            IndexEntry entry = (IndexEntry) iterator.next();
            
            org.jvnet.olt.minitm.SearchResult searchResult = new org.jvnet.olt.minitm.SearchResult(entry.getIndex(), matchQuality);
            
            searchResultsList.add(searchResult);
        }
    }    
}
