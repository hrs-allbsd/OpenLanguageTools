package org.jvnet.olt.filetype;

import java.util.*;
import java.util.regex.*;
import java.io.*;

/**
 * Following code is detecting supported file types
 *
 * @author  mt150864
 */
public class FileTypeDetector {
    
    // All file types supported in SunTrans
    public static final int UNDEFINED = 0;
    public static final int HTML = 1;
    public static final int TEXT = 2;
    public static final int XLZ = 3;
    public static final int SGML = 4;
    public static final int BOOK = 5;
    public static final int XML = 6;
    public static final int JSP = 7;
    public static final int PO = 8;
    public static final int JAVA = 9;
    public static final int PROPERTIES = 10;
    public static final int MSG = 11;
    public static final int FRAME = 12;
    public static final int DTD = 13;
    public static final int OPENOFFICE = 14;
    public static final int ULA = 15;
    public static final int MIF = 16;
    public static final int ART = 17;
    public static final int CSS = 18;
    public static final int STAROFFICE = 19;
    
    // File extension expression to file type mapping
    private static List filetypes = null;
    
    /**
     * Singleten constructor
     */
    private FileTypeDetector() {
    }
    
    /**
     *
     * Create one time filetype list
     *
     * @return List that contains all defined filetypes
     */
    private static List getFiletypes() {
        if(filetypes == null) {
            
            filetypes = Collections.synchronizedList(new ArrayList());
            
            //HTML
            BasicFileType fileType = new BasicFileType(HTML,"HyperText Markup Language (HTML)");
            fileType.addExtension("htm");
            fileType.addExtension("html");
            fileType.addExtension("xhtml");
            fileType.setRegExpContent("<html>");
            filetypes.add(fileType);
            
            //TEXT
            fileType = new BasicFileType(TEXT,"Simple Text File");
            fileType.addExtension("txt");
            filetypes.add(fileType);
            
            //XLZ
            fileType = new BasicFileType(XLZ,"SunTrans XLIFF file");
            fileType.addExtension("xlz");
            filetypes.add(fileType);
            
            //SGML
            fileType = new BasicFileType(SGML,"Standard Generalized Markup Language (SGML)");
            fileType.addExtension("sgm");
            fileType.addExtension("sgml");
            fileType.addExtension("\\d{1}[a-zA-Z]*");
            fileType.setRegExpContent("<!ENTITY.*?>");
            
            filetypes.add(fileType);
            
            //BOOK
            fileType = new BasicFileType(BOOK,"SGML Book file");
            fileType.addExtension("book");
            fileType.setRegExpContent("<!DOCTYPE BOOK.*?>");
            filetypes.add(fileType);
            
            //XML
            fileType = new BasicFileType(XML,"Extensible Markup Language (XML)");
            fileType.addExtension("xml");
            fileType.setRegExpContent("<\\?xml.*?\\?>");
            filetypes.add(fileType);
           
           //JSP
            fileType = new BasicFileType(JSP,"JavaServer Pages (JSP)");
            fileType.addExtension("jsp");
            fileType.setRegExpContent("<\\%\\@.*?\\%>");
            filetypes.add(fileType);
            
            //JAVA
            fileType = new BasicFileType(JAVA,"Java language file");
            fileType.addExtension("java");
            filetypes.add(fileType);
            
            //PO
            fileType = new BasicFileType(PO,"Portable Object File");
            fileType.addExtension("po");
            filetypes.add(fileType);
            
            //PROPERTIES
            fileType = new BasicFileType(PROPERTIES,"Java Properties File");
            fileType.addExtension("properties");
            filetypes.add(fileType);
            
            //MSG
            fileType = new BasicFileType(MSG,"Message Help File");
            fileType.addExtension("msg");
            fileType.addExtension("tmsg");
            filetypes.add(fileType);
            
            //DTD - mozilla dtd files
            fileType = new BasicFileType(DTD,"Mozilla DTD files");
            fileType.addExtension("dtd");
            filetypes.add(fileType);
            
            //FRAME
            fileType = new BasicFileType(FRAME,"Frame Maker File");
            fileType.addExtension("mif");
            filetypes.add(fileType);
            
            //OPENOFFICE
            fileType = new BasicFileType(OPENOFFICE,"OpenOffice Document File");
            fileType.addExtension("sxw");
            fileType.addExtension("sxc");
            fileType.addExtension("sxi");
            filetypes.add(fileType);
            
            //ULA
            fileType = new BasicFileType(ULA,"Ultimate Localization Automation (ULA)");
            fileType.addExtension("ula");
            filetypes.add(fileType);
            
            //ART
            fileType = new BasicFileType(ART,"Graphic ART file");
            fileType.addExtension("eps");
            fileType.addExtension("epsi");
            fileType.addExtension("jpg");
            fileType.addExtension("png");
            fileType.addExtension("tif");
            fileType.addExtension("tiff");
            fileType.addExtension("gif");
            fileType.addExtension("svg");
            fileType.addExtension("xpm");
            fileType.addExtension("rs");
            filetypes.add(fileType);

            //CSS
            fileType = new BasicFileType(CSS,"Cascading Style Sheets");
            fileType.addExtension("css");
            filetypes.add(fileType);
            
            //STAROFFICE XLIFF
            fileType = new BasicFileType(STAROFFICE,"Staroffice database file");
            fileType.addExtension("xliff");
            filetypes.add(fileType);
        }
        return filetypes;
    }
    
    
    /**
     * Methods that should guess file type from file name
     *
     * @param file that we need to process
     * @return file type defined in this class or undefined filetype if there is no definition for given type
     *
     */
    public static FileType getDefinition(int type) {
        FileType result = new BasicFileType(UNDEFINED,"Undefined File type");
        
        List filetypes = getFiletypes();
        
        Iterator it = filetypes.iterator();
        while(it.hasNext()) {
            FileType ft = (FileType)it.next();
            if(ft.getType()==type) {
                result = ft;
                break;
            }
            
        }
        return result;
    }
    
    /**
     * Detect file type for given file name
     *
     * @param file that we need detect
     * @return file type defined with integere value
     *
     */
    public static Integer detectFileType(File file) throws IOException {
        List filetypes = getFiletypes();
        
        // detect extension first
        Iterator it = filetypes.iterator();
        while(it.hasNext()) {
            FileType ft = (FileType)it.next();
            if(ft.detectExtension(file)) {
                return new Integer(ft.getType());
            }
        }
        
        // try advanced method
        it = filetypes.iterator();
        while(it.hasNext()) {
            FileType ft = (FileType)it.next();
            if(ft.detect(file)) {
                return new Integer(ft.getType());
            }
        }
        
        return new Integer(UNDEFINED);
    }
    
    
}
