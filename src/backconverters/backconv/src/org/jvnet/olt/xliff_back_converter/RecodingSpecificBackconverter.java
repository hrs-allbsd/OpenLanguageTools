/*
 * RecodingSpecificBackconverter.java
 *
 * Created on April 10, 2006, 4:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.xliff_back_converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 *
 * @author boris
 */
public class RecodingSpecificBackconverter implements SpecificBackConverter{
    
    /** Creates a new instance of RecodingSpecificBackconverter */
    public RecodingSpecificBackconverter() {
    }

    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        convert(filename,lang,encoding,null);
    }

    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        File oldFile = new File(filename+".tr");
        File newFile = new File(filename);

        if(! newFile.renameTo(oldFile))
            throw new SpecificBackConverterException("unable to rename file:"+newFile+" to "+oldFile);

        Writer w = null;
        Reader r = null;

        try{

            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile),encoding));
            r = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile),"UTF-8"));


            char[] buffer = new char[1024];
            do{
                int count = r.read(buffer);
                if(count == -1)
                    break;

                w.write(buffer,0,count);
            }
            while(true);
        }
        catch (UnsupportedEncodingException uee){
            throw  new SpecificBackConverterException(uee.toString());
        }
        catch (IOException ioe){
            throw new SpecificBackConverterException(ioe.toString());
        }
        finally {
            if(w != null)
                try{
                    w.close();
                }
                catch (IOException ioe){
                    ; //ignore
                }

            if(r != null)
                try{
                    r.close();
                }
                catch (IOException ioe){
                    ; //ignore
                }

            oldFile.delete();
        }
    }
    
}
