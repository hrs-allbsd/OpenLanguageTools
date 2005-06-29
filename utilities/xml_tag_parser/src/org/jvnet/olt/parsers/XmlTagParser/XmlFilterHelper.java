
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.XmlTagParser;

import java.util.*;

public class XmlFilterHelper {
    
    public static String stripDoubleQuotes(String quotedString) {      
        if (quotedString.charAt(quotedString.length()-1)=='\"' &&
            quotedString.charAt(0) == '\"'){
            return quotedString.substring(1, (quotedString.length() - 1));
        } else {
            return quotedString;
        }
    }
  
    public static void printMap(Map namespaceMap) {
        System.out.print("NAMESPACES = ");
        Iterator iterator = namespaceMap.values().iterator();
        while(iterator.hasNext()) {
            System.out.print("" + (String) iterator.next());
        }
        System.out.println("");
    }
    
    public static void printStack(Stack namespaceStack) {
        Stack backupStack = new Stack();
        
        System.out.println("Stack Contents");
        int i = 0;
        while(!namespaceStack.isEmpty()) {
            i++;
            System.out.print("" + i + " ");
            Map nameMap = (Map) namespaceStack.pop();
            XmlFilterHelper.printMap(nameMap);
            backupStack.push(nameMap);
        }
    
        System.out.println("namespaceStack size = " + namespaceStack.size());
        
        while(!backupStack.isEmpty()) {
            Map nameMap = (Map) backupStack.pop();
            namespaceStack.push(nameMap);
        }  
        
        System.out.println("namespaceStack size = " + namespaceStack.size());
    
    }
}
