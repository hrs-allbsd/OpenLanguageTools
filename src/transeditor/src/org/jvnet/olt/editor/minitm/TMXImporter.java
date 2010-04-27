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
 * TMXImporter.java
 *
 * Created on September 12, 2005, 12:32 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jvnet.olt.editor.minitm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
//import javax.xml.transform.sax.SAXTransformerFactory;
import net.sf.saxon.TransformerFactoryImpl;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jvnet.olt.editor.util.MultiWriter;
import org.jvnet.olt.minitm.MiniTMException;
import org.jvnet.olt.xliff.XLIFFEntityResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 *
 * @author boris
 */
public class TMXImporter {
    private static final Logger logger = Logger.getLogger(TMXImporter.class.getName());
    
    private String translatorId;
    private String srcLang;
    private String tgtLang;
    private String srcLangShort;
    private String tgtLangShort;
    
    /** Creates a new instance of TMXImporter */
    public TMXImporter() {
    }
    
    public void convertTMX2MTM(File tmxFile,File mtmTargetFile) throws IOException,SAXException,MiniTMException{
	doConvert(tmxFile,mtmTargetFile);
    }
    
    void doConvert(File tmxFile,File mtmTargetFile) throws IOException,SAXException {
	Reader fr = null;
	Reader templeate = null;
	Writer out = null;
	
	try{
            fr = new BufferedReader(new InputStreamReader(new FileInputStream(tmxFile),Charset.forName("UTF-8")));
            
	    XMLReader reader = XMLReaderFactory.createXMLReader();

	    reader.setEntityResolver(new EntityResolver() {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException,IOException {
		    System.out.println("public:"+publicId+" system:"+systemId);
		
		    return XLIFFEntityResolver.instance().resolveEntity(publicId,systemId);
		}
	    });

	    Source source = new SAXSource(reader,new InputSource(fr));
	    
	    templeate = template();
	    if(templeate == null)
		throw new NullPointerException("unable to find xslt template");
	    
	    //TransformerFactory tf = SAXTransformerFactory.newInstance();
            TransformerFactory tf = TransformerFactoryImpl.newInstance();
	    Templates tmplts = tf.newTemplates(new StreamSource(templeate));
	    
	    Transformer tr = tmplts.newTransformer();
	    tr.setParameter("srcLang", srcLang);
	    tr.setParameter("tgtLang", tgtLang);
        // ToDo: remove deprecated language parameters
	    tr.setParameter("srcLangShort", srcLangShort);
	    tr.setParameter("tgtLangShort", tgtLangShort);
        //
	    tr.setParameter("userId", translatorId);
	    
	    //Source source = constructSource(fis);
	    
	    out = new OutputStreamWriter(new FileOutputStream(mtmTargetFile),"UTF-8");
	    
	    Writer[] writers = new Writer[]{ out,new PrintWriter(System.out)};
	    
	    tr.transform(source,new StreamResult(new MultiWriter(writers)));
	    
	    out.close();
	    fr.close();
	    templeate.close();
	} catch (TransformerException te){
	    throw new SAXException(te);
	} finally {
	    if(fr != null){
		try{
		    fr.close();
		} catch (IOException ioe){
		    ;
		}
	    }
	    if(templeate != null){
		try{
		    templeate.close();
		} catch (IOException ioe){
		    ;
		}
	    }
	    if(out != null){
		try{
		    out.close();
		} catch (IOException ioe){
		    ;
		}
	    }
	}
    }
    
    public void setTranslatorId(String translatorId) {
	this.translatorId = translatorId;
    }
    
    public void setSrcLang(String srcLang) {
	this.srcLang = srcLang;
    }
    
    public void setTgtLang(String tgtLang) {
	this.tgtLang = tgtLang;
    }
    
    Reader template() throws IOException{
        InputStream is = getClass().getClassLoader().getResourceAsStream("xsl/tmx2mtm.xsl");
	return new InputStreamReader(is,"UTF-8");
    }

    /**
     * @deprecated only used for the old internal language codes
     * @param srcLangShort
     */
    public void setSrcLangShort(String srcLangShort) {
	this.srcLangShort = srcLangShort;
    }

    /**
     * @deprecated only used for the old internal language codes
     * @param tgtLangShort
     */
    public void setTgtLangShort(String tgtLangShort) {
	this.tgtLangShort = tgtLangShort;
    }
    
    Source constructSource(InputStream is) throws ParserConfigurationException,SAXException{
	//return new SAXSource(new InputSource(is));
	
	SAXParserFactory factory = SAXParserFactory.newInstance();
	factory.setValidating(false);
	
	SAXParser parser = factory.newSAXParser();
	XMLReader reader = parser.getXMLReader();
	
	reader.setEntityResolver(new EntityResolver() {
	    public InputSource resolveEntity(String publicId, String systemId) throws SAXException,IOException {
		System.out.println("public:"+publicId+" system:"+systemId);
		
		return null;
	    }
	});
	
	return new SAXSource(reader,new InputSource(is));
	
	
    }
}
