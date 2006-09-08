/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * NewJFrame.java
 *
 * Created on February 3, 2005, 5:52 PM
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.*;
import org.jvnet.olt.editor.util.Task;


/**
 *
 * @author  boris
 */
public class JBackConverterProgressFrame extends JDialog implements BackconversionStatusCallback {
    private JButton closeButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JPanel labelPanel2;
    private JScrollPane jScrollPane1;
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private int oneStep;
    private int fileCount;
    private int errorCount;
    private boolean done;
    
    public JBackConverterProgressFrame(JDialog parent) {
        super(parent);
        
        setLocationRelativeTo(parent);
    }
    
    protected void dialogInit() {
        super.dialogInit();
        
        setTitle("Backconversion progress");
        
        mainPanel = new JPanel();
        buttonPanel = new JPanel();
        labelPanel2 = new JPanel();
        logTextArea = new JTextArea();
        progressBar = new JProgressBar(0, 100);
        closeButton = new JButton("Close");
        
        closeButton.setEnabled(false);
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainLayout);
        
        mainPanel.setBorder(new EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        mainPanel.setMinimumSize(new java.awt.Dimension(200, 100));
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setMinimumSize(new java.awt.Dimension(100, 35));
        labelPanel.setMaximumSize(new java.awt.Dimension(65535, 35));
        labelPanel.setPreferredSize(new java.awt.Dimension(360, 35));
        labelPanel.add(new JLabel("Backconversion log messages:"));
        
        mainPanel.add(labelPanel);
        
        jScrollPane1 = new JScrollPane(logTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setMinimumSize(new Dimension(100, 80));
        jScrollPane1.setMaximumSize(new Dimension(65536, 65536));
        jScrollPane1.setPreferredSize(new Dimension(360, 80));
        
        mainPanel.add(jScrollPane1);
        
        labelPanel2.setMinimumSize(new java.awt.Dimension(100, 35));
        labelPanel2.setMaximumSize(new java.awt.Dimension(32767, 35));
        labelPanel2.setPreferredSize(new java.awt.Dimension(360, 35));
        labelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel2.add(new JLabel("Overall progress:"));
        mainPanel.add(labelPanel2);
        
        progressBar.setMaximumSize(new java.awt.Dimension(32767, 25));
        progressBar.setMinimumSize(new java.awt.Dimension(100, 25));
        progressBar.setPreferredSize(new java.awt.Dimension(360, 25));
        
        progressBar.setStringPainted(true);
        mainPanel.add(progressBar);
        
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 20));
        
        buttonPanel.setMinimumSize(new java.awt.Dimension(100, 65));
        buttonPanel.setMaximumSize(new java.awt.Dimension(32767, 65));
        buttonPanel.setPreferredSize(new java.awt.Dimension(360, 65));
        
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syntheticClose();
            }
        });
        
        logTextArea.setWrapStyleWord(false);
        logTextArea.setEditable(false);
        
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel);
        
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (done) {
                    syntheticClose();
                } else {
                    JOptionPane.showMessageDialog(JBackConverterProgressFrame.this, "Backcoversion in progress.Please wait", "Backconversion in progress", JOptionPane.OK_OPTION);
                }
            }
        });
        
        pack();
    }
    
    void syntheticClose() {
        WindowEvent synEvent = new WindowEvent(JBackConverterProgressFrame.this, WindowEvent.WINDOW_CLOSED);
        processWindowEvent(synEvent);
        this.dispose();
    }
    
    void log(final String msg) {
        final JTextArea textArea = logTextArea;
        
        new Task(){
            protected void execute(){
                textArea.append(msg);
            }
        }.start();
    }
    
    void tick() {
        final JTextArea textArea = logTextArea;
        final JProgressBar progBar = progressBar;
        
        new Task(){
            protected void execute(){
                progBar.setValue(progBar.getValue() + oneStep);
                textArea.setCaretPosition(textArea.getText().length());                
            }
        }.start();
    }
    
    public void conversionStart(int numFiles) {
        fileCount = numFiles;
        
        if (numFiles > 1) {
            oneStep = 100 / numFiles;
            log("Processing " + numFiles + " files:\n");
        }
    }
    
    public void fileStarted(java.io.File f) {
        log("\n");
        log("File " + f.getAbsolutePath());
    }
    
    public void fileError(int errorCode, Throwable t) {
        
        log("\n");
        
        if (errorCode == BackconversionStatusCallback.ERROR_BACKCONV) {
            log("backcoversion FAILED");
        } else if (errorCode == BackconversionStatusCallback.ERROR_TMX) {
            log("TMX generation FAILED");
        } else if (errorCode == BackconversionStatusCallback.ERROR_FRAMEFILE) {
            log("Frame file backconversion on this client is not available.");
        } else if (errorCode == BackconversionStatusCallback.ERROR_NO_FILES){
            log("No .xlz files found in source direcotry");        
        } else {
            log("Unknown error");
        }  
        
        log("\n");
        
        if(t != null){
            log(t.getMessage());
            log("\n");
        }
        
        
        errorCount++;
        
        tick();
    }
    
    public void fileSuccess() {
        log("\nOK\n");
        tick();
    }
    
    public void unlock() {
        closeButton.setEnabled(true);
    }
    
    synchronized public void conversionEnd() {
        done = true;
        progressBar.setValue(100);
        log("Backconversion finished\n\n");
        log("Processed " + fileCount + " files with " + errorCount + " errors\n");
        
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }
    
}
