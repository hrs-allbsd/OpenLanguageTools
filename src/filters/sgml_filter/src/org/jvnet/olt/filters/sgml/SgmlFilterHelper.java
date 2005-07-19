
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * SgmlFilterHelper.java
 *
 * Created on April 28, 2003, 10:39 AM
 */

package org.jvnet.olt.filters.sgml;

import org.jvnet.olt.filters.sgml.visitors.*;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import com.wutka.dtd.*;
//import org.jvnet.olt.parsers.SgmlTagParser.*;
import org.jvnet.olt.filters.segmenter_facade.*;
import org.jvnet.olt.parsers.tagged.*;
import org.jvnet.olt.filters.NonConformantSgmlDocFragmentParser.*;
import org.jvnet.olt.io.*;
import java.io.*;
import java.util.*;
/**
 * This is a small library of methods primarily used by the SgmlSegmenterVisitor, but
 * also by some other things inside the sgml filter class.
 * @author  timf
 */
public class SgmlFilterHelper {
    
    // stuff for different types of entity declaration we may come across
    public static final int ENTITYNAME=0;
    public static final int ENTITYVALUE=1;
    public static final int ENTITYTYPE=2;
    
    /** Creates a new instance of SgmlFilterHelper (this shouldn't have a constructor really) */
    public SgmlFilterHelper() {
    }
    
   
            
    /** This code calls John's normalising method below for all text that's not
     * wrapped in &lt;suntransxmlfilter&gt; tags
     * @param buffer the buffer containing text to be normalised
     * @throws SgmlFilterException if there was some problem during the normalisation process
     * @return a normalised version of the input string
     */
    protected static String normalise(StringBuffer buffer) throws SgmlFilterException {
        // System.out.println("Normalising " + buffer.toString()); 
        // we have to see how to do this.
        StringReader reader = new StringReader(buffer.toString());
        NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
        TextNormalisingVisitor visitor = new TextNormalisingVisitor();
        try {
            parser.parse();
            parser.walkParseTree(visitor,null);
        } catch (Throwable e){
            throw new SgmlFilterException("Exception trying to normalise text _"+buffer.toString()+"_ in sgml/xml/html parser " + e.getMessage());
        }
        return visitor.getText();
    }
    
