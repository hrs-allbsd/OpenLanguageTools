/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.lucene;

import org.jvnet.olt.index.*;
import org.jvnet.olt.minitm.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.document.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.analysis.*;

/**
 * Specific implementation of MiniTM that use Lucene index framework
 */
public abstract class LuceneMiniTM implements MiniTM {
    
    private static final Logger logger =  Logger.getLogger(LuceneMiniTM.class.getName());
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    // TM Configugration file name
    private static final String CONFIG_FILE = "TM_CONFIG.XML";
    
    // TM configuration is stored here
    private ConfigHolder config = null;
    
    // location of index store
    private String indexDir = null;
    
    // Lucene related stuff
    private IndexFacade index = null;
    
    
    /**
     * Standard constructor that create new object of LuceneMiniTM class
     *
     * @param indexDir location of Lucene index store
     * @param boolCreateIfMissing create new index store if there is no one
     * @param miniTmName name of the mini TM
     * @param sourceLang source language of the mini TM
     * @param targetLang target language of the mini TM
     *
     * @throws MiniTMException if some error occure
     */
    public LuceneMiniTM(String indexDir, boolean boolCreateIfMissing, String miniTmName, String sourceLang, String targetLang) throws MiniTMException {
        
        this.indexDir = indexDir;
        
        try {
            
            // create index store directory if the directory does not exist
            if(!new File(indexDir).exists()) {
                new File(indexDir).mkdirs();
            }
            
            String configHolderFile = indexDir + FILE_SEPARATOR + CONFIG_FILE;
            
            // add new config file or read new one
            if(!new File(configHolderFile).exists()) {
                
                config = new ConfigHolder();
                config.setNameTM(miniTmName);
                config.setSourceLang(sourceLang);
                config.setTargetLang(targetLang);
                
                try {
                    ConfigHolderHelper.write(configHolderFile,config);
                } catch(FileNotFoundException e) {
                    logger.log(Level.SEVERE,"Cannot write tm configuration: " + e.getMessage(),e);
                    throw new MiniTMException("Cannot write tm configguration: " + e.getMessage());
                }
                
            } else {
                
                config = ConfigHolderHelper.read(configHolderFile);
                
            }
            
            openDataStore();
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot create index directory: " + e.getMessage(),e);
            throw new MiniTMException("Cannot create index directory: " + e.getMessage());
        }
        
    }
    /**
     * Open lucene index store
     *
     * @throws MiniTMException if some error occure
     */
    protected final void openDataStore() throws MiniTMException {
        logger.log(Level.INFO,"Lucene index opening index store");
        
        boolean createIndex = false;
        
        if(!new File(indexDir + "/segments").exists()) {
            createIndex = true;
        }
        
        try {
            index = new IndexFacade(indexDir, new StandardAnalyzer(), createIndex);

        } catch(IOException e) {
            throw new MiniTMException("Cannot open fuzzy index store: " + e.getMessage());
        }
    }
    
