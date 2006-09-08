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
import javax.swing.JPanel;
import org.jvnet.olt.editor.util.Languages;


public class ContrastFrame extends JPanel {
    private static final Logger logger = Logger.getLogger(ContrastFrame.class.getName());
    boolean bFlag = false;
    TMInnerPanel tmLeftPanel;
    TMInnerPanel tmRightPanel;

    public ContrastFrame(boolean bOrention, Backend backend) {
        this.bFlag = bOrention;

        if (bFlag) {
            this.setLayout(new GridLayout(1, 2, 10, 0));
        } else {
            this.setLayout(new GridLayout(2, 1, 0, 10));
        }

        tmLeftPanel = new TMInnerPanel(0, backend);
        tmLeftPanel.setBorder(BorderFactory.createEtchedBorder());
        tmRightPanel = new TMInnerPanel(1, backend);
        tmRightPanel.setBorder(BorderFactory.createEtchedBorder());

        //if(MainFrame.strSrcFlag == null || MainFrame.strSrcFlag.equals(""))
        setDefaultLeftFlag();

        //else
        //    tmLeftPanel.setNationFlag(MainFrame.strSrcFlag);
        //if(MainFrame.strDestFlag == null || MainFrame.strDestFlag.equals(""))
        setDefaultRightFlag();

        //else
        //tmRightPanel.setNationFlag(MainFrame.strDestFlag);
        this.add(tmLeftPanel);
        this.add(tmRightPanel);
        this.setVisible(true);
    }

    public void setLeftFlag(String strFlagPath) {
        tmLeftPanel.setNationFlag(strFlagPath);
        //tmLeftPanel.setFlagTips(Languages.getLanguageName(strFlagPath));
    }

    public void setRightFlag(String strFlagPath) {
        tmRightPanel.setNationFlag(strFlagPath);
        //tmRightPanel.setFlagTips(Languages.getLanguageName(strFlagPath));
    }

    public void setLeftTooltip(String strFlagPath) {
        tmLeftPanel.setFlagTips(strFlagPath);
    }

    public void setRightTooltip(String strFlagPath) {
        tmRightPanel.setFlagTips(strFlagPath);
    }

    public void setDefaultLeftFlag() {
        try {
            tmLeftPanel.setNationFlag(org.jvnet.olt.editor.util.Languages.getFlagPath("US"));
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "setDefaultLeftFlag", e);

            //TODO throw exception ?
        }
    }

    public void setDefaultRightFlag() {
        try {
            tmRightPanel.setNationFlag(org.jvnet.olt.editor.util.Languages.getFlagPath("US"));
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "setDefaultRightFlag", e);

            //TODO throw exception ?
        }
    }

    public String GetNationName(String strFlagPath) {
        String strTemp;
        strTemp = strFlagPath;

        int i = strTemp.lastIndexOf("/");
        strTemp = strFlagPath.substring(i);
        strTemp = strTemp.substring(1, strTemp.length() - 4);
        strTemp = strTemp.replace('_', ' ');
        strTemp = strTemp.replace('1', '(');
        strTemp = strTemp.replace('2', ')');

        return strTemp;
    }
}
