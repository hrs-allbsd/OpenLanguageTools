/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.io.*;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;

import org.jvnet.olt.backconv.BackConverter;
import org.jvnet.olt.utilities.XliffZipFileIO;
import org.jvnet.olt.xliff_tmx_converter.XliffToTmxTransformer;
import org.jvnet.olt.xliff_tmx_converter.XliffToTmxTransformerException;


/**
 * User: boris
 * Date: Feb 3, 2005
 * Time: 2:02:34 PM
 */
public class JBackConverterExecutor {
    final Logger logger = Logger.getLogger(JBackConverterExecutor.class.getName());
    private boolean createTMX;
    private boolean status2SGML;
    private String encoding;
    private File fSrc;
    private File fTarget;
    private MainFrame mainFrame = MainFrame.getAnInstance();
    private BackconversionStatusCallback callback;
    private boolean sourceValid;
    private boolean targetValid;

    JBackConverterExecutor(boolean createTMXFile, String encoding, boolean status2SGML) {
        this.createTMX = createTMXFile;
        this.encoding = encoding;
        this.status2SGML = status2SGML;
    }

    public void setStatusCallback(BackconversionStatusCallback callback) {
        this.callback = callback;
    }

    /**
     * recursively find all xlz files
     */
    private Vector listAllFiles(File directory) {
        File[] fileList = null;
        Vector vectList = new Vector();

        if (directory.isDirectory()) {
            fileList = directory.listFiles();
        }

        String path = directory.getAbsolutePath();

        for (int i = 0; i < fileList.length; i++) {
            File f = new File(path + File.separator + fileList[i].getName());

            if (!f.isDirectory()) {
                if (f.getAbsolutePath().endsWith(".xlz") == true) {
                    vectList.addElement(fileList[i]);
                }
            } else {
                Vector subList = listAllFiles(f);
                Enumeration myenum = subList.elements();

                while (myenum.hasMoreElements())
                    vectList.addElement(myenum.nextElement());
            }
        }

        return vectList;
    }

    /**
     * BROKEN -- mkdirs returns false cotniunes anyway
     *
     * @param strSrcInput
     * @param strTargetInput
     * @param vInput
     * @return
     */
    private boolean createSubDir(String strSrcInput, String strTargetInput, Vector vInput) {
        for (int i = 0; i < vInput.size(); i++) {
            String strFileElement = ((File)vInput.elementAt(i)).getAbsolutePath();
            strFileElement = strFileElement.substring((int)(strFileElement.indexOf(strSrcInput) + strSrcInput.length() + 1), strFileElement.length());

            int iIndexLocal = strFileElement.lastIndexOf(File.separator);

            if (iIndexLocal != -1) {
                strFileElement = strFileElement.substring(0, strFileElement.lastIndexOf(File.separator));
            } else {
                continue;
            }

            if (strTargetInput.endsWith(File.separator) == true) {
                strFileElement = strTargetInput + strFileElement;
            } else {
                strFileElement = strTargetInput + File.separator + strFileElement;
            }

            File fSubDir = new File(strFileElement);

            if (!fSubDir.exists()) {
                boolean b = fSubDir.mkdirs();
            }
        }

        return true;
    }

    public void setSourceFile(String srcFile) throws FileNotFoundException, IOException {
        File xFile = new File(srcFile);

        if (!xFile.exists()) {
            throw new FileNotFoundException();
        }

        if (!xFile.canRead()) {
            throw new IOException();
        }

        fSrc = xFile;

        sourceValid = true;
    }

    public void setTargetFile(String tgtFile) throws FileNotFoundException, IOException {
        if (!sourceValid) {
            throw new IllegalStateException("the source dir is not valid");
        }

        File xFile = null;

        if ((tgtFile == null) || (tgtFile.length() == 0)) {
            if (fSrc.isDirectory()) {
                xFile = fSrc;
            } else {
                xFile = fSrc.getParentFile();
            }
        }

        xFile = new File(tgtFile);

        if (!xFile.exists()) {
            throw new FileNotFoundException();
        }

        if (!xFile.isDirectory()) {
            throw new IllegalArgumentException();
        }

        if (!xFile.canRead() || !xFile.canWrite()) {
            throw new IOException();
        }

        fTarget = xFile;

        targetValid = true;
    }

