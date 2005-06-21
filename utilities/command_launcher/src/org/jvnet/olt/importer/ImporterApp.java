
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ImporterApp.java
 *
 * Created on 02 October 2002, 15:23
 */

package org.jvnet.olt.importer;

import java.util.Properties;
import java.io.FileInputStream;
import org.jvnet.olt.util.DirectoryCommandRunner;

/**
 *
 * @author  jc73554
 */
public class ImporterApp
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if(args.length != 5)
        {
            System.out.print("\nUsage: importall <inputdir> <config file> <tmx attributes> <list of files> <use index (true|false)>\n\n");
            System.exit(1);
        }
        
        try
        {
            DirectoryCommandRunner launcher = new DirectoryCommandRunner();
            Properties properties = new Properties();
            properties.load(new FileInputStream(args[1]));
            
            AlignImportFileCommand command = null;
            command = new AlignImportFileCommand(properties, args[2], args[4]);
            launcher.launch(args[0], command, args[3]);
        }
        catch(Exception ex)
        {
            System.out.println("Exception thrown.");
            ex.printStackTrace();
        }        
    }   
}
