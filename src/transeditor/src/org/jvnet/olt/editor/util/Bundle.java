/*
 * Bundle.java
 *
 * Created on June 23, 2006, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author boris
 */
public class Bundle {

    private ResourceBundle rb;
    private String name;

    private static final Logger logger = Logger.getLogger(Bundle.class.getName());

    private static final Bundle EMPTY_BUNDLE = new EmptyBundle();
    
 
    static class EmptyBundle extends Bundle {
        EmptyBundle(){
            super("EMPTY",null);
        }

        public String getString(String key) {
            logger.warning("USING EMPTY BUNDLE FOR KEY:"+key);
            return key;
        }
        
        
    }    
    
    /** Creates a new instance of Bundle */
    private Bundle(String name, ResourceBundle rb) {
        this.rb = rb;
        this.name = name;
    }
    
    public static Bundle getBundle(Class clz){
        return lookupBundle(clz.getName(),Locale.getDefault(),Bundle.class.getClassLoader());
    }
    public static Bundle getBundle(Class clz,Locale l){
        return lookupBundle(clz.getName(),l,Bundle.class.getClassLoader());
    }
    
    
    private static Bundle lookupBundle(String name,Locale locale,ClassLoader cl) throws MissingResourceException {

        Bundle b = null;
        try{
            ResourceBundle rb = ResourceBundle.getBundle(name,locale,cl);
            b = new Bundle(name, rb);
        }
        catch (MissingResourceException mre){
            logger.warning("unable to find resource bundle for name:"+name+" locale:"+locale);
            logger.warning("Will return pass through bundle");
            
            b = Bundle.EMPTY_BUNDLE;
        }
        
        return b;
    }
    
    public String getString(String key){
        String rv = key;
        try{
            rv = rb.getString(key);
        }
        catch (MissingResourceException mre){
            logger.warning("resource missing in bundle :"+name+" for key: "+key);            
        }

        return rv;
    }

    public static Bundle getBundle(String name) {
        return lookupBundle(name,Locale.getDefault(),Bundle.class.getClassLoader());
    }
}
