
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * DefaultXmlBackConversionCommandFactory.java
 *
 * Created on February 10, 2005, 11:34 AM
 */

package org.jvnet.olt.xliff_back_converter.format.xml;

import java.util.HashMap;
import org.jvnet.olt.xliff_back_converter.format.xml.ooo.*;
/**
 * The default XML BackConversion command factory. Right now, the only 
 * Command we know about is the OpenOffice.org backconversion command, but
 * it's conceivable that others could be written and added to this class.
 * @author timf
 */
public class DefaultXmlBackConversionCommandFactory implements XmlBackConversionCommandFactory {
    
    private static final int OPENOFFICE = 0;
    private HashMap knownCommands;
    
    /** Creates a new instance of DefaultXmlBackConversionCommandFactory */
    public DefaultXmlBackConversionCommandFactory() {
        this.knownCommands = new HashMap();
        knownCommands.put("OpenOffice.org Writer", new Integer(OPENOFFICE));
        knownCommands.put("OpenOffice.org Impress", new Integer(OPENOFFICE));                           
        knownCommands.put("OpenOffice.org Calc", new Integer(OPENOFFICE));
    }

    /**
     * Gets an XmlBackConversion command object based on the value of the xmlType
     * parameter.
     * @param xmlType the XML filetype for which we're looking for a converter
     * @return an XmlBackConversion command for this xml type, or null if none is implemented
     */
    public XmlBackConversionCommand getXmlBackConversionCommand(String xmlType) {
        
        Integer commandKey = (Integer)knownCommands.get(xmlType);
        if (commandKey == null){ // we don't recognise the xmlType
            return null;
        }
        switch (commandKey.intValue()){
            case OPENOFFICE:
                return new OpenOfficeXmlBackConversionCommand(xmlType);
            default:
                return null;
        }   
    }
    
}
