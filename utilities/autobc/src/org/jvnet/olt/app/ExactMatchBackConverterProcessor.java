
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ExactMatchBackConverterProcessor.java
 *
 * Created on August 30, 2004, 5:32 PM
 */

package org.jvnet.olt.app;

import org.jvnet.olt.xsltrun.XsltStylesheet;
import org.jvnet.olt.xsltrun.XsltStylesheetException;
import java.io.IOException;

/** This class fleshes out the functionality of the AbstractXlzFileBackConverterProcessor
 * by providing the stylesheets to be run on the XLZ file prior to backconversion.
 * We are using implementation inheritance here, which is not the most elegant of
 * designs. This should be tidied up, possibly using a 'Replace Delegation with 
 * Delegation' refactoring.
 * In particular, this class applies 100% matches to 'trans-unit' elements prior
 * to doing back conversion.
 * @author  jc73554
 */
public class ExactMatchBackConverterProcessor extends org.jvnet.olt.app.AbstractXlzFileBackConverterProcessor {
    
    /** Creates a new instance of ExactMatchBackConverterProcessor */
    public ExactMatchBackConverterProcessor(String srcDir, String tgtDir, String tempDir, String encoding, boolean generateTmx) throws XsltStylesheetException, IOException {
        super(srcDir, tgtDir, tempDir, encoding, generateTmx);
        
        //  Create stylesheet objects
        XsltStylesheet applyMatchXslt = getStylesheetInstance("xslt/apply_exact_match.xsl");
        XsltStylesheet markTranslatedXslt = getStylesheetInstance("xslt/mark_translated.xsl");

        //  Add the stylesheets
        addStylesheet(applyMatchXslt);
        addStylesheet(markTranslatedXslt);
    }
    
}
