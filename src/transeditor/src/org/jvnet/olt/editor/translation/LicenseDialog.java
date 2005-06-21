/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/** This class shows license agreement.
 *
 * It has two modes of operation. When agreementMode is set to
 * true then the buttons Agree and decline are shown.
 * After pressing any of them one can query the reusult via
 * didAgree method.
 *
 * In non-agreement mode only OK button is displayed. The didAgree
 * value is not defined.
 *
 * @author Boris Steiner
 *
 */
public class LicenseDialog extends JDialog {
    private JFrame parent;
    private String licenseText;
    private boolean agreeMode = false;
    private JPanel buttons1;
    private JPanel buttons2;
    private boolean didAgree;

    /** Construct dialog with license text and in an operation mode
     *
     * @param parent Parent Frame
     * @param licenseText Text to be shown as license
     * @param agreeMode
     */
    public LicenseDialog(JFrame parent, String licenseText, boolean agreeMode) {
        super();

        this.parent = parent;
        this.licenseText = licenseText;
        this.agreeMode = agreeMode;

        init();
    }

    /** Create UI
     *
     */
    void init() {
        setTitle("Binary Licence Agreement");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout(20, 20));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = parent.getSize();

        frameSize.height = (int)(frameSize.height * 0.8);
        frameSize.width = (int)(frameSize.width * 0.8);

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        setSize((int)(frameSize.getWidth()), (int)(frameSize.getHeight()));
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        JLabel label = new JLabel("Please read the binary license agreement below:");
        label.setAlignmentY((float)0.5);
        getContentPane().add(label, BorderLayout.NORTH);

        JTextArea textPane = new JTextArea();
        textPane.setEditable(false);
        textPane.setText(licenseText);
        textPane.setLineWrap(true);
        textPane.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        buttons1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton agree = new JButton("Agree");
        agree.setActionCommand("agree");

        JButton decline = new JButton("Decline");

        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                didAgree = "agree".equals(e.getActionCommand());
                setVisible(false);
            }
        };

        agree.addActionListener(buttonListener);
        decline.addActionListener(buttonListener);

        buttons1.add(agree);
        buttons1.add(decline);

        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
        buttons2.add(ok);

        setButtons();
    }

    /** sets/resets pane containg buttons according to mode
     *
     */
    void setButtons() {
        getContentPane().remove(agreeMode ? buttons2 : buttons1);
        getContentPane().add(agreeMode ? buttons1 : buttons2, BorderLayout.SOUTH);
    }

    /** set agreement mode
     *
     * after a mode different from current one is set
     * setButtons is called
     *
     * @param mode
     */
    void setAgreementMode(boolean mode) {
        if (this.agreeMode == mode) {
            return;
        }

        this.agreeMode = mode;
        setButtons();
    }

    /** query agreement state.
     *
     * When agreementMode = false this value is undefined (may be true or false)
     *
     * @return
     */
    boolean getAgreementMode() {
        return this.agreeMode;
    }

    /** get user selection
     *
     * undefined when agreementMode != true
     *
     * @return
     */
    boolean didAgree() {
        return didAgree;
    }
}
