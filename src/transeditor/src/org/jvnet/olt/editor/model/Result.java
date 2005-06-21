/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/**
 * Title:        Alignment editor<p>
 * Description:  part of TMCi editor<p>
 * @version 1.0
 */
package org.jvnet.olt.editor.model;

public class Result {
    public Search search = null;
    public int rowIndex = -1;
    public int sentenceIndex = -1;
    public int position = -1;

    public Result(Search s, int row, int p) {
        search = s;
        rowIndex = row;
        position = p;
    }

    public Result(Search s, int row, int sentence, int p) {
        search = s;
        rowIndex = row;
        sentenceIndex = sentence;
        position = p;
    }

    public Object clone() {
        return new Result((Search)search.clone(), rowIndex, sentenceIndex, position);
    }
}
