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
