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

package org.jvnet.olt.xliff_back_converter;

/*
 * UnicodeEntityBackConverter.java
 *
 */

import java.io.*;

public class UnicodeEntityBackConverter {
    
    /** Creates a new instance of UnicodeEntityBackConverter */
    public UnicodeEntityBackConverter() {
    }
    
    public static void fix(String filename, UnicodeReverse ur, String charset) throws IOException{
            
            BufferedReader reader = new BufferedReader(new
            	InputStreamReader(new FileInputStream(filename), "UTF-8"));
//            BufferedReader reader = new BufferedReader(new
//            	InputStreamReader(new FileInputStream(filename), charset));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
            	new FileOutputStream(filename+".bak"),charset));
            
            int i=0;
            while ((i=reader.read()) != -1){
            	String tmp = ur.reverse((char)i);
            	if(tmp == null) {
                    wr.write( (char)i );
                }
            	else {
            		wr.write('&');
            		wr.write( tmp,0, tmp.length() );
            		wr.write(';');//??? here need to be confirmed based on the previous convertion
            	}
            }
            
            reader.close(); 
            wr.flush();
            wr.close();
            
            File oldF = new File(filename);
            oldF.delete();
            new File(filename+".bak").renameTo(oldF);        
    }    
}
