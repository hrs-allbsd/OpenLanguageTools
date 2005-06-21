
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * IndexEntry.java
 *
 * Created on May 25, 2004, 5:29 PM
 */

package org.jvnet.olt.index;

/**
 *
 * @author  jc73554
 */
public class IndexEntry implements Comparable {
    
    private String string;
    
    private long index;
    
    /** Creates a new instance of IndexEntry */
    public IndexEntry(String string, long index) {
        this.string = string;
        this.index = index;
    }
    
    public String getString() {
        return string;
    }
    
    public int getStringLength() {
        return string.length();
    }
    
    public long getIndex() {
        return index;
    }
    
    public boolean equals(Object o) {
        if(!(o instanceof IndexEntry)) { return false; }
        
        IndexEntry entry = (IndexEntry) o;
        
        return ((entry.string.equals(this.string)) && (entry.index == this.index));
    }
    
    public int hashCode() {
        return (string.hashCode() + (int) index);
    }
    
    public int compareTo(Object o) {
        if(!(o instanceof IndexEntry)) { 
            Class c = o.getClass();
            String s = c.getName();
            throw new ClassCastException("Attempting to compare object of type "+ s +" with an IndexEntry."); 
        }
        if(this.equals(o)) {
            return 0;
        }
        
        IndexEntry entry = (IndexEntry) o;
        
        int i = this.string.compareTo(entry.string);
        switch(i) {
            case -1:
                return -1;
            case 0:
                if(entry.index < this.index) {
                    return -1;
                } else {  //  Already checked for equals, therefore must be greater than.
                    return 1;
                } 
            case 1:
                return 1;
            default:  // A catch all: perhaps this should throw an exception.
                return 0;
        }
    }
    
}
