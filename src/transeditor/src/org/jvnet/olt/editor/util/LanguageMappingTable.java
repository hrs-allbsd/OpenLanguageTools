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
package org.jvnet.olt.editor.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * LanguageMappingTable.java
 *
 * Created on 11 June 2003, 14:34
 */
import java.util.StringTokenizer;


/**
 * This class is a kludge to translate <language>-<territory> codes to the
 * Editor internal codes. It is a stopgap class to fix bug . It will no
 * longer be needed once bug 4866728 is fixed.
 * It uses the Singleton and Factory Method patterns to ensure that there is
 * only one instance of this class in existence.
 * @author  jc73554
 */
public class LanguageMappingTable {
    private static LanguageMappingTable instance = null;
    private java.util. HashMap<String, String> mappingTable;

    LanguageMappingTable() {
        //  Create and initialize the table
        mappingTable = new java.util.HashMap<String,String>();
        mappingTable.put("en-gb", "EN");
        mappingTable.put("en-us", "US");
        mappingTable.put("en", "US");
        mappingTable.put("fr-fr", "FR");
        mappingTable.put("fr", "FR");
        mappingTable.put("de-de", "DE");
        mappingTable.put("de", "DE");
        mappingTable.put("it-it", "IT");
        mappingTable.put("it", "IT");
        mappingTable.put("sv-se", "SV");
        mappingTable.put("sv", "SV");
        mappingTable.put("es-es", "ES");
        mappingTable.put("es", "ES");
        mappingTable.put("zh-cn", "ZH");
        mappingTable.put("zh", "ZH");
        mappingTable.put("zh-tw", "ZT");
        mappingTable.put("ja-jp", "JA");
        mappingTable.put("ja", "JA");
        mappingTable.put("ko-kr", "KO");
        mappingTable.put("ko", "KO");
        mappingTable.put("cs-cz", "CS");
        mappingTable.put("cs", "CS");
        mappingTable.put("ru-ru", "RU");
        mappingTable.put("ru", "RU");
        mappingTable.put("pl-pl", "PL");
        mappingTable.put("pl", "PL");
        mappingTable.put("pt-br", "PB");
        mappingTable.put("pt-pt", "PT");
        mappingTable.put("pt", "PT");
        mappingTable.put("hi-in", "HI");
        mappingTable.put("ar-sa", "AR"); //  Arabic
        mappingTable.put("he-il", "HE"); //  Hebrew
        mappingTable.put("tr", "TR"); //  Turkish
        mappingTable.put("tr-tr", "TR"); //  Turkish
        mappingTable.put("es-amer", "EA"); //  Latin American Spanish

        mappingTable.put("uk-ua", "UK"); //  Ukranian
        mappingTable.put("ua", "UK"); //  Ukranian
        mappingTable.put("da-dk", "DA"); //  Danish
        mappingTable.put("dk", "DA"); //  Danish
        mappingTable.put("el-gr", "EL"); //  Greek
        mappingTable.put("gr", "EL"); //  Greek
        mappingTable.put("sk-sk", "SK");
        mappingTable.put("sk", "SK");
        mappingTable.put("hu-hu","HU");
        mappingTable.put("hu","HU");
        mappingTable.put("th-th","TH");
        mappingTable.put("th","TH");
        mappingTable.put("nl-be","BE");
        mappingTable.put("nl-nl","NL");
        mappingTable.put("nl","NL");

        //GNOME supported langs
        mappingTable.put("af-af", "AF"); //Afrikaans
        mappingTable.put("af", "AF"); //Afrikaans
        mappingTable.put("am-am", "AM"); //Amharic
        mappingTable.put("am", "AM"); //Amharic
        mappingTable.put("ang-ang", "AE"); //Old English

        mappingTable.put("as-as", "AS"); //Assamese
        mappingTable.put("as", "AS"); //Assamese
        mappingTable.put("az-az", "AZ"); //Azerbaijani
        mappingTable.put("az-ir", "AI"); //Iranian Azerbaijani
        mappingTable.put("az", "AZ"); //Azerbaijani
        mappingTable.put("be-by", "BY"); //Belarusian
        mappingTable.put("be", "BY"); //Belarusian
        mappingTable.put("bg-bg", "BG"); //Bulgarian
        mappingTable.put("bg", "BG"); //Bulgarian
        mappingTable.put("bn-bn", "BN"); //Bengali
        mappingTable.put("bn", "BN"); //Bengali
        mappingTable.put("br-br", "BR"); //Breton
        mappingTable.put("br", "BR"); //Breton
        mappingTable.put("bs-bs", "BS"); //Bosnian
        mappingTable.put("bs", "BS"); //Bosnian

        mappingTable.put("cy-cy", "CY"); //Welsh
        mappingTable.put("cy", "CY"); //Welsh

        mappingTable.put("en-au", "AU"); //Australian English
        mappingTable.put("en-ca", "CD"); //Canadian English
        // we cannot have two times "en-gb" as key. there is no internal "GB" language
        //mappingTable.put("en-gb", "GB"); //British English
        mappingTable.put("eo-eo", "EO"); //Esperanto

        mappingTable.put("et-et", "ET"); //Estonian
        mappingTable.put("et", "ET"); //Estonian
        mappingTable.put("eu-eu", "EU"); //Basque
        mappingTable.put("eu", "EU"); //Basque
        mappingTable.put("eu-eu", "EU"); //Basque
        mappingTable.put("fi-fi", "FI"); //Finnish
        mappingTable.put("fi", "FI"); //Finnish

        mappingTable.put("ga-ie", "GA"); //Irish Gaelic
        mappingTable.put("ga-ga", "GA"); //Irish Gaelic
        mappingTable.put("ga", "GA"); //Irish Gaelic

        mappingTable.put("gl-gl", "GL"); //Galician
        mappingTable.put("gl", "GL"); //Galician
        mappingTable.put("gu-gu", "GU"); //Gujarati
        mappingTable.put("gu", "GU"); //Gujarati

        mappingTable.put("hr-hr", "HR"); //Croatian
        mappingTable.put("hr", "HR"); //Croatian

        mappingTable.put("hy-hy", "HY"); //Armenian
        mappingTable.put("hy", "HY"); //Armenian
        mappingTable.put("ia-ia", "IA"); //Interlingua
        mappingTable.put("ia", "IA"); //Interlingua
        mappingTable.put("id-id", "ID"); //Indonesian
        mappingTable.put("id", "ID"); //Indonesian
        mappingTable.put("is-is", "IS"); //Icelandic
        mappingTable.put("is", "IS"); //Icelandic

        mappingTable.put("ka-ka", "KA"); //Georgian
        mappingTable.put("ka", "KA"); //Georgian
        mappingTable.put("kn-kn", "KN"); //Kannada
        mappingTable.put("kn", "KN"); //Kannada

        mappingTable.put("ku-ku", "KU"); //Kurdish
        mappingTable.put("ku", "KU"); //Kurdish
        mappingTable.put("li-li", "LI"); //Limburgish
        mappingTable.put("li", "LI"); //Limburgish
        mappingTable.put("lt-lt", "LT"); //Lithuanian
        mappingTable.put("lt", "LT"); //Lithuanian
        mappingTable.put("lv-lv", "LV"); //Latvian
        mappingTable.put("lv", "LV"); //Latvian
        mappingTable.put("mi-mi", "MI"); //Maori
        mappingTable.put("mi", "MI"); //Maori
        mappingTable.put("mk-mk", "MK"); //Macedonian
        mappingTable.put("mk", "MK"); //Macedonian
        mappingTable.put("ml-ml", "ML"); //Malayalam
        mappingTable.put("ml", "ML"); //Malayalam
        mappingTable.put("mn-mn", "MN"); //Mongolian
        mappingTable.put("mn", "MN"); //Mongolian
        mappingTable.put("mr-mr", "MR"); //Marathi
        mappingTable.put("mr", "MR"); //Marathi
        mappingTable.put("ms-ms", "MS"); //Malay
        mappingTable.put("ms", "MS"); //Malay
        mappingTable.put("nb-nb", "NB"); //Norwegian Bookmal
        mappingTable.put("nb", "NB"); //Norwegian Bookmal
        mappingTable.put("ne-ne", "NE"); //Nepali
        mappingTable.put("ne", "NE"); //Nepali

        mappingTable.put("nn-nn", "NN"); //Norwegian Nynorsk
        mappingTable.put("nn", "NN"); //Norwegian Nynorsk
        mappingTable.put("nso-nso", "NS"); //Northern Sotho
        mappingTable.put("nso", "NS"); //Northern Sotho
        mappingTable.put("or-or", "OR"); //Oriya
        mappingTable.put("or", "OR"); //Oriya
        mappingTable.put("pa-pa", "PA"); //Punjabi
        mappingTable.put("pa", "PA"); //Punjabi

        mappingTable.put("ro-ro", "RO"); //Romanian
        mappingTable.put("ro", "RO"); //Romanian

        mappingTable.put("rw-rw", "RW"); //Kinyarwanda
        mappingTable.put("rw", "RW"); //Kinyarwanda

        mappingTable.put("sl-sl", "SL"); //Slovenian
        mappingTable.put("sl", "SL"); //Slovenian
        mappingTable.put("sq-sq", "SQ"); //Albanian
        mappingTable.put("sq", "SQ"); //Albanian
        mappingTable.put("sr-sr", "SR"); //Serbian
        mappingTable.put("sr-cs", "SJ"); //Serbian Jekavian
        mappingTable.put("sr", "SR"); //Serbian

        mappingTable.put("ta-ta", "TA"); //Tamil
        mappingTable.put("ta", "TA"); //Tamil
        mappingTable.put("te-te", "TE"); //Telugu
        mappingTable.put("te", "TE"); //Telugu
        mappingTable.put("tg-tg", "TG"); //Tajik
        mappingTable.put("tg", "TG"); //Tajik

        mappingTable.put("tk-tk", "TK"); //Turkmen
        mappingTable.put("tk", "TK"); //Turkmen
        mappingTable.put("tl-tl", "TL"); //Tagalog
        mappingTable.put("tl", "TL"); //Tagalog

        mappingTable.put("ug-ug", "UG"); //Uighur
        mappingTable.put("ug", "UG"); //Uighur

        mappingTable.put("uz-uz", "UZ"); //Uzbek
        mappingTable.put("uz-latn", "UL"); //Uzbek Latin Script
        mappingTable.put("uz", "UZ"); //Uzbek
        mappingTable.put("vi-vn", "VI"); //Vietnamese
        mappingTable.put("vi", "VI"); //Vietnamese
        mappingTable.put("wa-wa", "WA"); //Wallon
        mappingTable.put("wa", "WA"); //Wallon
        mappingTable.put("xh-xh", "XH"); //Xhosa
        mappingTable.put("xh", "XH"); //Xhosa
        mappingTable.put("yi-yi", "YI"); //Yiddish
        mappingTable.put("yi", "YI"); //Yiddish
        mappingTable.put("yo-yo", "YO"); //Yoruba
        mappingTable.put("yo", "YO"); //Yoruba

        mappingTable.put("zu-zu", "ZU"); //Zulu
        mappingTable.put("zu", "ZU"); //Zulu
    }

