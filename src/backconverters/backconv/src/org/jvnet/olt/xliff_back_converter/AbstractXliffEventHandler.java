
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * TypeExtractingXliffEventHandler.java
 *
 * Created on June 21, 2004, 2:29 PM
 */

package org.jvnet.olt.xliff_back_converter;

/** This class implements a heap of no operation methods. It is intented to be 
 * used as a base class for other classes that only need to handle a few of the 
 * XLIFF events.
 * @author  jc73554
 */
public abstract class AbstractXliffEventHandler implements XliffHandler {
    
    /** Creates a new instance of TypeExtractingXliffEventHandler */
    public AbstractXliffEventHandler() {
    }
    
    /** Does nothing */
    public void end_alt_trans() {
    }
    
    /** Does nothing */
    public void end_bin_source() {
    }
    
    /** Does nothing */
    public void end_bin_target() {
    }
    
    /** Does nothing */
    public void end_bin_unit() {
    }
    
    /** Does nothing */
    public void end_body() {
    }
    
    /** Does nothing */
    public void end_bpt() {
    }
    
    /** Does nothing */
    public void end_context_group() {
    }
    
    /** Does nothing */
    public void end_count_group() {
    }
    
    /** Does nothing */
    public void end_ept() {
    }
    
    /** Does nothing */
    public void end_file() {
    }
    
    /** Does nothing */
    public void end_g() {
    }
    
    /** Does nothing */
    public void end_glossary() {
    }
    
    /** Does nothing */
    public void end_group() {
    }
    
    /** Does nothing */
    public void end_header() {
    }
    
    /** Does nothing */
    public void end_it() {
    }
    
    /** Does nothing */
    public void end_mrk() {
    }
    
    /** Does nothing */
    public void end_ph() {
    }
    
    /** Does nothing */
    public void end_phase() {
    }
    
    /** Does nothing */
    public void end_phase_group() {
    }
    
    /** Does nothing */
    public void end_prop_group() {
    }
    
    /** Does nothing */
    public void end_reference() {
    }
    
    /** Does nothing */
    public void end_skl() {
    }
    
    /** Does nothing */
    public void end_source() {
    }
    
    /** Does nothing */
    public void end_sub() {
    }
    
    /** Does nothing */
    public void end_target() {
    }
    
    /** Does nothing */
    public void end_trans_unit() {
    }
    
    /** Does nothing */
    public void end_xliff() {
    }
    
    /** Does nothing */
    public void handle_bpt(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_bx(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_context(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_count(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_ept(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_ex(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_external_file(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_g(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_internal_file(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_it(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_mrk(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_note(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_ph(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_prop(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_source(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_sub(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_target(java.lang.String data, org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void handle_x(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_alt_trans(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_bin_source(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_bin_target(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_bin_unit(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_body(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_bpt(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_context_group(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_count_group(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_ept(org.xml.sax.Attributes meta) {
    }
    
    /** Does Nothing */
    public void start_file(org.xml.sax.Attributes meta) {        
    }
    
    /** Does nothing */
    public void start_g(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_glossary(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_group(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_header(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_it(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_mrk(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_ph(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_phase(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_phase_group(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_prop_group(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_reference(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_skl(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_source(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_sub(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_target(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_trans_unit(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void start_xliff(org.xml.sax.Attributes meta) {
    }
    
    /** Does nothing */
    public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void endDocument() throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void endElement(String namespaceURI, String localName, String qName) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void endPrefixMapping(String prefix) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void ignorableWhitespace(char[] ch, int start, int length) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void processingInstruction(String target, String data) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void setDocumentLocator(org.xml.sax.Locator locator) {
    }
    
    /** Does nothing */
    public void skippedEntity(String name) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void startDocument() throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
    }
    
    /** Does nothing */
    public void startPrefixMapping(String prefix, String uri) throws org.xml.sax.SAXException {
    }
    
}
