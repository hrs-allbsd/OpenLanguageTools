
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.tmx_aml_converter;

//import org.jdom.*;
//import org.jdom.input.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

import java.io.*;
import java.util.*;

import org.jvnet.olt.converterhandler.Resource;
import org.jvnet.olt.tmx_aml_converter.TmxToAmlTransformerException;
import org.jvnet.olt.io.*;

/**
 * <p>Title: Alignment editor</p>
 * <p>Description: part of TMCi editor</p>
 * @author Charles
 * @version 1.0
 */

public class TmxToAmlConverter {
  private final String tagContainText = ",note,prop,seg,bpt,ept,sub,it,ph,hi,ut";
  private final String tagEmpty = ",map";
  private final String converterName = "TmxToAmlConverter";
  private final String converterVersion = "Version 1.0 test";

  private int srcposition = 0, trgposition = 0;
  private int srcnumber = 0, trgnumber = 0;
  private int tagtucount = 0;
  private String tmxFileName = "", amlFileName = "";
  private String srcLang = "", trgLang = "";

  /**
   * TMX -> AML Converter
   * @param tmxName         TMX file name, it can be null or ""
   * @param tmxReader       the reader used to read the tmx file
   * @param amlName         AML file name, it can be null or ""
   * @param amlWriter       the writer used to write out the aml file
   */
  public void tmxToAml(String tmxName, Reader tmxReader, String amlName, Writer amlWriter) throws TmxToAmlTransformerException,Exception {
    try {
      if(tmxName != null && tmxName != "") tmxFileName = new File(tmxName).getName();
      if(amlName != null && amlName != "") amlFileName = new File(amlName).getName();

      //Parsing the reader

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);

      DocumentBuilder domBuilder = factory.newDocumentBuilder();
      domBuilder.setEntityResolver(new TmxEntityResolver());
      domBuilder.setErrorHandler(new ErrorHandler() {
        public void error(SAXParseException ex) throws SAXException {
          System.err.println("Missing DOCTYPE.");
        }
        public void fatalError(SAXParseException ex) throws SAXException {
        }
        public void warning(SAXParseException ex) throws SAXException {
        }
      });

      InputSource inputSource = new InputSource(tmxReader);
      inputSource.setSystemId(Resource.TMXDTDFileName);

      Document tmxdoc = domBuilder.parse(inputSource);

      //create AML document via the TMX document
      Document amldoc = createAmlDoc(tmxdoc);

      //prepare the string of the aml document
      String amlstring = prepareForDoc(amldoc, Resource.AMLDTDFileName);

      //write the string of the aml document to the Writer
      amlWriter.write(amlstring);
      amlWriter.flush();


    } catch(DOMException domex) {
      domex.printStackTrace();
      throw new TmxToAmlTransformerException(domex.getMessage());
    } catch(Exception ex) {
      throw ex;
    }
  }

  /**
   * Create AML document
   * @param doc        the TMX document which would be reference to create a AML document
   * @return           a JDOM document
   */
  private Document createAmlDoc(Document doc) {
    Document amldoc = null;
    try {
      //create new document
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder domBuilder = factory.newDocumentBuilder();
      amldoc = domBuilder.newDocument();


      //create root Element
      Element elemAml = amldoc.createElement("aml");
      elemAml.setAttribute("version", "1.0");
      amldoc.appendChild(elemAml);
      //create DocumentType
      DOMImplementation domImpl = amldoc.getImplementation();
      domImpl.createDocumentType("aml",null,Resource.AMLDTDFileName);

      //get source language
      srcLang = ((Element)doc.getDocumentElement().getElementsByTagName("header").item(0)).getAttribute("srclang");
      //create Aml Document via the Tmx Document
      createAmlFromTmx(doc.getDocumentElement(), elemAml);

      //get the header Element
      Element elemHeader = (Element)elemAml.getElementsByTagName("header").item(0);
      //set trgLang attribute and others
      elemHeader.setAttribute("trglang",trgLang);
      elemHeader.setAttribute("o-tmf","tmx");
      elemHeader.setAttribute("creationtool",converterName);
      elemHeader.setAttribute("creationtoolversion",converterVersion);

      //using the "tmxFileName","amlFileName" to mark in the header of the aml file
      /*
      Element elemProp = null;
      if(tmxFileName != null && tmxFileName != "") {
        elemProp = amldoc.createElement("prop");
        elemProp.setAttribute("type","AlignmentEditor::TmxFile");
        Text text = amldoc.createTextNode(tmxFileName);
        elemProp.appendChild(text);
        elemHeader.appendChild(elemProp);
      }
      if(amlFileName != null && amlFileName != "") {
        elemProp = amldoc.createElement("prop");
        elemProp.setAttribute("type","AlignmentEditor::AmlFile");
        Text text = amldoc.createTextNode(amlFileName);
        elemProp.appendChild(text);
        elemHeader.appendChild(elemProp);
      }
      */
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return amldoc;
  }

  /**
   * Recursive method to create JDOM tree
   * @param efrom       TMX document element
   * @param eto         AML document element
   * @param srcLang     source language
   */
  private void createAmlFromTmx(Element efrom, Element eto) {

    //get all children Node
    NodeList nodelist = efrom.getChildNodes();

    int srcindex = 0, trgindex = 0;
    for(int i=0;i<nodelist.getLength();i++) {
      Node next = nodelist.item(i);
      if(next instanceof Text) {
        //Text content
        Text text = eto.getOwnerDocument().createTextNode(((Text)next).getData());
        eto.appendChild(text);

      } else if(next instanceof Element) {
        //Element content
        String eName = ((Element)next).getTagName();
        if(efrom.getTagName().equals("tu") && eName.equals("prop")) continue;
        if(efrom.getTagName().equals("tu") && eName.equals("note")) continue;

        Element elem = null;
        if(eName.equals("tu")) {
          tagtucount ++;
          //current child of Element efrom is "tu" tag
          if(((Element)next).getElementsByTagName("tuv").getLength() > 1)
            elem = eto.getOwnerDocument().createElement("aligned");
          else
            elem = eto.getOwnerDocument().createElement("unaligned");
          elem.setAttribute("id","a"+tagtucount);

        } else if(eName.equals("tuv") &&  ((Element)next).getAttribute("xml:lang").equals(srcLang)) {
          //current child of Element efrom is "tuv" tag and it indicates source segment
          elem = eto.getOwnerDocument().createElement("src");
          srcnumber ++;

          elem.setAttribute("pos","s"+srcposition++);

        } else if(eName.equals("tuv") && !((Element)next).getAttribute("xml:lang").equals(srcLang)) {
          //current child of Element efrom is "tuv" tag and it indicates target segment
          elem = eto.getOwnerDocument().createElement("trg");
          trgLang = ((Element)next).getAttribute("xml:lang");
          trgnumber ++;
          elem.setAttribute("pos","t"+trgposition++);

        } else {
          elem = eto.getOwnerDocument().createElement(eName);
          //get all attributes
          NamedNodeMap nnm = ((Element)next).getAttributes();
          for(int j=0;j<nnm.getLength();j++) {
            Attr attr = (Attr)nnm.item(j);
            elem.setAttribute(attr.getName(),attr.getValue());
          }
        }

        eto.appendChild(elem);
        createAmlFromTmx((Element)next,elem);
      }
    }

    if(efrom.getTagName().equals("tu")) {
      //set "index" and "total" attributes
      NodeList srclist = eto.getElementsByTagName("src");
      if(srcnumber > 1) {
        for(int k=0;k<srclist.getLength();k++) {
          ((Element)srclist.item(k)).setAttribute("index",String.valueOf(k+1));
          ((Element)srclist.item(k)).setAttribute("total",String.valueOf(srcnumber));
        }
      }
      NodeList trglist = eto.getElementsByTagName("trg");
      if(trgnumber > 1) {
        for(int k=0;k<trglist.getLength();k++) {
          ((Element)trglist.item(k)).setAttribute("index",String.valueOf(k+1));
          ((Element)trglist.item(k)).setAttribute("total",String.valueOf(trgnumber));
        }
      }
      srcnumber = 0;
      trgnumber = 0;
    }
  }

  /////////////////////////////////////////////////////////////////////////
  /**
   * Get the string of all elements in the JDOM tree
   * @param doc              document to generate a string
   * @param dtdFileName      DTD file name
   * @return
   */
  private String prepareForDoc(Document doc, String dtdFileName) {
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version=\"1.0\" ?>\n");
    sb.append("<!DOCTYPE " + ((dtdFileName.indexOf("tmx") != -1)?"tmx":"aml") + " SYSTEM \"" + dtdFileName + "\">");

    prepareForNodes(sb, doc.getDocumentElement(), 0, true);

    return sb.toString();
  }

  /**
   * Recursive Method to generate string with each element
   * @param sb             StringBuffer instance
   * @param element
   * @param level          tree node level
   * @param newline        if need a newline
   */
  private void prepareForNodes(StringBuffer sb,Element element,int level,boolean newline) {
    String elemName = element.getTagName();

    if(newline) {
      sb.append("\n");
      for(int i=0;i<level;i++) sb.append("  ");
    }

    //handle the attributes of the element
    sb.append("<"+elemName);
    //get its all attributes
    NamedNodeMap attrMap = element.getAttributes();
    for(int i=0;i<attrMap.getLength();i++) {
      Attr attr = (Attr)attrMap.item(i);
      String attrNamePrefix = element.getNamespaceURI();

      String attrName = attr.getName();
      String attrValue = attr.getValue();
      if(attrNamePrefix != null && attrNamePrefix.length() > 0) {
        sb.append(" "+attrNamePrefix+":"+attrName+"=\""+attrValue+"\"");
      } else {
        sb.append(" "+attrName+"=\""+attrValue+"\"");
      }
    }
    int loc1 = tagEmpty.indexOf(elemName);
    if(loc1 != -1 && tagEmpty.charAt(loc1-1) == ',') {
      sb.append(" />");
    } else {
      sb.append(">");
    }

    //get its all children
    NodeList childrenList = element.getChildNodes();
    for(int m=0;m<childrenList.getLength();m++) {

      if(element.getTagName().equals("seg") ||
         element.getTagName().equals("note") ||
         element.getTagName().equals("prop")) {
        newline = false;
      }
      Node next = childrenList.item(m);

      //System.out.println(next.getClass() + "   level="+level);

      if(next instanceof Text) {
        //handle (#PCDATA) if the element has
        int loc2 = tagContainText.indexOf(elemName);
        if(loc2 !=-1 && tagContainText.charAt(loc2-1) == ',') {
          String content = ((Text)next).getData();

          if(content != null && content.length() > 0) {
            try {
              content = wrapXMLChars(content);
            } catch(Exception ex){

            }
            if(newline) {
              sb.append("\n");
              for(int i=0;i<level+1;i++) sb.append("  ");
            }
            sb.append(content);
          }
        }
      } else if(next instanceof Element) {
        //recursive
        prepareForNodes(sb,(Element)next,level+1,newline);
      }

    }//end while

    //end the element
    if(loc1 != -1 && tagEmpty.charAt(loc1-1) == ',') return;

    if(newline) {
      sb.append("\n");
      for(int i=0;i<level;i++) sb.append("  ");
    }
    sb.append("</"+elemName+">");
    return;
  }

  /**
   * Replace the Entity Character ("<",">","&")
   * @param string
   * @return
   * @throws java.io.IOException
   */
  private static String wrapXMLChars(String string) throws java.io.IOException {
    StringWriter writer = new StringWriter();
    BufferedReader buf = new BufferedReader(new HTMLEscapeFilterReader(new StringReader(string)));
    while (buf.ready()) {
      int i = buf.read();
      if (i == -1) break;
      else writer.write(i);
    }
    return writer.toString();
  }

}
