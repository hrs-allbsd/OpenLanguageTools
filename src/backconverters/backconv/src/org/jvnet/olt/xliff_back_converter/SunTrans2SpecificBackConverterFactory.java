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
 * SunTrans2SpecificBackConverterFactory.java
 *
 * Created on August 5, 2003, 2:44 PM
 */

package org.jvnet.olt.xliff_back_converter;


import org.jvnet.olt.xliff_back_converter.format.html.HtmlSpecificBackConverter;
import org.jvnet.olt.xliff_back_converter.format.sgml.SgmlSpecificBackConverter;
import org.jvnet.olt.xliff_back_converter.format.xml.XmlSpecificBackConverter;
import org.jvnet.olt.xliff_back_converter.format.software.SoftwareSpecificBackConverter;

import java.util.HashMap;
import org.jvnet.olt.xliff_back_converter.format.xml.ooo.OpenOfficeSpecificBackConverter;
/**
 *
 * @author  timf
 */
public class SunTrans2SpecificBackConverterFactory implements SpecificBackConverterFactory {
    
    private SunTrans2SpecificBackConverterFactory fac = null;
    private static final int HTML = 0;
    private static final int SGML = 1;
    private static final int PLAINTEXT = 2;
    private static final int XML = 3;
    private static final int SOFTWARE = 4;
    //Open Office
    private static final int OPENOFFICE = 5;
    
    
    private HashMap converters;
    
    public SunTrans2SpecificBackConverterFactory(){
        
        converters = new HashMap();
        converters.put("html", new Integer(HTML));
        converters.put("htm", new Integer(HTML));
        converters.put("sgml", new Integer(SGML));
        converters.put("sgm", new Integer(SGML));
        converters.put("plaintext", new Integer(PLAINTEXT));
        converters.put("xml", new Integer(XML));
        
        converters.put("po", new Integer(SOFTWARE));
        converters.put("properties", new Integer(SOFTWARE));
        converters.put("msg" , new Integer(SOFTWARE));
        converters.put("tmsg", new Integer(SOFTWARE));
        converters.put("java", new Integer(SOFTWARE));        
        // we don't have one for JSP just yet - probably
        // should use the html one, not sure.
        
        //OpenOffice stuff: these strings actually come from workflow.properties
        converters.put("openoffice.org writer", new Integer(OPENOFFICE));
        converters.put("openoffice.org impress", new Integer(OPENOFFICE));
        converters.put("openoffice.org calc", new Integer(OPENOFFICE));
        converters.put("open document format text", new Integer(OPENOFFICE));
        converters.put("open document format spreadsheet", new Integer(OPENOFFICE));
        converters.put("open document format graphics", new Integer(OPENOFFICE));
        converters.put("open document format presentation", new Integer(OPENOFFICE));
        
        
    }
    
    
    public SpecificBackconverterBase getSpecificBackConverter(String datatype) {
        Integer type = (Integer)converters.get(datatype.toLowerCase());
        if (type == null){
            return new NullSpecificBackConverter();
        } else {
            switch (type.intValue()){
                case HTML:
                    return new HtmlSpecificBackConverter();
                case SGML:
                    return new SgmlSpecificBackConverter();
                case XML:
                    return new XmlSpecificBackConverter();
                case SOFTWARE:
                    return new SoftwareSpecificBackConverter();
                case OPENOFFICE:
                    return new OpenOfficeSpecificBackConverter();
                    // other types go in here 
                default:
                    return new NullSpecificBackConverter();
            }
        }
        
    }
}
