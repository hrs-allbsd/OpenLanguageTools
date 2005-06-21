
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
import org.jvnet.olt.xliff_back_converter.format.plaintext.PlaintextSpecificBackConverter;
import org.jvnet.olt.xliff_back_converter.format.xml.XmlSpecificBackConverter;
import org.jvnet.olt.xliff_back_converter.format.software.SoftwareSpecificBackConverter;

import java.util.HashMap;
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
        
    }
    
    
    public SpecificBackConverter getSpecificBackConverter(String datatype) {
        Integer type = (Integer)converters.get(datatype.toLowerCase());
        if (type == null){
            return new NullSpecificBackConverter();
        } else {
            switch (type.intValue()){
                case HTML:
                    return new HtmlSpecificBackConverter();
                case SGML:
                    return new SgmlSpecificBackConverter();
                case PLAINTEXT:
                    return new PlaintextSpecificBackConverter();
                case XML:
                    return new XmlSpecificBackConverter();
                case SOFTWARE:
                    return new SoftwareSpecificBackConverter();
                    // other types go in here                    
                default:
                    return new NullSpecificBackConverter();
            }
        }
        
    }
}