    /**
     *  This method translates <language>-<territory> codes to the
     *  Editor internal codes. It returns null if no mapping exists between the
     *  provided code and any of the internal Editor ones.
     *  like this
     */
    public String translateLangCode(String langCode) {
        String key = langCode.toLowerCase();
        String internalCode = mappingTable.get(key);

        if (internalCode == null) {
            //  No direct match found: see if we can find a match based on
            //  the first bit.
            StringTokenizer tokenizer = new StringTokenizer(key, "-");
            key = tokenizer.nextToken();
            internalCode = mappingTable.get(key);
        }

        if (internalCode == null) {
            //  if we still did not find a Code, just use the first bit
            //  in UPPERCASE
            StringTokenizer tokenizer = new StringTokenizer(key, "-");
            key = tokenizer.nextToken();
            internalCode = key.toUpperCase();
        }

        return internalCode;
    }

    public static synchronized LanguageMappingTable getInstance() {
        if (instance == null) {
            instance = new LanguageMappingTable();
        }

        return instance;
    }

    /** do a reverse lookup from internal (short) code to xlz long code.
     *
     * This method lookus up short encoding equivalent of long code supplied
     * by xlz file.
     * The attempt to find the best coding is done by looking up the longest code
     * mapped to the short code   <br>
     * Example:<br>
     * <pre>
     * for EN return en-GB<b>
     * for US return en-US<br>
     * </pre>
     *
     * <p>
     * All return codes are correctly capitalized (xx-XX)
     * </p>
     * @param shortEvilLangCode
     * @return equivalent encoding
     */
    public String reverseTranslateLangCode(String shortEvilLangCode) {
        Set<Entry<String, String>> s = mappingTable.entrySet();

        //Iterate thru the map entries and find the longest long code you can
        String theCode = null;

        for (Iterator<Entry<String, String>> i = s.iterator(); i.hasNext();) {
            Map.Entry<String,String> entry = i.next();

            String shortie = entry.getValue();
            String longCode = entry.getKey();

            if (shortEvilLangCode.equals(shortie) && ((theCode == null) || (longCode.length() > theCode.length()))) {
                theCode = longCode;
            }
        }

        //Found something ? Capitalize!
        if (theCode != null) {
            String[] pieces = theCode.split("-");

            if ((pieces == null) || (pieces.length != 2)) {
                return null;
            }

            return pieces[0] + "-" + pieces[1].toUpperCase();
        }

        return theCode;
    }
}
