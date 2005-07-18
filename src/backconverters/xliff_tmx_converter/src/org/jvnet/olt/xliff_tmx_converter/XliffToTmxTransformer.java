
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_tmx_converter;

import java.io.Reader;
import java.io.Writer;
import java.lang.Integer;

import javax.xml.transform.Templates;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.util.logging.*;

/**
 * XliffToTmxTransformer is the class that uses the <a
 * href="http://xml.apache.org/xalan-j/trax.html">TrAX API</a> and XSLT to
 * transform a XLIFF file to TMX.
 *
 * @author Brian Kidney
 * @created August 27, 2002
 */
public class XliffToTmxTransformer {

    /*
     * Logging object
     */
    private static Logger logger;

    /*
     * Stores the XSLT templates for transformation
     */
    private Templates templates;

    /*
     * The XMLReader for parsing and validation of the XLIFF file
     */
    private XMLReader xmlReader;

    /*
     * The context
     */
    private java.util.Stack context;

    /*
     * The Entity Resolver for resolving the location of the XLIFF DTD
     */
    private EntityResolver resolver = null;
    
    private EntityResolver testResolver = null;
    
    /**
     * Constructor for the XliffToTmxTransformer object
     *
     * @param theLogger The logger
     * @param xslFile The XSLT file used in the transformation
     * @param xliffDTD The Xliff DTD
     * @param testXliffDTD The Xliff DTD for the isFullyTranslated method
     * @exception XliffToTmxTransformerException Exception thrown if 
     * transformation fails.
     */
    public XliffToTmxTransformer(Logger theLogger, Reader xslFile, 
        Reader xliffDTD, Reader testXliffDTD) throws XliffToTmxTransformerException {
            
        this.logger = theLogger;
        logger.log(Level.FINEST, 
            "XliffToTMXTranformer Constructor created"); 
                   
        resolver = new XliffEntityResolver(logger, xliffDTD);         
        testResolver = new XliffEntityResolver(logger, testXliffDTD); 
        logger.log(Level.FINEST, "XliffEntityResolver created");
        context = new java.util.Stack();
        logger.log(Level.FINEST, "context created");
        try {
            TransformerFactory xformFactory
                 = TransformerFactory.newInstance();
            logger.log(Level.FINEST, "TransormerFactory created");     
            Source xsl = new
                StreamSource(xslFile);
            logger.log(Level.FINEST, "Source created"); 
            templates = xformFactory.newTemplates(xsl);
            logger.log(Level.FINEST, "Template created"); 

        } catch (TransformerException ex) {
            logger.log(Level.FINE, "Could not load the stylesheet", ex);
            XliffToTmxTransformerException newEx = new XliffToTmxTransformerException();
            newEx.setStackTrace(ex.getStackTrace());
            throw newEx;
        }
        
        
        logger.log(Level.FINEST, 
            "XliffToTMXTranformer Constructor finished");

    }
    
    /**
     * Checks if all the trans-units contained a translated segment in the 
     * xliff file.
     *
     * @param reader The input XLIFF to be checked.
     */
    public boolean isFullyTranslated(Reader reader) 
        throws XliffToTmxTransformerException {
           
        try {
            XliffHandlerImpl handlerImpl = new XliffHandlerImpl(logger);
            XliffHandler handler = handlerImpl;
            XliffParser parser =
                new XliffParser(handler, testResolver, logger);
            parser.parse(new InputSource(reader));
        } catch(XliffTargetNotFoundException ex) {
            logger.log(Level.FINE, "Translated target method not found in trans-unit", ex);
		ex.printStackTrace();
            return false;
        } catch (NullPointerException ex) {
            logger.log(Level.FINE, "Count not parse the XLIFF file", ex);
            throw new XliffToTmxTransformerException(ex.getMessage());
        } catch (SAXException ex) {
            logger.log(Level.FINE, "Could not parse the XLIFF file", ex);ex.printStackTrace();
            throw new XliffToTmxTransformerException(ex.getMessage());
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            logger.log(Level.FINE, 
                "Could not configure the XSLT parser", ex);ex.printStackTrace();
            throw new XliffToTmxTransformerException();
        } catch (java.io.IOException ex) {
            logger.log(Level.FINE, 
                "Problem accessing IO", ex);ex.printStackTrace();
            throw new XliffToTmxTransformerException();
        }        
        return true;
    }


