
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xml.xmlconfig; 

import java.io.File;
import org.xml.sax.InputSource;
import java.util.logging.*;
import java.util.List;
import java.util.Iterator;

/**
 *
 * @author  bpk1
 */
public class XmlLookupMain {
    
    public static void main(String[] args) {
        
                
        if (args.length != 1) {
            System.out.println("Usage: <xml-file>");
            System.exit(1);
        }
        
        InputSource inputSource = null;

        try {
            inputSource = new InputSource(args[0]);
        } catch (NullPointerException e) {
            System.exit(1);
        }
        
        try {
            
            
            XmlIdentifier xmlIdentifier = new XmlIdentifier();
            
            XmlLookupHandler xmlLookupHandler = new XmlLookupHandlerImpl(xmlIdentifier);
                    
            XmlLookupParser xmlLookupParser = 
                new XmlLookupParser(xmlLookupHandler, new XmlLookupResolver(
                xmlIdentifier, "/dev/null"),"/dev/null");
        
            xmlLookupParser.parse(inputSource);
        
        } catch(org.xml.sax.SAXException ex) {
            System.exit(1);
        }  catch(javax.xml.parsers.ParserConfigurationException ex) {
            System.exit(1);
        } catch(java.io.IOException ex) {
            System.exit(1);
        }
        
    }
    
}
