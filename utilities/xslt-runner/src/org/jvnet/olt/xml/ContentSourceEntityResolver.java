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
 * LoadableResourceEntityResolver.java
 *
 * Created on August 16, 2004, 4:57 PM
 */

package org.jvnet.olt.xml;

import org.jvnet.olt.util.ContentSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author  jc73554
 */
public class ContentSourceEntityResolver implements EntityResolver {
    
    private java.util.HashMap hashEntity;
    
    /** Creates a new instance of LoadableResourceEntityResolver */
    public ContentSourceEntityResolver() {
        hashEntity = new java.util.HashMap();
    }
    
    public InputSource resolveEntity(String publicId, String systemId) {
        ContentSource source = (ContentSource) hashEntity.get(systemId);
        if(source == null) { return null; }
        
        InputSource inputSource = null;
        try {
            inputSource = new InputSource(source.getReader());
        }
        catch(java.io.IOException ioEx) {
            ioEx.printStackTrace();
            return null;
        }
        return inputSource;
    }
    
    public void addEntityReference(String systemId, ContentSource source) {
        hashEntity.put(systemId, source);
    }
    
}
