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

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


public class CustomKeyboard extends JDialog implements ListSelectionListener {
    //extends DefaultCustomKeyboard {
    private static final Logger logger = Logger.getLogger(CustomKeyboard.class.getName());

    Border border1;
    TitledBorder titledBorder1;
    JLabel desLabel = new JLabel();
    JLabel categoryLabel = new JLabel();
    JLabel commandLabel = new JLabel();
    JLabel newShortCutLabel = new JLabel();
    JLabel curShortCutLabel = new JLabel();

    JPanel rightPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel innerLeftPanel = new JPanel();
    JPanel outerLeftPanel = new JPanel();

    JButton closeButton = new JButton();
    JButton setButton = new JButton();
    JButton resetButton = new JButton();
    JButton resetAllButton = new JButton();
    JButton noShortCutButton = new JButton();
    JList catList = new JList();
    JList comList = new JList();
    JTextField newShortcutTextField = new JTextField();
    JTextField curShortcutTextField = new JTextField();
    JFrame parent;
    KeyEvent curE = null;
    boolean set = false;
    protected String[] category = null;
    protected Vector[] commands = null;
    protected Vector[] menus = null;
    protected Vector[] strokes = null;
    protected ShortcutsBuilder builder;

    public CustomKeyboard(JFrame parent, ShortcutsBuilder builder) {
        super(parent);

        this.parent = parent;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setSize(this.getPreferredSize());
        this.category = builder.getCategories();
        this.commands = builder.getCommands();
        this.menus = builder.getMenus();
        this.strokes = builder.getStrokes();
        this.builder=builder;

        initGUI();
    }

