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

import java.util.Locale;

        
/**
 * Class for very basic language handling
 * A Language within OLTE represents an xml language and is identified by
 * <ul>
 *  <li>an ISO language code (ISO 639-1 or 639-2 if no 639-1 code is available)
 *  <li>an optional ISO Country code ((ISO 3166)
 * </ul>
 * Internally it is handled as a Locale. See Javadoc see java.util.Locale for
 * restrictions of the ISO 639 language code handling<br>
 * In contrast to the Locale, the String representation of a
 * Language will be it's DisplayName.
 * The CompareTo method is also based on the Locale's DisplayName.
 * @see java.util.Locale#Locale(java.lang.String) java.util.Locale for
 *          restrictions of the ISO 639 language code handling
 */
public class Language implements Comparable<Language>{
    private static Bundle bundle = Bundle.getBundle(Language.class.getName());
    private Locale _locale;
    private String _DisplayName;

    // used for "dynamic" localization of LanguageNames
    private Locale _currDefaultLocale;

    /**
     * Creates a new Language based on a given locale
     * @param locale Locale to create Language from
     */
    public Language( Locale locale){
        _locale = locale;
    }

    /**
     * Creates a new Language identified by an language identifier
     * @param langCode xml:lang or Loacle-style identifier for the language code
     */
    public Language( String langCode){
        String lang = langCode.replace('_', '-');
        String country = null;

        try {
            String[] codes = lang.split("-");
            lang = codes[0].toLowerCase();
            country = codes[1].toUpperCase();
        } catch (Exception e) {
            // ignore
        } 
        if ( lang != null ){
            if ( country != null ) {
                _locale = new Locale (lang, country);
            } else {
                 _locale = new Locale (lang);
            }
        } else {
            _locale = null;
        }
    }

    /**
     * Gets the old Editor's internal short code for the language
     * @return a two-digit short code that is internally used by OLTE
     * @deprecated use full xml:lang compliant codes instead
     */
    public String getShortCode() {
        String shortCode;
        if ( _locale == null ) {
            shortCode = null;
        } else {
            shortCode = LanguageMappingTable.getInstance().
                        translateLangCode(_locale.toString().replace('_', '-'));
        }
        return shortCode;
    }

    /**
     * Gets the language code
     * @return an xml:lang compliant language code
     */
    public String getCode() {
        return (_locale == null) ? null :  _locale.toString().replace('_', '-');
    }

    /**
     * Gets the language name
     * @return (localized) displayname for the language
     */
    public String getName() {

        if ( _DisplayName == null || _DisplayName.length() == 0 ||
            ! Locale.getDefault().equals(_currDefaultLocale)) {
            // no DisplayName retrieved yet - get the correct name
            _DisplayName = _locale.getDisplayName();

            // if the Displayname equals the locale's code, JVM cannot provide
            // a translation - provide our own
            if ( _DisplayName.equals(_locale.toString()) ) {
                _DisplayName = bundle.getString( "langName_".concat(_DisplayName));
            }
        }
        return _DisplayName;
    }

    /**
     * Gets the Locale accociated with the language
     * @return the locale accociated with the language (null for empty language)
     */
    public Locale getLocale() {
        return _locale ;
    }

    /**
     * Tests, if a given languge is "similar" to the current language<br>
     * Two languages are similar, if their language code (without region) is
     *  the same
     * @param lng Language to check for similarity
     * @return true if the language code of the given language object (without
     *      region code) matches the current language code (without region code)
     */
    public boolean isSimilar(Language lng) {
        if (lng == null) {
            return false;
        } else {
            return lng.getLocale().getLanguage().equals(_locale.getLanguage());
        }
        
    }
    public int compareTo(Language o) {
        String s1 = this.getName().concat( new Character((char)1).toString())
                                    .concat("{").concat(this.getCode()).concat("}");
        String s2 = o.getName().concat( new Character((char)1).toString()).
                                     concat("{").concat(o.getCode()).concat("}");
        return s1.compareTo(s2);
    }

    @Override
    public boolean equals( Object anObject ) {
        if ( anObject == null || anObject.getClass() != this.getClass()) {
            return false;
        } else {
            Language ln = (Language) anObject;
            return this._locale.equals( ln.getLocale());
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this._locale != null ? this._locale.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return (_locale == null) ? null :  getName();
    }

}
    
