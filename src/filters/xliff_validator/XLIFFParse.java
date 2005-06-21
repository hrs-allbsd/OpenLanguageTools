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

public class XLIFFParse extends DefaultHandler implements EntityResolver{
    
    private int state = 0;
    private Stack stateStack;
    private Map map;

    private String XLIFFDtd="xliff.dtd";
   
    private String unformatString="";
    private String formatString="";

    public static final int FILE = 0;
    public static final int TRANSUNIT = 1;
    public static final int SOURCE = 2;
    public static final int BPT = 3;
    public static final int EPT = 4;
    public static final int SUB = 5;
    public static final int PH = 6;
    public static final int UT = 7;
    public static final int HI = 8;
    public static final int IT = 9;
    public static final int EXTERNALFILE = 10;  
    public static final int BLANK=11;

   
    public String[] tagNameArr = {"file","trans-unit","source","bpt","ept","ph","ut","hi","it","external-file","blank" };

 
    public XLIFFParse(File input)	throws org.xml.sax.SAXException, java.io.IOException {

	map = new HashMap();
	stateStack = new Stack();
	// I'm only looking at very few tags at the moment
	map.put ("file", new Integer (FILE));
	map.put ("trans-unit", new Integer (TRANSUNIT));
	map.put ("seg", new Integer(SOURCE));
	map.put ("tu", new Integer(BPT));
	map.put ("ept", new Integer(EPT));
	map.put ("ph", new Integer(PH));
	map.put ("ut", new Integer(UT));
	map.put ("hi", new Integer(HI));
	map.put ("it", new Integer(IT));
	map.put ("external-file", new Integer(EXTERNALFILE));		 
	

	FileInputStream fis = new FileInputStream (input);
	InputStreamReader isr = new InputStreamReader (fis,"UTF-8");
	InputSource is = new InputSource (isr);

	// Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = this;
	try {
            idSet = new HashSet();
	    // Use the default (non-validating) parser
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	
	    factory.setValidating(true);
	    factory.setNamespaceAware(true);
        
        factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true); 
	    SAXParser saxParser = factory.newSAXParser();
	    saxParser.parse( is, handler );
	}
	catch (java.io.UnsupportedEncodingException io){
	    io.printStackTrace();
	}
	catch (javax.xml.parsers.ParserConfigurationException p){
	    p.printStackTrace();
	}
	
   

    }
    private Set idSet;

    public static void main(String argv[]) {

	try {

	    if (argv.length != 1){
		System.out.println ("Usage: <file.xlf>");
		System.exit(1);
	    }
	    File input = new File(argv[0]);
	    XLIFFParse xliffp = new XLIFFParse (input);
	    
	}
	catch (Exception e){
	    e.printStackTrace();
	}
        System.exit(0);
    }
    
    
    // doing special stuff for things within bpt and ept segments
    // -- 
    public void characters(char buf[], int offset, int len) throws SAXException {
	
	String s = new String(buf, offset, len);
	switch (state){
	    // all these elements contain pcdata, but can also hold formatting tags
	case SUB:
	case PH:
	case UT:
	case HI:
	case EXTERNALFILE:
	case SOURCE:
	    // add the chars to the formatted and unformatted string buffer
	    formatString=formatString+s;
	    unformatString=unformatString+s;
	    break;
	case BPT:             
	case EPT:
	case IT:
	    // add the chars to the formatted string buffer
	    formatString=formatString+s;
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

	Integer i = (Integer)map.get (qName.toLowerCase());
	if (i == null) {
	    state = BLANK;
	}
	else {
	    state = i.intValue();
	}

	switch (state) {
            case BPT:
                System.out.println("tuid=" + attrs.getValue("tuid"));
                break;
	    case TRANSUNIT:
            boolean hasPut = idSet.add(attrs.getValue("id"));
            if (!hasPut){
	       System.out.println ("WARNING ! id=" +attrs.getValue("id")+" exists more than once in the input file !");
            } else {
               System.out.println("id=" +attrs.getValue("id"));
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
	
	Integer i = (Integer)map.get (qName.toLowerCase());
	if (i == null) {
	    state = BLANK;
	}
	else {
	    state = i.intValue();
	}

	switch (state) {
	
	case BPT:
	case EPT:
	case IT:
	case SUB:
	    break;
	case TRANSUNIT:
	    break;
	case SOURCE:
	    // do something with the formatted and unformatted strings
	    
	    System.out.println (formatString);
	    
	    
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
	System.out.println ("&&"+tagNameArr[state]+"&&");
    }



    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }


    // Pick up the right dtd (ignoring the one specified in the header)
    public InputSource resolveEntity (String publicId, String systemId) {
	System.out.println ("systemId = " + systemId);
	
	File file = new File (systemId);

	System.out.println ("File.getName() is " + file.getName());
	if (file.getName().equals("xliff.dtd")){
	    System.out.println ("Using " + XLIFFDtd);
	    return new InputSource (XLIFFDtd);
	}
	else return null;
    }

}
