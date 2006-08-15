/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.util.Hashtable;
import org.jvnet.olt.editor.util.Bundle;
import java.util.Vector;


public class Languages {
    private static Bundle bundle = Bundle.getBundle(Languages.class.getName());
    public final static String imagePath = ImagePath.PATH + "flags/";
    static final Object[][] language_code_name = {
        { "US", bundle.getString("English_(United_States)"), "ISO-8859-1" },
        { "EN", bundle.getString("English_(United_Kingdom)"), "ISO-8859-1" },
        { "FR", bundle.getString("French"), "ISO-8859-1" },
        { "DE", bundle.getString("German"), "ISO-8859-1" },
        { "IT", bundle.getString("Italian"), "ISO-8859-1" },
        { "SV", bundle.getString("Swedish"), "ISO-8859-1" },
        { "ES", bundle.getString("Spanish"), "ISO-8859-1" },
        { "EA", bundle.getString("Spanish_(Americas)"), "ISO-8859-1" }, // This one is dodgy.
        { "ZH", bundle.getString("Simplified_Chinese"), "GB2312" },
        { "ZT", bundle.getString("Traditional_Chinese"), "BIG5" },
        { "JA", bundle.getString("Japanese"), "Shift_JIS" },
        { "KO", bundle.getString("Korean"), "EUC_KR" },
        { "CA", bundle.getString("Catalan"), "ISO-8859-1" },
        { "CS", bundle.getString("Czech"), "ISO-8859-2" },
        { "RU", bundle.getString("Russian"), "ISO-8859-5" },
        { "PL", bundle.getString("Polish"), "ISO-8859-2" },
        { "PB", bundle.getString("Br.Portuguese"), "ISO-8859-1" },
        { "PT", bundle.getString("Portuguese"), "ISO-8859-1" },
        { "AR", bundle.getString("Arabic_(Saudi_Arabia)"), "ISO-8859-6" }, //	Arabic
        { "HE", bundle.getString("Hebrew"), "ISO-8859-8" }, //	Hebrew
        { "UK", bundle.getString("Ukranian"), "ISO-8859-5" }, //    Ukranian
        { "DA", bundle.getString("Danish"), "ISO-8859-1" }, //	Danish
        { "EL", bundle.getString("Greek"), "ISO-8859-7" }, //	Greek
        { "SK", bundle.getString("Slovakian"), "ISO-8859-2" }, //    Slovakian
        { "TR", bundle.getString("Turkish"), "ISO-8859-9" },
        { "HI", bundle.getString("Hindi"), "UTF-8" },
        { "HU", bundle.getString("Hungarian"), "ISO-8859-2" },
        { "TH", bundle.getString("Thai"), "TIS620" },
        { "NL", bundle.getString("Dutch_(Netherlands)"), "ISO-8859-1" }, // this Dutch in Belgium is nl-NL
        { "BE", bundle.getString("Dutch_(Belgium)"), "ISO-8859-1" }, //this is Dutch in Netherlands nl-BE
        //These are languages for GNOME as of 2.12 localization
        {"AF", bundle.getString("Afrikaans"), "UTF8" },
        { "AM", bundle.getString("Amharic"), "UTF8" },
        { "AE", bundle.getString("Old_English"), "UTF8" },
        

        { "AS", bundle.getString("Assamese"), "UTF8" },
        { "AZ", bundle.getString("Azerbaijani"), "UTF8" },
        { "AI", bundle.getString("Iranian_Azerbaijani"), "UTF8" },
        { "BE", bundle.getString("Belarusian"), "UTF8" },
        { "BG", bundle.getString("Bulgarian"), "UTF8" },
        { "BN", bundle.getString("Bengali"), "UTF8" },
        { "BR", bundle.getString("Breton"), "UTF8" },
        { "BS", bundle.getString("Bosnian"), "UTF8" },
        

        { "CY", bundle.getString("Welsh"), "UTF8" },
        

        { "AU", bundle.getString("Australian_English"), "UTF8" },
        { "CD", bundle.getString("Canadian_English"), "UTF8" },
        { "GB", bundle.getString("British_English"), "UTF8" },
        { "EO", bundle.getString("Esperanto"), "UTF8" },
        

        { "ET", bundle.getString("Estonian"), "UTF8" },
        { "EU", bundle.getString("Basque"), "UTF8" },
        { "FA", bundle.getString("Persian"), "UTF8" },
        { "FI", bundle.getString("Finnish"), "UTF8" },
        

        { "GA", bundle.getString("Irish_Gaelic"), "UTF8" },
        { "GL", bundle.getString("Galician"), "UTF8" },
        { "GU", bundle.getString("Gujarati"), "UTF8" },
        

        { "HR", bundle.getString("Croatian"), "UTF8" },
        

        { "HY", bundle.getString("Armenian"), "UTF8" },
        { "IA", bundle.getString("Interlingua"), "UTF8" },
        { "ID", bundle.getString("Indonesian"), "UTF8" },
        { "IS", bundle.getString("Icelandic"), "UTF8" },
        

        { "KA", bundle.getString("Georgian"), "UTF8" },
        { "KN", bundle.getString("Kannada"), "UTF8" },
        

        { "KU", bundle.getString("Kurdish"), "UTF8" },
        { "LI", bundle.getString("Limburgish"), "UTF8" },
        { "LT", bundle.getString("Lithuanian"), "UTF8" },
        { "LV", bundle.getString("Latvian"), "UTF8" },
        { "MI", bundle.getString("Maori"), "UTF8" },
        { "MK", bundle.getString("Macedonian"), "UTF8" },
        { "ML", bundle.getString("Malayalam"), "UTF8" },
        { "MN", bundle.getString("Mongolian"), "UTF8" },
        { "MR", bundle.getString("Marathi"), "UTF8" },
        { "MS", bundle.getString("Malay"), "UTF8" },
        { "NB", bundle.getString("Norwegian_Bookmal"), "UTF8" },
        { "NE", bundle.getString("Nepali"), "UTF8" },
        

        { "NN", bundle.getString("Norwegian_Nynorsk"), "UTF8" },
        { "NS", bundle.getString("Northern_Sotho"), "UTF8" },
        { "OR", bundle.getString("Oriya"), "UTF8" },
        { "PA", bundle.getString("Punjabi"), "UTF8" },
        

        { "RO", bundle.getString("Romanian"), "UTF8" },
        

        { "RW", bundle.getString("Kinyarwanda"), "UTF8" },
        

        { "SL", bundle.getString("Slovenian"), "UTF8" },
        { "SQ", bundle.getString("Albanian"), "UTF8" },
        { "SR", bundle.getString("Serbian"), "UTF8" },
        { "SJ", bundle.getString("Serbian_Jekavian"), "UTF8" },
        

        { "TA", bundle.getString("Tamil"), "UTF8" },
        { "TE", bundle.getString("Telugu"), "UTF8" },
        { "TG", bundle.getString("Tajik"), "UTF8" },
        

        { "TK", bundle.getString("Turkmen"), "UTF8" },
        { "TL", bundle.getString("Tagalog"), "UTF8" },
        

        { "UG", bundle.getString("Uighur"), "UTF8" },
        

        { "UZ", bundle.getString("Uzbek"), "UTF8" },
        { "UL", bundle.getString("Uzbek_Latin_Script"), "UTF8" },
        { "VI", bundle.getString("Vietnamese"), "UTF8" },
        { "WA", bundle.getString("Wallon"), "UTF8" },
        { "XH", bundle.getString("Xhosa"), "UTF8" },
        { "YI", bundle.getString("Yiddish"), "UTF8" },
        { "YO", bundle.getString("Yoruba"), "UTF8" },
        

        { "ZU", bundle.getString("Zulu"), "UTF8" },
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
            String longCode = LanguageMappingTable.getInstance().reverseTranslateLangCode(code);
            
            if(longCode == null)
                strPath ="Unknown";
            else
                strPath = longCode.toLowerCase();
            
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
