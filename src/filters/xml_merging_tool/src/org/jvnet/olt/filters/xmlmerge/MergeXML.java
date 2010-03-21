//
///*
// * Copyright  2005 Sun Microsystems, Inc.
// * All rights reserved Use is subject to license terms.
// *
// */
//TODO: check and remove - never used but in tests
//package org.jvnet.olt.filters.xmlmerge;
///*
// * MergeXML.java
// *
// */
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import org.xml.sax.InputSource;
//import org.xml.sax.XMLReader;
//import org.xml.sax.SAXException;
//import javax.xml.transform.OutputKeys;
//
//import org.apache.xalan.serialize.Serializer;
//import org.apache.xalan.serialize.SerializerFactory;
//import org.apache.xalan.templates.OutputProperties;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Properties;
///**
// *
// * @author  Administrator
// */
//public class MergeXML {
//
//    private static final String VALIDATION_FEATURE_ID =
//                      "http://apache.org/xml/features/nonvalidating/load-external-dtd";
//
//    private MergingInfo mergingInfo;
//
//    /** Creates a new instance of MergeXML */
//    public MergeXML( MergingInfo mergingInfo){
//        this.mergingInfo = new MergingInfo(mergingInfo);
//        mergingInfo.validate();
//
//    }
//
//    /** Merge two xml file
//     * @exception MergeXMLFailedException if merging xml failed
//     */
//    public void generateNewXML() throws MergeXMLFailedException{
//
//        //get the original and translated parser instance
//        OriginalParser op = getOriginalParserInstance(mergingInfo.getMergeType());
//        TransParser tp = getTransParserInstance(mergingInfo.getMergeType());
//        SAXParser saxParser = null;
//        //long start = new Date().getTime();
//        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
//
//        //set the sax factory
//        try{
//            saxFactory.setFeature(VALIDATION_FEATURE_ID,false);
//            saxFactory.setValidating(false);
//            saxFactory.setNamespaceAware(true);
//        }catch(org.xml.sax.SAXNotRecognizedException snre){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not init the saxFactory ! " + snre.getMessage());
//            mxfe.setStackTrace(snre.getStackTrace());
//            throw mxfe;
//        }catch(javax.xml.parsers.ParserConfigurationException pce){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not init the saxFactory! " + pce.getMessage());
//            mxfe.setStackTrace(pce.getStackTrace());
//            throw mxfe;
//        }catch(org.xml.sax.SAXNotSupportedException snse){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not init the saxFactory ! " + snse.getMessage());
//            mxfe.setStackTrace(snse.getStackTrace());
//            throw mxfe;
//        }
//
//        //get the saxparser
//        try{
//            saxParser = saxFactory.newSAXParser();
//        }catch( org.xml.sax.SAXException se){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not init the saxParser!" + se.getMessage());
//            mxfe.setStackTrace(se.getStackTrace());
//            throw mxfe;
//        }catch(javax.xml.parsers.ParserConfigurationException pce){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not init the saxParser!" + pce.getMessage());
//            mxfe.setStackTrace(pce.getStackTrace());
//            throw mxfe;
//        }
//
//        //parse the translated parser
//        try{
//            saxParser.parse( mergingInfo.getTransInputSource() , tp);
//        }catch(SAXException se){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not parse the translated xml file!" + se.getMessage());
//            mxfe.setStackTrace(se.getStackTrace());
//            throw mxfe;
//        }catch(java.io.IOException ioe){
//            MergeXMLFailedException mxfe = new MergeXMLFailedException("Can not parse the translated xml file!" + ioe.getMessage());
//            mxfe.setStackTrace(ioe.getStackTrace());
//            throw mxfe;
//        }
//
//        //transfer the source content from translated parser to original parser
//        op.setSourceContentMap(tp.getSourceContentMap());
//
//        XMLReader xmlReader = null;
//        Serializer serializer = SerializerFactory.getSerializer(
//                OutputProperties.getDefaultMethodProperties("xml"));
//        serializer.setWriter(mergingInfo.getNewFileWriter());
//        Properties properties = new Properties();
//        serializer.setOutputFormat(properties);
//
//        //parse the original file and generate the new xml file
//       try{
//            xmlReader = saxParser.getXMLReader();
//
//            op.setParent(xmlReader);
//            op.setContentHandler(serializer.asContentHandler());
//
//            xmlReader.setProperty( "http://xml.org/sax/properties/lexical-handler", op );
//            op.parse(mergingInfo.getOriginalInputSource());
//        }catch(SAXException se){
//            MergeXMLFailedException mxfe =
//                    new MergeXMLFailedException("Can not parse the original xml file!\n" + se.getMessage());
//            //e.printStackTrace();
//            mxfe.setStackTrace(se.getStackTrace());
//            throw mxfe;
//        }catch(IOException ioe){
//            MergeXMLFailedException mxfe =
//                    new MergeXMLFailedException("Can not parse the original xml file!\n" + ioe.getMessage());
//            //e.printStackTrace();
//            mxfe.setStackTrace(ioe.getStackTrace());
//            throw mxfe;
//        }
//
//    }
//
//    /**Get the translated Parser Instance by mergeType
//     */
//    private TransParser getTransParserInstance(int mergeType){
//        switch (mergeType){
//            case MergeType.MERGE_BY_PARENT_ELEMENT_ID:
//                return new TPByElementID(mergingInfo);
//            case MergeType.MERGE_BY_SAME_ORDER:
//                return new TPBySameOrder(mergingInfo);
//            case MergeType.MERGE_BY_SOURCE_ID:
//                return new TPBySourceID(mergingInfo);
//            case MergeType.MERGE_XLIFF:
//                return new TPXliff(mergingInfo);
//        }
//        return null;
//    }
//
//    /** Get the original Parser Instrance by the mergeType
//     */
//    private OriginalParser getOriginalParserInstance(int mergeType){
//
//        switch (mergeType){
//            case MergeType.MERGE_BY_PARENT_ELEMENT_ID:
//                return new OPByElementID(mergingInfo);
//            case MergeType.MERGE_BY_SAME_ORDER:
//                return new OPBySameOrder(mergingInfo);
//            case MergeType.MERGE_BY_SOURCE_ID:
//                return new OPBySourceID(mergingInfo);
//            case MergeType.MERGE_XLIFF:
//                return new OPXliff(mergingInfo);
//        }
//        return null;
//    }
////    private Properties getOutputProperties(InputSource ins){
////        Properties properties = new Properties();
////
////        if(ins.getPublicId()!=null)
////             properties.put(OutputKeys.DOCTYPE_PUBLIC , ins.getPublicId());
////
////        if(ins.getEncoding()!=null)
////            properties.put(OutputKeys.ENCODING , ins.getEncoding());
////        System.out.println(ins.getEncoding());
////
////        return properties;
////    }
//
//}
