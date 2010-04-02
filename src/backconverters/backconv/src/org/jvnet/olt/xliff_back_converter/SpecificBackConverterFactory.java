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
 * SpecificBackConverterFactory.java
 *
 * Created on August 5, 2003, 2:43 PM
 */

package org.jvnet.olt.xliff_back_converter;

/**
 * This interface defines a thing that can make different types
 * of SpecificBackConverters - used during file backconversion to
 * take different actions depending on which specific filetype is
 * being used. 
 *
 * Right now, there's only one implementation - the 
 * SunTrans2SpecificBackConverter which just does provides converters
 * to do plain and simple backconversion to each respective original 
 * file format. 
 *
 * Perhaps you could have a different implementation of this
 * factory that produces nicely html-ized versions of each file format
 * for easy review by linguistic testers online.
 *
 * @author  timf
 */
public interface SpecificBackConverterFactory {
    
    SpecificBackconverterBase getSpecificBackConverter(String datatype);
    
}
