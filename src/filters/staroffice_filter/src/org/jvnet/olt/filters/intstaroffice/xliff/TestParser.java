
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TestParser.java
 *
 * Created on July 24, 2004, 3:43 PM
 */

package org.jvnet.olt.filters.intstaroffice.xliff;
import org.xml.sax.InputSource;
import java.io.FileReader;
/**
 *
 * @author  timf
 */
public class TestParser {
    
    /** Creates a new instance of TestParser */
    public TestParser(String filename) {
        
        try {
            
            XliffHandler handler = new XliffHandlerImpl();
            XliffParser parser = new XliffParser(handler, new NullEntityResolver());
            parser.parse(new InputSource(new FileReader(filename)));
            if (handler.isInternalStarOfficeXLIFF()){
                System.out.println("That XLIFF file came from the StarOffice Database");
            } else {
                System.out.println("That XML file did not come from the StarOffice Database");
            }
        } catch (java.io.IOException e){
            System.out.println("IOException thrown "+ e.getMessage());
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e){
            System.out.println("SAXException thrown "+ e.getMessage());
            e.printStackTrace();
        } catch (javax.xml.parsers.ParserConfigurationException e){
            System.out.println("ParserConfigurationException thrown "+ e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Usage: TestParser <filename>");
            System.exit(1);
        }
        TestParser test = new TestParser(args[0]);
    }
    
}
