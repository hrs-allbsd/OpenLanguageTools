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
 * Filter.java
 *
 * Created on June 30, 2003, 3:18 PM
 */

package org.jvnet.olt.filters.interfaces;

import org.jvnet.olt.alignment.Segment;
/** It's expected that all file format filters will eventually
 * implement this interface : a global way to define the various
 * file formats that we're dealing with.
 *
 * For the moment, we only define one method - for Klemens'
 * alignment algorithm, but evenutually, we'll include more here.
 *
 * Likewise, for the time being, we've got adaptors in this
 * directory to hide the complexity of the various filters.
 * Finally, there's a factory method that we can call to actually
 * invoke the correct segmenter - again, other classes that use
 * the filters will eventually call this, instead of invoking
 * the individual filters themselves.
 * @author timf
 */
public interface Filter {
    
    /** This method returns an array of Segment objects that Klemens'
     * alignment code can work on. It's assumed each individual implementation
     * will provide a way of taking in a Reader containing the stream to be parsed
     *
     * @param language the language of the file being parsed
     * @return An array of Segment objects
     *
     */    
    public Segment[] getSegments(String language);
       
}
