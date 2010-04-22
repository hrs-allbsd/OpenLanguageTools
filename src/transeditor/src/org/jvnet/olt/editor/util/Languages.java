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
 * Portions Copyright 2010 by André Schnabel
 *
 */
package org.jvnet.olt.editor.util;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Locale;

/**
* Helper class for very basic language handling
*/
public class Languages {

    private final static String imagePath = ImagePath.PATH + "flags/";
    
    private static Set<Language> allLanguages;
    private static Hashtable<String,Language> allLanguagesTable;

    static {
        // set up list of all available languages
        allLanguages = Collections.synchronizedSortedSet(new TreeSet<Language>());
        allLanguagesTable = new Hashtable<String,Language>();

        // first add languages we need, but which are not likely to be JVM locales
        String[] custLanguages = {
            "ang", "az-IR",
            "nso",
            "sr-CS"
        };
        for ( String ln: custLanguages ) {
            Language lng = new Language( ln );
            allLanguages.add(lng);
            allLanguagesTable.put(lng.getCode(), lng);
        }

        // now add all languages known by the JVM
        String[] allLangs = Locale.getISOLanguages();
        for (int i = 0; i < allLangs.length; i++) {
            Language lng = new Language(allLangs[i]);
            if ( ! allLanguagesTable.containsKey(lng.getCode()) ) {
                allLanguages.add(lng);
                allLanguagesTable.put(lng.getCode(), lng);
            }
        }

        // now add languages for all Locales known by the JVM
        // this adds the Region specific modifications
        Locale[] allLocales = Locale.getAvailableLocales();
        for (int i = 0; i < allLocales.length; i++) {
            Language lng = new Language(allLocales[i]);
            if ( ! allLanguagesTable.containsKey(lng.getCode()) ) {
                allLanguages.add(lng);
                allLanguagesTable.put(lng.getCode(), lng);
            }
        }

    }

    /**
     * Gets the name of a language for a given code
     * @param code xml:lang complient language code
     * @return  the (localized) display name of the language
     */
    public static String getLanguageName(String code) {
        Language lng = allLanguagesTable.get(code);
        if ( lng == null) {
            //late addition of a requested language if it was not already defined
            lng = new Language(code);
                allLanguages.add(lng);
                allLanguagesTable.put(lng.getCode(), lng);
        }
        return lng.getName();
    }


    /**
     * returns the full path to a flag image for the language code
     * if there is no dedicated flag image, "No-Flag" will be returned
     * @param code xml:lang complient language code
     * @return  full path to a flag image, which can be loaded as resource
     */
    public static String getFlagPath(String code) {
        String strPath = code.toLowerCase();
        Languages l = new Languages();

        // try to load the flag for the exact code
        Object o = l.getClass().getResource(imagePath + strPath + ".gif");

        if (o == null) {
            // ugly workaround -
            Language lng = new Language(code);
            strPath = lng.getLocale().toString().toLowerCase();
            o = l.getClass().getResource(imagePath + strPath + ".gif");
        }

        if (o == null) {
            // could not load the exact flag - try with language code only
            Language lng = new Language(code);
            strPath = lng.getLocale().toString().toLowerCase();
            o = l.getClass().getResource(imagePath + strPath + ".gif");
        }
        if (o == null) {
            return imagePath + "No-Flag.gif";
        } else {
            return imagePath + strPath + ".gif";
        }
    }

    
    public static String getFlagPathForUnknown() {
        return imagePath + "Unknown.gif";
    }

    
    /**
     * Returns a Vector for all available Languages
     * which Languages are available depends on the Java Runtime's avaliable locales
     * @return Vector for all available Languages
     */
    public static Vector<Language> getLanguages(){
        Vector<Language> s = new Vector<Language>(allLanguages);
        return s;
    }
    
    /**
     * Returns the default language (based on the default locale)
     * @return current default language
     */
    public static Language getDefault(){
        return new Language(Locale.getDefault());
    }

    /**
     * Returns a Language identified by it's code
     * @param code Language code
     * @return Language for the given code
     * @deprecated use new Language(code) constructor instead
     */
    public static Language findByCode(String code){
        return allLanguagesTable.get(code);
    }

    /**
     * Tests, if two given language objects are similar. 
     * @param l1 first language to compare
     * @param l2 second language to compare
     * @return true if the language code is the same for both objects<br>
     *          false in any other case
     */
    public static boolean areSimilar(Language l1, Language l2){
        boolean similar = false;
        try {
            similar = l1.isSimilar(l2);
        } catch (Exception e) {
        }
        return similar;
    }

    /**
     * Tests, if two given language objects are similar.
     * @param l1 first language to compare
     * @param l2 second language to compare
     * @return true if the language code is the same for both objects<br>
     *          false in any other case
     */
    public static boolean areSimilar(String l1, String l2){
        Language ln1 = new Language(l1);
        Language ln2 = new Language(l2);
        return areSimilar(ln1, ln2);
    }
}
