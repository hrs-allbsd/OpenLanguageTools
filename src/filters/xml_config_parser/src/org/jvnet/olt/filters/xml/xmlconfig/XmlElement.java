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
 * This class represents a XML Element
 *
 * @author    Brian Kidney
 * @email     brian.kidney@sun.com
 * @created   11 March 2003
 */
public class XmlElement {

    private String element;

    private boolean rootElement = false;
    private boolean defaultElement = true;
    private boolean translatable = true;
    private boolean empty = false;
    private boolean preserveWhitespace = false;
    private boolean nonsegmentable = false;
    private boolean nonsegmentablenowordcount = false;
    
    
    private String parentElement;
    private String hardness = "inline";


    /**
     * Constructor for the XmlElement object
     */
    public XmlElement() {
    }


    /**
     * Constructor for the XmlElement object
     *
     * @param element  The name of the element
     */
    public XmlElement(String element) {
        this.setElement(element);
    }


    /**
     * Sets the element attribute of the XmlElement object.
     * The element attribute is the name of the attribute.
     *
     * @param element  The new element value
     */
    public void setElement(String element) {
        this.element = element;
    }


    /**
     * Sets the rootElement attribute of the XmlElement object.
     * The rootElement should be true if this element is the root element 
     * of the XML document, false otherwise.
     *
     * @param rootElement  The new rootElement value
     */
    public void setRootElement(boolean rootElement) {
        this.rootElement = rootElement;
    }


    /**
     * Sets the defaultElement attribute of the XmlElement object.
     * The defaultElement should be true if this element represents all 
     * elements with the same element name, false otherwise.     
     *
     * @param defaultElement  The new defaultElement value
     */
    public void setDefaultElement(boolean defaultElement) {
        this.defaultElement = defaultElement;
    }


    /**
     * Sets the translatable attribute of the XmlElement object.
     * The translatable attribute should be true if this element is 
     * translatable, false otherwise.
     *
     * @param translatable  The new translatable value
     */
    public void setTranslatable(boolean translatable) {
        this.translatable = translatable;
    }


    public void setNonSegmentableNoWordcount(boolean nonsegmentablenowordcount){
        this.nonsegmentablenowordcount = nonsegmentablenowordcount;
    }
    
    public void setNonSegmentable(boolean nonsegmentable){
        this.nonsegmentable = nonsegmentable;
    }
    
    
    /**
     * Sets the empty attribute of the XmlElement object.
     * The empty attribute should be true if the element is true,
     * false otherwise.
     *
     * @param empty  The new empty value
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }


    /**
     * Sets the preserveWhitespace attribute of the XmlElement object.
     * The preserveWhitespace attribute should be true if whitespace is to 
     * be preserved, false otherwise.
     *
     * @param preserveWhitespace  The new preserveWhitespace value
     */
    public void setPreserveWhitespace(boolean preserveWhitespace) {
        this.preserveWhitespace = preserveWhitespace;
    }

    /**
     * Sets the parentElement attribute of the XmlElement object.
     *
     * @param parentElement  The ancestor of the current element.
     */
    public void setParentElement(String parentElement) {
        this.parentElement = parentElement;
    }

    /**
     * Sets the hardness attribute of the XmlElement object.
     *
     * @param hardness  The new hardness value
     */
    public void setHardness(String hardness) {
        this.hardness = hardness;
    }


    /**
     * Gets the element attribute of the XmlElement object.
     * The element attribute is the name of the attribute.
     *
     * @return   The element value
     */
    public String getElement() {
        return element;
    }


    /**
     * Gets the rootElement attribute of the XmlElement object.
     * The translatable attribute is true if this element is 
     * translatable, false otherwise.
     *
     * @return   The rootElement value
     */
    public boolean getRootElement() {
        return rootElement;
    }


    /**
     * Gets the defaultElement attribute of the XmlElement object.
     * The defaultElement is true if this element represents all 
     * elements with the same element name, false otherwise.
     *
     * @return   The defaultElement value
     */
    public boolean getDefaultElement() {
        return defaultElement;
    }


    /**
     * Gets the translatable attribute of the XmlElement object.
     * The translatable attribute is true if this element is 
     * translatable, false otherwise.
     *
     * @return   The translatable value
     */
    public boolean getTranslatable() {
        return translatable;
    }

    
    public boolean getNonSegmentableNoWordcount() {
        return nonsegmentablenowordcount;
    }
    
    
    public boolean getNonSegmentable() {
        return nonsegmentable;
    }

    /**
     * Gets the empty attribute of the XmlElement object.
     * The empty attribute is true if the element is true,
     * false otherwise.
     *
     * @return   The empty value
     */
    public boolean getEmpty() {
        return empty;
    }


    /**
     * Gets the preserveWhitespace attribute of the XmlElement object.
     * The preserveWhitespace attribute is true if whitespace is to 
     * be preserved, false otherwise.
     *
     * @return   The preserveWhitespace value
     */
    public boolean getPreserveWhitespace() {
        return preserveWhitespace;
    }
        
    /**
     * Gets the parentElement attribute of the XmlElement object
     *
     * @return   The ancestor of the current Element
     */
    public String getParentElement() {
        return parentElement;
    }


    /**
     * Gets the hardness attribute of the XmlElement object
     *
     * @return   The hardness value
     */
    public String getHardness() {
        return hardness;
    }


    /**
     * Returns a string describing the element.
     *
     * @return   A String describing the element. 
     */
    public String toString() {
        return "Element = " + getElement() + " \n" +
               "RootElement = " + getRootElement() + " \n" +
               "DefaultElement = " + getDefaultElement() + " \n" +
               "Translatable = " + getTranslatable() + " \n" +
               "NonSegmentable = " +getNonSegmentable() + "\n"+
               "NonSegmentableNoWordcount = " +getNonSegmentableNoWordcount()+ "\n"+
               "Empty = " + getEmpty() + " \n" +
               "PreserveWhitespace = " + getPreserveWhitespace() + " \n" +
               "ParentElement = " + getParentElement() + " \n" +
               "Hardness = " + getHardness() + " \n";
    }

}

