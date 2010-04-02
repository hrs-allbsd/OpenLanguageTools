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
public class RecodingSpecificBackconverter extends  SpecificBackconverterBase{
    
    /** Creates a new instance of RecodingSpecificBackconverter */
    public RecodingSpecificBackconverter() {
    }

    public void convert(File file) throws SpecificBackConverterException {
        File oldFile = new File(file.getAbsolutePath()+".tr");
        File newFile = file;
        
        if(! newFile.renameTo(oldFile))
            throw new SpecificBackConverterException("unable to rename file:"+newFile+" to "+oldFile);
        
        Writer w = null;
        Reader r = null;
        
        try{
            
            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile),targetEncoding));
            r = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile),"UTF-8"));
            
            
            char[] buffer = new char[1024];
            do{
                int count = r.read(buffer);
                if(count == -1)
                    break;
                
                w.write(buffer,0,count);
            }
            while(true);
        } catch (UnsupportedEncodingException uee){
            throw  new SpecificBackConverterException(uee.toString());
        } catch (IOException ioe){
            throw new SpecificBackConverterException(ioe.toString());
        } finally {
            if(w != null)
                try{
                    w.close();
                } catch (IOException ioe){
                    ; //ignore
                }
            
            if(r != null)
                try{
                    r.close();
                } catch (IOException ioe){
                    ; //ignore
                }
            
            oldFile.delete();
        }
    }
    
}
