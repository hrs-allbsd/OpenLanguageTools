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
 * TMXFormatter.java
 *
 * Created on November 9, 2003, 12:02 PM
 */

package org.jvnet.olt.filters.software;
import java.io.*;
import java.util.*;
// we're importing all the formatting types
// we could possibly try to be clever, and attempt
// to markup every message as html -=- if
// the parser failed, it would mean we had a non-html
// message. This could really help out the fuzzy match
// quality of software messages containing html text-
// not sure if this is worth the trouble though.

import org.jvnet.olt.format.printf.*;
import org.jvnet.olt.format.messageformat.*;
import org.jvnet.olt.format.*;
import org.jvnet.olt.io.*;



/**
 * This code is quite similar to the SegmenterFormatters that are used in the various
 * different file filters that SunTrans uses, but this does not implement the SegmenterFormatter
 * interface, since it's used for writing TMX files, where we're typically writing two
 * segments at a time. For now, we're limiting this class to outputting bi-language
 * TMX files (though the spec can cope with many). For clarity, we're using similar methods
 * as the TmxSegmenterFormatter, to ease the burden of maintenance for these classes.
 * (much of it is copied and pasted - sorry)
 * <br>
 * The easy thing to do, would be to limit this code to the software message file
 *
 * @author  timf
 */
public class TMXFormatter {

    private Writer tmxWriter;

    // initialising these, so that if we forget to set them by mistake, they make an invalid tmx file
    private String language = "<<\"\">>";
    private String dataType = "<<\"\">>";
    private String note = "<<\"\">>";
    private int transUnitId = 0;

    private HashMap m_hashFormatWrappers;
    private HashMap m_hashAvailableWrappers;

    private static final int PO = 5;
    private static final int MSG = 6;
    private static final int PROPERTIES = 7;
    private static final int JAVA = 8;


    /** Creates a new instance of TMXFormatter */
    public TMXFormatter(String dataType, String language, String sourceFileName, Writer tmxWriter) throws TMXFormatterException {
        // This hash maps from the TMX file type, to the correct
        /// formatter used for that type
        m_hashFormatWrappers = new HashMap();
        m_hashAvailableWrappers = new HashMap();
        m_hashAvailableWrappers.put("MSG",new Integer(MSG));
        m_hashAvailableWrappers.put("PO", new Integer(PO));
        m_hashAvailableWrappers.put("JAVA", new Integer(JAVA));
        m_hashAvailableWrappers.put("PROPERTIES",new Integer(PROPERTIES));
        try {
            this.tmxWriter = tmxWriter;
            this.language = language;
            this.dataType = dataType;
            System.out.println("Datatype is "+dataType);
            printTmxHeader(language, sourceFileName);
        }  catch (java.io.IOException e){
            throw new TMXFormatterException("Problem while writing segment " + e.getMessage());
        }
    }


    /** This method gets called to finish the writing of the TMX file
     *
     * @throws TMXFormatterException if there was some problem encountered during the method.
     *
     *
     */
    public void flush() throws TMXFormatterException {
        try {
            tmxWriter.write("</body>\n"+
            "</tmx>\n");
            tmxWriter.flush();
        }  catch (java.io.IOException e){
            throw new TMXFormatterException("Problem while writing segment " + e.getMessage());
        }
    }

    /** This method write the segment.
     * @param segment the segment to be written
     * @throws TMXFormatterException if there was some problem writing this segment.
     */
    public void writeTU(String srcSeg, String srcLang, String trgSeg, String trgLang, String msgid, String type, String comment) throws TMXFormatterException {
        writeTU(srcSeg, srcLang, trgSeg, trgLang, msgid, type, null, comment);
    }


    /** This method write the segment.
     * @param segment the segment to be written
     * @throws TMXFormatterException if there was some problem writing this segment.
     */
    public void writeTU(String srcSeg, String srcLang, String trgSeg, String trgLang, String msgid, String type, String domain, String comment) throws TMXFormatterException {
        try {
            if (msgid.length() == 0){
                throw new TMXFormatterException("Message id was 0 characters in length !");
            }
            // we probably shouldn't be throwing these
            if (srcSeg.length() == 0){
                System.err.println("Source segment was 0 characters in length !");
                //throw new TMXFormatterException("Source segment was 0 characters in length !");
            }
            if (trgSeg.length() == 0){
                System.err.println("Target segment was 0 characters in length !");
                //throw new TMXFormatterException("Target segment was 0 characters in length !");
            }
            transUnitId++;

            tmxWriter.write("<tu tuid=\"a"+transUnitId+"\" datatype=\""+type+"\">\n"+
            "    <prop type=\"SunTrans::MessageKey\">"+wrapTagsAndXMLChars(msgid)+"</prop>\n");

            // write the domain information
            if (domain != null) {
                tmxWriter.write("    <prop type=\"SunTrans::SoftFile\">" + wrapXMLChars(domain) + "</prop>\n");
            }

            // write the source language tuv
            tmxWriter.write("    <tuv xml:lang=\""+srcLang+"\">\n");
            tmxWriter.write("        <seg>"+wrapTagsAndXMLChars(srcSeg)+"</seg>\n");
            tmxWriter.write("    </tuv>\n");

            // now write the target langauge tuv
            tmxWriter.write("    <tuv xml:lang=\""+trgLang+"\">\n");
            if (comment != null){
                tmxWriter.write("        <note>"+wrapTagsAndXMLChars(comment)+"</note>\n");
            }
            tmxWriter.write("        <seg>"+wrapTagsAndXMLChars(trgSeg)+"</seg>\n");
            tmxWriter.write("    </tuv>\n");
            // end the trans unit
            tmxWriter.write("</tu>\n");
        } catch (java.io.IOException e){
            throw new TMXFormatterException("Problem while writing segment " + e.getMessage());
        }

    }

