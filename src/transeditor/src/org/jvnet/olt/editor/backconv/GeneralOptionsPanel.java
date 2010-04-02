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
import org.jvnet.olt.editor.util.Bundle;
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
    
    private Bundle bundle = Bundle.getBundle(GeneralOptionsPanel.class.getName());

    /** Creates a new instance of GeneralOptionsPanel */
    public GeneralOptionsPanel(BackConversionOptions model) {
        super();
        
        this.model = model;
        
        init();
    }
    
    private void init(){
        createTmxCheckBox = new JCheckBox(bundle.getString("Create_TMX_file"),model.isGenerateTMX());
        createTmxCheckBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox cb = (JCheckBox)e.getSource();
                
                model.setGenerateTMX(cb.isSelected());
            }
        });
        
        overwriteFilesCheckBox = new JCheckBox(bundle.getString("Overwrite_existing_files"),model.isOverwriteFiles());
        overwriteFilesCheckBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox cb = (JCheckBox)e.getSource();
                
                model.setOverwriteFiles(cb.isSelected());
            }
        });

        preferOrigNameCheckBox = new JCheckBox(bundle.getString("Use_file_name_stored_in_.xlz_file"),model.isPreferXLZNames());
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
