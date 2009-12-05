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
import java.util.HashMap;
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
    private Map defaultMap;
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
        this.defaultMap = new HashMap();
        this.defaultMap.put("Approve_and_go_to_Next_Translated", "130 10");
        this.defaultMap.put("Close", "130 87");
        this.defaultMap.put("Comment_on_Segment" + "->" + "Add_Comment", "650 78");
        this.defaultMap.put("Comment_on_Segment" + "->" + "Delete_Comment", "650 77");
        this.defaultMap.put("Comment_on_Segment" + "->" + "Edit_Comment", "650 66");
        this.defaultMap.put("Confirm_and_Translate_Next", "520 10");
        this.defaultMap.put("Copy", "130 67");
        this.defaultMap.put("Copy_Source", "520 155");
        this.defaultMap.put("Copy_Source_Tags", "585 155");
        this.defaultMap.put("Cut", "130 88");
        this.defaultMap.put("Exit", "130 81");
        this.defaultMap.put("Go_To_Bottom", "130 35");
        this.defaultMap.put("Go_To_Segment...", "130 71");
        this.defaultMap.put("Go_To_Top", "130 36");
        this.defaultMap.put("Maintain_Mini-TM", "130 77");
        this.defaultMap.put("Mark_Segment_As" + "->" + "Approved", "520 50");
        this.defaultMap.put("Mark_Segment_As" + "->" + "Rejected", "520 52");
        this.defaultMap.put("Mark_Segment_As" + "->" + "Translated", "520 49");
        this.defaultMap.put("Mark_Segment_As" + "->" + "Untranslated", "520 51");
        this.defaultMap.put("Merge_Mini-TMs", "195 77");
        this.defaultMap.put("New_Project...", "130 78");
        this.defaultMap.put("Next_Segment", "130 40");
        this.defaultMap.put("Open", "130 79");
        this.defaultMap.put("Page_Down", "0 34");
        this.defaultMap.put("Page_Up", "0 33");
        this.defaultMap.put("Paste", "130 86");
        this.defaultMap.put("Previous_Segment", "130 38");
        this.defaultMap.put("Print_Options...", "195 80");
        this.defaultMap.put("Print...", "130 80");
        this.defaultMap.put("Redo", "130 89");
        this.defaultMap.put("Reject_and_go_to_Next_Translated", "130 45");
        this.defaultMap.put("Save", "130 83");
        this.defaultMap.put("Save_As...", "195 83");
        this.defaultMap.put("Save_Mini-TM", "585 83");
        this.defaultMap.put("Search_Mini-TM", "520 81");
        this.defaultMap.put("Search/Replace", "0 114");
        this.defaultMap.put("Source_Context", "520 83");
        this.defaultMap.put("Spell_Checking", "0 118");
        this.defaultMap.put("Tag_Protection", "520 80");
        this.defaultMap.put("Tag_Verification", "130 84");
        this.defaultMap.put("Transfer", "130 70");
        this.defaultMap.put("Undo", "130 90");
        this.defaultMap.put("Untransfer", "195 70");
        this.defaultMap.put("Update_Mini-TM", "130 85");

        this.menuMap = menuMap;
        if (this.menuMap.isEmpty() ||
            this.menuMap.containsKey("Page Up"))
            // if we find a "Page Up" key, this is a 1.2.7 config which needs to be replaced
            setDefaults();
        this.menuBar = menuBar;
    }

    /** Gets a map of default shortcuts */
    public void setDefaults() {

        this.menuMap.putAll(this.defaultMap);
  }

    public Map getDefaultMap() {

        return this.defaultMap;
  }

    private void insertCommands(JMenu menu, Vector v, Vector mV, Vector stroke, String parentName, String parentText ) {
        int menuCount = menu.getMenuComponentCount();

        for (int j = 0; j < menuCount; j++) {
            Component o = menu.getMenuComponent(j);

            if (o instanceof JMenu) {
                JMenu xmenu = (JMenu)o;
                String name = xmenu.getName();
                String text = xmenu.getText();
                insertCommands(xmenu, v, mV, stroke, name, text);
            } else if (o instanceof JMenuItem) {
                String name = ((JMenuItem)o).getName();
                String text = ((JMenuItem)o).getText();

                if ((text.charAt(0) == '/') || (text.indexOf(":\\") != -1)) {
                    continue;
                } else {
                    String tempText = text;
                    if (parentText != null) {
                        tempText = parentText + "->" + tempText;
                    }
                    v.addElement(tempText);

                    String tempName = name;
                    if (parentName != null) {
                        tempName = parentName + "->" + tempName;
                    }
                    String shortcut = getShortcut(tempName);

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
            String text = menu.getText();
            category[i] = text;

            commands[i] = new Vector();
            menus[i] = new Vector();
            strokes[i] = new Vector();

            insertCommands(menu, commands[i], menus[i], strokes[i], null, null);
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

    private void saveCommands(JMenu menu, Vector v, Vector mV, Vector stroke, String parent ) {
        int menuCount = menu.getMenuComponentCount();

        for (int j = 0; j < menuCount; j++) {
            Component o = menu.getMenuComponent(j);

            if (o instanceof JMenu) {
                saveCommands((JMenu)o, v, mV, stroke, ((JMenu)o).getName());
            } else if (o instanceof JMenuItem) {
                JMenuItem item  = (JMenuItem)o;
                String text = item.getName();
                if(text == null)
                    text = item.getText();
                //String text = ((JMenuItem)o).getText();

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
