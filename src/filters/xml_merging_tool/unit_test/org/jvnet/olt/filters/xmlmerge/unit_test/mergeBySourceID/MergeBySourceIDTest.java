
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MergeBySourceIDTest.java
 * JUnit based test
 *
 * Created on 2004年4月9日, 下午3:38
 */

package org.jvnet.olt.filters.xmlmerge.unit_test.mergeBySourceID;

import junit.framework.*;

import org.jvnet.olt.filters.xmlmerge.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.AttributesImpl;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Fan Song
 */
public class MergeBySourceIDTest extends TestCase {
    private final String PATH               = "unit_test/test_files/mergeBySourceID/";
    private final String ORIGINAL_FILE_NAME = PATH + "originalXMLFile.xml";   //set the original file path
    private final String TRANS_FILE_NAME    = PATH + "transXMLFile.xml";      //set the translated file path
    
    private final String SOURCE_PATH_STR    = "root/file/unit/source";        //set the source element path
    
    private final String NEW_ATTR_VALUE     = "fr-FR";         //set the new language
    private final String ATTR_NAME          = "xml:lang";      //set the language attribute name
    private final String NEW_TARGET_ELEMENT_NAME = "target";   //set the new "translated source" element name
   
    private final String ID_ATTR_NAME = "id";
    
    private InputSource originalInputSource;
    private InputSource transInputSource ;
    
    public MergeBySourceIDTest(java.lang.String testName) {
        super(testName);
    }
    
    protected void setUp() throws java.lang.Exception{
        originalInputSource = new InputSource(ORIGINAL_FILE_NAME);
        transInputSource = new InputSource(TRANS_FILE_NAME);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MergeBySourceIDTest.class);
        return suite;
    }
    public void testBySourceID(){
        MergeXML mx;        
        FileWriter fw ;
        final String NEW_FILE_NAME = PATH + "by_source_ID.xml";
        
        try{
            fw = new FileWriter(NEW_FILE_NAME);
        }catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }
        
        MergingInfo mergingInfo = new MergingInfo(originalInputSource,transInputSource,fw,SOURCE_PATH_STR);
       
        AttributesImpl newAttrs = new AttributesImpl();
        newAttrs.addAttribute(null, null, ATTR_NAME, "CDATA", NEW_ATTR_VALUE);
        mergingInfo.setNewAttrs(newAttrs);
        
       mergingInfo.setNewTargetElementName(NEW_TARGET_ELEMENT_NAME);
        
        mergingInfo.setSourceIDAttrName(ID_ATTR_NAME);
        mergingInfo.setMergeType(MergeType.MERGE_BY_SOURCE_ID);
        
        mx = new MergeXML(mergingInfo);
        
        try{
        mx.generateNewXML();
        }catch(MergeXMLFailedException mxfe){
            System.out.println("can not merge xml files!");
            mxfe.printStackTrace();
        }
        
        try{
            if(null!=fw)
                fw.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }    
}
