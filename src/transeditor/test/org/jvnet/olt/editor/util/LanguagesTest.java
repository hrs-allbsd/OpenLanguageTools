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

import java.util.Vector;
import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LanguagesTest {

    public LanguagesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // initialise Languages Class
        Locale.setDefault(Locale.ENGLISH);
        Languages l = new Languages();
    }


    /**
     * Test of getLanguageName method, of class Languages.
     */
    @Test
    public void testGetLanguageName() {
        System.out.println("getLanguageName");

        // test language name translated by JVM
        assertEquals("English", Languages.getLanguageName("en"));
        assertEquals("English (United States)", Languages.getLanguageName("en-US"));

        // special case - nso is not translated by JVM
        assertEquals("Northern Sotho", Languages.getLanguageName("nso"));

        // test if localization works
        Locale.setDefault(Locale.GERMAN);
        assertTrue( ! "English".equals(Languages.getLanguageName("en")));
    }

    /**
     * Test of getFlagPath method, of class Languages.
     */
    @Test
    public void testGetFlagPath() {
        System.out.println("getFlagPath");

        // GetFlagPath should always return something
        assertNotNull(Languages.getFlagPath("en") );

        // a FlagPath must be available as ressource
        assertNotNull( this.getClass().getResource(Languages.getFlagPath("en")) );
    }

    /**
     * Test of getFlagPathForUnknown method, of class Languages.
     */
    @Test
    public void testGetFlagPathForUnknown() {
        System.out.println("getFlagPathForUnknown");

        // GetFlagPathForUnknown must return something
        assertNotNull(Languages.getFlagPathForUnknown() );

        // result of GetFlagPathForUnknown must be available as ressource
        assertNotNull( this.getClass().getResource(Languages.getFlagPathForUnknown()) );
    }

    /**
     * Test of getLanguages method, of class Languages.
     */
    @Test
    public void testGetLanguages() {
        System.out.println("getLanguages");

        Vector<Language> allLanguages = Languages.getLanguages();

        // list of all languages must not be empty
        assertTrue( ! allLanguages.isEmpty());

        // list of all languages must contain Egnlish and Language of current default locale
        assertTrue(  allLanguages.contains( new Language (Locale.ENGLISH )) );
        assertTrue(  allLanguages.contains( new Language (Locale.getDefault())) );

        // list of all languages must not contain invalid language
        assertTrue(  ! allLanguages.contains( new Language ("invalid-language" )) );

     }

    /**
     * Test of getDefault method, of class Languages.
     */
    @Test
    public void testGetDefault() {
        System.out.println("getDefault");

        // getDeafult must return something
        assertNotNull(Languages.getDefault());

        // Default Language is the Language of the default Locale
        assertEquals(new Language (Locale.getDefault() ), Languages.getDefault());
    }


    /**
     * Test of areSimilar method, of class Languages.
     */
    @Test
    public void testAreSimilar_Language_Language() {
        System.out.println("areSimilar");
        Language ln = new Language("en");

        // Language is similar to itself
        assertTrue( Languages.areSimilar(ln, ln));

        assertTrue( Languages.areSimilar(ln, new Language ("en-US")));
        assertTrue( Languages.areSimilar(new Language ("en-US"), ln ));

        assertTrue( ! Languages.areSimilar(ln, new Language ("de-DE")));
    }

    /**
     * Test of areSimilar method, of class Languages.
     */
    @Test
    public void testAreSimilar_String_String() {
        System.out.println("areSimilar");

        // Language is similar to itself
        assertTrue( Languages.areSimilar("en", "en"));

        assertTrue( Languages.areSimilar("en", "en-US"));
        assertTrue( Languages.areSimilar("en-US", "en" ));
        assertTrue( Languages.areSimilar("de-DE", "de_CH" ));

        assertTrue( ! Languages.areSimilar("en", "de-DE"));
    }

}