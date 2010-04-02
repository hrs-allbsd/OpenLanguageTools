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
