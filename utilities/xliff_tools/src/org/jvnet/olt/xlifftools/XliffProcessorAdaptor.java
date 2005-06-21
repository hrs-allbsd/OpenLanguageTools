
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xlifftools;

import org.jvnet.olt.xliffparser.XliffProcessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

import java.util.logging.Logger;

/**
 * User: boris
 * Date: Nov 8, 2004
 * Time: 2:31:18 PM
 */
public class XliffProcessorAdaptor implements XliffProcessor {
    Logger logger = Logger.getLogger(getClass().getName());

    public XliffProcessorAdaptor() {
    }


    public void start_mrk(Attributes meta) throws SAXException {
        logger.finer("start_mrk:" + meta);
    }

    public void handle_mrk(String data, Attributes meta) throws SAXException {
        logger.finer("handle_mrk:" +data+","+ meta);

    }

    public void end_mrk() throws SAXException {
        logger.finer("end_mrk:");

    }

    public void start_bin_unit(Attributes meta) throws SAXException {
        logger.finer("start_bin_unit:" + meta);

    }

    public void end_bin_unit() throws SAXException {
        logger.finer("end_bin_unit");

    }

    public void start_sub(Attributes meta) throws SAXException {
        logger.finer("start_sub:" + meta);

    }

    public void handle_sub(String data, Attributes meta) throws SAXException {
        logger.finer("handle_sub:" + data+","+meta);

    }

    public void end_sub() throws SAXException {
        logger.finer("end_sub" );

    }

    public void start_file(Attributes meta) throws SAXException {
        logger.finer("start_file" );

    }

    public void end_file() throws SAXException {
        logger.finer("start_mrk");

    }

    public void start_target(Attributes meta) throws SAXException {
        logger.finer("start_target:" + meta);

    }

    public void handle_target(String data, Attributes meta) throws SAXException {
        logger.finer("handle_target:" + data+"," + meta);

    }

    public void end_target() throws SAXException {
        logger.finer("end_target:");

    }

    public void handle_internal_file(String data, Attributes meta) throws SAXException {
        logger.finer("handle_internal_file:" +data+","+ meta);

    }

    public void start_xliff(Attributes meta) throws SAXException {
        logger.finer("start_xliff:" + meta);

    }

    public void end_xliff() throws SAXException {
        logger.finer("end_xliff:");

    }

    public void handle_bx(Attributes meta) throws SAXException {
        logger.finer("handle_bx:" + meta);

    }

    public void start_prop_group(Attributes meta) throws SAXException {
        logger.finer("start_mrk:" + meta);

    }

    public void end_prop_group() throws SAXException {
        logger.finer("end_prop_group:" );

    }

    public void handle_context(String data, Attributes meta) throws SAXException {
        logger.finer("handle_context:" + data+"," + meta);

    }

    public void start_skl(Attributes meta) throws SAXException {
        logger.finer("start_skl:" + meta);

    }

    public void end_skl() throws SAXException {
        logger.finer("end_skl:" );

    }

    public void start_trans_unit(Attributes meta) throws SAXException {
        logger.finer("start_trans_unit:" + meta);

    }

    public void end_trans_unit() throws SAXException {
        logger.finer("end_trans_unit:" );

    }

    public void start_phase(Attributes meta) throws SAXException {
        logger.finer("start_phase:" + meta);

    }

    public void end_phase() throws SAXException {
        logger.finer("end_phase:" );

    }

    public void start_body(Attributes meta) throws SAXException {
        logger.finer("start_body:" + meta);

    }

    public void end_body() throws SAXException {
        logger.finer("end_body:" );

    }

    public void start_group(Attributes meta) throws SAXException {
        logger.finer("start_group:" + meta);

    }

    public void end_group() throws SAXException {
        logger.finer("end_group:" );

    }

    public void handle_prop(String data, Attributes meta) throws SAXException {
        logger.finer("handle_prop:" + data+"," + meta);

    }

    public void start_ept(Attributes meta) throws SAXException {
        logger.finer("start_ept:" + meta);

    }

    public void handle_ept(String data, Attributes meta) throws SAXException {
        logger.finer("handle_ept:" + data+"," + meta);

    }

    public void end_ept() throws SAXException {
        logger.finer("end_ept:" );

    }

    public void start_header(Attributes meta) throws SAXException {
        logger.finer("start_header:" + meta);

    }

    public void end_header() throws SAXException {
        logger.finer("end_header:" );

    }

    public void start_ph(Attributes meta) throws SAXException {
        logger.finer("start_ph:" + meta);

    }

    public void handle_ph(String data, Attributes meta) throws SAXException {
        logger.finer("handle_ph:" + data+"," + meta);

    }