    private void printTmxHeader(String srcLang, String filename) throws java.io.IOException {
        tmxWriter.write("<?xml version=\"1.0\" ?>\n"+
        "<!DOCTYPE tmx SYSTEM \"tmx13.dtd\">\n"+
        "<tmx version=\"1.3\">\n"+
        "<header adminlang=\""+srcLang+"\" srclang=\""+srcLang+"\" o-tmf=\"xliff\" "+
        "segtype=\"sentence\" creationtoolversion=\"Pre FCS\" "+
        "creationtool=\"SunTrans 2 Software To TMX Converter\" datatype=\""+this.dataType+"\">\n"+
        // we probably want to add a prop element in here to give a hint as to the filename,
        // and other selection criteria -- something like :
        // <prop type="SunTrans::DocFile">foo.html</prop>
        "</header>\n"+
        "<body>\n");
    }

    /**
     * This method takes in characters in the string, and w converts them
     * by trying to pass them through John's Tmx formatting wrapper (which puts tags inside
     * bpt ept tags correctly. Should this fail for any reason, we default to passing
     * them through JohnC's HTMLEscapeFilterReader. What this does, is
     * convert ampersands, less-than and greater-than characters to an SGML/XML friendly
     * format using the &amp;amp; &amp;lt; and &amp;gt; entities -- not as useful to
     * Tmx-aware editors, but at least it will always produce valid XML.
     */
    private String wrapTagsAndXMLChars(String string) throws java.io.IOException {
        String output = string;
        try {
            // next, based on the type, we can get a FormatWrapper...

                // wrap the formatting (including any "dontsegment" protected text)
                output = wrapFormatting(output, this.dataType, true);
                // finally, remove protection from the dontsegment text (which has been enclosed
                // in <it> tags by the format wrapper)
        } catch (UnsupportedFormatException e){
            System.err.println("Unable to convert " + string +" to an Tmx representation since it's an unsupported format : " + e.getMessage());
            // we still have to put it in an "XML-safe" representation
            BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));
            StringWriter writer= new StringWriter();
            int i;
            while ((i = buf.read()) != -1){
                writer.write(i);
            }
            output = writer.toString();

        } catch (InvalidFormattingException e){
            System.err.println("Unable to convert " + string +" to an Tmx representation : " + e.getMessage());
            // for some reason, we weren't able to convert that character
            // we still have to put it in an "XML-safe" representation
            BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));
            StringWriter writer= new StringWriter();
            int i;
            while ((i = buf.read()) != -1){
                writer.write(i);
            }
            output = writer.toString();
        }
        return output;
    }


    /**
     * Do simple XML character wrapping, rather than try to wrap tags
     */
    private String wrapXMLChars(String string) throws java.io.IOException {
        BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));
        StringWriter writer= new StringWriter();
        int i;
        while ((i = buf.read()) != -1){
            writer.write(i);
        }
        return writer.toString();
    }

    // FIXME
    /** The below code is stolen from the TM tool's TmxOutputGenerator - we
     *  should at some point move that code into the Tmx utilities I think.
     *  Unfortunately, the tm tool code depends on this code in the filters, so we
     *  can't build that first, and simply use the methods from that class (it
     *  won't have been built yet) - so I'm copying & pasting that code : I know
     *  this is wrong and I apologise for it !
     */
    protected String wrapFormatting(String text, String type, boolean sourceText) throws UnsupportedFormatException, InvalidFormattingException {
        if(text == null) { return ""; }

        //  Get the correct format wrapper based on the type.
        FormatWrapper wrapper = getFormatWrapper(type, sourceText);
        String wrapped = "";
        try{
            wrapped = wrapper.wrapFormatting(text);
        } catch (Throwable e){
            // some bad formatting was found (we catch a Throwable here
            // because the blaML formats all use a javacc parser, which
            // throws Errors on getting a lexical err.
            throw new InvalidFormattingException("The formatting for segment _" + text +"_ is invalid");
        }
        //  Return the wrapped string.
        return wrapped;
    }

    // FIXME
    /** The below code is stolen from the TM tool's TmxOutputGenerator - we
     *  should at some point move that code into the xliff/Tmx utilities I think.
     *  Unfortunately, the tm tool code depends on this code in the filters, so we
     *  can't build that first, and simply use the methods from that class (it
     *  won't have been built yet) - so I'm copying & pasting that code : I know
     *  this is wrong and I apologise for it !
     *
     */
    protected FormatWrapper getFormatWrapper(String type, boolean sourceText) throws UnsupportedFormatException {
        //  Find or create an appropriate FormatWrapper object.
        FormatWrapper ex;
        if(m_hashFormatWrappers.containsKey(type)) {
            ex = (FormatWrapper) m_hashFormatWrappers.get(type);
            if(ex != null)
            { return ex; }
        }

        if(m_hashAvailableWrappers.containsKey(type)) {
            Integer code = (Integer) m_hashAvailableWrappers.get(type);
            int iExtract = code.intValue();
            switch(iExtract) {
                // we're only doing software messages here.
                case MSG:
                case PO:
                    ex = new PrintfFormatWrapper(true);
                    m_hashFormatWrappers.put(type, ex);
                    return ex;
                case PROPERTIES:
                case JAVA:
                    ex = new MessageFormatWrapper(true);
                    m_hashFormatWrappers.put(type,ex);
                    return ex;

                default:
                    throw new UnsupportedFormatException("Format is unsupported : " + type);
            }
        }
        else {
            throw new UnsupportedFormatException("Format is unsupported : " + type);
        }
    }

}