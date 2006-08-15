/*
 * SGMLOptionsPanel.java
 *
 * Created on August 26, 2005, 2:50 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jvnet.olt.editor.backconv;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.jvnet.olt.editor.util.Bundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.PlainDocument;
import org.jvnet.olt.xliff_back_converter.format.sgml.SgmlUnicodeReverseImpl;


/**
 *
 * @author boris
 */
public class SGMLOptionsPanel extends JPanel{
    
    private static final Logger logger = Logger.getLogger(SGMLOptionsPanel.class.getName());
    
    private JCheckBox writeStatusToSGML;
    private JCheckBox useCustomMapping;
    private JTable unicodeEntTable;
    private JLabel notificationLabel;
    private Unicode2EntTableModel tabModel;
    private boolean doUseCustomMapping;
    
    private Bundle bundle = Bundle.getBundle(SGMLOptionsPanel.class.getName());
    
    private BackConversionOptions model;
    /** Creates a new instance of SGMLOptionsPanel */
    public SGMLOptionsPanel(BackConversionOptions model) {
        this.model = model;
        
        init();
    }
    
    class EnablePropertyChangeDispatcher{
        private Set receivers = new HashSet();
        
        public EnablePropertyChangeDispatcher(){
        }
        
        public void setEnabled(boolean enabled) {
            for(Iterator i = receivers.iterator();i.hasNext();){
                JComponent comp = (JComponent )i.next();
                comp.setEnabled(enabled);
            }
        }
        
        public void addReceiver(JComponent comp){
            receivers.add(comp);
        }
        
    }
    
