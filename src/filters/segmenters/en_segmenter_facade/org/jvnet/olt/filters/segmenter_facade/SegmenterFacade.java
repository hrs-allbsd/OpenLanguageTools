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
 * SegmenterFacade.java
 *
 * Created on June 25, 2002, 1:36 PM
 */

package org.jvnet.olt.filters.segmenter_facade;

import java.util.*;
import org.jvnet.olt.filters.en_segmenter.*;

/**
 * SegmenterFacade is designed to take the pain away from dealing with different
 * segmenters. Provide it a language name, and an input and it will parse the
 * inputstream and you can use it to get the SegmentCollection from the underlying
 * parser.<br><br>
 *
 * @author  timf
 */
public class SegmenterFacade implements SegmenterInterface {
    
    private List segments;
    private List numbers;
    private List words;
    
    private Map formattingMap;
    private Map languageMap;
    
    
    private String language="";
    private java.io.Reader reader;
    private static final int EN = 0;
    private static final int DE = 1;
    private static final int ES = 2;
    private static final int FR = 3;
    private static final int IT = 4;
    private static final int SV = 5;
    
    /** Creates a new instance of SegmenterFacade */
    public SegmenterFacade(java.io.Reader reader, java.lang.String language) {
        
        segments = new ArrayList();
        formattingMap = new HashMap();
        
        numbers = new ArrayList();
        words = new ArrayList();
        
        // only adding the languages we support
        languageMap = new HashMap();
        languageMap.put("en",new Integer(EN));
        languageMap.put("en-AU",new Integer(EN));
        languageMap.put("en-CA",new Integer(EN));
        languageMap.put("en-GB",new Integer(EN));
        languageMap.put("en-HK",new Integer(EN));
        languageMap.put("en-IE",new Integer(EN));
        languageMap.put("en-IN",new Integer(EN));
        languageMap.put("en-LR",new Integer(EN));
        languageMap.put("en-NZ",new Integer(EN));
        languageMap.put("en-PH",new Integer(EN));
        languageMap.put("en-SG",new Integer(EN));
        languageMap.put("en-US",new Integer(EN));
        languageMap.put("en-ZA",new Integer(EN));
        languageMap.put("de",new Integer(DE));
        languageMap.put("de-AT",new Integer(DE));
        languageMap.put("de-BE",new Integer(DE));
        languageMap.put("de-CH",new Integer(DE));
        languageMap.put("de-DE",new Integer(DE));
        languageMap.put("es",new Integer(ES));
        languageMap.put("es-AR",new Integer(ES));
        languageMap.put("es-CL",new Integer(ES));
        languageMap.put("es-CO",new Integer(ES));
        languageMap.put("es-ES",new Integer(ES));
        languageMap.put("es-MX",new Integer(ES));
        languageMap.put("es-PE",new Integer(ES));
        languageMap.put("es-VE",new Integer(ES));
        languageMap.put("fr",new Integer(FR));
        languageMap.put("fr",new Integer(FR));
        languageMap.put("fr-BE",new Integer(FR));
        languageMap.put("fr-CA",new Integer(FR));
        languageMap.put("fr-CH",new Integer(FR));
        languageMap.put("fr-FR",new Integer(FR));
        languageMap.put("it",new Integer(IT));
        languageMap.put("it-IT",new Integer(IT));        
        languageMap.put("sv",new Integer(SV));
        languageMap.put("sv-FI",new Integer(SV));
        languageMap.put("sv-SE",new Integer(SV));
        
        this.reader=reader;
        this.language=language;
    }
    
    /**
     * This method parses the input reader based on the language specified. It
     * also runs that parser's SegmentCollectionFactoryVisitor, and populates
     * the local segmentList and formattingMap.
     */
    public void parse() throws java.lang.Exception {
        Integer availableLanguage = (Integer)languageMap.get(this.language.toLowerCase());
        if (availableLanguage == null){
            availableLanguage = new Integer(EN);
        }
        
        
        switch (availableLanguage.intValue()){
            case EN:
            default:
                Segmenter_en parser_en = new  Segmenter_en(reader);
                parser_en.parse();
                org.jvnet.olt.filters.en_segmenter.SegmentCollectionFactoryVisitor segmentVisit_en =
                new org.jvnet.olt.filters.en_segmenter.SegmentCollectionFactoryVisitor();
                parser_en.walkParseTree(segmentVisit_en, null);
                segments = segmentVisit_en.getCollection();
                formattingMap = segmentVisit_en.getFormatting();
        }
        
        
        
        
        
    }
    
    /**
     * This method parses the input reader based on the language specified. It
     * also runs that parser's SegmentCollectionFactoryVisitor, and populates
     * the local segmentList and formattingMap.
     */
    public void parseForStats() throws java.lang.Exception {
        Integer availableLanguage = (Integer)languageMap.get(this.language.toLowerCase());
        if (availableLanguage == null){
            availableLanguage = new Integer(EN);
        }
        
        
        switch (availableLanguage.intValue()){
            default:
                Segmenter_en parser_en = new  Segmenter_en(reader);
                parser_en.parse();
                org.jvnet.olt.filters.en_segmenter.SegmentStatsVisitor segmentVisit_en =
                new org.jvnet.olt.filters.en_segmenter.SegmentStatsVisitor();
                parser_en.walkParseTree(segmentVisit_en, null);
                words = segmentVisit_en.getWordList();
                numbers = segmentVisit_en.getNumberList();
        }
        
        
        
        
        
    }
    
    /**
     * Gets a list of segments produced by this segmenter
     */
    public java.util.List getSegments() {
        return this.segments;
    }
    
    /**
     * This gives us a Map of the formatting found by this segmenter :
     * that is, usually spaces after segments, \n after segments - that
     * sort of thing. These are formatting elements that are not part
     * of the segment itself.
     *
     * For example, for the text:
     *
     * "This is a sentence. So is this."
     *
     * We would have two segments and one formatting element. The map
     * uses the segment number preceeding each piece of formatting as
     * it's key.
     *
     * For the above example, our Map would have one entry :
     *  key  value
     *  1 =  " "
     * 
     */     
    public java.util.Map getFormatting() {
        return this.formattingMap;
    }
    
    /**
     * This returns a list of numbers that were found in this segment
     * which can be used by our alignment program
     *
     * @returns a list of the numbers found in this segment
     */
    public java.util.List getNumberList(){
        return this.numbers;
    }
    
    /**
     * This returns a list of all the individual words found in this
     * segment (may include repetitions) - this list can be used by
     * the alignment program, and the number of elements in the List
     * is the wordcount.
     */
    public java.util.List getWordList(){
        return this.words;
    }
    
    
}
