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

import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.*;

import org.jvnet.olt.editor.model.*;
import org.jvnet.olt.editor.util.*;


public class MiniTMTextPane extends JTextPane {
    private static final Logger logger = Logger.getLogger(MiniTMTextPane.class.getName());
    public DefaultStyledDocument content;
    public String value = "";
    public PivotBaseElement[] elements;
    int selectIdStart;
    int selectIdEnd;

    public MiniTMTextPane() {
        super();
        this.setEditable(true);
        content = new DefaultStyledDocument(PivotTextPane.sc);
        this.setEditorKit(PivotTextPane.sek);
        this.setSelectedTextColor(Color.blue);
        setStyledDocument(content);
        elements = new PivotBaseElement[0];
        selectIdStart = -1;
        selectIdEnd = -1;
        this.setBorder(null);
    }

    public String getText() {
        try {
            return content.getText(0, content.getLength());
        } catch (Exception ex) {
            return null;
        }
    }

    public void select(int start, int end) {
        try {
            content.setCharacterAttributes(start, end - start, PivotTextPane.selectStyle, true);
            super.select(start, end);
        } catch (Exception ex) {
        }
    }

    public void select(int start, int end, AttributeSet s) {
        if (start < 0) {
            start = 0;
        }

        if (end > content.getLength()) {
            end = content.getLength();
        }

        try {
            content.setCharacterAttributes(start, end - start, s, true);
        } catch (Exception ex) {
        }
    }

    public void unselect(int start, int end) {
        try {
            content.setCharacterAttributes(start, end - start, PivotTextPane.normalStyle, true);
        } catch (Exception ex) {
        }
    }

    /**
     *    Insert a string to this text pane.
     *  @param source The string that will be inserted.
     */
    public void setContent(String source, PivotBaseElement[] v) {
        if ((v == null) || (v.length == 0)) {
            v = BaseElements.extractContent(source);
        } else {
            /*StringBuffer s = new StringBuffer();
            for(int i =0;i<v.length;i++) {
              s.append((v[i]).getContent());
            }
            if(!(s.toString().equals(source))) source = s.toString();*/
        }

        value = source;

        try {
            content.remove(0, content.getLength());
            this.elements = v;

            content.insertString(0, source, PivotTextPane.normalStyle);
            setStyle();
        } catch (BadLocationException e) {
            logger.throwing(getClass().getName(), "setContent", e);
            logger.severe("Exception:" + e);
        } catch (Exception ex) {
            logger.throwing(getClass().getName(), "setContent", ex);
            logger.severe("Exception:" + ex);

            //TODO change to something better than Exception
        }
    }

    void setStyle() {
        int p = 0;

        for (int i = 0; i < elements.length; i++) {
            PivotBaseElement e = elements[i];
            int curLen = e.getContent().length();
            e.setPositionSite(p);

            switch (e.getElemType()) {
            case PivotBaseElement.BPT_ELEM:
            case PivotBaseElement.EPT_ELEM:
            case PivotBaseElement.IT_ELEM:
            case PivotBaseElement.MRK_ELEM:
            case PivotBaseElement.PH_ELEM:
                content.setCharacterAttributes(p, e.getContent().length(), PivotTextPane.tagStyle, true);

                break;

            case PivotBaseElement.TEXT:
                content.setCharacterAttributes(p, e.getContent().length(), PivotTextPane.normalStyle, true);

                break;
            }

            //            if(e.getFlag()) {
            //                Tag tag = e.getTag();
            //                if( tag.isVisible() ) {
            //                    content.setCharacterAttributes(p,curLen,PivotTextPane.tagStyle,true);
            //                }
            //            }
            //            else {
            //                if(i != 0 && i != (elements.length-1)) {
            //                    if(PivotTag.betweenIntegratedTag(i,elements)) {
            //                        content.setCharacterAttributes(p,curLen,PivotTextPane.tagStyle,true);
            //                    }
            //                    else {
            //                        content.setCharacterAttributes(p,curLen,PivotTextPane.normalStyle,true);
            //                    }
            //                }
            //                else {
            //                    content.setCharacterAttributes(p,curLen,PivotTextPane.normalStyle,true);
            //                }
            //            }
            p += curLen;
        }
    }

    public String getContent() {
        return getText();
    }
}
