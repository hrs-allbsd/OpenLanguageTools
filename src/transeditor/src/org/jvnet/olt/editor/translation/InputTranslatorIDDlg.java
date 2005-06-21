/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.util.logging.Logger;

import javax.swing.*;


//TODO add don;t ask again + you can change you translator Id by going blah blah
public class InputTranslatorIDDlg extends JDialog {
    private static final Logger logger = Logger.getLogger(InputTranslatorIDDlg.class.getName());
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel actionPanel = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton okButton = new JButton();
    JPanel centralPanel = new JPanel();
    JLabel infoLabel = new JLabel();
    JLabel idLabel = new JLabel();
    JTextField idTextField = new JTextField();
    private String translatorId;

    public InputTranslatorIDDlg(JFrame parent, String translatorId) {
        super(parent, true);

        this.translatorId = translatorId;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Added by cl141268, update() would be called automatically after Construtor
    when the dialog has been called, the idTextField component will be focused.*/
    public void update(Graphics g) {
        idTextField.requestFocus();
        idTextField.setCaretPosition(0);
    }

    //end added
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            return;
        }
    }

    private void jbInit() throws Exception {
        this.setTitle("Translator ID");
        this.getContentPane().setLayout(borderLayout1);
        actionPanel.setLayout(flowLayout1);
        okButton.setEnabled(false);
        okButton.setMaximumSize(new Dimension(75, 27));
        okButton.setMinimumSize(new Dimension(75, 27));
        okButton.setPreferredSize(new Dimension(75, 27));
        okButton.setMnemonic('O');
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        flowLayout1.setHgap(50);
        centralPanel.setLayout(null);
        infoLabel.setForeground(Color.black);
        infoLabel.setText("Please enter your translator ID:");
        infoLabel.setBounds(new Rectangle(3, 1, 317, 33));
        idLabel.setBounds(new Rectangle(71, 43, 88, 26));
        idLabel.setText("Translator ID:");
        idLabel.setForeground(Color.black);
        idTextField.setBounds(new Rectangle(167, 43, 73, 27));
        idTextField.setColumns(5);
        idTextField.setFont(new Font("Dialog", Font.PLAIN, 14));
        idTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    idTextField_actionPerformed(e);
                }
            });
        idTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    idTextField_keyReleased(e);
                }

                //public void keyTyped(KeyEvent e) {
                //  idTextField_keyTyped(e);
                //}
            });
        this.getContentPane().add(actionPanel, BorderLayout.SOUTH);
        actionPanel.add(okButton, null);
        this.getContentPane().add(centralPanel, BorderLayout.CENTER);
        centralPanel.add(infoLabel, null);
        centralPanel.add(idLabel, null);
        centralPanel.add(idTextField, null);

        //    this.addKeyListener(this);
        String defaultId = (translatorId == null) ? "" : translatorId;
        idTextField.setText(defaultId);

        okButton.setEnabled((defaultId != null) && (defaultId.trim().length() != 0));

        this.setSize(300, 160);
        this.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    void okButton_actionPerformed(ActionEvent e) {
        //String id = idTextField.getText();
        this.translatorId = idTextField.getText().trim();
        this.setVisible(false);
    }

    void idTextField_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int iRet = JOptionPane.showConfirmDialog(this, "Do you want to exit the editor?", "Information", JOptionPane.YES_NO_OPTION);

            switch (iRet) {
            case JOptionPane.YES_OPTION:
                System.exit(0);

                break;

            case JOptionPane.NO_OPTION:
                return;
            }
        }

        String id = idTextField.getText();

        if ((id == null) || (id.trim().length() == 0)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            okButton.setEnabled(false);

            return;
        }

        if ((id != null) && (id.trim().length() > 5)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            idTextField.setText(id.substring(0, id.length() - 1));

            return;
        }

        okButton.setEnabled(true);
    }

    void idTextField_actionPerformed(ActionEvent e) {
        okButton.doClick();
    }

    public String getTranslatorId() {
        return translatorId;
    }
}
