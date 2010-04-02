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

/*
 * ParseTest.java
 * JUnit based test
 *
 * Created on 18 September 2002, 13:32
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.io.IOException;
import java.io.StringReader;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.textui.TestRunner;

/**
 *
 * @author jc73554
 */
public class ParseTest extends TestCase
{
    
    public ParseTest(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(ParseTest.class);
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testEmpty()
    {
        String sgml = "";
        try
        {
            StringReader reader = new StringReader(sgml);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
        }
        catch(ParseException exParse)
        {
            fail("Parsing Error: " + exParse.getMessage());
        }
    }
    
    
    
}
