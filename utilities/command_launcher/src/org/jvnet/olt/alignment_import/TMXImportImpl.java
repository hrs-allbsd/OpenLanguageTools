
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TMXImportImpl.java
 *
 * Created on 02 October 2002, 16:55
 */

package org.jvnet.olt.alignment_import;


import org.xml.sax.InputSource;
import java.io.*;
import java.util.logging.*;
import java.util.Properties;

import java.sql.*;
import javax.sql.DataSource;


/**
 *
 * @author  jc73554
 */
public class TMXImportImpl
{
    public final static int RESET_CONNECTION_AT = 1000;
    /* A logger object */
    private Logger logger =
    Logger.getLogger("org.jvnet.olt.alignment_import");
    private TMXAlignmentImporterProperties props;
    private TMXAttributes tmxAttributes;
    private OracleImportTMXProcessor orac;
    private TMXParse tmxp;
    private Connection conn;
    private int counter;
    private File file;
    private boolean useIndexBoolean = true;
    private String url;
    
    /** Creates a new instance of TMXImportImpl */
    public TMXImportImpl(String attribsFile, Properties properties, String useIndexString, String url) throws Exception
    {
        logger.setLevel(Level.FINEST);
        logger.log(Level.FINER, "Attribs file: " + attribsFile);
        
        if(useIndexString.equals("false")) {
            useIndexBoolean = false;
        } else {
            useIndexBoolean = true;
        }
        this.url = url;
        buildImporterProperties(properties);
        createGlobalAttributes(attribsFile);
        setupDatabaseConnection();
        counter = 0;
             
        
    }
    
    protected void createGlobalAttributes(String attribsFile)
    {
        if((attribsFile != null) && (!attribsFile.equals("")))
        {
            try
            {
                logger.log(Level.FINEST, "Creating TMXAttributesCollection");
                TMXAttributesCollectionImpl tmxAtts = new
                TMXAttributesCollectionImpl();
                TMXAttributesCollection
                tmxAttributesCollection = tmxAtts;
                
                TMXAttributesHandlerImpl handlerImpl =
                new TMXAttributesHandlerImpl(logger,
                tmxAttributesCollection);
                TMXAttributesHandler handler = handlerImpl;
                
                TMXAttributesParser tmxAttributesParser =
                new TMXAttributesParser(handler, null, props, logger);
                tmxAttributesParser.parse(new InputSource(attribsFile));
                logger.log(Level.FINEST, "TMXAttributes Parsed");
                logger.log(Level.FINEST, "TMXAttributes is Global = " +
                tmxAttributesCollection.isGlobal());
                logger.log(Level.FINEST, "Getting global TMXAttributes");
                tmxAttributes =
                tmxAttributesCollection.getTMXAttributes(
                TMXAttributesCollection.GLOBAL);
                logger.log(Level.FINEST, "Got global TMXAttributes");
                
                
            } catch(org.xml.sax.SAXException ex)
            {
                logger.log(Level.SEVERE,
                "SAXException : Failed to read Attributes file",
                ex);
                System.exit(1);
            } catch(javax.xml.parsers.ParserConfigurationException ex)
            {
                logger.log(Level.SEVERE,
                "ParserConfigurationException : Failed to read " +
                "Attributes file", ex);
                System.exit(1);
            } catch(java.io.IOException ex)
            {
                logger.log(Level.SEVERE,
                "IOException: Failed to read Attributes file", ex);
                System.exit(1);
            }
        }
    }
    
    protected void buildImporterProperties(Properties properties)
    {
        props = new TMXAlignmentImporterProperties();
        
        
        props.setProperty(TMXAlignmentImporterProperties.OUTPUT_DIR,
        properties.getProperty(
        TMXAlignmentImporterProperties.OUTPUT_DIR, ""));
        props.setProperty(TMXAlignmentImporterProperties.TMX11_DTD,
        properties.getProperty(
        TMXAlignmentImporterProperties.TMX11_DTD, ""));
        props.setProperty(TMXAlignmentImporterProperties.TMX12_DTD,
        properties.getProperty(
        TMXAlignmentImporterProperties.TMX12_DTD, ""));
        props.setProperty(TMXAlignmentImporterProperties.TMX13_DTD,
        properties.getProperty(
        TMXAlignmentImporterProperties.TMX13_DTD, ""));
        props.setProperty(TMXAlignmentImporterProperties.TMX14_DTD,
        properties.getProperty(
        TMXAlignmentImporterProperties.TMX14_DTD, ""));
        props.setProperty(
        TMXAlignmentImporterProperties.TMX_ATTRIBUTES_DTD,
        properties.getProperty(
        TMXAlignmentImporterProperties.TMX_ATTRIBUTES_DTD, ""));
        props.setProperty(TMXAlignmentImporterProperties.JDBC_URL,
        properties.getProperty(
        TMXAlignmentImporterProperties.JDBC_URL, ""));
    }
    
    public void importFile(java.io.File input) throws Exception
    {
        try
        {
            file = input;
            counter++;
            if((counter % RESET_CONNECTION_AT) == 0) {
                releaseConnection();
                setupDatabaseConnection();
            } 
            tmxp.parse(input, tmxAttributes);
        }
        catch(Exception ex)
        {
            //  Close the database connection if an exception is thrown to
            //  free up any dangling cursors, then open up another one, before
            //  rethrowing the exception.
            orac.releaseConnection();
            setupDatabaseConnection();
            throw ex;
        }
    }
    
    protected void setupDatabaseConnection() throws Exception
    {
        try {
            String jdbc_url = props.getProperty(
            TMXAlignmentImporterProperties.JDBC_URL);
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            conn = DriverManager.getConnection(jdbc_url);
            
            String novaIndexURL = this.url;
            System.out.println("Database is " + jdbc_url);
            System.out.println("Importing to " +novaIndexURL);
            System.out.println("UseIndex flag is "+useIndexBoolean);
            orac = new OracleImportTMXProcessor(logger, props, conn, novaIndexURL);
            orac.setUseIndex(useIndexBoolean);
            
            
            TMXProcessor tmxProcessor = orac;
            
            TMXAttributesFilterImpl filter =
            new TMXAttributesFilterImpl(tmxProcessor, logger);
            TMXAttributesFilter tmxAttributesFilter = filter;
            
            tmxp = new TMXParse(tmxAttributesFilter, logger, props);
            
            orac.makeConnection();
        
        } catch (TMXFileProcessorException ex) {
            logger.log(Level.INFO, "The Alignment Import of TMX file " + file.getName() + " failed because: \n\n" + ex.getMessage());
        } 
    }
    
    public void releaseConnection() throws Exception
    {
        if(orac != null) 
        {
            orac.releaseConnection();
        }
    }
}
