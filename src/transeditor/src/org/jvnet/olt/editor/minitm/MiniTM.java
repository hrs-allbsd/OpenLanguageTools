/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.minitm;

import java.io.*;

import java.util.*;
import java.util.logging.*;


//import  org.jvnet.olt.editor.util.Encode;

/**
 * <p>Title: Open Language Tools XLIFF Translation Editor</p>
 * <p>Description: This class is the definition of Mini-TM, it is used to get/save a fuzzy match from/into the MiniTM file. </p>
 * @version 1.0
 */
public class MiniTM {
    private static Logger logger = Logger.getLogger(MiniTM.class.getName());
    final static int RECORDLENGTH = 24;
    final static int RECORDNUMBER = 4000;
    File indexFile;
    File tmFile;
    RandomAccessFile rafIndex;
    RandomAccessFile rafTM;
    long globalpos;
    int pagenumber = 0;
    int recordnumber = 0;
    long tmlength = 0;
    byte[] buffer = new byte[RECORDLENGTH * RECORDNUMBER];
    String miniTMDir = "mini-tm" + File.separator;
    String name;

    /**
     * Constructor function.
     */
    public MiniTM(String filename) {
        File d = new File(miniTMDir);

        if (!d.exists()) {
            d.mkdir();
        }

        name = filename;
        indexFile = new File(miniTMDir + name + ".INDEX");
        tmFile = new File(miniTMDir + name + ".MTM");

        try {
            if (indexFile.exists() && indexFile.isFile()) {
                rafIndex = new RandomAccessFile(indexFile, "rw");
                rafTM = new RandomAccessFile(tmFile, "rw");
                rafIndex.seek(0);
                pagenumber = rafIndex.readInt();
                recordnumber = rafIndex.readInt();

                if (rafIndex.length() >= 16) {
                    tmlength = rafIndex.readLong();
                } else {
                    tmlength = -1;
                }

                //                logger.finer("recorded TM File length = " + tmlength);
                //logger.finer("TM File length = " + rafTM.length());
                if (tmlength != rafTM.length()) {
                    logger.info("MiniTM is damaged or old version, create new one");
                    pagenumber = recordnumber = 0;
                    tmlength = 0;
                    rafIndex.writeInt(pagenumber);
                    rafIndex.writeInt(recordnumber);
                    rafIndex.writeLong(tmlength);
                    rafTM.setLength(0);
                    rafIndex.setLength(16);
                }
            } else {
                rafIndex = new RandomAccessFile(indexFile, "rw");
                rafIndex.writeInt(pagenumber);
                rafIndex.writeInt(recordnumber);
                rafIndex.writeLong(tmlength);
                rafTM = new RandomAccessFile(tmFile, "rw");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public TMUnit selectNoUse(String source) {
        long hashvalue = HashKey.getHashValue(source);

        if (recordnumber == 0) {
            return null;
        }

        for (int i = 0; i < pagenumber; i++) {
            int recordInPage = loadPage(i);

            int pointer = 0;

            for (int j = 0; j < recordInPage; j++) {
                long value = TypeConversion.bytesToLong(buffer, pointer);

                if (value == hashvalue) {
                    pointer += 8;

                    long keyOffset = TypeConversion.bytesToLong(buffer, pointer);
                    pointer += 8;

                    long transOffset = TypeConversion.bytesToLong(buffer, pointer);

                    String src = null;
                    String tgt = null;

                    try {
                        rafTM.seek(keyOffset);

                        //src = Encode.base64Decode(rafTM.readUTF());
                        src = rafTM.readUTF();
                        rafTM.seek(transOffset);

                        //tgt = Encode.base64Decode(rafTM.readUTF());
                        tgt = rafTM.readUTF();
                    } catch (Exception e) {
                        logger.throwing(getClass().getName(), "selectNoUse", e);
                        logger.severe("Exception: " + e.getMessage());
                    }

                    if (src.equals(source)) {
                        pointer -= 16;

                        return new TMUnit(src, tgt);
                    } else {
                        pointer -= 16;
                    }
                }

                pointer += RECORDLENGTH;
            }
        }

        return null;
    }

    public Vector selectAll(String source) {
        Vector units = new Vector();

        long hashvalue = HashKey.getHashValue(source);

        if (recordnumber == 0) {
            return null;
        }

        for (int i = 0; i < pagenumber; i++) {
            int recordInPage = loadPage(i);

            int pointer = 0;

            for (int j = 0; j < recordInPage; j++) {
                long value = TypeConversion.bytesToLong(buffer, pointer);

                if (value == hashvalue) {
                    pointer += 8;

                    long keyOffset = TypeConversion.bytesToLong(buffer, pointer);
                    pointer += 8;

                    long transOffset = TypeConversion.bytesToLong(buffer, pointer);

                    String src = null;
                    String tgt = null;

                    try {
                        rafTM.seek(keyOffset);

                        //src = Encode.base64Decode(rafTM.readUTF());
                        src = rafTM.readUTF();
                        rafTM.seek(transOffset);

                        //tgt = Encode.base64Decode(rafTM.readUTF());
                        tgt = rafTM.readUTF();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (src.equals(source)) {
                        pointer -= 16;
                        units.add(new TMUnit(src, tgt));
                    } else {
                        pointer -= 16;
                    }
                }

                pointer += RECORDLENGTH;
            }
        }

        return units;
    }

    public int loadPage(int page) {
        long pos = 16 + (page * RECORDLENGTH * RECORDNUMBER);

        try {
            rafIndex.seek(pos);

            int bytenum = rafIndex.read(buffer);

            return (bytenum / RECORDLENGTH);
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "loadPage", e);
            logger.severe("Exception: " + e.getMessage());

            //TODO throw upwards ???
            return -1;
        }
    }

    private void saveRecord(byte[] record) {
        try {
            long pos = 16 + (recordnumber * RECORDLENGTH);
            rafIndex.seek(pos);
            rafIndex.write(record);
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "setRecord", e);
            logger.severe("Exception: " + e.getMessage());

            //TODO -- throw an exception
        }
    }

    void resetBuffer() {
        for (int i = 0; i < (RECORDLENGTH * RECORDNUMBER); i++)
            buffer[i] = 0;
    }

    /**
     * Return boolean if the translator can update datasource
     * to the given data and translateddata.
     *
     * @param aunit The translation unit
     * @return boolean if this translator update it successfully
     */
    public boolean append(TMUnit aunit, boolean check) {
        if (check == true) {
            Vector tus = selectAll(aunit.getSource());

            if (tus != null) {
                for (int i = 0; i < tus.size(); i++) {
                    TMUnit tu = (TMUnit)(tus.elementAt(i));

                    if (tu.getSource().equals(aunit.getSource()) && tu.getTarget().equals(aunit.getTarget())) {
                        return false;
                    }
                }
            }
        }

        byte[] record = new byte[RECORDLENGTH];
        int appendpos = 0;
        long keyOffset = 0;
        long transOffset = 0;
        long hashvalue = HashKey.getHashValue(aunit.getSource());

        try {
            keyOffset = rafTM.length();
            rafTM.seek(keyOffset);

            //rafTM.writeUTF(Encode.base64Encode(aunit.getSource()));
            rafTM.writeUTF(aunit.getSource());

            transOffset = rafTM.length();
            rafTM.seek(transOffset);

            //rafTM.writeUTF(Encode.base64Encode(aunit.getTarget()));
            rafTM.writeUTF(aunit.getTarget());
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "append", e);
            logger.severe("Exception: " + e.getMessage());

            //TODO throw an exception
        }

        //logger.finer("keyOffset " + keyOffset + "   transOffset " + transOffset);
        TypeConversion.longToBytes(hashvalue, record, appendpos);
        appendpos += 8;
        TypeConversion.longToBytes(keyOffset, record, appendpos);
        appendpos += 8;
        TypeConversion.longToBytes(transOffset, record, appendpos);
        saveRecord(record);
        recordnumber++;

        int mod = recordnumber / RECORDNUMBER;
        int rem = recordnumber % RECORDNUMBER;

        if ((rem > 0) && (mod == pagenumber)) {
            pagenumber++;
        }

        try {
            rafIndex.seek(0);
            rafIndex.writeInt(pagenumber);
            rafIndex.writeInt(recordnumber);
            rafIndex.writeLong(rafTM.length());
        } catch (IOException e) {
            logger.throwing(getClass().getName(), "append", e);
            logger.severe("\nException: " + e.getMessage());

            //TODO throw an exception
        }

        return true;
    }

    /**
     * Return boolean if the translator close datasource
     *
     * @return boolean if this translator update it successfully
     */
    public boolean close() {
        try {
            //logger.finer("close minitm");
            rafIndex.seek(0);
            rafIndex.writeInt(pagenumber);
            rafIndex.writeInt(recordnumber);
            rafIndex.writeLong(rafTM.length());
            rafIndex.close();
            rafTM.close();
        } catch (IOException e) {
            logger.throwing(getClass().getName(), "close", e);
            logger.severe("\nException: " + e.getMessage());

            //TODO throw an exception~
        }

        return true;
    }
}
