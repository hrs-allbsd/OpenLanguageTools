/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * DefaultFormatElementExtractor.java
 *
 * Created on 13 January 2004, 11:16
 */
package org.jvnet.olt.editor.format;

import org.jvnet.olt.editor.model.PivotBaseElement;


/**
 *
 * @author  jc73554
 */
public class DefaultFormatElementExtractor implements FormatElementExtractor {
    /** Creates a new instance of DefaultFormatElementExtractor */
    public DefaultFormatElementExtractor() {
    }

    public PivotBaseElement[] extractBaseElements(String text) {
        PivotBaseElement[] array = new PivotBaseElement[1];
        array[0] = new PivotBaseElement(text, PivotBaseElement.TEXT);

        return array;
    }

    public String extractText(String formattedText) {
        return formattedText;
    }

    public String markupFormatForDisplay(String formattedText) {
        return formattedText;
    }

    public void setVariableManager(org.jvnet.olt.format.GlobalVariableManager gvm) {
        //  Do nothing.
    }
}
