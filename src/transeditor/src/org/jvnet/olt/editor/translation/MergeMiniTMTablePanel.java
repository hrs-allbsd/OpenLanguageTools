/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation;

import java.awt.*;
import java.awt.event.*;

import java.io.File;

import org.jvnet.olt.editor.util.Bundle;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.*;


public class MergeMiniTMTablePanel extends JPanel {
    private static final Logger logger = Logger.getLogger(MergeMiniTMTablePanel.class.getName());
    public static final int MANUAL_MERGE = 0;
    public static final int SEMI_AUTO_MERGE = 1;
    public static final int AUTO_MERGE = 2;
    public static Font dlgFont = new Font("Dialog", Font.BOLD, 12);
    public static String lanPair = null;
    BorderLayout borderLayout1 = new BorderLayout();
    MiniTMTableAlignment tmTableAlignment = new MiniTMTableAlignment(this, 0);
    MiniTMTableAlignment idTableAlignment = new MiniTMTableAlignment(this, 1);
    JPanel actionPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(10, 5, 5, 10);
        }
    };

    private Bundle bundle = Bundle.getBundle(MergeMiniTMTablePanel.class.getName());

    JPanel idactionPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(10, 5, 5, 10);
        }
    };

    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel selectPanel = new JPanel();
    JPanel orderPanel = new JPanel();
    JButton moveDownButton = new JButton();
    JButton moveUpButton = new JButton();
    JButton addButton = new JButton();
    JButton removeButton = new JButton();
    JPanel bottomActionPanel = new JPanel() {
        public Insets getInsets() {
            return new Insets(5, 5, 5, 5);
        }
    };

    JButton cancelButton = new JButton();
    JButton startButton = new JButton();
    FlowLayout flowLayout1 = new FlowLayout();
    JPanel optionPanel = new JPanel();
    JRadioButton manualMerge = new JRadioButton();
    JRadioButton semiautoMerge = new JRadioButton();
    JRadioButton autoMerge = new JRadioButton();
    ButtonGroup optionGroup = new ButtonGroup();

    //JCheckBox viewIDCheckBox = new JCheckBox();
    //JCheckBox viewDupCheckBox = new JCheckBox();
    JButton backButton = new JButton();
    JDialog dlg;
    Container parent;
    CardLayout cards;

    //boolean useTranslatorId = false;
    //boolean viewDuplicates = false;
    //boolean manual = true;
    JPanel centerPanel = new JPanel();
    JPanel panel = new JPanel();
    JPanel minitmsPanel = new JPanel();
    JPanel idsPanel = new JPanel();
    int mergeType = MANUAL_MERGE;

    //JPanel optionPanel = new JPanel();
    private java.util.List projectHistory;
    private String miniTmDir;
    SelectSegmentAlignment differentTranslation = new SelectSegmentAlignment() {
        Dimension size = new Dimension(500, 400);

        public Dimension getPreferredSize() {
            return size;
        }
    };

    JDialog selectDialog = null;
    int indexOfSelect = -1;
    JFileChooser fileChooser = new JFileChooser(System.getProperty("editor_home") + File.separator + "mini-tm");
    java.util.Vector miniTMs = new java.util.Vector();
    java.util.Vector files = new java.util.Vector();
    FlowLayout flowLayout2 = new FlowLayout();
    JRadioButton jRadioButton1 = new JRadioButton();
    JRadioButton jRadioButton2 = new JRadioButton();
    JRadioButton jRadioButton3 = new JRadioButton();

    class CancelMergingException extends Exception {
        public CancelMergingException() {
            super("Stop Merging Exception");
        }
    }

    class MiniTMOpenFilter extends javax.swing.filechooser.FileFilter {
        String tmExtension = "MTM";

        public boolean accept(File f) {
            if (f != null) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = getExtension(f);

                if ((extension != null) && extension.equals(tmExtension)) {
                    if (files.contains(f.getAbsolutePath())) {
                        return false;
                    }

                    StringTokenizer tokens = new StringTokenizer(f.getName(), "_", false);
                    int count = tokens.countTokens();

                    if (count == 3) {
                        return f.getName().endsWith(lanPair);
                    }
                }
            }

            return false;
        }

        public String getExtension(File f) {
            if (f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');

                if ((i > 0) && (i < (filename.length() - 1))) {
                    return filename.substring(i + 1);
                }

                ;
            }

            return null;
        }

        public String getDescription() {
            return bundle.getString("Mini-TM_Files");
        }
    }

    //TODO Do NOT pass the backend down here. Pass only the list of projects
    public MergeMiniTMTablePanel(JDialog dlg, Container parent, CardLayout cards, String miniTmDir, java.util.List projectHistory) {
        this(miniTmDir, projectHistory);

        this.dlg = dlg;
        this.parent = parent;
        this.cards = cards;
    }

    public MergeMiniTMTablePanel(String miniTmDir, java.util.List projectHistory) {
        this.projectHistory = projectHistory;
        this.miniTmDir = miniTmDir;

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        actionPanel.setLayout(borderLayout2);
        idactionPanel.setLayout(borderLayout3);
        moveDownButton.setMaximumSize(new Dimension(83, 27));
        moveDownButton.setMinimumSize(new Dimension(83, 27));
        moveDownButton.setPreferredSize(new Dimension(83, 27));
        moveDownButton.setText(bundle.getString("Move_Down"));
        moveDownButton.setBounds(new Rectangle(13, 52, 106, 27));
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    moveDownButton_actionPerformed(e);
                }
            });
        moveUpButton.setMaximumSize(new Dimension(83, 27));
        moveUpButton.setMinimumSize(new Dimension(83, 27));
        moveUpButton.setPreferredSize(new Dimension(83, 27));
        moveUpButton.setText(bundle.getString("Move_Up"));
        moveUpButton.setBounds(new Rectangle(13, 12, 106, 27));
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    moveUpButton_actionPerformed(e);
                }
            });
        orderPanel.setLayout(null);
        selectPanel.setLayout(null);
        addButton.setText(bundle.getString("Add..."));
        addButton.setBounds(new Rectangle(10, 10, 109, 28));
        addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addButton_actionPerformed(e);
                }
            });
        removeButton.setText(bundle.getString("Remove"));
        removeButton.setBounds(new Rectangle(10, 51, 109, 28));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeButton_actionPerformed(e);
                }
            });
        selectPanel.setMinimumSize(new Dimension(101, 69));
        selectPanel.setPreferredSize(new Dimension(101, 80));
        orderPanel.setMinimumSize(new Dimension(101, 80));
        orderPanel.setPreferredSize(new Dimension(101, 80));
        cancelButton.setMnemonic('C');
        cancelButton.setText(bundle.getString("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        startButton.setMnemonic('S');
        startButton.setText(bundle.getString("Start"));
        startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startButton_actionPerformed(e);
                }
            });
        bottomActionPanel.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        flowLayout1.setHgap(15);
        optionPanel.setLayout(flowLayout2);

        //viewIDCheckBox.setSelected(true);
        //viewIDCheckBox.addItemListener(this);
        //viewIDCheckBox.setText("Use Tanslator IDs");
        //viewDupCheckBox.setText("View Duplicates");
        //viewDupCheckBox.addItemListener(this);
        optionPanel.setPreferredSize(new Dimension(95, 40));
        actionPanel.setPreferredSize(new Dimension(140, 240));
        idactionPanel.setPreferredSize(new Dimension(140, 240));
        borderLayout2.setVgap(20);
        backButton.setMnemonic('B');
        backButton.setText(bundle.getString("Back"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    backButton_actionPerformed(e);
                }
            });
        minitmsPanel.setLayout(new BorderLayout());
        jRadioButton1.setText("jRadioButton1");
        jRadioButton2.setText("jRadioButton2");
        jRadioButton3.setText("jRadioButton3");
        manualMerge.setText(bundle.getString("Manual"));
        manualMerge.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    manualMerge_actionPerformed(e);
                }
            });
        semiautoMerge.setText(bundle.getString("Semi-automatic"));
        semiautoMerge.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    semiautoMerge_actionPerformed(e);
                }
            });
        autoMerge.setText(bundle.getString("Automatic"));
        autoMerge.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    autoMerge_actionPerformed(e);
                }
            });
        flowLayout2.setHgap(30);
        minitmsPanel.add(tmTableAlignment, BorderLayout.CENTER);
        minitmsPanel.add(actionPanel, BorderLayout.EAST);

        idsPanel.setLayout(new BorderLayout());
        idsPanel.add(idTableAlignment, BorderLayout.CENTER);
        idsPanel.add(idactionPanel, BorderLayout.EAST);

        panel.setLayout(new GridLayout(2, 1));
        panel.add(minitmsPanel);
        panel.add(idsPanel);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(optionPanel, BorderLayout.SOUTH);
        optionPanel.add(manualMerge, null);
        manualMerge.setSelected(true);
        optionPanel.add(semiautoMerge, null);
        optionPanel.add(autoMerge, null);
        optionGroup.add(manualMerge);
        optionGroup.add(semiautoMerge);
        optionGroup.add(autoMerge);

        this.add(centerPanel, BorderLayout.CENTER);
        actionPanel.add(selectPanel, BorderLayout.CENTER);
        idactionPanel.add(orderPanel, BorderLayout.CENTER);
        selectPanel.add(addButton, null);
        selectPanel.add(removeButton, null);

        //actionPanel.add(orderPanel, BorderLayout.CENTER);
        orderPanel.add(moveUpButton, null);
        orderPanel.add(moveDownButton, null);

        //actionPanel.add(optionPanel, BorderLayout.SOUTH);
        //optionPanel.add(viewIDCheckBox, null);
        //optionPanel.add(viewDupCheckBox, null);
        this.add(bottomActionPanel, BorderLayout.SOUTH);
        bottomActionPanel.add(backButton, null);
        bottomActionPanel.add(startButton, null);
        bottomActionPanel.add(cancelButton, null);
    }

    /*public void itemStateChanged(ItemEvent e) {
      if(e.getSource() == viewIDCheckBox) {
        boolean flag = viewIDCheckBox.isSelected();
        useTranslatorId = flag;
        manual = !flag;
        tmTableAlignment.isShowID = flag;
        tmTableAlignment.repaintSelf();
      }else if(e.getSource() == viewDupCheckBox){
        viewDuplicates = viewDupCheckBox.isSelected();
      }
    }*/
    void backButton_actionPerformed(ActionEvent e) {
        cards.show(parent, "first");
    }

    private int getIndexOfID(String id) {
        int index = -1;

        if (idTableAlignment.data != null) {
            for (int i = 0; i < idTableAlignment.data.length; i++) {
                if (id.equals(idTableAlignment.data[i])) {
                    index = i;

                    break;
                }
            }
        }

        return index;
    }

    void startButton_actionPerformed(ActionEvent e) {
        if (miniTMs.size() == 0) {
            return;
        }

        org.jvnet.olt.minitm.MiniTM destMiniTM = null;
        org.jvnet.olt.minitm.MiniTM[] srcMiniTMs = new org.jvnet.olt.fuzzy.basicsearch.BasicFuzzySearchMiniTM[miniTMs.size()];

        for (int i = 0; i < miniTMs.size(); i++) {
            srcMiniTMs[i] = (org.jvnet.olt.minitm.MiniTM)miniTMs.elementAt(i);
        }

        org.jvnet.olt.minitm.AlignedSegment[] segs = null;
        java.util.Hashtable flags = new java.util.Hashtable(); //1--to be added  ;0 --- unknown   -1--- to be deleted
        java.util.Vector unmergedSegments = new java.util.Vector();

        org.jvnet.olt.minitm.MiniTM tempSrc = null;

        try {
            destMiniTM = ((MergeMiniTMPanel)dlg).project.getMiniTM();

            int numOfTmfiles = files.size();

            if (numOfTmfiles == 0) {
                destMiniTM.saveMiniTmToFile();

                return;
            }

            String temp = null;

            //merge to one big Mini-tm
            File file1 = new File(miniTmDir + "tmp_" + destMiniTM.getSourceLang() + "_" + destMiniTM.getTargetLang());

            if (file1.exists()) {
                file1.delete();
            }

            tempSrc = new org.jvnet.olt.fuzzy.basicsearch.BasicFuzzySearchMiniTM(miniTmDir + "tmp_" + destMiniTM.getSourceLang() + "_" + destMiniTM.getTargetLang(), true, "tmp", destMiniTM.getSourceLang(), destMiniTM.getTargetLang());

            org.jvnet.olt.minitm.AlignedSegment[] segs1 = null;

            for (int j = 0; j < srcMiniTMs.length; j++) {
                segs1 = srcMiniTMs[j].getAllSegments();

                for (int k = 0; k < segs1.length; k++) {
                    tempSrc.addNewSegment(segs1[k]);

                    //logger.finer.println("add new Segment...");
                }

                segs1 = null;
                srcMiniTMs[j] = null;
            }

            segs = tempSrc.getAllSegments();

            for (int i = 0; i < segs.length; i++) {
                org.jvnet.olt.minitm.AlignedSegment seg = segs[i];
                long id = seg.getDataStoreKey();

                //logger.finer.println("**************************************************************************************");
                //        logger.finer.println("ld = "+id);
                if (flags.containsKey(String.valueOf(id))) {
                    continue;
                }

                //logger.finer.println("seg = "+seg.getSource());
                //        logger.finer.println("id = "+id);
                org.jvnet.olt.minitm.TMMatch[] matchs = tempSrc.getMatchFor(seg.getSource(), 100, Integer.MAX_VALUE);

                // for(int j=0;j<matchs.length;j++)
                // {
                //   logger.finer.println("matchs["+j+"]="+matchs[j].getSource());
                // }
                // logger.finer.println("**************************************************************************************");
                //        logger.finer.println("matchs.length = "+matchs.length);
                if (matchs.length == 0) {
                    continue;
                } else if (matchs.length == 1) {
                    flags.put(String.valueOf(id), new Integer(1)); //to be added
                } else {
                    String translation = matchs[0].getTranslation().toString();

                    String translator = matchs[0].getTranslatorID().toString();

                    int topIDIndex = getIndexOfID(translator);
                    int indexInMatch = 0;

                    boolean sameTrans = true;
                    boolean sameTranslator = true;

                    for (int j = 1; j < matchs.length; j++) {
                        if (translation.equals(matchs[j].getTranslation().toString())) { //same translation

                            if (translator.equals(matchs[j].getTranslatorID().toString())) { //same translator

                                continue;
                            } else {
                                sameTranslator = false;

                                int index = getIndexOfID(matchs[j].getTranslatorID().toString());

                                if ((index != -1) && (index < topIDIndex)) {
                                    topIDIndex = index;
                                    indexInMatch = j;
                                }
                            }
                        } else {
                            sameTrans = false;

                            if (translator.equals(matchs[j].getTranslatorID().toString())) { //same translator

                                continue;
                            } else {
                                sameTranslator = false;

                                int index = getIndexOfID(matchs[j].getTranslatorID().toString());

                                if ((index != -1) && (index < topIDIndex)) {
                                    topIDIndex = index;
                                    indexInMatch = j;
                                }
                            }
                        }
                    }

                    if (mergeType == this.MANUAL_MERGE) {
                        if (sameTrans) {
                            flags.put(String.valueOf(id), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (id1 == id) {
                                    continue;
                                }

                                flags.put(String.valueOf(id1), new Integer(-1));
                            }
                        } else {
                            Toolkit.getDefaultToolkit().beep();

                            int oldNum = differentTranslation.tableView.getRowCount();
                            SelectSegmentAlignment.data = matchs;
                            differentTranslation.repaintSelf(oldNum);

                            if (selectDialog == null) {
                                selectDialog = new JDialog(dlg, bundle.getString("Select_one_translation"), true) {
                                            protected void processWindowEvent(WindowEvent e) {
                                                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                    Component[] children = selectDialog.getContentPane().getComponents();
                                                    ((JButton)((JPanel)children[1]).getComponents()[1]).doClick();

                                                    return;
                                                }
                                            }
                                        };
                                selectDialog.getContentPane().setLayout(new BorderLayout());
                                selectDialog.getContentPane().add(differentTranslation, BorderLayout.CENTER);

                                JButton addSegmentButton = new JButton(bundle.getString("Add"));
                                JButton cancelSegmentButton = new JButton(bundle.getString("Cancel"));
                                JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                p.add(addSegmentButton);
                                p.add(cancelSegmentButton);
                                addSegmentButton.setBounds(10, 10, 40, 25);
                                addSegmentButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            int index1 = differentTranslation.tableView.getSelectedRow();

                                            if (index1 == -1) {
                                                return;
                                            }

                                            indexOfSelect = index1;

                                            selectDialog.setVisible(false);
                                        }
                                    });
                                cancelSegmentButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            indexOfSelect = -1;
                                            selectDialog.setVisible(false);
                                        }
                                    });

                                selectDialog.getContentPane().add(p, BorderLayout.SOUTH);
                                selectDialog.setSize(600, 400);
                                selectDialog.setLocationRelativeTo(dlg);
                                selectDialog.setVisible(true);
                            } else {
                                selectDialog.setLocationRelativeTo(dlg);
                                selectDialog.setVisible(true);
                            }

                            if (indexOfSelect == -1) {
                                throw new CancelMergingException();
                            }

                            flags.put(String.valueOf(matchs[indexOfSelect].getDataSourceKey()), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                if (k == indexOfSelect) {
                                    continue;
                                }

                                flags.put(String.valueOf(matchs[k].getDataSourceKey()), new Integer(-1));
                            }
                        }
                    } else if (mergeType == this.SEMI_AUTO_MERGE) {
                        if (sameTrans && sameTranslator) {
                            flags.put(String.valueOf(id), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (id1 == id) {
                                    continue;
                                }

                                flags.put(String.valueOf(id1), new Integer(-1));
                            }
                        } else if (sameTrans && !sameTranslator) {
                            //top id
                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (k == indexInMatch) {
                                    flags.put(String.valueOf(id1), new Integer(1));
                                } else {
                                    flags.put(String.valueOf(id1), new Integer(-1));
                                }
                            }
                        } else if (!sameTrans && sameTranslator) {
                            //ask
                            Toolkit.getDefaultToolkit().beep();

                            int oldNum = differentTranslation.tableView.getRowCount();
                            SelectSegmentAlignment.data = matchs;
                            differentTranslation.repaintSelf(oldNum);

                            if (selectDialog == null) {
                                selectDialog = new JDialog(dlg, bundle.getString("Select_one_translation"), true) {
                                            protected void processWindowEvent(WindowEvent e) {
                                                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                                                    Component[] children = selectDialog.getContentPane().getComponents();
                                                    ((JButton)((JPanel)children[1]).getComponents()[1]).doClick();

                                                    return;
                                                }
                                            }
                                        };
                                selectDialog.getContentPane().setLayout(new BorderLayout());
                                selectDialog.getContentPane().add(differentTranslation, BorderLayout.CENTER);

                                JButton addSegmentButton = new JButton(bundle.getString("Add"));
                                JButton cancelSegmentButton = new JButton(bundle.getString("Cancel"));
                                JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                p.add(addSegmentButton);
                                p.add(cancelSegmentButton);
                                addSegmentButton.setBounds(10, 10, 40, 25);
                                addSegmentButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            int index1 = differentTranslation.tableView.getSelectedRow();

                                            if (index1 == -1) {
                                                return;
                                            }

                                            indexOfSelect = index1;

                                            selectDialog.setVisible(false);
                                        }
                                    });
                                cancelSegmentButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            indexOfSelect = -1;
                                            selectDialog.setVisible(false);
                                        }
                                    });

                                selectDialog.getContentPane().add(p, BorderLayout.SOUTH);
                                selectDialog.setSize(600, 400);
                                selectDialog.setLocationRelativeTo(dlg);
                                selectDialog.setVisible(true);
                            } else {
                                selectDialog.setLocationRelativeTo(dlg);
                                selectDialog.setVisible(true);
                            }

                            if (indexOfSelect == -1) {
                                throw new CancelMergingException();
                            }

                            flags.put(String.valueOf(matchs[indexOfSelect].getDataSourceKey()), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                if (k == indexOfSelect) {
                                    continue;
                                }

                                flags.put(String.valueOf(matchs[k].getDataSourceKey()), new Integer(-1));
                            }
                        } else if (!sameTrans && !sameTranslator) {
                            //top id
                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (k == indexInMatch) {
                                    flags.put(String.valueOf(id1), new Integer(1));
                                } else {
                                    flags.put(String.valueOf(id1), new Integer(-1));
                                }
                            }
                        }
                    } else if (mergeType == this.AUTO_MERGE) {
                        if (sameTrans && sameTranslator) {
                            flags.put(String.valueOf(id), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (id1 == id) {
                                    continue;
                                }

                                flags.put(String.valueOf(id1), new Integer(-1));
                            }
                        } else if (sameTrans && !sameTranslator) {
                            //top id
                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (k == indexInMatch) {
                                    flags.put(String.valueOf(id1), new Integer(1));
                                } else {
                                    flags.put(String.valueOf(id1), new Integer(-1));
                                }
                            }
                        } else if (!sameTrans && sameTranslator) {
                            flags.put(String.valueOf(id), new Integer(1));

                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (id1 == id) {
                                    continue;
                                }

                                flags.put(String.valueOf(id1), new Integer(-1));
                            }
                        } else if (!sameTrans && !sameTranslator) {
                            //top id
                            for (int k = 0; k < matchs.length; k++) {
                                long id1 = matchs[k].getDataSourceKey();

                                if (k == indexInMatch) {
                                    flags.put(String.valueOf(id1), new Integer(1));
                                } else {
                                    flags.put(String.valueOf(id1), new Integer(-1));
                                }
                            }
                        }
                    }
                }

                matchs = null;
            }

            tempSrc = null;

            File file = new File(miniTmDir + "tmp_" + destMiniTM.getSourceLang() + "_" + destMiniTM.getTargetLang());
            file.delete();

            for (int i = 0; i < segs.length; i++) {
                Object flag = flags.get(String.valueOf(segs[i].getDataStoreKey()));

                if (flag == null) {
                    continue;
                }

                if (((Integer)flag).intValue() == 1) {
                    destMiniTM.addNewSegment(segs[i]);
                }
            }

            destMiniTM.saveMiniTmToFile();

            org.jvnet.olt.editor.model.TransProject project = ((MergeMiniTMPanel)dlg).project;
            String temp1 = project.getProjectName() + "_" + project.getSrcLang() + "_" + project.getTgtLang();

            //TODO get rod of this !!!
            //projectHistory.add(projectHistory.isEmpty() ? 0 : 1, temp1);
            logger.finer("Should have added project to history but did not.");

            ((MergeMiniTMPanel)dlg).init();
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(dlg, bundle.getString("The_selected_mini-TMs_have_been_successfully_merged."), bundle.getString("Merge_process"), JOptionPane.PLAIN_MESSAGE);

            this.cards.show(parent, "first");
        } catch (org.jvnet.olt.minitm.MiniTMException ex) {
        } catch (CancelMergingException ex) {
            //logger.finer.println("Cancel");
        } finally {
            tempSrc = null;
            destMiniTM = null;
            srcMiniTMs = null;
            segs = null;
            flags = null;
            unmergedSegments = null;
        }
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        dlg.setVisible(false);
    }

    void addButton_actionPerformed(ActionEvent e) {
        lanPair = "_" + 
            ((MergeMiniTMPanel)dlg).sourceLan + 
            "_" + 
            ((MergeMiniTMPanel)dlg).targetLan + 
            ".MTM";

        MiniTMOpenFilter filter = new MiniTMOpenFilter();
        fileChooser.setFileFilter(filter);
        fileChooser.setApproveButtonText(bundle.getString("Add"));
        fileChooser.setDialogTitle(bundle.getString("Add_Mini-TM_File"));
        fileChooser.setApproveButtonMnemonic('A');

        int ret = fileChooser.showOpenDialog(this);

        switch (ret) {
        case -1:
        case JOptionPane.CANCEL_OPTION:
            filter = null;

            return;

        case JOptionPane.OK_OPTION:

            File file = fileChooser.getSelectedFile();

            //StringTokenizer tokens = new StringTokenizer(file.getName(),"_",false);
            //String miniTMName = (String)tokens.nextElement();
            String filename = file.getAbsolutePath();
            String temp = filename.substring(filename.lastIndexOf(File.separator) + 1, filename.length());
            StringTokenizer tokens = new StringTokenizer(temp, "_", false);

            if (tokens.countTokens() == 3) {
                String miniTMName = (String)tokens.nextElement();

                //String id = (String)tokens.nextElement();
                String srcLan = (String)tokens.nextElement();
                String tgtLan = (String)tokens.nextElement();

                try {
                    org.jvnet.olt.minitm.MiniTM tempTM = new org.jvnet.olt.fuzzy.basicsearch.BasicFuzzySearchMiniTM(filename, true, miniTMName, srcLan, tgtLan);
                    String[] ids = tempTM.getAllTranslatorIDs();
                    idTableAlignment.append(ids);
                    tmTableAlignment.append(miniTMName);

                    files.add(file.getAbsolutePath());
                    miniTMs.addElement(tempTM);
                } catch (org.jvnet.olt.minitm.MiniTMException ex) {
                }

                filter = null;
            }
        }
    }

    void removeButton_actionPerformed(ActionEvent e) {
        int index = tmTableAlignment.tableView.getSelectedRow();

        if (index != -1) {
            org.jvnet.olt.minitm.MiniTM tempTM = (org.jvnet.olt.minitm.MiniTM)miniTMs.elementAt(index);
            String[] ids = tempTM.getAllTranslatorIDs();
            int[] deleteIdsFlag = new int[ids.length];

            for (int i = 0; i < ids.length; i++)
                deleteIdsFlag[i] = 1;

            for (int j = 0; j < miniTMs.size(); j++) {
                if (j == index) {
                    continue;
                }

                String[] idsTemp = ((org.jvnet.olt.minitm.MiniTM)miniTMs.elementAt(j)).getAllTranslatorIDs();

                for (int k = 0; k < deleteIdsFlag.length; k++) {
                    if (deleteIdsFlag[k] == 0) {
                        continue;
                    }

                    for (int kk = 0; kk < idsTemp.length; kk++) {
                        if (ids[k].equals(idsTemp[kk])) {
                            deleteIdsFlag[k] = 0;
                        }
                    }
                }
            }

            idTableAlignment.remove(ids, deleteIdsFlag);

            files.remove(index);
            miniTMs.removeElementAt(index);
        }

        tmTableAlignment.remove();
    }

    void moveUpButton_actionPerformed(ActionEvent e) {
        idTableAlignment.moveUp();
    }

    void moveDownButton_actionPerformed(ActionEvent e) {
        idTableAlignment.moveDown();
    }

    void manualMerge_actionPerformed(ActionEvent e) {
        mergeType = MANUAL_MERGE;

        //this.manual = true;
        //this.useTranslatorId = false;
        //this.viewDuplicates = true;
    }

    void autoMerge_actionPerformed(ActionEvent e) {
        mergeType = AUTO_MERGE;

        //this.manual = false;
        //this.useTranslatorId = true;
        //this.viewDuplicates = false;
    }

    void semiautoMerge_actionPerformed(ActionEvent e) {
        mergeType = SEMI_AUTO_MERGE;

        //this.manual = false;
        //this.useTranslatorId = true;
        //this.viewDuplicates = true;
    }
}
