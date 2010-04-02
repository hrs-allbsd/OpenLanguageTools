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

package org.jvnet.olt.filters.xml.xmlconfig;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import org.xml.sax.InputSource;
import java.io.*;
import java.security.*;
import java.net.URLEncoder;
import java.net.URLDecoder;
import javax.naming.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author  bpk1
 */
public class XmlConfigManager {
    
    private static XmlConfigStore configStore = new XmlConfigStore();
    
    public static final String PUBLIC_ID_PREFIX = "pid:";
    public static final String SYSTEM_ID_PREFIX = "sid:";
    public static final String SCHEMA_LOCATION_PREFIX = "sl:";
    public static final String NO_NAMESPACE_SCHEMA_LOCATION_PREFIX = "nnsl:";
    public static final String NAMESPACE_PREFIX = "ns:";
    
    private static MessageDigest md = null;
    
    private static final String ALGORITHM = "MD5";
    
    private static String xmlConfigDTDLocation;
    private static String xmlConfigStoreLocation;
    
    private static boolean hasStarted = false;
    
    public static void init(String theXmlConfigDTDLocation, String theXmlConfigStoreLocation) throws IOException {
        if(!hasStarted) {
            
            xmlConfigDTDLocation = theXmlConfigDTDLocation;
            xmlConfigStoreLocation = theXmlConfigStoreLocation;
            
            XmlConfigManager.buildXmlConfigStore(xmlConfigStoreLocation);
            hasStarted = true;
        }
    }
    
    
    public static String getXmlConfigDTDLocation() throws NamingException {
        if(!hasStarted) {
            Context initial = new InitialContext();
            return ((String)
            initial.lookup("java:comp/env/string/XMLCONFIGDTD"));
        }
        
        return xmlConfigDTDLocation;
    }
    
    public static String getXmlConfigStoreLocation() throws NamingException {
        if(!hasStarted) {
            Context initial = new InitialContext();
            return ((String)
            initial.lookup("java:comp/env/string/XMLCONFIGDIRECTORY"));
        }
        
        return xmlConfigStoreLocation;
    }
    
    
    
    public static void addXmlConfig(XmlConfig config) {
        
        if(!config.getSystemID().equals("")){
            configStore.addXmlConfig((SYSTEM_ID_PREFIX + config.getSystemID()), config);
        }
        
        if(!config.getPublicID().equals("")){
            configStore.addXmlConfig((PUBLIC_ID_PREFIX + config.getPublicID()), config);
        }
        
        if(!config.getNoNamespaceSchemaLocation().equals("")){
            configStore.addXmlConfig((NO_NAMESPACE_SCHEMA_LOCATION_PREFIX  + config.getNoNamespaceSchemaLocation()), config);
        }
        
        if(!config.getSchemaLocation().equals("")){
            configStore.addXmlConfig((SCHEMA_LOCATION_PREFIX + config.getSchemaLocation()), config);
        }
        
        if(!config.getNamespace().equals("")){
            configStore.addXmlConfig((NAMESPACE_PREFIX + config.getNamespace()), config);
        }
        
    }
    
    public static String getXmlConfigName(XmlConfig config) throws XmlConfigNamingException {
        
        String configName = "";
        if (config == null){
            return " ** Unknown XML Configuration ** ";
        }
        if(!config.getSystemID().equals("")){
            configName = configName + (SYSTEM_ID_PREFIX + config.getSystemID() + ":");
        }
        
        if(!config.getPublicID().equals("")){
            configName = configName + (PUBLIC_ID_PREFIX + config.getPublicID() + ":");
        }
        
        if(!config.getNoNamespaceSchemaLocation().equals("")){
            configName = configName + (NO_NAMESPACE_SCHEMA_LOCATION_PREFIX + config.getNoNamespaceSchemaLocation() + ":");
        }
        
        if(!config.getSchemaLocation().equals("")){
            configName = configName + (SCHEMA_LOCATION_PREFIX + config.getSchemaLocation() + ":");
        }
        
        if(!config.getNamespace().equals("")){
            configName = configName + (NAMESPACE_PREFIX + config.getNamespace() + ":");
        }
        
        if(configName.equals("")) {
            throw new XmlConfigNamingException("The configuration file is empty");
        }
        
        try {
            String configNameDigest = generateDigest(configName);
            
            String configNameEncoded = URLEncoder.encode(configNameDigest, "UTF-8");
            // windows can't cope with "*" in the filename, aw bless..
            // need to replace with something it can deal with.
            configNameEncoded = configNameEncoded.replace('*', '_');
            
            String configFileName = configNameEncoded + ".conf";
            
            return configFileName;
            
        } catch(NoSuchAlgorithmException ex) {
            throw new XmlConfigNamingException("Failed to MD5 hash " + configName + "-- due to the MD5 Algorithm not found.");
        } catch(UnsupportedEncodingException ex) {
            throw new XmlConfigNamingException("Failed to MD5 hash " + configName + " due to an unsupported exception. \n" + ex.getMessage());
        } catch(IOException ex) {
            throw new XmlConfigNamingException("Failed to Base64 encode the MD5 hash due to an IOException \n" + ex.getMessage());
        }
        
        
    }
    
