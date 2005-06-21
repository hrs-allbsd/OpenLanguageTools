/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
/*
 * ShortcutsBuilder.java
 *
 * Created on June 10, 2005, 4:25 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.jvnet.olt.editor.translation;

import java.awt.Component;
import java.awt.event.KeyEvent;

import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 *
 * @author boris
 */
public class ShortcutsBuilder {
    private static final Logger logger = Logger.getLogger(ShortcutsBuilder.class.getName());
    private static ShortcutsBuilder instance;
    private String[] category = null;
    private Vector[] commands = null;
    private Vector[] menus = null;
    private Vector[] strokes = null;
    private Map menuMap;
    private JMenuBar menuBar;

    static class KeyStrokeRecord {
        KeyStroke oldStroke;
        KeyStroke newStroke;

        public KeyStrokeRecord(KeyStroke o) {
            oldStroke = o;
            newStroke = null;
        }

        public void setNew(KeyStroke n) {
            newStroke = n;
        }

        public KeyStroke getNew() {
            return newStroke;
        }

        public KeyStroke getOld() {
            return oldStroke;
        }

        public void reset() {
            newStroke = null;
        }
    }

    /** Creates a new instance of ShortcutsBuilder */
    public ShortcutsBuilder(Map menuMap, JMenuBar menuBar) {
        synchronized (ShortcutsBuilder.class) {
            if (instance != null) {
                throw new RuntimeException("ShortcutsBuilder has already been constructed");
            }

            instance = this;
        }

        this.menuMap = menuMap;
        this.menuBar = menuBar;
    }

    private void insertCommands(JMenu menu, Vector v, Vector mV, Vector stroke, String parent) {
        int menuCount = menu.getMenuComponentCount();

        for (int j = 0; j < menuCount; j++) {
            Component o = menu.getMenuComponent(j);

            if (o instanceof JMenu) {
                insertCommands((JMenu)o, v, mV, stroke, ((JMenu)o).getText());
            } else if (o instanceof JMenuItem) {
                String text = ((JMenuItem)o).getText();

                if ((text.charAt(0) == '/') || (text.indexOf(":\\") != -1)) {
                    continue;
                } else {
                    String temp = text;

                    if (parent != null) {
                        temp = parent + "->" + temp;
                    }

                    v.addElement(temp);

                    String shortcut = getShortcut(temp);

                    if ((shortcut != null) && (shortcut.length() != 0)) {
                        int iMode = Integer.parseInt(shortcut.substring(0, shortcut.indexOf(" ")));
                        int iKeyCode = Integer.parseInt(shortcut.substring(shortcut.indexOf(" ") + 1, shortcut.length()));
                        String strShortCut = KeyEvent.getKeyModifiersText(iMode);
                        strShortCut += "-";
                        strShortCut += KeyEvent.getKeyText(iKeyCode);

                        KeyStroke s = KeyStroke.getKeyStroke(iKeyCode, iMode);

                        if (s != null) {
                            ((JMenuItem)o).setAccelerator(s);
                        }
                    }

                    mV.addElement(o);
                    stroke.addElement(new KeyStrokeRecord(((JMenuItem)o).getAccelerator()));
                }
            }
        }
    }

    public void parseMenu() {
        int count = menuBar.getMenuCount();
        category = new String[count];
        commands = new Vector[count];
        menus = new Vector[count];
        strokes = new Vector[count];

        for (int i = 0; i < count; i++) {
            JMenu menu = menuBar.getMenu(i);
            category[i] = menu.getText();

            commands[i] = new Vector();
            menus[i] = new Vector();
            strokes[i] = new Vector();

            insertCommands(menu, commands[i], menus[i], strokes[i], null);
        }
    }

    public Map extractShortcuts() {
        int count = menuBar.getMenuCount();

        for (int i = 0; i < count; i++) {
            JMenu menu = menuBar.getMenu(i);
            saveCommands(menu, commands[i], menus[i], strokes[i], null);
        }

        return menuMap;
    }

    private void saveCommands(JMenu menu, Vector v, Vector mV, Vector stroke, String parent) {
        int menuCount = menu.getMenuComponentCount();

        for (int j = 0; j < menuCount; j++) {
            Component o = menu.getMenuComponent(j);

            if (o instanceof JMenu) {
                saveCommands((JMenu)o, v, mV, stroke, ((JMenu)o).getText());
            } else if (o instanceof JMenuItem) {
                String text = ((JMenuItem)o).getText();

                if ((text.charAt(0) == '/') || (text.indexOf(":\\") != -1)) {
                    continue;
                } else {
                    String temp = text;

                    if (parent != null) {
                        temp = parent + "->" + temp;
                    }

                    KeyStroke strokeTemp = ((JMenuItem)o).getAccelerator();

                    if (strokeTemp == null) {
                        setShortcut(temp, "");
                    } else {
                        //logger.finer(strokeTemp.toString());
                        String tt = "";
                        int mod = strokeTemp.getModifiers();
                        int keycode = strokeTemp.getKeyCode();
                        tt = Integer.toString(mod) + " " + Integer.toString(keycode);
                        setShortcut(temp, tt);
                    }
                }
            }
        }
    }

    private String getShortcut(String menuName) {
        return (String)menuMap.get(menuName);
    }

    private void setShortcut(String menuName, String stroke) {
        menuMap.put(menuName, stroke);
    }

    public String[] getCategories() {
        return category;
    }

    public Vector[] getCommands() {
        return commands;
    }

    public Vector[] getMenus() {
        return menus;
    }

    public Vector[] getStrokes() {
        return strokes;
    }

    // bug 4744603 -------------------------------------------------------------------
    //add by cl141268 get shortcuts keystroke
    public static KeyStroke getMenuItemShortcut(String menuName, String commandName) {
        return instance.internalGetMenuItemShortcut(menuName, commandName);
    }

    private KeyStroke internalGetMenuItemShortcut(String menuName, String commandName) {
        int i = 0;
        int j = 0;

        for (i = 0; i < category.length; i++) {
            //logger.finer(category[i]);
            if ((category[i]).toLowerCase().equals(menuName.toLowerCase())) {
                break;
            }
        }

        if (i < category.length) {
            Vector menuV = menus[i];
            Vector cmds = commands[i];

            for (j = 0; j < cmds.size(); j++) {
                //logger.finer(cmds.get(j));
                if (cmds.get(j).toString().toLowerCase().equals(commandName.toLowerCase())) {
                    break;
                }
            }

            if (j < cmds.size()) {
                JMenuItem item = (JMenuItem)menuV.get(j);

                return item.getAccelerator();
            }
        }

        return null;
    }
}
