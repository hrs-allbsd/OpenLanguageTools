

package org.jvnet.olt.lucene;

import junit.framework.TestCase;
import org.jvnet.olt.fuzzy.basicsearch.*;
import org.jvnet.olt.minitm.*;
import java.io.File;


public class LuceneMiniTMTest extends TestCase {
    
    String tmpFile = null;
    BasicFuzzySearchMiniTM tm = null;
    
    
    public void setUp() throws Exception {
        System.out.println("Opening index store");
        
        tmpFile = File.createTempFile("pre","tm").getAbsolutePath();
        new File(tmpFile).delete();
        tm = new BasicFuzzySearchMiniTM(tmpFile,true,"tm","US","CZ");
    }
    
    
    public void testAddRemoveDocument() throws Exception {
        System.out.println("Adding document");
        
        AlignedSegment s = new AlignedSegment("This is a simple sentence.","Tohle je jednoducha veta.","nemo");
        tm.addNewSegment(s);
        AlignedSegment[] segs = tm.getAllSegments();
        assertEquals("Document not inserted",1,segs.length);
        
        
        System.out.println("Test update document");
        
        segs[0].setSource("This is another simple sentence");
        tm.updateSegment(segs[0],segs[0].getDataStoreKey());
        segs = tm.getAllSegments();
        assertEquals("Document not update","This is another simple sentence",segs[0].getSource());
        
        System.out.println("Removing document");
        
        tm.removeSegment(segs[0],segs[0].getDataStoreKey());
        segs = tm.getAllSegments();
        assertEquals("Document not deleted",1,segs.length);
    }
    
    
    public void testSearch() throws Exception {
        System.out.println("Searching for 100% match");
        
        AlignedSegment s = new AlignedSegment("This is a simple sentence.","Tohle je jednoducha veta.","nemo");
        tm.addNewSegment(s);
        
        TMMatch[] matches = tm.getMatchFor("This is a simple sentence.",75,100);
        assertEquals("Cannot get 100% match", 100, matches[0].getRatioOfMatch());
        
        System.out.println("Testing fuzzy matches");
        matches = tm.getMatchFor("This is another simple sentences.",75,100);
        assertEquals("Cannot get fuzzy match",76,matches[0].getRatioOfMatch());
    }
    
   
    public void testIsDuplicate() throws Exception {
        System.out.println("Testing is duplicate");
        
        AlignedSegment s = new AlignedSegment("This is a duplicate test.","Tohle je test duplicity.","nemo");
        tm.addNewSegment(s);
        assertEquals("isDuplicate() function does not work", true, tm.isDuplicate("This is a duplicate test.",null));
    }
   
    
    public void tearDown() throws Exception {
        System.out.println("Closing index store");
        
        tm.close();
    }
    
    
}
