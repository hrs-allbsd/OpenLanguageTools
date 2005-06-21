
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.parsers.SgmlDocFragmentParser;

import java.util.HashMap;

public class DocbookTagTable implements TagTable
{
  private HashMap hashPcdata;
  private HashMap hashVerbatim;
  private HashMap hashEmpty;

  public DocbookTagTable()
  {
    hashPcdata = new HashMap();

    //  Create the tag table here.
    hashPcdata.put("abbrev","abbrev");
    hashPcdata.put("accel","accel");
    hashPcdata.put("ackno","ackno");
    hashPcdata.put("acronym","acronym");
    hashPcdata.put("action","action");
    hashPcdata.put("address","address");
    hashPcdata.put("alt","alt");
    hashPcdata.put("application","application");
    hashPcdata.put("arg","arg");
    hashPcdata.put("artpagenums","artpagenums");
    hashPcdata.put("attribution","attribution");
    hashPcdata.put("authorinitials","authorinitials");
    hashPcdata.put("bibliomisc","bibliomisc");
    hashPcdata.put("bibliomixed","bibliomixed");
    hashPcdata.put("bibliomset","bibliomset");
    hashPcdata.put("bridgehead","bridgehead");
    hashPcdata.put("citation","citation");
    hashPcdata.put("citetitle","citetitle");
    hashPcdata.put("city","city");
    hashPcdata.put("classname","classname");
    hashPcdata.put("collabname","collabname");
    hashPcdata.put("command","command");
    hashPcdata.put("comment","comment");
    hashPcdata.put("computeroutput","computeroutput");
    hashPcdata.put("confdates","confdates");
    hashPcdata.put("confnum","confnum");
    hashPcdata.put("confsponsor","confsponsor");
    hashPcdata.put("conftitle","conftitle");
    hashPcdata.put("constant","constant");
    hashPcdata.put("contractnum","contractnum");
    hashPcdata.put("contractsponsor","contractsponsor");
    hashPcdata.put("contrib","contrib");
    hashPcdata.put("corpauthor","corpauthor");
    hashPcdata.put("corpname","corpname");
    hashPcdata.put("country","country");
    hashPcdata.put("database","database");
    hashPcdata.put("date","date");
    hashPcdata.put("edition","edition");
    hashPcdata.put("email","email");
    hashPcdata.put("emphasis","emphasis");
    hashPcdata.put("entry","entry");
    hashPcdata.put("envar","envar");
    hashPcdata.put("errorcode","errorcode");
    hashPcdata.put("errorname","errorname");
    hashPcdata.put("errortype","errortype");
    hashPcdata.put("fax","fax");
    hashPcdata.put("filename","filename");
    hashPcdata.put("firstname","firstname");
    hashPcdata.put("firstterm","firstterm");
    hashPcdata.put("foreignphrase","foreignphrase");
    hashPcdata.put("funcdef","funcdef");
    hashPcdata.put("funcparams","funcparams");
    hashPcdata.put("funcsynopsisinfo","funcsynopsisinfo");
    hashPcdata.put("function","function");
    hashPcdata.put("glosssee","glosssee");
    hashPcdata.put("glossseealso","glossseealso");
    hashPcdata.put("glossterm","glossterm");
    hashPcdata.put("guibutton","guibutton");
    hashPcdata.put("guiicon","guiicon");
    hashPcdata.put("guilabel","guilabel");
    hashPcdata.put("guimenu","guimenu");
    hashPcdata.put("guimenuitem","guimenuitem");
    hashPcdata.put("guisubmenu","guisubmenu");
    hashPcdata.put("hardware","hardware");
    hashPcdata.put("holder","holder");
    hashPcdata.put("honorific","honorific");
    hashPcdata.put("interface","interface");
    hashPcdata.put("interfacedefinition","interfacedefinition");
    hashPcdata.put("invpartnumber","invpartnumber");
    hashPcdata.put("isbn","isbn");
    hashPcdata.put("issn","issn");
    hashPcdata.put("issuenum","issuenum");
    hashPcdata.put("jobtitle","jobtitle");
    hashPcdata.put("keycap","keycap");
    hashPcdata.put("keycode","keycode");
    hashPcdata.put("keysym","keysym");
    hashPcdata.put("keyword","keyword");
    hashPcdata.put("label","label");
    hashPcdata.put("lineage","lineage");
    hashPcdata.put("lineannotation","lineannotation");
    hashPcdata.put("link","link");
    hashPcdata.put("literal","literal");
    hashPcdata.put("lotentry","lotentry");
    hashPcdata.put("manvolnum","manvolnum");
    hashPcdata.put("markup","markup");
    hashPcdata.put("medialabel","medialabel");
    hashPcdata.put("member","member");
    hashPcdata.put("modespec","modespec");
    hashPcdata.put("mousebutton","mousebutton");
    hashPcdata.put("msgaud","msgaud");
    hashPcdata.put("msglevel","msglevel");
    hashPcdata.put("msgorig","msgorig");
    hashPcdata.put("olink","olink");
    hashPcdata.put("option","option");
    hashPcdata.put("optional","optional");
    hashPcdata.put("orgdiv","orgdiv");
    hashPcdata.put("orgname","orgname");
    hashPcdata.put("otheraddr","otheraddr");
    hashPcdata.put("othername","othername");
    hashPcdata.put("pagenums","pagenums");
    hashPcdata.put("para","para");
    hashPcdata.put("paramdef","paramdef");
    hashPcdata.put("parameter","parameter");
    hashPcdata.put("phone","phone");
    hashPcdata.put("phrase","phrase");
    hashPcdata.put("pob","pob");
    hashPcdata.put("postcode","postcode");
    hashPcdata.put("primary","primary");
    hashPcdata.put("primaryie","primaryie");
    hashPcdata.put("productname","productname");
    hashPcdata.put("productnumber","productnumber");
    hashPcdata.put("prompt","prompt");
    hashPcdata.put("property","property");
    hashPcdata.put("pubdate","pubdate");
    hashPcdata.put("publishername","publishername");
    hashPcdata.put("pubsnumber","pubsnumber");
    hashPcdata.put("quote","quote");
    hashPcdata.put("refclass","refclass");
    hashPcdata.put("refdescriptor","refdescriptor");
    hashPcdata.put("refentrytitle","refentrytitle");
    hashPcdata.put("refmiscinfo","refmiscinfo");
    hashPcdata.put("refname","refname");
    hashPcdata.put("refpurpose","refpurpose");
    hashPcdata.put("releaseinfo","releaseinfo");
    hashPcdata.put("replaceable","replaceable");
    hashPcdata.put("returnvalue","returnvalue");
    hashPcdata.put("revnumber","revnumber");
    hashPcdata.put("revremark","revremark");
    hashPcdata.put("screeninfo","screeninfo");
    hashPcdata.put("secondary","secondary");
    hashPcdata.put("secondaryie","secondaryie");
    hashPcdata.put("see","see");
    hashPcdata.put("seealso","seealso");
    hashPcdata.put("seealsoie","seealsoie");
    hashPcdata.put("seeie","seeie");
    hashPcdata.put("seg","seg");
    hashPcdata.put("segtitle","segtitle");
    hashPcdata.put("seriesvolnums","seriesvolnums");
    hashPcdata.put("sgmltag","sgmltag");
    hashPcdata.put("shortaffil","shortaffil");
    hashPcdata.put("simpara","simpara");
    hashPcdata.put("state","state");
    hashPcdata.put("street","street");
    hashPcdata.put("structfield","structfield");
    hashPcdata.put("structname","structname");
    hashPcdata.put("subjectterm","subjectterm");
    hashPcdata.put("subscript","subscript");
    hashPcdata.put("subtitle","subtitle");
    hashPcdata.put("superscript","superscript");
    hashPcdata.put("surname","surname");
    hashPcdata.put("symbol","symbol");
    hashPcdata.put("synopsis","synopsis");
    hashPcdata.put("systemitem","systemitem");
    hashPcdata.put("term","term");
    hashPcdata.put("tertiary","tertiary");
    hashPcdata.put("tertiaryie","tertiaryie");
    hashPcdata.put("title","title");
    hashPcdata.put("titleabbrev","titleabbrev");
    hashPcdata.put("tocback","tocback");
    hashPcdata.put("tocentry","tocentry");
    hashPcdata.put("tocfront","tocfront");
    hashPcdata.put("token","token");
    hashPcdata.put("trademark","trademark");
    hashPcdata.put("type","type");
    hashPcdata.put("ulink","ulink");
    hashPcdata.put("userinput","userinput");
    hashPcdata.put("varname","varname");
    hashPcdata.put("volumenum","volumenum");
    hashPcdata.put("wordasword","wordasword");
    hashPcdata.put("year","year");
   
    //  The Verbatim layout
    hashVerbatim = new HashMap();
    hashVerbatim.put("address","address");
    hashVerbatim.put("screen", "screen");
    hashVerbatim.put("screenco", "screenco");
    hashVerbatim.put("programlisting", "programlisting");
    hashVerbatim.put("programlistingco", "programlistingco");
    hashVerbatim.put("literallayout", "literallayout");
    hashVerbatim.put("synopsis","synopsis");
    hashVerbatim.put("screenshot","screenshot");

    //  "EMPTY" tags
    hashEmpty = new HashMap();
    hashEmpty.put("colspec", "colspec");
    hashEmpty.put("spanspec", "spanspec");
    hashEmpty.put("area", "area");
    hashEmpty.put("videodata", "videodata");
    hashEmpty.put("audiodata", "audiodata");
    hashEmpty.put("imagedata", "imagedata");
    hashEmpty.put("sbr", "sbr");
    hashEmpty.put("void", "void");
    hashEmpty.put("varargs", "varargs");
    hashEmpty.put("co", "co");
    hashEmpty.put("footnoteref", "footnoteref");
    hashEmpty.put("xref", "xref");
    hashEmpty.put("anchor", "anchor");
    hashEmpty.put("beginpage", "beginpage");  
  }

  public boolean tagMayContainPcdata(String tagName)
  {
    return hashPcdata.containsKey(tagName);
  }

  public boolean tagForcesVerbatimLayout(String tagName)
  {
    return hashVerbatim.containsKey(tagName);
  }

  public boolean tagEmpty(String tagName)
  {
    return hashEmpty.containsKey(tagName);
  }
}
