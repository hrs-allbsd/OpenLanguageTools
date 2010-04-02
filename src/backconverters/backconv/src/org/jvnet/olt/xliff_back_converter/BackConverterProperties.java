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
 * BackConverterProperties.java
 *
 * Created on August 14, 2002, 4:39 PM
 */
package org.jvnet.olt.xliff_back_converter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    
    /* Name of property specifying which file name to use: either the original
     * file name stored in tag file, attribute original (when property set to true)
     * or name of the xlz file
     */
    public static final String PROP_GEN_PREFER_XLZ_NAME = "general.prefer_xlz_name";
    
    /* name of property specifying name of the current xlz file. This name us used
     * if users choses to backconvert to file similar to file with xlz name, rather
     * than file name stored in xlz file
     */
    public static final String PROP_GEN_XLZ_FILE_NAME = "general.xlz_file_name";
    
    /* name of the property specifying name of encoding property. This encoding property
     * is used for correct file encodig */
    public static final String PROP_GEN_FILE_ENCODING = "general.encoding";

    /** name of the property specifying whether to overwrite existing files or not
     */
    public static final String PROP_GEN_OVERWRITE_FILES = "general.overwrite_files";
    
    /* name of property specifying  whether to write translation status to SGML
     * or not
     */
    public static final String PROP_SGML_WRITE_TRANS_STATUS = "sgml.write_trans_status";


    
    private Properties props = new Properties();

    private Map unicode2entityIncludeMap = new HashMap();
    private Map unicode2entityExcludeMap = new HashMap();



    /**
     * Creates a new instance of BackConverterProperties
     */
    public BackConverterProperties() { 
        setDefaults();
    }

    public BackConverterProperties(Properties props){
        setDefaults();
        this.props.putAll(props);
    }
    
    private void setDefaults(){
        setBooleanProperty(PROP_GEN_PREFER_XLZ_NAME,true);
    }
    
    /**
     * Gets the property attribute of the BackConverterProperties object
     *
     * @param thePropertyName  The property key name
     * @return                 The property value
     */
    public String getProperty(java.lang.String thePropertyName) {
        return getProperty(thePropertyName,null);
    }


    /**
     * Sets the property attribute of the BackConverterProperties object
     *
     * @param thePropertyName   The new property key name value
     * @param thePropertyValue  The new property value
     */
    public void setProperty(java.lang.String thePropertyName, java.lang.String 
        thePropertyValue) {
        props.setProperty(thePropertyName, thePropertyValue);
    }

    public void setProperty(java.lang.String thePropertyName,boolean value) {
        props.setProperty(thePropertyName, Boolean.toString(value));
    }

    public String getProperty(String propName,String defaultValue){
        return props.getProperty(propName,defaultValue);
    }

    public boolean getBooleanProperty(String propName){
        return getBooleanProperty(propName,false);
    }

    public boolean getBooleanProperty(String propName,boolean defaultValue){
        String prop = props.getProperty(propName);
        return prop == null ? defaultValue : "true".equals(prop);
    }

    public void setBooleanProperty(String propName,boolean value){
        props.setProperty(propName,Boolean.toString(value));
    }
    
    public Properties getProperties(){
        return new Properties(props);
    }
    
    public void setSGMLUnicode2EntityIncludeMap(Map u2cMap){
        this.unicode2entityIncludeMap.clear();
        this.unicode2entityIncludeMap.putAll(u2cMap);
    }

    public Map getSGMLUnicode2EntityIncludeMap(){
        return this.unicode2entityIncludeMap;
    }
    public void setSGMLUnicode2EntityExcludeMap(Map u2cMap){
        this.unicode2entityExcludeMap.clear();
        this.unicode2entityExcludeMap.putAll(u2cMap);
    }

    public Map getSGMLUnicode2EntityExcludeMap(){
        return this.unicode2entityExcludeMap;
    }
    
    
}

