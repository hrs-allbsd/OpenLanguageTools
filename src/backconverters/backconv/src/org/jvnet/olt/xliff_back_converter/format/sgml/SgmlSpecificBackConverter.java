
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
import java.io.File;
import java.util.Collections;
import java.util.Map;
import org.jvnet.olt.xliff_back_converter.*;
/**
 *
 * @author  timf
 */
public class SgmlSpecificBackConverter extends  SpecificBackconverterBase{
    
    //private BackConverterProperties properties;
    
    
    class CustomMapping implements UnicodeReverse {
        UnicodeReverse delegate;
        Map incMap;
        Map excMap;
        
        CustomMapping(UnicodeReverse delegate,Map includeMap,Map excludeMap){
            this.delegate = delegate;
            this.incMap = includeMap;
            this.excMap = excludeMap;
            
            if(this.incMap == null)
                this.incMap = Collections.EMPTY_MAP;
            if(this.excMap == null)
                this.excMap = Collections.EMPTY_MAP;
        }
        
        public String reverse(char uc) {
            Character cc = new Character(uc);
            
            boolean exclude = false;
            if(excMap.containsKey(cc))
                exclude = true;
            
            if(incMap.containsKey(cc)){
               return (String)incMap.get(cc); 
            }
            
            if(exclude)
                return null;
            
            return delegate.reverse(uc);
        }
        
    }

    public SgmlSpecificBackConverter(){
        super();
    }

    /** Creates a new instance of HtmlSpecificBackConverter */
/*
    public SgmlSpecificBackConverter(BackConverterProperties properties) {
        this.properties = properties;
    }
  */
    /*
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
     */
    public void convert(File file) throws SpecificBackConverterException {
        try {
            UnicodeReverse ur = new SgmlUnicodeReverseImpl();
            
            System.out.println("props:"+props);
            UnicodeReverse wrapper = new CustomMapping(ur,props.getSGMLUnicode2EntityIncludeMap(),props.getSGMLUnicode2EntityExcludeMap());
            
            UnicodeEntityBackConverter.fix(file.getAbsolutePath(), wrapper, encoding);
            
            //
            //  This code is added as a fix for bug 5023094. In the case of 
            //  Japanese text, when edited on Windows the wavy dash character
            //  inserted into the file is not the correct one. This incorrect 
            //  character doesn't display nicely in terminals, and gets transformed
            //  into an equals sign when SGML is converted to PDF.
            //
            if(lang.toLowerCase().equals("ja-jp")) {
                WavyDashConverter wdc = new WavyDashConverter();
                wdc.convertFile(file.getAbsolutePath(), encoding);
            }
            //  End fix.
        } 
        catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during sgml format-specific back conversion : " +e.getMessage());
        } 
    }
}
