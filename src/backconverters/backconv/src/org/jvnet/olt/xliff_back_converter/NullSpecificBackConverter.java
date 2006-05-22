
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
