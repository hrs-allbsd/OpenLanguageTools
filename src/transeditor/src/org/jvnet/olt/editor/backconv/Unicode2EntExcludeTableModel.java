/*
 * Unicode2EntExcludeTableModel.java
 *
 * Created on August 31, 2005, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.backconv;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableModel;

/**
 *
 * @author boris
 */
public class Unicode2EntExcludeTableModel implements TableModel{
    private HashSet tableListeners = new HashSet();
    private String[] columnNames = new String[]{"Unicode char","Unicode code","SGML Entity","Exclude"};
    
    private List items;
    private BitSet exclusions;
    private Map targetMap;
    
    /** Creates a new instance of Unicode2EntExcludeTableModel */
    public Unicode2EntExcludeTableModel(List items,Map targetMap) {
        if(items == null)
            items = Collections.EMPTY_LIST;
        this.items = new ArrayList(items);
        
        this.targetMap = targetMap;
        
        if(this.targetMap == null)
            this.targetMap = new HashMap();

        this.exclusions = new BitSet(items.size());
    }

    public void removeTableModelListener(javax.swing.event.TableModelListener l) {
        tableListeners.remove(l);
    }

    public void addTableModelListener(javax.swing.event.TableModelListener l) {
        tableListeners.add(l);
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
        return columnIndex != 3 ? String.class : Boolean.class;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex < 3 )
            return;
        
        Unicode2Ent ent = (Unicode2Ent)items.get(rowIndex);
        
        boolean val = ((Boolean)aValue).booleanValue();
        exclusions.set(rowIndex,val);
        
        if(val)
            targetMap.put(ent.getChr(),ent.getEntity());
        else
            targetMap.remove(ent.getChr());
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }

    public int getRowCount() {
        return items.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex < 3){
            Unicode2Ent ent = (Unicode2Ent)items.get(rowIndex);
            switch (columnIndex){
                case 0: 
                    return ent.getChr().toString();
                case 1:
                    return ent.getHex();
                case 2:
                    return "&"+ent.getEntity()+";";
                default:
                    return null;
            }
            
        }
        else{
            return new Boolean(exclusions.get(rowIndex));
        }
    }   
    
    public static Unicode2EntExcludeTableModel modelFromChar2StringMap(Map m,Map targetMap){
        List x = new LinkedList();
        for(Iterator i = m.entrySet().iterator();i.hasNext();){
            Map.Entry entry = (Map.Entry)i.next();
            
            Character c = (Character)entry.getKey();
            String entity = (String)entry.getValue();
            
            x.add(new Unicode2Ent(c,entity));
        }
        
        
        return new Unicode2EntExcludeTableModel(x,targetMap);
    }
}
