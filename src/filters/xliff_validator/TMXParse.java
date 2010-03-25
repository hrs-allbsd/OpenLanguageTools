import java.io.*;
import java.net.URL;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.util.*;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
// Code written in a hurry to validate TMX - copy of XLIFFParse. 
public class TMXParse extends DefaultHandler implements EntityResolver{
    
    private int state = 0;
    private Stack stateStack;
    private Map map;
    private boolean containsEnGb = false;
    private String XLIFFDtd="tmx13.dtd";
    
    private String unformatString="";
    private String formatString="";
    
    public static final int FILE = 0;
    public static final int TRANSUNIT = 1;
    public static final int SOURCE = 2;
    public static final int TUV = 3;
    public static final int TU = 4;
    public static final int EPT = 5;
    public static final int BPT = 6;
    public static final int IT = 7;
    
    public static final int BLANK=8;
    
    
    public String[] tagNameArr = {"file","trans-unit","source","tuv","tu","ept","bpt","it","blank" };
    
    public TMXParse(File input) throws org.xml.sax.SAXException, java.io.IOException {
        
        map = new HashMap();
        stateStack = new Stack();
        // I'm only looking at very few tags at the moment
        map.put("file", new Integer(FILE));
        map.put("trans-unit", new Integer(TRANSUNIT));
        map.put("seg", new Integer(SOURCE));
        map.put("tu", new Integer(TU));
        map.put("tuv", new Integer(TUV));
        map.put("bpt", new Integer(BPT));
        map.put("ept", new Integer(EPT));
        map.put("it", new Integer(IT));
        
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input));
        InputStreamReader isr = new InputStreamReader(bis,"UTF-8");
        InputSource is = new InputSource(isr);
        
        // Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = this;
        try {
            // Use the default (non-validating) parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( is, handler );
        } catch (java.io.UnsupportedEncodingException io){
            io.printStackTrace();
        } catch (javax.xml.parsers.ParserConfigurationException p){
            p.printStackTrace();
        }
        
        
        
    }
    
    
    public static void main(String argv[]) {
        try {
            if (argv.length != 1){
                System.out.println("Usage: <file.xlf>");
                System.out.println("Input is assumed to be UTF-8");
                System.exit(1);
            }
            File input = new File(argv[0]);
            TMXParse tmxp = new TMXParse(input);
            if (tmxp.containsEnGb){
                System.out.println("Warning - this TMX file contains an en-GB language id!");
                System.exit(1);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("An exception was thrown during parsing : "+e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
    
    
    // doing special stuff for things within bpt and ept segments
    // --
    public void characters(char buf[], int offset, int len) throws SAXException {
        
        String s = new String(buf, offset, len);
        switch (state){
            // all these elements contain pcdata, but can also hold formatting tags
            case IT:
            case BPT:
            case EPT:
                formatString=formatString+s;
                break;
            case SOURCE:
                // add the chars to the formatted and unformatted string buffer
                formatString=formatString+s;
                unformatString=unformatString+s;
                break;
            default:
                // do nothing
                break;
        }
    }
    
    
    public void startElement(String namespaceURI,
            String sName, // simple name (localName)
            String qName, // qualified name
            Attributes attrs)
            throws SAXException {
        
        // save the current state
        stateStack.push(new Integer(state));
        
        Integer i = (Integer)map.get(qName.toLowerCase());
        if (i == null) {
            state = BLANK;
        } else {
            state = i.intValue();
        }
        
        switch (state) {
            case TU:
                System.out.println("id=" + attrs.getValue("tuid"));
                break;
            case TUV:
                String lang = attrs.getValue("xml:lang");
                // Mirek wants to see if the file contains the en-GB language code
                // so we set a flag if we see this.
                if (lang.equals("en-GB")){
                    this.containsEnGb = true;
                }
                break;
            default:
                ;
        }
        
        
    }
    
    
    public void endElement(String namespaceURI,
            String sName, // simple name (localName)
            String qName // qualified name
            ) throws SAXException {
        
        Integer i = (Integer)map.get(qName.toLowerCase());
        if (i == null) {
            state = BLANK;
        } else {
            state = i.intValue();
        }
        
        switch (state) {
            case SOURCE:
                // do something with the formatted and unformatted string
                System.out.println(formatString);
                unformatString="";
                formatString="";
                break;
            default:
                ;
        }
        // Now, restore the state.
        state = ((Integer)stateStack.pop()).intValue();
    }
    
    
    
    private void printState(){
        System.out.println("&&"+tagNameArr[state]+"&&");
    }
    
    
    
    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }
    
    
    // Pick up the right dtd (ignoring the one specified in the header)
    public InputSource resolveEntity(String publicId, String systemId) {
        System.out.println("systemId = " + systemId);
        
        File file = new File(systemId);
        
        System.out.println("File.getName() is " + file.getName());
        if (file.getName().equals("xliff.dtd")){
            System.out.println("Using " + XLIFFDtd);
            return new InputSource(XLIFFDtd);
        } else return null;
    }
    
}
