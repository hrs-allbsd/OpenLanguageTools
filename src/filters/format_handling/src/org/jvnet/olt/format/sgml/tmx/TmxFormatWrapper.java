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
 * STmxFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.sgml.tmx;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.SgmlDocFragmentParser.SgmlDocFragmentParser;

/**
 *
 * @author  jc73554
 */
public class TmxFormatWrapper implements FormatWrapper
{
    private GlobalVariableManager m_gvm;
    
    /** Creates a new instance of SgmlFormatWrapper */
    public TmxFormatWrapper(GlobalVariableManager gvm)
    {
        m_gvm = gvm;
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException
    {
        try
        {
            StringReader reader = new StringReader(text);
            SgmlDocFragmentParser parser = new SgmlDocFragmentParser(reader);
            parser.parse();
            
            TmxFormatWrappingVisitor visitor = new TmxFormatWrappingVisitor(m_gvm);
            
            parser.walkParseTree(visitor, null);
            return visitor.getWrappedString();
        }
        catch(Exception exParse)
        {
            throw new InvalidFormattingException(exParse.getMessage());
        }
    }
}
