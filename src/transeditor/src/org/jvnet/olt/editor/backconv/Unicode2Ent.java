package org.jvnet.olt.editor.backconv;

import java.util.Comparator;


class Unicode2Ent implements Comparable {
    
    
    
    private Character chr;
    
    private String entity;
    
    private String hex;
    
    public static class HexValueComparator implements Comparator {
        
        public int compare(Object o1, Object o2) {
            Unicode2Ent u1 = (Unicode2Ent)o1;
            Unicode2Ent u2 = (Unicode2Ent)o2;
            
            
            return String.CASE_INSENSITIVE_ORDER.compare(u1.getHex(),u2.getHex());
        }
    }
    
    Unicode2Ent(){
    }
    
    Unicode2Ent(Character c,String ent){
        setChr(c);
        setEntity(ent);        
    }
    
    Unicode2Ent(char c, String ent) {
        this(new Character(c),ent);
    }
    
    Character getChr()    {
        return chr;
    }
    
    String getEntity()    {
        return entity;
    }
    
    String getHex()    {
        return hex;
    }
    
    void setHex(String hx) throws IllegalArgumentException {
        hx = hexNormalize(hx);
        try {
            int i = Integer.parseInt(hx,16);
            chr = new Character((char) i);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(hx+" is not valid hexadecimal value");
        }
        
        hex = hx;
    }
    
    public static String hexNormalize(String hx)    {
        while(hx.length() < 4)
            hx = "0" + hx;
        return hx;
    }
    
    void setChr(Character ch) {
        this.chr = ch;
        String hx = Integer.toHexString((int)ch.charValue());
        setHex(hx);
    }
    
    void setEntity(String ent)    {
        this.entity = ent;
    }
    
    public static boolean validHexValue(String hexValue) {
        try {
            Integer.parseInt(hexValue, 16);
            return true;
        } catch (NumberFormatException nfe)        {
            return false;
        }
    }
    
    public int compareTo(Object o) {
        if(equals(o))
            return 1;
        Unicode2Ent m = (Unicode2Ent)o;
        int other = m.getChr().charValue();
        int us = getChr().charValue();
        
        if(us < other)
            return -1;
        if(us > other)
            return 1;
        
        return 0;
    }
    
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (obj instanceof Unicode2Ent) {
            Unicode2Ent ent = (Unicode2Ent)obj;
            return chr.equals(ent.getChr());
        }
        return false;
    }
    
    public int hashCode() {
        return Character.getNumericValue(chr.charValue());
    }

}
