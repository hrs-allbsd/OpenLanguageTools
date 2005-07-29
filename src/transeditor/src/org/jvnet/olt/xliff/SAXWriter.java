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
import org.jvnet.olt.xliff.handlers.ParserX;
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
            writer.write(Constants.DOCTYPE_XLIFF_1_0);
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
        px.addHandler("/xliff/file/body/trans-unit", new TransUnitHandler(ctx));
        px.addHandler("/xliff/file/body/trans-unit/source", new SourceHandler(ctx));
        px.addHandler("/xliff/file/body/trans-unit/target", new TargetHandler(ctx));
        px.addHandler("/xliff/file/body/trans-unit/note", new NoteHandler(ctx, false));

        Map uriMapping = new HashMap();
        uriMapping.put("urn:oasis:names:tc:xliff:document:1.1", "");
        uriMapping.put("xml", "xml");

        px.setPrefixMap(uriMapping);

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser saxParser = factory.newSAXParser();

            if (version.isXLIFF11()) {
                saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
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
