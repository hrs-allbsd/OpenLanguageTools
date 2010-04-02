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
