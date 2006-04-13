
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * HtmlTagTable.java
 *
 * Created on June 26, 2002, 3:03 PM
 */

package org.jvnet.olt.filters.sgml.docbook;

import java.util.HashSet;
import org.jvnet.olt.parsers.tagged.TagTable;

public class DocbookTagTable implements TagTable {

    private HashSet pcdataSet;
    private HashSet verbatimSet;
    private HashSet emptySet;
    
    public DocbookTagTable() {
        pcdataSet = new HashSet();
        
        pcdataSet.add("abbrev");
        pcdataSet.add("accel");
        pcdataSet.add("ackno");
        pcdataSet.add("acronym");
        pcdataSet.add("action");
        pcdataSet.add("address");
        pcdataSet.add("alt");
        pcdataSet.add("application");
        pcdataSet.add("artpagenums");
        pcdataSet.add("attribution");
        pcdataSet.add("authorinitials");
        pcdataSet.add("bibliomisc");
        pcdataSet.add("bibliomixed");
        pcdataSet.add("bibliomset");
        pcdataSet.add("bridgehead");
        pcdataSet.add("citation");
        pcdataSet.add("citerefentry");
        pcdataSet.add("citetitle");
        pcdataSet.add("city");
        pcdataSet.add("classname");
        pcdataSet.add("collabname");
        pcdataSet.add("comment");
        pcdataSet.add("computeroutput");
        pcdataSet.add("confdates");
        pcdataSet.add("confnum");
        pcdataSet.add("confsponsor");
        pcdataSet.add("conftitle");
        pcdataSet.add("constant");
        pcdataSet.add("contractnum");
        pcdataSet.add("contractsponsor");
        pcdataSet.add("contrib");
        pcdataSet.add("corpauthor");
        pcdataSet.add("corpname");
        pcdataSet.add("country");
        pcdataSet.add("database");
        pcdataSet.add("date");
        pcdataSet.add("edition");
        pcdataSet.add("email");
        pcdataSet.add("emphasis");
        pcdataSet.add("envar");
        pcdataSet.add("errorcode");
        pcdataSet.add("errorname");
        pcdataSet.add("errortype");
        pcdataSet.add("fax");
        pcdataSet.add("filename");
        pcdataSet.add("firstname");
        pcdataSet.add("firstterm");
        pcdataSet.add("foreignphrase");
        pcdataSet.add("funcdef");
        pcdataSet.add("funcparams");
        pcdataSet.add("funcsynopsisinfo");
        pcdataSet.add("function");
        pcdataSet.add("glossterm");
        pcdataSet.add("guibutton");
        pcdataSet.add("guiicon");
        pcdataSet.add("guilabel");
        pcdataSet.add("guimenu");
        pcdataSet.add("guimenuitem");
        pcdataSet.add("guisubmenu");
        pcdataSet.add("hardware");
        pcdataSet.add("holder");
        pcdataSet.add("honorific");
        pcdataSet.add("indexterm");
        pcdataSet.add("interface");
        pcdataSet.add("interfacedefinition");
        pcdataSet.add("invpartnumber");
        pcdataSet.add("isbn");
        pcdataSet.add("issn");
        pcdataSet.add("issuenum");
        pcdataSet.add("jobtitle");
        pcdataSet.add("keycap");
        pcdataSet.add("keycode");
        pcdataSet.add("keysym");
        pcdataSet.add("keyword");
        pcdataSet.add("label");
        pcdataSet.add("lineage");
        pcdataSet.add("lineannotation");
        pcdataSet.add("link");
        pcdataSet.add("literal");
        pcdataSet.add("lotentry");
        pcdataSet.add("manvolnum");
        pcdataSet.add("markup");
        pcdataSet.add("medialabel");
        pcdataSet.add("member");
        pcdataSet.add("modespec");
        pcdataSet.add("mousebutton");
        pcdataSet.add("msgaud");
        pcdataSet.add("msglevel");
        pcdataSet.add("menuchoice");
        pcdataSet.add("msgorig");
        pcdataSet.add("olink");
        pcdataSet.add("option");
        pcdataSet.add("optional");
        pcdataSet.add("orgdiv");
        pcdataSet.add("orgname");
        pcdataSet.add("otheraddr");
        pcdataSet.add("othername");
        pcdataSet.add("pagenums");
        pcdataSet.add("paramdef");
        pcdataSet.add("parameter");
        pcdataSet.add("phone");
        pcdataSet.add("phrase");
        pcdataSet.add("pob");
        pcdataSet.add("postcode");
        pcdataSet.add("primary");
        pcdataSet.add("primaryie");
        pcdataSet.add("productname");
        pcdataSet.add("productnumber");
        pcdataSet.add("prompt");
        pcdataSet.add("property");
        pcdataSet.add("pubdate");
        pcdataSet.add("publishername");
        pcdataSet.add("pubsnumber");
        pcdataSet.add("quote");
        pcdataSet.add("refclass");
        pcdataSet.add("refdescriptor");
        pcdataSet.add("refentrytitle");
        pcdataSet.add("refmiscinfo");
        pcdataSet.add("refname");
        pcdataSet.add("refpurpose");
        pcdataSet.add("releaseinfo");
        pcdataSet.add("remark");
        pcdataSet.add("replaceable");
        pcdataSet.add("returnvalue");
        pcdataSet.add("revnumber");
        pcdataSet.add("revremark");
        pcdataSet.add("screeninfo");
        pcdataSet.add("secondary");
        pcdataSet.add("secondaryie");
        
        pcdataSet.add("see");
        pcdataSet.add("seealso");
        pcdataSet.add("seealsoie");
        pcdataSet.add("seeie");
        pcdataSet.add("seg");
        pcdataSet.add("segtitle");
        pcdataSet.add("seriesvolnums");
        pcdataSet.add("sgmltag");
        pcdataSet.add("shortaffil");
        pcdataSet.add("state");
        pcdataSet.add("street");
        pcdataSet.add("structfield");
        pcdataSet.add("structname");
        pcdataSet.add("subjectterm");
        pcdataSet.add("subscript");
        pcdataSet.add("subtitle");
        pcdataSet.add("superscript");
        pcdataSet.add("surname");
        pcdataSet.add("symbol");
        pcdataSet.add("synopsis");
        pcdataSet.add("systemitem");
        pcdataSet.add("term");
        pcdataSet.add("tertiary");
        pcdataSet.add("tertiaryie");

        pcdataSet.add("titleabbrev");
        pcdataSet.add("tocback");
        pcdataSet.add("tocentry");
        pcdataSet.add("tocfront");
        pcdataSet.add("token");
        pcdataSet.add("trademark");
        pcdataSet.add("type");
        pcdataSet.add("ulink");
        pcdataSet.add("userinput");
        pcdataSet.add("varname");
        pcdataSet.add("volumenum");
        pcdataSet.add("wordasword");
        pcdataSet.add("year");        
        // this text is at pcdata level, but it's also non-segementable
        // (and should be placed in <it> tags in xliff.

        // add a note tag for these g
        // aren't inline to give the translator a hint that these
        // sections may contain translatbale text (but they're dontsegment)
        pcdataSet.add("screen");
        pcdataSet.add("programlisting");
        
        pcdataSet.add("filename");
        pcdataSet.add("command");
        pcdataSet.add("replaceable");
        pcdataSet.add("option");
        pcdataSet.add("literal");
        pcdataSet.add("userinput");
        pcdataSet.add("olink");
        pcdataSet.add("subjectterm");
        pcdataSet.add("trademark");
        pcdataSet.add("refentrytitle");
        
        pcdataSet.add("computeroutput");
        // inline tags that are empty
        // bugid 4925931 - colspec are block tags
        //pcdataSet.add("colspec");
        pcdataSet.add("spanspec");
        pcdataSet.add("area");
        pcdataSet.add("videodata");
        pcdataSet.add("audiodata");
        pcdataSet.add("imagedata");
        pcdataSet.add("sbr");
        pcdataSet.add("void");
        pcdataSet.add("varargs");
        pcdataSet.add("co");
        pcdataSet.add("footnoteref");
        pcdataSet.add("xref");
        pcdataSet.add("anchor");
        pcdataSet.add("beginpage");
        
        
        //  The Verbatim layout
        verbatimSet = new HashSet();
        verbatimSet.add("address");
        verbatimSet.add("screen");
        verbatimSet.add("screenco");
        verbatimSet.add("programlisting");
        verbatimSet.add("programlistingco");
        verbatimSet.add("literallayout");
        verbatimSet.add("screenshot");
        verbatimSet.add("arg");
        
        //  "EMPTY" tags
        emptySet = new HashSet();
        emptySet.add("colspec");
        emptySet.add("spanspec");
        emptySet.add("area");
        emptySet.add("videodata");
        emptySet.add("audiodata");
        emptySet.add("imagedata");
        emptySet.add("sbr");
        emptySet.add("void");
        emptySet.add("varargs");
        emptySet.add("co");
        emptySet.add("footnoteref");
        emptySet.add("xref");
        emptySet.add("anchor");
        emptySet.add("beginpage");
        
        emptySet.add("suntransxmlfilter");
    }
    
    public boolean tagMayContainPcdata(String tagName) {
        //if (tagName.length() == 0)
          //  return true;
        return pcdataSet.contains(tagName.toLowerCase());
    }
    
    public boolean tagForcesVerbatimLayout(String tagName) {
        return verbatimSet.contains(tagName.toLowerCase());
    }
    
    public boolean tagEmpty(String tagName) {
        return emptySet.contains(tagName.toLowerCase());
    }
    
    public boolean tagEmpty(String tagName, String namespaceID) {        
        return tagEmpty(tagName);        
    }
    
    public boolean tagForcesVerbatimLayout(String tagName, String namespaceID) {
        return tagForcesVerbatimLayout(tagName);
    }
    
    public boolean tagMayContainPcdata(String tagName, String namespaceID) {
        return tagMayContainPcdata(tagName);
    }
    
}
