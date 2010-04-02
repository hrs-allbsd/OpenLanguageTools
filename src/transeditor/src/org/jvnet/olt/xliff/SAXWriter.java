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
package org.jvnet.olt.xliff;

import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*
 * SAXWriter2.java
 *
 * Created on February 28, 2005, 2:33 PM
 */
import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.xliff.handlers.ParserX;
import org.jvnet.olt.xliff.handlers.Handler;

import org.jvnet.olt.xliff.writer.handlers.Context;
import org.jvnet.olt.xliff.writer.handlers.FileHandler;
import org.jvnet.olt.xliff.writer.handlers.HeaderHandler;
import org.jvnet.olt.xliff.writer.handlers.NoteHandler;
import org.jvnet.olt.xliff.writer.handlers.SourceHandler;
import org.jvnet.olt.xliff.writer.handlers.TargetHandler;
import org.jvnet.olt.xliff.writer.handlers.TransUnitHandler;
import org.jvnet.olt.xliff.writer.handlers.XLIFFHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 *
 * @author boris
 */
public class SAXWriter {
    private Logger logger = Logger.getLogger(SAXWriter.class.getName());
    private Map srcChangeSet = new HashMap();
    private Map tgtChangeSet = new HashMap();
    private String targetLanguage;
    private TrackingComments comments;
    private Version version;

    public SAXWriter(Version v) {
        if (v == null) {
            throw new NullPointerException("version is null");
        }

        this.version = v;
    }


    public void write(java.io.Reader reader, java.io.Writer writer,boolean autosave) throws IOException, SAXException {
        writer.write("<?xml version=\"1.0\" ?>\n");

        if (version.isXLIFF10()) {
            writer.write(Constants.DOCTYPE_XLIFF_1_0 + "\n");
        }

        XMLDumper dumper = new XMLDumper(writer);

        logger.finest("About to save");
        logger.finest("Source change set:"+srcChangeSet);
        logger.finest("Target change set:"+tgtChangeSet);        
        
        
        Context ctx = new Context(dumper, version);

        ctx.setSrcChangeSet(autosave ? new HashMap(srcChangeSet) : srcChangeSet);
        ctx.setTgtChangeSet(autosave ? new HashMap(tgtChangeSet) : tgtChangeSet);

        ctx.setTargetLang(targetLanguage);
        ctx.setTrackingComments(comments);

        ParserX px = new ParserX();
        px.addHandler("/xliff", new XLIFFHandler(ctx));
        px.addHandler("/xliff/file", new FileHandler(ctx));
        px.addHandler("/xliff/file/header", new HeaderHandler(ctx));

        Handler tuHandler = new TransUnitHandler(ctx);
        Handler srcHandler = new SourceHandler(ctx);
        Handler tgtHandler = new TargetHandler(ctx);
        Handler noteHandler = new NoteHandler(ctx, false);
        
        px.addHandler("/xliff/file/body/trans-unit", tuHandler);
        px.addHandler("/xliff/file/body/trans-unit/source", srcHandler);
        px.addHandler("/xliff/file/body/trans-unit/target", tgtHandler);
        px.addHandler("/xliff/file/body/trans-unit/note", noteHandler);

        px.addHandler("/xliff/file/body/group/.*trans-unit/source", srcHandler);
        px.addHandler("/xliff/file/body/group/.*trans-unit/target", tgtHandler);
        px.addHandler("/xliff/file/body/group/.*trans-unit/note", noteHandler);

        px.addHandler("/xliff/file/body/group/.*trans-unit", new TransUnitHandler(ctx));
        px.addHandler("/xliff/file/body/group/.*trans-unit/source", new SourceHandler(ctx));
        px.addHandler("/xliff/file/body/group/.*trans-unit/target", new TargetHandler(ctx));
        px.addHandler("/xliff/file/body/group/.*trans-unit/note", new NoteHandler(ctx, false));

        Map uriMapping = new HashMap();
        if (version.isXLIFF11()) {
            uriMapping.put("urn:oasis:names:tc:xliff:document:1.1", "");
        } else if (version.isXLIFF12()) {
            uriMapping.put("urn:oasis:names:tc:xliff:document:1.2", "");
        }
        uriMapping.put("http://www.w3.org/XML/1998/namespace", "xml");

        px.setPrefixMap(uriMapping);

        try {
            Backend backend = Backend.instance();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating( ! version.isXLIFF10() && backend.getConfig().isBFlagValidateXLIFF() ); //disable Validation for XLIFF 1.0
            factory.setNamespaceAware(true);

            SAXParser saxParser = factory.newSAXParser();

            if ( factory.isValidating() ) {
                if (version.isXLIFF11()) {
                    saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                    saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                            "/xsd/xliff-core-1.1.xsd");
                } else if (version.isXLIFF12_strict()) {
                    saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                    saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                            "/xsd/xliff-core-1.2-strict.xsd");
                } else if (version.isXLIFF12_transitional()) {
                    saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                    saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                            "/xsd/xliff-core-1.2-transitional.xsd");
                }
            }

            XMLReader sr = saxParser.getXMLReader();
            sr.setEntityResolver(XLIFFEntityResolver.instance());
            sr.setContentHandler(px);
            sr.setErrorHandler(px);

            sr.parse(new InputSource(reader));
        } catch (ParserConfigurationException pce) {
            //TODO add some
            throw new SAXException(pce);
        } finally {

            
            logger.finest("Autosave:"+autosave);
            logger.finest("Source change set (unless autosave should be clean):"+srcChangeSet);
            logger.finest("Target change set (unless autosave should be clean):"+tgtChangeSet);        
            
            //srcChangeSet.clear();
            //tgtChangeSet.clear();
            targetLanguage = null;

            comments = null;
        }
    }

    public void saveTargetLanguageCode(String strTgtLanInput) {
        targetLanguage = strTgtLanInput;
    }

    public void saveTargetSentence(XLIFFSentence aSentence) {
        tgtChangeSet.put(aSentence.getTransUnitId(), aSentence);
    }

    public void saveSourceSentence(XLIFFSentence aSentence) {
        srcChangeSet.put(aSentence.getTransUnitId(), aSentence);
    }

    public void saveComments(TrackingComments tc) {
        this.comments = tc;
    }
}
