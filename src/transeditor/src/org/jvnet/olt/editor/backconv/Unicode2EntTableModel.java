package org.jvnet.olt.editor.backconv;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class Unicode2EntTableModel implements TableModel {
    
    Logger logger = Logger.getLogger(Unicode2EntTableModel.class.getName());
    
    List items = new ArrayList();
    
    String columnNames[] = new String[] {"Unicode char","Unicode value","SGML entity"};
    
    Set tableListeners = new HashSet();
    
    Map targetMap;
    
    public Unicode2EntTableModel(List items,Map targetMap){
        logger.finest("items:"+items);  
        
        this.items.addAll(items);
        this.targetMap = targetMap;
        if(targetMap == null)
            this.targetMap = new HashMap();
                
    }
    
    public int getRowCount()    {
        return items.size();
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public boolean isCellEditable(int row, int column)    {
        return true;
    }
    
    public String getColumnName(int column)    {
        if(column < columnNames.length)
            return columnNames[column];
        
        return columnNames[column];
    }
    
    public Object getValueAt(int row, int column) {
        if(row > items.size() || column > columnNames.length)
            return "???";
        Unicode2Ent ent = (Unicode2Ent)items.get(row);
        switch (column) {
            case 0:
                
                return new String(new char[] {ent.getChr().charValue()});
            case 1:
                
                return ent.getHex();
            case 2:
                
                return ent.getEntity();
            default:
                
                return "???";
        }
    }
    
    public int getColumnCount()    {
        return columnNames.length;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex > items.size() || columnIndex > 2)
            return;
        Unicode2Ent ent = (Unicode2Ent)items.get(rowIndex);
        
        logger.finest("Set value:"+aValue+" "+aValue.getClass().getName());
        switch (columnIndex) {
            case 0:
                
                ent.setChr(new Character(((String) aValue).charAt(0)));
                
                fireModelChange(rowIndex,1);
                
                
                break;
            case 1:
                
                ent.setHex((String) aValue);
                
                fireModelChange(rowIndex,0);
                
                break;
            case 2:
                
                ent.setEntity((String) aValue);
                
                break;
            default:
                
                logger.warning("Strange column:"+columnIndex);
        }
        
        targetMap.put(ent.getChr(),ent.getEntity());
    }
    
    public void addTableModelListener(TableModelListener l)    {
        tableListeners.add(l);
    }
    
    public void removeTableModelListener(TableModelListener l)    {
        tableListeners.remove(l);
    }
    
    public boolean isCellEditable(EventObject anEvent)    {
        return true;
    }
    
    void fireModelChange() {
        TableModelEvent e = new TableModelEvent(this);
        for (Iterator i = tableListeners.iterator(); i.hasNext(); ) {
            TableModelListener tml = (TableModelListener)i.next();
            
            tml.tableChanged(e);
        }
    }
    
    void fireModelChange(int row, int col) {
        TableModelEvent e = new TableModelEvent(this,row,row,col);
        for (Iterator i = tableListeners.iterator(); i.hasNext(); ) {
            TableModelListener tml = (TableModelListener)i.next();
            
            tml.tableChanged(e);
        }
    }
    
    void addNewRow() {
        items.add(new Unicode2Ent('?', "&entity"));
        fireModelChange();
    }
    
    void removeSelectedItems(int[] selection) {
        for (int i = 0; selection != null && i < selection.length; i++){
            Unicode2Ent ent = (Unicode2Ent)items.get(i);
            targetMap.remove(ent.getChr());
            items.set(selection[i],null);
        }
        
        for (Iterator i = items.iterator(); i.hasNext(); )        {
            if(i.next() == null)
                i.remove();
        }
        
        fireModelChange();
    }
    
    /* find mapping by character
     *
     */
    boolean existsMapping(char c,int row) {
        for(Iterator i = items.iterator();i.hasNext();){
            Unicode2Ent ent = (Unicode2Ent)i.next();
            if(row-- == 0)
                continue;
            
            if(ent.getChr().charValue() == c)
                return true;
        }
        return false;
    }
    
    /** find mapping by hexa value
     *
     */
    boolean existsMapping(String hexValue, int row) {
        hexValue = Unicode2Ent.hexNormalize(hexValue);
        
        for(Iterator i = items.iterator();i.hasNext();){
            Unicode2Ent ent = (Unicode2Ent)i.next();
            if(row-- == 0)
                continue;
            if(ent.getHex().equals(hexValue))
                return true;
        }
        return false;
    }
  
    public Map getHexStringMap(){
        Map map = new HashMap();
        for(Iterator i = items.iterator();i.hasNext();){
            Unicode2Ent ent = (Unicode2Ent)i.next();
            
            map.put(ent.getHex(),ent.getEntity());
        }
        return map;
    }

    public Map getChrStringMap(){
        Map map = new HashMap();
        for(Iterator i = items.iterator();i.hasNext();){
            Unicode2Ent ent = (Unicode2Ent)i.next();
            
            map.put(ent.getChr(),ent.getEntity());
        }
        return map;
    }
    
/*    
    public static Unicode2EntTableModel modelFromChar2StringMap(Map m){
        Unicode2EntTableModel  model = new Unicode2EntTableModel();
    
        for(Iterator i = m.entrySet().iterator();i.hasNext();){
            Map.Entry entry = (Map.Entry)i.next();
            
            Character c = (Character)entry.getKey();
            String entity = (String)entry.getValue();
            
            model.items.add(new Unicode2Ent(c,entity));
        }
        return model;
    }
*/
    public static Unicode2EntTableModel  modelFromHex2StringMap(Map hex2entMap,Map targetMap) throws IllegalArgumentException {
        List xx = new LinkedList();
        
        for(Iterator i = hex2entMap.entrySet().iterator();i.hasNext();){
            Map.Entry entry = (Map.Entry)i.next();
            
            String hex = (String)entry.getKey();
            
            String entity = (String)entry.getValue();
            
            try{
                int x = Integer.parseInt(hex,16);
                xx.add(new Unicode2Ent((char)x,entity));
            }
            catch (NumberFormatException nfe){
                throw new IllegalArgumentException("parsing of hex string failed");
            }
        }
        
        Unicode2EntTableModel model = new Unicode2EntTableModel (xx,targetMap);
        
        return model;
    }
}
