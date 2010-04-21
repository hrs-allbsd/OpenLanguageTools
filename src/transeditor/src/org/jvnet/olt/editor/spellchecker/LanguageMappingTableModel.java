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
 * LanguageMappingTableModel.java
 *
 * Created on December 14, 2006, 3:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jvnet.olt.editor.spellchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.jvnet.olt.editor.util.Languages;
import org.jvnet.olt.editor.util.Language;

/**
 *
 * @author boris
 */
public class LanguageMappingTableModel extends AbstractTableModel{
    private static final String[] columns = new String[]{"Project language","Spellchecker language"};
    
    class Mapping {
        Language lang;
        String spellLang;
        
        Mapping(Language lang,String spellLang){
            this.lang = lang;
            this.spellLang = spellLang;
        }
    }
    
    private Map<String,String> map = new HashMap<String,String>();
    
    private List<Mapping> mappings = new ArrayList<Mapping>();
    /** Creates a new instance of LanguageMappingTableModel */
    public LanguageMappingTableModel(Map<String,String> map) {     
        if(map != null)
            this.map = map;
        
        for(Map.Entry<String,String> e: map.entrySet()){
            String key = e.getKey();
            
            Language lang = new Language (key);
            if(lang !=null){                
                mappings.add(new Mapping(lang,e.getValue()));
            }
        }
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex > mappings.size())
            return null;
        Mapping m = mappings.get(rowIndex);
        
        if(columnIndex == 0)
            return m.lang;
        else if(columnIndex == 1)
            return m.spellLang;
        else
            return null;
            
    }
    
    public int getRowCount() {
        return mappings.size();
    }
    
    public int getColumnCount() {
        return 2;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex > 2 )
            return ;
        
        Mapping m = null;
        if(rowIndex < mappings.size()){
            m = mappings.get(rowIndex);
        }
            
        if(columnIndex == 0){
            m.lang = (Language)aValue;
            fireTableRowsUpdated(rowIndex,rowIndex);
        }
        else if(columnIndex == 1){
            m.spellLang = (String)aValue;
            fireTableRowsUpdated(rowIndex,rowIndex);                
        }
        map.put(m.lang.getShortCode(),m.spellLang);
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0)
            return Language.class;
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    public Map<String,String> getMappingTable(){
        return map;
    }
    
    public void removeRows(int[] indexes){
        int size = mappings.size();
        
        for(int i: indexes){
            if(i < size){
                Mapping m = mappings.set(i,null);
                map.remove(m.lang.getShortCode());
            }
        }

        int row = 0;
        for (Iterator i = mappings.iterator(); i.hasNext();) {
            Mapping mapping = (Mapping) i.next();
            if(mapping == null){
                i.remove();
                
                fireTableRowsDeleted(row,row);
                row++;
            }
            
        }
        
    }

    void addRow(Language lng,String spellLang) {
        mappings.add(new Mapping(lng,spellLang));
        
        int size = mappings.size()-1;
        fireTableRowsInserted(size,size);
    }
}
