
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * DropFileExtractor.java
 *
 * Created on May 22, 2005, 11:35 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.filters.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Extracts dropped list of file names from drop event.
 * @author boris
 */
public class DropFileExtractor {
    private static final Logger logger = Logger.getLogger(DropFileExtractor.class.getName());

    /**
     * Creates a new instance of DropFileExtractor.
     */
    public DropFileExtractor() {
    }

    /**
     * Exracts file names from a drop event.
     * @param dtde Drop event to extract the filenames from
     * @return List of java.io.File instances. The returned list is never null, but may be a Collections.EMPTY_LIST
     */
    public List extractFiles(java.awt.dnd.DropTargetDropEvent dtde) {
        Transferable trans = dtde.getTransferable();

        List fileList = Collections.EMPTY_LIST;

        try {
            DataFlavor[] flavors = dtde.getCurrentDataFlavors();
            DataFlavor flavor = DataFlavor.selectBestTextFlavor(flavors);

            if (flavor == null) {
                for (int i = 0; i < flavors.length; i++) {
                    if (trans.isDataFlavorSupported(flavors[i])) {
                        fileList = processDrop(trans, flavors[i]);
                    }
                }
            } else {
                fileList = processDrop(trans, flavor);
            }
        } catch (java.awt.datatransfer.UnsupportedFlavorException e) {
            logger.throwing(getClass().getName(), "extractFiles",e);
        } catch (java.io.IOException e) {
            logger.throwing(getClass().getName(), "extractFiles",e);
        }

        return fileList;
    }
    
    /**
     * This method tries to deal with the Transferable with clues given by
     * the DataFlavor. Since different platforms manage drag+drop differently,
     * this code is a little complex. I try to return a list of File objects
     * from this method.
     * @return a list of java.io.File objects containing the files to be converted
     */    
    private List processDrop(Transferable trans, DataFlavor flavor) throws IOException, UnsupportedFlavorException {
        List fileList = new ArrayList();
        String repClassStr = flavor.getDefaultRepresentationClassAsString();
        String mimeType = flavor.getMimeType();
        logger.finer(flavor.getHumanPresentableName() + " == " + repClassStr + " m " + mimeType);

        Object o = null;

        // check for simple strings first, this appears to be okay on Solaris/GNOME
        if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String s = (String)trans.getTransferData(DataFlavor.stringFlavor);
            logger.log(Level.CONFIG, "TransferData == " + s);

            String[] filenames = s.split("\n");

            for (int i = 0; i < filenames.length; i++) {
                File f = new File(filenames[i].replaceAll("file://", "").trim());
                fileList.add(f);
            }

            // cool we're done!
            return fileList;
        }

        // otherwise, things are more complex.
        o = trans.getTransferData(flavor);

        // We need to work out what type this object is.
        // Different platforms handle drag+drop differently, so this is a bit
        // of a hack.
        if (o != null) {
            if (o instanceof java.util.List) {
                fileList = getWin32FileList(o);
            } else if (o instanceof java.io.InputStreamReader) {
                fileList = getMacOSXFileList(o);
            } else {
                logger.log(Level.SEVERE, "Object type " + o.getClass().toString() + " unknown.");
            }
        } else {
            logger.finer("o was null!");
        }

        return fileList;
    }

    /**
     * MacOSX specific extraction woodoo
     * @param o 
     * @throws java.io.IOException 
     * @return 
     */
    private List getMacOSXFileList(Object o) throws java.io.IOException {
        List fileList = new ArrayList();

        // Mac OSX appears to use InputStreamReader
        // I can't work out how to handle multiple files yet
        Reader reader = (InputStreamReader)o;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while ((i = reader.read()) != -1) {
            buf.append((char)i);
        }

        // buf now contains something like :
        // file://localhost/Users/timf/Desktop/file.html
        // this is not ideal, but it'll do for now.
        File f = new File(buf.toString().replaceAll("file://localhost", "").trim());
        fileList.add(f);

        return fileList;
    }

    /**
     * Win32 specific extraction woodoo
     */
    private List getWin32FileList(Object o) {
        List fileList = new ArrayList();

        // Windows appears to give us a list of files (even if there's
        // only one file being dropped
        List l = (List)o;
        Iterator i = l.iterator();

        while (i.hasNext()) {
            Object content = i.next();

            if (content instanceof java.io.File) {
                File f = (File)content;
                fileList.add(f);
            } else {
                logger.log(Level.SEVERE, "List contents of type " + content.getClass().toString() + " are unknown.");
            }
        }

        return fileList;
    }
}
