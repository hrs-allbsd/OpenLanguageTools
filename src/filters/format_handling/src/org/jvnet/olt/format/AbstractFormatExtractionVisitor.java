package org.jvnet.olt.format;

import java.util.Map;
import java.util.Arrays;

/**
 * Abstract class that simplify creation of ExtractionVisitors
 *
 * We need to transform all formating to 'normalized' form. Here is an
 * example of the normalized form:
 *
 * The formatted segment:  'This is just a <b>test</b>.'
 *
 * will be transformed to: 'XAXBX'
 *
 * All tags a replaced with unique characters and all PCDATA are replace with character 'X'
 *
 *
 * @author mt150864
 */
public abstract class AbstractFormatExtractionVisitor {
    
    // character that replace all PCDATA nodes
    protected static final char PCDATA_REPLACEMENT = 'X';
    
    // first unique character in the formatMap
    protected static final char FIRST_UNIQUE_CHAR  = '@';
    
    // map that holds format -> unique character mapping
    protected Map formatMap;
    
    // buffer that holds normalized form
    protected StringBuffer normalizedForm;
    
    // state that is true if the last normalized character in the buffer is PCDATA
    protected boolean pcdataSect;
    
    // counter that holds last unique character that was created
    protected char    uniqueCharCounter;
    
    /**
     * Constructs a new AbstractFormatExtractingVisitor.
     *
     * @param formatMap contains mapping from a tag to unique character
     *               the normalized form is constructed by replacing
     *               the tags with charaters from the map.
     *               If some tag is not in the map new record is added
     *
     */
    public AbstractFormatExtractionVisitor(Map formatMap) {
        this.formatMap = formatMap;
        pcdataSect = false;
     
        // sort the unique characters in the formatMap and get the last character
        if(formatMap.size()>0) {
            Object[] os = formatMap.values().toArray();
            Arrays.sort(os);
            uniqueCharCounter = ((Character)os[os.length-1]).charValue();
        } else {
            uniqueCharCounter =FIRST_UNIQUE_CHAR;
        }
        
        normalizedForm   = new StringBuffer();
    }
    
    /**
     * Get the normalized form of the segment
     *
     * @return the normalized form
     */
    public String getNormalizedForm() {
        return normalizedForm.toString();
    }
    
    /**
     * Check the existence of the tag in the formatMap and add the appropriate
     * character
     *
     * @param tag the String that represents the formatting
     * @param large indicate that the formatting string is big
     */
    protected void addFormat(String tag, boolean large) {
        Character normalizedChar = null;
        
        if(formatMap.containsKey(tag)) {
            normalizedChar = (Character)formatMap.get(tag);
        } else {
            uniqueCharCounter++;
            // jump over the PCDATA character
            if(uniqueCharCounter==PCDATA_REPLACEMENT) {
                uniqueCharCounter++;
            }
            
            normalizedChar = new Character(uniqueCharCounter);
            
            formatMap.put(tag,normalizedChar);
        }
        
        normalizedForm.append(normalizedChar);
        // add one more character for large formatted sentence
        if(large) {
            normalizedForm.append(normalizedChar);
        }
        
        pcdataSect = false;
    }

    /**
     * Check the existence of the tag in the formatMap and add the appropriate
     * character
     *
     * @param tag the String that represents the formatting
     */
    protected void addFormat(String tag) {
        addFormat(tag,false);
    }
    
    /**
     * Add PCData to normalizedForm
     *
     */
    protected void addPCData() {
        if(!pcdataSect) {
            normalizedForm.append(PCDATA_REPLACEMENT);
            pcdataSect = true;
        }
    }
    
}
