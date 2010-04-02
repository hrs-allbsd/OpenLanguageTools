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
 * XliffEntityResolver.java
 *
 * Created on 08 August 2003, 17:19
 */

package org.jvnet.olt.xlifftools;

import java.io.File;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.InputStream;

/**
 *
 * @author  jc73554
 */
public class XliffEntityResolver implements EntityResolver
{
    
    /** Creates a new instance of XliffEntityResolver */
    public XliffEntityResolver()
    {
    }
    
    public InputSource resolveEntity(String publicId, String systemId)
    {
        if(systemId.endsWith("xliff.dtd"))
        {
            InputStream istream = this.getClass().getResourceAsStream("/dtd/xliff.dtd");
            if(istream != null)
            {
                InputSource source = new InputSource(istream);
                return source;
            }
            else
            {
                System.err.println("DTD InputStream was null!");
                return null;
            }
        }
        else
        {
            //  We only resolve XLIFF SYSTEM ids!
            return null;
        }

    }
    
}
