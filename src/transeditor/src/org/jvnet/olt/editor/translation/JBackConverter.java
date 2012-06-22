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
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.jvnet.olt.editor.util.Bundle;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.jvnet.olt.editor.backconv.BackConversionOptions;
import org.jvnet.olt.editor.backconv.GeneralOptionsPanel;
import org.jvnet.olt.editor.backconv.SGMLOptionsPanel;
import org.jvnet.olt.editor.model.CharacterEncoding;


public class JBackConverter extends JDialog {
    private static final Logger logger = Logger.getLogger(JBackConverter.class.getName());
    
    private JFrame m_frame;
    private JPanel jPanelSrc; // = new JPanel();
    private JLabel jLabel1; // = new JLabel();
    private JTextField jSrcFileName; // = new JTextField();
    private JButton jbtnSrc; // = new JButton();
    private JLabel jLabel2; // = new JLabel();
    private JLabel jLabel3; // = new JLabel();
    private JTextField jTagFileName; // = new JTextField();
    private JButton jbtnTar; // = new JButton();
    private JComboBox jComboBox1;
    private JButton jbtnExit; // = new JButton();
    private JButton jbtnConvert; // = new JButton();//true for file; false for dir;
    private JCheckBox jcbTMX; // = new JCheckBox();
    private JTabbedPane tabbedPane;
    private SGMLOptionsPanel sgmlOptions;
    
    //TODO change to somthing better
    private BackConversionOptions options ;
    
    private Bundle bundle = Bundle.getBundle(JBackConverter.class.getName());
//private String strCurrentPath = null;
    private File preselectedFile;
    private javax.swing.JCheckBox jcbTransState; // = new JCheckBox();

    public JBackConverter(JFrame m_fmInput) {
        super(m_fmInput, true);

        m_frame = m_fmInput;
    }

    public void setPreselectedFile(File f){
        this.preselectedFile = f;
        if(f != null){
            jSrcFileName.setText(f.getAbsolutePath());
            jTagFileName.setText(f.getParentFile().getAbsolutePath());
        }
    }
    protected void dialogInit() {
        super.dialogInit();
        
        bundle = Bundle.getBundle(JBackConverter.class.getName());

        options = new BackConversionOptions();
        
        GridBagConstraints gridBagConstraints;

        setTitle(bundle.getString("Back_Converter_Dialog"));

        //jcbTransState = new javax.swing.JCheckBox();
        jComboBox1 = new JComboBox(CharacterEncoding.getCharacterEncodingList());
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jbtnSrc = new javax.swing.JButton();
        jbtnTar = new javax.swing.JButton();
        jSrcFileName = new javax.swing.JTextField();
        jTagFileName = new javax.swing.JTextField();
        //jcbTMX = new javax.swing.JCheckBox();

        JPanel jPanel2 = new javax.swing.JPanel();
        jbtnConvert = new javax.swing.JButton();
        jbtnExit = new javax.swing.JButton();

        JPanel thePanel = new JPanel();

        getContentPane().setLayout(new GridBagLayout());

/*        jcbTransState.setText("Write translation status into SGML files");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jcbTransState, gridBagConstraints);
*/
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jComboBox1, gridBagConstraints);