    public void end_ph() throws SAXException {
        logger.finer("end_ph:" );

    }

    public void start_g(Attributes meta) throws SAXException {
        logger.finer("start_g:" + meta);

    }

    public void handle_g(String data, Attributes meta) throws SAXException {
        logger.finer("handle_g:" + data+"," + meta);

    }

    public void end_g() throws SAXException {
        logger.finer("end_g:" );

    }

    public void handle_note(String data, Attributes meta) throws SAXException {
        logger.finer("handle_note:" +data+"," +  meta);

    }

    public void handle_ex(Attributes meta) throws SAXException {
        logger.finer("handle_ex:" + meta);

    }

    public void start_phase_group(Attributes meta) throws SAXException {
        logger.finer("start_phase_group:" + meta);

    }

    public void end_phase_group() throws SAXException {
        logger.finer("end_phase_group:" );

    }

    public void handle_external_file(Attributes meta) throws SAXException {
        logger.finer("handle_external_file:" + meta);

    }

    public void start_alt_trans(Attributes meta) throws SAXException {
        logger.finer("start_alt_trans:" + meta);

    }

    public void end_alt_trans() throws SAXException {
        logger.finer("end_alt_trans:");

    }

    public void start_context_group(Attributes meta) throws SAXException {
        logger.finer("start_context_group:" + meta);

    }

    public void end_context_group() throws SAXException {
        logger.finer("end_context_group:" );

    }

    public void start_bpt(Attributes meta) throws SAXException {
        logger.finer("start_bpt:" + meta);

    }

    public void handle_bpt(String data, Attributes meta) throws SAXException {
        logger.finer("handle_bpt:" + data+"," + meta);

    }

    public void end_bpt() throws SAXException {
        logger.finer("end_bpt:" );

    }

    public void start_it(Attributes meta) throws SAXException {
        logger.finer("start_it:" + meta);

    }

    public void handle_it(String data, Attributes meta) throws SAXException {
        logger.finer("handle_it:" +data+"," +  meta);

    }

    public void end_it() throws SAXException {
        logger.finer("end_it:" );

    }

    public void start_count_group(Attributes meta) throws SAXException {
        logger.finer("start_count_group:" + meta);

    }

    public void end_count_group() throws SAXException {
        logger.finer("end_count_group:" );

    }

    public void start_bin_source(Attributes meta) throws SAXException {
        logger.finer("start_bin_source:" + meta);

    }

    public void end_bin_source() throws SAXException {
        logger.finer("end_bin_source:" );

    }

    public void start_glossary(Attributes meta) throws SAXException {
        logger.finer("start_glossary:" + meta);

    }

    public void end_glossary() throws SAXException {
        logger.finer("end_glossary:" );

    }

    public void start_source(Attributes meta) throws SAXException {
        logger.finer("start_source:" + meta);

    }

    public void handle_source(String data, Attributes meta) throws SAXException {
        logger.finer("handle_source:" + data+"," + meta);

    }

    public void end_source() throws SAXException {
        logger.finer("end_source:" );

    }

    public void handle_count(String data, Attributes meta) throws SAXException {
        logger.finer("handle_count:" + data+"," + meta);

    }

    public void handle_x(Attributes meta) throws SAXException {
        logger.finer("handle_x:" + meta);

    }

    public void start_bin_target(Attributes meta) throws SAXException {
        logger.finer("start_bin_target:" + meta);

    }

    public void end_bin_target() throws SAXException {
        logger.finer("end_bin_target:" );

    }

    public void start_reference(Attributes meta) throws SAXException {
        logger.finer("start_reference:" + meta);

    }

    public void end_reference() throws SAXException {
        logger.finer("end_reference:");

    }

    public void endDocument() throws SAXException {
        logger.finer("endDocument:");

    }

    public void startDocument() throws SAXException {
        logger.finer("startDocument:" );

    }

    public void characters(char ch[], int start, int length) throws SAXException {
        logger.finer("characters" );

    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        logger.finer("ignorableWhitespace");

    }

    public void endPrefixMapping(String prefix) throws SAXException {
        logger.finer("endPrefixMapping" );

    }

    public void skippedEntity(String name) throws SAXException {
        logger.finer("skippedEntity:" );

    }

    public void setDocumentLocator(Locator locator) {
        logger.finer("setDocumentLocator:" );

    }

    public void processingInstruction(String target, String data) throws SAXException {
        logger.finer("processingInstruction:" );

    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        logger.finer("startPrefixMapping:" );

    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        logger.finer("endElement:" );

    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        logger.finer("startElement:" );

    }
}
