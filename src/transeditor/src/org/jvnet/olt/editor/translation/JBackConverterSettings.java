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
 * JBackConverterSettings.java
 *
 * Created on 12 February 2004, 14:10
 */
package org.jvnet.olt.editor.translation;


/** This class stores the state of the JBackConverter dialog settings in between
 * invocations of the back conversion function. It is an instance of the Memento
 * pattern.
 * @author  jc73554
 */
public class JBackConverterSettings {
    private String source;
    private String targetDir;
    private String encoding;
    private boolean createTmx;
    private boolean writeStatus;

    /** Creates a new instance of JBackConverterSettings */
    public JBackConverterSettings(String source, String targetDir, String encoding, boolean createTmx, boolean writeStatus) {
        this.source = source;
        this.targetDir = targetDir;
        this.encoding = encoding;
        this.createTmx = createTmx;
        this.writeStatus = writeStatus;
    }

    /**
     */
    public void setDialogSettings(JBackConverter dialog) {
        //  Set the source directory/file
        dialog.setSourceField(source);

        //  Set the target directory
        dialog.setTargetField(targetDir);

        //  Set the encoding
        dialog.setEncodingSelection(encoding);

        //  Set the create TMX checkbox
        dialog.setCreateTmxCheckbox(createTmx);

        //  Set the write translation status checkbox
        dialog.setWriteStatusCheckbox(writeStatus);
    }
}
