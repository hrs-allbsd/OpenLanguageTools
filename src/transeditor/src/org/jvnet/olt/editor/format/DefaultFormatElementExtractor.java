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
