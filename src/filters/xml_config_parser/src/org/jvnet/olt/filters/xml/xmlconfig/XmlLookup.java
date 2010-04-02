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

/**
 * Description of the Class
 *
 *@author    kb128066
 *@created   June 2, 2003
 */
public class XmlLookup {

    private String namespaceID = "";
    private String publicID = "";
    private String systemID = "";
    private String schemaLocation = "";
    private String noNamespaceSchemaLocation = "";
    private String rootElement = "";


    /**
     * Sets the namespaceID attribute of the XmlLookup object
     *
     *@param namespaceID  The new namespaceID value
     */
    public void setNamespaceID(String namespaceID) {
        this.namespaceID = namespaceID;
    }


    /**
     * Sets the publicID attribute of the XmlLookup object
     *
     *@param publicID  The new publicID value
     */
    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }


    /**
     * Sets the systemID attribute of the XmlLookup object
     *
     *@param systemID  The new systemID value
     */
    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }


    /**
     * Sets the schemaLocation attribute of the XmlLookup object
     *
     *@param schemaLocation  The new schemaLocation value
     */
    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }


    /**
     * Sets the noNamespaceSchemaLocation attribute of the XmlLookup object
     *
     *@param noNamespaceSchemaLocation  The new noNamespaceSchemaLocation value
     */
    public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }


    /**
     * Sets the rootElement attribute of the XmlLookup object
     *
     *@param rootElement  The new rootElement value
     */
    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }


    /**
     * Gets the namespaceID attribute of the XmlLookup object
     *
     *@return   The namespaceID value
     */
    public String getNamespaceID() {
        return namespaceID;
    }


    /**
     * Gets the publicID attribute of the XmlLookup object
     *
     *@return   The publicID value
     */
    public String getPublicID() {
        return publicID;
    }


    /**
     * Gets the systemID attribute of the XmlLookup object
     *
     *@return   The systemID value
     */
    public String getSystemID() {
        return systemID;
    }


    /**
     * Gets the schemaLocation attribute of the XmlLookup object
     *
     *@return   The schemaLocation value
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }


    /**
     * Gets the noNamespaceSchemaLocation attribute of the XmlLookup object
     *
     *@return   The noNamespaceSchemaLocation value
     */
    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }


    /**
     * Gets the rootElement attribute of the XmlLookup object
     *
     *@return   The rootElement value
     */
    public String getRootElement() {
        return rootElement;
    }

}

