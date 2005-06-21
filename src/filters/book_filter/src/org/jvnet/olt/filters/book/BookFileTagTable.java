
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * BookFileTagTable.java
 *
 * Created on July 10, 2003, 2:19 PM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.parsers.tagged.TagTable;
import java.util.HashSet;
/**
 *
 * @author  timf
 */
public class BookFileTagTable implements TagTable {
    private HashSet pcdataSet;
    
    /** Creates a new instance of BookFileTagTable */
    public BookFileTagTable() {
        pcdataSet = new HashSet();
        pcdataSet.add("screen");
        pcdataSet.add("programlisting");
        pcdataSet.add("filename");
        pcdataSet.add("command");
        pcdataSet.add("replaceable");
        pcdataSet.add("option");
        pcdataSet.add("literal");
        pcdataSet.add("userinput");
        pcdataSet.add("olink");
        pcdataSet.add("subjectterm");
        pcdataSet.add("trademark");
        pcdataSet.add("refentrytitle");
        pcdataSet.add("holder");
        pcdataSet.add("pubsnumber");
        pcdataSet.add("pubdate");
        pcdataSet.add("copyright");
        pcdataSet.add("year");
        pcdataSet.add("author");
        pcdataSet.add("firstname");
        pcdataSet.add("surname");
    
    }
    
    public boolean tagEmpty(String tagName) {
        return false;
    }
    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return false;
    }
    
    public boolean tagMayContainPcdata(String tagName) {
        if (pcdataSet.contains(tagName.toLowerCase())){
            return true;
        } else
            return false;
    }
    
    public boolean tagEmpty(String tagName, String namespaceID) {
        return tagEmpty(tagName);
    }
    
    public boolean tagForcesVerbatimLayout(String tagName, String namespaceID) {
        return tagForcesVerbatimLayout(tagName);
    }
    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}
