
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BackConverterProperties.java
 *
 * Created on August 14, 2002, 4:39 PM
 */
package org.jvnet.olt.xliff_back_converter;

import java.util.Map;
import java.util.HashMap;

/**
 * Stores the properties required by the Xliff Back Converter
 *
 * @author    Brian Kidney
 * @version   August 15, 2002
 */
public class BackConverterProperties {

    /**
     * The key pointer to the location of the resource directory
     */
    public final static String RESOURCE_DIR = "ResourceDir";

    /**
     * The key pointer to the location of the output directory
     */
    public final static String OUTPUT_DIR = "OutputDir";

    /**
     * The key pointer to the location of the XLIFF DTD
     */
    public final static String XLIFF_DTD = "XliffDTD";
    
    /**
     * The key pointer to the location of the XLIFF SKELETON DTD
     */
    public final static String XLIFF_SKELETON_DTD = "XliffSkeletonDTD";
    
    /**
     * The key pointer to the location of the external skeletion file.
     */
    public final static String XLIFF_SKELETON_DIR = "XliffSkeletonDir";

    private Map props = new HashMap();


    /**
     * Creates a new instance of BackConverterProperties
     */
    public BackConverterProperties() { 
    }


    /**
     * Gets the property attribute of the BackConverterProperties object
     *
     * @param thePropertyName  The property key name
     * @return                 The property value
     */
    String getProperty(java.lang.String thePropertyName) {
        return (String) props.get(thePropertyName);
    }


    /**
     * Sets the property attribute of the BackConverterProperties object
     *
     * @param thePropertyName   The new property key name value
     * @param thePropertyValue  The new property value
     */
    void setProperty(java.lang.String thePropertyName, java.lang.String 
        thePropertyValue) {
        props.put(thePropertyName, thePropertyValue);
    }

}

