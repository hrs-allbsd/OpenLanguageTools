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
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlSpecificBackConverter.java
 *
 * Created on August 5, 2003, 2:50 PM
 */

package org.jvnet.olt.xliff_back_converter.format.xml;
import org.jvnet.olt.xliff_back_converter.*;
import org.jvnet.olt.filters.xml.XmlEncodingTagController;
import org.jvnet.olt.utilities.XliffZipFileIO;
import java.io.File;
import java.util.Properties;
/**
 *
 * The standard action the XML specific backconverter takes, is to fix the
 * XML encoding declaration at the top of the file (eg. we translate a file
 * from en-US to ja-JP, we should also make sure that the encoding the file
 * gets backconverted to (say, eucJP) also appears in the <code>&lt;?xml</code>
 * declaration.
 *
 * Otherwise it's also a "meta" backconverter - that is, we often use
 * XML as an intermediate step to converting more complex document types. For
 * example, OpenOffice.org sxw files are zip archives containing one translatable
 * file ("content.xml") - so we backconvert first to content.xml, and then take
 * additional actions on this file, to re-archive it with the rest of the .sxw
 * file, which has been carried along as cookies in the .xlz (XLIFF zip) archive.
 * For these cases, if we're providing XML-type-specific conversion, we let the
 * converter choose what it wants to do (wrt. mucking with the xml declaration, etc.)
 *
 * Other file formats may choose to add functionality here.
 *
 * @author  timf
 */
public class XmlSpecificBackConverter extends  SpecificBackconverterBase{    
    /** Creates a new instance of XmlSpecificBackConverter */

    public XmlSpecificBackConverter() {
    }
/*
    public XmlSpecificBackConverter(BackConverterProperties properties) {
        this.properties = properties;
    }
  */  
//    public void convert(String filename, String lang, String encoding, String originalXlzFilename) throws SpecificBackConverterException {
    public void convert(File file) throws SpecificBackConverterException {
        try {
            XmlEncodingTagController.fixEncodingTag(file.getAbsolutePath(), UTF8);
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error fixing XML encoding declaration : " +e.getMessage());
        }
    }
/*
        try {
            if (originalXlzFilename != null){
                XliffZipFileIO xlz = new XliffZipFileIO(new File(originalXlzFilename));
                if (xlz.hasWorkflowProperties()){
                    Properties props = xlz.getWorkflowProperties();
                    String type = props.getProperty("xmltype");
                    XmlBackConversionCommandFactory factory = new DefaultXmlBackConversionCommandFactory();
                    XmlBackConversionCommand command = factory.getXmlBackConversionCommand(type);
                    if (command != null){
                        command.convert(filename,lang,encoding,originalXlzFilename);
                    } else {
                        doGenericXmlBackConversion(filename, encoding);
                    }
                }
                
            } else {
                doGenericXmlBackConversion(filename, encoding);
            }
        } catch (java.util.zip.ZipException e){
            throw new SpecificBackConverterException("Zip Error with xlz file for XML back conversion : " +e.getMessage());
        } catch (XmlBackConversionCommandException e){
            e.printStackTrace();
            throw new SpecificBackConverterException("Error during XML-specific backconversion : " +e.getMessage());
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("IO Error during XML-specific backconversion : " +e.getMessage());
        }
    }
    
    public void convert(String filename, String lang, String encoding) throws SpecificBackConverterException {
        convert(filename, lang, encoding, null);
    }
    
    private void doGenericXmlBackConversion(String filename, String encoding) throws SpecificBackConverterException{
        try {
            XmlEncodingTagController.fixEncodingTag(filename, encoding);
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error fixing XML encoding declaration : " +e.getMessage());
        }
    }
*/    
}
