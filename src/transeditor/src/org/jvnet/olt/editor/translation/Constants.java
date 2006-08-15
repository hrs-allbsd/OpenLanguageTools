/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * Constants.java
 *
 * Created on April 5, 2005, 9:36 AM
 *
 */
package org.jvnet.olt.editor.translation;


/**
 *
 * @author boris
 */
final public class Constants {
    //IMPORTANT: the @ VERSION @ token is replaced during build (target pre-compile) with 
    // the version number stored in build.local.properties.
    // To keep this file intact build.xml make a copy of this file to Constants.java_ 
    // which then copied back to Constants.java after compilation
    public static final String EDITOR_VERSION = "1.2.4";
    public static final String TOOL_NAME = "Open Language Tools XLIFF Translation Editor, v" + EDITOR_VERSION;
    public static final String DOCTYPE_XLIFF_1_0 = "<!DOCTYPE xliff PUBLIC \"-//XLIFF//DTD XLIFF//EN\" \"http://www.oasis-open.org/committees/xliff/documents/xliff.dtd\">";

    //For parsing XLIFF 1.1 which is validated using schema
    public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    //empty string in context
    public static final String CONTEXT_EMPTY = "[SunTrans::Empty]";
    public static final String XLIFF_1_1_URI = "urn:oasis:names:tc:xliff:document:1.1";

    public static final String MAILING_LIST="dev@open-language-tools.dev.java.net";
    
    /** Creates a new instance of Constants */
    private Constants() {
    }
}
