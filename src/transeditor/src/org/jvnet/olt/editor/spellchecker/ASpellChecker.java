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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import org.jvnet.olt.editor.spellchecker.SpellChecker.Session;

/**
 *
 * @author boris
 */
public class ASpellChecker extends SpellChecker{
    
    private enum OperatingSystem { LINUX, WINDOWS, SOLARIS,OTHER };
     
    private static final String VAR_EDITOR_HOME="${EDITOR_HOME}";
    private static final String VAR_PROJECT_LANG="${PROJECT_LANG}";
    private static final String VAR_TRANS_PROJECT_LANG="${TRANS_PROJECT_LANG}";
    private static final String VAR_USER_HOME="${USER_HOME}";
    
    
    private static final Logger logger = Logger.getLogger(ASpellChecker.class.getName());
    
    private  Set<Session> sessions = new HashSet<Session>();
    
    private static final List<DefaultSuggestion> NOTHING = Collections.emptyList();
    
    private String lastCmd;
    private String defaultCommand;
    
    ASpellCustomizerPanel customizer;
    
    Map<String,String> mappingTable = new HashMap<String,String>();
    
    
    class ASpellSession implements SpellChecker.Session{
        
        PrintWriter pw;
        BufferedReader r;
        Process p;
        
        boolean firstRun = true;;
        
        ASpellSession(Process p) throws IOException {
            this.p = p;
            
            this.pw = new PrintWriter(p.getOutputStream());
            this.r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            readBanner(r);
        }
        
