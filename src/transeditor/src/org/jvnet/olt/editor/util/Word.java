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
package org.jvnet.olt.editor.util;


/**
    This is a helper class to WordOp - and isn't needed by developers wishing to
    use this package.

    @author Tim Foster
    @see org.jvnet.olt.editor.util.WordOp

*/
public class Word {
    private String word;
    private int position;

    protected Word(String word, int position) {
        this.word = word;
        this.position = position;
    }

    protected String getWord() {
        return this.word;
    }

    protected int getPos() {
        return this.position;
    }

    protected boolean equals(Word word1, Word word2) {
        if (word1.getWord().toLowerCase().equals(word2.getWord().toLowerCase()) && (word1.getPos() == word2.getPos())) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return this.word + "." + this.position;
    }
}