    /* This code comes directly from org.jvnet.olt.parsers.SgmlDocFragmentParser.NewlineRemovingVisitor
     * written by John Corrigan. Nice code !
     */
    /** Written by JohnC, this does SGML whitespace normalisation of the input.
     * Detailed comments are included in the source code.
     * @param buffer Input containing text to be normalised.
     * @throws SgmlFilterException if an error was encounted during normalisation.
     * @return a normalised version of the input.
     */
    public static String sgmlNormalise(StringBuffer buffer) throws SgmlFilterException {
        StringBuffer bufOut = new StringBuffer();
        StringReader reader = new StringReader(buffer.toString());
        boolean removeWhitespace = true;
        final int DEFAULT = 0;
        final int WS = 1;
        final int CR_AFTER_WS = 2;
        final int CR = 3;
        
        int state = DEFAULT;
        
        //  State machine
        //
        //   +----+-------+-------+-------+--------+
        //   ||||||  WS   |  CR   |  DEF  | WS_CR  |
        //   +----+-------+-------+-------+--------+
        //   | ws |  WS   |  WS   |  WS   |  WS    |
        //   |    |  (ws) |  (ws) |  (ws) |  (ws)  |
        //   |    |   or  |   or  |   or  |   or   |
        //   |    |   ()  |  (' ')|  (' ')|    ()  |
        //   +----+-------+-------+-------+--------+
        //   | cr | WS_CR |  CR   |  CR   | WS_CR  |
        //   |    |   ()  |   ()  |   ()  |   ()   |
        //   +----+-------+-------+-------+--------+
        //   | ch |  DEF  |  DEF  |  DEF  |  DEF   |
        //   |    |   (ch)|(' ' + |  (ch) |  (ch)  |
        //   |    |       |   ch) |       |        |
        //   +----+-------+-------+-------+--------+
        //
        //   DEF = default state
        //   WS  = white space state
        //   ws  = white space character
        //   CR  = Carriage Return state
        //   cr  = Carriage Return character
        //   WS_CR  = carriage return after white space state
        //   ch  = Any character that is not ws or CR
        //
        //   The table represents the transitions that occur
        //   when the characters are read in when the system
        //   is in a given state. The top row represents the
        //   current state. The entries in the table represent
        //   the new states. The stuff in brackets is what is
        //   written out on transition to the new state.
        //
        int ch = 0;
        
        try {
            while((ch = reader.read()) != -1) {
                switch(ch) {
                    case (int) '\t':
                    case (int) ' ':
                        if(removeWhitespace) {
                            //  Normalize whitespace, i.e., only output the first
                            //  whitespace chracter. Ensure it is a space. This
                            //  will be the case unless we are in the states
                            //  WS or CR_AFTER_WS.
                            if( !( state == WS ||
                            state == CR_AFTER_WS) ) {
                                bufOut.append(' ');
                            }
                        }
                        else {
                            //  Leave whitespace alone if removeWhitespace is false.
                            bufOut.append((char) ch);
                        }
                        state = WS;
                        break;
                    case (int) '\n':
                    case (int) '\r':
                        if( state == WS ||
                        state == CR_AFTER_WS) {
                            state = CR_AFTER_WS;
                        }
                        else {
                            state = CR;
                        }
                        break;
                    default:
                        // handle other whitespaces, not mentioned in the above CASES
                        // (we treat them just as we do in the case of ' ' and '\t')
                        // ----------
                        if (Character.isWhitespace((char)ch)) {
                            if(removeWhitespace) {
                                //  Normalize whitespace, i.e., only output the first
                                //  whitespace chracter. Ensure it is a space. This
                                //  will be the case unless we are in the states
                                //  WS or CR_AFTER_WS.
                                if( !( state == WS ||
                                state == CR_AFTER_WS) ) {
                                    bufOut.append(' ');
                                }
                            }
                            else {
                                //  Leave whitespace alone if removeWhitespace is false.
                                bufOut.append((char) ch);
                            }                            
                        }
                        //----------
                        else if( state == CR ) { bufOut.append(' '); }
                        bufOut.append((char) ch);
                        state = DEFAULT;
                        break;
                }
            }
            //  Should we append a space if we end in the CR state?
            if( state == CR ) { bufOut.append('\n'); }
        }
        catch(IOException ioEx) {
            throw new SgmlFilterException("Caught io exception in normalise function");
        }
        return bufOut.toString();
    }
    
    /** returns any leading whitespace on a string. For example,
     * "  this is text" will return "  "
     * @param s string to be processed
     * @return any leading whitespace characters.
     */
    protected static String getLeadingWhitespace(String s){
        StringBuffer output = new StringBuffer();
        boolean writtenfirstnonspace = false;
        for (int i=0; i<s.length();i++){
            if (Character.isWhitespace(s.charAt(i)) && !writtenfirstnonspace){
                output.append(s.charAt(i));
            } else {
                writtenfirstnonspace = true;
            }
        }
        return output.toString();
    }
    
    /* returns the string minus any leading whitespace. For example,
     * "  this is text  " will return "this is text  "
     */
    /** This method strips any leading whitespace characters from the input string. It
     * is mainly used to determine what's "mid-segment-formatting".
     * @param s the input string to be processed
     * @return the input string with any leading whitespace characters removed.
     *
     */
    public static String stripLeadingWhitespace(String s){
        StringBuffer output = new StringBuffer();
        boolean writtenfirstnonspace = false;
        for (int i=0; i<s.length();i++){
            if (Character.isWhitespace(s.charAt(i)) && !writtenfirstnonspace){
                ;
            } else {
                output.append(s.charAt(i));
                writtenfirstnonspace = true;
            }
        }
        return output.toString();
    }
    
