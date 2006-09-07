/*
 * GeneralOptionsPanel.java
 *
 * Created on August 26, 2005, 2:50 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jvnet.olt.editor.backconv;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author boris
 */
public class GeneralOptionsPanel extends JPanel{
    private BackConversionOptions model;
    
    private JCheckBox createTmxCheckBox;
    private JCheckBox overwriteFilesCheckBox;
    private JCheckBox preferOrigNameCheckBox;
    /** Creates a new instance of GeneralOptionsPanel */
    public GeneralOptionsPanel(BackConversionOptions model) {
        super();
        
        this.model = model;
        
        init();
    }
    
    private void init(){
        createTmxCheckBox = new JCheckBox("Create TMX file",model.isGenerateTMX());
        createTmxCheckBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox cb = (JCheckBox)e.getSource();
                
                model.setGenerateTMX(cb.isSelected());
            }
        });
        
        overwriteFilesCheckBox = new JCheckBox("Overwrite existing files",model.isOverwriteFiles());
        overwriteFilesCheckBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox cb = (JCheckBox)e.getSource();
                
                model.setOverwriteFiles(cb.isSelected());
            }
        });

        preferOrigNameCheckBox = new JCheckBox("Use file name stored in .xlz file",model.isPreferXLZNames());
        preferOrigNameCheckBox .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox cb = (JCheckBox)e.getSource();
                
                model.setPreferXLZNames(cb.isSelected());
            }
        });
     
        setLayout(new GridLayout(3,1,10,0));
        
        add(createTmxCheckBox);
        add(overwriteFilesCheckBox);
        add(preferOrigNameCheckBox);
    }
    
}
