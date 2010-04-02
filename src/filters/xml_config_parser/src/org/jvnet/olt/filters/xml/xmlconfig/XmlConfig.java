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

package org.jvnet.olt.filters.xml.xmlconfig;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the configuration of a XML document for the 
 * SunTrans2 XML Parser. 
 *
 * @author    Brian Kidney
 * @email     brian.kidney@sun.com
 * @created   11 March 2003
 */
public class XmlConfig {

    private String fileType = "";
    private String fileTypeDescription = "";
    private String author = "";
    private String generatedBy = "";
    private String generatedDate = "";
    private String lastModifiedDate = "";
    private String identifier = "";
    private String systemID = "";
    private String publicID = "";
    private String schema = "";
    private String noNamespaceSchemaLocation = "";
    private String schemaLocation = "";
    private String namespace = "";

    private Map elementsNameMap = new HashMap();
    private Map xmlElementsAttributesMap = new HashMap();
    private Map allAttributesMap = new HashMap();


    /**
     * Constructor for the XmlConfig object
     */
    public XmlConfig() {
    }


    /**
     * Constructor for the XmlConfig object
     *
     * @param fileType  The XML Document type being configured.  
     */
    public XmlConfig(String fileType) {
        this.setFileType(fileType);
    }


    /**
     * Sets the fileType attribute of the XmlConfig object.
     * The type of XML file (e.g. XHTML, MathML, etc.).
     *
     * @param fileType  The new fileType value
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    /**
     * Sets the fileTypeDescription attribute of the XmlConfig object.
     * A description of the XML file tpye being dealt with.
     *
     * @param fileTypeDescription  The new fileTypeDescription value
     */
    public void setFileTypeDescription(String fileTypeDescription) {
        this.fileTypeDescription = fileTypeDescription;
    }


    /**
     * Sets the author attribute of the XmlConfig object.
     * The person who modified the XML configuration file to make it 
     * suitable for use with the XML file type in question. 
     *
     * @param author  The new author value
     */
    public void setAuthor(String author) {
        this.author = author;
    }


