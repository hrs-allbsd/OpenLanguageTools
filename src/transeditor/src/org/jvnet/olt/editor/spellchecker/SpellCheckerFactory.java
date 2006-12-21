/*
 * SpellCheckerFactory.java
 *
 * Created on November 30, 2006, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.jvnet.olt.editor.translation.Backend;

/**
 *
 * @author boris
 */
public class SpellCheckerFactory {
    private static final Logger logger = Logger.getLogger(SpellCheckerFactory.class.getName());
    
    static private SpellCheckerFactory instance = null;
    
    private static final String PROPS_KEY_CLASS="spellchecker.class";
    private static final String PROPS_KEY_NAME="spellchecker.name";
    private static final String PROPS_KEY_DISPLAY_NAME="spellchecker.display.name";
    
    Map<String,SpellCheckerHolder> nameClassMap = new HashMap<String,SpellCheckerHolder>();
    
    class SpellCheckerHolder {
        final Class<? extends SpellChecker> clazz;
        final String dispName;
        final Properties props;
        final Method createMethod;
        
        SpellCheckerHolder(Class clazz,Method createMethod,String dispName,Properties props){
            this.clazz = clazz;
            this.dispName = dispName;
            this.props = props;
            this.createMethod = createMethod;
        }
    }
    /** Creates a new instance of SpellCheckerFactory */
    protected  SpellCheckerFactory() {
    }
    
    public static SpellCheckerFactory instance(){
        synchronized(SpellCheckerFactory.class){
            if(instance == null){
                instance = new SpellCheckerFactory();
                
                try{
                    instance.findSpellCheckers();
                } catch (IOException ioe){
                    logger.warning("Exception occured while loading spellcheckers:"+ioe);
                    logger.warning("*NO SPELLCHECKERS WILL BE LOADED*");
                }
            }
        }
        return instance;
    }
    
    public static SpellChecker[] getAllSpellCheckers(Properties defaults){
        SpellCheckerFactory fa = instance();
        
        
        Set<SpellChecker> set = new LinkedHashSet<SpellChecker>();
        int j = 0;
        for(String checkerName: fa.nameClassMap.keySet()){
            try{
                set.add( fa.create(checkerName,defaults) );
            }
            catch (SpellCheckerCreationException scce){
                logger.warning("Unable to create spellchecker:"+checkerName);
            }
        }
        SpellChecker[] rv = set.toArray(new SpellChecker[]{});

        return rv;
    }
    
    static public void updateSpellCheckers(SpellChecker[] sc){
        SpellCheckerFactory i = instance();
        for(SpellChecker s: sc){
            if(s != null)
                i.releaseSpellChecker(s);
        }
    }
    
    public SpellChecker create(String name) throws SpellCheckerCreationException {
        return create(name,null);        
    }
    
    public SpellChecker create(String name,Properties defaults) throws SpellCheckerCreationException {
    
        if(nameClassMap.containsKey(name)){
            SpellCheckerHolder holder  = nameClassMap.get(name);
            
            Properties p = new Properties(holder.props);
            
            if(defaults != null)
                p.putAll(defaults);
            
            try{
                SpellChecker sp = (SpellChecker)holder.createMethod.invoke(null,p);
                
                sp.setFactory(this);                
                Preferences prefs = getPreferences(sp.getName());
                sp.loadConfig(prefs);
                
                return sp;
            } catch (IllegalAccessException iae){
                throw new SpellCheckerCreationException(iae);
                
            } catch (IllegalArgumentException iare){
                throw new SpellCheckerCreationException(iare);
            } 
            catch (InvocationTargetException ite){
                throw new SpellCheckerCreationException(ite);
            }
            
        }
        
        return null;
    }
    
    private void findSpellCheckers() throws IOException {
        try{
            Enumeration<URL> en = getClass().getClassLoader().getResources("spellchecker.properties");
            while(en.hasMoreElements()){
                URL u = en.nextElement();
                
                logger.info("Loading spellchecker from:"+u);
                
                InputStream is = null;
                try{
                    is = u.openStream();
                    
                    Properties p = new Properties();
                    p.load(is);
                    
                    logger.info("Loaded properties");
                    
                    String className = p.getProperty(PROPS_KEY_CLASS);
                    
                    if(className == null){
                        logger.warning("Spellchecker class not specified. This file can not be loaded");
                        continue;
                    }
                    String name = p.getProperty(PROPS_KEY_NAME,className);
                    String displayName = p.getProperty(PROPS_KEY_DISPLAY_NAME,className);
                    
                    try{
                        logger.info("Looking up class:"+className);
                        Class clazz = Class.forName(className);
                        logger.info("Class found");
                        
                        if(clazz.isAssignableFrom(SpellChecker.class)){
                            logger.warning("The class "+className+" does not implement SpellChecker interface; it will be not loaded");
                            continue;
                        }
                        
                        Method createMethod = clazz.getMethod("create",Properties.class);
                        if(! Modifier.isStatic(createMethod.getModifiers())){
                            logger.warning("Method 'static SpellChecker create(Properties p)' in class "+className+" could not be found; spellchecker will not be added");
                            continue;
                        }
                        
                        logger.info("Creating new spellchecker entry under name:"+name);
                        SpellCheckerHolder holder = new SpellCheckerHolder(clazz,createMethod,displayName,p);
                        nameClassMap.put(name,holder);
                    } catch (ClassNotFoundException cnfe){
                        logger.severe("Unable to find class:"+className);
                    } catch (NoSuchMethodException nsme){
                        logger.severe("Method 'static SpellChecker create(Properties p)' in class "+className+" could not be found; spellchecker will not be added");
                    }
                } catch (IOException ioe){
                    logger.severe("Unable to open stream from URL:"+u);
                } finally {
                    try{
                        if(is != null)
                            is.close();
                    } catch (IOException ioe){
                        ; //do nothing
                    }
                }
            }
            
        } finally {
            
        }
    }

    protected void releaseSpellChecker(SpellChecker checker) {
        try {
            Preferences prefs = getPreferences(checker.getName());

            checker.storeConfig(prefs);
            prefs.flush();
        }
        catch (BackingStoreException ex) {
            logger.warning("Exception thrown while saving spellchecker prefs:"+ex);
        };
    }
    
    protected Preferences getPreferences(String spellCheckerName){
        Preferences root =  Backend.instance().getConfig().getPreferencesRootForSpellCheckers();
        return root.node(spellCheckerName);
    }
    

}
