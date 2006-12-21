/*
 * DocIteratorImplTest.java
 *
 * Created on December 1, 2006, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.translation;

import junit.framework.TestCase;
import org.jvnet.olt.editor.model.TMData;
import org.jvnet.olt.editor.model.TMData.TMSentence;
import org.jvnet.olt.editor.translation.DocIterator.Direction;
import org.jvnet.olt.editor.translation.DocIterator.Type;
/**
 *
 * @author boris
 */
public class DocIteratorImplTest extends TestCase {
    TMSentence[] sntncs;
    
    class XTMData extends TMData{
        int size;
        
        XTMData(int size){
            super();
            
            this.size = size;
        }
        
        public boolean build(){
            this.tmsentences = new XTMSentence[10];
            for(int i = 0;i < tmsentences.length;i++){
                tmsentences[i] = new XTMSentence(i);
                tmsentences[i].setSource("s"+i);
                tmsentences[i].setTranslation("t"+i);
            }
            return true;
        }
        
        class XTMSentence extends TMSentence {
            private String src;
            private String tgt;
            
            XTMSentence(int segId){
                super();
            }
            
            @Override
            public void setSource(String src){
                this.src = src;
            }
            
            @Override
            public String getSource(){
                return this.src;
            }
            
            @Override
            public void setTranslation(String tgt){
                this.tgt = tgt;
            }
            
            @Override
            public String getTranslation(){
                return this.tgt;
            }
            
        }
        
        
    }
    
    /** Creates a new instance of DocIteratorImplTest */
    public DocIteratorImplTest() {
        
        
    }
    
    public void setUp(){
        XTMData data = new XTMData(10);
        data.build();
        sntncs = data.tmsentences;
    }
    
    public void testSeekToSegment() throws Exception {
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);

        i.seekToSegment(5);
        assertEquals(5, i.currentSegment());
        
        i.seekToSegment(9);
        assertEquals(9,i.currentSegment());
        
        try{
            int l = i.getSegmentsCount();
            i.seekToSegment(l+10);

            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae){
            // all ok
        }
        
        i.seekToSegment(5);
        assertTrue(i.hasNext());
        assertEquals("s5",i.next());
        
        //two seek to segment with the same segment number *MUST* yield the same
        i.seekToSegment(3);
        i.seekToSegment(3);
        
        assertEquals(3,i.currentSegment());
    }
    
    public void testSetDirectionAfterConstruction(){
        DocIteratorImpl ii = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);
        ii.setDirection(DocIterator.Direction.UP);
        ii.setDirection(DocIterator.Direction.UP);

        assertFalse(ii.hasNext());
    }

    public void testSetDirectionAfterConstruction2(){
        DocIteratorImpl ii = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);
        ii.setDirection(DocIterator.Direction.DOWN);
        ii.setDirection(DocIterator.Direction.UP);
        ii.setDirection(DocIterator.Direction.DOWN);

        assertTrue(ii.hasNext());
        assertEquals("s0",ii.next());
    }
    
    public void testSourceDown() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);
        
        StringBuilder sb = new StringBuilder();
        int j = 0;
        while(i.hasNext()){
            j++;
            
            sb.append(i.next());
            sb.append(" ");
        }
        
        assertEquals(10, j);
        assertEquals("s0 s1 s2 s3 s4 s5 s6 s7 s8 s9 ",sb.toString());
    }
    
    public void testSourceUp() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.UP);
        
        assertFalse(i.hasNext());
        
        i.seekToSegment(9);
        
        StringBuilder sb = new StringBuilder();
        int j = 0;
        while(i.hasNext()){
            j++;
            
            sb.append(i.next());
            sb.append(" ");
        }
        
        assertEquals(10, j);
        assertEquals("s9 s8 s7 s6 s5 s4 s3 s2 s1 s0 ",sb.toString());
    }
    
    public void testTargetDown() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.TARGET,Direction.DOWN);
        
        StringBuilder sb = new StringBuilder();
        int j = 0;
        while(i.hasNext()){
            j++;
            
            sb.append(i.next());
            sb.append(" ");
        }
        
        assertEquals(10, j);
        assertEquals("t0 t1 t2 t3 t4 t5 t6 t7 t8 t9 ",sb.toString());
    }
    
    public void testTargetUp() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.TARGET,Direction.UP);
        
        assertFalse(i.hasNext());
        
        i.seekToSegment(9);
        
        StringBuilder sb = new StringBuilder();
        int j = 0;
        while(i.hasNext()){
            j++;
            
            sb.append(i.next());
            sb.append(" ");
        }
        
        assertEquals(10, j);
        assertEquals("t9 t8 t7 t6 t5 t4 t3 t2 t1 t0 ",sb.toString());
    }
    
    public void testBothUp() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.BOTH,Direction.UP);
        
        assertFalse(i.hasNext());
        
        i.seekToSegment(19);
        
        StringBuilder sb = new StringBuilder();
        int j = 0;
        while(i.hasNext()){
            j++;
            
            sb.append(i.next());
            sb.append(" ");
        }
        
        assertEquals(20, j);
        assertEquals("t9 s9 t8 s8 t7 s7 t6 s6 t5 s5 t4 s4 t3 s3 t2 s2 t1 s1 t0 s0 ",sb.toString());
    }
    
    public void testSetDirection() throws Exception{
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);
        
        i.seekToSegment(5);
        assertEquals("s5",i.next());
        i.setDirection(DocIterator.Direction.UP);
        assertTrue(i.hasNext());
        assertEquals("s5",i.next());
        
    }
    
    public void testFailures() throws Exception {
        DocIteratorImpl i = new DocIteratorImpl(sntncs,Type.SOURCE,Direction.DOWN);
        i.next();
        
    }
}
