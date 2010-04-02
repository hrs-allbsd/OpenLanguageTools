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
 * MrkContentTracker.java
 *
 * Created on April 21, 2004, 5:19 PM
 */
package org.jvnet.olt.xliff.mrk;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;
import org.jvnet.olt.xliff.MrkContent;


/** This class tracks the content of 'mrk' elements detected in XLIFF files. The
 * reason it exists is to enable a GlobalVariableManager object to be populated
 * with parameter entities form marked sections. This will allow the editor to
 * work out if marked sections should be marked as included or ignored when they
 * are being processed, i.e., when they are written out to the XLIFF file and when
 * the format highlighting stuff takes place.
 * This works because ignored marked sections are stored in 'mrk' elements with
 * 'mtype'
 * @author  jc73554
 */
public class MrkContentTracker {
    private List mrkRegistered;

    /** Creates a new instance of MrkContentTracker */
    public MrkContentTracker() {
        mrkRegistered = new LinkedList();
    }

    /** This method registers the content of an 'mrk' element. It extracts and
     * any global identifiers in the content, and classifies them based on the
     * value of the 'mtype' parameter.
     */
    public void registerMrk(MrkContent mrk) {
        try {
            MrkItem item = createMrkItem(mrk);
            mrkRegistered.add(item);
        } catch (PatternSyntaxException syntaxEx) {
            syntaxEx.printStackTrace();
        }
    }

    /** This method creates and classifies MrkItem objects. The method exists
     * primarily to support a design for test methodology.
     */
    protected MrkItem createMrkItem(MrkContent mrk) throws PatternSyntaxException {
        String content = mrk.getContent();

        //  Work out if there is an marked section in the content
        //  Create a regex pattern and determine if the 'content string matches
        //  it. The perl 5 pattern match expression used is m/^<!\[\s*([^\[\s]*)\s*[/
        Pattern p = Pattern.compile("^<!\\[\\s*([^\\[\\s]*)\\s*\\[");
        Matcher matcher = p.matcher(content);

        if (matcher.find()) {
            String label = matcher.group(1);

            boolean isConstMrk = (label.equals("INCLUDE") || label.equals("IGNORE") || label.equals(""));

            if (isConstMrk) {
                return new ConstantMarkedSection(content, label);
            } else if (label.equals("CDATA")) {
                return new PlainMrk(content);
            } else {
                //  Classify based on the 'mtype' string and add to the list
                //  'mrk' elements with 'mtype="protect"' denote ignored marked
                //  sections.
                String mtype = mrk.getMtype();
                boolean isIgnoredMrk = mtype.equals("protect");

                return new MarkedSection(content, label, isIgnoredMrk);
            }
        } else {
            //  If no, just add a non identifer type to the list.
            return new PlainMrk(content);
        }
    }

    /** This method populates a GlobalVariableManager based on the registered
     * 'mrk' elements.
     */
    public void populate(GlobalVariableManager gvm) {
        //  Iterate over the mrkRegistered list.
        java.util.Iterator iter = mrkRegistered.iterator();

        while (iter.hasNext()) {
            MrkItem item = (MrkItem)iter.next();

            if (item.hasIdentifier()) {
                try {
                    String resolvesTo = (item.isIgnored() ? "IGNORE" : "INCLUDE");
                    gvm.setVariable(item.getIdentifier(), resolvesTo, "PARAMETER");
                } catch (GlobalVariableManagerException gvmEx) {
                    gvmEx.printStackTrace();
                }
            }
        }
    }
}
