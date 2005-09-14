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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.jvnet.olt.minitm.MiniTMException;
import org.jvnet.olt.xliff.XLIFFEntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

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
        InputStream fis = null;
        InputStream templeate = null;
        OutputStream out = null;
        
        try{
            
            fis = new FileInputStream(tmxFile);
            InputSource is = new InputSource(fis);
            
            templeate = template();
            if(templeate == null)
                throw new NullPointerException("unable to find xslt template");
            
            InputSource tis = new InputSource(templeate);
            
            
            out = new FileOutputStream(mtmTargetFile);
            
            TransformerFactory tf = TransformerFactory.newInstance();            
            Transformer tr = tf.newTransformer(new SAXSource(tis));
        
            tr.setParameter("srcLang", srcLang);
            tr.setParameter("tgtLang", tgtLang);
            tr.setParameter("srcLangShort", srcLangShort);
            tr.setParameter("tgtLangShort", tgtLangShort);
            tr.setParameter("userId", translatorId);
            
            Source source = constructSource(fis);
            
            tr.transform(source,new StreamResult(out));
            
            out.close();
            fis.close();
        }
        catch (TransformerException te){
            throw new SAXException(te);
        }
        catch (ParserConfigurationException pce){
            throw new SAXException(pce);
        }
        finally {
            if(fis != null){
                try{
                    fis.close();
                }
                catch (IOException ioe){
                    ;
                }
            }
            if(templeate != null){
                try{
                    templeate.close();
                }
                catch (IOException ioe){
                    ;
                }
            }            
            if(out != null){
                try{
                    out.close();
                }
                catch (IOException ioe){
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
    
    InputStream template(){
        return getClass().getClassLoader().getResourceAsStream("resources/tmx2mtm.xsl");
    }

    public void setSrcLangShort(String srcLangShort) {
        this.srcLangShort = srcLangShort;
    }

    public void setTgtLangShort(String tgtLangShort) {
        this.tgtLangShort = tgtLangShort;
    }
    
    Source constructSource(InputStream is) throws ParserConfigurationException,SAXException{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setEntityResolver(XLIFFEntityResolver.instance());
        
        return new SAXSource(parser.getXMLReader(), new InputSource(is));
    }
    
}
