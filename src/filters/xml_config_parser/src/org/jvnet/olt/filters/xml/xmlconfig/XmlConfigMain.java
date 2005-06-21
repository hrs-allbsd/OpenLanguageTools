
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlConfigMain.java
 *
 * Created on 06 February 2003, 00:15
 */

package org.jvnet.olt.filters.xml.xmlconfig;

import java.io.File;
import org.xml.sax.InputSource;
import java.util.logging.*;

/**
 *
 * @author  bpk1
 */
public class XmlConfigMain {
    
    private static Logger logger =
    Logger.getLogger("org.jvnet.olt.filters.xml.xmlconfig");
    
    /** Creates a new instance of XmlConfigMain */
    public XmlConfigMain() {
        logger.log(Level.INFO, "Main1");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("system: Main");
        logger.log(Level.INFO, "Main");
        
        XmlConfigMain configMain = new XmlConfigMain();
        
        if (args.length < 3) {
            System.out.println("Usage: <xml-config-dtd> <xml-config-store> <xml-config-directory>");
            System.exit(1);
        }
        try {
            XmlConfigManager.init(args[0], args[1]);
        }catch (java.io.IOException e){
            System.out.println("IOException thrown when initialising the XML Config manager "+ e.getMessage());
            System.exit(1);
        }
        
        for(int i = 2; i < args.length; i++) {
            
            File configFile = new File(args[i]);
            try {
                XmlConfigManager.processConfig(configFile);
            } catch(ProcessXmlConfigException ex) {
                System.out.println(ex.getMessage());
            }
            
        }
        
        System.out.println("XmlConfigs: \n" + XmlConfigManager.returnString());
        
    }
    
}
