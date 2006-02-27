
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * PlaintextSpecificBackConverter.java
 *
 * Created on August 6, 2003, 1:51 PM
 */

package org.jvnet.olt.xliff_back_converter.format.plaintext;
import org.jvnet.olt.xliff_back_converter.*;
import org.jvnet.olt.io.HTMLEscapeFilterWriter;
import java.io.*;
/**
 *
 * @author  timf
 */
public class PlaintextSpecificBackConverter implements SpecificBackConverter {
    
    private BackConverterProperties properties;
    /** Creates a new instance of PlaintextSpecificBackConverter */
    public PlaintextSpecificBackConverter(BackConverterProperties properties) {
        this.properties = properties;
    }
    
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        try {
            //System.out.println("Trying to do plaintext specific back conversion ! on " + filename+ " with encoding" + encoding);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),encoding);
            BufferedReader bufr = new BufferedReader(reader);
            
            File tmp = new File(filename+".st2-temp");
            //System.out.println("Writing tmp file " + tmp.getCanonicalPath());
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(tmp),encoding);
            HTMLEscapeFilterWriter filter = new HTMLEscapeFilterWriter(writer);
            BufferedWriter bufw = new BufferedWriter(filter);
            
            int i=0;
            while((i=bufr.read()) != -1){
                bufw.write(i);
            }
            
            bufw.flush();
            bufw.close();
            bufr.close();
            if (!tmp.renameTo(new File(filename))){
                System.out.println("Warning : Couldn't rename output file ! " + tmp + " contains correct file.");
            }
            
            
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during sgml format-specific back conversion : " +e.getMessage());
        }
    }
    
    
    
}
