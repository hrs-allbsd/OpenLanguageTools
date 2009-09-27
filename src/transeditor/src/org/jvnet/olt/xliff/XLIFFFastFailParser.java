/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * XLIFFFastFailParser.java
 *
 * Created on April 22, 2005, 6:22 PM
 *
 */
package org.jvnet.olt.xliff;

import java.io.IOException;

import java.util.logging.Logger;

import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/** This parser parses an XML to find out what version of XLIFF it contains.
 *
 * The constructed parser MAY NOT be validating.
 * The xliff element has a version attribute which reveals which type of XLIFF it is.
 *
 * This implementation supports two types of searches. The first is searching for the xliff
 * version only, the other is searching for source and target languages in the first occurence
 * of the file element.
 *
 * When doing the version search, the search is aborted upon the first element found, by throwing an
 * exception (to abort the SAX parsing)
 * SuccessException is thrown only if xliff element was found that has an argument version, containg
 * parsable version via Version.fromString()
 * In any other case an FailureException is thrown.
 *
 *
 * It's also possible to specify the maximum treshhold of opened elements when looking for the file
 * element mainly to make the class responsive in case of a file with large header elements.
 *
 *
 * @author boris
 */
public class XLIFFFastFailParser extends DefaultHandler implements EntityResolver {
    private static final Logger logger = Logger.getLogger(XLIFFFastFailParser.class.getName());
    public static final int SEARCH_TYPE_VERSION = 0;
    public static final int SEARCH_TYPE_LANGS = 1;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_FAILED_ON_XLIFF = 1;
    public static final int STATUS_IS_XLIFF = 4;
    public static final int STATUS_FAILED_ON_TRESHHOLD = 2;
    public static final int STATUS_SUCCESS = 3;
    private boolean isXliff;
    private int status = STATUS_NONE;
    private String srcLang;
    private String tgtLang;
    private Version version;
    private String schemaLocation;
    private int elemTrashhold = Integer.MAX_VALUE;
    private int elemCount;
    private int searchType = SEARCH_TYPE_VERSION;

    public class SuccessException extends SAXException {
        SuccessException() {
            super("success");
        }
    }

    public class FailureException extends SAXException {
        private int status;

        FailureException(int status) {
            super("failure");
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public XLIFFFastFailParser(int searchType) {
        if ((searchType < SEARCH_TYPE_VERSION) || (searchType > SEARCH_TYPE_LANGS)) {
            throw new IllegalArgumentException("invalid search type:" + searchType);
        }

        this.searchType = searchType;
    }

    public void startDocument() throws org.xml.sax.SAXException {
        elemCount = 0;
    }

    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        logger.finer("Start " + qName);

        if (!isXliff) {
            isXliff = "xliff".equals(localName);
            status = isXliff ? STATUS_IS_XLIFF : STATUS_FAILED_ON_XLIFF;

            if (!isXliff) {
                throw new FailureException(status);
            }

            //get the version
            String strVer = attributes.getValue("version");

            if (strVer == null) {
                status = STATUS_FAILED_ON_XLIFF;
                throw new FailureException(status);
            }

            schemaLocation = attributes.getValue("xsi:schemaLocation");
            // add the 1.2 flavor
            if (strVer.equals("1.2")) {
                if ( schemaLocation != null && schemaLocation.contains("strict") ) {
                    strVer = strVer + "_strict";
                } else {
                    strVer = strVer + "_transitional";
                }
            }

            this.version = Version.fromString(strVer);

            if (this.version == null) {
                status = STATUS_FAILED_ON_XLIFF;
                throw new FailureException(status);
            }

            if (searchType == SEARCH_TYPE_VERSION) {
                status = STATUS_SUCCESS;
                throw new SuccessException();
            }

            return;
        }

        if (elemCount++ > elemTrashhold) {
            status = STATUS_FAILED_ON_TRESHHOLD;
            throw new FailureException(status);
        }

        if ("file".equals(localName)) {
            srcLang = attributes.getValue("source-language");
            tgtLang = attributes.getValue("target-language");

            status = STATUS_SUCCESS;
            throw new SuccessException();
        }
    }

    /*
        public boolean isXLIFF(){
            return isXliff;
        }
    */
    public Version getVersion() {
        return version;
    }

    public int getStatus() {
        return status;
    }

    /** how many opening elements to scan before giving up
     *
     */
    public void setElementsTrashHold(int maxElems) {
        this.elemTrashhold = maxElems;
    }

    public String getSourceLanguage() {
        return srcLang;
    }

    public String getTargetLanguage() {
        return tgtLang;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void warning(org.xml.sax.SAXParseException e) throws SAXException {
        logger.warning("Line "+ e.getLineNumber() + ", Column "+ e.getColumnNumber() + " : " +  e.toString());
    }

    public void fatalError(org.xml.sax.SAXParseException e) throws SAXException {
        logger.severe("Line "+ e.getLineNumber() + ", Column "+ e.getColumnNumber() + " : " +  e.toString());
        throw new FailureException(STATUS_NONE);
    }

    public void error(org.xml.sax.SAXParseException e) throws SAXException {
        logger.severe("Line "+ e.getLineNumber() + ", Column "+ e.getColumnNumber() + " : " +  e.toString());
        throw new FailureException(STATUS_NONE);
    }

    public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        logger.finer("resolve");

        return XLIFFEntityResolver.instance().resolveEntity(publicId, systemId);
    }
}
