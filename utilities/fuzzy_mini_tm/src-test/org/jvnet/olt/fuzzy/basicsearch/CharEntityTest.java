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


/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.fuzzy.basicsearch;

import junit.framework.*;
import java.util.Hashtable;
import java.io.*;
import org.xml.sax.*;
import org.jvnet.olt.minitm.TMUnit;
import org.jvnet.olt.index.IdSequence;
import org.jvnet.olt.index.MiniTmContentHandler;
import javax.xml.parsers.*;

public class CharEntityTest
extends TestCase
{
    private Hashtable hash;
    
    public CharEntityTest(String name)
    {
        super(name);
    }
    
    public void setUp()
    {
        
        hash = new Hashtable();
        
        try
        {
            IdSequence sequence = new IdSequence();
            
            String string =   "<?xml version=\"1.0\" ?>\n" +
            "<minitm name=\"sample\">\n" +
            "  <entry>\n" +
            "    <source>The first source segment.</source>\n" +
            "    <translation>A translation &amp; more!</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "  <entry>\n" +
            "    <source>foo &lt; bar</source>\n" +
            "    <translation>bar &gt; foo</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "  <entry>\n" +
            "    <source>Once upon a time, in a galaxy, far, far, away...</source>\n" +
            "    <translation>The starwars quote translation would go here.</translation>\n" +
            "    <translatorId>johnc</translatorId>\n" +
            "  </entry>\n" +
            "</minitm>\n";
            
            StringReader reader = new StringReader(string);
            InputSource is = new InputSource(reader);
            
            MiniTmContentHandler handler = new MiniTmContentHandler(hash, sequence);
            
            //SAXParserFactory factory = org.apache.xerces.jaxp.SAXParserFactoryImpl.newInstance();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            
            factory.setValidating(true);  
            factory.setNamespaceAware(true);
            
            factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
            XMLReader parser = factory.newSAXParser().getXMLReader();
            
            parser.setContentHandler(handler);
            parser.setErrorHandler(handler);
            
            parser.parse(is);
        }
        catch(Exception ex)
        { fail(ex.getMessage()); }
    }
    
    public void testEntityConversion()
    {
        TMUnit unit = (TMUnit) hash.get( new Long(1L));
        assertNotNull(unit);
        assertEquals("The first source segment.", unit.getSource());
        assertEquals("A translation & more!", unit.getTranslation());
        assertEquals("johnc", unit.getTranslatorID());
        
        unit = (TMUnit) hash.get( new Long(2L));
        assertNotNull(unit);
        assertEquals("foo < bar", unit.getSource());
        assertEquals("bar > foo", unit.getTranslation());
        assertEquals("johnc", unit.getTranslatorID());
    }
}
