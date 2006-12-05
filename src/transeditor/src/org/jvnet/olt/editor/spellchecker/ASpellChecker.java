/*
 * ASpellChecker.java
 *
 * Created on November 16, 2006, 1:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import org.jvnet.olt.editor.spellchecker.SpellChecker.Session;

/**
 *
 * @author boris
 */
public class ASpellChecker extends SpellChecker{
    private static final Logger logger = Logger.getLogger(ASpellChecker.class.getName());
    
    private  Set<Session> sessions = new HashSet<Session>();
    
    private static final List<DefaultSuggestion> NOTHING = Collections.emptyList();
    
    class ASpellSession implements SpellChecker.Session{
        
        PrintWriter pw;
        BufferedReader r;
        Process p;
        
        boolean firstRun = true;;
        
        ASpellSession(Process p) throws IOException {
            this.p = p;
            
            this.pw = new PrintWriter(p.getOutputStream());
            this.r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            do{
                String line = r.readLine();
                if(line == null)
                    throw new IOException("end of stream");
                if(parseReply(line) == null)
                    break;
            }
            while(true);
        }
        
        public void wordAccpeted(String s) {
            // we do nothing in this implementation
        }
        
        public Suggestion[] getSuggestions(String word) {
            
            pw.println(word);
            pw.flush();
            
            try{
                List<DefaultSuggestion> rv = new LinkedList<DefaultSuggestion>();
                do{
                    String line = r.readLine();
                    
                    if(line == null || "".equals(line.trim())){
                        break;
                    }
                    List<DefaultSuggestion> partial = parseReply(line);
                    
                    if(partial == null)
                        continue;
                    
                    rv.addAll(partial);
                }
                while(true);
                
                return rv.toArray(new DefaultSuggestion[]{});
            } catch (IOException ioe){
                logger.warning("Exception while reading from aspell:"+ioe);
                return new DefaultSuggestion[]{};
            }
        }
        
        
        public void addToPersonal(String s) {
            pw.print("* ");
            pw.println(s);
            pw.flush();
            pw.println("#");
            pw.flush();
        }
        
        public void ignoreWord(String word, boolean alwaysIgnore) {
            //this implementation does nothing
        }
        
        void close(){
            pw.close();
            
            try{
                r.close();
            } catch (IOException ioe){
                logger.warning("Exception while closing spellchecker:"+ioe);
            }
            
            // call destroy ??
            // check exitValue ?
        }
        
        List<DefaultSuggestion> parseReply(String reply){
            if(reply == null || "".equals(reply.trim()) ){
                return NOTHING;
            }
            
            //header => need next line
            if(reply.startsWith("@")){
                return null;
            } else if(reply.startsWith("*")){
                return NOTHING;
            } else if(reply.startsWith("&")){
                int delimIdx = reply.indexOf(":");
                if(delimIdx == -1)
                    return NOTHING;
                
                String candidates = (delimIdx + 1 >= reply.length()) ? "" : reply.substring(delimIdx+1);
                String[] parts = candidates.split(",");
                
                List<DefaultSuggestion> rv = new LinkedList<DefaultSuggestion>();
                
                for(String p: parts){
                    if(p == null || "".equals(p.trim()))
                        continue;
                    rv.add(new DefaultSuggestion(p.trim() ) );
                }
                
                
                return rv;
            } else
                logger.warning("Unexpected reply read:"+reply);
            
            return NOTHING;
            
        }
        
    }
    
    /** Creates a new instance of ASpellChecker */
    private ASpellChecker(Properties p) {
        super(p);
    }
    
    public SpellChecker.Session startSession(String lang) throws SessionStartException {
        String[] cmd = createASpellCommand(lang);
        
        Session session = null;
        
        try{
            Process p = Runtime.getRuntime().exec(cmd);
            
            if(!checkProcessOK(p)){
                StringBuilder sb = new StringBuilder();
                for(String s: cmd){
                    sb.append(s).append(" ");
                }
                
                logger.warning("Starting the process '"+sb.toString()+"' has exited");
                
                throw new SessionStartException();
            }
            
            try{
                Session s = new ASpellSession(p);
                sessions.add(s);
                return s;
            } catch (IOException ioe){
                logger.warning("IOException while startup:"+ioe);
                throw new SessionStartException(ioe);
            }
        } catch (IOException ioe){
            throw new SessionStartException(ioe);
        }
    }
    
    boolean  checkProcessOK(Process p){
        try{
            int rv = p.exitValue();
            
            return false;
        } catch (IllegalThreadStateException itse){
            //this is ok
        }
        return true;
    }
    
    public boolean endSession(SpellChecker.Session sess) {
        
        if(sessions.contains(sess)){
            ((ASpellSession)sess).close();
            return sessions.remove(sess);
        }
        
        return false;
    }
    
    static public SpellChecker create(Properties prop){
        return new ASpellChecker(prop);
    }
    
    public String[] getSupportedLanguages() {
        return null;
    }
    
    private String[] createASpellCommand(String lang) {
        //  Intorduce an evil hack here: different command strings based on the
        //  OS platform the system is running on.
        //boolean boolUsingSunOS = System.getProperty("os.name").equals("SunOS");
        
        String dictLang = lang;
        
        String os = System.getProperty("os.name").toLowerCase();
        
        boolean onWin = os.startsWith("windows");
        boolean onLin = os.indexOf("linux") != -1;
        boolean onSol = os.startsWith("sunos");
        
        //fallback to SOL. Do far
        if(!onWin && !onLin && !onSol)
            onSol = true;
        
        String[] cmd = new String[5];
        String aspellHomePath = "/usr/bin/aspell";
        
        logger.finest("Aspell home:"+aspellHomePath);
        
        if (onLin) {
            //  Command to use on Linux; we assume there is an aspell installed somewhere
            cmd[0] = "aspell";
            cmd[1] = "--lang="+dictLang;
            cmd[2] = "-a";
            cmd[3] = "";
            cmd[4] = "";
        } else if(onSol){
            //  Command to use on Solaris
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + lang;
            cmd[2] = "--dict-dir=" + aspellHomePath + "lib" + File.separator + "aspell";
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell";
            cmd[4] = "-a";
        } else if(onWin){
            //  Command to use on other platforms, i.e., Windows
            cmd[0] = aspellHomePath + "bin" + File.separator + "aspell";
            cmd[1] = "--lang=" + dictLang;
            cmd[2] = "--master=" + aspellHomePath + "dict" + File.separator + "aspell" + File.separator + dictLang;
            cmd[3] = "--data-dir=" + aspellHomePath + "share" + File.separator + "aspell";
            cmd[4] = "-a";
            
        }
        logger.finest("Command:"+cmd[0]+" "+cmd[1]+" "+cmd[2]+" "+cmd[3]+" "+cmd[4]);
        logger.finest("Are we running on win? "+onWin);
        
        
        return cmd;
        
    }
    
    public String getDisplayName() {
        return "ASpell";
    }
    
    public String getName() {
        return "aspell";
    }

    protected void finalize() throws Throwable {
        if(sessions != null && !sessions.isEmpty()){
            logger.warning("The spellcheckers sessions have not been finished!");
            for(Session s: sessions)
                endSession(s);
        }
    }
    
}
