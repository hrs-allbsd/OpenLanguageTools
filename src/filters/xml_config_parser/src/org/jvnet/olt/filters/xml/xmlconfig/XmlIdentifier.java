
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xml.xmlconfig;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * This class represents the identification information of a XML document 
 * for the SunTrans2 XML Parser. 
 *
 * @author    Brian Kidney
 * @email     brian.kidney@sun.com
 * @created   30 June 2003
 */
public class XmlIdentifier implements java.io.Serializable {

    private String systemID;
    private String publicID;
    private String noNamespaceSchemaLocation;
    private String schemaLocation;

    private List namespaceList = new LinkedList();

    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }
    
    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }
    
    public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }
    
    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }
    
    public void setNamespaceList(List namespaceList) {
        this.namespaceList = namespaceList;
    }
    
    public void addNamespace(String namespace) {
        namespaceList.add(namespace);
    }
    
    public String getSystemID() {
        if (systemID == null){
            systemID = "";
        }
        return systemID;
    }
    
    public String getPublicID() {
        if (publicID == null){
            publicID = "";
        }
        return publicID;
    }
    
    public String getNoNamespaceSchemaLocation() {
        if (noNamespaceSchemaLocation == null){
            noNamespaceSchemaLocation = "";
        }
        return noNamespaceSchemaLocation;
    }
    
    public String getSchemaLocation() {
        if (schemaLocation == null){
            schemaLocation = "";
        }
        return schemaLocation;
    }
    
    public List getNamespaceList() {
        if (namespaceList == null){
            namespaceList = new LinkedList();
        }
        return namespaceList;
    }
    
    public String toString() {
        
        String result = "SystemID = " + this.getSystemID() + "\n" +
        "PublicID = " + this.getPublicID() + "\n" +
        "SchemaLocation = " + this.getSchemaLocation() + "\n" +
        "NoNamespaceSchemaLocation = " + this.getNoNamespaceSchemaLocation() + "\n";
        
        Iterator namespaceIterator = this.getNamespaceList().listIterator(0);
        
        if(namespaceIterator.hasNext()) {
            result = result + "Namespace: \n";
        }        
        
        while(namespaceIterator.hasNext()) {
            result = result + (String) namespaceIterator.next() + "\n";
        }
        
        return result;
    }

}
