
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * Book.java
 *
 * Created on May 1, 2003, 3:33 PM
 */

package org.jvnet.olt.filters.book;
//import org.jvnet.olt.parsers.SgmlDocFragmentParser.*;
import  org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.parsers.DTDDocFragmentParser.*;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.XliffSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.NullSegmenterFormatter;
import org.jvnet.olt.filters.segmenters.formatters.SegmenterFormatterException;
import org.jvnet.olt.utilities.*;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;
import org.jvnet.olt.parsers.tagged.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;

import com.wutka.dtd.*;

/**
 * This allows us to get information about Solbook .book files. It works by running over the book
 * file, and then recursively running over each system entity (sgml file) the book file
 * calls that is included (ie. not in an ignored marked section) and checks to see if that
 * file is translatable.
 *
 * Upon creating an instance of the class Book, we get back a list of translatable files
 * from this book, and a GlobalVariableManager that allows us to reference any entity
 * declared in the book file, or other included DTD files.
 *
 * An additional thing - we now try to determine if sgml files found during our search
 * were from SYSTEM entities that were within programlisting or screen, etc. tags - eg.
 * "bla bla <programlisting>&my-sample-code;</programlisting>"
 * This suggests that the entire contents of the file referenced by that entity probably
 * isn't "normal" sgml, and shouldn't be treated as such. Instead, we want to put that
 * entire file in a single segment.
 *
 * The singleSegTranslatable set member variable should keep track of those.
 *
 * 
 * @author  timf
 */
public class Book {
    
    private GlobalVariableManager gvm;
    private HashSet translatable = new HashSet();
    private HashSet singleSegTranslatable = new HashSet();
    private Logger logger;
    
    // a simple constructor used for testing...
    public Book(Reader reader, String dir, GlobalVariableManager gvm, boolean isDTD) throws BookException {
        this(reader, dir, gvm, null, isDTD);
    }
    
    /** Creates a new instance of Book
     *
     * @param reader a reader to read the book file
     * @param dir the directory where the book file can be found (references to other
     * dtds within the book file are relative to this directory)
     * @param gvm a global variable manager (that may already exist, in which case
     * we append to it - null otherwise)
     * @param isDTD true if this file is a DTD, false otherwise (.book files are sgml files
     * but we also use this class to read any dtd files the original book file #includes)
     */
    public Book(Reader reader, String dir, GlobalVariableManager gvm, Logger log, boolean isDTD) throws BookException{
        try {
            if (dir == null){
                System.out.println("dir was null, using ./");
                dir = "./";
            }
            if (gvm == null){
                this.gvm = new EntityManager();
            }
            if (log == null){
                // try and get a logger
                java.util.logging.LogManager manager = LogManager.getLogManager();
                logger = manager.getLogger("org.jvnet.olt.filters.book.Book");
                // otherwise, use default
                if (logger == null){
                    System.out.println("Still have null logger - using default");
                    logger = Logger.global;
                }
            } else {
                this.logger = log;
            }
            
            ProcessedSgmlFileVisitor proc;
            GVMFactoryVisitor visitor;
            SegmenterFormatter formatter = new NullSegmenterFormatter();
            // work out what files have to be translated according to this book file
            if (!isDTD){
                NonConformantSgmlDocFragmentParser parser = new  NonConformantSgmlDocFragmentParser(reader);
                visitor = new GVMFactoryVisitor(this.gvm, dir,new NonConformantToTaggedMarkupConverter());
                parser.parse();
                parser.walkParseTree(visitor, null);
                this.gvm = visitor.getGlobalVariableManager();
                proc = new ProcessedSgmlFileVisitor(this.gvm);
                parser.walkParseTree(proc,null);
            } else {
                DTDDocFragmentParser parser = new DTDDocFragmentParser(reader);
                visitor = new GVMFactoryVisitor(this.gvm, dir,new DTDToTaggedMarkupConverter());
                parser.parse();
                parser.walkParseTree(visitor, null);
                this.gvm = visitor.getGlobalVariableManager();
                proc = new ProcessedSgmlFileVisitor(this.gvm);
                // we don't need to collect translatable files if we're just
                // procesing a dtd
                //parser.walkParseTree(proc,null);
            }
            
            if (!isDTD){
                List files = Collections.synchronizedList(proc.getTranslatableFiles());
                Iterator it = files.iterator();
                translatable = new HashSet();
                singleSegTranslatable = new HashSet();
                
                while (it.hasNext()){
                    String trans = (String)it.next();
                    if (trans == null){
                        System.out.println("Eek, got a null!");
                    }
                    //System.out.println("Translatable from bookfile " + trans);
                    translatable.add(trans);
                }
                
                files.addAll(Collections.synchronizedList(proc.getSingleSegmentTranslatableFiles()));
                it = Collections.synchronizedList(proc.getSingleSegmentTranslatableFiles()).iterator();
                while (it.hasNext()){
                    String singleSeg = (String)it.next();
                    if (singleSeg == null){
                        System.out.println("Eek, got a null single segment translatable file!");
                    }
                    System.out.println("SingleSegment translatable from bookfile "+ singleSeg);
                    singleSegTranslatable.add(singleSeg);
                }
                
                
                
                // now load the other files, and see if there's anything else we should translate
                //System.out.println("looking for other files....");
                it = files.iterator();
                while (it.hasNext()){
                    String filename2 = (String)it.next();
                    try {
                        //System.out.println("Looking for translatable files in " + filename2 +" ---------");
                        findTranslatableFiles(filename2,dir,translatable, singleSegTranslatable);
                    } catch (java.io.FileNotFoundException e){
                        //System.out.println("Unable to find "+filename2+" "+e.getMessage());
                        logger.log(Level.WARNING,"Unable to locate translatable file - cannot process "+filename2);
                    }
                }
                logger.log(Level.INFO,"Got " + translatable.size() + " translatable files from book file");
                logger.log(Level.INFO,"Got " + singleSegTranslatable.size() +" files which contain 1 segment");
            }
            
        } catch (IOException e){
            throw new BookException("IO Exception parsing book file " + e.getMessage());            
        } catch (org.jvnet.olt.parsers.SgmlDocFragmentParser.ParseException p){
            throw new BookException("Exception parsing book file " + p.getMessage());
        } catch (org.jvnet.olt.parsers.DTDDocFragmentParser.ParseException d){            
            throw new BookException("Exception parsing included dtd file " + d.getMessage());
        } catch (Throwable t){
            t.printStackTrace();
            throw new BookException("Some other exception occurred processing the book file in " + dir 
            +" : "+ t.getMessage());
        }
    }
    
