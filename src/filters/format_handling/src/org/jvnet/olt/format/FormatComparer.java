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

package org.jvnet.olt.format;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

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
    
    private static final int CORRECTION_CONSTANT = 3;
    
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
        Map formatMap = new HashMap();
        
        FormatExtractor srcExtractor   = getFormatExtractor(srcType);
        FormatExtractor matchExtractor = getFormatExtractor(matchType);
        
        String normalizedSource = srcExtractor.getNormalizedForm(src,formatMap,gvm);
        //System.out.println("normalizedSource: " + normalizedSource);
        String normalizedMatch  = matchExtractor.getNormalizedForm(match,formatMap,gvm);
        //System.out.println("normalizedMatch: " + normalizedMatch);
  
        int editDistance = computeLevenshteinDistance(normalizedSource.toCharArray(),normalizedMatch.toCharArray());
        
        int penalty = (int)Math.ceil(editDistance/1.5);
        
        return penalty;
        
        //return (int)Math.ceil(editDistance/CORRECTION_CONSTANT);
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
        } else {
            throw new UnsupportedFormatException("Format is unsupported : " + type);
        }
    }
    
    /**
     * Compute edit distance with Levenstein distance algorith
     *
     * @param str1 the first string to compare
     * @param str2 the second string to compare
     *
     * @return the edit distance for str1 and str2
     */
    private static int computeLevenshteinDistance(char[] str1, char[] str2) {
        int[][] distance = new int[str1.length+1][];
        
        for(int i=0; i<=str1.length; i++){
            distance[i] = new int[str2.length+1];
            distance[i][0] = i; 
        }
        for(int j=0; j<str2.length+1; j++)
            distance[0][j]=j;
        
        for(int i=1; i<=str1.length; i++)
            for(int j=1;j<=str2.length; j++)
                distance[i][j]= minimum(distance[i-1][j]+1, distance[i][j-1]+1,
                        distance[i-1][j-1]+((str1[i-1]==str2[j-1])?0:1));
        
        return distance[str1.length][str2.length];
    }
    
    /**
     * Helper method for Levevenstein edit distance algorithm to count
     * atomic minimal edit operation.
     */
    private static int minimum(int a, int b, int c){
        if (a<=b && a<=c)
            return a;
        if (b<=a && b<=c)
            return b;
        return c;
    }
    
    public static void main(String[] args) throws Throwable {
        FormatComparer fc = new FormatComparer();
        int formatPenalty = fc.compareFormats(args[0], args[1], args[2], args[3], new org.jvnet.olt.format.sgml.EntityManager());
        System.out.println("Format penalty: " + formatPenalty);
    }
    
}
