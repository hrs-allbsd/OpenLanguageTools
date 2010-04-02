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

/**
 * This class represents a XML Element Name
 *
 * @author    Brian Kidney
 * @email     brian.kidney@sun.com
 * @created   11 March 2003
 */
public class XmlElementName {

    private XmlElement defaultElement;

    private Map elementsMap = new HashMap();
    
    /**
     * Constructor for the XmlElementName object
     */
    public XmlElementName() {
    }
    
    /**
     * Constructor for the XmlElementName object
     */
    public XmlElementName(XmlElement element) { 
                
        this.addXmlElement(element);
    }
    
    public void setDefaultElement(XmlElement theDefaultElement) {
                
        this.defaultElement = theDefaultElement;
               
    }
    
    public XmlElement getDefaultElement() {
        return defaultElement;
    }
    
    public void addXmlElement(XmlElement element) {
                
        if(element.getDefaultElement()) {
            this.setDefaultElement(element);
        } else {
            elementsMap.put(element.getParentElement(), element);
        }
    }
    
    public XmlElement getChildElement(String parentElement) {
        return (XmlElement) elementsMap.get(parentElement); 
    } 
    
    public Iterator getXmlElementIterator() {
        return elementsMap.values().iterator();
    }
    
    
}
        
    
