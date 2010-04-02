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
 * XsltStylesheet.java
 *
 * Created on August 13, 2004, 3:12 PM
 */

package org.jvnet.olt.xsltrun;

import org.jvnet.olt.util.ContentSource;
import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;

/** A wrapper class for stylesheet data. This class takes a ContentSource and
 * generates templates. These templates can then be used to produce a Transformer
 * object on request.
 * @author  jc73554
 */
public class XsltStylesheet {
    
    private Templates templates;
    
    private java.util.Map xsltParameters = null;
    
    /** Creates a new instance of XsltStylesheet */
    public XsltStylesheet(ContentSource stylesheet) throws XsltStylesheetException, IOException {
        //  get a reader for the stylesheet and create a Source
        Source source = new StreamSource(stylesheet.getReader());
        
        //  create a transformer factory
        TransformerFactory factory = TransformerFactory.newInstance();
        
        //  generate the templates
        try {
            templates = factory.newTemplates(source);
        }
        catch(TransformerConfigurationException configEx) {
            //  Throw a new exception in here
            XsltStylesheetException ex = new XsltStylesheetException();
            ex.setStackTrace(configEx.getStackTrace());
            
            throw ex;
        }
    }
    
    /** An alternative constructor to allow XSLT Parameters to be specified.
     */
    public XsltStylesheet(ContentSource stylesheet, java.util.Map xsltParameters) throws XsltStylesheetException, IOException {
        this(stylesheet);
        this.xsltParameters = xsltParameters;
    }
    /** This method returns a transformer for the stylesheet. It also adds any 
     * XSLT parameters specified to the transformer.
     */
    public Transformer getTransformer() throws TransformerConfigurationException, TransformerException, IOException {
        Transformer transformer = templates.newTransformer();
        
        //  Take the list of parameters we have and apply them to the transformer
        if(xsltParameters != null) {
            java.util.Set nameSet = xsltParameters.keySet();
            java.util.Iterator iterator = nameSet.iterator();
            while(iterator.hasNext()) {
                String name = (String) iterator.next();
                String value = (String) xsltParameters.get(name);
                transformer.setParameter(name, value);
            }
        }
        
        return transformer;
    }
    
    /** This method allow parameters to be passed to the XSLT style sheet.
     */
    public void setXsltParameters(java.util.Map xsltParameters) {
        if(this.xsltParameters == null) {
            this.xsltParameters = xsltParameters;
        } else {
            this.xsltParameters.putAll(xsltParameters);
        }
    }
    
    /** This method allow a parameter to be passed to the XSLT style sheet.
     */
    public void setXsltParameter(String name, String value) {
        if((name == null) || (value == null) || name.equals("")) {
            System.err.println("A bad parameter was supplied: " +name+ " = " +value);
            return;
        }
        if(xsltParameters == null) {
            xsltParameters = new java.util.HashMap();
        }
        xsltParameters.put(name, value);
    }
    
    public void clearXsltParameters() {
        this.xsltParameters = null;
    }
    
    
}
