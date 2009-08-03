/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * SAXReader.java
 *
 * Created on April 19, 2005, 3:57 PM
 *
 */
package org.jvnet.olt.xliff;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jvnet.olt.editor.translation.Constants;
import org.jvnet.olt.editor.translation.Backend;
import org.jvnet.olt.xliff.handlers.ParserX;
import org.jvnet.olt.xliff.reader.handlers.AltTransContextGroupHandler;
import org.jvnet.olt.xliff.reader.handlers.AltTransHandler;
import org.jvnet.olt.xliff.reader.handlers.Context;
import org.jvnet.olt.xliff.reader.handlers.FileHandler;
import org.jvnet.olt.xliff.reader.handlers.GroupHandler;
import org.jvnet.olt.xliff.reader.handlers.NoteHandler;
import org.jvnet.olt.xliff.reader.handlers.PhaseHandler;
import org.jvnet.olt.xliff.reader.handlers.PropGroupHandler;
import org.jvnet.olt.xliff.reader.handlers.SourceContextGroupHandler;
import org.jvnet.olt.xliff.reader.handlers.SourceHandler;
import org.jvnet.olt.xliff.reader.handlers.TargetHandler;
import org.jvnet.olt.xliff.reader.handlers.TransUnitHandler;
import org.jvnet.olt.xliff.reader.handlers.XliffHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 *
 * @author boris
 */
public class SAXReader {
    private Version version;
    private String schemaLocation;

    /** Creates a new instance of SAXReader */
    public SAXReader(Version ver) {
        if (ver == null) {
            throw new NullPointerException("version may not be null");
        }

        this.version = ver;
    }
    public SAXReader(Version ver, String schemaLocation) {
        this (ver);
        this.schemaLocation = schemaLocation;
    }

    public Context parse(java.io.Reader r) throws IOException, SAXException, ParserConfigurationException {
        Context ctx = new Context(version);

        ParserX xp = new ParserX();

        TransUnitHandler tuHandler = new TransUnitHandler(ctx);
        SourceHandler srcHandler = new SourceHandler(ctx);
        TargetHandler tgtHandler = new TargetHandler(ctx);
        NoteHandler tuNote = new NoteHandler(ctx, NoteHandler.NOTE_TRANS_UNIT);
        NoteHandler altTuNote = new NoteHandler(ctx, NoteHandler.NOTE_ALT_TRANS_UNIT);
        SourceContextGroupHandler srcCtxHandler = new SourceContextGroupHandler(ctx);
        AltTransHandler altTransHandler = new AltTransHandler(ctx);
        AltTransContextGroupHandler altCtxHandler = new AltTransContextGroupHandler(ctx);
        PropGroupHandler propGrpHandler = new PropGroupHandler(ctx);

        xp.addHandler("/xliff", new XliffHandler(ctx));
        xp.addHandler("/xliff/file", new FileHandler(ctx));
        xp.addHandler("/xliff/file/header/phase-group/phase/", new PhaseHandler(ctx));
        xp.addHandler("/xliff/file/header/note", new NoteHandler(ctx, NoteHandler.NOTE_HEADER));

        xp.addHandler("/xliff/file/body/trans-unit", tuHandler);
        xp.addHandler("/xliff/file/body/trans-unit/source", srcHandler);
        xp.addHandler("/xliff/file/body/trans-unit/target", tgtHandler);
        xp.addHandler("/xliff/file/body/trans-unit/context-group", srcCtxHandler);
        xp.addHandler("/xliff/file/body/trans-unit/note", tuNote);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans", altTransHandler);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans/source", srcHandler);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans/target", tgtHandler);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans/prop-group", propGrpHandler);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans/note", altTuNote);
        xp.addHandler("/xliff/file/body/trans-unit/alt-trans/context-group", altCtxHandler);

        xp.addHandler("/xliff/file/body/group", new GroupHandler(ctx));
        xp.addHandler("/xliff/file/body/group/trans-unit", tuHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/source", srcHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/target", tgtHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/context-group", srcCtxHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/note", tuNote);
        xp.addHandler("/xliff/file/body/group/trans-unit/alt-trans", altTransHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/alt-trans/source", srcHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/alt-trans/target", tgtHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/alt-trans/prop-group", propGrpHandler);
        xp.addHandler("/xliff/file/body/group/trans-unit/alt-trans/context-group", altCtxHandler);

        Map uriMapping = new HashMap();
        if (version.isXLIFF11()) {
            uriMapping.put("urn:oasis:names:tc:xliff:document:1.1", "");
        } else if (version.isXLIFF12()) {
            uriMapping.put("urn:oasis:names:tc:xliff:document:1.2", "");
        }
        uriMapping.put("http://www.w3.org/XML/1998/namespace", "xml");

        xp.setPrefixMap(uriMapping);

        XMLReader xrdr = xmlReader();
        xrdr.setContentHandler(xp);
        xrdr.setEntityResolver(XLIFFEntityResolver.instance());
        xrdr.setErrorHandler(xp);
        xrdr.parse(new InputSource(r));

        return ctx;
    }

    private XMLReader xmlReader() throws SAXException, ParserConfigurationException {
        Backend backend = Backend.instance();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating( ! version.isXLIFF10() && backend.getConfig().isBFlagValidateXLIFF() ); //disable Validation for XLIFF 1.0
        factory.setNamespaceAware(true);

        SAXParser saxParser = factory.newSAXParser();

        if (factory.isValidating()) {
            if (version.isXLIFF11()) {
                saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                        "/resources/xliff-core-1.1.xsd");
            } else if (version.isXLIFF12_strict()) {
                saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                        "/resources/xliff-core-1.2-strict.xsd");
            } else if (version.isXLIFF12_transitional()) {
                saxParser.setProperty(Constants.JAXP_SCHEMA_LANGUAGE, Constants.W3C_XML_SCHEMA);
                saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
                                        "/resources/xliff-core-1.2-transitional.xsd");
            }
        }

        return saxParser.getXMLReader();
    }
}
