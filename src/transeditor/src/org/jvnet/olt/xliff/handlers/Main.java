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
 * Main.java
 *
 * Created on April 7, 2005, 6:40 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.xliff.handlers;

import java.io.FileReader;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author boris
 */
public class Main {
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /** Creates a new instance of Main */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().run();
    }

    void run() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false); //the code was generated according DTD
            factory.setNamespaceAware(true); //the code was generated according DTD

            SAXParser saxParser = factory.newSAXParser();
            saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

            XMLReader rdr = saxParser.getXMLReader();
            DefaultHandler hndlr = parser();
            rdr.setContentHandler(hndlr);

            //rdr.setErrorHandler(hndlr);
            //rdr.setEntityResolver(hndlr);
            //XMLReader reader = saxParser.getXMLReader();
            //reader.
            //saxParser.parse(new InputSource(new FileReader("tim.xlf")),new Parser());
            rdr.parse(new InputSource(new FileReader("/home/boris/tmp/abcdef/xmltest/1.xml")));
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
        }
    }

    ParserX parser() {
        ParserX x = new ParserX();
        x.addHandler("/xliff/context-group/", new XHandler());

        return x;
    }
}
