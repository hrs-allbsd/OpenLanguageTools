
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BookFileSegmenterTable.java
 *
 * Created on July 10, 2003, 2:17 PM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.alignment.Segment;
import java.util.HashSet;
/**
 *
 * @author  timf
 */
public class BookFileSegmenterTable implements SegmenterTable {
    private HashSet dontSegmentSet;
    private HashSet dontSegmentOrCountSet;
    private HashSet dontTranslateSet;
    
    /** Creates a new instance of BookFileSegmenterTable */
    public BookFileSegmenterTable() {
        dontSegmentSet = new HashSet();
        dontSegmentOrCountSet = new HashSet();
        
        dontSegmentSet.add("term");
        dontSegmentSet.add("title");
        dontSegmentSet.add("citetitle");
        dontSegmentSet.add("phrase");
        dontSegmentSet.add("option");
        dontSegmentSet.add("literal");
        dontSegmentSet.add("subjectterm");
        dontSegmentSet.add("literallayout");
        dontSegmentSet.add("refentrytitle");
        dontSegmentSet.add("indexterm");
        dontSegmentSet.add("primary");
        dontSegmentSet.add("secondary");
        
        dontSegmentSet.add("book");
        dontSegmentSet.add("author");
        dontSegmentSet.add("firstname");
        dontSegmentSet.add("surname");
        dontSegmentSet.add("publisher");
        dontSegmentSet.add("subjectset");
        dontSegmentSet.add("holder");
        dontSegmentSet.add("year");
        dontSegmentSet.add("copyright");
        
        dontSegmentOrCountSet.add("address");
        dontSegmentOrCountSet.add("city");
        dontSegmentOrCountSet.add("street");
        dontSegmentOrCountSet.add("postcode");
        dontSegmentOrCountSet.add("state");
        dontSegmentOrCountSet.add("publishername");
        dontSegmentOrCountSet.add("country");
        dontSegmentOrCountSet.add("programlisting");
        dontSegmentOrCountSet.add("screen");
        dontSegmentOrCountSet.add("filename");
        dontSegmentOrCountSet.add("command");
        dontSegmentOrCountSet.add("computeroutput");
        dontSegmentOrCountSet.add("trademark");
        dontSegmentOrCountSet.add("userinput");
        dontSegmentOrCountSet.add("replaceable");
        dontSegmentOrCountSet.add("olink");
        
        
        dontTranslateSet = new HashSet();
        dontTranslateSet.add("comment");
    }
    
    public boolean containsTranslatableAttribute(String tag) {
        if (tag.toLowerCase().equals("book"))
            return true;
        else return false;
    }
    
    public boolean containsTranslatableAttribute(String tag, String namespaceID) {
        if (tag.toLowerCase().equals("book"))
            return true;
        else return false;
    }
    
    public boolean containsTranslatableText(String tag) {
        return !dontTranslateSet.contains(tag.toLowerCase());
    }
    
    public boolean containsTranslatableText(String tag, String namespaceID) {
        return containsTranslatableText(tag);
    }
    
    public boolean dontSegmentInsideTag(String tagname) {
        if (dontSegmentSet.contains(tagname.toLowerCase())){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean dontSegmentInsideTag(String tagname, String namespaceID) {
        return dontSegmentInsideTag(tagname);
    }
    
    public java.util.List getAttrList(String tag) {
        if (tag.toLowerCase().equals("book")){
            java.util.ArrayList list = new java.util.ArrayList();
            list.add("fpi");
            list.add("lang");
            return list;
        } else {
            return new java.util.ArrayList();
        }
    }
    
    public java.util.List getAttrList(String tag, String namespaceID) {
        return getAttrList(tag);
    }
    
    public int getBlockLevel(String tag) {
        return Segment.SOFT;
    }
    
    public int getBlockLevel(String tag, String namespaceID) {
        return 0;
    }
    
    public Character getEntityCharValue(String entity) {
        return null;
    }
    
    public boolean dontSegmentOrCountInsideTag(String tagname) {
        return dontSegmentOrCountSet.contains(tagname.toLowerCase());
    }
    
    
    public boolean dontSegmentOrCountInsideTag(String tagname, String namespaceID) {
        return dontSegmentOrCountInsideTag(tagname);
    }
    
    
    public boolean includeCommentsInTranslatableSection(String tag) {
        return false;
    }
    
    public boolean pcdataTranslatableByDefault() {
        return true;
    }
    
}
