/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;
import org.jvnet.olt.editor.util.Bundle;

import java.util.logging.Logger;

import javax.swing.*;


//TODO add don;t ask again + you can change you translator Id by going blah blah
//
// SimonAW says: by going WHAT?
public class InputTranslatorIDDlg extends JDialog {
    private static final Logger logger = Logger.getLogger(InputTranslatorIDDlg.class.getName());
    private Bundle bundle = Bundle.getBundle(InputTranslatorIDDlg.class.getName());

	// Data
    private String translatorId;

	// UI Elements
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JLabel lblHeader = null;
	private JLabel lblPrompt = null;
	private JTextField txtTranslatorID = null;

	private JPanel pnlContents = null;
	private JPanel pnlControls = null;

	private GridBagLayout layout = null;
    
	/** Main C'tor
	 */
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
        txtTranslatorID.requestFocus();
        txtTranslatorID.setCaretPosition(0);
    }

    //end added
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            return;
        }
    }

    private void jbInit() throws Exception {
        this.setTitle(bundle.getString("Translator_ID"));

		layout = new GridBagLayout();
		pnlContents = new JPanel( layout );

		lblHeader = new JLabel( bundle.getString("Please_enter_your_translator_ID:") );
		/*
		lblHeader.setOpaque( true );
		lblHeader.setBackground( new Color( 220, 220, 220 ) );
		*/
		lblPrompt = new JLabel( bundle.getString("Translator_ID:"), SwingConstants.RIGHT );
		txtTranslatorID = new JTextField();
        txtTranslatorID.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    idTextField_actionPerformed(e);
                }
            });
        txtTranslatorID.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    idTextField_keyReleased(e);
                }

                //public void keyTyped(KeyEvent e) {
                //  idTextField_keyTyped(e);
                //}
            });


		btnOK = new JButton( bundle.getString("OK") );
		btnOK.setEnabled( false );
        btnOK.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
		btnCancel = new JButton( bundle.getString("Cancel") );
        btnCancel.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					handleCancelRequest();
                }
            });

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.ipadx = 4;
		constraints.ipady = 4;
		constraints.weightx = 1.0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets( 20, 8, 8, 8 );
		constraints.gridwidth = 3;
		pnlContents.add( lblHeader, constraints );

		constraints.insets = new Insets( 8, 8, 8, 8 );
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		pnlContents.add( lblPrompt, constraints );

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		pnlContents.add( txtTranslatorID, constraints );

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.LAST_LINE_END;
		pnlContents.add( btnOK, constraints );

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		pnlContents.add( btnCancel, constraints );

		setContentPane( pnlContents );

        String defaultId = (translatorId == null) ? "" : translatorId;
        txtTranslatorID.setText(defaultId);

        btnOK.setEnabled((defaultId != null) && (defaultId.trim().length() != 0));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();

		Dimension d = layout.preferredLayoutSize( pnlContents );
		d.setSize( d.width*1.2, d.height*1.2 );

		setSize( d );
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		/*
        this.getContentPane().setLayout(borderLayout1);
        actionPanel.setLayout(flowLayout1);
        okButton.setEnabled(false);
        okButton.setMaximumSize(new Dimension(75, 27));
        okButton.setMinimumSize(new Dimension(75, 27));
        okButton.setPreferredSize(new Dimension(75, 27));
        okButton.setMnemonic('O');
        okButton.setText(bundle.getString("OK"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        flowLayout1.setHgap(50);
        centralPanel.setLayout(null);
        infoLabel.setForeground(Color.black);
        infoLabel.setText(bundle.getString("Please_enter_your_translator_ID:"));
        infoLabel.setBounds(new Rectangle(3, 1, 317, 33));
        idLabel.setBounds(new Rectangle(71, 43, 88, 26));
        idLabel.setText(bundle.getString("Translator_ID:"));
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

        this.setSize(400, 160);
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
		*/
    }

    void okButton_actionPerformed(ActionEvent e) {
        //String id = idTextField.getText();
        this.translatorId = txtTranslatorID.getText().trim();
        this.setVisible(false);
    }


	private void handleCancelRequest() {
		int iRet = JOptionPane.showConfirmDialog(
				this,
				bundle.getString("Do_you_want_to_exit_the_editor?"),
				bundle.getString("Information"),
				JOptionPane.YES_NO_OPTION
				);

            switch (iRet) {
            case JOptionPane.YES_OPTION:
                System.exit(0);

                break;

            case JOptionPane.NO_OPTION:
                return;
            }
	}

    void idTextField_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			handleCancelRequest();
        }

        String id = txtTranslatorID.getText();

        if ((id == null) || (id.trim().length() == 0)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            btnOK.setEnabled(false);

            return;
        }

        if ((id != null) && (id.trim().length() > 5)) {
            //Toolkit.getDefaultToolkit().beep();
            //JOptionPane.showMessageDialog(this,"Invalidate translator ID!","Error",JOptionPane.WARNING_MESSAGE);
            txtTranslatorID.setText(id.substring(0, id.length() - 1));

            return;
        }

        btnOK.setEnabled(true);
    }

    void idTextField_actionPerformed(ActionEvent e) {
        btnOK.doClick();
    }

    public String getTranslatorId() {
        return translatorId;
    }
}
