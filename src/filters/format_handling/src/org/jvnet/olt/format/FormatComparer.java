
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.format;

import java.util.Iterator;
import java.util.HashMap;

import org.jvnet.olt.format.plaintext.PlainTextFormatExtractor;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.SgmlFormatExtractor;
import org.jvnet.olt.format.brokensgml.BrokenSgmlFormatExtractor;
import org.jvnet.olt.format.soxliff.SOXliffFormatExtractor;
import org.jvnet.olt.format.printf.PrintfFormatExtractor;
import org.jvnet.olt.format.messageformat.MessageFormatExtractor;

public class FormatComparer {
    private HashMap m_hashExtractors;
    private HashMap m_hashAvailableExtractors;
    
    private static final int BLAML=0;
    private static final int PLAINTEXT=1;
    
    private static final int PRINTF=2;
    private static final int MESSAGEFORMAT=3;
    private static final int BROKENML=4;
    private static final int SOXLIFF=5;
    
    public FormatComparer() {
        m_hashExtractors = new HashMap();
        m_hashAvailableExtractors = new HashMap();
        m_hashAvailableExtractors.put("HTML", new Integer(BLAML));
        m_hashAvailableExtractors.put("SGML", new Integer(BLAML));
        m_hashAvailableExtractors.put("XML", new Integer(BLAML));
        m_hashAvailableExtractors.put("JSP", new Integer(BROKENML));
        m_hashAvailableExtractors.put("PLAINTEXT", new Integer(PLAINTEXT));
        
        m_hashAvailableExtractors.put("MSG", new Integer(PRINTF));
        m_hashAvailableExtractors.put("PO", new Integer(PRINTF));
        m_hashAvailableExtractors.put("PROPERTIES", new Integer(MESSAGEFORMAT));
        m_hashAvailableExtractors.put("JAVA", new Integer(MESSAGEFORMAT));
        m_hashAvailableExtractors.put("DTD", new Integer(MESSAGEFORMAT));
        m_hashAvailableExtractors.put("STAROFFICE", new Integer(SOXLIFF));
        
    }
    
    /**
     *  This method is responsible for comparing the formats of the two
     *  strings provided and producing a number that is to be used as a
     *  penalty score on the match.
     *  @return A value representing the penalty to be applied to the percentage match due to the differences in formatting between the two strings.
     */
    public int compareFormats(String src, String srcType, String match, String matchType, GlobalVariableManager gvm) throws InvalidFormattingException, UnsupportedFormatException {
        //  Extract the formatting from source string
        HashMap sourceMap = extractFormatting(src, srcType.toUpperCase(), gvm);
        
        //  Extract the formatting from match string
        HashMap matchMap = extractFormatting(match, matchType.toUpperCase(), gvm);
        
        //  Do comparison
        int formatPenalty = 0;
        
        Iterator sourceIterator = sourceMap.keySet().iterator();
        String sourceKey;
        FormatItem sourceItem;
        FormatItem matchItem;
        while(sourceIterator.hasNext()) {
            sourceKey = (String) sourceIterator.next();
            if(matchMap.containsKey(sourceKey)) {
                sourceItem = (FormatItem) sourceMap.get(sourceKey);
                matchItem = (FormatItem) matchMap.get(sourceKey);
                
                int numMatch = matchItem.getOccurrences();
                int numSrc = sourceItem.getOccurrences();
                
                if(numMatch != numSrc) {
                    formatPenalty +=
                    (Math.abs(numMatch - numSrc) * sourceItem.getPenalty());
                }
                
                matchMap.remove(sourceKey);
            }
            else {
                sourceItem = (FormatItem) sourceMap.get(sourceKey);
                formatPenalty +=
                (Math.abs(sourceItem.getOccurrences()) * sourceItem.getPenalty());
                
            }
        }
        
        Iterator matchIterator = matchMap.values().iterator();
        while(matchIterator.hasNext()) {
            //  Anything left in matchMap wasn't found in sourceMap therefore
            //  apply the penalties.
            matchItem = (FormatItem) matchIterator.next();
            
            formatPenalty +=
            (Math.abs(matchItem.getOccurrences()) * matchItem.getPenalty());
        }
        
        return formatPenalty;
    }
    
    protected HashMap extractFormatting(String string, String type, GlobalVariableManager gvm) throws InvalidFormattingException, UnsupportedFormatException {
        FormatExtractor extractor = getFormatExtractor(type);
        
        HashMap formats = extractor.getFormatting(string, gvm);
        
        return formats;
    }
    
    protected FormatExtractor getFormatExtractor(String type)
    throws UnsupportedFormatException {
        //  Find or create an appropriate FormatExtractor object.
        //  return new SgmlFormatExtractor();
        FormatExtractor ex;
        if(m_hashExtractors.containsKey(type)) {
            ex = (FormatExtractor) m_hashExtractors.get(type);
            if(ex != null) { return ex; }
        }
        
        if(m_hashAvailableExtractors.containsKey(type)) {
            Integer code = (Integer) m_hashAvailableExtractors.get(type.toUpperCase());
            int iExtract = code.intValue();
            
            switch(iExtract) {
                case BLAML:
                    //  SGML like markup languages
                    ex = new SgmlFormatExtractor();
                    m_hashExtractors.put(type, ex);
                    return ex;
                case PLAINTEXT:
                    ex = new PlainTextFormatExtractor();
                    m_hashExtractors.put(type, ex);
                    return ex;
                case MESSAGEFORMAT:
                    ex = new MessageFormatExtractor();
                    m_hashExtractors.put(type, ex);
                    return ex;
                case PRINTF:
                    ex = new PrintfFormatExtractor();
                    m_hashExtractors.put(type, ex);
                    return ex;
                case BROKENML:
                    ex = new BrokenSgmlFormatExtractor();
                    m_hashExtractors.put(type, ex);
                    return ex;
                case SOXLIFF:
                    ex = new SOXliffFormatExtractor();
                    m_hashExtractors.put(type, ex);
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
