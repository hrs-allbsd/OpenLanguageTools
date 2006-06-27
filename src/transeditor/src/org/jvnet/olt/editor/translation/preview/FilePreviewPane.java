/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.translation.preview;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jvnet.olt.editor.util.LanguageMappingTable;
import org.jvnet.olt.editor.util.Languages;
import org.jvnet.olt.utilities.XliffZipFileIO;

import org.xml.sax.InputSource;


/**
 * User: boris
 * Date: Feb 10, 2005
 * Time: 1:20:08 PM
 */
public class FilePreviewPane extends JPanel implements PropertyChangeListener {
    private ResourceBundle bundle = ResourceBundle.getBundle(FilePreviewPane.class.getName());    
    private String sourceLang;
    private String targetLang;
    private JLabel srcLangImgLabel;
    private JLabel tgtLangImgLabel;
    private JLabel srcLangLabel;
    private JLabel tgtLangLabel;
    private JLabel fileNameLabel;
    private boolean previewEnabled = true;
    private File lastFile;
    ImageIcon unknown;

    public FilePreviewPane(JFileChooser chooser) {
        super();

        chooser.addPropertyChangeListener(this);

        buildUI();
    }

    void buildUI() {
        setPreferredSize(new Dimension(150, 300));

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        Dimension prefDim = new Dimension(150, 50);

        final JPanel fileNamePanel = new JPanel();
        fileNamePanel.setLayout(new BorderLayout());
        fileNameLabel = new JLabel("");
        fileNameLabel.setPreferredSize(new Dimension(150, 30));

        fileNamePanel.add(new JLabel(bundle.getString("File:")), BorderLayout.WEST);
        fileNamePanel.add(fileNameLabel, BorderLayout.CENTER);

        add(fileNamePanel);

        final JPanel srcPanel = new JPanel();
        srcPanel.setLayout(new GridLayout(3, 1));
        srcPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        srcLangImgLabel = new JLabel((Icon)null, JLabel.CENTER);
        srcLangImgLabel.setPreferredSize(prefDim);
        srcLangLabel = new JLabel("", JLabel.CENTER);

        srcPanel.add(new JLabel(bundle.getString("Source_language:")));
        srcPanel.add(srcLangImgLabel);
        srcPanel.add(srcLangLabel);

        tgtLangImgLabel = new JLabel((Icon)null, JLabel.CENTER);
        tgtLangImgLabel.setPreferredSize(prefDim);

        tgtLangLabel = new JLabel("", JLabel.CENTER);

        final JPanel tgtPanel = new JPanel();
        tgtPanel.setLayout(new GridLayout(3, 1));
        tgtPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        tgtPanel.add(new JLabel(bundle.getString("Target_language:")));
        tgtPanel.add(tgtLangImgLabel);
        tgtPanel.add(tgtLangLabel);

        add(srcPanel);
        add(tgtPanel);

        final JCheckBox enablePreview = new JCheckBox(bundle.getString("Enable_preview"), true);
        enablePreview.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    previewEnabled = e.getStateChange() == ItemEvent.SELECTED;

                    if (previewEnabled) {
                        previewFile(lastFile);
                    } else {
                        reset();
                        fileNameLabel.setText("");
                    }
                }
            });

        add(new JPanel() {

                {
                    setLayout(new BorderLayout());
                    add(enablePreview, BorderLayout.CENTER);
                }
            });

        unknown = loadIcon(null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
            reset();

            return;
        }

        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
            lastFile = (File)evt.getNewValue();

            if (previewEnabled) {
                previewFile(lastFile);
            }
        }
    }

    void previewFile(File file) {
        if ((file == null) || !file.exists()) {
            reset();

            return;
        }

        //this is safe and can not mess anything up
        fileNameLabel.setText(file.getName());

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            XliffZipFileIO xlzip = new XliffZipFileIO(file);
            Reader r = xlzip.getXliffReader();

            if (!peekIntoFile(r)) {
                reset();

                return;
            }

            ImageIcon icon = loadIcon(sourceLang);
            srcLangImgLabel.setIcon(icon);
            setLangLable(sourceLang, srcLangLabel);

            icon = loadIcon(targetLang);
            tgtLangImgLabel.setIcon(icon);
            setLangLable(targetLang, tgtLangLabel);
        } catch (IOException e) {
            reset();
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void setLangLable(String shortLangCode, JLabel label) {
        if (shortLangCode == null) {
            label.setText(bundle.getString("Not_specified"));
        } else {
            String code = LanguageMappingTable.getInstance().translateLangCode(shortLangCode);
            label.setText(Languages.getLanguageName(code));
        }
    }

    private void reset() {
        srcLangImgLabel.setIcon(null);
        srcLangLabel.setText("");
        tgtLangImgLabel.setIcon(null);
        tgtLangLabel.setText("");

        //fileNameLabel.setText("");
    }

    boolean peekIntoFile(Reader r) throws IOException {
        FastFailHanlder ffh = new FastFailHanlder(this);

        try {
            //TODO singletons ???
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            parser.parse(new InputSource(r), ffh);
        } catch (AbortParsingException ape) {
            sourceLang = ffh.getSrcLang();
            targetLang = ffh.getTgtLang();

            return ape.isSuccess();
        } catch (Throwable t) {
            return false;
        }

        //if we parsed till end then there's somthing wrong with the file
        return false;
    }

    void abort(boolean success) throws AbortParsingException {
        throw new AbortParsingException(success);
    }

    //TODO move to ONE PLACE
    ImageIcon loadIcon(String langCode) {
        if (langCode == null) {
            String path = Languages.getFlagPathForUnknown();

            return new ImageIcon(getClass().getResource(path));
        } else {
            try {
                String lc = LanguageMappingTable.getInstance().translateLangCode(langCode);
                String path = Languages.getFlagPath(lc);

                return new ImageIcon(getClass().getResource(path));
            } catch (Exception e) {
                return null;
            }
        }
    }
}
