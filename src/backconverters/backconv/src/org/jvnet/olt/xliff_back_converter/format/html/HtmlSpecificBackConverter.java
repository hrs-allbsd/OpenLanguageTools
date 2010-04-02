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
            UnicodeReverse ur = new HTMLUnicodeReverseImpl();
            UnicodeEntityBackConverter.fix(file.getAbsolutePath(), ur, UTF8);
            
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during html format-specific back conversion : " +e.getMessage());
        }
    }

    public void postConvert(File file) throws SpecificBackConverterException {
        try {
            HtmlMetaTagController.fixMetaTag(file.getAbsolutePath(), targetEncoding);            
        } catch (java.io.IOException e){
            throw new SpecificBackConverterException("Error during html format-specific back conversion : " +e.getMessage());
        }
    }
    
}
