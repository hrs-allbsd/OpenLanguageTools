
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

package org.jvnet.olt.xliff_back_converter.format.software;
import org.jvnet.olt.filters.software.SoftwareFileReformat;
import org.jvnet.olt.xliff_back_converter.*;
/**
 *
 * @author  timf
 */
public class SoftwareSpecificBackConverter implements SpecificBackConverter {
    private BackConverterProperties properties;
    /** Creates a new instance of HtmlSpecificBackConverter */
    public SoftwareSpecificBackConverter(BackConverterProperties properties) {
        this.properties = properties;
    }
    
    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
        convert(filename, lang, encoding);
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        try {
            SoftwareFileReformat reformat = new SoftwareFileReformat(filename, lang, encoding);
        } catch (org.jvnet.olt.tmci.TMCParseException e){
            throw new SpecificBackConverterException("Parse Error : " +e.getMessage());
        } catch (Throwable t){
            t.printStackTrace();
            throw new SpecificBackConverterException("Parser Error trying to reformat software message file - is the format valid ?\n"+t.getMessage());
        }

    }
}
