
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.minitm;

public class SGMLFormatRemovingStrategy
implements FormatRemovingStrategy
{
    public String removeFormatting(String string) throws MiniTMException
    {
        StringCleaningAdapter formatRemover =
            new StringCleaningAdapter(string);
        
        return formatRemover.getPlainText();
    }
    
    public java.util.List extractFormatting(String string) throws MiniTMException
    {
        StringCleaningAdapter formatRemover =
            new StringCleaningAdapter(string);
        
        return formatRemover.getMarkup();      
    }
    
}
