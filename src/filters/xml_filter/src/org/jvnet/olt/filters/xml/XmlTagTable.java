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
 * XmlTagTable.java
 *
 */

package org.jvnet.olt.filters.xml;

import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfig;
import org.jvnet.olt.filters.xml.xmlconfig.XmlElement;
import java.util.HashMap;

public class XmlTagTable implements TagTable {
    
    private XmlConfig xmlConfig;
    
    private HashMap configMap;
    
    public XmlTagTable(XmlConfig theXmlConfig) throws XmlFilterException {
        if (theXmlConfig == null){
            throw new XmlFilterException("The XmlConfig object was null");
        }
        this.xmlConfig = theXmlConfig;
    }
    
    public XmlTagTable(HashMap theConfigMap) throws XmlFilterException {
        if (theConfigMap == null){
            throw new XmlFilterException("The XmlconfigMap was null");
        } else if (theConfigMap.size() == 0){
            throw new XmlFilterException("No xmlConfig elements in that xmlConfigMap");
        } else {
            this.configMap = theConfigMap;
        }
        
    }
    
    public boolean tagMayContainPcdata(String tagName) {
        return this.tagMayContainPcdata(tagName, xmlConfig);
    }
    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return this.tagForcesVerbatimLayout(tagName, xmlConfig);
    }
    
    public boolean tagEmpty(String tagName) {
        return this.tagEmpty(tagName, xmlConfig);
    }
    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        
        if(namespaceID.equals("")) {
            return this.tagMayContainPcdata(tagName);
        }
        
        namespaceID = this.stripColon(namespaceID);
        
        XmlConfig config = (XmlConfig) configMap.get(namespaceID);
        
        return this.tagMayContainPcdata(tagName, config);
    }
    
    public boolean tagForcesVerbatimLayout(String tagName,
    String namespaceID) {
        if(namespaceID.equals("")) {
            return this.tagForcesVerbatimLayout(tagName);
        }
        
        namespaceID = this.stripColon(namespaceID);
        
        XmlConfig config = (XmlConfig) configMap.get(namespaceID);
        
        return this.tagForcesVerbatimLayout(tagName, config);
    }
    
    public boolean tagEmpty(String tagName, String namespaceID) {
        if(namespaceID.equals("")) {
            return this.tagEmpty(tagName);
        }
        
        namespaceID = this.stripColon(namespaceID);
        
        XmlConfig config = (XmlConfig) configMap.get(namespaceID);
        return this.tagEmpty(tagName, config);
    }
    
    
    public boolean tagMayContainPcdata(String tagName, XmlConfig config) {
        
        if(tagName.equals("suntransxmlfilter")) {
            return true;
        }
        
        boolean result = false;
        try {
            XmlElement element = config.getXmlElement(tagName);
            if(element.getHardness().equals("inline")) {
                result = true;
            }
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    public boolean tagForcesVerbatimLayout(String tagName, XmlConfig config) {
        boolean result = false;
        try {
            XmlElement element = config.getXmlElement(tagName);
            result = element.getPreserveWhitespace();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    public boolean tagEmpty(String tagName, XmlConfig config) {
        
        if(tagName.equals("suntransxmlfilter")) {
            return true;
        }
        
        boolean result = false;
        try {
            XmlElement element = config.getXmlElement(tagName);
            result = element.getEmpty();
        } catch (NullPointerException ex) {
        }
        return result;
    }
    
    private String stripColon(String colonString) {
        if(colonString.endsWith(":")) {
            int endString = colonString.lastIndexOf(":");
            return colonString.substring(0, endString);
        }
        return colonString;
    }
}
