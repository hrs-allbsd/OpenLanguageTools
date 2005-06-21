
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffZipFileIO.java
 *
 * Created on August 29, 2002, 2:01 PM
 */

package org.jvnet.olt.utilities;

import java.util.zip.*;
import java.io.*;
/**
 *
 * @author  timf
 */
public class TestXliffZipFile {
    
    public TestXliffZipFile(File zipfile){
        try {            
            // This test code reads a .xlz file, and writes the xliff portion of
            // it to an output file output.xlz
            XliffZipFileIO xlz1 = new XliffZipFileIO(zipfile);
            XliffZipFileIO xlz2 = new XliffZipFileIO(new File("output.xlf"));

            Writer xwr2 = xlz2.getXliffWriter();
            Writer swr2 = xlz2.getSklWriter();
                        
            Reader xr1 = xlz1.getXliffReader();
            int c = 0;
            while ((c = xr1.read()) != -1){
                xwr2.write((char)c);
                //System.out.println ("written" +(char)c) ;
            }       
                                               
            xlz2.writeZipFile();
            
            Reader xlf = xlz2.getXliffReader();
           // Reader xlf2 = xlz2.getXliffReader();
            int i=0;
            while ((i= xlf.read()) != -1){
                System.out.print((char)i);
            }
                        
            
            
            
            
        } catch (java.util.zip.ZipException e){
            System.out.println("Zip problems : "+ e.getMessage());
            e.printStackTrace();
        } catch (java.io.IOException e){
            System.out.println("IO problems : "+ e.getMessage());
        }
        
        
    }
    
    
    public static void main(String[] args){
        TestXliffZipFile test = new TestXliffZipFile(new File(args[0]));
    }
    
    
    
}