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
 * SgmlFormatWrapper.java
 *
 * Created on 29 July 2002, 18:26
 */

package org.jvnet.olt.format.xml;

import org.jvnet.olt.format.FormatWrapper;
import java.io.StringReader;
import org.jvnet.olt.format.InvalidFormattingException;
import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.parsers.XmlDocFragmentParser.XmlDocFragmentParser;
import org.jvnet.olt.parsers.tagged.TagTable;
import org.jvnet.olt.parsers.tagged.SegmenterTable;
import org.jvnet.olt.parsers.tagged.DefaultTagTable;
import org.jvnet.olt.parsers.tagged.DefaultSegmenterTable;

/**
 *
 * @author  jc73554
 */
public class XmlFormatWrapper implements FormatWrapper
{
    private GlobalVariableManager m_gvm;
    private TagTable tagTable=null;
    private SegmenterTable segmenterTable=null;
    
    /** Creates a new instance of SgmlFormatWrapper */
    public XmlFormatWrapper(GlobalVariableManager gvm)
    {
        m_gvm = gvm;
        tagTable = new DefaultTagTable();
        segmenterTable = new DefaultSegmenterTable();
    }
    
    /** Creates a new instance of SgmlFormatWrapper */
    public XmlFormatWrapper(GlobalVariableManager gvm, TagTable tagTable, SegmenterTable segmenterTable){
        m_gvm = gvm;
        this.tagTable = tagTable;
        this.segmenterTable = segmenterTable;
    }
    
    public String wrapFormatting(String text) throws InvalidFormattingException
    {
        try
        {
            StringReader reader = new StringReader(text);
            XmlDocFragmentParser parser = new XmlDocFragmentParser(reader);
            parser.parse();
            XmlFormatWrappingVisitor visitor;
            // if these guys are null, we'll just use traditional sgml format wrapping
            if (tagTable == null || segmenterTable == null){
                visitor = new XmlFormatWrappingVisitor(m_gvm);
            }
            else {
                visitor = new XmlFormatWrappingVisitor(m_gvm, tagTable, segmenterTable);
            }
                
            
            parser.walkParseTree(visitor, null);
            return visitor.getWrappedString();
        }
        catch(Exception exParse)
        {
            InvalidFormattingException newEx =  new InvalidFormattingException(exParse.getMessage());
            newEx.setStackTrace(exParse.getStackTrace());            
            throw newEx;
        }
    }
    
    
}
