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
/*
 * OpenFileThread.java
 *
 * Created on March 22, 2005, 7:52 PM
 */
package org.jvnet.olt.editor.translation;

import java.awt.Toolkit;

import java.io.File;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jvnet.olt.editor.model.TMData;
import org.jvnet.olt.editor.model.TransProject;
import org.jvnet.olt.editor.util.*;
//import org.jvnet.olt.editor.util.LanguageMappingTable;
import org.jvnet.olt.xliff.XLIFFParser;


/**
 *
 * @author boris
 */
class OpenFileThread extends Thread {
    private static final Logger logger = Logger.getLogger(OpenFileThread.class.getName());
    private File file;
    private Backend backend;
    private PostXLIFFOpenHandler post;

    public OpenFileThread(File file, Backend backend, PostXLIFFOpenHandler post) {
        this.file = file;

        //TODO backend is not here
        this.backend = backend;
        this.post = post;
    }

    public void run() {
        try {
            TransProject project = backend.getProject();

            XLIFFParser xp = new XLIFFParser(file);

            xp.examineFile();
            
            xp.readFile();
            
            //if (!xp.build()) {
            //    post.buildMethodFailed();

            //    return;
            //}

            TMData tmpdata = new TMData(project, xp);

            //TODO make sure document is set as modified so that the forced target lang gets saved
            //boolean forcedLang = false;

            while (!tmpdata.build()) {
                if (tmpdata.getLastError() == TMData.ERROR_TARGET_LANG_MISSING) {
                    //String longTargLang = LanguageMappingTable.getInstance().reverseTranslateLangCode(project.getTgtLang());
                    String longTargLang = project.getTgtLang();
                    if (!post.acceptProjectLanguage(longTargLang)) {
                        return;
                    }

                    xp.setTargetLanguage(longTargLang);
                    tmpdata.setTargetLanguage(project.getTgtLang());
                } else {
                    logger.warning("Some other error than MISSING TARGET LANG occured; Strange");

                    return;
                }
            }

            if(tmpdata.getHasMiniTMMatches() && post.propagateMiniTMMatches()){
                if(tmpdata.populateFromMiniTM())
                    tmpdata.saveAllTranslations(false,backend.getConfig().isBFlagWriteProtection());                
            }
            
            String srcLan = tmpdata.getSourceLanguageCode();
            String tgtLan = tmpdata.getTargetLanguageCode();

            //if ((srcLan.compareToIgnoreCase(project.getSrcLang()) != 0) || (tgtLan.compareToIgnoreCase(project.getTgtLang()) != 0)) {
            if (( ! Languages.areSimilar(srcLan , project.getSrcLang()) ) ||
                ( ! Languages.areSimilar(tgtLan , project.getTgtLang()))) {
                post.languagesMisMatch();

                //Languages do not fit; we're done here
                return;
            }

            // setting the dataType of the project now that we know what it is
            // then force the miniTM to reload itself so that the fuzzy matching
            // algorithm used will match the current datatype
            project.setDataType(xp.getOriginalDataType());
            project.reloadMinitm();

            backend.setTMData(tmpdata);
            backend.setCurrentFile(file);
            backend.setXLIFFParser(xp);

            post.success();

            //        } catch(java.lang.OutOfMemoryError ec) {
            //            post.exceptionThrown(ec);
        } catch (Throwable exce) {
            logger.throwing(getClass().getName(), "run()", exce);
            logger.severe("Exception:" + exce);

            post.exceptionThrown(exce);
        } finally {
            post.done();
        }
    }

    public static interface PostXLIFFOpenHandler {
        void buildMethodFailed();

        boolean acceptProjectLanguage(String longLangName);

        void languagesMisMatch();

        void success();

        void exceptionThrown(Throwable t);

        void done();
        
        boolean propagateMiniTMMatches();
    }
}
