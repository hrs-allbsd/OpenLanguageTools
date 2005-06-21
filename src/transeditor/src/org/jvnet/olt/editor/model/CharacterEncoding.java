/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.model;

import java.util.HashMap;


/**
 * Character Encoding contains the Charset IANA names and their equivalent
 * Java I/O names.
 *
 * Note: This is not the best way of doing this as it includes some layout
 * information in the model. Templating would be a better way of doing this.
 *
 * @author    kb128066
 * @created   March 24, 2003
 */
public class CharacterEncoding {
    /** A Hash Map containing the the Charset IANA name as a key and
        the Java I/O name as a value.
      */
    public static HashMap charsetMap = null;

    /** Constructor for the CharacterEncoding object */
    public CharacterEncoding() {
    }

    /**
     * Gets the java I/O name for the given IANA charset name.
     *
     * @param charset  The IANA charset name.
     * @return         The javaName value
     */
    public static String getJavaName(String charset) {
        if (charsetMap == null) {
            initCharsetMap();
        }

        if (charsetMap.containsKey(charset)) {
            return (String)charsetMap.get(charset);
        } else {
            return "";
        }
    }

    /**
     * Gets the characterEncodingList attribute of the CharacterEncoding
     * class.
     *
     *@return   The characterEncodingList value
     */
    public static String[] getCharacterEncodingList() {
        String[] strEncodingName = {
            "UTF-8", "", "Full Unicode", "------------------", "UTF-8", "UTF-16", "UTF-16BE",
            "UTF-16LE", "UCS-2", "UCS-2BE", "UCS-2LE", "JAVA", "", "European Lanuages",
            "------------------", "ASCII", "ISO-8859-1", "ISO-8859-2", "ISO-8859-3", "ISO-8859-4",
            "ISO-8859-5", "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", "ISO-8859-13",
            "ISO-8859-15", "KOI8-R", "CP1250", "CP1251", "CP1252", "CP1253", "CP1254", "CP1257",
            "CP850", "CP866", "Mac Roman", "Mac CentralEurope", "Mac Iceland", "Mac Croatian",
            "Mac Romania", "Mac Cyrillic", "Mac Ukraine", "Mac Greek", "Mac Turkish", "",
            "Semitic languages", "------------------", "ISO-8859-6", "ISO-8859-8", "CP1255",
            "CP1256", "CP862", "Mac Hebrew", "Mac Arabic", "", "Japanese", "------------------",
            "EUC-JP", "SHIFT-JIS", "CP932", "ISO-2022-JP", "", "Chinese", "------------------",
            "EUC-CN", "GBK", "EUC-TW", "BIG5", "CP950", "BIG5-HKSCS", "ISO-2022-CN", "", "Korean",
            "------------------", "EUC-KR", "CP949", "ISO-2022-KR", "JOHAB", "", "Thai",
            "------------------", "TIS-620", "CP874", "Mac Thai", "", "Laotian",
            "------------------", "CP1133", "", "Vietnamese", "------------------", "CP1258", "",
            "Other CP", "------------------", "CP037", "CP273", "CP277", "CP278", "CP280", "CP284",
            "CP285", "CP297", "CP420", "CP424", "CP437", "CP500", "CP737", "CP775", "CP838", "CP850",
            "CP852", "CP855", "CP856", "CP857", "CP858", "CP860", "CP861", "CP862", "CP863", "CP864",
            "CP865", "CP866", "CP868", "CP869", "CP870", "CP871", "CP874", "CP875", "CP918", "CP921",
            "CP922", "CP930", "CP933", "CP935", "CP937", "CP939", "CP942", "CP942C", "CP943",
            "CP943C", "CP948", "CP949C", "CP964", "CP970", "CP1006", "CP1025", "CP1026", "CP1046",
            "CP1097", "CP1098", "CP1112", "CP1122", "CP1123", "CP1124", "CP1140", "CP1141", "CP1142",
            "CP1143", "CP1144", "CP1145", "CP1146", "CP1147", "CP1148", "CP1149", "CP1381", "CP1383",
            "CP33722"
        };

        return strEncodingName;
    }

