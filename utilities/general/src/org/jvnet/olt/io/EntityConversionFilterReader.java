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
 * EntityConversionFilterReader.java
 *
 * Created on December 15, 2003, 12:16 PM
 */

package org.jvnet.olt.io;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the reverse of the EntityConversionFilterWriter, that is, it reads
 * plaintext strings, and converts any unusual/interesting characters (defined by the
 * entityMap) into their corresponding xml/sgml entity representations.
 *
 * This code does the same thing as the HtmlEscapeFilterReader, the only difference is
 * that it can take a map to allow the user to configure which characters get mapped
 * to which entities. As a result, it's slightly slower than the HtmlEscapeFilterReader
 * but the fact that it's extensible is probably good. If performance is key, then this
 * is worth considering.
 *
 * @author  timf
 */
public class EntityConversionFilterReader extends java.io.FilterReader {
    private Reader reader;
    
    private static final int NORMAL = 0;
    private static final int READING_ENTITY_CHAR = 1;
    private static final int LAST_CHAR_IN_BUFFER = 2;
    
    // this is the state we're in.
    private int state = NORMAL;
    
    // this allows us to save the state when calling a .mark() method
    // on the reader. (Hmm, actually don't think we care about this anymore)
    private int savedMarkState = NORMAL;
    
    // an index into the StringBuffer containing the entity.
    private int entityBuffIndex = 0;
    private StringBuffer entityBuff = new StringBuffer();
    
    private Map entityMap = new HashMap();
    private boolean boolEndOfStream = false;
    
    /** Creates a new instance of EntityConversionFilterReader.
     * The default EntityConversionFilterReader converts ampersand,
     * greater-than and less-than characters to their xml equivalent.
     * You can use the set entity map to add to these, but at the moment
     * there's no way to remove the default behaviour - it's always
     * added for safety so we can't write invalid xml.
     */
    public EntityConversionFilterReader(Reader in) {
        super(in);
        this.reader = in;
        this.entityMap = new HashMap();
        entityMap.put(new Character('&'),"&amp;");
        entityMap.put(new Character('<'), "&lt;");
        entityMap.put(new Character('>'), "&gt;");
    }
    
    /**
     * This adds the parameter entityMap to this readers
     * substitution list. It also then proceeds to add
     * &amp; &lt; and &gt; in case the user has tried to
     * override these
     */
    public void setEntityMap(Map entityMap){
        this.entityMap.clear();
        this.entityMap.putAll(entityMap);
        this.entityMap.put(new Character('&'),"&amp;");
        this.entityMap.put(new Character('<'), "&lt;");
        this.entityMap.put(new Character('>'), "&gt;");
    }
    
    public boolean markSupported() {
        return this.reader.markSupported();
    }
    
    public int read() throws java.io.IOException {
        int c=-1;
        switch (state){
            
            case NORMAL: // where we're reading normal character values
                c = this.reader.read();
                if (c == -1){
                    return c;
                }
                Character mychar = new Character((char)c);
                String entity = (String)entityMap.get(mychar);
                if (entity != null){
                    
                    this.entityBuff = new StringBuffer(entity);
                    if (entityBuff.length() == 0){
                        throw new java.io.IOException("0 length substitution found for " + c+" in EntityConversionReader.");
                    }
                    c = (int)entityBuff.charAt(0); // read the 1st character in the substitution
                    
                    if (entityBuff.length() == 1){ // for 1 char subst, go back to normal state
                        state = NORMAL;
                    } else { // read the rest of the entity characters
                        entityBuffIndex++;
                        state = READING_ENTITY_CHAR;
                    }
                }
                break;
                
            case READING_ENTITY_CHAR:
                c = (int)entityBuff.charAt(entityBuffIndex);
                entityBuffIndex++;
                if (entityBuffIndex == entityBuff.length() -1){
                    state = LAST_CHAR_IN_BUFFER;
                }
                break;
                
            case LAST_CHAR_IN_BUFFER:
                c = (int)entityBuff.charAt(entityBuffIndex);
                entityBuffIndex = 0;
                entityBuff = new StringBuffer();
                state = NORMAL;
                break;
            default:
                throw new java.io.IOException("Unknown state entered in EntityConversionFilterReader - please report this as a bug !");
                
        }
        return c;
    }
    
    // this implementation taken from ControlDEscapeFilterReader ..
    public int read(char[] cbuf) throws java.io.IOException {
        int pos = 0;
        int cval = 0;
        
        if(boolEndOfStream) { return -1; }
        
        while(pos < cbuf.length) {
            cval = this.read();
            cbuf[pos++] = (char) cval;
            if(cval == -1) {
                boolEndOfStream = true;
                return pos - 1;     //  Chop off the EOF
            }
        }
        return pos;
    }
    
    // this implementation taken from ControlDEscapeFilterReader ..
    public int read(char[] cbuf,int off, int len) throws java.io.IOException {
        int pos = off;
        int cval = 0;
        
        if(boolEndOfStream) { return -1; }
        
        while((pos < cbuf.length) && ((pos - off) < len)) {
            cval = this.read();
            cbuf[pos++] = (char) cval;
            if(cval == -1) {
                boolEndOfStream = true;
                return (pos - off) - 1;  //  Chop off the EOF
            }
        }
        return (pos - off);
    }
    
    public boolean ready() throws java.io.IOException{
        return this.reader.ready();
    }
    
    public void reset() throws java.io.IOException{
        if (this.reader.markSupported()){
            state = savedMarkState;
        }
        this.reader.reset();
    }
    
    /**
     * Note that skipping here, skips the underlying
     * reader - if we're in the middle of converting a
     * character like '&amp;' to "&amp;amp;" then subsequent
     * calls to read will continue to perform the conversion of that
     * character until it's finished. After the final read
     * call, the underlying stream will return the character
     * we've skipped to.
     */
    public long skip(long param) throws java.io.IOException{
        return this.reader.skip(param);
    }
    
    public void mark(int readAheadLimit) throws java.io.IOException{
        this.reader.mark(readAheadLimit);
        this.savedMarkState = this.state;
    }
    
}
