
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
/**
 *
 * @author  timf
 */
public class HtmlSpecificBackConverter implements SpecificBackConverter {
    
    private String datatype = "HTML";
    
    /** Creates a new instance of HtmlSpecificBackConverter */
    public HtmlSpecificBackConverter() {
    }
    
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        try {
            HtmlMetaTagController.fixMetaTag(filename, encoding);            
            UnicodeReverse ur = new HTMLUnicodeReverseImpl();
            UnicodeEntityBackConverter.fix(filename, ur, encoding);
            
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during html format-specific back conversion : " +e.getMessage());
        }
    }
    
}
