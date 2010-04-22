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
 * Copyright  2010 by André Schnabel
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.editor.util;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class LanguageTest {

    public LanguageTest() {
    }

    /**
     * Test of getCode method, of class Language.
     * getCode must always return vaild xml:language code
     */
    @Test
    public void testGetCode() {
        Language ln;
        System.out.println("getCode");

        // test get-code for languages created from Strings
        ln = new Language ("en");
        assertEquals("en", ln.getCode());

        ln = new Language ("de-DE");
        assertEquals("de-DE", ln.getCode());

        ln = new Language ("de_DE");
        assertEquals("de-DE", ln.getCode());

        //special case - lower/upper case does not matter on language creation
        // but we expect the correct code to be returned
        ln = new Language ("DE-de");
        assertEquals("de-DE", ln.getCode());

        //special case - "ji" is deprecated and has been replaced by yi in ISO 639
        // but Java delivers the old codes
        ln = new Language ("yi");
        assertEquals("ji", ln.getCode() );

        // test get-code for languages created from Locales
        ln = new Language ( new Locale ("en"));
        assertEquals("en", ln.getCode());

        // test get-code for languages created from Locales
        ln = new Language ( new Locale ("en","us"));
        assertEquals("en-US", ln.getCode());

        
    }

    /**
     * Test of getName method, of class Language.
     * returns a localized name of the language
     * for testability we explizitliy set US locale
     */
    @Test
    public void testGetName() {
        Language ln;
        Locale.setDefault(Locale.ENGLISH);
        System.out.println("getName");

        // test language name translated by JVM
        ln = new Language ( "en");
        assertEquals("English", ln.getName());

        ln = new Language ( "en-US");
        assertEquals("English (United States)", ln.getName());

        // special case - nso is not translated by JVM
        ln = new Language ( "nso");
        assertEquals("Northern Sotho", ln.getName());

        // test if localization works
        ln = new Language ("en");
        assertTrue( "English".equals(ln.getName()) );
        Locale.setDefault(Locale.GERMAN);
        assertTrue( ! "English".equals(ln.getName()) );

    }

    /**
     * Test of getLocale method, of class Language.
     * if a Langugae is created from a xml:lang string, it needs to
     * deliver the according locale
     */
    @Test
    public void testGetLocale() {
        Language ln;
        System.out.println("getLocale");

        ln = new Language ("en");
        assertEquals(new Locale ("en"), ln.getLocale() );

        ln = new Language ("en-US");
        assertEquals(new Locale ("en","US"), ln.getLocale() );

        ln = new Language ("ans");
        assertEquals(new Locale ("ans"), ln.getLocale());

        ln = new Language ("en-US");
        assertNotSame(new Locale ("en"), ln.getLocale());

    }

    /**
     * Test of isSimilar method, of class Language.
     * two languages are similar, if the language part is the same
     */
    @Test
    public void testIsSimilar() {
        System.out.println("isSimilar");
        Language ln1 ;
        Language ln2 ;

        ln1 = new Language ("en");
        ln2 = new Language ("en");
        assertEquals(true, ln1.isSimilar(ln2) );

        ln1 = new Language ("en");
        ln2 = new Language ("en-US");
        assertEquals(true, ln1.isSimilar(ln2) );
        assertEquals(true, ln2.isSimilar(ln1) );
        assertEquals(true, ln2.isSimilar(ln2) );

        ln1 = new Language ("en-GB");
        ln2 = new Language ("en-US");
        assertEquals(true, ln1.isSimilar(ln2) );

        ln1 = new Language ("de-CH");
        ln2 = new Language ("fr-CH");
        assertEquals(false, ln1.isSimilar(ln2) );


    }

    /**
     * Test of compareTo method, of class Language.
     * Compare is based on the (localized) DisplayName
     */
    @Test
    public void testCompareTo() {
        Locale.setDefault(Locale.ENGLISH);
        Language ln1 ;
        Language ln2 ;
        System.out.println("compareTo");

        // compare to same language must be 0
        ln1 = new Language ("en");
        assertEquals(0, ln1.compareTo(ln1) );

        // language with region info is always bigger then without
        ln1 = new Language ("en");
        ln2 = new Language ("en-US");
        assertTrue( ln1.compareTo(ln2) < 0 );

        // "German" is bigger than "English"
        ln1 = new Language ("de");
        ln2 = new Language ("en");

        assertTrue( ln1.compareTo(ln2) > 0 );

        // make sure that we get inverse results
        assertEquals( ln1.compareTo(ln2) , - ln2.compareTo(ln1) );

        
    }

    /**
     * Test of equals method, of class Language.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Language ln1 ;
        Language ln2 ;

        Object anObject = null;

        ln1 = new Language ("en");
        ln2 = new Language ("en-US");

        assertTrue( ! ln1.equals(anObject) );
        assertTrue( ! ln1.equals(ln2) );
        assertTrue( ln1.equals(ln1) );


    }

    /**
     * Test of toString method, of class Language.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Language ln;
        Locale.setDefault(Locale.ENGLISH);

        // test language name translated by JVM
        ln = new Language ( "en");
        assertEquals("English", ln.toString());

        ln = new Language ( "en-US");
        assertEquals("English (United States)", ln.toString());

        // special case - nso is not translated by JVM
        ln = new Language ( "nso");
        assertEquals("Northern Sotho", ln.toString());

        // test if localization works
         ln = new Language ("en");
        assertTrue( "English".equals(ln.toString()) );
        Locale.setDefault(Locale.GERMAN);
        assertTrue( ! "English".equals(ln.toString()) );

        // toString must return the same as .getName
        assertEquals(ln.getName(), ln.toString());

    }

}