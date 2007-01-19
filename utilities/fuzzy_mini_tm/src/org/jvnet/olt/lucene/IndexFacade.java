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
 * Hide IndexModifier, IndexReader and IndexSearcher under one facade
 */
public class IndexFacade extends IndexModifier {
    
    // private reader and searcher
    private IndexReader   reader;
    private IndexSearcher searcher;
    
    // index directory
    private String dirName = null;

    // flag that identify if index is modified
    private volatile boolean isModified = false;
    
    /** 
     * Creates a new instance of IndexFacade
     */
    public IndexFacade(String dirName, Analyzer analyzer, boolean create) throws IOException {
        super(dirName,analyzer,create);
        super.setUseCompoundFile(false);
        
        this.dirName = dirName;
        reader = IndexReader.open(dirName);
        searcher = new IndexSearcher(dirName);
    }
    
    /**
     * Returns the documents matching query
     *
     * @param query the index query
     *
     * @throws IOException is some error occure
     */
    public synchronized Hits search(Query query) throws IOException {
        if(isModified) {
            reloadReaders();
        }
        
        return searcher.search(query);
    }
    
    /**
     * Returns one greater than the largest possible document number.
     *
     * @throws IOException if some error occure
     */
    public synchronized int maxDoc() throws IOException {
        if(isModified) {
            reloadReaders();
        }
        
        return reader.maxDoc();
    }
    
    /**
     * Returns the stored fields of the nth Document in this index.
     *
     * @param n the position of document in index
     *
     * @throws IOException if some error occure
     */
    public synchronized Document document(int n) throws IOException {
        if(isModified) {
            reloadReaders();
        }
        
        return reader.document(n);
    }
    
    /**
     * Adds a document to this index.
     *
     * @param doc the document to add
     *
     * @throws IOException if some error occure
     * @throws IllegalStateException if the index is closed
     */
    public synchronized void addDocument(Document doc) throws IOException {
        super.addDocument(doc);
        isModified = true;
    }
    
    /**
     * Deletes all documents containing term. 
     *
     * @param term the term that identify documents to remove
     *
     * @throws IOException if some error occure
     */
    public synchronized void deleteDocument(Term term) throws IOException {
        super.deleteDocuments(term);
        isModified = true;
    }
    
    /**
     * Merges all segments together into a single segment,
     * optimizing an index for search.
     *
     * @throws IOException if some error occure
     */
    public synchronized void optimize() throws IOException {
        try {
            assureOpen();
            super.optimize();
            reloadReaders();
        } catch (IllegalStateException e) {
            System.out.println("Index is already closed");
        }
    }
    
    /**
     * Reload IndexSearcher and IndexReader after modification in
     * index has been done
     *
     * @throws IOException of an error occure
     */
    private void reloadReaders() throws IOException {
        super.flush();
        reader.close();
        reader = IndexReader.open(dirName);
        searcher.close();
        searcher = new IndexSearcher(dirName);
        isModified = false;
    }
    
    /**
     * Close this index, writing all pending changes to disk.
     *
     * @throws IOException if some error occure
     */
    public synchronized void close() throws IOException {
        if(searcher!=null) {
            searcher.close();
        }
        
        if(reader!=null) {
            reader.close();
        }
        
        try {
            super.close();
        } catch(IllegalStateException e) {
            System.out.println("Index is already closed");
        }
    }
    
}
