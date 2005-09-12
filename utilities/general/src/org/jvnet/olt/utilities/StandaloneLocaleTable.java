/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.utilities;

/*
 * StandaloneLocaleTable.java
 *
 * Created on September 5, 2002, 11:50 AM
 */

import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is used as the main Open Language Tools list of supported langauges,
 * supported encodings, and which languages map to which default encoding.
 * Where possible, the TM server uses a database (the LANGUAGE table) instead
 * of this class, but for  standalone tools, like the command line user
 * interface, or the back-converters used in the translation editor, we
 * use this locale table.
 *
 * @author  timf
 */
public class StandaloneLocaleTable implements LocaleTable {
    
    HashMap localeMap;
    HashSet codesetSet;
    
    /** Creates a new instance of StandaloneLocaleTable */
    public StandaloneLocaleTable() {
        this.localeMap = new HashMap();
        this.codesetSet = new HashSet();
        
        // this map stores the identifier -> default encoding
        localeMap.put("ar-AE","ISO-8859-6");
        localeMap.put("ar-EG","ISO-8859-6");
        localeMap.put("ar-IL","ISO-8859-6");
        localeMap.put("ar-SA","ISO-8859-6");
        localeMap.put("zh-CN","EUC-CN");
        localeMap.put("zh-HK","EUC-CN");
        localeMap.put("zh-SG","EUC-CN");
        localeMap.put("zh-TW","EUC_TW");
        localeMap.put("zh-gan","EUC-CN");
        localeMap.put("zh-guoyu","EUC-CN");
        localeMap.put("zh-hakka","EUC-CN");
        localeMap.put("zh-min","EUC-CN");
        localeMap.put("zh-min-nan","EUC-CN");
        localeMap.put("zh-wuu","EUC-CN");
        localeMap.put("zh-xiang","EUC-CN");
        localeMap.put("zh-yue","EUC-CN");
        localeMap.put("cs-CZ","ISO-8859-2");
        localeMap.put("da-DT","ISO-8859-1");
        localeMap.put("da-DK","ISO-8859-1");
        localeMap.put("nl-BE","ISO-8859-1");
        localeMap.put("nl-NL","ISO-8859-1");
        localeMap.put("en-AU","ISO-8859-1");
        localeMap.put("en-CA","ISO-8859-1");
        localeMap.put("en-GB","ISO-8859-1");
        localeMap.put("en-HK","ISO-8859-1");
        localeMap.put("en-IE","ISO-8859-1");
        localeMap.put("en-IN","ISO-8859-1");
        localeMap.put("en-LR","ISO-8859-1");
        localeMap.put("en-NZ","ISO-8859-1");
        localeMap.put("en-PH","ISO-8859-1");
        localeMap.put("en-SG","ISO-8859-1");
        localeMap.put("en-US","ISO-8859-1");
        localeMap.put("en-ZA","ISO-8859-1");
        localeMap.put("fi-FI","ISO-8859-1");
        localeMap.put("fr-BE","ISO-8859-1");
        localeMap.put("fr-CA","ISO-8859-1");
        localeMap.put("fr-CH","ISO-8859-1");
        localeMap.put("fr-FR","ISO-8859-1");
        localeMap.put("de-AT","ISO-8859-1");
        localeMap.put("de-BE","ISO-8859-1");
        localeMap.put("de-CH","ISO-8859-1");
        localeMap.put("de-DE","ISO-8859-1");
        localeMap.put("el-GR","ISO-8859-7");
        localeMap.put("he-IL","ISO-8859-8");
        localeMap.put("hi-IN","UTF-8");
        localeMap.put("hu-HU","ISO-8859-2");
        localeMap.put("ga-IE","ISO-8859-1");
        localeMap.put("it-IT","ISO-8859-1");
        localeMap.put("ja-JP","EUC_JP");
        // not in solaris yet (probably not iso1, though)
        localeMap.put("kk-KZ","ISO-8859-1");
        
        localeMap.put("ko-KR","EUC_KR");
        
        // not in solaris yet (probably not iso1)
        localeMap.put("ms-ID","ISO-8859-1");
        localeMap.put("ms-MY","ISO-8859-1");
        localeMap.put("ms-SG","ISO-8859-1");
        
        localeMap.put("no-NO","ISO-8859-1");
        localeMap.put("pl-PL","ISO-8859-2");
        localeMap.put("pt-BR","ISO-8859-1");
        localeMap.put("pt-PT","ISO-8859-1");
        localeMap.put("ru-KZ","ISO-8859-5");
        localeMap.put("ru-RU","ISO-8859-5");
        localeMap.put("uk-UA","ISO-8859-5");
        localeMap.put("sk-SK","ISO-8859-2");
        
        // bit dodgy this, it's a general "locale" for latin america,
        // but this really doesn't exist in the real world.
        localeMap.put("es-AMER","ISO-8859-1");
        
        localeMap.put("es-AR","ISO-8859-1");
        localeMap.put("es-CL","ISO-8859-1");
        localeMap.put("es-CO","ISO-8859-1");
        localeMap.put("es-ES","ISO-8859-1");
        localeMap.put("es-MX","ISO-8859-1");
        localeMap.put("es-PE","ISO-8859-1");
        localeMap.put("es-VE","ISO-8859-1");
        localeMap.put("sv-FI","ISO-8859-1");
        localeMap.put("sv-SE","ISO-8859-1");
        
        // this is probably also wrong, but isn't in solaris yet
        localeMap.put("ta-SG","ISO-8859-1");
        
        localeMap.put("th-TH","TIS620");
        localeMap.put("tr-TR","ISO-8859-9");
        
        // not in solaris yet (probably not iso1)
        localeMap.put("vi-VT","ISO-8859-1");
        
        //GNOME supported languages
        localeMap.put("af-AF","UTF-8"); //Afrikaans
        localeMap.put("am-AM","UTF-8"); //Amharic
        localeMap.put("ang-ANG","UTF-8"); //Old English
        
        localeMap.put("as-AS","UTF-8"); //Assamese
        localeMap.put("az-AZ","UTF-8"); //Azerbaijani
        localeMap.put("az-IR","UTF-8"); //Iranian Azerbaijani
        localeMap.put("be-BE","UTF-8"); //Belarusian
        localeMap.put("bg-BG","UTF-8"); //Bulgarian
        localeMap.put("bn-BN","UTF-8"); //Bengali
        localeMap.put("br-BR","UTF-8"); //Breton
        localeMap.put("bs-BS","UTF-8"); //Bosnian
        
        localeMap.put("cy-CY","UTF-8"); //Welsh
        
        localeMap.put("eo-EO","UTF-8"); //Esperanto
        
        localeMap.put("et-ET","UTF-8"); //Estonian
        localeMap.put("eu-EU","UTF-8"); //Basque
        localeMap.put("fa-FA","UTF-8"); //Persian
        
        localeMap.put("gl-GL","UTF-8"); //Galician
        localeMap.put("gu-GU","UTF-8"); //Gujarati
        
        localeMap.put("hr-HR","UTF-8"); //Croatian
        
        localeMap.put("hy-HY","UTF-8"); //Armenian
        localeMap.put("ia-IA","UTF-8"); //Interlingua
        localeMap.put("id-ID","UTF-8"); //Indonesian
        localeMap.put("is-IS","UTF-8"); //Icelandic
        
        localeMap.put("ka-KA","UTF-8"); //Georgian
        localeMap.put("kn-KN","UTF-8"); //Kannada
        
        localeMap.put("ku-KU","UTF-8"); //Kurdish
        localeMap.put("li-LI","UTF-8"); //Limburgish
        localeMap.put("lt-LT","UTF-8"); //Lithuanian
        localeMap.put("lv-LV","UTF-8"); //Latvian
        localeMap.put("mi-MI","UTF-8"); //Maori
        localeMap.put("mk-MK","UTF-8"); //Macedonian
        localeMap.put("ml-ML","UTF-8"); //Malayalam
        localeMap.put("mn-MN","UTF-8"); //Mongolian
        localeMap.put("mr-MR","UTF-8"); //Marathi
        localeMap.put("ms-MS","UTF-8"); //Malay
        localeMap.put("nb-NB","UTF-8"); //Norwegian Bookmal
        localeMap.put("ne-NE","UTF-8"); //Nepali
        
        localeMap.put("nn-NN","UTF-8"); //Norwegian Nynorsk
        localeMap.put("nso-NSO","UTF-8"); //Northern Sotho
        localeMap.put("or-OR","UTF-8"); //Oriya
        localeMap.put("pa-PA","UTF-8"); //Punjabi
        
        localeMap.put("ro-RO","UTF-8"); //Romanian
        
        localeMap.put("rw-RW","UTF-8"); //Kinyarwanda
        
        localeMap.put("sl-SL","UTF-8"); //Slovenian
        localeMap.put("sq-SQ","UTF-8"); //Albanian
        localeMap.put("sr-SR","UTF-8"); //Serbian
        localeMap.put("sr-CS","UTF-8"); //Serbian Jekavian
        
        localeMap.put("ta-TA","UTF-8"); //Tamil
        localeMap.put("te-TE","UTF-8"); //Telugu
        localeMap.put("tg-TG","UTF-8"); //Tajik
        
        localeMap.put("tk-TK","UTF-8"); //Turkmen
        localeMap.put("tl-TL","UTF-8"); //Tagalog
        
        localeMap.put("ug-UG","UTF-8"); //Uighur
        
        localeMap.put("uz-UZ","UTF-8"); //Uzbek
        localeMap.put("uz-LATN","UTF-8"); //Uzbek Latin Script
        localeMap.put("vi-VI","UTF-8"); //Vietnamese
        localeMap.put("wa-WA","UTF-8"); //Wallon
        localeMap.put("xh-XH","UTF-8"); //Xhosa
        localeMap.put("yi-YI","UTF-8"); //Yiddish
        localeMap.put("yo-YO","UTF-8"); //Yoruba
        
        localeMap.put("zu-ZU","UTF-8"); //Zulu
        
        
        codesetSet.add("ASCII");
        codesetSet.add("Big5");
        codesetSet.add("Big5");
        codesetSet.add("Big5-HKSCS");
        codesetSet.add("Big5_HKSCS");
        codesetSet.add("Big5_Solaris");
        codesetSet.add("Cp037");
        codesetSet.add("Cp1006");
        codesetSet.add("Cp1025");
        codesetSet.add("Cp1026");
        codesetSet.add("Cp1046");
        codesetSet.add("Cp1097");
        codesetSet.add("Cp1098");
        codesetSet.add("Cp1112");
        codesetSet.add("Cp1122");
        codesetSet.add("Cp1123");
        codesetSet.add("Cp1124");
        codesetSet.add("Cp1140");
        codesetSet.add("Cp1141");
        codesetSet.add("Cp1142");
        codesetSet.add("Cp1143");
        codesetSet.add("Cp1144");
        codesetSet.add("Cp1145");
        codesetSet.add("Cp1146");
        codesetSet.add("Cp1147");
        codesetSet.add("Cp1148");
        codesetSet.add("Cp1149");
        codesetSet.add("Cp1250");
        codesetSet.add("Cp1252");
        codesetSet.add("Cp1253");
        codesetSet.add("Cp1254");
        codesetSet.add("Cp1255");
        codesetSet.add("Cp1256");
        codesetSet.add("Cp1257");
        codesetSet.add("Cp1258");
        codesetSet.add("Cp1381");
        codesetSet.add("Cp1383");
        codesetSet.add("Cp273");
        codesetSet.add("Cp277");
        codesetSet.add("Cp278");
        codesetSet.add("Cp280");
        codesetSet.add("Cp284");
        codesetSet.add("Cp285");
        codesetSet.add("Cp297");
        codesetSet.add("Cp33722");
        codesetSet.add("Cp420");
        codesetSet.add("Cp424");
        codesetSet.add("Cp437");
        codesetSet.add("Cp500");
        codesetSet.add("Cp737");
        codesetSet.add("Cp775");
        codesetSet.add("Cp838");
        codesetSet.add("Cp850");
        codesetSet.add("Cp852");
        codesetSet.add("Cp855");
        codesetSet.add("Cp856");
        codesetSet.add("Cp857");
        codesetSet.add("Cp858");
        codesetSet.add("Cp860");
        codesetSet.add("Cp861");
        codesetSet.add("Cp862");
        codesetSet.add("Cp863");
        codesetSet.add("Cp864");
        codesetSet.add("Cp865");
        codesetSet.add("Cp866");
        codesetSet.add("Cp868");
        codesetSet.add("Cp869");
        codesetSet.add("Cp870");
        codesetSet.add("Cp871");
        codesetSet.add("Cp874");
        codesetSet.add("Cp875");
        codesetSet.add("Cp918");
        codesetSet.add("Cp921");
        codesetSet.add("Cp922");
        codesetSet.add("Cp930");
        codesetSet.add("Cp933");
        codesetSet.add("Cp935");
        codesetSet.add("Cp937");
        codesetSet.add("Cp939");
        codesetSet.add("Cp942");
        codesetSet.add("Cp942C");
        codesetSet.add("Cp943");
        codesetSet.add("Cp943C");
        codesetSet.add("Cp948");
        codesetSet.add("Cp949");
        codesetSet.add("Cp949C");
        codesetSet.add("Cp950");
        codesetSet.add("Cp964");
        codesetSet.add("Cp970");
        codesetSet.add("EUC-CN");
        codesetSet.add("EUC_CN");
        codesetSet.add("EUC-JP");
        codesetSet.add("EUC_JP");
        codesetSet.add("EUC-JP-LINUX");
        codesetSet.add("EUC_JP_LINUX");
        codesetSet.add("EUC-KR");
        codesetSet.add("EUC_KR");
        codesetSet.add("EUC-TW");
        codesetSet.add("EUC_TW");
        codesetSet.add("GB18030");
        codesetSet.add("GB18030");
        codesetSet.add("GBK");
        codesetSet.add("GBK");
        codesetSet.add("ISCII91");
        codesetSet.add("ISCII91");
        codesetSet.add("ISO-2022-CN-CNS");
        codesetSet.add("ISO2022CN_CNS");
        codesetSet.add("ISO-2022-CN-GB");
        codesetSet.add("ISO2022CN_GB");
        codesetSet.add("ISO-2022-JP");
        codesetSet.add("ISO2022JP");
        codesetSet.add("ISO-2022-KR");
        codesetSet.add("ISO2022KR");
        codesetSet.add("ISO-8859-1");
        codesetSet.add("ISO8859_1");
        codesetSet.add("ISO-8859-13");
        codesetSet.add("ISO8859_13");
        codesetSet.add("ISO-8859-15");
        codesetSet.add("ISO8859_15");
        codesetSet.add("ISO-8859-2");
        codesetSet.add("ISO8859_2");
        codesetSet.add("ISO-8859-3");
        codesetSet.add("ISO8859_3");
        codesetSet.add("ISO-8859-4");
        codesetSet.add("ISO8859_4");
        codesetSet.add("ISO-8859-5");
        codesetSet.add("ISO8859_5");
        codesetSet.add("ISO-8859-6");
        codesetSet.add("ISO8859_6");
        codesetSet.add("ISO-8859-7");
        codesetSet.add("ISO8859_7");
        codesetSet.add("ISO-8859-8");
        codesetSet.add("ISO8859_8");
        codesetSet.add("ISO-8859-9");
        codesetSet.add("ISO8859_9");
        codesetSet.add("JISAutoDetect");
        codesetSet.add("KOI8-R");
        codesetSet.add("KOI8_R");
        codesetSet.add("MacArabic");
        codesetSet.add("MacCentralEurope");
        codesetSet.add("MacCroatian");
        codesetSet.add("MacCyrillic");
        codesetSet.add("MacDingbat");
        codesetSet.add("MacGreek");
        codesetSet.add("MacHebrew");
        codesetSet.add("MacIceland");
        codesetSet.add("MacRoman");
        codesetSet.add("MacRomania");
        codesetSet.add("MacSymbol");
        codesetSet.add("MacThai");
        codesetSet.add("MacTurkish");
        codesetSet.add("MacUkraine");
        codesetSet.add("MS874");
        codesetSet.add("MS932");
        codesetSet.add("MS936");
        codesetSet.add("MS949");
        codesetSet.add("MS950");
        codesetSet.add("Shift_JIS");
        codesetSet.add("SJIS");
        codesetSet.add("TIS-620");
        codesetSet.add("TIS620");
        codesetSet.add("UnicodeBigUnmarked");
        codesetSet.add("UnicodeLittleUnmarked");
        codesetSet.add("US-ASCII");
        codesetSet.add("UTF-16");
        codesetSet.add("UTF-16");
        codesetSet.add("UTF-16BE");
        codesetSet.add("UTF-16LE");
        codesetSet.add("UTF-8");
        codesetSet.add("UTF8");
        codesetSet.add("windows-1250");
        codesetSet.add("windows-1252");
        codesetSet.add("windows-1253");
        codesetSet.add("windows-1254");
        codesetSet.add("windows-1255");
        codesetSet.add("windows-1256");
        codesetSet.add("windows-1257");
        codesetSet.add("windows-1258");
        codesetSet.add("windows-31j");
        codesetSet.add("windows-936");
        codesetSet.add("windows-949");
        codesetSet.add("windows-950");
        
    }
    
    /**
     * This gets us the default encoding for any language identifier.
     * If the identifier parameter isn't supported by this class, we
     * return null
     */
    public String getDefaultEncoding(java.lang.String identifier) {
        return (String)localeMap.get(identifier);
    }
    
    /**
     * This determines if an language identifier is valid (supported by
     * SunTrans2)
     */
    public boolean isValidIdentifier(java.lang.String identifier) {
        if (localeMap.containsKey(identifier))
            return true;
        else return false;
    }
    
    /**
     * This determines if a codeset name is valid (supported by SunTrans2)
     */
    public boolean isValidCodeset(String codeset) {
        if (codesetSet.contains(codeset))
            return true;
        else return false;
    }
    
}
