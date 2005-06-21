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