    private void init() {
        setLayout(new BorderLayout());
        
        final EnablePropertyChangeDispatcher disp = new EnablePropertyChangeDispatcher();
        
        writeStatusToSGML = new JCheckBox(bundle.getString("Write_translation_status_into_SGML_files"));
        writeStatusToSGML.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox)e.getSource();
                model.setWriteTransStatusToSGML(cb.isSelected());
            }
            
        });
        
        useCustomMapping = new JCheckBox(bundle.getString("Use_custom_mapping"));
        useCustomMapping.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox)e.getSource();
                disp.setEnabled(cb.isSelected());
                
                doUseCustomMapping = cb.isSelected();
            }
            
        });
        
        JPanel uniPanel = new JPanel(new BorderLayout());
        uniPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("Unicode_entity_translation_table")));
        notificationLabel = new JLabel();
        notificationLabel.setForeground(Color.RED);
        
        
        tabModel = Unicode2EntTableModel.modelFromHex2StringMap(model.getUnicode2entityIncludeMap(),model.getUnicode2entityIncludeMap());
        unicodeEntTable = new JTable(tabModel);
        
        unicodeEntTable.getColumnModel().getColumn(0).setCellEditor(new ValidatingCellEditor(new XTextField(),
                new StringValidator(){
            public boolean validate(String value){
                boolean exists =  value.length() > 0 ? tabModel.existsMapping(value.charAt(0),unicodeEntTable.getEditingRow()) : false;
                
                if(exists){
                    Toolkit.getDefaultToolkit().beep();
                    
                    notificationLabel.setText(bundle.getString("This_mapping_already_exists"));
                } else{
                    notificationLabel.setText("");
                }
                return !exists;
            };
            
        }));
        
        unicodeEntTable.getColumnModel().getColumn(1).setCellEditor(
                new ValidatingCellEditor(new JTextField(),new StringValidator(){
            
            public boolean validate(String value){
                boolean rv = Unicode2Ent.validHexValue(value);
                boolean rv2 = ! tabModel.existsMapping(value,unicodeEntTable.getEditingRow());
                
                if(!rv)
                    JOptionPane.showMessageDialog(SGMLOptionsPanel.this,bundle.getString("This_is_not_a_valid_hexadecimal_value"),bundle.getString("Invalid_input"),JOptionPane.ERROR_MESSAGE);
                if(!rv2)
                    notificationLabel.setText(bundle.getString("This_mapping_already_exists"));
                
                int row = unicodeEntTable.getEditingRow();
                int col = unicodeEntTable.getEditingColumn();
                tabModel.getValueAt(row,col);
                
                if(rv && rv2)
                    notificationLabel.setText("");
                return rv && rv2;
            }
        }));
        
        
        unicodeEntTable.setDefaultEditor(String.class,new DefaultCellEditor( new JTextField()));
        
        JScrollPane scrollPane = new JScrollPane(unicodeEntTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setPreferredSize(new Dimension(300,150));
        
        final JButton addMappingButton = new JButton(bundle.getString("Add_mapping"));
        final JButton removeMappigButton = new JButton(bundle.getString("Remove_mapping"));
        final JButton showDefaultsButton = new JButton(bundle.getString("Default_mapping..."));
        
        removeMappigButton.setEnabled(unicodeEntTable.getSelectedRow() != -1);
        
        showDefaultsButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SortedMap map = new TreeMap();
                map.putAll(new SgmlUnicodeReverseImpl().getMap());
                
                Unicode2EntExcludeTableModel model = Unicode2EntExcludeTableModel.modelFromChar2StringMap(map,SGMLOptionsPanel.this.model.getUnicode2entityExcludeMap());
                final JDialog frame = new JDialog((JFrame)null,true);
                frame.setLocationRelativeTo(SGMLOptionsPanel.this);
                frame.setSize(400,300);
                frame.getContentPane().setLayout(new BorderLayout());
                
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                
                JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton closeButton = new JButton(bundle.getString("Close"));
                
                closeButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                    }
                });
                
                buttonsPanel.add(closeButton);
                
                frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
                frame.getContentPane().add(new JLabel(bundle.getString("Default_char_to_entity_mapping")),BorderLayout.NORTH);
                frame.getContentPane().add(buttonsPanel,BorderLayout.SOUTH);
                
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                frame.setVisible(true);
            }
        });
        
        unicodeEntTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel sm = (ListSelectionModel)e.getSource();
                removeMappigButton.setEnabled(! (sm.isSelectionEmpty()));
            }
        });
        
        addMappingButton.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e) {
                tabModel.addNewRow();
            }
        });
        
        removeMappigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selection = unicodeEntTable.getSelectedRows();
                
                tabModel.removeSelectedItems(selection);
            }
        });
        
        JPanel buttonsPanel = new JPanel();
        LayoutManager lay = new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(lay);
        
        buttonsPanel.add(addMappingButton);
        buttonsPanel.add(removeMappigButton);
        buttonsPanel.add(showDefaultsButton);
        
        uniPanel.add(scrollPane,BorderLayout.CENTER);
        uniPanel.add(buttonsPanel,BorderLayout.EAST);
        uniPanel.add(notificationLabel,BorderLayout.SOUTH);
        
        JPanel dummy = new JPanel();
        LayoutManager lm = new BoxLayout(dummy, BoxLayout.Y_AXIS);
        dummy.setLayout(lm);
        
        dummy.add(writeStatusToSGML);
        dummy.add(useCustomMapping);
        
        //add(writeStatusToSGML,BorderLayout.NORTH);
        add(dummy,BorderLayout.NORTH);
        add(uniPanel,BorderLayout.CENTER);
        
        disp.addReceiver(addMappingButton);
        disp.addReceiver(removeMappigButton);
        disp.addReceiver(showDefaultsButton);
        disp.addReceiver(unicodeEntTable);
        disp.setEnabled(false);
    }
    
    interface StringValidator {
        public boolean validate(String value);
    }
    
    
    //this class adapts the DefaultCellEditor to validation. If passed StringValidator
    //reutrns true, the input is valid and editing is aborted
    class ValidatingCellEditor extends DefaultCellEditor{
        StringValidator v;
        ValidatingCellEditor(JTextField textField,StringValidator v){
            super(textField);
            
            this.v = v;
        }
        
        
        
        public boolean stopCellEditing() {
            String value = (String)super.getCellEditorValue();
            
            if(v.validate(value))
                return super.stopCellEditing();
            else{
                return false;
            }
            
        }
        
        
    }
    
    //This field overloads createDefaultModel and supplies it's own model.
    //The model limits length of the string to 1 char
    class XTextField extends JTextField {
        protected javax.swing.text.Document createDefaultModel() {
            return new XDocument(getText());
        }
        
        class XDocument extends PlainDocument {
            Logger logger = Logger.getLogger(XDocument.class.getName());
            String x;
            
            XDocument(String x){
                this.x = x;
            }
            
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                logger.finest("insertString "+offs+" "+str);
                
                if(offs > 0)
                    return;
                if(str.length() > 0)
                    x = new String(new char[]{str.charAt(str.length() -1)});
                else
                    x = "";
                
                super.insertString(offs,x,a);
            }
            
            public String getText(int offset, int length) throws javax.swing.text.BadLocationException {
                logger.finest("getText"+offset+" "+length);
                if(offset > 0)
                    return "";
                
                return x.substring(0,length);
            }
            
            public int getLength() {
                int len = x == null ? 0 : x.length();
                logger.finest("getLength"+len);
                return len;
            }
        }
    }
    
    public Map getUnicode2EntityMap(){
        return doUseCustomMapping ? tabModel.getChrStringMap() : Collections.EMPTY_MAP;
    }
    
    public Map getHex2EntityMap(){
        return doUseCustomMapping ? tabModel.getHexStringMap() : Collections.EMPTY_MAP;
    }
}
