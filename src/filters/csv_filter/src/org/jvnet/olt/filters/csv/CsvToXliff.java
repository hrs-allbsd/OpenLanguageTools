/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jvnet.olt.utilities.XliffZipFileIO;

import org.jvnet.olt.filters.segmenters.formatters.*;
import org.jvnet.olt.format.plaintext.PlainTextFormatWrapper;
import org.jvnet.olt.format.FormatWrapper;


/**
 * A class that converts CSV (comma separated value) text files into XLIFF. It
 * takes an input file and an encoding as arguments, and creates an XLZ file from
 * the contents. The filter operates in two different modes :
 *
 * The first mode is where we want to translate the entire CSV file, or portions
 * of it into another language. For that, we allow users to specify an integer
 * array identifying which fields are translatable.
 *
 * In the second mode, we expect that the CSV file is bi-lingual, and has one
 * column containing source text, one column containing the translation,
 * and other columns which aren't translatable. For this, we allow users to
 * specify the source column number, and target column number. All other
 * columns are assumed to be non-translatable.
 *
 * We currently aren't doing any further segmentation on the fields in the input
 * file : this would be possible to implement for the mono-lingual CSV to XLIFF
 * conversion, but very difficult for the bi-lingual converter (when you segment
 * the source string, you would also need to segment any existing target string,
 * and align the resulting source and target sentences correctly - we're not going
 * to go there for the time being)
 *
 * @author timf
 */
public class CsvToXliff {
    
    private Character separator=null;
    private Character escape=null;
    private int totalFields=-1;
    
    private String sourceLang="";
    private List translatableList=null;
    
    private int sourceField=-1;
    private int targetField=-1;
    
    /** Creates a new instance of CsvToXliff
     */
    public CsvToXliff(String sourceLang, Properties props)
    throws CsvToXliffException {
        
        String separatorStr = props.getProperty("field.separator");
        String escapeStr = props.getProperty("escape.char");
        String totalStr = props.getProperty("total.fields");
        if (separatorStr == null || escapeStr == null || totalStr == null){
            throw new CsvToXliffException("Error - keys \"field.separator\", \"escape.char\" and \"total.fields\" must be specified in properties file !");
        }
        
        this.separator = new Character(separatorStr.charAt(0));
        this.escape = new Character(escapeStr.charAt(0));
        this.totalFields = Integer.parseInt(totalStr);
        
        this.sourceLang = sourceLang;
        
        // bi-lingual behaviour
        // System.out.println("Using bilingual CsvToXliff mode.");
        String source = props.getProperty("source.field");
        String target = props.getProperty("target.field");
        if (source == null|| target == null){
            throw new CsvToXliffException("Need to specify properties values for keys "+
                    "\"source.field\" and \"target.field\"");
        }
        this.sourceField = Integer.parseInt(source);
        this.targetField = Integer.parseInt(target);
        if (this.sourceField >= this.totalFields){
            throw new CsvToXliffException("Source field is greater or equal to the total number of fields !"+
                    " (fields are indexed from 0)");
        }
        
        if (this.targetField >= this.totalFields){
            throw new CsvToXliffException("Target field is greater or equal to the total number of fields !"+
                    " (fields are indexed from 0)");
        }
        
        // mono-lingual behaviour
        // System.out.println("Using monolingual CsvToXliff mode.");
        String translatable = props.getProperty("translatable.fields");
        if (translatable == null){
            throw new CsvToXliffException("Need to specify properties value for key \"translatable.fields\"");
        }
        this.translatableList = new ArrayList();
        StringTokenizer tok = new StringTokenizer(translatable,",");
        while (tok.hasMoreTokens()){
            String s = (String)tok.nextToken();
            int number = Integer.parseInt(s);
            if (number >= this.totalFields){
                throw new CsvToXliffException("Translatable field number "+number+
                        " specified in properties file is greater or equal to the total number of fields !"+
                        " (fields are indexed from 0)");
            }
            this.translatableList.add(new Integer(Integer.parseInt(s)));
        }
        
    }
    