    void convert() {
        //Run conversion in separate thread
        new Thread(new Runnable() {
                public void run() {
                    doConvert();
                }
            }).start();
    }

    private void doConvert() {
        if (!(targetValid && sourceValid)) {
            throw new IllegalStateException("target or source not valid");
        }

        if (callback == null) {
            throw new NullPointerException("status callback is null");
        }

        try {
            BackConverter xbc = null;

            if (fSrc.isDirectory()) { // select a directory

                Vector v = listAllFiles(fSrc);

                if (v.size() == 0) {
                    JOptionPane.showMessageDialog(mainFrame, "The selected directory contains no xlz files", "warning", JOptionPane.OK_OPTION);

                    return;
                }

                callback.conversionStart(v.size());

                boolean b = createSubDir(fSrc.getAbsolutePath(), fTarget.getAbsolutePath(), v);

                for (Iterator i = v.iterator(); i.hasNext();) {
                    File curFile = (File)i.next();

                    callback.fileStarted(curFile);

                    if (isFrameFile(fSrc)) {
                        throw new UnsupportedOperationException("FrameMaker backconv not supported");
                    }

                    org.jvnet.olt.util.ContentSource xlfDtdSource = new org.jvnet.olt.util.LoadableResourceContentSource("resources/xliff.dtd");
                    org.jvnet.olt.util.ContentSource sklDtdSource = new org.jvnet.olt.util.LoadableResourceContentSource("resources/tt-xliff-skl.dtd");

                    xbc = new BackConverter(xlfDtdSource, sklDtdSource, logger);

                    String strSubDir = curFile.getAbsolutePath();
                    strSubDir = strSubDir.substring((int)(strSubDir.indexOf(fSrc.getAbsolutePath()) + fSrc.getAbsolutePath().length() + 1), strSubDir.length());

                    int iIndexLocal = strSubDir.lastIndexOf(File.separator);

                    if (iIndexLocal != -1) {
                        strSubDir = strSubDir.substring(0, strSubDir.lastIndexOf(File.separator));
                    } else {
                        strSubDir = "";
                    }

                    try {
                        xbc.backConvert(curFile, fTarget.getAbsolutePath() + File.separator + strSubDir, false, encoding, status2SGML);

                        boolean tmxFailed = false;

                        if (createTMX == true) {
                            XliffToTmxTransformer xtt = createXLIFF2TMXTransformer();
                            String fileName = curFile.getAbsolutePath();

                            //int iPosLocal = fileName.indexOf(fSrc.getAbsolutePath())+fSrc.get
                            fileName = fileName.substring((int)(fileName.indexOf(fSrc.getAbsolutePath()) + fSrc.getAbsolutePath().length() + 1), fileName.length());

                            //System.out.println("fileName = "+fileName);
                            String fileNamePrefix = fileName.substring(0, fileName.lastIndexOf(".") + 1);
                            String outputFileName = fileNamePrefix + "tmx";

                            String fullPath = fTarget.getAbsolutePath() + File.separator + outputFileName;

                            tmxFailed = !createTMX(xtt, curFile, new File(fullPath));
                        }

                        if (tmxFailed) {
                            callback.fileError(BackconversionStatusCallback.ERROR_TMX);
                        } else {
                            callback.fileSuccess();
                        }
                    } catch (org.jvnet.olt.backconv.BackConverterException xbce) {
                        xbce.printStackTrace();
                        callback.fileError(BackconversionStatusCallback.ERROR_BACKCONV);
                    } catch (org.jvnet.olt.xliff_tmx_converter.XliffToTmxTransformerException xtc) {
                        xtc.printStackTrace();
                        callback.fileError(BackconversionStatusCallback.ERROR_TMX);
                    } catch (UnsupportedOperationException uoe) {
                        //frame maker stuff
                        callback.fileError(BackconversionStatusCallback.ERROR_FRAMEFILE);
                    }
                }

                callback.conversionEnd();
            } else { // select single file

                org.jvnet.olt.util.ContentSource xlfDtdSource = new org.jvnet.olt.util.LoadableResourceContentSource("resources/xliff.dtd");
                org.jvnet.olt.util.ContentSource sklDtdSource = new org.jvnet.olt.util.LoadableResourceContentSource("resources/tt-xliff-skl.dtd");

                callback.conversionStart(1);
                callback.fileStarted(fSrc);

                try {
                    if (isFrameFile(fSrc)) {
                        throw new UnsupportedOperationException("FrameMaker backconv not supported");
                    }

                    xbc = new BackConverter(xlfDtdSource, sklDtdSource, logger);

                    xbc.backConvert(fSrc, fTarget.getAbsolutePath(), false, encoding, status2SGML);

                    boolean tmxFailed = false;

                    if (createTMX == true) {
                        XliffToTmxTransformer xtt = createXLIFF2TMXTransformer();

                        String fileName = fSrc.getName();
                        String fileNamePrefix = fileName.substring(0, fileName.lastIndexOf(".") + 1);
                        String outputFileName = fileNamePrefix + "tmx";
                        String fullPath = fTarget.getAbsolutePath() + File.separator + outputFileName;

                        tmxFailed = !createTMX(xtt, fSrc, new File(fullPath));
                    }

                    if (tmxFailed) {
                        callback.fileError(BackconversionStatusCallback.ERROR_TMX);
                    } else {
                        callback.fileSuccess();
                    }
                } catch (org.jvnet.olt.backconv.BackConverterException xbce) {
                    xbce.printStackTrace();
                    callback.fileError(BackconversionStatusCallback.ERROR_BACKCONV);
                } catch (org.jvnet.olt.xliff_tmx_converter.XliffToTmxTransformerException xtc) {
                    xtc.printStackTrace();
                    callback.fileError(BackconversionStatusCallback.ERROR_TMX);
                } catch (UnsupportedOperationException uoe) {
                    //frame maker stuff
                    callback.fileError(BackconversionStatusCallback.ERROR_FRAMEFILE);
                }

                callback.conversionEnd();
            }
        } catch (Throwable ec) {
            ec.printStackTrace();
        } finally {
            callback.unlock();
        }
    }

