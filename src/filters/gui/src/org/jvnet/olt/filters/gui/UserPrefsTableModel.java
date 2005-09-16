/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.filters.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author timf
 */
public class UserPrefsTableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    private String[] columnNames;
    
    private Map preferences = null;
    private XliffFilterGUI gui = null;
    private List model;
    private Set listeners = new HashSet();
    
    class Tuple{
        Object key;
        Object value;
        
        Tuple(Object key,Object value){
            this.key = key;
            this.value = value;
        }
        
    }
    /** Creates a new instance of UserPrefsTableModel */
    public UserPrefsTableModel(Map preferences, XliffFilterGUI gui) {
        
        columnNames = new String[] {
            java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages").getString("Key"),
            java.util.ResourceBundle.getBundle("org/jvnet/olt/filters/gui/XliffFilterGUIMessages").getString("Value")};
        
        this.model = makeModel(preferences);
        this.gui = gui;
    }
    
    private List makeModel(Map m){
        ArrayList l = new ArrayList();
        for(Iterator i = m.entrySet().iterator();i.hasNext();){
            Map.Entry e = (Map.Entry)i.next();
            l.add(new Tuple(e.getKey(),e.getValue()));
        }
        
        return l;
    }
    
  /*  public void addTableModelListener(javax.swing.event.TableModelListener tableModelListener) {
        listeners.add(tableModelListener);
    }
   */
    public Class getColumnClass(int param) {
        return String.class;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String getColumnName(int param) {
        return columnNames[param];
    }
    
    public int getRowCount() {
        return model.size();
    }
    
    public Object getValueAt(int row, int column) {
        Tuple t = (Tuple)model.get(row);
        
        return column == 0 ? t.key : t.value;
    }
    
    public boolean isCellEditable(int param, int param1) {
        return true;
    }
    
    /*public void removeTableModelListener(javax.swing.event.TableModelListener tableModelListener) {
        listeners.add(tableModelListener);
    }*/
    
    public void setValueAt(Object obj, int row, int column) {
        Tuple t = (Tuple)model.get(row);
        if(column == 0){
            t.key = obj;
        } else {
            t.value = obj;
        }
        
        this.gui.setAttribute(t.key, t.value);
        //fireTableRowsUpdated(row, column);
    }
    
}
