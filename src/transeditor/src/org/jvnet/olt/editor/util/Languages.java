/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;


public class Languages {
    public final static String imagePath = ImagePath.PATH + "flags/";
    static final Object[][] language_code_name = {
        { "US", "English (United States)", "ISO-8859-1" },
        { "EN", "English (United Kingdom)", "ISO-8859-1" },
        { "FR", "French", "ISO-8859-1" },
        { "DE", "German", "ISO-8859-1" },
        { "IT", "Italian", "ISO-8859-1" },
        { "SV", "Swedish", "ISO-8859-1" },
        { "ES", "Spanish", "ISO-8859-1" },
        { "EA", "Spanish (Americas)", "ISO-8859-1" }, // This one is dodgy.
        { "ZH", "Simplified Chinese", "GB2312" },
        { "ZT", "Traditional Chinese", "BIG5" },
        { "JA", "Japanese", "Shift_JIS" },
        { "KO", "Korean", "EUC_KR" },
        { "CA", "Catalan", "ISO-8859-1" },
        { "CS", "Czech", "ISO-8859-2" },
        { "RU", "Russian", "ISO-8859-5" },
        { "PL", "Polish", "ISO-8859-2" },
        { "PB", "Br.Portuguese", "ISO-8859-1" },
        { "PT", "Portuguese", "ISO-8859-1" },
        { "AR", "Arabic (Saudi Arabia)", "ISO-8859-6" }, //	Arabic
        { "HE", "Hebrew", "ISO-8859-8" }, //	Hebrew
        { "UK", "Ukranian", "ISO-8859-5" }, //    Ukranian
        { "DA", "Danish", "ISO-8859-1" }, //	Danish
        { "EL", "Greek", "ISO-8859-7" }, //	Greek
        { "SK", "Slovakian", "ISO-8859-2" }, //    Slovakian
        { "TR", "Turkish", "ISO-8859-9" },
        { "HI", "Hindi", "UTF-8" },
        { "HU", "Hungarian", "ISO-8859-2" },
        { "TH", "Thai", "TIS620" },
        { "NL", "Dutch (Netherlands)", "ISO-8859-1" }, // this Dutch in Belgium is nl-NL
        { "BE", "Dutch (Belgium)", "ISO-8859-1" }, //this is Dutch in Netherlands nl-BE
        //These are languages for GNOME as of 2.12 localization
        {"AF", "Afrikaans", "UTF8" },
        { "AM", "Amharic", "UTF8" },
        { "AE", "Old English", "UTF8" },
        

        { "AS", "Assamese", "UTF8" },
        { "AZ", "Azerbaijani", "UTF8" },
        { "AI", "Iranian Azerbaijani", "UTF8" },
        { "BE", "Belarusian", "UTF8" },
        { "BG", "Bulgarian", "UTF8" },
        { "BN", "Bengali", "UTF8" },
        { "BR", "Breton", "UTF8" },
        { "BS", "Bosnian", "UTF8" },
        

        { "CY", "Welsh", "UTF8" },
        

        { "AU", "Australian English", "UTF8" },
        { "CD", "Canadian English", "UTF8" },
        { "GB", "British English", "UTF8" },
        { "EO", "Esperanto", "UTF8" },
        

        { "ET", "Estonian", "UTF8" },
        { "EU", "Basque", "UTF8" },
        { "FA", "Persian", "UTF8" },
        { "FI", "Finnish", "UTF8" },
        

        { "GA", "Irish Gaelic", "UTF8" },
        { "GL", "Galician", "UTF8" },
        { "GU", "Gujarati", "UTF8" },
        

        { "HR", "Croatian", "UTF8" },
        

        { "HY", "Armenian", "UTF8" },
        { "IA", "Interlingua", "UTF8" },
        { "ID", "Indonesian", "UTF8" },
        { "IS", "Icelandic", "UTF8" },
        

        { "KA", "Georgian", "UTF8" },
        { "KN", "Kannada", "UTF8" },
        

        { "KU", "Kurdish", "UTF8" },
        { "LI", "Limburgish", "UTF8" },
        { "LT", "Lithuanian", "UTF8" },
        { "LV", "Latvian", "UTF8" },
        { "MI", "Maori", "UTF8" },
        { "MK", "Macedonian", "UTF8" },
        { "ML", "Malayalam", "UTF8" },
        { "MN", "Mongolian", "UTF8" },
        { "MR", "Marathi", "UTF8" },
        { "MS", "Malay", "UTF8" },
        { "NB", "Norwegian Bookmal", "UTF8" },
        { "NE", "Nepali", "UTF8" },
        

        { "NN", "Norwegian Nynorsk", "UTF8" },
        { "NS", "Northern Sotho", "UTF8" },
        { "OR", "Oriya", "UTF8" },
        { "PA", "Punjabi", "UTF8" },
        

        { "RO", "Romanian", "UTF8" },
        

        { "RW", "Kinyarwanda", "UTF8" },
        

        { "SL", "Slovenian", "UTF8" },
        { "SQ", "Albanian", "UTF8" },
        { "SR", "Serbian", "UTF8" },
        { "SJ", "Serbian Jekavian", "UTF8" },
        

        { "TA", "Tamil", "UTF8" },
        { "TE", "Telugu", "UTF8" },
        { "TG", "Tajik", "UTF8" },
        

        { "TK", "Turkmen", "UTF8" },
        { "TL", "Tagalog", "UTF8" },
        

        { "UG", "Uighur", "UTF8" },
        

        { "UZ", "Uzbek", "UTF8" },
        { "UL", "Uzbek Latin Script", "UTF8" },
        { "VI", "Vietnamese", "UTF8" },
        { "WA", "Wallon", "UTF8" },
        { "XH", "Xhosa", "UTF8" },
        { "YI", "Yiddish", "UTF8" },
        { "YO", "Yoruba", "UTF8" },
        

        { "ZU", "Zulu", "UTF8" },
    };
    static Hashtable table = new Hashtable();