        jLabel1.setText(bundle.getString("Source_file/directory"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);

        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText(bundle.getString("Encoding"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints.weighty = 0.5;

        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText(bundle.getString("Target_Directory"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints.weighty = 0.5;

        getContentPane().add(jLabel3, gridBagConstraints);

        jbtnSrc.setText(bundle.getString("Browse..."));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        getContentPane().add(jbtnSrc, gridBagConstraints);

        jbtnSrc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbtnSrc_actionPerformed(e);
                }
            });

        jbtnTar.setText(bundle.getString("Browse..."));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        getContentPane().add(jbtnTar, gridBagConstraints);

        jbtnTar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbtnTar_actionPerformed(e);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;

        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        getContentPane().add(jSrcFileName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;

        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTagFileName, gridBagConstraints);

        /*jcbTMX.setText("Create TMX File");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(jcbTMX, gridBagConstraints);
        */
        jbtnConvert.setText(bundle.getString("OK"));
        jbtnConvert.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jbtnConvert_actionPerformed(evt);
                }
            });

        jPanel2.add(jbtnConvert);

        jbtnExit.setText(bundle.getString("Cancel"));
        jPanel2.add(jbtnExit);
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jbtnExit_actionPerformed(e);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        sgmlOptions = new SGMLOptionsPanel(options);
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addTab(bundle.getString("General"), new GeneralOptionsPanel(options));
        tabbedPane.addTab(bundle.getString("SGML"), sgmlOptions);
        
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
        tabPanel.add(tabbedPane,BorderLayout.CENTER);
        
        tabPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("Backconversion_options")));
        
        JButton saveConfigButton = new JButton(bundle.getString("Save_config"));
        saveConfigButton.setEnabled(false);
        JPanel saveCfgPanel = new JPanel(new BorderLayout());
        saveCfgPanel.add(saveConfigButton,BorderLayout.EAST);
              
        tabPanel.add(saveCfgPanel,BorderLayout.SOUTH);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabPanel, gridBagConstraints);

        
        
        pack();
    }

    void jbtnExit_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    void jbtnConvert_actionPerformed(ActionEvent e) {
        String charsetString = jComboBox1.getSelectedItem().toString();
        String javaCharsetName = CharacterEncoding.getJavaName(charsetString);

        if ("".equals(javaCharsetName)) {
            JOptionPane.showMessageDialog(this, bundle.getString("Please_select_a_valid_encoding!"), bundle.getString("Warning"), JOptionPane.OK_OPTION);

            //            jComboBox1.setSelectedIndex(0);
            return;
        }

        String strSource = jSrcFileName.getText();
        String strOutput = jTagFileName.getText();

        if (strSource.length() == 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("Please_specify_location_of_conversion_source_file"), bundle.getString("Warning"), JOptionPane.OK_OPTION);

            return;
        }
        //TODO Change to pass Options to Executor

        options.setEncoding(javaCharsetName);
        boolean bTmx = options.isGenerateTMX();
        boolean boolWriteTransStatus = options.isWriteTransStatusToSGML();

        JBackConverterExecutor exec = new JBackConverterExecutor(options.createBackConverterProperties(), charsetString,options.isGenerateTMX());

        try {
            exec.setSourceFile(strSource);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(m_frame, bundle.getString("Could_not_find_the_source_file.Please_select_an_existing_.xlz_file_and_try_again."), bundle.getString("Error"), JOptionPane.OK_OPTION);

            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(m_frame, bundle.getString("Could_to_open_the_source_file_for_reading."), bundle.getString("Error"), JOptionPane.OK_OPTION);

            return;
        }

        try {
            exec.setTargetFile(strOutput);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(m_frame, bundle.getString("The_target_directory_could_not_be_found.Please_select_an_existing_directory_and_try_again"), bundle.getString("Error"), JOptionPane.OK_OPTION);

            return;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(m_frame, bundle.getString("The_target_file_is_not_a_directory.Please_select_a_directory_and_try_again"), bundle.getString("Error"), JOptionPane.OK_OPTION);

            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(m_frame, bundle.getString("The_target_directory_is_not_readable_or_writable.Please_select_a_directory_that_is_readable_and_writable_and_try_again"), bundle.getString("Error"), JOptionPane.OK_OPTION);

            return;
        }

        
        Map m = sgmlOptions.getUnicode2EntityMap();
        logger.finest("Map:"+m);
        
        JBackConverterProgressFrame progressFrame = new JBackConverterProgressFrame(this);
        progressFrame.setVisible(true);

        //Disable myself; wait until child dlg is closed; enable myself
        setEnabled(false);
        progressFrame.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    JBackConverter.this.setEnabled(true);
                }
            });

        exec.setStatusCallback(progressFrame);
        exec.convert();
    }

    void jbtnSrc_actionPerformed(ActionEvent e) {
        File fSrcFile = new File(".");

        JFileChooser m_fChooser = new JFileChooser(new File(MainFrame.strCurrentDir).getParentFile());
        m_fChooser.setAcceptAllFileFilterUsed(false);
        m_fChooser.setFileFilter(OpenFileFilters.XLZ_FILTER);
        m_fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // allow file and directory
        m_fChooser.setSelectedFile(new File("."));
        m_fChooser.setCurrentDirectory(preselectedFile.getParentFile());

        int result = m_fChooser.showOpenDialog(this);

        switch (result) {
        case JFileChooser.APPROVE_OPTION:
            fSrcFile = m_fChooser.getSelectedFile();

            break;

        case JFileChooser.CANCEL_OPTION:
            return;
        }

        jSrcFileName.setText(fSrcFile.getAbsolutePath());

        if ((jTagFileName.getText() == null) || (jTagFileName.getText().length() == 0)) {
            jTagFileName.setText(fSrcFile.isDirectory() ? fSrcFile.getAbsolutePath() : fSrcFile.getParentFile().getAbsolutePath());
        }
    }

    void jbtnTar_actionPerformed(ActionEvent e) {
        File fTargetFile = new File(".");
        JFileChooser m_fChooser = new JFileChooser(new File(MainFrame.strCurrentDir).getParentFile());
        m_fChooser.setFileFilter(OpenFileFilters.XLZ_FILTER);
        m_fChooser.setAcceptAllFileFilterUsed(false);
        m_fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        m_fChooser.setCurrentDirectory(preselectedFile.getParentFile());

        int result = m_fChooser.showSaveDialog(this);

        switch (result) {
        case JFileChooser.APPROVE_OPTION:
            fTargetFile = m_fChooser.getSelectedFile();

            break;

        case JFileChooser.CANCEL_OPTION:
            return;
        }

        jTagFileName.setText(fTargetFile.getAbsolutePath());
    }

    /** This method accepts a Memento and sets settings.
     */
    public void setDialogSettings(JBackConverterSettings settings) {
        settings.setDialogSettings(this);
    }

    /** This method creates a Memento to store settings.
     */
    public JBackConverterSettings getDialogSettings() {
        //TODO change to reflect the options
        
        String source = jSrcFileName.getText();
        String targetDir = jTagFileName.getText();
        String encoding = (String)jComboBox1.getSelectedItem();
        boolean createTmx = options.isGenerateTMX();
        boolean writeStatus = options.isWriteTransStatusToSGML();

        JBackConverterSettings settings = new JBackConverterSettings(source, targetDir, encoding, createTmx, writeStatus);

        return settings; 
    }

    //  The methods below set the values of various fields in the dialog box.
    //
    public void setSourceField(java.lang.String source) {
        jSrcFileName.setText(source);
    }

    public void setTargetField(java.lang.String targetDir) {
        jTagFileName.setText(targetDir);
    }

    public void setEncodingSelection(java.lang.String encoding) {
        jComboBox1.setSelectedItem(encoding);
    }

    public void setCreateTmxCheckbox(boolean createTmx) {
        //jcbTMX.setSelected(createTmx);
    }

    public void setWriteStatusCheckbox(boolean writeStatus) {
        //jcbTransState.setSelected(writeStatus);
    }
}
