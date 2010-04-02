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

/*
 * XsltProcessorRunnable.java
 *
 * Created on August 13, 2004, 3:08 PM
 */

package org.jvnet.olt.xsltrun;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/** This Runnable reads from the reader, applies an XSLT transfromation and writes
 * to the Writer.
 * @author  jc73554
 */
public class XsltProcessorRunable implements java.lang.Runnable {
    
    private Reader reader;
    private Writer writer;
    private XsltStylesheet stylesheet;
    private EntityResolver resolver;
    
    /** Creates a new instance of XsltProcessorRunnable */
    public XsltProcessorRunable(XsltStylesheet stylesheet, EntityResolver resolver) {
        this.resolver = resolver;
        this.stylesheet = stylesheet;
    }
    
    public void run() {
        //  Create transformer
        Transformer transformer = null;
        try {
            transformer = stylesheet.getTransformer();
        }
        catch(Exception ex) {
            System.err.println("Error in thread: " +Thread.currentThread().getName());
            ex.printStackTrace();
            return;   //  Can't make a transformer: get out of here!
        }
        
        //  Apply the transform
        try {
            //  Build a parser factory.
            SAXParserFactory factory = SAXParserFactory.newInstance();
                
            factory.setValidating(true);
            factory.setNamespaceAware(true);
                        
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();                        
            xmlReader.setEntityResolver(resolver);
                                   
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            Result result = new StreamResult(writer);
            
            transformer.transform(source,  result);
            writer.flush();
            writer.close();
            reader.close();
        }
        catch(Exception ex) {
            System.err.println("Error in thread: " +Thread.currentThread().getName());
            ex.printStackTrace();
        }
    }
    
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    
    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
