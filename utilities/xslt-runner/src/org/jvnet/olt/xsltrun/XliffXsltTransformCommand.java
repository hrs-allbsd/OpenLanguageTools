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
 * XliffXsltTransformCommand.java
 *
 * Created on August 13, 2004, 3:15 PM
 */

package org.jvnet.olt.xsltrun;

import org.jvnet.olt.io.Pipe;
import org.jvnet.olt.util.Command;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** This command runs XSLT transforms. Multiple transforms can be executed on a
 * given stream.  
 * @author  jc73554
 */
public class XliffXsltTransformCommand implements Command {
    
    private List stylesheetList;
    private org.xml.sax.EntityResolver resolver;
    
    /** Creates a new instance of XliffXsltTransformCommand */
    public XliffXsltTransformCommand(org.xml.sax.EntityResolver resolver) {
        this.resolver = resolver;
        stylesheetList = new LinkedList();
    }
    
    /** Execute previously added XSLT transform scripts on the input and write 
     * it to the output. If no XSLT scripts have been run then the method just 
     * returns.
     */
    public void execute(Reader source, Writer target) throws IOException {
        List threadList = new LinkedList();
        
        //  Determine the number of stylesheets we have to run.
        XsltStylesheet[] array = (XsltStylesheet[]) stylesheetList.toArray(new XsltStylesheet[0]);
        
        int numSheets = array.length;
        
        if(numSheets < 1) {
            //  Maintenance note: perhaps throw an exception here
            System.err.println("There are no stylesheets to process.");
            return;
        }
        
        //  Loop over the stylesheets
        Pipe pipe = null;
        for(int i = 0; i < numSheets; i++) {
            boolean first = (i == 0);
            boolean last = (i == (numSheets - 1) );
            
            //  Create a runable to run each one
            XsltProcessorRunable xsltProc = new XsltProcessorRunable(array[i], resolver);
            
            //  Set the reader and writer for the stylesheet runnable
            if(first) {
                xsltProc.setReader(source);
            } else {
                //  Perhaps test to ensure the pipe is not null here!
                xsltProc.setReader(pipe.getReader());
            }
            
            if(last) {
                xsltProc.setWriter(target);
            } else {
                pipe = new Pipe();
                xsltProc.setWriter(pipe.getWriter());
            }
            
            //  Add the runable to a thread
            Thread thread = new Thread(xsltProc);
            threadList.add(thread);
        }
        
        //  Start all the threads
        startThreads(threadList);
        
        //  Wait for the threads to finish
        //  Maintenance note: this should probably be replace by Thread.join()
        //  mechanisms.
        while(threadsRunning(threadList)) {
            //  Yield execution or sleep()
            Thread currThread = Thread.currentThread();
            try {
                currThread.sleep(1000);
            }
            catch(InterruptedException intEx) {
                System.err.println("Main thread interrutped!");
                intEx.printStackTrace();
            }
        }
    }
    
    public void addStylesheet(XsltStylesheet ss) {
        stylesheetList.add(ss);
    }
    

    
    
    /** Poll to see if threads are running.
     */
    protected boolean threadsRunning(List threadList) {
        Iterator iterator = threadList.iterator();
        while(iterator.hasNext()) {
            Thread thread = (Thread) iterator.next();
            if(thread.isAlive()) { return true; }
        }
        return false;
    }
    
    protected void startThreads(List threadList) {
        //  Maintenance note:
        //  This may not work. The threads may need to be started in reverse
        //  order.
        Iterator iterator = threadList.iterator();
        while(iterator.hasNext()) {
            Thread thread = (Thread) iterator.next();
            thread.start();
        }
    }
}
