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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
    private JTable unicodeEntTable;
    private JLabel notificationLabel;
    private Unicode2EntTableModel tabModel;
    
    private BackConversionOptions model;
    /** Creates a new instance of SGMLOptionsPanel */
    public SGMLOptionsPanel(BackConversionOptions model) {
        this.model = model;
        
        init();
    }
    
    private void init() {
        setLayout(new BorderLayout());
        
        writeStatusToSGML = new JCheckBox("Write translation status into SGML files");
        writeStatusToSGML.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox)e.getSource();
                model.setWriteTransStatusToSGML(cb.isSelected());
            }
            
        });
                
                
        JPanel uniPanel = new JPanel(new BorderLayout());
        uniPanel.setBorder(BorderFactory.createTitledBorder("Unicode entity translation table"));
        
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
                    
                    notificationLabel.setText("This mapping already exists");
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
                    JOptionPane.showMessageDialog(SGMLOptionsPanel.this,"This is not a valid hexadecimal value","Invalid input",JOptionPane.ERROR_MESSAGE);
                if(!rv2)
                    notificationLabel.setText("This mapping already exists");
                
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

        final JButton addMappingButton = new JButton("Add mapping");
        final JButton removeMappigButton = new JButton("Remove mapping");
        final JButton showDefaultsButton = new JButton("Default mapping...");
        
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
                JButton closeButton = new JButton("Close");

                closeButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                    }
                });
                
                buttonsPanel.add(closeButton);
                
                frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
                frame.getContentPane().add(new JLabel("Default char to entity mapping"),BorderLayout.NORTH);
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
        
        add(writeStatusToSGML,BorderLayout.NORTH);
        add(uniPanel);
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
        return tabModel.getChrStringMap();
    }
    
    public Map getHex2EntityMap(){
        return tabModel.getHexStringMap();
    }
}
