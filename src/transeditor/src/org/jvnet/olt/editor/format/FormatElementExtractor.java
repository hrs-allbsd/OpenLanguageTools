/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * FormatExtractor.java
 *
 * Created on 06 January 2004, 15:41
 */
package org.jvnet.olt.editor.format;

import org.jvnet.olt.editor.model.PivotBaseElement;
import org.jvnet.olt.format.GlobalVariableManager;


/**
 *
 * @author  jc73554
 */
public interface FormatElementExtractor {
    public PivotBaseElement[] extractBaseElements(String text);

    public String extractText(String formattedText);

    public String markupFormatForDisplay(String formattedText);

    public void setVariableManager(org.jvnet.olt.format.GlobalVariableManager gvm);
}
