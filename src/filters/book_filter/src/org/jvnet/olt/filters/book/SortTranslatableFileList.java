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
 * SortTranslatableFileList.java
 *
 * Created on June 3, 2003, 9:53 AM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.format.GlobalVariableManager;
import java.util.*;
import java.io.*;
import java.util.logging.*;
/**
 * This class is designed to run over a list of files that have been
 * sent to the SunTrans system looking for sgml .book files.
 * Once a book file is found, all files in that directory (and it's
 * subdirectories are sent to the Book class as candidates for
 * translation)
 *
 * @author  timf
 */
public class SortTranslatableFileList  {
    
    /** Creates a new instance of SortTranslatableFileList. It's main task is to populate
     *  two maps. The first map is a hash containing the global variable managers produced
     *  for the files in this job - the key for each entry in the map is the .book file name.
     *
     *  The second map is a map of all the translatable files, and which .book files they
     *  belong to.
     *
     *  Given any file, further down the processing chain, we lookup that filename in the
     *  translatable map : if it has an entry, then it's translatable, and we can find out
     *  the name of the book file for that file.
     *
     *  Next, we can retrieve the gvm for that file from the other map : allowing us to
     *  resolve any marked sections that are included in this translatable file.
     *
     *  @param filelist a list of files to find translatable sgml files in
     *  @param sourceEncoding the file encoding to open the sources files in
     */
    private HashMap globalVariableManagerMap;
    private HashMap translatableFileMap;
    private HashMap singleSegTranslatableFileMap;
       
    public SortTranslatableFileList(List filelist, String sourceEncoding, Logger logger) throws BookException {
        logger.log(Level.CONFIG,"In STF constructor");
        try {
            if (logger == null){
                logger = LogManager.getLogManager().getLogger("org.jvnet.olt.filters.book");
            }                
            globalVariableManagerMap = new HashMap();
            translatableFileMap = null;
            singleSegTranslatableFileMap = null;
            
            Iterator it = filelist.iterator();
            while (it.hasNext()){
                String filename = (String)it.next();
                String ext = getExtension(filename);
                // we ignore all files in the input, except the book file
                if (ext.equals("book")){
                    try { 
                        HashSet translatableSet = new HashSet();
                        HashSet singleSegTranslatableSet = new HashSet();
                        translatableFileMap = new HashMap();
                        singleSegTranslatableFileMap = new HashMap();
                    
                        File file = new File(filename);
                        String directory = file.getParent();
                        if (directory == null){
                            directory = System.getProperty("user.dir");
                        }
                        // nasty hack here to normalise filenames
                        filename = filename.replaceAll("//","/");
                        logger.log(Level.CONFIG,"Found book file " + filename +" which comes from directory " + directory);
                        // now, based on these files, we work out which are translatable
                        BufferedReader reader = new BufferedReader(new InputStreamReader
                        (new FileInputStream(file),sourceEncoding));
                        Book book = new Book(reader, directory, null, logger, false);
                        translatableSet = book.getTranslatableFiles();
                        singleSegTranslatableSet = book.getSingleSegTranslatableFiles();
                         
                        GlobalVariableManager gvm = book.getGlobalVariableManager();
                        // store the gvm for this book in a hashMap
                        globalVariableManagerMap.put(filename, gvm);
                    
                    
                        // next store each translatable file - and which gvm it's using
                        // in the translatableFileMap
                        Iterator iter = translatableSet.iterator();
                        while (iter.hasNext()){
                            String s = (String)iter.next();
                            logger.log(Level.CONFIG,"Translatable file from "+file.getName()+" : "+directory+"/"+s);
                            translatableFileMap.put(directory+"/"+s,filename);
                        }
                    
                        // now do the same thing for the single segment translatable files
                        iter = singleSegTranslatableSet.iterator();
                        while (iter.hasNext()){
                            String s = (String)iter.next();
                            logger.log(Level.CONFIG,"Single segment translatable file from "+file.getName()+" : "+directory+"/"+s);
                            singleSegTranslatableFileMap.put(directory+"/"+s,filename);
                        }
                    } catch (BookException e) {
                        logger.log(Level.SEVERE,"Cannot parse book file: " + filename,e); 
                    }
                }
            }
        } catch (java.io.IOException e){
            logger.log(Level.WARNING, "IO exception trying to read book file " + e.getMessage());
        }
        
    }
    
    /** This method takes in a filename, and tries to detect it's type. At the moment
     * this is done purely on filename extension
     *
     */
    private static String getExtension(String filename){
        
        String ext = "";
        StringTokenizer st = new StringTokenizer(filename, ".");
        while (st.hasMoreTokens()){
            ext = st.nextToken();
        }
        ext = ext.toLowerCase();
        return ext;
    }
    
    
    /**
     * Simply finds all files under a given directory
     */
    private static List getAllFilesUnder(String directory){
        List fileList = new ArrayList();
        File dir = new File(directory);
        String[] names = dir.list();
        if (names != null){
            for (int i=0; i< names.length; i++){
                File filename = new File(dir.getPath() + "/"+names[i]);
                if (filename.isDirectory()){
                    String path = dir.getPath();
                    if (path.equals("")){
                        path = ".";
                    }
                    String file = path + "/" + names[i];
                    fileList.addAll(getAllFilesUnder(path + "/" + names[i]));
                }
                else {
                    String path = dir.getPath();
                    if (path.equals("")){
                        path = ".";
                    }
                    String file = path + "/" + names[i];
                    fileList.add(file);
                }
                
            }
        }
        return fileList;
    }
    
    
    