    /**
     * Generates The HashMap of the IANA charset names and their Java I/o
     * name values.
     */
    private static void initCharsetMap() {
        charsetMap = new HashMap();

        charsetMap.put("UTF-8", "UTF8");
        charsetMap.put("UTF-16", "UTF-16");
        charsetMap.put("UTF-16BE", "UTF-16BE");
        charsetMap.put("UTF-16LE", "UTF-16LE");
        charsetMap.put("UCS-2", "UTF-16");
        charsetMap.put("UCS-2BE", "UnicodeBig");
        charsetMap.put("UCS-2LE", "UnicodeLittle");
        charsetMap.put("JAVA", "UnicodeLittle");
        charsetMap.put("ASCII", "ASCII");

        charsetMap.put("ISO-8859-1", "ISO8859_1");
        charsetMap.put("ISO-8859-2", "ISO8859_2");
        charsetMap.put("ISO-8859-3", "ISO8859_3");
        charsetMap.put("ISO-8859-4", "ISO8859_4");
        charsetMap.put("ISO-8859-5", "ISO8859_5");
        charsetMap.put("ISO-8859-6", "ISO8859_6");
        charsetMap.put("ISO-8859-7", "ISO8859_7");
        charsetMap.put("ISO-8859-8", "ISO8859_8");
        charsetMap.put("ISO-8859-9", "ISO8859_9");
        charsetMap.put("ISO-8859-13", "ISO8859_13");
        charsetMap.put("ISO-8859-15", "ISO8859_15");
        charsetMap.put("ISO-2022-JP", "ISO2022JP");
        charsetMap.put("ISO-2022-KR", "ISO2022KR");

        charsetMap.put("windows-950", "MS950");
        charsetMap.put("windows-949", "MS949");
        charsetMap.put("windows-1250", "Cp1250");
        charsetMap.put("windows-1251", "Cp1251");
        charsetMap.put("windows-1252", "Cp1252");
        charsetMap.put("windows-1253", "Cp1253");
        charsetMap.put("windows-1254", "Cp1254");
        charsetMap.put("windows-1255", "Cp1255");
        charsetMap.put("windows-1256", "Cp1256");
        charsetMap.put("windows-1257", "Cp1257");
        charsetMap.put("windows-1258", "Cp1258");

        charsetMap.put("CP950", "MS950");
        charsetMap.put("CP949", "MS949");
        charsetMap.put("CP1250", "Cp1250");
        charsetMap.put("CP1251", "Cp1251");
        charsetMap.put("CP1252", "Cp1252");
        charsetMap.put("CP1253", "Cp1253");
        charsetMap.put("CP1254", "Cp1254");
        charsetMap.put("CP1255", "Cp1255");
        charsetMap.put("CP1256", "Cp1256");
        charsetMap.put("CP1257", "Cp1257");
        charsetMap.put("CP1258", "Cp1258");

        charsetMap.put("IBM932", "MS932");
        charsetMap.put("IBM1133", "Cp1133");
        charsetMap.put("CP932", "MS932");
        charsetMap.put("CP1133", "Cp1133");

        charsetMap.put("Mac Roman", "MacRoman");
        charsetMap.put("Mac CentralEurope", "MacCentralEurope");
        charsetMap.put("Mac Iceland", "MacIceland");
        charsetMap.put("Mac Croatian", "MacCroatian");
        charsetMap.put("Mac Romania", "MacRomania");
        charsetMap.put("Mac Cyrillic", "MacCyrillic");
        charsetMap.put("Mac Ukraine", "MacUkraine");
        charsetMap.put("Mac Greek", "MacGreek");
        charsetMap.put("Mac Turkish", "MacTurkish");
        charsetMap.put("Mac Hebrew", "MacHebrew");
        charsetMap.put("Mac Arabic", "MacArabic");
        charsetMap.put("Mac Thai", "MacThai");

        charsetMap.put("JISAutoDetect", "JISAutoDetect");
        charsetMap.put("GB18030", "GB18030");
        charsetMap.put("EUC-JP-LINUX", "EUC_JP_LINUX");
        charsetMap.put("windows-31j", "MS932");
        charsetMap.put("KOI8-R", "KOI8_R");
        charsetMap.put("eucJP", "EUC_JP");
        charsetMap.put("EUC-JP", "EUC_JP");
        charsetMap.put("SHIFT-JIS", "SJIS");

        charsetMap.put("zh_CN-euc", "EUC_CN");
        charsetMap.put("zh_TW-euc", "EUC_TW");
        charsetMap.put("EUC-CN", "EUC_CN");
        charsetMap.put("GBK", "MS936");
        charsetMap.put("EUC-TW", "EUC_TW");
        charsetMap.put("BIG5", "Big5");
        charsetMap.put("BIG5-HKSCS", "Big5-HKSCS");
        charsetMap.put("ISO-2022-CN", "ISO2022CN");
        charsetMap.put("ko_kr.euc", "EUC_KR");
        charsetMap.put("EUC-KR", "EUC_KR");

        charsetMap.put("JOHAB", "Johab");
        charsetMap.put("TIS-620", "TIS620");

        charsetMap.put("IBM037", "Cp037");
        charsetMap.put("IBM273", "Cp273");
        charsetMap.put("IBM277", "Cp277");
        charsetMap.put("IBM278", "Cp278");
        charsetMap.put("IBM280", "Cp280");
        charsetMap.put("IBM284", "Cp284");
        charsetMap.put("IBM285", "Cp285");
        charsetMap.put("IBM297", "Cp297");
        charsetMap.put("IBM420", "Cp420");
        charsetMap.put("IBM424", "Cp424");
        charsetMap.put("IBM437", "Cp437");
        charsetMap.put("IBM500", "Cp500");
        charsetMap.put("IBM737", "Cp737");
        charsetMap.put("IBM775", "Cp775");
        charsetMap.put("IBM838", "Cp838");
        charsetMap.put("IBM850", "Cp850");
        charsetMap.put("IBM852", "Cp852");
        charsetMap.put("IBM855", "Cp855");
        charsetMap.put("IBM856", "Cp856");
        charsetMap.put("IBM857", "Cp857");
        charsetMap.put("IBM858", "Cp858");
        charsetMap.put("IBM860", "Cp860");
        charsetMap.put("IBM861", "Cp861");
        charsetMap.put("IBM862", "Cp862");
        charsetMap.put("IBM863", "Cp863");
        charsetMap.put("IBM864", "Cp864");
        charsetMap.put("IBM865", "Cp865");
        charsetMap.put("IBM866", "Cp866");
        charsetMap.put("IBM868", "Cp868");
        charsetMap.put("IBM869", "Cp869");
        charsetMap.put("IBM870", "Cp870");
        charsetMap.put("IBM871", "Cp871");
        charsetMap.put("IBM874", "Cp874");
        charsetMap.put("IBM875", "Cp875");
        charsetMap.put("IBM918", "Cp918");

        /*
         * The following are alias for IBM above
         */
        charsetMap.put("CP037", "Cp037");
        charsetMap.put("CP273", "Cp273");
        charsetMap.put("CP277", "Cp277");
        charsetMap.put("CP278", "Cp278");
        charsetMap.put("CP280", "Cp280");
        charsetMap.put("CP284", "Cp284");
        charsetMap.put("CP285", "Cp285");
        charsetMap.put("CP297", "Cp297");
        charsetMap.put("CP420", "Cp420");
        charsetMap.put("CP424", "Cp424");
        charsetMap.put("CP437", "Cp437");
        charsetMap.put("CP500", "Cp500");
        charsetMap.put("CP737", "Cp737");
        charsetMap.put("CP775", "Cp775");
        charsetMap.put("CP838", "Cp838");
        charsetMap.put("CP850", "Cp850");
        charsetMap.put("CP852", "Cp852");
        charsetMap.put("CP855", "Cp855");
        charsetMap.put("CP856", "Cp856");
        charsetMap.put("CP857", "Cp857");
        charsetMap.put("CP858", "Cp858");
        charsetMap.put("CP860", "Cp860");
        charsetMap.put("CP861", "Cp861");
        charsetMap.put("CP862", "Cp862");
        charsetMap.put("CP863", "Cp863");
        charsetMap.put("CP864", "Cp864");
        charsetMap.put("CP865", "Cp865");
        charsetMap.put("CP866", "Cp866");
        charsetMap.put("CP868", "Cp868");
        charsetMap.put("CP869", "Cp869");
        charsetMap.put("CP870", "Cp870");
        charsetMap.put("CP871", "Cp871");
        charsetMap.put("CP874", "Cp874");
        charsetMap.put("CP875", "Cp875");
        charsetMap.put("CP918", "Cp918");

        /*
         * End alias for IBM
         */
        charsetMap.put("CP921", "Cp921");
        charsetMap.put("CP922", "Cp922");
        charsetMap.put("CP930", "Cp930");
        charsetMap.put("CP933", "Cp933");
        charsetMap.put("CP935", "Cp935");
        charsetMap.put("CP937", "Cp937");
        charsetMap.put("CP939", "Cp939");
        charsetMap.put("CP942", "Cp942");
        charsetMap.put("CP942C", "Cp942C");
        charsetMap.put("CP943", "Cp943");
        charsetMap.put("CP943C", "Cp943C");
        charsetMap.put("CP948", "Cp948");
        charsetMap.put("CP949C", "Cp949C");
        charsetMap.put("CP964", "Cp964");
        charsetMap.put("CP970", "Cp970");
        charsetMap.put("CP1006", "Cp1006");
        charsetMap.put("CP1025", "Cp1025");
        charsetMap.put("CP1026", "Cp1026");
        charsetMap.put("CP1046", "Cp1046");
        charsetMap.put("CP1097", "Cp1097");
        charsetMap.put("CP1098", "Cp1098");
        charsetMap.put("CP1112", "Cp1112");
        charsetMap.put("CP1122", "Cp1122");
        charsetMap.put("CP1123", "Cp1123");
        charsetMap.put("CP1124", "Cp1124");
        charsetMap.put("CP1140", "Cp1140");
        charsetMap.put("CP1141", "Cp1141");
        charsetMap.put("CP1142", "Cp1142");
        charsetMap.put("CP1143", "Cp1143");
        charsetMap.put("CP1144", "Cp1144");
        charsetMap.put("CP1145", "Cp1145");
        charsetMap.put("CP1146", "Cp1146");
        charsetMap.put("CP1147", "Cp1147");
        charsetMap.put("CP1148", "Cp1148");
        charsetMap.put("CP1149", "Cp1149");
        charsetMap.put("CP1381", "Cp1381");
        charsetMap.put("CP1383", "Cp1383");
        charsetMap.put("CP33722", "Cp33722");
    }
}