    /**
     *
     * This is the mode for bi-lingual CSV files - we expect input something like
     *
     * 1234, This is translatable, ABC, <insert transation here>
     * 5678, This too\, is translatable, DEF, <place for translation>
     *
     * Since column numbers are indexed, starting from 0, for the above example,
     * we might invoke this class using the statement :
     *
     * We expect some properties to be set, which are listed in the constructor
     * for this class.
     *
     * @param csvFile the cvs file to be converted to XLIFF
     * @param encoding the encoding used by the cvsFile
     * @param sourceLang the source langauge in the cvsFile
     * @param sourceField the column number of the source field (columns start at 0)
     * @param targetField the column number of the target field
     * @param totalFields the number of total columns to expect
     * @param separator the column separator character
     * @param escape the separator escape character
     */
    public void convertBi(File csvFile, String encoding)
    throws CsvToXliffException {
        
        if (this.sourceField == -1 ||
                this.targetField == -1 ||
                this.totalFields == -1 ) {
            throw new CsvToXliffException("Need values for source field, target field and total fields for bilingual CSV conversion");
        }
        
        try {
            File xlzFile = new File(csvFile.getAbsolutePath()+".xlz");
            XliffZipFileIO xlzIO = new XliffZipFileIO(xlzFile);
            
            InputStreamReader reader = new InputStreamReader(new FileInputStream(csvFile),encoding);
            
            CsvParser parser = new CsvParser(this.separator.charValue(), this.escape.charValue(), this.totalFields);
            parser.parse(reader);
            
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            
            SegmenterFormatter formatter = new XliffSegmenterFormatter("PLAINTEXT",  sourceLang, csvFile.getName(),
                    xlzIO.getXliffWriter(), xlzIO.getSklWriter(), wrapper);
            
            List csvTokenList = parser.getCsvTokenList();
            writeBilingualXliff(csvTokenList, sourceField, targetField, formatter);
            xlzIO.writeZipFile();
            
        } catch (java.io.IOException e){
            throw new CsvToXliffException("IO Exception converting "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        } catch (CsvParserException e){
            throw new CsvToXliffException("Parse exception converting "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        } catch (SegmenterFormatterException e){
            throw new CsvToXliffException("Exception writing contents for "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        }
        
        
    }
    
    /**
     * This is the mode for monolingual CSV files - we expect input something like
     *
     * 1234, This is translatable, ABC, This is also translatable
     * 5678, This too\, is translatable, DEF, Need to translate this also
     *
     * We expect some properties to be set, which are listed in the documentation
     * for the constructor of this class.
     *
     * @param csvFile the cvs file to be converted to XLIFF
     * @param encoding the encoding used by the cvsFile
     * @param sourceLang the source langauge in the cvsFile
     * @param translate an integer array of translatable fields (columns start at 0)
     * @param separator the column separator character
     * @param escape the separator escape character
     */
    public void convertMono(File csvFile, String encoding)
    throws CsvToXliffException {
        
        if (this.translatableList == null){
            throw new CsvToXliffException("Need list of translatable fields for mono-lingual CSV conversion!");
        }
        if (this.totalFields == -1) {
            throw new CsvToXliffException("Need total fields to be a positive whole number.");
        }
        
        try {
            File xlzFile = new File(csvFile.getAbsolutePath()+".xlz");
            XliffZipFileIO xlzIO = new XliffZipFileIO(xlzFile);
            
            InputStreamReader reader = new InputStreamReader(new FileInputStream(csvFile),encoding);
            
            CsvParser parser = new CsvParser(this.separator.charValue(), this.escape.charValue(), this.totalFields);
            parser.parse(reader);
            
            FormatWrapper wrapper = new PlainTextFormatWrapper();
            
            SegmenterFormatter formatter = new XliffSegmenterFormatter("PLAINTEXT",  sourceLang, csvFile.getName(),
                    xlzIO.getXliffWriter(), xlzIO.getSklWriter(), wrapper);
            
            List csvTokenList = parser.getCsvTokenList();
            Set translatableFields = makeTranslatableSet(this.translatableList);
            writeMonolingualXliff(csvTokenList, translatableFields, formatter);
            xlzIO.writeZipFile();
            
        } catch (java.io.IOException e){
            throw new CsvToXliffException("IO Exception converting "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        } catch (CsvParserException e){
            throw new CsvToXliffException("Parse exception converting "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        } catch (SegmenterFormatterException e){
            throw new CsvToXliffException("Exception writing contents for "+csvFile.getAbsolutePath()+" "+e.getMessage(),e);
        }
        
    }
    
    private void writeBilingualXliff(List csvTokenList, int sourceField, int targetField, SegmenterFormatter formatter)
    throws SegmenterFormatterException {
        
        // somewhere to save the source string
        String source = "";
        
        // iterate over our token list
        for (int i=0 ; i<csvTokenList.size(); i++){
            
            CsvToken t = (CsvToken)csvTokenList.get(i);
            
            switch (t.getType()){
                
                case CsvTokenTypes.VALUE:
                    if (t.getFieldNumber() == sourceField) {
                        source = t.getValue();
                        // okay to write the source field as formatting
                        formatter.writeFormatting(source);
                        
                    } else if (t.getFieldNumber() == targetField){
                        // write the segment - we haven't got a wordcounter yet
                        formatter.writeSegment(source, t.getValue(), -1);
                        source = "";
                        
                    } else {
                        formatter.writeFormatting(t.getValue());
                    }
                    break;
                    
                case CsvTokenTypes.SEPARATOR:
                case CsvTokenTypes.NEWLINE:
                    // write these as formatting
                    formatter.writeFormatting(t.getValue());
            }
        }
        formatter.flush();
    }
    
    private void writeMonolingualXliff(List csvTokenList, Set translatableFields, SegmenterFormatter formatter)
    throws SegmenterFormatterException{
        // iterate over our token list
        for (int i=0 ; i<csvTokenList.size(); i++){
            
            CsvToken t = (CsvToken)csvTokenList.get(i);
            
            switch (t.getType()){
                
                case CsvTokenTypes.VALUE:
                    if (translatableFields.contains(new Integer(t.getFieldNumber()))){
                        // write the segment - we haven't got a wordcounter yet
                        formatter.writeSegment(t.getValue(), -1);
                    } else {
                        formatter.writeFormatting(t.getValue());
                    }
                    break;
                    
                case CsvTokenTypes.SEPARATOR:
                case CsvTokenTypes.NEWLINE:
                    // write these as formatting
                    formatter.writeFormatting(t.getValue());
            }
        }
        formatter.flush();
    }
    
    /**
     * Simple method to create a Set containing all of the integers
     * in the input : I do this just to make sure they're unique
     */
    private Set makeTranslatableSet(List translatable){
        Set translatableSet = new HashSet();
        for (int i=0; i< translatable.size(); i++){
            translatableSet.add(translatable.get(i));
        }
        return translatableSet;
    }
    
    
    /**
     * Simple main method to test the CsvToXliff filter - we require two arguments
     * a source file for conversion and a properties file containing configuration
     * for the filter
     */
    public static void main(String[] args){
        try {
            if (args.length != 4){
                System.out.println("Usage : CsvToXliff <source file> <properties file> <source lang> [bi|mono] ");
                System.exit(1);
            }
            
            File file = new File(args[0]);
            String encoding = System.getProperty("file.encoding");
            
            Properties props = new Properties();
            props.load(new FileInputStream(args[1]));
            
            CsvToXliff converter = new CsvToXliff(args[2], props);
            if (args[3].equals("bi")){
                converter.convertBi(new File(args[0]), encoding);
            } else if (args[3].equals("mono")){
                converter.convertMono(new File(args[0]), encoding);
            } else {
                System.out.println("Unknown filter mode - not doing conversion.");
            }
            
            
            
        } catch (Exception e){ // pretty simple exception handling here
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
    
}
