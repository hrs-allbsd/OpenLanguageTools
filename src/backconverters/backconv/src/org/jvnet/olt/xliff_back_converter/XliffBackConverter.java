/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/

/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.xliff_back_converter;

import java.io.*;
import java.util.Properties;
import org.jvnet.olt.utilities.XliffZipFileIO;

/**
 * The XliffBackConverter is resonsible for converting Xliff files back into
 * their original format in either a source or target language.
 *
 * @author Brian Kidney
 * @created September 5, 2002
 */
public class XliffBackConverter {

    /*
     * XLIFF DTD
     */
    private Reader xliffDTD;

    /*
     * XLIFF Skeleton DTD
     */
    private Reader skeletonDTD;
    
    private SegmentedFile segmentedFile;

    /*
     * The Logger to be used
     */
    private static java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger("org.jvnet.olt.xliff_back_converter");

    private XliffBackConverterInfo backConverterInfo;
    
    private BackConverterProperties props = new BackConverterProperties();


    /**
     * Constructor for the XliffBackConverter object
     *
     * The Xliff Back Converter takes in a XLIFF File and back converts it.
     *
     * @param theXliffDTD The XLIFF DTD
     * @param theSkeletonDTD The Skeleton DTD
     * @param theLogger The logger
     */
    public XliffBackConverter(Reader theXliffDTD, Reader theSkeletonDTD, java.util.logging.Logger theLogger) {

        this.xliffDTD = theXliffDTD;
        this.skeletonDTD = theSkeletonDTD;
        this.logger = theLogger;
        backConverterInfo = new XliffBackConverterInfo();
    }

    public void setBackconverterProperties(BackConverterProperties props){
        this.props = props;
    }
    
    /** This method is an overloaded version of the main backConvert method. It 
     * assumes that writing SGML processing instructions into the code is not 
     * necessary. It delegates to the other backConvert method.
     */
    public void backConvert(File file, String dir,
        boolean getSource, String charSet)
                            throws XliffBackConverterException{
        backConvert(file, dir, getSource, charSet, false);                  
    }
                                
