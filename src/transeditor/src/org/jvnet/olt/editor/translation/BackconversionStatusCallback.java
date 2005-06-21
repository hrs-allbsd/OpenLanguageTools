/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * BackconversionStatusCallback.java
 *
 * Created on February 4, 2005, 1:50 PM
 */
package org.jvnet.olt.editor.translation;

import java.io.File;


/**
 *
 * @author boris
 */
public interface BackconversionStatusCallback {
    public static final int ERROR_TMX = 0;
    public static final int ERROR_BACKCONV = 1;
    public static final int ERROR_FRAMEFILE = 2;

    public void conversionStart(int numFiles);

    public void fileStarted(File f);

    public void fileSuccess();

    public void fileError(int errorCode);

    public void conversionEnd();

    public void unlock();
}
