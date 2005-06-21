
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
    
    SpecificBackConverter getSpecificBackConverter(String datatype);
    
}
