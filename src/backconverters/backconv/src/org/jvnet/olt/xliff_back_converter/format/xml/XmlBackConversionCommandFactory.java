
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlBackConversionCommandFactory.java
 *
 * Created on February 10, 2005, 11:17 AM
 */

package org.jvnet.olt.xliff_back_converter.format.xml;

/**
 * We're using the GoF Factory pattern here to allow us to return one of
 * several different XmlBackConversionCommands - which are ultimately chosen
 * based on the value of the "xmltype" workflow property (a cookie that gets
 * set in the "workflow.properties" file, which is stored in xlz files)
 * 
 * We're doing this specifically for XML, since the XML converter is
 * a generic filter which can be used for many different XML types and we'd
 * like a way to be able to take different backconversion actions for different
 * XML types. The idea of this class is very similar to that of the 
 * SpecificBackConverter class in a parent package : which takes actions based
 * on the original file type (eg. html, sgml, po, properties, etc.) - but XML is a
 * meta-format, so that's why this class is required.
 *
 * @author timf
 */
public interface XmlBackConversionCommandFactory {
    
    /**
     * Gets an XmlBackConversion command object based on the value of the xmlType
     * parameter
     * @param xmlType the XML filetype for which we're looking for a converter
     * @return an XmlBackConversion command for this xml type, or null if none is implemented
     * for this xmlType.
     */
    public XmlBackConversionCommand getXmlBackConversionCommand(String xmlType);
    
}
