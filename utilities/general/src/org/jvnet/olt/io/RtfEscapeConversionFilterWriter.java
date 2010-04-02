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
 * RtfEscapeConverter.java
 *
 * Created on 25 September 2003, 14:05
 */

package org.jvnet.olt.io;

import java.io.Writer;
import java.io.IOException;

/**
 * This class replaces RTF character escape codes with their respective
 * characters. The codes are describe by the regular expression,
 * <code>\\'[0-9a-fA-F][0-9a-fA-F]</code>. The last two characters of the code are
 * the hexadecimal representation of an 8 bit character. The following state
 * diagram shows how this class works:<br>
 * <img src="doc-files/RtfEscapeConversionFilterWriterStateDiagram1.png" >
 * @author  jc73554
 */
public class RtfEscapeConversionFilterWriter extends java.io.FilterWriter {
    
    private static final int DEFAULT = 0;
    private static final int READ_SLASH = 1;
    private static final int READ_APOS = 2;
    private static final int READ_FIRST_DIGIT = 3;
    
    private StringBuffer m_buffer;
    
    private int m_state;
    
    private StringBuffer m_hexBuf;
    
    /** Creates a new instance of RtfEscapeConverter
     * @param writer A writer to delegate write operations to.
     */
    public RtfEscapeConversionFilterWriter(Writer writer) {
        super(writer);
        m_buffer = new StringBuffer();
        m_hexBuf = new StringBuffer();
        m_state = DEFAULT;
    }
    
    /** Flushes the current entity and then delegates to superclass {@link java.io.FilterWriter}.
     * @throws IOException Thrown by superclass {@link java.io.FilterWriter}
     */
    public void flush() throws IOException {
        flushBuffer();
        out.flush();
    }
    
    /** Flushes the current entity and then delegates to superclass {@link java.io.FilterWriter}.
     * @throws IOException Thrown by superclass {@link java.io.FilterWriter}
     */
    public void close() throws IOException {
        flushBuffer();
        out.close();
    }
    
    /** This method is the main method in the conversion step. It maintains a state
     * machine to recognize RTF character escapes and replaces any escapes recognized
     * to their respective characters.
     * @param c Character to write
     * @throws IOException Thrown if the superclass encounters an I/O error.
     */
    public void write(int c) throws IOException {
        switch(m_state) {
            case READ_SLASH:
                if(c == '\'') {
                    m_buffer.append((char) c);
                    m_state = READ_APOS;
                } else {
                    m_state = DEFAULT;
                    flushBuffer();
                    out.write(c);
                }
                break;
            case READ_APOS:
                if(isHexCharacter((char) c)) {
                    //  Store the character
                    m_hexBuf.append((char) c);
                    m_buffer.append((char) c);
                    m_state = READ_FIRST_DIGIT;
                } else {
                    m_state = DEFAULT;
                    flushBuffer();
                    out.write(c);
                }
                break;
            case READ_FIRST_DIGIT:
                if(isHexCharacter((char) c)) {
                    //  Convert the character and write out
                    try {
                        m_hexBuf.append((char) c);
                        out.write(Integer.parseInt( m_hexBuf.toString(), 16));
                        
                    }
                    catch(NumberFormatException numEx) {
                        flushBuffer();
                        out.write(c);
                    }
                    m_state = DEFAULT;
                } else {
                    m_state = DEFAULT;
                    flushBuffer();
                    out.write(c);
                }
                m_hexBuf.delete(0, m_hexBuf.length());
                break;
            default:
                if(c == '\\') {
                    m_buffer.append((char) c);
                    m_state = READ_SLASH;
                } else {
                    out.write(c);
                }
                break;
        }
    }
    
    /** This method overrides that of {@link java.io.FilterWriter} to convert character
     * entity references to their respective characters.
     * @param cbuf Character buffer
     * @param off Character offset in the buffer to start.
     * @param len Langth of sub buffer to write.
     * @throws IOException Thrown if the write fails.
     */
    public void write(char[] cbuf,int off, int len) throws IOException {
        int i = off;
        while((i < cbuf.length) && ((i - off) < len)) {
            this.write((int) cbuf[i++]);
        }
    }
    
    
    /** This method overrides that of {@link java.io.FilterWriter} to convert character
     * entity references to their respective characters.
     * @param str A string.
     * @param off Character offset in the string to start.
     * @param len Langth of substring to write.
     * @throws IOException Thrown if the write fails.
     */
    public void write(String str,int off, int len) throws IOException {
        try {
            int i = off;
            while((i < str.length()) && ((i - off) < len)) {
                this.write((int) str.charAt(i++));
            }
        }
        catch(StringIndexOutOfBoundsException ex) {
            System.err.println(ex);
        }
    }
    
    /** This method overrides that of {@link java.io.FilterWriter} to convert character
     * entity references to their respective characters.
     * @param str A string
     * @throws IOException Thrown if the write fails.
     */
    public void write(String str) throws IOException {
        this.write(str,0,str.length());
    }
    
    /** This writes out the contents of entity buffer and then empties the buffer.
     * @throws IOException Thrown if the write fails.
     */
    public void flushBuffer() throws IOException {
        out.write(m_buffer.toString());
        m_buffer.delete(0, m_buffer.length());
    }
    
    
    
    /** This method determines if a given character is a hex digit.
     * @param ch The character to test.
     * @return The value true is returned if the character is a hex digit or false otherwise.
     */
    public boolean isHexCharacter(char ch) {
        return (Character.digit(ch, 16) != -1);
    }
    
}
