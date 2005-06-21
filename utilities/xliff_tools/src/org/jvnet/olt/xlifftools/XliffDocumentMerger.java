
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffDocumentMerger.java
 *
 * Created on 07 August 2003, 16:26
 */

package org.jvnet.olt.xlifftools;

import org.jvnet.olt.xliff.PlainXliffDocument;
import org.jvnet.olt.xliff.XliffDocument;
import org.jvnet.olt.xliff.ZippedXliffDocument;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.xliffparser.XliffParser;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;





/**
 *
 * @author  jc73554
 */
public class XliffDocumentMerger
{
    
    private String m_suffix;
    
    /** Creates a new instance of XliffDocumentMerger */
    public XliffDocumentMerger(String suffix)
    {
        m_suffix = suffix;
    }
    
    public static void main(String[] args)
    {
        try
        {
            if(args.length != 4)
            {
                XliffDocumentMerger.printUsage();
                System.exit(2);
            }
            //  Get info from the arguments: inputDir, outputDir, fileName.
            String inputDirName = args[0];
            String outputDirName = args[1];
            String fileName = args[2];
            String suffix = args[3];
            
            //  Create XliffDocumentMerger.
            XliffDocumentMerger merger = new XliffDocumentMerger(suffix);
            
            //  Call the mergeDocuments method.
            File inputDir = new File(inputDirName);
            
            //  Build list of input files matching the pattern specified.
            List docsToMerge = merger.buildDocList(inputDir, fileName);
            
            XliffDocument outputDoc = merger.mergeDocuments(docsToMerge, fileName);
            
            //  Write out the merged document to the output dir.
            File outputFile = new File(outputDirName, fileName);
            outputDoc.writeTo(outputFile);
            
            System.exit(0);
        }
        catch(Exception ex)
        {
            XliffDocumentMerger.printUsage();
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void printUsage()
    {
        System.out.println("");
        System.out.println("Usage:  mergexliff <input dir> <output dir> <filename> <suffix>");
        System.out.println("");
    }
    
    public XliffDocument mergeDocuments(List docsToMerge, String fileName) throws IOException, SAXException, ParserConfigurationException
    {
        //  Create an empty XliffDocument
        XliffDocument outputDoc = createOutputDoc(fileName);
        Writer outputXliffWriter = outputDoc.getXliffWriter();
        
        boolean boolFirstDoc = true;
        
        XliffFileMergingProcessor processor = null;
        
        //  Iterate through the list
        Iterator iterator = docsToMerge.iterator();
        while(iterator.hasNext())
        {
            File xliffFile = (File) iterator.next();
            System.out.println("Processing file... " + xliffFile.getName());
            
            XliffDocument currentDoc = openXliffDoc(xliffFile);
            
            //  If first, copy skeleton data from document to
            if(boolFirstDoc)
            {
                transferSkeletonData(currentDoc,outputDoc);
                
                processor = new XliffFileMergingProcessor(outputXliffWriter,true);
                EntityResolver resolver = new XliffEntityResolver();
                XliffParser parser = new XliffParser(processor, resolver);
                
                parser.parse(currentDoc);
                
                boolFirstDoc = false;
            }
            else
            {
                //  Process the document
                processor = new XliffFileMergingProcessor(outputXliffWriter,false);
                EntityResolver resolver = new XliffEntityResolver();
                XliffParser parser = new XliffParser(processor, resolver);
                
                parser.parse(currentDoc);
            }
        }
        //  Do any necessary cleanup.
        if(processor != null)
        { processor.closeOutDocument(); }
        
        return outputDoc;
    }
    
    public List buildDocList(File inputDir, String fileName)
    {
        //  Generate doc list based on pattern.
        //  Strip filename extension (xlz or xlf).
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        
        //  Create filter
        XliffChunkFileFilter filter = new XliffChunkFileFilter(baseName, m_suffix);
        File[] array = inputDir.listFiles(filter);
        
        //  Sort the file list based on the index on them.
        XliffChunkFilenameComparator comparator = new XliffChunkFilenameComparator();
        Arrays.sort(array, comparator);
        
        return Arrays.asList(array);
    }
    
    protected XliffDocument createOutputDoc(String fileName) throws IOException
    {
        //  Create output file type based on extension
        String lcFileName = fileName.toLowerCase();
        if(lcFileName.endsWith(".xlz"))
        {
            return new ZippedXliffDocument("", "", fileName);
        }
        else
        {
            return new PlainXliffDocument("", fileName);
        }
    }
    
    protected XliffDocument openXliffDoc(File file) throws IOException
    {
        XliffZipFileIO xliffIO = null;
        try
        {
            xliffIO = new XliffZipFileIO(file);
        }
        catch(ZipException exZip)
        {
            exZip.printStackTrace();
            throw new IOException(exZip.getMessage());
        }
        return xliffIO.toXliffDocument();
    }
    
    protected void transferSkeletonData(XliffDocument oldDoc, XliffDocument newDoc) throws IOException
    {
        if(oldDoc.hasSkeletonDocument() && newDoc.hasSkeletonDocument())
        {
            try
            {
                newDoc.setSkeletonData(oldDoc.getSkeletonDocument());
            }
            catch(UnsupportedOperationException exUnsupp)
            {
                //  Can't do stuff
                exUnsupp.printStackTrace();
            }
        }
    }
    
}
