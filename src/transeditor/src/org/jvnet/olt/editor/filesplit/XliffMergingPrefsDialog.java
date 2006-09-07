
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * XliffMergingPrefsDialog.java
 *
 * Created on 26 September 2003, 18:12
 */

package org.jvnet.olt.editor.filesplit;

import org.jvnet.olt.editor.filefilters.XliffMergeDirectoryFileFilter;
import org.jvnet.olt.editor.filefilters.WritableDirectoryFileFilter;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author  jc73554
 */
public class XliffMergingPrefsDialog extends JDialog {
    
    private String defaultInputDir;
    
    private String defaultOutputDir;
    
    private String defaultSuffix;
    
    private String defaultFileName;
    
    /** Holds value of property mergePreferences. */
    private XliffMergingPrefs mergePreferences;
    
    
    /** Creates new form XliffMergingPrefsDialog */
    public XliffMergingPrefsDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        javax.swing.JLabel jLabel1;
        javax.swing.JLabel jLabel2;
        javax.swing.JLabel jLabel3;
        javax.swing.JLabel jLabel4;
        javax.swing.JPanel jPanel1;
        javax.swing.JPanel jPanel2;
        javax.swing.JPanel jPanel3;
        javax.swing.JPanel jPanel4;
        javax.swing.JPanel jPanel5;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        inputDirectoryTextField = new javax.swing.JTextField();
        browseInputDir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        outputDirectoryTextField = new javax.swing.JTextField();
        browseOutputDir = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        baseFileNameTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        suffixTextField = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("XLIFF Document Merging Preferences");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(7, 7, 7, 7)));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Input directory:    ");
        jPanel2.add(jLabel1);

        inputDirectoryTextField.setColumns(24);
        inputDirectoryTextField.setToolTipText("Directory to look in for input files");
        jPanel2.add(inputDirectoryTextField);

        browseInputDir.setText("...");
        browseInputDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseInputDirActionPerformed(evt);
            }
        });

        jPanel2.add(browseInputDir);

        jPanel1.add(jPanel2);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Output directory: ");
        jPanel3.add(jLabel2);

        outputDirectoryTextField.setColumns(24);
        outputDirectoryTextField.setToolTipText("The Directory to put the output file in");
        jPanel3.add(outputDirectoryTextField);

        browseOutputDir.setText("...");
        browseOutputDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseOutputDirActionPerformed(evt);
            }
        });

        jPanel3.add(browseOutputDir);

        jPanel1.add(jPanel3);

        jLabel3.setText("Base file name:");
        jPanel4.add(jLabel3);

        baseFileNameTextField.setColumns(30);
        baseFileNameTextField.setToolTipText("The name of the original XLIFF file");
        jPanel4.add(baseFileNameTextField);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel4.setText("Suffix: ");
        jPanel5.add(jLabel4);

        suffixTextField.setColumns(20);
        suffixTextField.setToolTipText("The suffix used in the split XLIFF files");
        jPanel5.add(suffixTextField);

        jPanel1.add(jPanel5);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 2, 7, 0));

        buttonPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EtchedBorder(), new javax.swing.border.EmptyBorder(new java.awt.Insets(7, 7, 7, 7))));
        okButton.setMnemonic('O');
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(okButton);

        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-451)/2, (screenSize.height-273)/2, 451, 273);
    }//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        handleCancelButton();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        handleOkButton();
    }//GEN-LAST:event_okButtonActionPerformed
    
    private void browseOutputDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseOutputDirActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        WritableDirectoryFileFilter dirFilter = new WritableDirectoryFileFilter();
        
        fileChooser.addChoosableFileFilter(dirFilter);
        fileChooser.setFileFilter(dirFilter);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(null);
        
        int iReturn = fileChooser.showDialog(this, "Select");
        if(iReturn == JFileChooser.APPROVE_OPTION) {
            outputDirectoryTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_browseOutputDirActionPerformed
    
    private void browseInputDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseInputDirActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        XliffMergeDirectoryFileFilter dirFilter = new XliffMergeDirectoryFileFilter();
        
        fileChooser.addChoosableFileFilter(dirFilter);
        fileChooser.setFileFilter(dirFilter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(null);
        
        int iReturn = fileChooser.showDialog(this, "Select");
        if(iReturn == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile.isDirectory()) {
                inputDirectoryTextField.setText(selectedFile.getAbsolutePath());
            } else {
                String message = "The input location selected is not a readable directory. Please select a readable directory.";
                JOptionPane.showMessageDialog(this, message, "Invalid form values", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_browseInputDirActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        handleCancelButton();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new XliffMergingPrefsDialog(new JFrame(), true).show();
        System.exit(0);
    }
    
    protected void handleOkButton() {
        //  Set the defaults
        defaultFileName = baseFileNameTextField.getText();
        defaultSuffix = suffixTextField.getText();
        defaultInputDir = inputDirectoryTextField.getText();
        defaultOutputDir = outputDirectoryTextField.getText();
        
        File inputDir = new File(defaultInputDir);
        File outputDir = new File(defaultOutputDir);
        boolean formInputOkay = false;
        
        if(testFormValues(inputDir, outputDir, defaultFileName, defaultSuffix)) {
            mergePreferences = new XliffMergingPrefs();
            mergePreferences.setInputDir(inputDir);
            mergePreferences.setOutputDir(outputDir);
            mergePreferences.setBaseFileName(defaultFileName);
            mergePreferences.setSuffix(defaultSuffix);
            
            setVisible(false);
            dispose();
        }
    }
    
    protected void handleCancelButton() {
        setVisible(false);
        dispose();
    }
    
    protected boolean testFormValues(File inputDir, File outputDir, java.lang.String baseFileName, java.lang.String suffix) {
        String message = "";
        if( !(inputDir.exists() && inputDir.isDirectory() && inputDir.canRead()) ) {
            //  Display error message
            message = "The input directory chosen is not a readable directory. Please select a readable directory.";
            JOptionPane.showMessageDialog(this, message, "Invalid form values", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if( !(outputDir.exists() && outputDir.isDirectory() && outputDir.canWrite()) ) {
            //  Display error message
            message = "The output location selected is not a writable directory. Please select a writable directory.";
            JOptionPane.showMessageDialog(this, message, "Invalid form values", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //  maintenance note: it may be necessary to put tests in here for the
        //  filename and the suffix.
        
        return true;
    }
    
    /** Getter for property mergePreferences.
     * @return Value of property mergePreferences.
     */
    public XliffMergingPrefs getMergePreferences() {
        return this.mergePreferences;
    }
    
    public void setDefaultInputDir(java.lang.String inputDirName) {
        defaultInputDir = inputDirName;
    }
    
    public void setDefaultSuffix(java.lang.String suffix) {
        defaultSuffix = suffix;
    }
    
    public void setDefaultFileName(java.lang.String fileName) {
        defaultFileName = fileName;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField baseFileNameTextField;
    private javax.swing.JButton browseInputDir;
    private javax.swing.JButton browseOutputDir;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField inputDirectoryTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField outputDirectoryTextField;
    private javax.swing.JTextField suffixTextField;
    // End of variables declaration//GEN-END:variables
    
    
}