    boolean createTMX(XliffToTmxTransformer xtt, File inFile, File outFile) throws IOException, XliffToTmxTransformerException, UnsupportedOperationException {
        XliffZipFileIO xlz1 = new XliffZipFileIO(inFile);

        Reader xlfFile = xlz1.getXliffReader();
        boolean translated = xtt.isFullyTranslated(xlfFile);

        if (!translated) {
            return false;
        }

        Writer output = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
        XliffZipFileIO xzf = new XliffZipFileIO(inFile);
        Reader inputXliffFile = xzf.getXliffReader();
        xtt.doTransform(inputXliffFile, output);
        output.flush();
        output.close();

        return true;
    }

    XliffToTmxTransformer createXLIFF2TMXTransformer() throws XliffToTmxTransformerException {
        Reader fr_xlf;
        Reader fr_xlf2;
        Reader fr_skl;
        Reader fr_testxlf;
        Reader fr_xsl;

        fr_xlf = new InputStreamReader(this.getClass().getResourceAsStream("/resources/xliff.dtd"));
        fr_skl = new InputStreamReader(this.getClass().getResourceAsStream("/resources/tt-xliff-skl.dtd"));
        fr_xlf2 = new InputStreamReader(this.getClass().getResourceAsStream("/resources/xliff.dtd"));
        fr_xsl = new InputStreamReader(this.getClass().getResourceAsStream("/resources/xliff-tmx_MN.xsl"));
        fr_testxlf = new InputStreamReader(this.getClass().getResourceAsStream("/resources/xliff.dtd"));

        return new XliffToTmxTransformer(logger, fr_xsl, fr_xlf, fr_testxlf);
    }

    boolean isFrameFile(File f) throws IOException {
        XliffZipFileIO xlzio = new XliffZipFileIO(f);
        Properties props = xlzio.getWorkflowProperties();

        return (props == null) ? false : "FrameMaker".equals(props.getProperty("xmltype"));
    }
}
