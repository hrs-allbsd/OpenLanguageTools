/*
 * FormattingException.java
 *
 * Created on December 15, 2005, 10:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.model;

import org.jvnet.olt.editor.util.NestableException;

/**
 *
 * @author boris
 */
public class FormattingException extends NestableException {
    private String sentence;
    private int segmentNo;
    
    /**
     * Creates a new instance of <code>FormattingException</code> without detail message.
     */
    public FormattingException() {
        super();
    }
    
    
    /**
     * Constructs an instance of <code>FormattingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FormattingException(String msg) {
        super(msg);
    }
    
    public FormattingException(String message,Throwable t){
        super(message,t);
    }

    public FormattingException(Throwable t){
        super(t);
    }
    
    public FormattingException(String message,Throwable t,String sentence,int segmentNo){
        super(message,t);
        this.segmentNo = segmentNo;
        this.sentence = sentence;
        
    }
    
    public String getSentence(){
        return sentence;
    }
    
    public int getSegmentNumber(){
        return segmentNo;
    }
}