    /**
     * Sets the generatedBy attribute of the XmlConfig object.
     * The tool that auto-generated the XML configuration file.
     *
     * @param generatedBy  The new generatedBy value
     */
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }


    /**
     * Sets the generatedDate attribute of the XmlConfig object.
     * The date on which the XML configuration file was generated.
     *
     * @param generatedDate  The new generatedDate value
     */
    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }


    /**
     * Sets the lastModifiedDate attribute of the XmlConfig object.
     * The last date on which the XML configuration file was generated.
     *
     * @param lastModifiedDate  The new lastModifiedDate value
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    /**
     * Sets the identifier attribute of the XmlConfig object.
     * Indicates if the XML file of this particular type is linked to 
     * either a dtd or schema.
     *
     * @param identifier  The new identifier value
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    /**
     * Sets the systemID attribute of the XmlConfig object
     *
     * @param systemID  The new systemID value
     */
    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }


    /**
     * Sets the publicID attribute of the XmlConfig object
     *
     * @param publicID  The new publicID value
     */
    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }


    /**
     * Sets the schema attribute of the XmlConfig object
     *
     * @param schema  The new schema value
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }


    /**
     * Sets the noNamespaceSchemaLocation attribute of the XmlConfig object
     *
     * @param noNamespaceSchemaLocation  The new noNamespaceSchemaLocation
     *      value
     */
    public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }
    
    /**
     * Sets the schemaLocation attribute of the XmlConfig object
     *
     * @param schemaLocation  The new schemaLocation
     *      value
     */
    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }
    
    /**
     * Sets the namesapce attribute of the XmlConfig object
     *
     * @param namespace  The new namespace
     *      value
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public void addXmlElementName(XmlElement element) {
        
        String elementName = element.getElement();
        if(elementsNameMap.containsKey(elementName)) {
            XmlElementName xmlElementName = (XmlElementName) elementsNameMap.get(elementName);
            xmlElementName.addXmlElement(element);
        } else {
             XmlElementName xmlElementName = new XmlElementName(element);  
             elementsNameMap.put(elementName, xmlElementName);
        }
            
    }
    
    public void addXmlAttribute(XmlAttribute attribute) {
                
        String attributeName = attribute.getAttributeName();
        String parentElement = attribute.getParentElement();
        if(attribute.getDefaultAttribute()) {
            allAttributesMap.put(attributeName, attribute); 
        } else if(!parentElement.equals("")) {
            if(xmlElementsAttributesMap.containsKey(parentElement)) {
                if(attribute.getTranslatable()) {
                    HashMap attributesMap = (HashMap) 
                        xmlElementsAttributesMap.get(parentElement);
                    attributesMap.put(attributeName, attribute);
                }
            } else {
                HashMap attributesMap = new HashMap();
                attributesMap.put(attributeName, attribute);
                xmlElementsAttributesMap.put(parentElement, attributesMap);
            }
        }
        
        
    }
    
    public XmlElement getXmlElement(String elementName) {
        XmlElementName xmlElementNameValue = (XmlElementName) 
            elementsNameMap.get(elementName);
                
        XmlElement defaultXmlElement =  
            xmlElementNameValue.getDefaultElement();
            
        return defaultXmlElement;
    }
    
    public XmlAttribute getXmlAttribute(String attributeName) {
        XmlAttribute xmlAttribute = (XmlAttribute) 
            allAttributesMap.get(attributeName);     
           
        return xmlAttribute;
    }


    /**
     * Gets the fileType attribute of the XmlConfig object.
     * The type of XML file (e.g. XHTML, MathML, etc.). 
     *
     * @return   The fileType value
     */
    public String getFileType() {
        return fileType;
    }


    /**
     * Gets the fileTypeDescription attribute of the XmlConfig object.
     * A description of the XML file tpye being dealt with.
     *
     * @return   The fileTypeDescription value
     */
    public String getFileTypeDescription() {
        return fileTypeDescription;
    }


    /**
     * Gets the author attribute of the XmlConfig object.
     * The person who modified the XML configuration file to make it 
     * suitable for use with the XML file type in question. 
     *
     * @return   The author value
     */
    public String getAuthor() {
        return author;
    }


    /**
     * Gets the generatedBy attribute of the XmlConfig object.
     * The tool that auto-generated the XML configuration file.
     *
     * @return   The generatedBy value
     */
    public String getGeneratedBy() {
        return generatedBy;
    }


    /**
     * Gets the generatedDate attribute of the XmlConfig object.
     * The date on which the XML configuration file was generated.
     *
     * @return   The generatedDate value
     */
    public String getGeneratedDate() {
        return generatedDate;
    }


    /**
     * Gets the lastModifiedDate attribute of the XmlConfig object.
     * The last date on which the XML configuration file was generated. 
     *
     * @return   The lastModifiedDate value
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }


    /**
     * Gets the identifier attribute of the XmlConfig object.
     * Indicates if the XML file of this particular type is linked to 
     * either a dtd or schema.
     *
     * @return   The identifier value
     */
    public String getIdentifier() {
        return identifier;
    }


    /**
     * Gets the systemID attribute of the XmlConfig object
     *
     * @return   The systemID value
     */
    public String getSystemID() {
        return systemID;
    }


    /**
     * Gets the publicID attribute of the XmlConfig object
     *
     * @return   The publicID value
     */
    public String getPublicID() {
        return publicID;
    }


    /**
     * Gets the schema attribute of the XmlConfig object
     *
     * @return   The schema value
     */
    public String getSchema() {
        return schema;
    }


    /**
     * Gets the noNamespaceSchemaLocation attribute of the XmlConfig object
     *
     * @return   The noNamespaceSchemaLocation value
     */
    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }
    
    /**
     * Gets the schemaLocation attribute of the XmlConfig object
     *
     * @return   The schemaLocation value
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }
    
    /**
     * Gets the namespace attribute of the XmlConfig object
     *
     * @return   The namespace value
     */
    public String getNamespace() {
        return namespace;
    }


    /**
     * Returns a string describing the XML Configuration.
     *
     * @return   A string describing the XML Configuration.
     */
    public String toString() {
        
        String result = "\n\n\n";
                
        Iterator xmlElementNameIterator = 
            elementsNameMap.keySet().iterator();  
        while(xmlElementNameIterator.hasNext()) {
            
            String xmlElementNameKey = (String) 
                xmlElementNameIterator.next();
            
                
            result = result + "Xml Element Name : " + xmlElementNameKey + "\n"; 
            
            XmlElementName xmlElementNameValue = (XmlElementName) 
                elementsNameMap.get(xmlElementNameKey);
                
                
            XmlElement defaultXmlElement = xmlElementNameValue.getDefaultElement();
            
            
            if(!(defaultXmlElement == null)) {
                result = result + ("- Default Element : \n " + 
                    defaultXmlElement.toString());
                
                result = result + "Attributes for " + defaultXmlElement.getElement() + " = \n";
                result = result + getAttributesString(defaultXmlElement.getElement());
                
            }
            
            Iterator xmlElementIterator = 
                xmlElementNameValue.getXmlElementIterator();
            
                
            while(xmlElementIterator.hasNext()) {
                XmlElement xmlElement = (XmlElement) 
                    xmlElementIterator.next();
                result = result + ("--- Element : \n " + 
                    xmlElement.toString());
                
                result = result + "Attributes for " + xmlElement.getElement() + " = \n";
                result = result + getAttributesString(xmlElement.getElement());
                 
            }   
        }
        
        result = result + getAllAttributes();
                       
        result = result + 
            "FileType = " + this.getFileType() + " \n" +
            "FileTypeDescription = " + this.getFileTypeDescription() +
            " \n" +
            "Author = " + this.getAuthor() + " \n" +
            "GeneratedBy = " + this.getGeneratedBy() + " \n" +
            "GeneratedDate = " + this.getGeneratedDate() + " \n" +
            "LastModifiedDate = " + this.getLastModifiedDate() +
            " \n" +
            "Identifier = " + this.getIdentifier() + " \n" +
            "SystemID = " + this.getSystemID() + " \n" +
            "PublicID = " + this.getPublicID() + " \n" +
            "Schema = " + this.getSchema() + " \n" +
            "Namespace = " + this.getNamespace() + " \n" +
            "NoNamespaceSchemaLocation = " +
            this.getNoNamespaceSchemaLocation() + " \n";
        
        return result;
    }
    
    private String getAttributesString(String elementName) {
        
        String result = "";
        List attList = getAttributes(elementName);
        
        for(int i = 0; i < attList.size(); i++) {
            result = result + (String) attList.get(i) + "\n";
        }
        
        return result;
        
    }
    
    public List getAttributes(String elementName) {
        
        HashMap allAttributesForElelment = new HashMap();
        allAttributesForElelment.putAll(allAttributesMap);
        
        Map attributesForElement = getAttributesForElement(elementName);         
        Iterator attIterator = attributesForElement.values().iterator();
        
        while(attIterator.hasNext()) {
            XmlAttribute attribute = (XmlAttribute) attIterator.next();
            String attributeName = attribute.getAttributeName();
            
            if(attribute.getTranslatable()) {                
                allAttributesForElelment.put(attributeName, attribute);
            } else {
                allAttributesForElelment.remove(attributeName);
            }
        }
                    
        List attributeList = new ArrayList(
            allAttributesForElelment.keySet());
        
        return attributeList;
    }
    
    private String getAllAttributes() {
        
        String result = "";
        
        if(!allAttributesMap.isEmpty()) {
            
            Iterator xmlAttributeKeyIterator = 
                allAttributesMap.keySet().iterator();
                  
            while(xmlAttributeKeyIterator.hasNext()) {
                
                result = result + ("--- AllAttribute : \n " +  
                    (String) xmlAttributeKeyIterator.next());
            }
        
        }
        
        return result;
    }
    
    private Map getAttributesForElement(String elementName) {
        
        String result = "";
        
        Map attributesMap = new HashMap();
        
        if(!xmlElementsAttributesMap.isEmpty() && xmlElementsAttributesMap.containsKey(
        elementName)) {
            /* System.out.println("11 ");
            System.out.println("xmlElementsAttributesMap size = " + xmlElementsAttributesMap.size());
            
            System.out.println(elementName); */
            
            attributesMap = (HashMap) 
                xmlElementsAttributesMap.get(elementName);
                
                                
            /* System.out.println("attributesMap size = " + attributesMap.size());
            Iterator xmlAttributeMapKeyIterator = 
                attributesMap.keySet().iterator();
          
            while(xmlAttributeMapKeyIterator.hasNext()) {
                String att = (String) xmlAttributeMapKeyIterator.next();
                System.out.println("^^ Att: " + att);
                result = result + ("+++ Attribute : \n " +  
                    att);
            }  */             
        
        }
        
        return attributesMap; 
    }

}