    protected void setButton_actionPerformed(ActionEvent e) {
        int index1 = catList.getSelectedIndex();
        int index2 = comList.getSelectedIndex();

        if ((index1 == -1) || (index2 == -1)) {
            return;
        }

        try {
            if (curE != null) {
                KeyStroke stroke = KeyStroke.getKeyStrokeForEvent(curE);

                boolean usedByOtherMenu = false;
OUT: 
                for (int i = 0; i < menus.length; i++) {
                    Vector temp = menus[i];

                    for (int j = 0; j < temp.size(); j++) {
                        JMenuItem m = (JMenuItem)temp.elementAt(j);

                        if (stroke.equals(m.getAccelerator())) {
                            usedByOtherMenu = true;

                            break OUT;
                        }
                    }
                }

                if (usedByOtherMenu) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this, "This shortcut is already in use. Please choose another shortcut.", "Duplicate Shortcut", JOptionPane.WARNING_MESSAGE);

                    return;
                }

                ((ShortcutsBuilder.KeyStrokeRecord)strokes[index1].elementAt(index2)).setNew(stroke);
                ((JMenuItem)menus[index1].elementAt(index2)).setAccelerator(null);
                ((JMenuItem)menus[index1].elementAt(index2)).setAccelerator(stroke);

                // bug 4744603 refresh the keymap when changing the shortcuts of MenuItem PageUp or PageDown
                ///////////////////////////
                if (((JMenuItem)menus[index1].elementAt(index2)).getText().toLowerCase().equals("page down") || ((JMenuItem)menus[index1].elementAt(index2)).getText().toLowerCase().equals("page up")) {
                    if (PivotTextEditor1.sourceEditor != null) {
                        PivotTextEditor1.sourceEditor.resetPivotTextPaneKeymp();
                    }

                    if (PivotTextEditor1.targetEditor != null) {
                        PivotTextEditor1.targetEditor.resetPivotTextPaneKeymp();
                    }
                }

                ///////////////////////////
                curShortcutTextField.setText(newShortcutTextField.getText());
                newShortcutTextField.setText("");

                this.setButton.setEnabled(false);
                this.resetButton.setEnabled(true);
                this.resetAllButton.setEnabled(true);
                this.noShortCutButton.setEnabled(true);
                curE = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void resetAllButton_actionPerformed(ActionEvent e) {
        int ret = JOptionPane.showConfirmDialog(this, "This action will replace all customized shortcuts with the default settings.\nAre you sure you want to proceed?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (ret == JOptionPane.YES_OPTION) {
            this.builder.setDefaults();
            this.builder.parseMenu();
            /*
            for (int i = 0; i < strokes.length; i++) {
                for (int j = 0; j < strokes[i].size(); j++) {
                    ((JMenuItem)menus[i].elementAt(j)).setAccelerator(null);

                    String strShortCut = (String)this.shortCutmap.get(((JMenuItem)menus[i].elementAt(j)).getName());

                    if (strShortCut != null) {
                        int iMode = Integer.parseInt(strShortCut.substring(0, strShortCut.indexOf(" ")));
                        int iKeyCode = Integer.parseInt(strShortCut.substring(strShortCut.indexOf(" ") + 1, strShortCut.length()));
                        KeyStroke newKeyStroke = KeyStroke.getKeyStroke(iKeyCode, iMode);
                        ((JMenuItem)menus[i].elementAt(j)).setAccelerator(newKeyStroke);
                    }

                    ((ShortcutsBuilder.KeyStrokeRecord)strokes[i].get(j)).reset();
                }
            }
            */

            /**
             * refresh GUI
             */
            int index1 = catList.getSelectedIndex();
            int index2 = comList.getSelectedIndex();

            if ((index1 == -1) || (index2 == -1)) {
                curShortcutTextField.setText("");
                newShortcutTextField.setEditable(false);

                return;
            }

            KeyStroke stroke = ((ShortcutsBuilder.KeyStrokeRecord)strokes[index1].elementAt(index2)).getNew();

            if (stroke == null) {
                JMenuItem o = (JMenuItem)menus[index1].elementAt(index2);
                stroke = o.getAccelerator();
            }

            if (stroke != null) {
                int keyCode = stroke.getKeyCode();
                int mod = stroke.getModifiers();
                String modStr = KeyEvent.getKeyModifiersText(mod);
                String keyStr = KeyEvent.getKeyText(keyCode);

                if (modStr.length() == 0) {
                    curShortcutTextField.setText(keyStr);
                } else {
                    curShortcutTextField.setText(modStr + "+" + keyStr);
                }

                this.noShortCutButton.setEnabled(true);
            } else {
                curShortcutTextField.setText("");
                this.noShortCutButton.setEnabled(false);
            }

            this.resetButton.setEnabled(true);
            this.resetAllButton.setEnabled(true);
        }
    }

    protected void resetButton_actionPerformed(ActionEvent e) {
        int index1 = catList.getSelectedIndex();
        int index2 = comList.getSelectedIndex();

        if ((index1 == -1) || (index2 == -1)) {
            return;
        }

        try {
            ((JMenuItem)menus[index1].elementAt(index2)).setAccelerator(null);

            // this does not really work for sub menu entries, as we don't identify the parent here
            // TODO: get the partent->item here

            String strShortCut = (String)this.builder.getDefaultMap().get(((JMenuItem)menus[index1].elementAt(index2)).getName());

            if (strShortCut != null) {
                int iMode = Integer.parseInt(strShortCut.substring(0, strShortCut.indexOf(" ")));
                int iKeyCode = Integer.parseInt(strShortCut.substring(strShortCut.indexOf(" ") + 1, strShortCut.length()));
                KeyStroke newKeyStroke = KeyStroke.getKeyStroke(iKeyCode, iMode);
                ((JMenuItem)menus[index1].elementAt(index2)).setAccelerator(newKeyStroke);
            }

            ((ShortcutsBuilder.KeyStrokeRecord)strokes[index1].get(index2)).reset();

            /**
             * refresh GUI
             */
            JMenuItem o = (JMenuItem)menus[index1].elementAt(index2);
            KeyStroke stroke = o.getAccelerator();

            if (stroke != null) {
                int keyCode = stroke.getKeyCode();
                int mod = stroke.getModifiers();
                String modStr = KeyEvent.getKeyModifiersText(mod);
                String keyStr = KeyEvent.getKeyText(keyCode);

                if (modStr.length() == 0) {
                    curShortcutTextField.setText(keyStr);
                } else {
                    curShortcutTextField.setText(modStr + "+" + keyStr);
                }

                this.noShortCutButton.setEnabled(true);
            } else {
                curShortcutTextField.setText("");
                this.noShortCutButton.setEnabled(false);
            }

            this.resetButton.setEnabled(true);
            this.resetAllButton.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
       // bug 4744603 -------------------------------------------------------------------
       //add by cl141268 get shortcuts keystroke
       public static KeyStroke getMenuItemShortcut(String menuName, String commandName) {
           int i=0,j=0;
           for(i=0;i<category.length;i++) {
               //logger.finer(category[i]);
               if((category[i]).toLowerCase().equals(menuName.toLowerCase())) break;
           }
           if(i < category.length) {
               Vector menuV = menus[i];
               Vector cmds = commands[i];
               for(j=0;j<cmds.size();j++) {
                   //logger.finer(cmds.get(j));
                   if(cmds.get(j).toString().toLowerCase().equals(commandName.toLowerCase())) break;
               }
               if(j<cmds.size()) {
                   JMenuItem item = (JMenuItem)menuV.get(j);
                   return item.getAccelerator();
               }
           }
           return null;
       }
     *
     */
    private void jbInit() throws Exception {

        // outer left dialog panel
        categoryLabel.setText("Menus:");
        categoryLabel.setAlignmentX(LEFT_ALIGNMENT);
        categoryLabel.setToolTipText("");
        categoryLabel.setForeground(Color.black);

        JScrollPane scr1 = new JScrollPane(catList);

        curShortCutLabel.setText("Current shortcut:");
        curShortCutLabel.setAlignmentX(LEFT_ALIGNMENT);
        curShortCutLabel.setToolTipText("");
        curShortCutLabel.setForeground(Color.black);

        curShortcutTextField.setEditable(false);
        curShortcutTextField.setMaximumSize( new Dimension (Short.MAX_VALUE, 22));

        outerLeftPanel.setLayout(new BoxLayout(outerLeftPanel, BoxLayout.Y_AXIS ));
        outerLeftPanel.add(categoryLabel);
        outerLeftPanel.add(scr1);
        outerLeftPanel.add(curShortCutLabel);
        outerLeftPanel.add(curShortcutTextField);

        // inner left dialog panel
        commandLabel.setText("Menu Options:");
        commandLabel.setAlignmentX(LEFT_ALIGNMENT);
        commandLabel.setToolTipText("");
        commandLabel.setForeground(Color.black);

        JScrollPane scr2 = new JScrollPane(comList);

        newShortCutLabel.setText("New shortcut:");
        newShortCutLabel.setAlignmentX(LEFT_ALIGNMENT);
        newShortCutLabel.setMaximumSize( new Dimension (Short.MAX_VALUE, 22));
        newShortCutLabel.setToolTipText("");
        newShortCutLabel.setForeground(Color.black);

        newShortcutTextField.setEditable(false);
        newShortcutTextField.setMaximumSize( new Dimension (Short.MAX_VALUE, 22));
        newShortcutTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    newShortcutTextField_keyReleased(e);
                }

                public void keyPressed(KeyEvent e) {
                    newShortcutTextField_keyPressed(e);
                }

                public void keyTyped(KeyEvent e) {
                    e.consume();
                }
            });

        innerLeftPanel.setLayout(new BoxLayout(innerLeftPanel, BoxLayout.Y_AXIS ));
        innerLeftPanel.setAlignmentX(LEFT_ALIGNMENT);
        innerLeftPanel.add(commandLabel);
        innerLeftPanel.add(scr2);
        innerLeftPanel.add(newShortCutLabel);
        innerLeftPanel.add(newShortcutTextField);

        // right dialog panel (buttons)
        closeButton.setText("Close");
        closeButton.setBounds(new Rectangle(11, 223, 102, 27));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeButton_actionPerformed(e);
                }
            });

        setButton.setText("Set");
        setButton.setBounds(new Rectangle(11, 42, 102, 27));
        setButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setButton_actionPerformed(e);
                }
            });

        resetButton.setText("Reset");
        resetButton.setBounds(new Rectangle(11, 79, 102, 27));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetButton_actionPerformed(e);
                }
            });

        resetAllButton.setText("Reset All...");
        resetAllButton.setBounds(new Rectangle(11, 115, 102, 27));
        resetAllButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetAllButton_actionPerformed(e);
                }
            });

        noShortCutButton.setMargin(new Insets(2, 10, 2, 10));
        noShortCutButton.setText("No Shortcut");
        noShortCutButton.setBounds(new Rectangle(11, 151, 102, 27));
        noShortCutButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    noShortCutButton_actionPerformed(e);
                }
            });

        rightPanel.setLayout(null);
        rightPanel.setMinimumSize(new Dimension(110, 300));
        rightPanel.setPreferredSize(new Dimension(120, 300));
        rightPanel.add(setButton, null);
        rightPanel.add(resetAllButton, null);
        rightPanel.add(resetButton, null);
        rightPanel.add(noShortCutButton, null);
        rightPanel.add(closeButton, BorderLayout.SOUTH);

        // center panel - contains the "both left" panels and the descritption label
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142));
        titledBorder1 = new TitledBorder(border1, "Description");
        desLabel.setBorder(titledBorder1);

        centerPanel.setLayout(new BorderLayout(10,5));
        centerPanel.add(outerLeftPanel, BorderLayout.WEST);
        centerPanel.add(innerLeftPanel, BorderLayout.CENTER);
        centerPanel.add(desLabel, BorderLayout.SOUTH);

        this.getContentPane().setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension (400,300));
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);
    }

    private void initGUI() {
        catList.setListData(category);
        catList.setSelectedIndex(0);
        this.valueChanged(new ListSelectionEvent(catList, 0, 0, true));
        catList.addListSelectionListener(this);
        catList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        comList.addListSelectionListener(this);

        /*newShortcutTextField.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        curShortcutTextField.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        comList.setBackground(MainFrame.DEFAULT_BACK_GROUND);
        catList.setBackground(MainFrame.DEFAULT_BACK_GROUND);
         */
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == catList) {
            int index = catList.getSelectedIndex();

            if (index != -1) {
                comList.setListData(commands[index]);
                comList.setSelectedIndex(0);
                this.valueChanged(new ListSelectionEvent(comList, 0, 0, true));
            }
        } else if (e.getSource() == comList) {
            int index1 = catList.getSelectedIndex();
            int index2 = comList.getSelectedIndex();

            if ((index1 == -1) || (index2 == -1)) {
                curShortcutTextField.setText("");
                newShortcutTextField.setEditable(false);
                desLabel.setText("");

                this.setButton.setEnabled(false);
                this.resetButton.setEnabled(false);
                this.resetAllButton.setEnabled(false);
                this.noShortCutButton.setEnabled(false);

                return;
            }

            KeyStroke stroke = ((ShortcutsBuilder.KeyStrokeRecord)strokes[index1].elementAt(index2)).getNew();

            if (stroke == null) {
                JMenuItem o = (JMenuItem)menus[index1].elementAt(index2);
                stroke = o.getAccelerator();
            }

            if (stroke != null) {
                int keyCode = stroke.getKeyCode();
                int mod = stroke.getModifiers();
                String modStr = KeyEvent.getKeyModifiersText(mod);
                String keyStr = KeyEvent.getKeyText(keyCode);

                if (modStr.length() == 0) {
                    curShortcutTextField.setText(keyStr);
                } else {
                    curShortcutTextField.setText(modStr + "+" + keyStr);
                }

                this.noShortCutButton.setEnabled(true);

                //refresh GUI
                this.resetButton.setEnabled(true);
                this.resetAllButton.setEnabled(true);
                this.noShortCutButton.setEnabled(true);
            } else {
                curShortcutTextField.setText("");

                //refresh GUI
                this.resetButton.setEnabled(true);
                this.resetAllButton.setEnabled(true);
                this.noShortCutButton.setEnabled(false);
            }

            newShortcutTextField.setText("");
            newShortcutTextField.setEditable(true);
            this.desLabel.setText(((JMenuItem)menus[index1].elementAt(index2)).getToolTipText());
            curE = null;
            set = false;
        }
    }

    void newShortcutTextField_keyReleased(KeyEvent e) {
        if ((curE != null) && !set) {
            KeyStroke stroke = KeyStroke.getKeyStrokeForEvent(curE);
            int keyCode = stroke.getKeyCode();
            int mod = stroke.getModifiers();
            String modStr = KeyEvent.getKeyModifiersText(mod);
            String keyStr = KeyEvent.getKeyText(keyCode);

            if (modStr.length() == 0) {
                newShortcutTextField.setText(keyStr);
            } else {
                newShortcutTextField.setText(modStr + "+" + keyStr);
            }

            set = true;
            this.setButton.setEnabled(true);
        }
    }

    void newShortcutTextField_keyPressed(KeyEvent e) {
        curE = e;
        set = false;
    }

    protected void closeButton_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    protected void noShortCutButton_actionPerformed(ActionEvent e) {
        int index1 = catList.getSelectedIndex();
        int index2 = comList.getSelectedIndex();

        if ((index1 == -1) || (index2 == -1)) {
            return;
        }

        try {
            ((ShortcutsBuilder.KeyStrokeRecord)strokes[index1].elementAt(index2)).reset();
            ((JMenuItem)menus[index1].elementAt(index2)).setAccelerator(null);

            /**
             * refresh GUI
             */
            curShortcutTextField.setText("");
            this.resetButton.setEnabled(true);
            this.resetAllButton.setEnabled(true);
            this.noShortCutButton.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