    /**
     * This is the method responsible for carrying out the back conversion.
     *
     * @param file The xlz file containing a xliff file and its
     * corresponding skeleton file that are to be back converted.
     * @param dir The directory where the back converted file is to be
     * saved (e.g. /space/briank/output/).
     * @param getSource A boolean to indicate if the source is to be back
     * converted (when it is set to true) or if the target is to be back
     * converted (when it is set to false).
     * @param charSet The Charset the file is to be encoded as. See <a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">Supported Encodings</a> for possible values. This value may be set to null, in
     * which case the default encoding for the target language defined in
     * the XLIFF file will be used.
     * @exception XliffBackConverterException Thrown when a XLIIFF Back
     * conversion fails.
     * @return XliffBackConverterInfo Informatino about the
     * XliffBackConversion
     */
    public void backConvert(File file, String dir,
        boolean getSource, String charSet, boolean writeTransStatus)
                            throws XliffBackConverterException{

    	this.logger.fine("start back convert");
        /* Info about the back conversion */
        backConverterInfo = new XliffBackConverterInfo();

        /*
         * Stores the properties required by the Back Converter - This is no
         * longer used, but may be used in the future.
         */
        //BackConverterProperties props = new BackConverterProperties();

        /*
         * Create the XliffHandler and XliffParser and send the XLIFF file
         * to the parser.
         */
        try {
            org.jvnet.olt.utilities.XliffZipFileIO xlzZipFile = new org.jvnet.olt.utilities.XliffZipFileIO(file);
            if (!xlzZipFile.hasSkeleton()){
                throw new XliffBackConverterException("Unable to run the backconverter on this XLIFF  : it does not have a skeleton file associated with it.");
            }
            
            Reader xliffFile    = null;
            Reader skeletonFile = null;
            XliffHandlerImpl handlerImpl = null;
            String encUTF8 = "UTF-8";

            try{
                xliffFile    = xlzZipFile.getXliffReader();
                skeletonFile = xlzZipFile.getSklReader();



                //XliffSkeletonHandlerImpl skeletonHandlerImpl = new
                //    XliffSkeletonHandlerImpl( logger, props, dir, charSet, writeTransStatus);
                XliffSkeletonHandlerImpl skeletonHandlerImpl = new
                    XliffSkeletonHandlerImpl( logger, props, dir, encUTF8, writeTransStatus);
                XliffSkeletonHandler skeletonHandler = skeletonHandlerImpl;

                XliffSkeletonParser skeletonParser =
                    new XliffSkeletonParser(skeletonHandler, null, logger,
                    props, skeletonDTD);

                handlerImpl =
                    new XliffHandlerImpl(logger, props, skeletonParser,
                    skeletonFile, skeletonHandlerImpl, getSource,
                    backConverterInfo);
                XliffHandler handler = handlerImpl;

                XliffParser parser =
                    new XliffParser(handler, null, logger, props, xliffDTD);

                parser.parse(new org.xml.sax.InputSource(xliffFile));
            }
            finally {
                if(xliffFile != null)
                    xliffFile.close();
                if(skeletonFile != null)
                    skeletonFile.close();
            }

            SegmentedFile segFile = handlerImpl.getSegmentedFile();
            this.segmentedFile = segFile;
            String datatype = segFile.getDatatype();
            segFile.getOriginalFilename();
            String lang = segFile.getTargetLanguage();
            //System.out.println("Datatype is " + datatype);
            
            SunTrans2SpecificBackConverterFactory fac = new SunTrans2SpecificBackConverterFactory();

             //check for OpenOffice content
            {
                XliffZipFileIO xlz = xlzZipFile;
                if (xlz.hasWorkflowProperties()){
                    Properties props = xlz.getWorkflowProperties();
                    if(props != null){
                        String type = props.getProperty("xmltype");
                        if(type != null)
                            datatype = type;
                    }
                }
            }
            //backconvert
            //String convertee = dir+File.separator+segFile.getOriginalFilename();
            File convertee = new File(dir,segFile.getOriginalFilename());

            //SpecificBackConverterFactory fac = new SunTrans2SpecificBackConverterFactory(props);
            SpecificBackconverterBase specific= fac.getSpecificBackConverter(datatype);
            specific.setTargetEncoding(charSet);
            specific.setLang(lang);
            specific.setDataType(datatype);
            specific.setOriginalXlzFile(file);
//            specific.setFilename(convertee);
            logger.info("props:"+props);
            specific.setProps(props);

            //backconvert
            //String convertee = dir+File.separator+segFile.getOriginalFilename();
            //specific.convert(convertee, lang, charSet, file.getAbsolutePath());
            specific.convert(convertee);

            //if the result is *NOT* a binary file then do recoding
            if(!specific.isBinaryFormat()){
                //recode to native encoding (if needed)
                SpecificBackconverterBase recoder = new RecodingSpecificBackconverter();
                recoder.setTargetEncoding(charSet);
                recoder.setLang(lang);
                recoder.setDataType(datatype);
                recoder.setOriginalXlzFile(file);
                //recoder.setFilename(convertee);
                recoder.setProps(props);
                
                //recoder.convert(convertee, lang, charSet, file.getAbsolutePath());
                recoder.convert(convertee);
                recoder.postConvert(file);
            }      
            
            //do postconversion tasks
            
            specific.postConvert(convertee);
           
        } catch (java.util.zip.ZipException ex) {
            logger.log(java.util.logging.Level.SEVERE, "ZipException - unable to read xlz " +
                "file", ex);
            throw new XliffBackConverterException(ex.getMessage());
        } catch (org.xml.sax.SAXException ex) {
            logger.log(java.util.logging.Level.SEVERE, "SAXException ", ex);
            throw new XliffBackConverterException(ex.getMessage());
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            logger.log(java.util.logging.Level.SEVERE, "ParserConfigurationException", ex);
            throw new XliffBackConverterException(ex.getMessage());
        } catch (java.io.IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "IOException", ex);
            throw new XliffBackConverterException(ex.getMessage());
        } catch (SpecificBackConverterException ex) {
            logger.log(java.util.logging.Level.SEVERE, "SpecificBackConverterException", ex);
            throw new XliffBackConverterException(ex.getMessage());
        }
    }

    /**
     * Gets the numOfTransUnits attribute of the XliffBackConverterInfo
     * object, if the back conversion process isn't finished, then it will
     * return -1.
     *
     * @return The number of trans-units in an xliff file
     */
    public int getNumOfTransUnits() {
        return backConverterInfo.getNumOfTransUnits();
    }

    /**
     * Gets the numOfTargetUnits attribute of the XliffBackConverterInfo
     * object, if the back conversion process isn't finished, then it will
     * return -1.
     *
     * @return The number of target segments that where back converted.
     */
    public int getNumOfTargetUnits() {
        return backConverterInfo.getNumOfTargetUnits();
    }

    /**
     * Returns true if the back conversion process is finished, false
     * otherwise
     *
     * @return true if the back conversion process is finished, false
     * otherwise.
     */
    public boolean isFinished() {
        return backConverterInfo.isFinished();
    }

    public void setSegmentedFile(SegmentedFile theSegmentedFile){
    	this.segmentedFile = theSegmentedFile;
    }
    public SegmentedFile getSegmentedFile(){
    	return this.segmentedFile;
    }
}


