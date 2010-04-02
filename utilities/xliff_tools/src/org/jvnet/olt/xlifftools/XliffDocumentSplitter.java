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
 * XliffDocumentSplitter.java
 *
 * Created on 07 August 2003, 16:26
 */

package org.jvnet.olt.xlifftools;

import org.jvnet.olt.xliff.XliffDocument;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.xliffparser.XliffParser;
import org.jvnet.olt.xliffparser.XliffProcessor;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.EntityResolver;



/**
 *
 * @author  jc73554
 */
public class XliffDocumentSplitter
{
    private int m_unitModulus;
    private String m_suffix;
    private int m_splitCount;
    
    /** Creates a new instance of XliffDocumentSplitter */
    public XliffDocumentSplitter(String suffix, int unitModulus)
    {
        m_suffix = suffix;
        m_unitModulus = unitModulus;
    }
    
    /**
     *  This method is the interface for splitting an XliffDocument into a
     *  number of smaller XliffDocuments, based on the modulus provided when
     *  creating the splitter. The smaller documents have a name that is derived
     *  from the original document name, but has a suffix attached, that
     *  consists of the suffix string provided, and a number. The number shows
     *  the order of creation of the smaller documents.
     */
    public List splitDocument(XliffDocument doc) throws IOException
    {
        SplitXliffDocumentListHandler xliffList = new SplitXliffDocumentListHandler(m_suffix);
               
        //  Create new parser
        XliffProcessor processor = new XliffFileSplittingProcessor(doc, xliffList, m_unitModulus);
        EntityResolver resolver = new XliffEntityResolver();
        XliffParser parser = new XliffParser(processor, resolver);
        try
        {
            parser.parse(doc);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.err.println("XML error.");
        }
        return xliffList.getXliffDocList();
    }
    
    public static void main(java.lang.String[] args)
    {
        try
        {            
            if(args.length != 4) 
            { 
                XliffDocumentSplitter.printUsage();
                System.exit(1);
            }
            
            //  Read args: document to split, number of units per fragment, suffix 
            String filePath = args[0];
            String suffix = args[1];
            
            String outputLoc = args[3];
            
            int modulus = Integer.parseInt(args[2]);
            File file = new File(filePath);
            
            File outputFileLoc = new File(outputLoc);
            
            //  Create XliffDocument object from a File
            XliffZipFileIO xliffZipFile = new XliffZipFileIO(file);
            XliffDocument doc = xliffZipFile.toXliffDocument();
            
            //  Create an XliffDocumentSplitter
            XliffDocumentSplitter splitter = new XliffDocumentSplitter(suffix, modulus);
            
            //  Split the document
            List outputDocs = splitter.splitDocument(doc);
            System.out.println("Documents generated: " + outputDocs.size());
            
            //  Process the list of document fragments.
            splitter.writeOutputDocuments(outputDocs, outputFileLoc);
            
            System.exit(0);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }   
    
    public static void printUsage()
    {
        System.out.println("");
        System.out.println("Usage:  splitxliff <input file path> <suffix> <units per file> <output dir>");
        System.out.println("");
    }


    /** This method takes a List containing XliffDocuments, and Writes them out to files.
     * @param docList A List containing XliffDocument objects to be written out to file.
     * @param location A File representing the directory that the user wishes the output to be written to.
     * @throws IOException Thrown when an I/O error occurs.
     */    
    public void writeOutputDocuments(List docList, File location) throws IOException
    {
        //  Guard clause: Test the output location
        if(!(location.isDirectory() && location.canWrite()) ) 
        {
            throw new IOException("The name specified for the ouptut location is not a writable directory.");
        }
        
        //  Write out each file
        Iterator iterator = docList.iterator();
        while(iterator.hasNext())
        {
            XliffDocument xliffDoc = (XliffDocument) iterator.next();
            File outputFile = new File(location, xliffDoc.getDocumentName());
            if(outputFile.exists()) 
            {
                //  Do something here. Throw an Exception?
                throw new IOException("The output file '" + outputFile.getName() +"' already exists in the directory '"+ outputFile.getParent() +"'.");
            }
            else
            {
                xliffDoc.writeTo(outputFile);
            }
        }
    }    
}
