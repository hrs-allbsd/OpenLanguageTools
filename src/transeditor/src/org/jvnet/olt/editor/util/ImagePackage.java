/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.awt.Image;

import javax.swing.ImageIcon;


public class ImagePackage {
    private static ImagePackage instance = null;

    /**
     * GUI gifs
     */
    public static ImageIcon imageOpen;
    public static ImageIcon imageSave;
    public static ImageIcon imageUpdate;
    public static ImageIcon imageHelp;
    public static ImageIcon imageNext;
    public static ImageIcon imagePrev;

    //public static ImageIcon imageClose;
    public static ImageIcon imageUndo;
    public static ImageIcon imageRedo;
    public static ImageIcon imageCopy;
    public static ImageIcon imagePaste;
    public static ImageIcon imageCut;
    public static ImageIcon imageSearch;
    public static ImageIcon imageMTAGNU;
    public static ImageIcon imageMVAGNT;
    public static ImageIcon imageSpellCheck;
    public static ImageIcon imageTagVerify;
    public static ImageIcon imageFind;
    public static ImageIcon imageUnTrans;
    public static ImageIcon imageComment;
    public static ImageIcon imageUpload;
    public static ImageIcon imageApproveFile;
    public static ImageIcon imageRejectFile;
    public static ImageIcon imageApproveSegment;
    public static ImageIcon imageRejectSegment;

    //nation flags
    public static ImageIcon[] imageNations = new ImageIcon[Languages.language_code_name.length];

    //translation status and type gifs
    public static ImageIcon[] statusIcon = new ImageIcon[4];
    public static ImageIcon[] typeIcon = new ImageIcon[6];

    private ImagePackage() {
        //init gifs in gui
        imageOpen = new ImageIcon(getClass().getResource("/resources/gifs/Open.gif"));
        imageSave = new ImageIcon(getClass().getResource("/resources/gifs/Save.gif"));
        imageUpdate = new ImageIcon(getClass().getResource("/resources/gifs/MiniTM.gif"));
        imageHelp = new ImageIcon(getClass().getResource("/resources/gifs/Help.gif"));
        imageNext = new ImageIcon(getClass().getResource("/resources/gifs/Next.gif"));
        imagePrev = new ImageIcon(getClass().getResource("/resources/gifs/Previous.gif"));

        imageUndo = new ImageIcon(getClass().getResource("/resources/gifs/Undo.gif"));
        imageRedo = new ImageIcon(getClass().getResource("/resources/gifs/Redo.gif"));
        imageCopy = new ImageIcon(getClass().getResource("/resources/gifs/Copy.gif"));
        imagePaste = new ImageIcon(getClass().getResource("/resources/gifs/Paste.gif"));
        imageCut = new ImageIcon(getClass().getResource("/resources/gifs/Cut.gif"));
        imageSearch = new ImageIcon(getClass().getResource("/resources/gifs/Search.gif"));
        imageMTAGNU = new ImageIcon(getClass().getResource("/resources/gifs/Translated.gif"));
        imageMVAGNT = new ImageIcon(getClass().getResource("/resources/gifs/Review.gif"));
        imageSpellCheck = new ImageIcon(getClass().getResource("/resources/gifs/SpellCheck.gif"));
        imageTagVerify = new ImageIcon(getClass().getResource("/resources/gifs/TagVerify.gif"));
        imageFind = new ImageIcon(getClass().getResource("/resources/gifs/Find.gif"));

        imageComment = new ImageIcon(getClass().getResource("/resources/gifs/Comment.gif"));
        imageUpload = new ImageIcon(getClass().getResource("/resources/gifs/Upload.gif"));
        imageApproveFile = new ImageIcon(getClass().getResource("/resources/gifs/AcceptFile.gif"));
        imageRejectFile = new ImageIcon(getClass().getResource("/resources/gifs/RejectFile.gif"));
        imageApproveSegment = new ImageIcon(getClass().getResource("/resources/gifs/AcceptSegment.gif"));
        imageRejectSegment = new ImageIcon(getClass().getResource("/resources/gifs/RejectSegment.gif"));

        //Init translation type and status gifs;
        statusIcon[0] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/untrans.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        statusIcon[1] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/trans.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        statusIcon[2] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/verified.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        statusIcon[3] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/rejected.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));

        typeIcon[0] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/untrans.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        typeIcon[1] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/exact.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        typeIcon[2] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/auto.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        typeIcon[3] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/fuzzy.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        typeIcon[4] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/user.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        typeIcon[5] = new ImageIcon(new ImageIcon(getClass().getResource("/resources/gifs/user.gif")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }

    public static synchronized ImagePackage instance() {
        if (instance == null) {
            instance = new ImagePackage();
        }

        return instance;
    }
}
