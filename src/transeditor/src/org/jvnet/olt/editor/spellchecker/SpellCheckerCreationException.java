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
 * SpellCheckerCreationException.java
 *
 * Created on November 30, 2006, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

/**
 *
 * @author boris
 */
public class SpellCheckerCreationException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SpellCheckerCreationException</code> without detail message.
     */
    public SpellCheckerCreationException() {
    }
    
    
    /**
     * Constructs an instance of <code>SpellCheckerCreationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SpellCheckerCreationException(String msg) {
        super(msg);
    }

    public SpellCheckerCreationException(Throwable t) {
        super(t);
    }
}