    public static void main(String[] args){
        //List l = getAllFilesUnder(args[0]);
        List l = initTestList();
        try {
        SortTranslatableFileList t = new SortTranslatableFileList(l, "ISO8859-1", null);
        } catch (BookException e ){
            System.out.println("A BookFileException was thrown " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List initTestList(){
        ArrayList l = new ArrayList();
        l.add("./SYSADV4/fr-legal.sgm");
        l.add("./SYSADV4/fr-other-trademarks.sgm");
        l.add("./SYSADV4/fr-sun-trademarks.sgm");
        l.add("./SYSADV4/glossary.sgm");
        l.add("./SYSADV4/legal.sgm");
        l.add("./SYSADV4/mailadmin.sgm");
        l.add("./SYSADV4/mailintro.sgm");
        l.add("./SYSADV4/mailrefer.sgm");
        l.add("./SYSADV4/mailtm.sgm");
        l.add("./SYSADV4/modemtm.sgm");
        l.add("./SYSADV4/nca.sgm");
        l.add("./SYSADV4/netmonitor.sgm");
        l.add("./SYSADV4/netmonitortm.sgm");
        l.add("./SYSADV4/netservices.sgm");
        l.add("./SYSADV4/nsov.sgm");
        l.add("./SYSADV4/other-trademarks.sgm");
        l.add("./SYSADV4/ppp.auth.sgm");
        l.add("./SYSADV4/ppp.convert.sgm");
        l.add("./SYSADV4/ppp.dialin.sgm");
        l.add("./SYSADV4/ppp.intro.sgm");
        l.add("./SYSADV4/ppp.leased.sgm");
        l.add("./SYSADV4/ppp.p2plink.sgm");
        l.add("./SYSADV4/ppp.pppoe.sgm");
        l.add("./SYSADV4/ppp.reference.sgm");
        l.add("./SYSADV4/ppp.trouble.sgm");
        l.add("./SYSADV4/preface.sgm");
        l.add("./SYSADV4/remoteHowToAccess.sgm");
        l.add("./SYSADV4/rfsadmin.sgm");
        l.add("./SYSADV4/rfsintro.sgm");
        l.add("./SYSADV4/rfsrefer.sgm");
        l.add("./SYSADV4/rfstm.sgm");
        l.add("./SYSADV4/rmconfig.sgm");
        l.add("./SYSADV4/rmconsole.sgm");
        l.add("./SYSADV4/rmctrl.sgm");
        l.add("./SYSADV4/rmexacct.sgm");
        l.add("./SYSADV4/rmintro.sgm");
        l.add("./SYSADV4/rmpools.sgm");
        l.add("./SYSADV4/rm.rcapd.sgm");
        l.add("./SYSADV4/rmsched.sgm");
        l.add("./SYSADV4/rmtask.sgm");
        l.add("./SYSADV4/rm.tm.sgm");
        l.add("./SYSADV4/sagset.sgm");
        l.add("./SYSADV4/sendmail.sgm");
        l.add("./SYSADV4/slp.config.sgm");
        l.add("./SYSADV4/slp.intro.sgm");
        l.add("./SYSADV4/slp.legacy.sgm");
        l.add("./SYSADV4/slp.reference.sgm");
        l.add("./SYSADV4/slp.setup.sgm");
        l.add("./SYSADV4/slp.tm.sgm");
        l.add("./SYSADV4/sundocs.sgm");
        l.add("./SYSADV4/sun-trademarks.sgm");
        l.add("./SYSADV4/time.sgm");
        l.add("./SYSADV4/typeconv.sgm");
        l.add("./SYSADV4/updates.sgm");
        l.add("./SYSADV4/uucpov.sgm");
        l.add("./SYSADV4/uucpref.sgm");
        l.add("./SYSADV4/uucptasks.sgm");
        l.add("./SYSADV4/wuftp.sgm");
        l.add("./SYSADV4/wwrsov.sgm");
        l.add("./SYSADV4/wwrstm.sgm");
        l.add("./SYSADV4/SYSADV4.book");
        l.add("./SYSADV4/SYSADV4.ent");
        return l;
    }
    /**
     * This method returns the translatable file map found. This maps
     * the absolute path of each file called from the book file to the
     * book file itself. Note that the return value can be null : which 
     * suggests that we haven't found a .book file at all, rather than
     * a book file that just doesn't include any files.
     */
    public HashMap getTranslatableFileMap(){
        return this.translatableFileMap;
    }
    
    /**
     * This method returns the single segment translatable file map found.
     * This maps the absolute path of each file containing a programlisting or likewise
     * called from the book file to the
     * book file itself. Note that the return value can be null : which 
     * suggests that we haven't found a .book file at all, rather than
     * a book file that just doesn't include any files.
     */
    public HashMap getSingleSegTranslatableFileMap(){
        return this.singleSegTranslatableFileMap;
    }
    
    
    /**
     * This returns a map containing the global variable managers
     * found in this delivery (list of files). Normally, a delivery 
     * only contains one book file, so there's only one entry in this map
     * (which maps the name of the book file to the GlobalVariableManager object)
     * If there are no book files found, then we return an empty map
     * - not an null !
     */
    public HashMap getGlobalVariableManagerMap(){
        return this.globalVariableManagerMap;
    }
}
