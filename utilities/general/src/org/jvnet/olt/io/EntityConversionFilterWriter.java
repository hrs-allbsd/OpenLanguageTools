
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * EntityConversionFilterWriter.java
 *
 * Created on 22 September 2003, 18:08
 */

package org.jvnet.olt.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/** This class is a FilterWriter that replaces known character entity references with
 * their respective characters. Entities are added to the list of known entities
 * using the {@link #addEntity } method. The state transition diagram looks like:<br>
 * <img src="doc-files/EntityConvertingWriterStateDiagram1.png">
 * @author jc73554
 */
public abstract class EntityConversionFilterWriter extends FilterWriter {
    
    /** This is a named constant to indicate the writer is in pass through state. */    
    private static final int PASS_THROUGH_STATE   = 0;
    
    /** This is a named constant to indicate that the writer is in an entity recognizing
     * state.
     */    
    private static final int READING_ENTITY_STATE = 1;
    
    private HashMap m_entityMap;
    private int m_state = PASS_THROUGH_STATE;
    protected StringBuffer m_buffer;
    
    /** Creates a new instance of EntityConversionFilterWriter
     * @param writer A writer. Selected entities are replaced with their character equivalents.
     */
    public EntityConversionFilterWriter(Writer writer) {
        super(writer);
        m_entityMap = new HashMap();
        m_buffer = new StringBuffer();
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
    
    /** This method overrides that of {@link java.io.FilterWriter} to convert character
     * entity references to their respective characters.
     * @param c A character.
     * @throws IOException Thrown if the write fails.
     */    
    public void write(int c) throws IOException {
        boolean boolProcessingEntity = (m_state == READING_ENTITY_STATE);
        if(boolProcessingEntity) {
            if(c == '&') {
                flushBuffer();
                m_state = READING_ENTITY_STATE;
                m_buffer.append((char) c);
            } else if(c == ';') {
                m_buffer.append((char) c);
                processEntity();
                m_state = PASS_THROUGH_STATE;
            } else if(isEntityChar(c)) {
                m_buffer.append((char) c);
            } else {
                m_buffer.append((char) c);
                flushBuffer();
                m_state = PASS_THROUGH_STATE;
            }
        } else {  //  State test alternate branch
            if(c == '&') {
                m_state = READING_ENTITY_STATE;
                m_buffer.append((char) c);
            } else {
                out.write(c);
            }
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
    
    /** This method processes an entity that has been recognized. If the entity is a
     * known one then, it is replaced by its associated character. If it is not, then
     * handleUnknownEntity is called.
     * @throws IOException Thrown if the character associated with an entity could not be written or if an
     * unknown entity could not be handled.
     */    
    public void processEntity() throws IOException {
        String entityRef = m_buffer.toString();
        Character character = (Character) m_entityMap.get(entityRef);
        if(character != null) {
            out.write(character.charValue());
            m_buffer.delete(0, m_buffer.length());
        } else {
            handleUnknownEntity();
        }
    }
    
    /** This method tests whether a particular entity is a valid character that can
     * appear in an entity reference. The valid characters are letters, digits, 
     * '-', '_', and '.' as defined in the XML specification.
     * @param c The character to test
     * @return This method returns true if a character can appear in an entity reference and
     * false otherwise.
     */    
    public boolean isEntityChar(int c) {
        boolean boolIsEntityChar = ( Character.isLetterOrDigit((char) c) ||
                c == '_' ||
                c == '-' ||
                c == '.');
        return boolIsEntityChar;
    }
    
    /** This method adds an entity to the list of known entities and associates a
     * character with it.
     * @param entityRef The entity reference to add to the list of recognized entities. The reference
     * should include opening '&' and closing ';'.
     * @param ch The character to associate with the entity reference.
     */    
    protected void addEntity(final java.lang.String entityRef, final char ch) {
        m_entityMap.put(entityRef, new Character(ch));
    }
    
    /** This method is a template method that allows for subclasses
     * to selectively override the classes behaviour when an
     * unknown entity is recognized. Default behaviour is to flush the entity
     * buffer.
     * @throws IOException Whenever the case arises that an unknown entity is an error
     */    
    public void handleUnknownEntity() throws java.io.IOException {
        flushBuffer();
    }
    
}