    /**
     * Given a sgml filename, this runs the ProcessedSgmlFileVisitor over the input file
     * to determine which files are called in that file, and adds them to the
     * translatable set (unless they'return already in there)
     */
    private void findTranslatableFiles(String filename, String directory, Set translatable, Set singleSegTranslatable) throws IOException, org.jvnet.olt.parsers.SgmlDocFragmentParser.ParseException, Exception{
        NonConformantSgmlDocFragmentParser parser;
        File test = new File(directory+"/"+filename);
        if (!test.exists()){
            logger.log(Level.SEVERE,"Translatable file " + filename + " does not exist in the input");
            return;
        }
            
        try {
            //System.out.println("Processing new file ...  " + filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader
            (new FileInputStream(directory+"/"+filename),"ISO8859-1"));
            parser = new  NonConformantSgmlDocFragmentParser(reader);
            parser.parse();
        } catch (Throwable t){
            t.printStackTrace();
            throw new org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.ParseException("Exception trying to parse file "+ filename+" while finding translatable SGML files");
        }
            ProcessedSgmlFileVisitor proc = new ProcessedSgmlFileVisitor(gvm);
            parser.walkParseTree(proc,null);
            List list = proc.getTranslatableFiles();
            Iterator it = list.iterator();
            while (it.hasNext()){
                String trans = (String)it.next();
                if (!translatable.contains(trans)){
                    translatable.add(trans);
                    //System.out.println("translatable file from " + trans+" found in "+filename);
                    findTranslatableFiles(trans,directory,translatable, singleSegTranslatable);
                }
            }
            // Now, do the same for any singleSegment translatable files found
            list = proc.getSingleSegmentTranslatableFiles();
            it = list.iterator();
            while (it.hasNext()){
                String singleSegTrans = (String)it.next();
                if (!translatable.contains(singleSegTrans)){
                    singleSegTranslatable.add(singleSegTrans);
                    //System.out.println("single segment translatable file from " + trans+" found in "+filename);
                    // We *dont* recurse when we find one of these, since the entire file should
                    // contain one segment
                    //findTranslatableFiles(trans,directory,translatable);
                }
            }
        
        
        
    }
    
    
    public static void main(String args[]){
        try {
            if (args.length > 1){
                System.out.println("Usage: Book <book file>");
                System.exit(1);
            }
            if (args.length == 1){
                File file = new File(args[0]);
                String dir = file.getParent();
                BufferedReader reader = new BufferedReader(new InputStreamReader
                (new FileInputStream(args[0]),"ISO8859-1"));
                Book book = new Book(reader, dir , null, null, false); 
                System.out.println("Translatable files are " + book.getTranslatableFiles());
                System.out.println("GVM is " + book.getGlobalVariableManager());
            }
        } catch (java.io.IOException e){
            System.out.println("IO Exception thrown while reading book file " + e.getMessage());
        } catch (BookException e ){
            System.out.println("BookException thrown while reading file " + args[0] + " " + e.getMessage());
        }
    }
    
    /**
     * @return the global variable manager that provides entity information for this book
     */
    public GlobalVariableManager getGlobalVariableManager(){
        return this.gvm;
    }
    
    /**
     * @return the set of files that are for translation in this book file
     */
    public HashSet getTranslatableFiles(){
        return this.translatable;
    }
    
    /**
     * @return the set of files that appear to be program listings, screen output, etc.
     */ 
    public HashSet getSingleSegTranslatableFiles(){
        return this.singleSegTranslatable;
    }
    
    
}
