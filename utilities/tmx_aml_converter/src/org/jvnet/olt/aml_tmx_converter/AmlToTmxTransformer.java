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

package org.jvnet.olt.aml_tmx_converter;

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

import org.jvnet.olt.converterhandler.Resource;
/**
 * AmlToTmxTransformer is the class that uses the <a
 * href="http://xml.apache.org/xalan-j/trax.html">TrAX API</a> and XSLT to
 * transform a TMX file to AML.
 *
 * @author Charles Liu
 * @created December 5, 2002
 */
public class AmlToTmxTransformer {

  /**
  * Stores the XSLT templates for transformation
  */
  private Templates templates;

  /**
  * The XMLReader for parsing and validation of the AML file
  */
  private XMLReader xmlReader;

  /**
  * The context
  */
  private java.util.Stack context;

  /**
  * The Entity Resolver for resolving the location of the AML DTD
  */
  private EntityResolver resolver = null;

  private EntityResolver testResolver = null;

  /**
   * Constructor for the XliffToTmxTransformer object
   *
   * @param xslFileReader        The XSLT file used in the transformation
   * @param amlDTDReader         The AML DTD file reader
   * @param amlTestDTDReader     The AML DTD file reader to test the file whether contains <unaligned>
   * @exception AmlToTmxTransformerException Exception thrown if transformation fails.
   */
  public AmlToTmxTransformer(Reader xslFileReader, Reader amlDTDReader, Reader amlTestDTDReader)
      throws AmlToTmxTransformerException {

    resolver = new AmlEntityResolver(amlDTDReader);
    testResolver = new AmlEntityResolver(amlTestDTDReader);
    context = new java.util.Stack();
    try {
      TransformerFactory xformFactory  = TransformerFactory.newInstance();
      Source xsl = new StreamSource(xslFileReader);
      templates = xformFactory.newTemplates(xsl);

    } catch (TransformerException ex) {
      ex.printStackTrace();
      throw new AmlToTmxTransformerException();
    }

  }

  /**
   * Checks if all the segments are aligned with other
   *
   * @param reader The input AML to be checked.
   */
  public boolean isFullyAligned(Reader reader)
      throws AmlToTmxTransformerException {

    AmlXMLReader handler = null;
    try {
      javax.xml.parsers.SAXParserFactory factory =
          javax.xml.parsers.SAXParserFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);

      XMLReader parser = factory.newSAXParser().getXMLReader();
      handler = new AmlXMLReader();
      parser.setContentHandler(handler);
      parser.setErrorHandler(handler.getDefaultErrorHandler());
      parser.setEntityResolver(testResolver);

      InputSource inputSource = new InputSource(reader);
      inputSource.setSystemId(Resource.AMLDTDFileName);

      parser.parse(inputSource);

    } catch (AmlUnalignedFoundException ex) {
      return false;

    } catch (SAXException ex) {
      if(ex.getMessage().equals("Found unaligned segment")) {
        return false;
      }
      System.out.println("**Could not parse the AML file:"+ex);
      throw new AmlToTmxTransformerException(ex.getMessage());

    } catch (javax.xml.parsers.ParserConfigurationException ex) {
      System.out.println("Could not configure the XSLT parser:"+ex);
      throw new AmlToTmxTransformerException();

    } catch (java.io.IOException ex) {
      System.out.println("Problem accessing IO:"+ex);
      throw new AmlToTmxTransformerException();

    }
    return true;
  }


  /**
   * The <code>doTransform</code> carries out the transformation of the
   * AML from a reader object to the resulting TMX in a writer object.
   * <p>
   *
   * A <code>XMLReader</code> is used to parse the AML file, this <code>XMLReader</code>
   * uses an <code>EntityResolver</code> to load the AML DTD from a
   * local directory, as opposed to the location on the internet defined
   * in the doctype of the AML file (This avoids any potential problems
   * with proxy servers and speeds up loading). This DTD is then used to
   * validate the AML file.</p>
   *
   * @param reader The input AML to be transformed.
   * @param out The resulting TMX from the transformation.
   * @exception AmlToTmxTransformerException Description of the Exception
   */
  public void doTransform(Reader reader, Writer out)
      throws AmlToTmxTransformerException {

    try {
      javax.xml.parsers.SAXParserFactory factory =
          javax.xml.parsers.SAXParserFactory.newInstance();

      factory.setValidating(true);
      factory.setNamespaceAware(true);

      xmlReader = factory.newSAXParser().getXMLReader();
      xmlReader.setEntityResolver(resolver);
      xmlReader.setErrorHandler(this.getDefaultErrorHandler());

      Source source = new SAXSource(xmlReader,  new InputSource(reader));
      //set systemId
      source.setSystemId(Resource.AMLDTDFileName);

      Transformer transformer = templates.newTransformer();

      Result result = new StreamResult(out);
      transformer.transform(source, result);

    } catch (javax.xml.transform.TransformerConfigurationException ex) {
      System.out.println("Could not configure the XSLT transformer:"+ex);
      throw new AmlToTmxTransformerException();
    } catch (SAXException ex) {
      ex.printStackTrace();
      System.out.println("Could not parse the AML file:"+ex);
      throw new AmlToTmxTransformerException(ex.getMessage());
    } catch (javax.xml.transform.TransformerException ex) {
      ex.printStackTrace();
      System.out.println("Could not carry out the XSLT Transformation:"+ex);
      throw new AmlToTmxTransformerException();
    } catch (javax.xml.parsers.ParserConfigurationException ex) {
      System.out.println("Could not configure the XSLT parser:"+ex);
      throw new AmlToTmxTransformerException();
    }

  }
  /**
   * Creates default error handler used by the <code>XMLReader</code>.
   *
   * @return org.xml.sax.ErrorHandler implementation
   */
  protected ErrorHandler getDefaultErrorHandler() {
    return new ErrorHandler() {
      public void error(SAXParseException ex) throws SAXException {
        if (context.isEmpty()) {
          System.out.println("Missing DOCTYPE.");
        }
        throw ex;
      }
      public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
      }
      public void warning(SAXParseException ex) throws SAXException {
        // ignore
      }
    };
  }


}