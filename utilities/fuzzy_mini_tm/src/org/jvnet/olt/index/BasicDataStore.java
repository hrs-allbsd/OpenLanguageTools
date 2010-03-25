
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

import org.jvnet.olt.minitm.*;
import java.util.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.parsers.*;


public class BasicDataStore
implements DataStore {
    /**
     *  The sequence variable that is used for generationg unique
     *  ids.
     */
    protected IdSequence idSequence;
    
    /**
     *  This variable stores the path to the MiniTM file.
     */
    private String fileName;
    
    /**
     *  This variable stores the name of the MiniTM.
     */
    private String tmName = "NONAMEYET";
    
    private String srclang;
    private String tgtlang;
    
    /**
     *  This Hashtable holds all the TMUnits and is indexed on the
     *  long values that are the data source ids.
     */
    private Hashtable hashTMUnits;
    
    public BasicDataStore(String tmFile,
    boolean boolCreateIfMissing,
    String name,
    String sourceLang,
    String targetLang)
    throws DataStoreException {
        tmName = name;
        srclang = sourceLang;
        tgtlang = targetLang;
        
        idSequence = new IdSequence();
        
        //  Create the data structure that will hold all the TMUnits.
        hashTMUnits = new Hashtable();
        
        //  Check if file exists.
        if(tmFileExists(tmFile)) {
            //  If it does slurp it up into a data structure
            fileName = tmFile;
            
            populateHashtable(hashTMUnits, idSequence, fileName);
        }
        else {
            //  If not check if the flag to create one is set
            //  If so create one.
            if(boolCreateIfMissing) {
                fileName = tmFile;
            }
            else {
                //  Throw a suitable exception here.
                throw new DataStoreException("TM file \"" + tmFile +"\" is either missing, cannot be read, or cannot be written to!");
            }
        }
    }
    
    public void populateHashtable(Hashtable  hash,
    IdSequence sequence,
    String     tmFile)
    throws DataStoreException {
        //  Build the hashtable of TMUnits in here.
        try {
            File file = new File(tmFile);
            FileInputStream ios = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(ios,"UTF8");
            InputSource inSrc = new InputSource(reader);
            
            //  This class does the actual building of TMUnits.
            //  The rest of this code kicks off the SAX parser.
            MiniTmContentHandler handler = new MiniTmContentHandler(hash, sequence);

            //
            //SAXParserFactory factory = org.apache.xerces.jaxp.SAXParserFactoryImpl.newInstance();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setValidating(false);  //the code was generated according DTD
            factory.setNamespaceAware(true);  //the code was generated according DTD
            
            factory.setFeature("http://xml.org/sax/features/external-general-entities", true);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
            XMLReader parser = factory.newSAXParser().getXMLReader();
            parser.setContentHandler(handler);
            
            parser.setErrorHandler(handler);
            
            parser.parse(inSrc);
            
            tmName = handler.getTmName();
            srclang = handler.getSourceLang();
            tgtlang = handler.getTargetLang();
            reader.close();
        }
        catch(javax.xml.parsers.ParserConfigurationException exParse) {
            throw new DataStoreException("Error parsing Mini TM file.\n\n" +
            exParse.getMessage() );
        }
        catch(SAXException saxEx) {
            throw new DataStoreException("Error parsing Mini TM file.\n\n" +
            saxEx.getMessage() );
        }
        catch(IOException ioEx) {
            throw new DataStoreException("Error reading Mini TM file.\n\n" +
            ioEx.getMessage() );
        }
    }
    
    
    public void saveMiniTmToFile()
    throws DataStoreException {
        MiniTmExporter exporter = new MiniTmExporter();
        exporter.export(hashTMUnits, fileName, tmName, srclang, tgtlang);
    }
    
    protected void finalize()
    throws Throwable {
        saveMiniTmToFile();
    }
    
    
    public long insertItem(AlignedSegment segment) {
        long unitId = idSequence.getNextId();
        
        TMUnit unit = new TMUnit(unitId,
        segment.getSource(),
        segment.getTranslation(),
        segment.getTranslatorID());
        
        hashTMUnits.put( new Long(unitId), unit);
        
        return unitId;
    }
    
    public void removeItem(long key) {
        hashTMUnits.remove( new Long(key));
    }
    
    /**
     *  This method retrieves a TMUnit from the hash based on
     *  its key.
     */
    public TMUnit getItem(long key) {
        return (TMUnit) hashTMUnits.get( new Long(key));
    }
    
    public TMUnit[] getAllTMs() {
        int numUnits = hashTMUnits.size();
        TMUnit[] tmUnits = new TMUnit[numUnits];
        
        int i = 0;
        
        Enumeration enumeration = hashTMUnits.elements();
        while(enumeration.hasMoreElements() && (i < numUnits)) {
            tmUnits[i] = (TMUnit) enumeration.nextElement();
            i++;
        }
        
        return tmUnits;
    }
    
    /**
     *  This method returns a string array containing the distinct
     *  translator IDs in the TMUnits of the data store.
     */
    public String[] getAllTranslatorIDs() {
        //  We use a set to filter out duplicates from our list of
        //  translator IDs.
        Set set = new HashSet();
        
        //  Iterate through all the TM Units and insert their
        //  translator IDs into the set.
        Enumeration myenum = hashTMUnits.elements();
        while(myenum.hasMoreElements()) {
            TMUnit unit = (TMUnit) myenum.nextElement();
            set.add(unit.getTranslatorID());
        }
        
        //  Return a string array. The zero size string array is
        //  passed to the toArray method to type the return.
        return (String[]) set.toArray(new String[0]);
    }
    
    public AlignedSegment[] getAllSegments() {
        int numUnits = hashTMUnits.size();
        AlignedSegment[] segments = new AlignedSegment[numUnits];
        
        int i = 0;
        
        TMUnit tempUnit = null;
        
        Enumeration myenum = hashTMUnits.elements();
        while(myenum.hasMoreElements() && (i < numUnits)) {
            tempUnit = (TMUnit) myenum.nextElement();
            segments[i] = new AlignedSegment(tempUnit.getSource(),
            tempUnit.getTranslation(),
            tempUnit.getTranslatorID(),
            tempUnit.getDataSourceKey());
            i++;
        }
        
        return segments;
    }
    
    
    public boolean tmFileExists(String tmFile)
    throws DataStoreException {
        //  I may have to modify this method. There are a number
        //  of reasons why this may fail. The different reasons
        //  are not being communicated to the user.
        File file = new File(tmFile);
        
        boolean boolUsableTMFileExists = ( file.exists()   &&
        file.canRead()  &&
        file.canWrite());
        
        return boolUsableTMFileExists;
    }
    
    public String getTmName() {
        return tmName;
    }
    
    public String getSourceLang() {
        return srclang;
    }
    
    public String getTargetLang() {
        return tgtlang;
    }
}
