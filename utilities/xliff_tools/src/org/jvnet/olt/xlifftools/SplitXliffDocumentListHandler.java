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
 * SplitXliffDocumentListHandler.java
 *
 * Created on 08 August 2003, 15:58
 */

package org.jvnet.olt.xlifftools;

import org.jvnet.olt.xliff.PlainXliffDocument;
import org.jvnet.olt.xliff.XliffDocument;
import org.jvnet.olt.xliff.XliffSkeletonDocument;
import org.jvnet.olt.xliff.ZippedXliffDocument;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author  jc73554
 */
public class SplitXliffDocumentListHandler
{
    private String m_suffix;
    private int m_splitCount =1;
    
    private List docList;
    private XliffDocument currentDoc;
   
    /** Creates a new instance of SplitXliffDocumentListHandler */
    public SplitXliffDocumentListHandler(String suffix)
    {
        m_suffix = suffix;

        docList = new LinkedList();
    }
    
    protected XliffDocument createOutputDocument(XliffDocument doc)
    {
        //  Extract skeleton and workflow data from the document
        String docName = generateOutputDocName(doc.getDocumentName());
        
        if(doc.hasSkeletonDocument())
        {
            try
            {
                XliffSkeletonDocument sklDoc = doc.getSkeletonDocument();
                String sklData = extractSkeletonData(sklDoc);
                
                if(doc.hasWorkflowProperties())
                {
                    try
                    {
                        Properties props = doc.getWorkflowPropertiesInterface().getWorkflowProperties();
                        return new ZippedXliffDocument("", sklData, props, docName);
                    }
                    catch(UnsupportedOperationException unsupEx1)
                    {
                        unsupEx1.printStackTrace();
                        System.err.println("The Xliff Document does not allow access to workflow data. Generating an XLIFF document without workflow data.");                        
                        return new ZippedXliffDocument("", sklData, docName);
                    }
                }
                else
                {
                    return new ZippedXliffDocument("", sklData, docName);
                }
            }
            catch(UnsupportedOperationException unsupEx)
            {
                unsupEx.printStackTrace();
                System.err.println("The Xliff Document does not allow access to skeleton file data. Generating a plain XLIFF document.");
                return new PlainXliffDocument("",docName);
            }
            catch(IOException ioEx)
            {
                ioEx.printStackTrace();
                return null;
            }
        }
        else
        {
            return new PlainXliffDocument("",docName);
        }
    }
    
    protected String generateOutputDocName(String docName)
    {
        //  Get the main name of the file
        String nameCore = null;
        String nameExtension = null;
        
        //  Split the name at the extension 
        int index = docName.lastIndexOf('.');
        if(index != -1)
        {
            nameCore = docName.substring(0, index);
            nameExtension = docName.substring(index);
        }
        else
        {
            nameCore = docName;
            nameExtension = "";
        }
        
        StringBuffer buffer = new StringBuffer(nameCore);
        buffer.append("_");
        buffer.append(m_suffix);
        buffer.append("_");
        buffer.append(Integer.toString(m_splitCount));
        buffer.append(nameExtension);
        
        return buffer.toString();
    }
    
    protected String extractSkeletonData(XliffSkeletonDocument sklDoc)
    {
        try
        {
            Reader reader = sklDoc.getSklReader();
            StringWriter writer = new StringWriter();
            int ch = 0;
            while((ch = reader.read()) != -1)
            {
                writer.write(ch);
            }
            return writer.toString();
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
            //  Fixme: this needs to be handled better.
            return "";
        }
    }
    
    public XliffDocument getNewOutputDocument(XliffDocument masterDoc)
    {
        currentDoc = createOutputDocument(masterDoc);
        docList.add(currentDoc);
        m_splitCount++;
        return currentDoc;
    }
    
    public List getXliffDocList()
    {
        return docList;
    }    
    
}