    /** gets trailing whitespace from the input string
     * @param s input string
     * @return any trailing whitespace found in the input string
     */
    protected static String getTrailingWhitespace(String s){
        StringBuffer output = new StringBuffer();
        boolean writtenfirstnonspace = false;
        for (int i=s.length()-1; i>=0;i--){
            if (Character.isWhitespace(s.charAt(i)) && !writtenfirstnonspace){
                output.insert(0,s.charAt(i));
            } else {
                writtenfirstnonspace = true;
            }
        }
        return output.toString();
    }
    
    /** removes trailing whitespace from the input string
     * @param s the input string
     * @return the input string, minus any trailing whitespace
     */
    protected static String stripTrailingWhitespace(String s){
        StringBuffer output = new StringBuffer();
        boolean writtenfirstnonspace = false;
        for (int i=s.length()-1; i>=0;i--){
            if (Character.isWhitespace(s.charAt(i)) && !writtenfirstnonspace){
                ;
            } else {
                output.insert(0,s.charAt(i));
                writtenfirstnonspace = true;
            }
        }
        return output.toString();
    }
    
    
    /** This is an attempt to convert character references as defined
     * in the sgml spec. It converts named character references as defined by the
     * current SegmenterTable, numeric character references (both decimal and hex) and
     * some special character references like &amp;#SPACE; and &amp#TAB; It does not do some
     * of the unusual things like &amp#RS;B
     *
     * In general, any entity or character reference that's not recognised will not be converted,
     * and preserved in the original text.
     *
     * A special note : we do not convert &amp; &lt; or &gt; (or
     * their numeric equivalence in decimal or hex, to make life easier
     * for people later on.)
     * <pre>
     * References recognised here are of the form :
     * &amp;#[number][end]
     * &amp;#x[hex number][end]
     * &amp;[word][end]
     * &amp;#SPACE; == ' '
     * &amp;#TAB; == '\t'
     *
     * where [end] is one of ';' '\n' '\r' ' ' '&lt;' '&gt;'
     * </pre><br>
     * 
     * @return The input string with character references processed as per description.
     * @param segmenterTable the segmenter table used to convert entities
     * @param s The input string
     * @throws SgmlFilterException if any errors were encountered during character reference conversion.
     */
    protected static String convertCharacterEntities(String s, SegmenterTable segmenterTable) throws SgmlFilterException {
        
        StringBuffer sourceBuf = new StringBuffer(s);
        StringBuffer outputBuf = new StringBuffer(s.length());
        StringBuffer entityBuf = new StringBuffer(8);
        StringBuffer eroBuf = new StringBuffer(3);
        /*
         * "amp" == '\u0026'
         * "lt" == '\u003c'
         * "gt" == '\u003e'
         
         * to be nice, we're normalising everything that's > < or & to
         * one representation
         */
        final int IN_AMP = 0;
        final int IN_HASH = 1;
        final int IN_HEXHASH = 2;
        final int DEFAULT = 4;
        int state = DEFAULT;
        
        for (int i=0; i < s.length(); i++){
            char c = sourceBuf.charAt(i);
            if (c == '&'){
                eroBuf.append(c);
                state = IN_AMP;
            } else if (c == '#'){
                if (state == IN_AMP){
                    eroBuf.append(c);
                    state = IN_HASH;
                }
                else outputBuf.append(c);
            } else if (c == 'x' || c == 'X'){
                if (state == IN_HASH){
                    eroBuf.append(c);
                    state = IN_HEXHASH;
                }
                else if (state == IN_AMP || state == IN_HASH || state == IN_HEXHASH){
                    entityBuf.append(c);
                } else {
                    outputBuf.append(c);
                }
            }
            else if (c == ';' || c == '<' || c == '>' || c == '\n' || c == '\r' || c== ' ' || c=='!'){
                // reached something that ends an entity definition : check to see if we're defining an entity
                if (state == IN_HASH || state == IN_HEXHASH || state == IN_AMP){
                    // in a decimal numeric entity
                    if (state == IN_HASH){
                        decodeNumericCharacterRef(c, entityBuf, outputBuf);
                        state = DEFAULT;
                        entityBuf = new StringBuffer(8);
                        eroBuf = new StringBuffer(3);
                    }
                    // in a hex numeric entity
                    else if (state == IN_HEXHASH){
                        decodeHexNumericCharacterRef(c, entityBuf, outputBuf);
                        state = DEFAULT;
                        entityBuf = new StringBuffer(8);
                        eroBuf = new StringBuffer(3);
                        
                    }
                    // in a character entity
                    else {
                        // look up the entity we've found
                        decodeCharacterRef(c, segmenterTable, entityBuf, outputBuf);
                        state = DEFAULT;
                        entityBuf = new StringBuffer(8);
                        eroBuf = new StringBuffer(3);
                        
                    }
                }
                // output the ; < > \r or \n character since we're not defining an entity
                else outputBuf.append(c);
                
            } else if (state == IN_AMP || state == IN_HASH || state == IN_HEXHASH){
                entityBuf.append(c);
            }
            else {
                outputBuf.append(c);
            }
        }
        // finally, check to see if we're in some unknown state at the last character
        switch (state){
            case IN_AMP:
                if (entityBuf.length() == 0){
                    outputBuf.append(eroBuf);
                }else {
                    decodeCharacterRef('&',segmenterTable, entityBuf, outputBuf);
                }
                break;
            case IN_HASH:
                if (entityBuf.length() == 0){
                    outputBuf.append(eroBuf);
                } else {
                    decodeNumericCharacterRef('#', entityBuf, outputBuf);
                }
                break;
            case IN_HEXHASH:
                if (entityBuf.length() == 0){
                    outputBuf.append(eroBuf);
                } else {
                    decodeHexNumericCharacterRef('x', entityBuf, outputBuf);
                }
                break;
        }
        
        return outputBuf.toString();
    }
    
