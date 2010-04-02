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
 * DefaultSuggestion.java
 *
 * Created on November 16, 2006, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

/**
 *
 * @author boris
 */
public class DefaultSuggestion implements Suggestion {
    final private String word;
    
    /** Creates a new instance of DefaultSuggestion */
    public DefaultSuggestion(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public String toString() {
        return word;
    }
    
    
}
