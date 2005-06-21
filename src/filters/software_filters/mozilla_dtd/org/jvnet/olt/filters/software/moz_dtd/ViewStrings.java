
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * ViewStrings.java
 *
 * Created on May 4, 2005, 2:08 PM
 *
 * 
 */

package org.jvnet.olt.filters.software.moz_dtd;
import java.io.*;

/**
 * Simple class to view the contents of a mozilla message file (DTD). It uses
 * the output of the ST2MessageArrayFactoryVisitor to print all of the key/value
 * pairs in the message file.
 * @author timf
 */
public class ViewStrings {
    
    /**
     * Creates a new instance of ViewStrings
     * @param filename The name of the file we want to parse. Assumes UTF-8 encoding.
     */
    public ViewStrings(String filename) {
        try{
            Reader reader = new InputStreamReader(new FileInputStream(filename),"UTF-8");
            MozDTDFileParserFacade facade = new MozDTDFileParserFacade();            
            String[][] arr = facade.getSunTrans2MessageStringArr(reader,"");
            for (int i=0; i<arr.length;i++){
                System.out.println("key = "+arr[i][0]);
                System.out.println("val = "+arr[i][2]);
            }
            
        }catch (Exception e){ // bad exception handling here, but it's only test code
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println("Usage: ViewStrings <mozilla dtd file>");
            System.exit(1);
        }
        ViewStrings view = new ViewStrings(args[0]);  
    }
}
