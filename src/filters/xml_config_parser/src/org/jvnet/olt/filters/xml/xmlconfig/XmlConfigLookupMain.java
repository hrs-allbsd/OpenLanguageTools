
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlConfigLookupMain.java
 *
 * Created on 06 February 2003, 00:15
 */

package org.jvnet.olt.filters.xml.xmlconfig; 

import java.io.File;
import org.xml.sax.InputSource;
import java.util.logging.*;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 *
 * @author  bpk1
 */
public class XmlConfigLookupMain {
    
    private static Logger logger =
        Logger.getLogger("org.jvnet.olt.filters.xml.xmlconfig");
    
    /** Creates a new instance of XmlConfigMain */
    public XmlConfigLookupMain() {
        logger.log(Level.INFO, "Main1");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        logger.log(Level.INFO, "Main");
        
        XmlConfigLookupMain configMain = new XmlConfigLookupMain();
        
        if (args.length < 4) {
            System.out.println("Usage: <xml-config-dtd> <xml-config-store> <xml-file> <xml-config-file>");
            System.exit(1);
        }
        
        /* XmlConfigManager configManager = new XmlConfigManager(); */
        try {  
        XmlConfigManager.init(args[0], args[1]);
        } catch (java.io.IOException e){
            System.out.println("IOException creating XmlConfigManager " + e.getMessage());
        }
        for(int i = 3; i < args.length; i++) {
            
            File configFile = new File(args[i]);            
            try {                
                XmlConfigManager.processConfig(configFile);
            } catch(ProcessXmlConfigException ex) {
                System.out.println(ex.getMessage());
            }
            
        }
        
        System.out.println("XmlConfigs: \n" + XmlConfigManager.returnString());
        
        InputSource inputSource = null;

        try {
            inputSource = new InputSource(args[2]);
        } catch (NullPointerException e) {
            System.exit(1);
        }
        
        try {
            
            XmlIdentifier xmlIdentifier = new XmlIdentifier();
                        
            XmlLookupHandler xmlLookupHandler = new XmlLookupHandlerImpl(xmlIdentifier);
                    
            XmlLookupParser xmlLookupParser = 
                new XmlLookupParser(xmlLookupHandler, new XmlLookupResolver(xmlIdentifier,"/dev/null"),"/dev/null");
        
            xmlLookupParser.parse(inputSource);
            
            System.out.println("XmlIdentifier = " + xmlIdentifier.toString());
            
            try {
                Collection configCollection = XmlConfigManager.getXmlConfigCollection(xmlIdentifier, Logger.global);
                
                Iterator configIterator = configCollection.iterator();
                
                System.out.println("The Configs");
                
                int count = 1;
                
                while(configIterator.hasNext()) {                    
                    XmlConfig config =  (XmlConfig) configIterator.next();
                    System.out.println("Config " + count + " FileType = " + config.getFileType());
                    count++;                   
                }
                
            } catch(NullPointerException ex) {
                System.out.println("No Config files found");
            }
            
        
        } catch(org.xml.sax.SAXException ex) {
            System.exit(1);
        }  catch(javax.xml.parsers.ParserConfigurationException ex) {
            System.exit(1);
        } catch(java.io.IOException ex) {
            System.exit(1);
        }
            
        
    }
    
   
    
}