    /**
     * methods to check duplicate segments in TM
     *
     * @param newSource source of the checked segment
     * @param translationId id of translator
     *
     * @throws MiniTMException of some error occure
     */
    public boolean isDuplicate(String newSource, String translationID) throws MiniTMException {
        logger.log(Level.FINE,"Checking duplicate entry");
        
        boolean result = false;
        
        Query query = null;
        Hits  hits  = null;
        
        String sourceUnformat = removeFormatting(newSource);
        String text = QueryParser.escape(sourceUnformat);
        
        try {
            Set terms = Collections.synchronizedSet(new HashSet());
            QueryParser parser = new QueryParser("contents",new StandardAnalyzer());
            query = parser.parse(text);
            query.extractTerms(terms);
            
            // do standard search
            query = parser.parse(text);
            
            synchronized(index) {
                
                hits = index.search(query);
                
                // check when ever there is same document
                if(hits!=null && hits.length()>0) {
                    Document doc = hits.doc(0);
                    String source = doc.get("contents");
                    if(sourceUnformat.equals(source)) {
                        result = true;
                    }
                }
            }
            
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Lucene index cannot read from index store: " + e.getMessage(),e);
            throw new MiniTMException("Cannot read from lucene index store: " + e.getMessage());
        } catch(org.apache.lucene.queryParser.ParseException e) {
            logger.log(Level.SEVERE,"Lucene index cannot parse query: " + e.getMessage(),e);
            throw new MiniTMException("Cannot parse query for lucene index store: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Add new segment to index store
     *
     * @param segement to add
     *
     * @param throws MiniTMException if some error occure
     */
    public void addNewSegment(AlignedSegment segment) throws MiniTMException {
        
        long dataStoreId = -1;
        
        // get next id number
        synchronized(config) {
            dataStoreId = config.getLastID() + 1;
            config.setLastID(dataStoreId);
            try {
                ConfigHolderHelper.write(indexDir + FILE_SEPARATOR + CONFIG_FILE,config);
            } catch(FileNotFoundException e) {
                logger.log(Level.SEVERE,"Cannot write tm configuration: " + e.getMessage(),e);
                throw new MiniTMException("Cannot write tm configguration: " + e.getMessage());
            }
        }
        
        addNewSegment(segment,dataStoreId);
    }
    
    /**
     * Add new segment to index store
     *
     * @param segment to add
     * @param dataStoreId to add
     *
     * @param throws MiniTMException if some error occure
     */
    private void addNewSegment(AlignedSegment segment,long dataStoreId) throws MiniTMException {
        logger.log(Level.FINE,"Adding new segment to index store - dataStoreId: " + dataStoreId);
        
        // create new document from the segment
        Document doc = new Document();
        doc.add(new Field("contents", removeFormatting(segment.getSource()), Field.Store.YES,Field.Index.TOKENIZED));
        doc.add(new Field("id",Long.toString(dataStoreId) ,Field.Store.YES,Field.Index.UN_TOKENIZED));
        doc.add(new Field("formattedSource",segment.getSource(),Field.Store.YES,Field.Index.NO));
        doc.add(new Field("formattedTranslation",segment.getTranslation(),Field.Store.YES,Field.Index.NO));
        doc.add(new Field("translatorId",segment.getTranslatorID(),Field.Store.YES,Field.Index.NO));
        
        try {
            synchronized(index) {
                index.addDocument(doc);
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot write to lucene index store: " + e.getMessage(),e);
            throw new MiniTMException("Cannot write to lucene index store: " + e.getMessage());
        }
    }
    
    /**
     * Remove segment from index store
     *
     * @param segment to remove
     * @param dataStoreId the data store id of the segment
     *
     * @throws MiniTMException if some error occure
     */
    public void removeSegment(AlignedSegment segment, long dataStoreId) throws MiniTMException {
        logger.log(Level.FINE,"Removing segment from index store dataStoreId: " + dataStoreId);
        
        try {
            synchronized(index) {
                index.deleteDocuments(new Term("id",Long.toString(dataStoreId)));
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot delete document from lucene index store: " + e.getMessage(),e);
            throw new MiniTMException("Cannot delete document from lucene index store: " + e.getMessage());
        } catch(IllegalStateException e) {
            logger.log(Level.SEVERE,"Lucene index store is already closed: " + e.getMessage(),e);
            throw new MiniTMException("Lucene index store is already closed: " + e.getMessage());
        }
    }
    
    /**
     * Update existing segment in the data store. Currently the method remove the segment and add
     * new one to data store
     *
     * @param segment to update
     * @param dataStoreId the id of the segment
     *
     * @throws MiniTMException if some error occure
     */
    public void updateSegment(AlignedSegment segment, long dataStoreId) throws MiniTMException {
        logger.log(Level.FINE,"Removing segment dataStoreId: " + dataStoreId);
        removeSegment(segment,dataStoreId);
        logger.log(Level.FINE,"Adding segment dataStoreId: " + segment.getDataStoreKey());
        addNewSegment(segment,segment.getDataStoreKey());
    }
    
    
    /**
     * Fetch all existing segment from index store
     *
     * @return an array of segments
     */
    public AlignedSegment[] getAllSegments() {
        logger.log(Level.INFO,"Fetching all segments from index store");
        List result = new LinkedList();
        
        try {
            synchronized(index) {
                
                int maxDoc = index.maxDoc();
                for(int i=0;i<maxDoc;i++) {
                    Document doc = index.document(i);
                    
                    String source = doc.get("formattedSource");
                    String translation = doc.get("formattedTranslation");
                    long id = Long.parseLong(doc.get("id"));
                    String translatorId = doc.get("translatorId");
                    
                    AlignedSegment segment = new AlignedSegment(source,translation,translatorId,id);
                    
                    result.add(segment);
                }
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot read document from lucene index store: " + e.getMessage(),e);
            throw new RuntimeException("Cannot read document from lucene index store: " + e.getMessage(),e);
        } catch(NumberFormatException e) {
            logger.log(Level.SEVERE,"Cannot parse id value in lucene document: " + e.getMessage(),e);
            throw new RuntimeException("Cannot parse id value in lucene document: " + e.getMessage());
        }
        
        return (AlignedSegment[])result.toArray(new AlignedSegment[0]);
    }
    
    /**
     * Match segment in index store
     *
     * @param newSource source of matched segment
     * @param matchThreshold minimal match rate
     * @param maxMatchesToReturn max number of matches to return
     *
     * @return an array of matches
     *
     * @throws MiniTMException if an error occure
     */
    public TMMatch[] getMatchFor(String newSource, int matchThreshold, int maxMatchesToReturn) throws MiniTMException {
        logger.log(Level.FINE,"Matching segment in index store: " + newSource);
        
        if (newSource == null || "".equals(newSource)){
            logger.log(Level.SEVERE,"Lucene index query object is null!");
            return new TMMatch[] {};
        }
        
        List matches = new LinkedList();
        
        Query query = null;
        Hits  hits  = null;
        
        // normalize matched text
        String unformattedSource = removeFormatting(newSource);
        String text = QueryParser.escape(unformattedSource);
        
        try {
            // get number of tokens in queryt
            Set terms = Collections.synchronizedSet(new HashSet());
            QueryParser parser = new QueryParser("contents",new StandardAnalyzer());
            query = parser.parse(text);
            query.extractTerms(terms);
            
            synchronized(index) {
                
                // do fuzzy search for small texts
                if(terms.size()<4) {
                    CustomPhraseParser fuzzyParser = new CustomPhraseParser("contents",new StandardAnalyzer());
                    query = fuzzyParser.parse(text);
                } else {
                    // do standard search for bigger texts
                    query = parser.parse(text);
                }
                hits = index.search(query);
                
                // add matches to the result
                if(hits!=null && hits.length()>0) {
                    
                    int addedMatches = 0;
                    
                    for(int i=0;i<hits.length() && addedMatches<maxMatchesToReturn;i++) {
                        Document doc = hits.doc(i);
                        String source = doc.get("formattedSource");
                        String translation = doc.get("formattedTranslation");
                        long id = Long.parseLong(doc.get("id"));
                        String translatorId = doc.get("translatorId");
                        TMUnit unit = new TMUnit(id,source,translation,translatorId);
                        
                        // count match rate
                        float matchPercent = StringComparer.calculatePercentMatch(newSource, source);
                        
                        if(matchPercent>=matchThreshold) {
                            TMMatch match = createMatch(newSource,unit);
                            matches.add(match);
                            addedMatches++;
                        }
                        
                    }
                    
                }
                
            }
            
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Lucene index cannot read from index store: " + e.getMessage(),e);
            throw new MiniTMException("Lucene index cannot read from index store: " + e.getMessage());
        } catch(org.apache.lucene.queryParser.ParseException e) {
            logger.log(Level.SEVERE,"Lucene index cannot parse query: " + e.getMessage(),e);
            throw new MiniTMException("Lucene index cannot parse query: " + e.getMessage());
        } catch(NumberFormatException e) {
            logger.log(Level.SEVERE,"Lucene index document id is not number: " + e.getMessage(),e);
            throw new MiniTMException("Lucene index document id is not number: " + e.getMessage());
        }
        
        return (TMMatch[])matches.toArray(new TMMatch[0]);
    }
    
    /**
     *  This method is a template method that can be overridden by
     *  subclasses to change the format removing strategy. In this method
     *  the FormatRemovingStrategy object passes the string through untouched.
     */
    protected abstract FormatRemovingStrategy selectFormatRemover();
    
    /**
     * This method is a template method (designed to be overridden) that
     * creates match objects. In this implementation the match quality
     * value is passed through from the fuzzy index without being modified.
     *
     * @param sourceFormatting An ordered list of source formatting
     * @param result A search result returned from the fuzzy index.
     *
     * @throws MiniTMException if some error occure
     */
    protected abstract TMMatch createMatch(String sourceString, TMUnit unit) throws MiniTMException;
    
    /**
     * Get an array of all translators id in index store. Not implemented yet.
     *
     * @return an array of all translators ids in index store
     */
    public String[] getAllTranslatorIDs() {
        // don't have implementation in Lucene right now
        return new String[] {};
    }
    
    /**
     * Return the name of the miniTM
     *
     * @return name of mini-TM
     */
    public String getName() {
        return config.getNameTM();
    }
    
    /**
     * Return source language of mini-TM
     *
     * @return source language of mini-TM
     */
    public String getSourceLang() {
        return config.getSourceLang();
    }
    
    /**
     * Return target language of mini-TM
     *
     * @return target language of mini-TM
     */
    public String getTargetLang() {
        return config.getTargetLang();
    }
    
    /**
     *  This method allows the user to force a save of the TM file.
     *
     *  @throws MiniTMException if an error occure
     */
    public void saveMiniTmToFile() throws MiniTMException {
        logger.log(Level.INFO,"Optimizing index store");
        try {
            synchronized(index) {
                index.optimize();
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot optimize index store: " + e.getMessage(),e);
            throw new MiniTMException("Cannot optimize index store: " + e.getMessage());
        }
    }
    
    
    /**
     * Close all resources
     *
     * @throws MiniTMException if an error occure
     */
    public void close() throws MiniTMException {
        try {
            index.close();
        } catch(IOException e) {
            logger.log(Level.SEVERE,"Cannot close index store: " + e.getMessage(),e);
            throw new MiniTMException("Cannot close index store: " + e.getMessage());
        }
    }
    
    /**
     * Remove all formatting from a source
     *
     * @param segmentText a text with formating
     *
     * @return original text without formatting
     */
    protected String removeFormatting(java.lang.String segmentText) throws org.jvnet.olt.minitm.MiniTMException {
        //  Create a FormatRemovingStrategy
        FormatRemovingStrategy fr = selectFormatRemover();
        return fr.removeFormatting(segmentText);
    }
    
}
