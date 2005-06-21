
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MergeBySameOrderTest.java
 * JUnit based test
 */

package org.jvnet.olt.filters.xmlmerge.unit_test.mergeBySameOrder;

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
 * @author Fan Song
 */
public class MergeBySameOrderTest extends TestCase {
    private final String PATH               = "unit_test/test_files/mergeBySameOrder/";
    private final String ORIGINAL_FILE_NAME = PATH + "originalXMLFile.xml";   //set the original file path
    private final String TRANS_FILE_NAME    = PATH + "transXMLFile.xml";      //set the translated file path
        
    private final String SOURCE_PATH_STR    = "root/file/unit/source";        //set the source element path
    
    private final String NEW_ATTR_VALUE     = "fr-FR";         //set the new language
    private final String ATTR_NAME          = "xml:lang";      //set the language attribute name
    private final String NEW_TARGET_ELEMENT_NAME = "target";   //set the new "translated source" element name
    
    private InputSource originalInputSource;
    private InputSource transInputSource ;
    
    public MergeBySameOrderTest(java.lang.String testName) {
        super(testName);
    }
    
    protected void setUp() throws java.lang.Exception{
        originalInputSource = new InputSource(ORIGINAL_FILE_NAME);
        transInputSource = new InputSource(TRANS_FILE_NAME);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(MergeBySameOrderTest.class);
        return suite;
    }
    
    public void testAllFunc(){
        MergeXML mx;
        FileWriter fw ;
        final String NEW_FILE_NAME = PATH + "all_func.xml";//set the new file name
        try{
            fw = new FileWriter(NEW_FILE_NAME);
        }catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }
        
        MergingInfo mergingInfo = new MergingInfo(originalInputSource,transInputSource,fw,SOURCE_PATH_STR);
        
        mergingInfo.setNewTargetElementName(NEW_TARGET_ELEMENT_NAME);
        
        AttributesImpl newAttrs = new AttributesImpl();
        newAttrs.addAttribute(null, null, ATTR_NAME, "CDATA", NEW_ATTR_VALUE);
        
        mergingInfo.setNewAttrs(newAttrs);
        mergingInfo.setMergeType(MergeType.MERGE_BY_SAME_ORDER);
        
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
    public void testMiniFunc(){
        MergeXML mx;
        FileWriter fw ;
        final String NEW_FILE_NAME = PATH + "without_changing_attributes_and_source_name.xml";//set the new file name
        try{
            fw = new FileWriter(NEW_FILE_NAME);
        }catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }
        
        MergingInfo mergingInfo = new MergingInfo(originalInputSource,transInputSource,fw,SOURCE_PATH_STR);
                
        mergingInfo.setMergeType(MergeType.MERGE_BY_SAME_ORDER);
        
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
