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
    @see com.sun.tmc.stringdifference.WordOp

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
