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
 * WavyDashConverter.java
 *
 * Created on 15 April 2004, 14:19
 */

package org.jvnet.olt.xliff_back_converter.format.sgml;

import java.io.*;

/** This class opens a given file, and converts any occurrance of '\uFF5E' to
 * '\u301C'. It is not the most efficient implementation of this functionality:
 * a FilterWriter would have been better. However it will do for now until this
 * functionality is refactored.
 * @author  jc73554
 */
public class WavyDashConverter {
    
    /** Creates a new instance of WavyDashConverter */
    public WavyDashConverter() {
    }
    
   /** This method does the conversion.
    */
    public void convertFile(String filename, String encoding) throws IOException, UnsupportedEncodingException {
        //  Open input stream
        File inputFile = new File(filename);
        InputStream  istream = new FileInputStream(inputFile);
        Reader reader = new BufferedReader(new InputStreamReader(istream, encoding));
        
        //  Create an output file
        File outputFile = new File(filename + ".tmp");
        
        //  Open an output stream
        OutputStream ostream = new FileOutputStream(outputFile);
        Writer writer = new BufferedWriter(new OutputStreamWriter(ostream,encoding));
        
        // Do conversion
        convert(reader, writer);
        
        reader.close();
        writer.flush();
        writer.close();
    
        //  Delete input file
        inputFile.delete();
        
        //  Move output file
        outputFile.renameTo(inputFile);
    }
    
    /** This method does the actual conversion given input ant output streams.
     * Note: it exists mostly to support a "design for test" methodology.
     * @param reader A reader on the stream containing the text to be converted.
     * @param writer A writer to output the converted text to.
     */
    public void convert(Reader reader, Writer writer) throws IOException {
        //  Read, convert if necessary, and write.
        int ch = 0;
        while((ch = reader.read()) != -1) {
            if(ch == ((int)'\uFF5E') ) {
                writer.write('\u301C');
            } else {
                writer.write(ch);
            }
        }        
    }
    
}
