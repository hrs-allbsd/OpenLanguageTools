/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.lucene;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 * Serialize/Deserialize ConfigHolder class
 *
 */
public class ConfigHolderHelper {
    
    /** do not allow to create any instance */
    private ConfigHolderHelper() {
    }
    
    /**
     * Read configuration from given file name
     *
     * @param filename the name of file where configuration is stored
     *
     * @return new instance of ConfigHolder class
     *
     * @throws FileNotFoundException if the file cannot be readed
     */
    public static ConfigHolder read(String filename) throws FileNotFoundException {
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        ConfigHolder o = (ConfigHolder)decoder.readObject();
        decoder.close();
        return o;
    }
    
    /**
     * Write configuration to given file name
     *
     * @param filename where to store the confiuration
     * @param config the ConfigHolder instance to serialize
     *
     * @throws FileNotFoundException if the configuration cannot be written
     */
    public static void write(String filename,ConfigHolder config) throws FileNotFoundException {
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(filename)));
        encoder.writeObject(config);
        encoder.close();
    }
    
}
