/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * FileCopier.java
 *
 * Created on March 4, 2005, 2:29 PM
 */
package org.jvnet.olt.editor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


/**
 *
 * @author boris
 */

//TODO add tests for copyFile* methods reg. encoded copying etc
public class FileUtils {
    /** Creates a new instance of FileCopier */
    private FileUtils() {
    }

    /** copies all from reader to destFile.
     *
     * destFile is encoded in UTF-8, allocates 4KB buffer
     *
     */
    static public void copyStreamToFile(Reader reader, File destFile) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));

        char[] buffer = new char[4096];

        try {
            int read = 0;

            do {
                read = reader.read(buffer);

                if (read == -1) {
                    break;
                }

                writer.write(buffer, 0, read);
            } while (true);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /** Copy file srcFile to destFile.
     *
     *This method copies srcFile to destFile using UTF-8 encoding, so copying
     *binary files may result in corrupted destination files (e.g. copying zip files)
     *
     */
    static public void copyFiles(File srcFile, File destFile) throws IOException, IllegalArgumentException {
        if ((srcFile == null) || (destFile == null)) {
            throw new NullPointerException(((srcFile == null) ? "source" : " destination ") + " file is null");
        }

        if (!srcFile.exists()) {
            throw new IOException("source file does not exist:" + srcFile);
        }

        if (!srcFile.isFile()) {
            throw new IllegalArgumentException("source file is not a file:" + srcFile);
        }

        if (!srcFile.canRead()) {
            throw new IOException("can not read source file:" + srcFile);
        }

        if (destFile.exists()) {
            if (!destFile.isFile()) {
                throw new IllegalArgumentException("desctination file is not a file" + destFile);
            }

            if (!destFile.canWrite()) {
                throw new IOException("can not write destination file:" + destFile);
            }
        }

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "UTF-8"));
        Reader reader = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");

        char[] buffer = new char[4096];

        try {
            int read = 0;

            do {
                read = reader.read(buffer);

                if (read == -1) {
                    break;
                }

                writer.write(buffer, 0, read);
            } while (true);
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    /** Copy file srcFile to file destFile.
     *
     * This method will copy source file to destination file by bytes.
     * This method is intended to be used for binary files.
     *
     */
    static public void copyFileNoEncode(File srcFile, File destFile) throws IOException {
        if ((srcFile == null) || (destFile == null)) {
            throw new NullPointerException(((srcFile == null) ? "source" : " destination ") + " file is null");
        }

        if (!srcFile.exists()) {
            throw new IOException("source file does not exist:" + srcFile);
        }

        if (!srcFile.isFile()) {
            throw new IllegalArgumentException("source file is not a file:" + srcFile);
        }

        if (!srcFile.canRead()) {
            throw new IOException("can not read source file:" + srcFile);
        }

        if (destFile.exists()) {
            if (!destFile.isFile()) {
                throw new IllegalArgumentException("desctination file is not a file" + destFile);
            }

            if (!destFile.canWrite()) {
                throw new IOException("can not write destination file:" + destFile);
            }
        }

        InputStream is = new FileInputStream(srcFile);
        OutputStream os = new FileOutputStream(destFile);

        byte[] buffer = new byte[4096];

        try {
            int read = 0;

            do {
                read = is.read(buffer);

                if (read == -1) {
                    break;
                }

                os.write(buffer, 0, read);
            } while (true);
        } finally {
            if (os != null) {
                os.close();
            }

            if (is != null) {
                is.close();
            }
        }
    }

    static public void copyFileToStream(File srcFile, Writer writer) throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(srcFile), "UTF-8");

        char[] buffer = new char[4096];

        try {
            int read = 0;

            do {
                read = reader.read(buffer);

                if (read == -1) {
                    break;
                }

                writer.write(buffer, 0, read);
            } while (true);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
