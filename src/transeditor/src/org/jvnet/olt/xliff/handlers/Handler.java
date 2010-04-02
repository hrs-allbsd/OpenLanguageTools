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
 * Handler.java
 *
 * Created on April 7, 2005, 5:32 PM
 *
 */
package org.jvnet.olt.xliff.handlers;

import java.util.Stack;

import org.jvnet.olt.xliff.ReaderException;


/**
 *
 * @author boris
 */
abstract public class Handler {
    private Stack elementStack;
    private Stack handlerStack;

    /** Creates a new instance of Handler */
    public Handler() {
    }

    abstract public void dispatch(Element element, boolean start) throws ReaderException;

    public void dispatchChars(Element element, char[] chars, int start, int length) throws ReaderException {
        //do nothing
    }

    public void dispatchIgnorableChars(Element element, char[] chars, int start, int length) throws ReaderException {
        //do nothing
    }

    public void setElementStack(Stack elStack) {
        this.elementStack = elStack;
    }

    protected Stack getElementStack() {
        return elementStack;
    }

    public void setHandlerStack(Stack hnStack) {
        this.handlerStack = hnStack;
    }

    protected Stack getHandlerStack() {
        return handlerStack;
    }

    public boolean handleSubElements() {
        return false;
    }
}
