
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * MergeXLIFFTest.java
 * JUnit based test
 *
 * Created on 2004年4月9日, 下午6:16
 */

package org.jvnet.olt.filters.xmlmerge.unit_test.mergeXLIFF;

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
 * @author sssfff
 */
public class MergeXLIFFTest extends TestCase {
    final String path               = "unit_test/test_files/mergeXLIFF/";
    final String ORIGINAL_FILE_NAME = path + "originalSource.xliff";   //set the original file path
    final String TRANS_FILE_NAME    = path + "transSource.xliff";      //set the translated file path
    final String NEW_FILE_NAME      = path + "new_xliff_file.xliff";           //set the new file name
    
    final String SOURCE_PATH_STR    = "xliff/file/body/trans-unit/source"; //set the source element path
    
    final String NEW_ATTR_VALUE     = "fr-FR";                 //set the new language
    final String ATTR_NAME          = "xml:lang";                  //set the language attribute name
    final String NEW_TARGET_ELEMENT_NAME = "target";           //set the new "translated source" element name
    final String ID_ELEMENT_NAME = "trans-unit";
    final String ID_ATTR_NAME = "id";
    
    private InputSource originalInputSource;
    private InputSource transInputSource ;
    
    public MergeXLIFFTest(java.lang.String testName) {
        super(testName);
    }
        
    protected void setUp() throws java.lang.Exception{
        originalInputSource = new InputSource(ORIGINAL_FILE_NAME);
        transInputSource = new InputSource(TRANS_FILE_NAME);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MergeXLIFFTest.class);
        return suite;
    }
    
    public void testXLIFF() {
        MergeXML mx;
        
        FileWriter fw ;
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
        
        mergingInfo.setIDLocation(ID_ELEMENT_NAME , ID_ATTR_NAME);
        mergingInfo.setNewTargetElementName(NEW_TARGET_ELEMENT_NAME);
        
        mergingInfo.setIDLocation(ID_ELEMENT_NAME, ID_ATTR_NAME);
        mergingInfo.setMergeType(MergeType.MERGE_XLIFF);
        
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