    /**
     * The <code>doTransform</code> carries out the transformation of the
     * XLIFF from a reader object to the resulting TMX in a writer object.
     * <p>
     *
     * A <code>XMLReader</code> is used to parse the XLIFF file, this <code>XMLReader</code>
     * uses an <code>EntityResolver</code> to load the XLIFF DTD from a
     * local directory, as opposed to the location on the internet defined
     * in the doctype of the XLIFF file (This avoids any potential problems
     * with proxy servers and speeds up loading). This DTD is then used to
     * validate the XLIFF file.</p>
     *
     * @param reader The input XLIFF to be transformed.
     * @param out The resulting TMX from the transformation.
     * @exception XliffToTmxTransformerException Description of the
     *      Exception
     */
    public void doTransform(Reader reader, Writer out)
        throws XliffToTmxTransformerException {
       
        try {
                    
            
            logger.log(Level.FINEST, "Starting transformation");            
            javax.xml.parsers.SAXParserFactory factory =
                javax.xml.parsers.SAXParserFactory.newInstance();
                
            logger.log(Level.FINEST, "SAX Parser Created");
            factory.setValidating(true);
            
            logger.log(Level.FINEST, "SAX Parser set to validating");
            factory.setNamespaceAware(true);
                        
            logger.log(Level.FINEST, "SAX Parser set to namespace aware");
            xmlReader = factory.newSAXParser().getXMLReader();
            
            logger.log(Level.FINEST, "XMLReader created");
            xmlReader.setErrorHandler(this.getDefaultErrorHandler());
            
            logger.log(Level.FINEST, "Error handler set");
            xmlReader.setEntityResolver(resolver);
            
            logger.log(Level.FINEST, "Entity Reolver set");
            Source source = new SAXSource(xmlReader,
                new InputSource(reader));
                
            logger.log(Level.FINEST, "SAX Source created");
            Result result = new StreamResult(out);
            
            logger.log(Level.FINEST, "Result stream created");
            Transformer transformer = templates.newTransformer();
            
            logger.log(Level.FINEST, "Transformer created");
            transformer.transform(source, result);
            
            logger.log(Level.FINEST, "transformation completed");
            
        } catch (javax.xml.transform.TransformerConfigurationException ex) {
            logger.log(Level.FINE,
                "Could not configure the XSLT transformer", ex);
            throw new XliffToTmxTransformerException();
        } catch (NullPointerException ex) {
            logger.log(Level.FINE, "Count not parse the XLIFF file", ex);
            throw new XliffToTmxTransformerException(ex.getMessage());
        } catch (SAXException ex) {
            logger.log(Level.FINE, "Could not parse the XLIFF file", ex);
            throw new XliffToTmxTransformerException(ex.getMessage());
        } catch (javax.xml.transform.TransformerException ex) {
            logger.log(Level.FINE,
                "Could not carry out the XSLT Transformation", ex);
            throw new XliffToTmxTransformerException();
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            logger.log(Level.FINE, 
                "Could not configure the XSLT parser", ex);
            throw new XliffToTmxTransformerException();
        }

    }


    /**
     * Creates default error handler used by the <code>XMLReader</code>.
     *
     * @return org.xml.sax.ErrorHandler implementation
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return
            new ErrorHandler() {
                public void error(SAXParseException ex)
                     throws SAXException {
                    if (context.isEmpty()) {
                        logger.log(Level.FINE, "Missing DOCTYPE.");
                    }
                    throw ex;
                }


                public void fatalError(SAXParseException ex)
                     throws SAXException {
                    throw ex;
                }


                public void warning(SAXParseException ex)
                     throws SAXException {
                    // ignore
                }
            };
    }

}

