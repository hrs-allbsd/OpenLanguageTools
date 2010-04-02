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
 * ContentSource.java
 *
 * Created on June 16, 2004, 2:28 PM
 */

package org.jvnet.olt.util;

import java.io.IOException;
import java.io.Reader;

/** This interface represents a readable resource, a source of content. It 
 * encapsulates all the messiness of having to create a java.io.Reader from
 * some source, repeatedly, be that source a file, a resource path in a jar, 
 * a URL, etc.
 * @author  jc73554
 */
public interface ContentSource {
    
    public Reader getReader() throws IOException;
    
}
