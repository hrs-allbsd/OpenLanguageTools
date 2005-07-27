/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;


public class JCommentDialog extends JDialog implements DocumentListener, MouseListener, KeyListener {
    private static final Logger logger = Logger.getLogger(JCommentDialog.class.getName());
    
    public static final int COMMENT_SEGMENT = 0;
    public static final int COMMENT_FILE = 1;
    private JPopupMenu jPopupMenuComment = new JPopupMenu();
    private JMenuItem jMenuItemClose = new JMenuItem();
    private JPanel jPanel = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jCommentPanel = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JScrollPane jScrollPane = new JScrollPane();
    private JTextPane jTextPaneComment = new JTextPane();
    private JPanel jButtonPanel = new JPanel();
    private JButton jButtonStamp = new JButton();
    private Border border1;
    private int type = -1;
    private boolean modified = false;
    private boolean inSetProcess = false;
    private JButton jButtonSave = new JButton();
    private JButton jButtonClose = new JButton();
    private String translatorId;
    private String comment;
    private String originalComment;

    private boolean needsSave;

    public JCommentDialog(Frame owner) throws HeadlessException {
        super(owner, "", true);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void setTranslatorId(String translatorId) {
        this.translatorId = translatorId;
    }
*/
    private void jbInit() throws Exception {
        border1 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(142, 142, 142)), "Comment"), BorderFactory.createEmptyBorder(0, 1, 1, 1));

        jMenuItemClose.setToolTipText("Close Comment");
        jMenuItemClose.setMnemonic('S');
        jMenuItemClose.setText("Close Comment");
        jMenuItemClose.addActionListener(new JCommentDialog_jMenuItemClose_actionAdapter(this));

        jButtonStamp.setText("Date Stamp");
        jButtonStamp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButtonStamp_actionPerformed(e);
                }
            });
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new JCommentDialog_jButtonSave_actionAdapter(this));
        jButtonClose.setActionCommand("jButtonClose");
        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new JCommentDialog_jButtonClose_actionAdapter(this));
        jPopupMenuComment.add(jMenuItemClose);

        this.getContentPane().add(jPanel, BorderLayout.CENTER);
        jPanel.setLayout(borderLayout1);
        jPanel.add(jCommentPanel, BorderLayout.CENTER);
        jPanel.add(jButtonPanel, BorderLayout.SOUTH);

        jCommentPanel.setLayout(borderLayout2);
        jCommentPanel.add(jScrollPane, BorderLayout.CENTER);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setBorder(border1);
        jScrollPane.getViewport().add(jTextPaneComment, null);

        jTextPaneComment.getDocument().addDocumentListener(this);
        jTextPaneComment.addKeyListener(this);
        jTextPaneComment.addMouseListener(this);

        jButtonPanel.add(jButtonStamp);
        jButtonPanel.add(jButtonSave, null);
        jButtonPanel.add(jButtonClose, null);
    }

    public void init(int type, String comment, int rowNo,String transId) {
        this.comment = comment;
        this.originalComment = comment;
        this.translatorId = transId;

        this.type = type;
        this.modified = false;
        jButtonSave.setEnabled(modified);

        inSetProcess = true;

        if (type == COMMENT_SEGMENT) {
            this.setTitle("Comment on segment no." + (rowNo + 1));
        } else if (type == COMMENT_FILE) {
            this.setTitle("Comment on File");
        }

        jTextPaneComment.setText((comment == null) ? "" : comment);

        inSetProcess = false;

        //needs save must be false before we do editting;
        needsSave = false;

        this.pack();
        this.setSize(400, 300);
        this.setLocationRelativeTo(MainFrame.getAnInstance());
        this.setVisible(true);

        
        /*
          String tmpComment = null;

        this.type = type;
        this.modified = false;
        jButtonSave.setEnabled(modified);

        inSetProcess = true;
        if(type == COMMENT_SEGMENT) {
          int currentrow = MainFrame.curRow;
          this.setTitle("Comment on segment no." + (currentrow+1));
          tmpComment = MainFrame.tmpdata.getCommentsTrack().getComment(MainFrame.tmpdata.tmsentences[currentrow].getTransUintID());
        }
        else if(type == COMMENT_FILE){
          this.setTitle("Comment on File");
          tmpComment = MainFrame.tmpdata.getCommentsTrack().getComment("header");
        }
        if(tmpComment == null)
          jTextPaneComment.setText("");
        else
          jTextPaneComment.setText(tmpComment);

        inSetProcess = false;

        this.pack();
        this.setSize(400,300);
        this.setLocationRelativeTo(MainFrame.getAnInstance());
        this.setVisible(true);
         */
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jButtonClose_actionPerformed(null);
        } else {
            super.processWindowEvent(e);
        }
    }

    private void writeBackTheComment() {
        //if (modified) {
            String str = jTextPaneComment.getText();
            comment = ((str == null) || (str.trim().length() == 0)) ? null : str;
            
            if(comment == null && originalComment == null)
                needsSave = false;
            else if(comment != null && originalComment != null)
                needsSave = ! comment.equals(originalComment);
            else
                needsSave = true;
            
            logger.finer("Orig comment:"+originalComment);
            logger.finer("New  comment:"+ comment);
            logger.finer("Needs save  :"+ needsSave);
        //}

        /*        if(modified) {
                    String key = null;
                    if(type == COMMENT_SEGMENT) {
                        key = MainFrame.tmpdata.tmsentences[MainFrame.curRow].getTransUintID();
                    } else if(type == COMMENT_FILE) {
                        key = "header";
                    }
                    String tmpComment = jTextPaneComment.getText();
                    if(tmpComment.length() == 0)
                        MainFrame.tmpdata.getCommentsTrack().setComment(key, null);
                    else
                        MainFrame.tmpdata.getCommentsTrack().setComment(key, tmpComment);
                    modified = false;

                    MainFrame.getAnInstance().setBHasModified(true);
                }
         */
    }

    public String getComment() {
        return comment;
    }

    void jMenuItemClose_actionPerformed(ActionEvent e) {
        jButtonClose_actionPerformed(null);
    }

    void jButtonStamp_actionPerformed(ActionEvent e) {
        StringBuffer sb = new StringBuffer(jTextPaneComment.getText());
        java.util.Calendar curDate = java.util.Calendar.getInstance();
        sb.append("\n" + translatorId + "  " + curDate.get(Calendar.YEAR) + "-" + curDate.get(Calendar.MONTH) + "-" + curDate.get(Calendar.DAY_OF_MONTH));

        jTextPaneComment.setText(sb.toString());
        jTextPaneComment.requestFocus();
    }

    void jButtonSave_actionPerformed(ActionEvent e) {
        writeBackTheComment();
        jButtonSave.setEnabled(modified);
    }

    void jButtonClose_actionPerformed(ActionEvent e) {
        if (modified) {
            Toolkit.getDefaultToolkit().beep();

            int iResult = JOptionPane.showConfirmDialog(this, "The comment has been changed. Do you wish to save it?", "Save Comment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (iResult == JOptionPane.YES_OPTION) {
                writeBackTheComment();
            }
        }

        this.dispose();
    }

    /**
     * Document Listener
     */
    public void insertUpdate(DocumentEvent e) {
        if (inSetProcess) {
            return;
        }

        modified = true;
        jButtonSave.setEnabled(true);
    }

    public void removeUpdate(DocumentEvent e) {
        if (inSetProcess) {
            return;
        }

        modified = true;
        jButtonSave.setEnabled(true);
    }

    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * Mouse Listener
     */
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        JTextComponent textComponent = (JTextComponent)(e.getSource());

        if ((e.getButton() == e.BUTTON2) || (e.getButton() == e.BUTTON3)) {
            jPopupMenuComment.show(this, e.getX(), e.getY());
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Key Listener
     */
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        JTextComponent textComponent = (JTextComponent)(e.getSource());

        if (e.getKeyCode() == KeyEvent.VK_CUT) {
            textComponent.cut();
        } else if (e.getKeyCode() == KeyEvent.VK_COPY) {
            textComponent.copy();
        } else if (e.getKeyCode() == KeyEvent.VK_PASTE) {
            textComponent.paste();
        } else if ((e.getKeyCode() == KeyEvent.VK_X) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            textComponent.cut();
        } else if ((e.getKeyCode() == KeyEvent.VK_C) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            textComponent.copy();
        } else if ((e.getKeyCode() == KeyEvent.VK_V) && (e.getModifiers() == KeyEvent.CTRL_MASK)) {
            textComponent.paste();
        }
    }
   
    boolean needsSave(){
        return needsSave;
    }
}


class JCommentDialog_jMenuItemClose_actionAdapter implements java.awt.event.ActionListener {
    private JCommentDialog adaptee;

    JCommentDialog_jMenuItemClose_actionAdapter(JCommentDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuItemClose_actionPerformed(e);
    }
}


class JCommentDialog_jButtonSave_actionAdapter implements java.awt.event.ActionListener {
    private JCommentDialog adaptee;

    JCommentDialog_jButtonSave_actionAdapter(JCommentDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonSave_actionPerformed(e);
    }
}


class JCommentDialog_jButtonClose_actionAdapter implements java.awt.event.ActionListener {
    private JCommentDialog adaptee;

    JCommentDialog_jButtonClose_actionAdapter(JCommentDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonClose_actionPerformed(e);
    }
}
