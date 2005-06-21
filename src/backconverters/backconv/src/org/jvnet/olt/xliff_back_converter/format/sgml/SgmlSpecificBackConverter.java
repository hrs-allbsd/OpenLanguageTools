
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlSpecificBackConverter.java
 *
 * Created on August 5, 2003, 2:50 PM
 */

package org.jvnet.olt.xliff_back_converter.format.sgml;
import org.jvnet.olt.xliff_back_converter.*;
/**
 *
 * @author  timf
 */
public class SgmlSpecificBackConverter implements SpecificBackConverter {
    
    /** Creates a new instance of HtmlSpecificBackConverter */
    public SgmlSpecificBackConverter() {
        
    }
    
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        try {
            UnicodeReverse ur = new SgmlUnicodeReverseImpl();
            UnicodeEntityBackConverter.fix(filename, ur, encoding);
            
            //
            //  This code is added as a fix for bug 5023094. In the case of 
            //  Japanese text, when edited on Windows the wavy dash character
            //  inserted into the file is not the correct one. This incorrect 
            //  character doesn't display nicely in terminals, and gets transformed
            //  into an equals sign when SGML is converted to PDF.
            //
            if(lang.toLowerCase().equals("ja-jp")) {
                WavyDashConverter wdc = new WavyDashConverter();
                wdc.convertFile(filename, encoding);
            }
            //  End fix.
        } 
        catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during sgml format-specific back conversion : " +e.getMessage());
        } 
    }
}
