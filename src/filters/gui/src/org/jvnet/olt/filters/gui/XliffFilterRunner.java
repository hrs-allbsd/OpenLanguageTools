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

package org.jvnet.olt.filters.gui;

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A simple class that implements Runnable to kick off XLIFF conversion for
 * the file passed in the constructor. 
 * @author timf
 */
public class XliffFilterRunner implements Runnable {
    
    private File fileToConvert = null;
    private XliffFilterFacade filterFacade = null;
    private Logger logger = null;
    private Map attributes = null;
    private XliffFilterGUI gui = null;

    private static final java.util.ResourceBundle xliffFilterGUIMessages = java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages");
    
    /**
     * Does XLIFF conversion of the input job - uses XliffFilterFacade to do
     * the work.
     * @param fileToConvert the file we want to convert to XLIFF
     * @param attributes the attributes we want to pass to the conversion job
     * @param logger a means to log debug and other messages
     * @param gui used to pop up a completion dialog box
     */
    public XliffFilterRunner(File fileToConvert, Map attributes, Logger logger, XliffFilterGUI gui) {
        this.fileToConvert = fileToConvert;
        this.filterFacade = new XliffFilterFacade(attributes, logger);
        this.attributes = attributes;
        this.logger = logger;
        this.gui = gui;
    }
    
    public void run(){
        try {
            this.filterFacade.convert(fileToConvert, this.attributes, this.logger);
            //this.gui.showCompletionDialog(fileToConvert, true);
        } catch (XliffFilterFacadeException e){
            this.logger.log(Level.WARNING,MessageFormat.format(
                    xliffFilterGUIMessages.getString("Problem_converting_file_o"),
                    new Object[] {fileToConvert.getAbsolutePath(), e.getMessage()}));
            //this.gui.showCompletionDialog(fileToConvert, false, e.getMessage());
        }
    }

}
