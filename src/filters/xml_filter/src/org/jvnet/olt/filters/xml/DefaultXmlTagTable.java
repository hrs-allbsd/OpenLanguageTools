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
 * DefaultXmlTagTable.java
 *
 * Created on October 29, 2003, 4:19 PM
 */

package org.jvnet.olt.filters.xml;
import org.jvnet.olt.filters.xml.xmlconfig.XmlConfig;

/**
 *
 * @author  timf
 */
public class DefaultXmlTagTable implements org.jvnet.olt.parsers.tagged.TagTable {
    
   
    public boolean tagEmpty(String str) {
        return false;
    }
    
    public boolean tagEmpty(String str, String str1) {
        return false;
    }
    
    public boolean tagForcesVerbatimLayout(String str) {
        return false;
    }
    
    public boolean tagForcesVerbatimLayout(String str, String str1) {
        return false;
    }
    
    public boolean tagMayContainPcdata(String str) {
        return false;
    }
    
    public boolean tagMayContainPcdata(String str, String str1) {
        return false;
    }
    
}
