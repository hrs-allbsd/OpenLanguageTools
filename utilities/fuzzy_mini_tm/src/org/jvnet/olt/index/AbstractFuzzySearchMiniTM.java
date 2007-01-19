
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

import org.jvnet.olt.minitm.*;
import java.util.*;

public abstract class AbstractFuzzySearchMiniTM
implements MiniTM {
    protected MiniTMIndex fuzzyIndex;
    protected DataStore dataStore;
    
    public AbstractFuzzySearchMiniTM(String tmFile,
    boolean boolCreateIfMissing,
    String miniTmName,
    String sourceLang,
    String targetLang)
    throws MiniTMException {
        //  Pass on physical layer reference (file name)
        dataStore = new BasicDataStore(tmFile,
        boolCreateIfMissing,
        miniTmName,
        sourceLang,
        targetLang);
                
        //  Generate the fuzzy index
        fuzzyIndex = new ApproximateMatchIndex();
    }
    
    protected final void openDataStore() throws MiniTMException {
        fuzzyIndex.openDataStore(dataStore, selectFormatRemover());
    }
    
    public boolean isDuplicate(String newSource, String translationID)
    throws MiniTMException {
        //  Not sure this is a good idea but it will do for now.
        //  We may want the translation to be checked as well before returning
        //  true.
        return fuzzyIndex.inIndex(removeFormatting(newSource));
    }
    
    public void addNewSegment(AlignedSegment segment)
    throws MiniTMException {
        synchedAddNewSegment(segment);
    }
    
    
    public void updateSegment(AlignedSegment segment, long dataStoreId)
    throws MiniTMException {
        synchedUpdateSegment(segment, dataStoreId);
    }
    
    public void removeSegment(AlignedSegment segment, long dataStoreId)
    throws MiniTMException {
        synchedRemoveSegment(segment, dataStoreId);
    }
    
    //
    //  Synchronized methods to protect the underlying data in a
    //  multithreaded environment.
    //
    protected synchronized void synchedAddNewSegment(AlignedSegment segment)
    throws MiniTMException {
        long id = dataStore.insertItem(segment);
        fuzzyIndex.insertItem(removeFormatting(segment.getSource()), id);
    }
    
    
    protected synchronized void synchedUpdateSegment(AlignedSegment segment,
    long dataStoreId)
    throws MiniTMException {
        TMUnit unit = dataStore.getItem(dataStoreId);
        
        if(unit != null) {
            //  Make sure base text matches
            if(unit.getSource().equals(segment.getSource())) {
                unit.updateTranslation(segment.getTranslation(),
                segment.getTranslatorID());
            }
        }
    }
    
    protected synchronized void synchedRemoveSegment(AlignedSegment segment,
    long dataStoreId)
    throws MiniTMException {
        //  Remove item from the fuzzy index.
        fuzzyIndex.removeItem(removeFormatting(segment.getSource()),
        dataStoreId);
        
        //  Remove from the DataStore.
        dataStore.removeItem(dataStoreId);
    }
    
    
    
    public AlignedSegment[] getAllSegments() {
        //  Delegate to the DataStore
        synchronized(this) {
            return dataStore.getAllSegments();
        }
    }
    
    public TMMatch[] getMatchFor(String newSource,
    int matchThreshold,
    int maxMatchesToReturn)
    throws MiniTMException {
        synchronized(this) {
            SearchResult[] results = fuzzyIndex.doSearch(removeFormatting(newSource), matchThreshold);
            
            if(results == null) {
                return new TMMatch[0];
            }
           
            int maxMatch;
            
            //  Determine how many matches to return.
            if( (maxMatchesToReturn > 0) && (maxMatchesToReturn <= results.length) ) {
                maxMatch = maxMatchesToReturn;
            }
            else {
                maxMatch = results.length;
            }
            
            List listMatches = new LinkedList();
            
            int i = 0;
            int j = 0;
            while((i < maxMatch) && (j < results.length)) {
                try {
                    TMMatch match = createMatch( newSource, results[j]);
                    j++;
                    
                    //  Test to see if formatting has booted us out of the match
                    //  zone.
                    if(match.getRatioOfMatch() >= matchThreshold) {
                        //  Slot into the array.
                        listMatches.add(match);
                        i++;
                    }
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    throw new MiniTMException("Array bounds error" +ex.getMessage());
                }
            }
            //  Providing an TMMatch[] to the the toArray method to type the
            //  returned array.
            return (TMMatch[]) listMatches.toArray(new TMMatch[0]);
        }
    }
    
    //  template methods
    /**
     *  This method is a template method that can be overridden by
     *  subclasses to change the format removing strategy. In this method
     *  the FormatRemovingStrategy object passes the string through untouched.
     */
    protected abstract FormatRemovingStrategy selectFormatRemover();
    
    /**
     *  This method is a template method (designed to be overridden) that
     *  creates match objects. In this implementation the match quality
     *  value is passed through from the fuzzy index without being modified.
     *  @param sourceFormatting An ordered list of source formatting
     *  @param result A search result returned from the fuzzy index.
     */
    protected abstract TMMatch createMatch(String sourceString,
    SearchResult result)
    throws MiniTMException;
    
    public String[] getAllTranslatorIDs() {
        //  Delegate to the data store.
        return dataStore.getAllTranslatorIDs();
    }
    
    //  Accessors
    public String getName() { return dataStore.getTmName(); }
    public String getSourceLang() { return dataStore.getSourceLang(); }
    public String getTargetLang() { return dataStore.getTargetLang(); }
    
    public void saveMiniTmToFile()
    throws MiniTMException {
        dataStore.saveMiniTmToFile();
    }
    
    protected String removeFormatting(java.lang.String segmentText) throws org.jvnet.olt.minitm.MiniTMException {
        //  Create a FormatRemovingStrategy
        FormatRemovingStrategy fr = selectFormatRemover();
        return fr.removeFormatting(segmentText);
    }
    
    /**
     * Close all resources
     */
    public void close() throws MiniTMException {
        dataStore.saveMiniTmToFile();
    }
    
}
