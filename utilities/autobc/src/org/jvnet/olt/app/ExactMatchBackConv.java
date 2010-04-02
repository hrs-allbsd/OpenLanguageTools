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
 * AutoBackConv.java
 *
 * Created on August 17, 2004, 4:36 PM
 */

package org.jvnet.olt.app;

import org.jvnet.olt.harvester.FileHarvester;
import org.jvnet.olt.io.SelectByExtensionFileFilter;
import org.jvnet.olt.xsltrun.XsltStylesheetException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/** 
 * @author  jc73554
 */
public class ExactMatchBackConv {
    
    /** Creates a new instance of AutoBackConv */
    public ExactMatchBackConv() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length != 4) {
            System.out.println("Wrong number of arguments provided:" + args.length);
            System.out.println(getUsageMessage());
            System.exit(3);
        }
        
        //  Name our command line arguments
        String srcDir = args[0];
        String tgtDir = args[1];
        String tempDir = args[2];        
        String encoding = args[3];        
        
        //  Create an object instance
        ExactMatchBackConv autobc = new ExactMatchBackConv();
        
        //  Run the app.
        try {
            autobc.doBackConvert(srcDir, tgtDir, tempDir, encoding);
            System.exit(0);
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
            System.exit(1);
        }
        catch(XsltStylesheetException xsltEx) {
            xsltEx.printStackTrace();
            System.exit(2);            
        }
    }
    
    public void doBackConvert(String srcDir, String tgtDir, String tempDir, String encoding) throws XsltStylesheetException, IOException {
        doBackConvert(srcDir, tgtDir, tempDir, encoding, false);
    }
    
    public void doBackConvert(String srcDir, String tgtDir, String tempDir, String encoding, boolean generateTmx) throws XsltStylesheetException, IOException {
        //  Get a list of XLZ files
        List xlzList = getXlzFiles(srcDir);
        
        //  Acknowledge that file search has finished
        int numFiles = xlzList.size();
        System.out.println("Found " + numFiles + " XLZ files. Beginning processing...");
     
        //  Create an object to process the files
        ExactMatchBackConverterProcessor processor = new ExactMatchBackConverterProcessor(srcDir, tgtDir, tempDir, encoding, generateTmx);
        
        //  Iterate over the List, processing each file.
        Iterator iterator = xlzList.iterator();
        while(iterator.hasNext()) {
            File file = (File) iterator.next();
            
            //  Delegate file processing to another object
            processor.processXlzFile(file);
        }
    }
    
    protected List getXlzFiles(String srcDir) throws IOException{
        SelectByExtensionFileFilter filter = new SelectByExtensionFileFilter("xlz");
        
        FileHarvester harvester = new FileHarvester();
        
        return harvester.harvestFiles(srcDir, filter);
    }
    
    public static String getUsageMessage() {
        return "Usage: autobc-exact.sh <input dir> <output dir> <temp dir> <encoding>";
    }
    
}
