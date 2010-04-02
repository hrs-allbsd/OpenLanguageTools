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
 * XliffDocument.java
 *
 * Created on 21 March 2003, 15:42
 */

package org.jvnet.olt.xliff;

import java.io.IOException;

/**
 *
 * @author  jc73554
 */
public interface XliffDocument
{
    public String getDocumentText();
    
    public void setDocumentText(String text);
    
    public String getDocumentName();
    
    public String getMimeType();
    
    public byte[] getBytes() throws IOException;
    
    
    //  Accessing internal XLIFF buffer
    public java.io.Writer getXliffWriter() throws IOException;
    
    public java.io.Reader getXliffReader() throws IOException;
    
    //  Serializing to streams
    public void writeTo(java.io.OutputStream ostream) throws IOException;

    public void readFrom(java.io.InputStream istream) throws IOException;
    
    //  Serializing to files
    public void writeTo(java.io.File outFile) throws IOException;

    public void readFrom(java.io.File inFile) throws IOException;

    //  Interrogation of the interface
    public boolean hasWorkflowProperties();

    public boolean hasSkeletonDocument();
    
    //  Interface narrowing operations for some XLIFF documents that contain
    //  ancilliary data, such as skeleton dat, or information about the workflow.
    // 
    //  Maintenance note: This is a bit of a hack. It allows us to get at ancilliary 
    //  data when given a XliffDocument interface, without having to get rid of 
    //  the XliffSkeletonDocument interface (hwich would have resulted in a lot 
    //  of code change in calling classes.
    public WorkflowProperties getWorkflowPropertiesInterface() throws UnsupportedOperationException;
    
    public XliffSkeletonDocument getSkeletonDocument() throws UnsupportedOperationException;

    public void setSkeletonData(org.jvnet.olt.xliff.XliffSkeletonDocument sourceSkeletonDoc) throws UnsupportedOperationException, IOException;
    
    
}