    public static void saveXmlConfig(File configFile, XmlConfig config) throws SaveXmlConfigException {
        
        
        try {
            
            String configFileName = XmlConfigManager.getXmlConfigName(config);
            
            File newConfigFile = new File(xmlConfigStoreLocation + File.separator + configFileName);
            
            FileUtil.copy(configFile, newConfigFile);
            
        } catch (XmlConfigNamingException ex) {
            throw new SaveXmlConfigException("XmlConfigNamingException while to save the xml config file \n" + ex.getMessage());
        } catch (IOException ex) {
            throw new SaveXmlConfigException("IOException while attempting to copy Config file to \n" + ex.getMessage());
        }
        
    }
    
    public static Collection getXmlConfigCollection(XmlIdentifier xmlIdentifier, Logger logger) {
        
        Collection configList = new HashSet();
        
        List namespaceList = xmlIdentifier.getNamespaceList();
        
        if(!namespaceList.isEmpty()) {
            Iterator namespaceIterator = namespaceList.listIterator(0);
            
            while(namespaceIterator.hasNext()) {
                String namespace =  (String) namespaceIterator.next();
                XmlConfig config = configStore.getXmlConfig(NAMESPACE_PREFIX + namespace);
                configList.add(config);
            }
        }
        
        String systemID = xmlIdentifier.getSystemID();
        String publicID = xmlIdentifier.getPublicID();
        String schemaLocation = xmlIdentifier.getSchemaLocation();
        String noNamespaceSchemaLocation = xmlIdentifier.getNoNamespaceSchemaLocation();
        
        if(!systemID.equals("")) {
            try {
                XmlConfig config = configStore.getXmlConfig(SYSTEM_ID_PREFIX + systemID);
                if (config != null){
                    configList.add(config);
                } else {
                    logger.log(Level.INFO,"XmlConfig " + SYSTEM_ID_PREFIX + systemID + " not found");
                }
            } catch(NullPointerException ex) {
                logger.log(Level.INFO,"XmlConfig " + SYSTEM_ID_PREFIX + systemID + " not found");
            }
        }
        
        if(!publicID.equals("")) {
            try {
                XmlConfig config = configStore.getXmlConfig(PUBLIC_ID_PREFIX + publicID);
                if (config != null){
                    configList.add(config);
                } else {
                    logger.log(Level.INFO,"XmlConfig " + PUBLIC_ID_PREFIX + publicID + " not found");
                }
            } catch(NullPointerException ex) {
                logger.log(Level.INFO,"XmlConfig " + PUBLIC_ID_PREFIX + publicID + " not found");
            }
        }
        
        if(!schemaLocation.equals("")) {
            try {
                XmlConfig config = configStore.getXmlConfig(SCHEMA_LOCATION_PREFIX + schemaLocation);
                if (config != null){
                    configList.add(config);
                } else {
                    logger.log(Level.INFO,"XmlConfig " + SCHEMA_LOCATION_PREFIX + schemaLocation + " not found");
                }
            } catch(NullPointerException ex) {
                logger.log(Level.INFO,"XmlConfig " + SCHEMA_LOCATION_PREFIX + schemaLocation + " not found");
            }
        }
        
        if(!noNamespaceSchemaLocation.equals("")) {
            try {
                XmlConfig config = configStore.getXmlConfig(NO_NAMESPACE_SCHEMA_LOCATION_PREFIX + noNamespaceSchemaLocation);
                if (config != null){
                    configList.add(config);
                } else {
                    logger.log(Level.INFO,"XmlConfig " + NO_NAMESPACE_SCHEMA_LOCATION_PREFIX + noNamespaceSchemaLocation + " not found");
                }
            } catch(NullPointerException ex) {
                logger.log(Level.INFO,"XmlConfig " + NO_NAMESPACE_SCHEMA_LOCATION_PREFIX + noNamespaceSchemaLocation + " not found");
            }
        }
        
        return configList;
        
        
    }
    
