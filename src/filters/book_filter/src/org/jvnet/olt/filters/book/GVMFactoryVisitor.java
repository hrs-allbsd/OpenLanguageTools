
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * GVMFactoryVisitor.java
 *
 * Created on May 1, 2003, 3:30 PM
 */

package org.jvnet.olt.filters.book;
import org.jvnet.olt.parsers.tagged.*;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.sgml.EntityManager;
import com.wutka.dtd.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
/**
 *
 * @author  timf
 */
public class GVMFactoryVisitor implements TaggedMarkupVisitor, TaggedMarkupNodeConstants {
    private GlobalVariableManager gvm;
    private String dir;
    private NodeTypeConverter converter;
    
    /**
     * This constructor allows us to append onto an existing
     * gvm, if we specify one
     */
    public GVMFactoryVisitor(GlobalVariableManager gvm, String dir, NodeTypeConverter converter) {
        if (gvm == null){
            this.gvm = new EntityManager();
        } else {
            this.gvm = gvm;
        }
        this.dir = dir;
        this.converter = converter;
    }
    
    public Object visit(TaggedMarkupNode node, Object data) {
        
        switch(converter.convert(node.getType())){
            case ENTITY_DECL:
                parseEntity(node.getNodeData());
                break;
            case INT_ENTITY:
                addEntitiesFrom(node.getNodeData());
                break;
        }
        return null;
    }
    
    public void parseEntity(String s){
        try {
            StringReader reader = new StringReader(s);
            DTDParser parser = new DTDParser(reader);
            DTD dtd = parser.parse();
            
            Hashtable entities = dtd.entities;
            Hashtable externalDTDs = dtd.externalDTDs;
            
            Enumeration e = dtd.entities.elements();
            
            while (e.hasMoreElements()){
                DTDEntity entity = (DTDEntity)e.nextElement();
                String name = "";
                String value = "";
                String type = "";
                if (entity.name != null){
                    name = entity.name;
                }
                
                if (entity.isParsed()){
                    type="PARAMETER";
                } else {
                    type = "INTERNAL";
                }
                
                if (entity.value != null){
                    value= entity.value;
                }
                
                if (entity.externalID != null)  {
                    if (entity.externalID instanceof DTDSystem) {
                        //System.out.println("    System: "+
                        //entity.externalID.system);
                        value = entity.externalID.system;
                    }
                    else {
                        DTDPublic pub = (DTDPublic) entity.externalID;
                        
                        //System.out.println("    Public: "+
                        //pub.pub+" "+pub.system);
                        // not sure what we should do with public entity values.
                        // nothing for the time being.
                    }
                }
                
                if (entity.externalID instanceof DTDSystem) {
                    //System.out.println("    System: "+
                    //entity.externalID.system);
                    value=entity.externalID.system;
                    type = "SYSTEM";
                }
                else {
                    DTDPublic pub = (DTDPublic) entity.externalID;
                    
                    //System.out.println("    Public: "+
                    //pub.pub+" "+pub.system);
                }
                
                
                if (entity.ndata != null){
                    //System.out.println("    NDATA "+entity.ndata);
                }
                //System.out.println(name +" = "+value);
                gvm.setVariable(name,value,type);
            }
        } catch (Exception e){
            //System.out.println(e.getMessage());
        }
        
        
        
    }
    
    public void addEntitiesFrom(String systemFileEntity) {
        try {
            if (systemFileEntity.charAt(0) == '%' && systemFileEntity.charAt(systemFileEntity.length() -1) == ';')
                systemFileEntity = systemFileEntity.substring(1,systemFileEntity.length() -1);
            //System.out.println("################ adding entities from " + gvm.resolveVariable(systemFileEntity) + " ######");
            Book book = new Book(new FileReader(this.dir+"/"+gvm.resolveVariable(systemFileEntity,"SYSTEM")),
            dir,this.gvm, true);
            this.gvm = book.getGlobalVariableManager();
            //System.out.println("############### finished adding entities from included DTD ########");
        } catch (Exception e ){
            System.out.println("caught exception trying to resolve stuff from " + systemFileEntity+ " : " + e.getMessage());
        }
    }
    
    
    public GlobalVariableManager getGlobalVariableManager(){
        return this.gvm;
    }
    
}
