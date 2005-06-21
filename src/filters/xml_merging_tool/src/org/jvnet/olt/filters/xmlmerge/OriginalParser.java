
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.xmlmerge;
/*
 * MergeXML.java
 *
 */
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.ext.LexicalHandler;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
/**
 * the original sax parser could run over the source xml file, and whenever it found a source element
 * that we have a translation for, it could also output the translated item from the Map.The new xml file
 * will be generated after the parse process of original sax parser finish.
 * @author  Fan Song
 */
abstract class OriginalParser extends XMLFilterImpl implements LexicalHandler {    
    protected int currentDepth;             // current depth
    protected int sourceDepth;              // the depth of source
    protected int diffPathDepth=0;          // the current depth of error path 
    protected boolean isInSource = false;  // the current cursor is in the source element
    
    protected String[] sourcePath;          // the source path array
    protected Map sourceContentMap;         // source content map
    protected Writer writer;
    
    protected char[] indent = null;
    protected String indentTmp;
    protected int indentLen = 0;
    
    

    /** Creates a new instance of ReadSource */
    public OriginalParser(MergingInfo mergingInfo) {
        this.writer     = mergingInfo.getNewFileWriter();
        this.sourcePath           = mergingInfo.getSourcePathArray();
        this.sourceDepth          = this.sourcePath.length-1;
        this.currentDepth         = -1;
        this.diffPathDepth        = 0;            
    }
      
    /**set the source content map 
     *@param sourceContentMap The map in which the source content is stored .
     */
    public void setSourceContentMap(java.util.Map sourceContentMap) {
        this.sourceContentMap = sourceContentMap;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{       
        super.startElement(uri,localName,qName,atts);
        doStartElement(uri,localName,qName,atts);
    }
    
    /** what to do when start a element
     * It may be override this method in a subclass to take specific actions at the start of
     * each element
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
     */
    protected abstract void doStartElement(String uri, String localName, String qName, Attributes atts) throws SAXException;
    
    public void endElement(String  uri, String localName, String qName) throws SAXException{
        super.endElement(uri, localName , qName);
        doEndElement(uri,localName,qName);
    }
    
    /** what to do when end a element
     * It may be override this method in a subclass to take specific actions at the end of
     * each element
     * @exception org.xml.sax.SAXException Any SAX exception, possibly wrapping another exception.
     */
    protected abstract void doEndElement(String uri, String localName, String qName) throws SAXException;
    
    public void characters( char ch[], int start, int length)throws SAXException{
        super.characters(ch, start,length);
        if( indent == null ){
            indentTmp = new String(ch , start , length);
        }
    }
    
//    public void startPrefixMapping(String prefix, String uri)throws SAXException{
//        super.startPrefixMapping(prefix , uri );
//    }
//    
//    public void endPrefixMapping(String prefix)throws SAXException{
//        super.endPrefixMapping(prefix);
//    }
//    public void processingInstruction(String target, String data)throws SAXException{
//        super.processingInstruction(target , data);
//    }
//    public void startDTD(String name, String publicId, String systemId) throws SAXException {
//        System.out.println("notatinDecl");
//    }
//    public void startDocument() throws SAXException{
//        super.startDocument();
//    }
//    
//    public void endDocument() throws SAXException{
//        super.endDocument();
//    }
    public void startDTD(String name, String publicId,  String systemId)
    throws SAXException{
        String doctype = "<!DOCTYPE " + name + " "
                      + (publicId !=null ? "PUBLIC" : "SYSTEM")
                      + (publicId !=null ? " \"" + publicId + "\"":"")
                      + (systemId !=null ? " \"" + systemId + "\"":"")
                      + ">\r\n" ;
        try{                          
        writer.write(doctype); 
        }catch(IOException ioe){
            ioe.printStackTrace();
            System.exit(-1);
        }
//          System.out.println(doctype);  
    }
    
    public void endDTD()	throws SAXException{
        //System.out.println("endDTD");
    }
    
    public void startEntity(String name)throws SAXException{
        ;
    }
    
    public void endEntity(String name)throws SAXException{
        ;
    }
    
    
    public void startCDATA() throws SAXException{
        ;
    }
    
    
    public void endCDATA()throws SAXException{
        ;
    }
    
    
    public void comment(char ch[], int start, int length)throws SAXException{
        ;
    }


}