    private static void buildXmlConfigStore(String directoryLocation) throws IOException {
        File directory = new File(directoryLocation);
        if (!directory.exists()){
            throw new IOException("Directory " + directoryLocation +" does not exist."+
            " Please report this error to SunTrans2 support staff");
        }
        File[] fileArray = directory.listFiles();
        
        for(int i = 0; i < fileArray.length; i++) {
            File myFile = fileArray[i];
            if(myFile.isFile() && myFile.getName().endsWith(".conf")){
                try {
                    XmlConfigManager.nonSavingProcessConfig(myFile);
                } catch(ProcessXmlConfigException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        
    }
    
    private static XmlConfig nonSavingProcessConfig(File configFile) throws ProcessXmlConfigException {
        InputSource inputSource = null;
        
        try {
            Reader fileReader = new BufferedReader(new FileReader(configFile));
            inputSource = new InputSource(fileReader);
            
            XmlConfig config = new XmlConfig();
            
            XmlConfigHandler xmlConfigHandler = new XmlConfigHandlerImpl(config);
            
            File xmlConfigDtdFile = new File(xmlConfigDTDLocation);
            
            Reader xmlConfigDTDReader = new BufferedReader(new
            FileReader(xmlConfigDtdFile));
            
            XmlConfigResolver entityResolver = new XmlConfigResolver(xmlConfigDTDReader);
            
            XmlConfigParser xmlConfigParser =
            new XmlConfigParser(xmlConfigHandler, entityResolver);
            
            xmlConfigParser.parse(inputSource);
            
            XmlConfigManager.addXmlConfig(config);
            
            return config;
            
            
        } catch (FileNotFoundException ex) {
            throw new ProcessXmlConfigException("FileNotFoundException while processing file " + configFile.getName() + "\n" + ex.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new ProcessXmlConfigException("NullPointerException while processing file " + configFile.getName() + "\n" + e.getMessage());
        } catch(org.xml.sax.SAXException ex) {
            throw new ProcessXmlConfigException("SAXException while processing file " + configFile + "\n" + ex.getMessage());
        }  catch(javax.xml.parsers.ParserConfigurationException ex) {
            throw new ProcessXmlConfigException("ParserConfigurationException while processing file " + configFile + "\n" + ex.getMessage());
        } catch(java.io.IOException ex) {
            throw new ProcessXmlConfigException("IOException while processing file " + configFile + "\n" + ex.getMessage());
        }
    }
    
    public static void processConfig(File configFile) throws ProcessXmlConfigException {
        
        try{
            XmlConfig config = XmlConfigManager.nonSavingProcessConfig(configFile);
            
            
            XmlConfigManager.saveXmlConfig(configFile, config);
            
        } catch(SaveXmlConfigException ex) {
            throw new ProcessXmlConfigException("Unable to save config file " + configFile + "to file system \n" + ex.getMessage());
        }
        
        
    }
    
    
    public static String returnString() {
        return configStore.toString();
    }
    
    private static String generateDigest(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        md = MessageDigest.getInstance(ALGORITHM);
        
        String digest = "";
        
        // ISO-8859-1 gives us the raw bytes from the digest I think
        byte[] bytes = md.digest(string.getBytes("ISO-8859-1"));
        digest = new String(bytes,"ISO-8859-1");
        
        
        return digest;
    }
    
    
}
