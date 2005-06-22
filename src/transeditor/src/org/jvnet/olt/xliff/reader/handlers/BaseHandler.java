/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * BaseHandler.java
 *
 * Created on April 18, 2005, 5:07 PM
 *
 */
package org.jvnet.olt.xliff.reader.handlers;

import java.util.logging.Logger;

import org.jvnet.olt.xliff.handlers.Handler;


/**
 *
 * @author boris
 */
abstract public class BaseHandler extends Handler {
    protected final Context ctx;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    /** Creates a new instance of BaseHandler */
    public BaseHandler(Context ctx) {
        this.ctx = ctx;
    }
}
