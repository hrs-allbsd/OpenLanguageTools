
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

package org.jvnet.olt.xliff_back_converter.format.html;
import org.jvnet.olt.xliff_back_converter.*;
import org.jvnet.olt.filters.html.HtmlMetaTagController;
import java.io.File;
/**
 *
 * @author  timf
 */
public class HtmlSpecificBackConverter extends SpecificBackconverterBase {
    
    //private String datatype = "HTML";
    
    //private BackConverterProperties properties;
    /** Creates a new instance of HtmlSpecificBackConverter */
/*
    public HtmlSpecificBackConverter(BackConverterProperties properties) {
        this.properties = properties;
    }
  */

    public HtmlSpecificBackConverter(){
        super();
    }

/*    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
 */
    public void convert(File file) throws SpecificBackConverterException {
        try {
            HtmlMetaTagController.fixMetaTag(file.getAbsolutePath(), encoding);
            UnicodeReverse ur = new HTMLUnicodeReverseImpl();
            UnicodeEntityBackConverter.fix(file.getAbsolutePath(), ur, encoding);
            
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during html format-specific back conversion : " +e.getMessage());
        }
    }
    
}
