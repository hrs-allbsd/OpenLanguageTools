/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


public class CustomKeyboard extends JDialog implements ListSelectionListener {
    //extends DefaultCustomKeyboard {
    private static final Logger logger = Logger.getLogger(CustomKeyboard.class.getName());
    static HashMap shortCutmap = new HashMap();

    static {
        shortCutmap.put("Open", "130 79");
        shortCutmap.put("Save", "130 83");
        shortCutmap.put("Close", "520 67");
        shortCutmap.put("Print...", "130 80");
        shortCutmap.put("Exit", "520 88");
        shortCutmap.put("Undo", "130 90");
        shortCutmap.put("Redo", "130 89");
        shortCutmap.put("Cut", "130 88");
        shortCutmap.put("Copy", "130 67");
        shortCutmap.put("Paste", "130 86");
        shortCutmap.put("Transfer", "130 70");
        shortCutmap.put("Copy Source", "520 92");
        shortCutmap.put("Copy Source Tags", "520 52");
        shortCutmap.put("Clear Target", "520 127");
        shortCutmap.put("Mark Segment As->Translated", "520 49");
        shortCutmap.put("Mark Segment As->Verified", "0 116");
        shortCutmap.put("Mark Segment As->Untranslated", "0 120");
        shortCutmap.put("Confirm and Translate Next", "520 61");
        shortCutmap.put("Confirm and Verify Next", "520 116");
        shortCutmap.put("Search/Replace", "0 115");
        shortCutmap.put("Next Segment", "130 40");
        shortCutmap.put("Previous Segment", "130 38");
        shortCutmap.put("Go To Top", "130 36");
        shortCutmap.put("Go To Bottom", "130 35");
        shortCutmap.put("Page Up", "0 33");
        shortCutmap.put("Page Down", "0 34");
        shortCutmap.put("Go To Segment...", "130 71");
        shortCutmap.put("Tag Verification", "130 84");
        shortCutmap.put("Spell Checking", "0 118");
        shortCutmap.put("Update Mini-TM", "130 85");
        shortCutmap.put("Maintain Mini-TM", "130 77");
        shortCutmap.put("Tag Protection", "520 80");
    }

    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    Border border1;
    TitledBorder titledBorder1;
    JLabel desLabel = new JLabel();
    JLabel categoryLabel = new JLabel();
    JLabel commandLabel = new JLabel();
    JLabel newShortCutLabel = new JLabel();
    JLabel curShortCutLabel = new JLabel();
    JPanel leftPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(10, 10, 10, 5);
        }
    };

    JPanel rightPanel = new JPanel();
    JPanel centerPanel = new JPanel();
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

    public CustomKeyboard(JFrame parent, ShortcutsBuilder builder) {
        super(parent);

        this.parent = parent;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setSize(460, 360);
        this.category = builder.getCategories();
        this.commands = builder.getCommands();
        this.menus = builder.getMenus();
        this.strokes = builder.getStrokes();

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
            for (int i = 0; i < strokes.length; i++) {
                for (int j = 0; j < strokes[i].size(); j++) {
                    ((JMenuItem)menus[i].elementAt(j)).setAccelerator(null);

                    String strShortCut = (String)this.shortCutmap.get(((JMenuItem)menus[i].elementAt(j)).getText());

                    if (strShortCut != null) {
                        int iMode = Integer.parseInt(strShortCut.substring(0, strShortCut.indexOf(" ")));
                        int iKeyCode = Integer.parseInt(strShortCut.substring(strShortCut.indexOf(" ") + 1, strShortCut.length()));
                        KeyStroke newKeyStroke = KeyStroke.getKeyStroke(iKeyCode, iMode);
                        ((JMenuItem)menus[i].elementAt(j)).setAccelerator(newKeyStroke);
                    }

                    ((ShortcutsBuilder.KeyStrokeRecord)strokes[i].get(j)).reset();
                }
            }

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

            String strShortCut = (String)this.shortCutmap.get(((JMenuItem)menus[index1].elementAt(index2)).getText());

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
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142));
        titledBorder1 = new TitledBorder(border1, "Description");
        desLabel.setBorder(titledBorder1);
        desLabel.setBounds(new Rectangle(6, 259, 308, 45));

        categoryLabel.setBounds(new Rectangle(6, 15, 144, 24));
        categoryLabel.setText("Menus:");
        categoryLabel.setToolTipText("");
        categoryLabel.setForeground(Color.black);

        commandLabel.setBounds(new Rectangle(164, 15, 144, 24));
        commandLabel.setText("Menu Options:");
        commandLabel.setToolTipText("");
        commandLabel.setForeground(Color.black);

        JScrollPane scr1 = new JScrollPane(catList);
        scr1.setBounds(new Rectangle(6, 43, 144, 153));

        JScrollPane scr2 = new JScrollPane(comList);
        scr2.setBounds(new Rectangle(164, 43, 144, 153));

        newShortCutLabel.setBounds(new Rectangle(6, 199, 144, 25));
        newShortCutLabel.setText("New shortcut:");
        newShortCutLabel.setToolTipText("");
        newShortCutLabel.setForeground(Color.black);

        curShortCutLabel.setBounds(new Rectangle(164, 199, 144, 25));
        curShortCutLabel.setText("Current shortcut:");
        curShortCutLabel.setToolTipText("");
        curShortCutLabel.setForeground(Color.black);

        newShortcutTextField.setEditable(false);
        newShortcutTextField.setBounds(new Rectangle(6, 226, 144, 22));
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

        curShortcutTextField.setBounds(new Rectangle(164, 226, 144, 22));
        curShortcutTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    e.consume();
                }
            });

        closeButton.setText("Close");
        closeButton.setBounds(new Rectangle(11, 223, 97, 27));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    closeButton_actionPerformed(e);
                }
            });

        setButton.setText("Set");
        setButton.setBounds(new Rectangle(11, 42, 97, 27));
        setButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setButton_actionPerformed(e);
                }
            });

        resetButton.setText("Reset");
        resetButton.setBounds(new Rectangle(11, 79, 97, 27));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetButton_actionPerformed(e);
                }
            });

        resetAllButton.setText("Reset All...");
        resetAllButton.setBounds(new Rectangle(11, 115, 97, 27));
        resetAllButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetAllButton_actionPerformed(e);
                }
            });

        noShortCutButton.setMargin(new Insets(2, 10, 2, 10));
        noShortCutButton.setText("No Shortcut");
        noShortCutButton.setBounds(new Rectangle(11, 151, 97, 27));
        noShortCutButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    noShortCutButton_actionPerformed(e);
                }
            });

        rightPanel.setLayout(null);
        rightPanel.setMinimumSize(new Dimension(50, 300));
        rightPanel.setPreferredSize(new Dimension(120, 300));
        rightPanel.add(setButton, null);
        rightPanel.add(closeButton, null);
        rightPanel.add(resetAllButton, null);
        rightPanel.add(resetButton, null);
        rightPanel.add(noShortCutButton, null);

        centerPanel.setLayout(null);
        centerPanel.add(desLabel, null);
        centerPanel.add(scr1, null);
        centerPanel.add(scr2, null);
        centerPanel.add(curShortCutLabel, null);
        centerPanel.add(commandLabel, null);
        centerPanel.add(newShortcutTextField, null);
        centerPanel.add(newShortCutLabel, null);
        centerPanel.add(curShortcutTextField, null);
        centerPanel.add(categoryLabel, null);

        leftPanel.setLayout(borderLayout2);
        leftPanel.add(centerPanel, BorderLayout.CENTER);

        this.getContentPane().setLayout(borderLayout1);
        this.getContentPane().add(leftPanel, BorderLayout.CENTER);
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
