/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.lang.*;
import org.jvnet.olt.editor.util.Bundle;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;


class JAutoSaveDlg extends JDialog {
    private static final Logger logger = Logger.getLogger(JAutoSaveDlg.class.getName());
    
    private JComboBox jCbInterval = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private JCheckBox jCkEnable = null;
    
    private boolean canceled;
    private boolean bEnable;
    private int interval;

    private Bundle bundle = Bundle.getBundle(JAutoSaveDlg.class.getName());
    
    public JAutoSaveDlg(JFrame owner, boolean enabled, int interval) {
        super(owner,"", true);
        setTitle(bundle.getString("Autosave_Property_Setting"));
        this.bEnable = enabled;

        this.interval = interval;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        
        setLayout(new BorderLayout());
        JPanel bordrPanel = new JPanel(new BorderLayout());
        bordrPanel.setBorder(new TitledBorder(new EtchedBorder(),"Autosave Options"));
        getContentPane().add(bordrPanel,BorderLayout.CENTER);
        
        JButton jBtnOK = new JButton(org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/translation/JAutoSaveDlg").getString("OK"));
        jBtnOK.setMnemonic('O');
        jBtnOK.setSelected(true);
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnOK_actionPerformed(e);
                }
            });
        
        JButton jBtnCancel = new JButton(org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/translation/JAutoSaveDlg").getString("Cancel"));
        jBtnCancel.setMnemonic('C');
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jBtnCancel_actionPerformed(e);
                }
            });

        JPanel btnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnsPanel.add(jBtnOK);
        btnsPanel.add(jBtnCancel);
        
        bordrPanel.add(btnsPanel,BorderLayout.SOUTH);
                
        jCkEnable = new JCheckBox(org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/translation/JAutoSaveDlg").getString("Enable_Autosave_Function"));
        jCkEnable.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCkEnable_actionPerformed(e);
                }
            });
            
        bordrPanel.add(jCkEnable,BorderLayout.NORTH);
         
        JPanel cntrPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        jCbInterval = new JComboBox(new Object[]{1,2,3,4,5,6,7,8,9,10});
        jCbInterval.setEditable(true);
        
        jLabel1 = new JLabel(org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/translation/JAutoSaveDlg").getString("Autosave_file_every"));
        jLabel2 = new JLabel(org.jvnet.olt.editor.util.Bundle.getBundle("org/jvnet/olt/editor/translation/JAutoSaveDlg").getString("__Minutes"));
            
  
        cntrPanel.add(jLabel1);
        cntrPanel.add(jCbInterval);
        cntrPanel.add(jLabel2);
        
        bordrPanel.add(cntrPanel,BorderLayout.CENTER);
        
        if (bEnable) {
            jCkEnable.setSelected(true);
            jCbInterval.setEnabled(true);
            jLabel1.setEnabled(true);
        } else {
            jCbInterval.setEnabled(false);
            jLabel1.setEnabled(false);
        }

        if (this.interval > 10) {
            ((JTextField)jCbInterval.getEditor().getEditorComponent()).setText(Integer.toString(this.interval));
        } else {
            jCbInterval.setSelectedIndex(interval - 1);
        }
        
        this.setResizable(true);
        
        pack();
    }

    void jBtnOK_actionPerformed(ActionEvent e) {
        bEnable = this.jCkEnable.isSelected();

        String str = ((JTextField)jCbInterval.getEditor().getEditorComponent()).getText();

        if ((str == null) || (str.length() == 0)) {
            return;
        }

        if (!validateInput(str)) {
            return;
        }

        this.setVisible(false);
    }

    void jBtnCancel_actionPerformed(ActionEvent e) {
        this.setVisible(false);

        this.canceled = true;
    }

    void jCkEnable_actionPerformed(ActionEvent e) {
        bEnable = jCkEnable.isSelected();

        jCbInterval.setEnabled(bEnable);
        jLabel1.setEnabled(bEnable);
        jLabel2.setEnabled(bEnable);

        if (bEnable) {
            String str = ((JTextField)jCbInterval.getEditor().getEditorComponent()).getText();
            interval = Integer.parseInt(str);
        }
    }

    private boolean validateInput(String strInput) {
        try {
            int interval = Integer.parseInt(strInput);

            if ((interval <= 0) || (interval > 60)) {
                String strErrorMsg =bundle.getString("Please_enter_the_auto_save_interval_in_minutes_between_1_and_60.");
                JOptionPane.showMessageDialog(this, strErrorMsg,bundle.getString("Error_Input"), JOptionPane.OK_OPTION);

                return false;
            }

            this.interval = interval;

            return true;
        } catch (Exception ex) {
            String strErrorMsg =bundle.getString("The_autosave_interval_must_be_a_number_between_1_and_60.");
            JOptionPane.showMessageDialog(this, strErrorMsg,bundle.getString("Error_Input"), JOptionPane.OK_OPTION);

            return false;
        }
    }

    public int getInterval() {
        return interval;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public boolean isAutoSaveEnabled() {
        return bEnable;
    }
}