        private void readBanner(BufferedReader r)throws IOException {
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
            if(!checkProcessOK(p)){
                try{
                    int retries = 5;
                    while(!checkProcessOK(p) && retries-- >= 0){
                        p = startProcess(lastCmd);
                        this.pw = new PrintWriter(p.getOutputStream());
                        this.r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        
                        readBanner(r);
                        
                    }
                    
                    if(retries == 0)
                        return new DefaultSuggestion[]{};
                } catch (SessionStartException ex){
                    logger.warning("Exception while starting spellchecker:"+ex);
                    return new DefaultSuggestion[]{};
                } catch (IOException ex) {
                    logger.warning("Exception while starting spellchecker:"+ex);
                    return new DefaultSuggestion[]{};
                }
                
                
            }
            
            
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
        
        public boolean close(){
            //call the owner to do the house cleaning and call back
            //to realClose(); see you there ;-)
            return endSession(this);
        }
        
        
        //do the real clean up here
        protected boolean realClose(){
            pw.close();
            
            try{
                r.close();
            } catch (IOException ioe){
                logger.warning("Exception while closing spellchecker:"+ioe);
            }
            
            // call destroy ??
            // check exitValue ?
            return true;
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
        
        @Override
        protected void finalize(){
            endSession(this);
        }
        
    }
    
    /** Creates a new instance of ASpellChecker */
    private ASpellChecker(Properties p) {
        super(p);
    }
    
    
    
    public SpellChecker.Session startSession(String lang) throws SessionStartException {
        lastCmd = createASpellCommand(defaultCommand,lang);
        
        Session session = null;
        
        try{
            
            Process p = startProcess(lastCmd);
            
            try{
                session = new ASpellSession(p);
                sessions.add(session);
                return session;
            } catch (IOException ioe){
                logger.warning("IOException while startup:"+ioe);
                if(session != null)
                    endSession(session);
                throw new SessionStartException(lastCmd,ioe);
            } finally {
                
            }
        } catch (IOException ioe){
            throw new SessionStartException(lastCmd,ioe);
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
            ((ASpellSession)sess).realClose();
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
    
    private String createASpellCommand(String fullCommand,String lang){
        String tgtLang = lang;
        if(mappingTable.containsKey(lang)){
            tgtLang = mappingTable.get(lang);
        }else{
            // provide a maybe similar language
            String part = lang.split("-")[0];
            if(mappingTable.containsKey(part)){
                tgtLang = mappingTable.get(part);
            }
        }
        
        StringBuilder sb = new StringBuilder(fullCommand);

        String[][] map = new String[][]{
            {VAR_EDITOR_HOME,props.getProperty("EDITOR_HOME") },
            {VAR_USER_HOME,props.getProperty("USER_HOME") },
            {VAR_PROJECT_LANG,lang },
            {VAR_TRANS_PROJECT_LANG,tgtLang},
            
        };
        
        for(int i = 0; i < map.length;i++){
            String src = map[i][0];
            String tgt = map[i][1];
            
            //bogus counter prevents us from "eternal loop" (tm)
            int bogusCounter = 111;
            int idx = 0;
            while((idx = sb.indexOf(src)) != -1 && --bogusCounter > 0){
                sb.replace(idx,idx+src.length(),tgt);            
            }
        }

        return sb.toString();
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
    
    protected Process startProcess(String cmd) throws SessionStartException,IOException{
        Process p = Runtime.getRuntime().exec(cmd);
        try{
            Thread.currentThread().sleep(1000);
        }
        catch(InterruptedException ie){
            logger.warning("wait for child process has been interrupted");
        }
        if(!checkProcessOK(p)){
            
            logger.warning("Starting the process '"+cmd+"' has exited");
            
            throw new SessionStartException(cmd);
        }
        return p;
    }
    
    public JComponent getCustomizer(){
        if(customizer == null){
            customizer = new ASpellCustomizerPanel(this,mappingTable);
        }
        
        return customizer;
    }
    
    String makeDefaultCommand(){

        String s = File.separator;
        String q = "\"";
        String cmd =null;
        
        OperatingSystem os = opereratingSystem();
        switch (os){
            case WINDOWS:
                cmd= q+VAR_EDITOR_HOME+s+"spellchecker"+s+"bin"+s+"aspell.exe"+q+" ";
                cmd+="--lang="+VAR_TRANS_PROJECT_LANG+" ";
                cmd+="--master="+q+VAR_EDITOR_HOME+s+"spellchecker"+s+"dict"+s+"aspell"+s+VAR_TRANS_PROJECT_LANG+q+" ";
                cmd+="--data-dir="+q+VAR_EDITOR_HOME+s+"spellchecker"+s+"share"+s+"aspell"+q+" ";
                cmd+=" -a";                
            break;
            case SOLARIS:
                cmd= VAR_EDITOR_HOME+s+"spellchecker"+s+"bin"+s+"aspell ";
                cmd+="--lang="+VAR_TRANS_PROJECT_LANG;
                cmd+=" --dict-dir="+VAR_EDITOR_HOME+s+"spellchecker"+s+"lib"+s+"aspell"+s;
                cmd+=" --data-dir="+VAR_EDITOR_HOME+s+"spellchecker"+s+"share"+s+"aspell";
                cmd+=" -a";
            break;
            case LINUX:
            case OTHER:
                cmd = s+"usr"+s+"bin"+s+"aspell -a --lang="+VAR_TRANS_PROJECT_LANG;
                
        }

        return cmd;
    }
    
    protected void storeConfig(Preferences prefs) {
        if(customizer != null){
            defaultCommand = customizer.getCommandTextField().getText();
            customizer = null;
        }
        prefs.put("binary",defaultCommand);
        
        try{
            if(prefs.nodeExists("lang_mapping")){
                Preferences n = prefs.node("lang_mapping");
                n.removeNode();
                n.flush();
            }
            
            Preferences n = prefs.node("lang_mapping");
            
            for(Map.Entry<String,String> e: mappingTable.entrySet()){
                n.put(e.getKey(),e.getValue());
            }
            
        } catch (BackingStoreException bse){
            
        }
    }
    
    protected void loadConfig(Preferences prefs) {
        defaultCommand = prefs.get("binary",makeDefaultCommand());
        
        try{
            if(prefs.nodeExists("lang_mapping")){
                Preferences n = prefs.node("lang_mapping");
                String[] keys = n.keys();
                
                mappingTable = new HashMap<String,String>();
                
                for(String key: keys)
                    mappingTable.put(key,n.get(key,""));
                
            } else{
                String[][] defaults = null;
                OperatingSystem os = opereratingSystem();
                switch (os){
                    case WINDOWS:
                        defaults = new String[][]{
                            {"DE","german"},
                            {"FR","francais"},
                            {"ES","spanish"},
                            {"IT","italian"},
                            {"SV","swedish"},
                        };
                     break;
                     case SOLARIS:
                     case LINUX:
                     case OTHER:
                         defaults = null;
                    //no defaults for linux or other systems
                }
                
                if(defaults != null)
                    for(String[] dfs: defaults)
                        mappingTable.put(dfs[0],dfs[1]);
            }
        } catch (BackingStoreException bse){
            logger.warning("While saving language mapping:"+bse);
        }
        
    }
    
    public String getDefaultCommand(){
        return defaultCommand;
    }
    
    public void setDefaultCommand(String defaultCommand){
        this.defaultCommand = defaultCommand;
    }

    private OperatingSystem opereratingSystem(){
        String os = System.getProperty("os.name").toLowerCase();
        
        if(os.startsWith("windows"))
            return OperatingSystem.WINDOWS;
        else if(os.startsWith("sunos"))
            return OperatingSystem.SOLARIS;
        else if(os.indexOf("linux") != -1)
            return OperatingSystem.LINUX;
        else        
            return OperatingSystem.OTHER;
    }
}