    /** This converts a string buffer containing a numeric character reference
     * (like &#123;) into it's unicode equivalent;
     * @param c the end character of this entity, %,&lt;,! (eg. &amp;entity% &amp;entity< &amp;entity!, etc.)
     * @param entityBuf the buffer containing the input
     * @param outputBuf the buffer to put the output into
     * @throws NumberFormatException if it's not a numeric character reference.
     */
    protected static void decodeNumericCharacterRef(char c, StringBuffer entityBuf, StringBuffer outputBuf) throws java.lang.NumberFormatException {
        
        try {
            final int AMP = 38;
            final int LT = 60;
            final int GT = 62;
            
            int myint = (char)Integer.parseInt(entityBuf.toString());
            boolean converted = false;
            // check to see if it's one of the 'special' entities for xml
            switch (myint){
                case AMP:
                    outputBuf.append("&amp");
                    converted = true;
                    break;
                case GT:
                    outputBuf.append("&gt");
                    converted = true;
                    break;
                case LT:
                    outputBuf.append("&lt");
                    converted = true;
                    break;
            }
            if (converted){
                if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                    outputBuf.append((char)c);
                }
                else outputBuf.append(';');
            }
            if (!converted){
                char mychar = (char)myint;
                outputBuf.append(mychar);
                if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                    outputBuf.append((char)c);
                }
            }
            
        }catch (java.lang.NumberFormatException e){
            // it's a number, but we can't decode it, so write the original ent.
            // there's a possibility the string could be one of SPACE or TAB as defined
            // in the sgml spec.
            String str = entityBuf.toString();
            if (str.equals("SPACE")){
                outputBuf.append(' ');
            } else if (str.equals("TAB")){
                outputBuf.append('\t');
            } else {
                outputBuf.append('&');
                outputBuf.append(entityBuf.toString());
                if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                    outputBuf.append((char)c);
                }
                else outputBuf.append(';');
            }
        }
    }
    
    
    /** This converts a string buffer containing a hexadecimal numeric character reference
     * (like &#x0123;) into it's unicode equivalent;
     * @param c the end character of this entity, %,&lt;,! (eg. &amp;entity% &amp;entity< &amp;entity!, etc.)
     * @param entityBuf the buffer containing the input
     * @param outputBuf the buffer to put the output into
     */
    protected static void decodeHexNumericCharacterRef(char c, StringBuffer entityBuf, StringBuffer outputBuf) {
        
        try {
            
            final int AMP = 38;
            final int LT = 60;
            final int GT = 62;
            
            int myint = (char)Integer.parseInt(entityBuf.toString(),16);
            boolean converted = false;
            // check to see if it's one of the special characters for xml
            switch (myint){
                case AMP:
                    outputBuf.append("&amp");
                    converted = true;
                    break;
                case GT:
                    outputBuf.append("&gt");
                    converted = true;
                    break;
                case LT:
                    outputBuf.append("&lt");
                    converted = true;
                    break;
            }
            if (converted){
                if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                    outputBuf.append((char)c);
                }
                else outputBuf.append(';');
            }
            if (!converted){
                char mychar = (char)myint;
                outputBuf.append(mychar);
                if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                    outputBuf.append((char)c);
                }
            }
        } catch (java.lang.NumberFormatException e){
            // it's a number, but we can't decode it
            outputBuf.append('&');
            outputBuf.append(entityBuf.toString());
            if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                outputBuf.append((char)c);
            }
            else outputBuf.append(';');
        }
    }
    
    /** This converts a string buffer containing a named character reference
     * (like &entity;) into it's unicode equivalent;
     * @param segmenterTable the segmenter table that maps these names to values
     * @param c the end character of this entity, %,&lt;,! (eg. &amp;entity% &amp;entity< &amp;entity!, etc.)
     * @param entityBuf the buffer containing the input
     * @param outputBuf the buffer to put the output into
     */
    protected static void decodeCharacterRef(char c, SegmenterTable segmenterTable, StringBuffer entityBuf, StringBuffer outputBuf){
        
        
        Character character = segmenterTable.getEntityCharValue(entityBuf.toString());
        if (character != null){
            outputBuf.append(character.charValue());
            if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                outputBuf.append((char)c);
            }
        } else {
            // now, it's an unknown entity (wearing dark classes and hanging around with other government Spooks)
            outputBuf.append('&');
            outputBuf.append(entityBuf.toString());
            if (c == '<' || c == '>' || c == '\n' || c == '\r' || c == '!' || c == ' '){
                outputBuf.append((char)c);
            }
            else outputBuf.append(';');
        }
    }
    
    /** resolves a marked section flag
     * @param msFlag the name of the marked section
     * @param gvm a global variable manager containing information about the know marked section
     * flags.
     * @throws SgmlFilterException if there was some problem resolving this marked section
     * @return false if this is a marked section that resolves to IGNORE, true otherwise
     */
    public static boolean isIncludedMarkedSection(String msFlag, GlobalVariableManager gvm) throws SgmlFilterException {
        // first check to see if it's easily resolvable
        if (msFlag.equals("INCLUDE")){
            return true;
        } else if (msFlag.equals("IGNORE")) {
            return false;
        }
                
        String variable = gvm.resolveVariable(msFlag,"PARAMETER");
        if (variable.equals("INCLUDE")){
            return true;
        } else if (variable.equals("IGNORE")) {
            return false;
        }
        return true;
        
    }
    
    /** Converts a string to an XML-safe representation, by replacing &lt; with
     * &amp;lt; etc.
     * @param string string to be converted
     * @throws IOException if there was some problem converting the string
     * @return an xmlrepresentation of that string
     */
    protected static String wrapXMLChars(String string) throws java.io.IOException {
        BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));
        StringWriter writer= new StringWriter();
        int i;
        while ((i = buf.read()) != -1){
            writer.write(i);
        }
        return writer.toString();
    }
    
    /** This method is used to protect the input text from segmentation. It works, by
     * putting the input text into the attribute of a special tag. Since the segmenter
     * takes the contents of tags to be an atomic unit, it will not segment this text.
     *
     * This is done mostly to allow for marked sections, which contain segmentable text
     * but may be marked as "IGNORE" - we needed some way to prevent the entire marked
     * section from being segmented.
     *
     * The user can supply the name of an attribute which can hold the protected text,
     * which later on can be detected when removing the segmentation protection (so
     * we could have segmentation protection removers that takes different actions,
     * depending on the attribute that held the protected text)
     * @see RemoveSegmentationProtectionVisitor
     * @param string input text
     * @param attribute the attribute name to add the text to.
     * @throws IOException if there was some problem protecting the text
     * @return a string that has been protected
     */
    protected static String insertSegmentationProtection(String string, String attribute) throws java.io.IOException{
        // if we've been given nothing as input, return nothing.
        if (string.length() == 0){
            return "";
        }
        String wrapped = wrapXMLChars(string);
        return "<suntransxmlfilter "+attribute+"=\""+wrapped.replaceAll("\"","&quot;")+"\" />";
    }
    
    /** This method removes the special wrapped tag that we inserted before segmentation,
     * giving us the original text of the tag and is terribly messy. It returns an xliff
     * version of the text.
     * @see RemoveSEgmentationProtectionVisitor
     * @param string the string to remove the protection from
     * @param attribute the attribute that holds the protected text
     * @throws SgmlFilterException if there was some problem removing the protected text
     * @return the original string
     */
    protected static String removeSegmentationProtection(String string, String attribute) throws SgmlFilterException {
        // if we've been given no input, return nothing
        if (string.length() == 0){
            return "";
        }
        StringReader reader = new StringReader(string);
        RemoveSegmentationProtectionVisitor visitor = new RemoveSegmentationProtectionVisitor(attribute);
        NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
        try{
            parser.parse();
            parser.walkParseTree(visitor,null);
            String removed = visitor.getRemovedString();
            if (removed.length() == 0)
                return string;
            else return removed;
            
        } catch (Exception e){
            throw new SgmlFilterException("Exception trying to remove segmentation protection from " +
            string+" "+e.getMessage());
        }
        //System.out.println("Ain't doing nuttin");
        //return string;
    }
    
    protected static int countSegment(String segment, SegmenterTable segmenterTable, TagTable tagTable, String language, List tagList)  throws SgmlFilterException{
        return countSegment(segment, segmenterTable, tagTable, language, tagList, null);      
    }
    
    /** Wordcounts a segment
     * @param segment the segment to be wordcounted
     * @param segmenterTable the segmenter table that tells us which tags have translatable attributes
     * @param tagTable the tag table that describes this markup format
     * @param language the language used to do the wordcount
     * @param tagList a List containing Tag objects that give us more information abuot the tag than we could
     * get from the segment on it's own (including which namespace each tag is in)
     * @throws SgmlFilterException if there was some problem found during wordcount
     * @return the wordcount of the input segment
     */
    protected static int countSegment(String segment, SegmenterTable segmenterTable, TagTable tagTable, String language, List tagList, GlobalVariableManager gvm)  throws SgmlFilterException{
        String completeSegment = SgmlFilterHelper.removeSegmentationProtection(segment,"dontsegment");
        StringReader reader = new StringReader(completeSegment);
        NonConformantSgmlDocFragmentParser parser = new NonConformantSgmlDocFragmentParser(reader);
        AttributeCountNonConformantSgmlDocFragmentVisitor visitor =
        new AttributeCountNonConformantSgmlDocFragmentVisitor(segmenterTable,tagTable, language, tagList);
        int count = 0;
        try {
            parser.parse();
            parser.walkParseTree(visitor, null);
            count = visitor.getCount();
        }catch (Throwable e){
            e.printStackTrace();
            throw new SgmlFilterException("Exception trying to wordcount attributes from " + segment);
        }
        return count+wordCount(completeSegment, language, gvm);
    }
    
    
    public static int wordCount(String segment, String language) throws SgmlFilterException {
        return wordCount(segment,language, null);
    }
    
    /** This method returns a wordcount as computed by the SegmentStatsVisitor class
     * in the plaintext segmenter package. Note that words do not currently include numbers !
     * Also note that words don't include &nbsp; &gt; or &lt; entities !
     * @return a wordcount of the number of words in this segment.
     * @param language the language of the input text (so we can use the right segmenter to count
     * the words)
     * @param segment The segment to be counted.
     * @throws SgmlFilterException if some error was encountered while wordcounting (most probably coming from the
     * plaintext segmenter)
     */
    public static int wordCount(String segment, String language, GlobalVariableManager gvm) throws SgmlFilterException {
        StringReader reader = new StringReader(segment);
        SegmenterFacade segmenterFacade = new SegmenterFacade(reader, language);
        
        try {
            segmenterFacade.parseForStats();
        } catch (java.lang.Exception e){
            throw new SgmlFilterException("Caught an exception doing wordcounting " + e.getMessage());
        }
        List words = segmenterFacade.getWordList();
                
        int count=0;
        // now to compute the count
        // == number of words - number of words that are &nbsp; or &gt; or &lt;
        for (int i=0;i<words.size();i++){
            count++;            
            String word = (String)words.get(i);
            if (word.equals("&nbsp;") ||
                word.equals("&gt;") || 
                word.equals("&lt;") ||
                word.equals("&amp;")){
                    // we don't count those entities
                word = "";
            } else if (gvm!=null){
                if (wordIsSystemEntityRef(word, gvm)){
                    // we don't count system entity references
                    count--;
                }
            }
            // finally, check to see if we've got a zero length string now
            if (word.length() == 0){
                // and don't count that
                count--;
            }
        }
        return count;
    }
    
    private static boolean wordIsSystemEntityRef(String entityRef, GlobalVariableManager gvm){                
        if (entityRef.charAt(0)=='&' && entityRef.charAt(entityRef.length()-1)==';'){
            return gvm.isVariableDefined(entityRef.substring(1,entityRef.length()-1),"SYSTEM");
        }
        return false;
    }
    
    /** Allows us to get more information about an entity declaration
     * a 3 element array, the array is as follows :
     * arr [ENTITYNAME] = the name of the entity
     * arr [ENTITYVALUE] = the value of the entity
     * arr [ENTITYTYPE] = the type, currently either "INTERNAL", "PARAMETER" or "SYSTEM"
     *
     * This returns null if there was some problem while parsing the entity or if
     * we should just treat the contents as formatting.
     */
    protected static String[] parseEntity(String s, GlobalVariableManager gvm){
        try {
            StringReader reader = new StringReader(s);
            DTDParser parser = new DTDParser(reader);
            DTD dtd = parser.parse();
            
            Hashtable entities = dtd.entities;
            Hashtable externalDTDs = dtd.externalDTDs;
            String[] retval = new String[3];
            
            Enumeration e = dtd.entities.elements();
            
            while (e.hasMoreElements()){
                DTDEntity entity = (DTDEntity)e.nextElement();
                String name = "";
                String value = "";
                String type = "";
                // any entities containing ndata, we treat as formatting
                if (entity.ndata != null){
                    return null;
                }
                
                if (entity.name != null){
                    //System.out.println("    Name: " + entity.name);
                    name = entity.name;
                }
                
                if (entity.isParsed()){
                    type="PARAMETER";
                } else {
                    type = "INTERNAL";
                }
                
                if (entity.value != null){
                    //System.out.println("    Value: "+entity.value);
                    value= entity.value;
                }
                
                if (entity.externalID != null){
                    
                    if (entity.externalID instanceof DTDSystem) {
                        //System.out.println("    System: "+
                        //entity.externalID.system);
                        value=entity.externalID.system;
                        type = "SYSTEM";
                    }
                    /*else { don't care about these
                        DTDPublic pub = (DTDPublic) entity.externalID;
                        System.out.println("    Public: "+
                        pub.pub+" "+pub.system);
                    }*/
                }
                
                /*if (entity.ndata != null){
                    System.out.println("    NDATA "+entity.ndata);
                }*/
                //System.out.println(name +" = "+value);
                gvm.setVariable(name,value,type);
                retval[ENTITYNAME]=name;
                retval[ENTITYVALUE]=value;
                retval[ENTITYTYPE]=type;
                return retval;
            }
            return null;
        } catch (Exception e){ // this is safe enough here.
            //System.out.println(e.getMessage());
            return null;
        }
    }
    
}
