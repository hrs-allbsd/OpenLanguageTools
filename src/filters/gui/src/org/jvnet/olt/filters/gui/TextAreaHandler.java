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

import java.util.MissingResourceException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/** Log handler that logs into a textarea.
 *
 * @author boris
 */
public class TextAreaHandler extends Handler{
    /**
     * target text area
     */
    private JTextArea area;
    private static final java.util.ResourceBundle xliffFilterGUIMessages = java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages");
    
    /**
     * default formatter
     */
    private SimpleFormatter formatter = new SimpleFormatter();
    
    class SimpleFormatter extends Formatter{
        StringBuffer sb = new StringBuffer(200);
        
        public String format(java.util.logging.LogRecord record) {
            return fmt(record).toString();
        }
        
        /**
         * internal format implementation
         */
        private StringBuffer fmt(LogRecord rec){
            sb.delete(0,sb.length());
            try {
                sb.append(xliffFilterGUIMessages.getString(rec.getLevel().toString()));
            } catch (MissingResourceException e){
                // it's not too severe if we have a Level that's not localised
                System.out.println("unlocalised message "+rec.getLevel().toString());
                sb.append(rec.getLevel().toString());
            }
            sb.append(": ");
            sb.append(rec.getMessage());
            
            return sb;
        }
        
    }
    
    /**
     * Creates a new instance of TextAreaLogger
     * @param area Text are to receive messages
     */
    public TextAreaHandler(JTextArea area) {
        this.area = area;
    }
    
    /**
     * publish the logrecrod
     * @param record log record to publish
     */
    public void publish(final java.util.logging.LogRecord record) {
        if(record == null)
            return;
        
        if(area != null ){
            final String msg = formatter.format(record)+"\n";
            
            
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    area.append(msg);
                    area.setCaretPosition(area.getText().length());
                }
                
                
            });
        }
    }
    
    /**
     * create default formatter
     * @return returns TextAreaHandler.SimpleFormatter instance
     */
    public java.util.logging.Formatter getFormatter() {
        return formatter;
    }
    
    /**
     * method from parent.
     *
     * Does nothing
     * @throws java.lang.SecurityException never thrown.
     */
    public void close() throws SecurityException {
    }
    
    /**
     * method from parent.
     *
     * Does nothing.
     */
    public void flush() {
    }
    
    
    
}
