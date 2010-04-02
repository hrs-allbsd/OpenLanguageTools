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
 * NullSpecificBackConverter.java
 *
 * Created on August 5, 2003, 3:34 PM
 */

package org.jvnet.olt.xliff_back_converter;

import java.io.File;

/**
 * This class doesn't do anything : it's a placeholder that gets used if
 * there are no particular back converters that need to get run for this
 * file type.
 * @author  timf
 */
public class NullSpecificBackConverter extends  SpecificBackconverterBase{
    
    /** Creates a new instance of NullSpecificBackConverter */
    public NullSpecificBackConverter() {
    }

    public void convert(File file) throws SpecificBackConverterException {
    }

    
}
