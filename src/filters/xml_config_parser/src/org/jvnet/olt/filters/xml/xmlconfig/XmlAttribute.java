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
 * This class represents a XML Attribute
 *
 * @author    Brian Kidney
 * @created   11 March 2003
 * @email     brian.kidney@sun.com
 */
public class XmlAttribute {

    private String attributeName;
    private boolean defaultAttribute = true;
    private boolean translatable = false;
    private String parentElement;


    /**
     * Constructor for the XmlAttribute object
     */
    public XmlAttribute() {
    }


    /**
     * Constructor for the XmlAttribute object
     *
     * @param attributeName  The name of the attribute
     */
    public XmlAttribute(String attributeName) {
        this.setAttributeName(attributeName);
    }


    /**
     * Sets the attributeName attribute of the XmlAttribute object.
     * The attributeName attribute represents the name of the xml attribute.
     *
     * @param attributeName  The new attributeName value
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


    /**
     * Sets the defaultAttribute attribute of the XmlAttribute object.
     * The defaultAttribute should be set to true if this attribute represents 
     * all attributes with the same attribute name, false otherwise.
     *
     * @param defaultAttribute  The new defaultAttribute value
     */
    public void setDefaultAttribute(boolean defaultAttribute) {
        this.defaultAttribute = defaultAttribute;
    }


    /**
     * Sets the translatable attribute of the XmlAttribute object.
     * The translatable attribute should be true if this element is 
     * translatable, false otherwise.
     *
     * @param translatable  The new translatable value
     */
    public void setTranslatable(boolean translatable) {
        this.translatable = translatable;
    }


    /**
     * Sets the parentElement attribute of the XmlAttribute object.
     * The parent element is the XML element that this attribute belongs in.     
     *
     * @param parentElement  The new parentElement value
     */
    public void setParentElement(String parentElement) {
        this.parentElement = parentElement;
    }


    /**
     * Gets the attributeName attribute of the XmlAttribute object.
     * The attributeName attribute represents the name of the xml attribute.
     *
     * @return   The attributeName value
     */
    public String getAttributeName() {
        return attributeName;
    }


    /**
     * Gets the defaultAttribute attribute of the XmlAttribute object.
     * The defaultAttribute is true if this attribute represents 
     * all attributes with the same attribute name, false otherwise.
     *
     * @return   The defaultAttribute value
     */
    public boolean getDefaultAttribute() {
        return defaultAttribute;
    }


    /**
     * Gets the translatable attribute of the XmlAttribute object.
     * The translatable attribute is true if this element is 
     * translatable, false otherwise.
     *
     * @return   The translatable value
     */
    public boolean getTranslatable() {
        return translatable;
    }


    /**
     * Gets the parentElement attribute of the XmlAttribute object.
     * The parent element is the XML element that this attribute belongs in.
     *
     * @return   The parentElement value
     */
    public String getParentElement() {
        return parentElement;
    }


    /**
     * Returns a string describing the element.
     *
     * @return   A String describing the element. 
     */
    public String toString() {
        return "AttributeName = " + getAttributeName() + " \n" +
                "DefaultAttribute = " + getDefaultAttribute() + " \n" +
                "Translatable = " + getTranslatable() + " \n" +
                "ParentElement = " + getParentElement() + " \n";
    }

}

