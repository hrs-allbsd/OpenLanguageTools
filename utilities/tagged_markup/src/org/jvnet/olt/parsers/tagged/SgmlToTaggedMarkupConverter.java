
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XmlToTaggedMarkupConverter.java
 *
 * Created on May 14, 2003, 4:44 PM
 */

package org.jvnet.olt.parsers.tagged;

/**
 *
 * @author  timf
 */
public class SgmlToTaggedMarkupConverter implements NodeTypeConverter {
    
    /** Creates a new instance of XmlToTaggedMarkupConverter 
     * Maintenance note - really this class should be in the sgml
     * parser package, not the tagged_markup package...
     */
    public SgmlToTaggedMarkupConverter() {
    }
    
    /**
     * This converts a node type from SgmlDocFragmentParserTreeConstants into it's
     * equivalent node type as a TaggedMarkupNodeConstants
     *
     * @param originalNodeType the sgml node type you want converted
     * @return a TaggedMarkupNode type
     */
    public int convert(int originalNodeType) {
        // at the moment, the sgml constants are exactly
        // the same as the tagged node constants - so there's
        // no work to do. Otherwise, we'd build a HashMap that
        // maps from SgmlDocFragmentParserTreeConstants to 
        // TaggedMarkupNodeConstants, and use that
        return originalNodeType;
    }
    
}