    static {
        for (int i = 0; i < language_code_name.length; i++) {
            table.put(language_code_name[i][0], language_code_name[i][1]);
        }
    }

    public static String getLanguageName(String code) {
        //        System.out.println("code = "+code);
        //String str = (String)name2id.get(code);
        //if(str == null) return (String)table.get(code);
        //else
        return (String)table.get(code);
    }

    public static String getLanguageCode(String lan) {
        for (int i = 0; i < language_code_name.length; i++) {
            if (((String)language_code_name[i][1]).equals(lan)) {
                return (String)language_code_name[i][0];
            }
        }

        return null;
    }

    public static String getLanguageENC(String code) {
        for (int i = 0; i < language_code_name.length; i++) {
            if (((String)language_code_name[i][0]).equals(code)) {
                return (String)language_code_name[i][2];
            }
        }

        return null;
    }

    //TODO throw some reasonable exception
    public static String getFlagPath(String code) throws Exception {
        String strPath = "";

        try {
            strPath = getLanguageName(code).replace(' ', '_');
            strPath = strPath.replace('(', '1');
            strPath = strPath.replace(')', '2');
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("LanguageError");
        }

        Languages l = new Languages();
        Object o = l.getClass().getResource(imagePath + strPath + ".gif");

        if (o == null) {
            return imagePath + "No-Flag.gif";
        } else {
            return imagePath + strPath + ".gif";
        }
    }

    public static String getFlagPathForLan(String lan) {
        lan = lan.replace(' ', '_');
        lan = lan.replace('(', '1');
        lan = lan.replace(')', '2');

        Languages l = new Languages();
        Object o = l.getClass().getResource(imagePath + lan.replace(' ', '_') + ".gif");

        if (o == null) {
            return imagePath + "No-Flag.gif";
        } else {
            return imagePath + lan.replace(' ', '_') + ".gif";
        }
    }

    public static String getFlagPathForUnknown() {
        return imagePath + "Unknown.gif";
    }

    public static Vector getLanguagesBySort() {
        Vector v = new Vector(language_code_name.length);

        for (int i = 0; i < language_code_name.length; i++) {
            String temp = getCurMinimal(v);

            if (!v.contains(temp)) {
                v.addElement(temp);
            }
        }

        return v;
    }

    private static String getCurMinimal(Vector v) {
        String min = null;

        if (v.size() == 0) {
            min = (String)language_code_name[0][1];

            for (int i = 1; i < language_code_name.length; i++) {
                if (((String)language_code_name[i][1]).compareTo(min) < 0) {
                    min = (String)language_code_name[i][1];
                }
            }
        } else {
            min = (String)v.elementAt(v.size() - 1);

            for (int i = 0; i < language_code_name.length; i++) {
                if (((String)language_code_name[i][1]).compareTo(min) > 0) {
                    min = (String)language_code_name[i][1];

                    break;
                }
            }

            for (int i = 0; i < language_code_name.length; i++) {
                String temp = (String)language_code_name[i][1];

                if ((v.indexOf(temp) == -1) && (temp.compareTo(min) < 0)) {
                    min = (String)language_code_name[i][1];
                }
            }
        }

        return min;
    }
}
