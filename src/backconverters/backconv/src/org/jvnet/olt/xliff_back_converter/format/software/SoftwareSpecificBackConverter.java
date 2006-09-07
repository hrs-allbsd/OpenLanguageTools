
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
import java.io.File;
import org.jvnet.olt.filters.software.SoftwareFileReformat;
import org.jvnet.olt.xliff_back_converter.*;
/**
 *
 * @author  timf
 */
public class SoftwareSpecificBackConverter extends  SpecificBackconverterBase {
    /** Creates a new instance of HtmlSpecificBackConverter */

    public SoftwareSpecificBackConverter(){
        super();
    }
/*
    public SoftwareSpecificBackConverter(BackConverterProperties properties) {
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
            SoftwareFileReformat reformat = new SoftwareFileReformat(file.getAbsolutePath(), lang, UTF8);
        } catch (org.jvnet.olt.tmci.TMCParseException e){
            throw new SpecificBackConverterException("Parse Error : " +e.getMessage());
        } catch (Throwable t){
            t.printStackTrace();
            throw new SpecificBackConverterException("Parser Error trying to reformat software message file - is the format valid ?\n"+t.getMessage());
        }

    }
}
