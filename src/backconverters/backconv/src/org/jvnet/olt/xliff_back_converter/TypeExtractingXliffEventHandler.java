
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TypeExtractingXliffEventHandler.java
 *
 * Created on June 21, 2004, 2:39 PM
 */

package org.jvnet.olt.xliff_back_converter;

/** This class is concerned with determining what the values of attributes in 
 * the 'file' element are, and passing that on to apps.
 * @author  jc73554
 */
public class TypeExtractingXliffEventHandler extends AbstractXliffEventHandler {
    
    private String originalType = "";
    
    /** Creates a new instance of TypeExtractingXliffEventHandler */
    public TypeExtractingXliffEventHandler() {
    }
    
    /** This method is an XML parser event handler for XLIFF 'file' elements. It is
     * fired whenever an opening tag for a 'file' element is encountered.
     */
    public void start_file(org.xml.sax.Attributes meta) {
        //  Get attribute value for "datatype"
        String attrib = meta.getValue("datatype");
        
        if(attrib != null) {
            originalType = attrib;
        }
    }
    
    public String getOriginalType() {
        return originalType;
    }
}
