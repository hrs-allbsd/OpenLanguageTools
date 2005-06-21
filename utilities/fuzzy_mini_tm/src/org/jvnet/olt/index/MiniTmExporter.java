
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

package org.jvnet.olt.index;

import java.util.*;
import java.io.*;
import org.jvnet.olt.minitm.*;

/**
 *  This class exports the mini TM from memory to a file.
 */
public class MiniTmExporter
{

  /**
   *  The export function is responsible for writing the mini TM to an XML
   *  file. If the specified file exists already it will be overwritten. 
   *  If it does not exist it will be created along with any necessary 
   *  parent directories.
   *  @param tmunits This is the hashtable containing all the paired segments.
   *  @param fileName This is the filename of the file to store the mini TM data in.
   *  @param tmName The name of the TM
   *  @param sourceLang A string representing the source language for the TM.
   *  @param targetLang A string representing the target language for the TM.
   *  @exception DataStoreException
   */
  public void export(Hashtable tmunits, 
		     String fileName, 
		     String tmName, 
		     String sourceLang,
		     String targetLang)
    throws DataStoreException
  {
    try
    {
      //  Create temporary file
      File fileInput = new File(fileName);

      //  Get the absolute path to the file and find where the last '/'
      //  is.
      String filepath = fileInput.getAbsolutePath();
      int lastIndex =  filepath.lastIndexOf(File.separatorChar);

      File tempFile;

      //  If there is a '/' character in the path, then make the path be
      //  everything up to the last one. Create a temporary file in the
      //  directory described by this new path.
      //  Otherwise create a temporary file in the current directory.
      if(lastIndex > 0)
      {
	filepath = filepath.substring(0, lastIndex);
	File minitmDir =  new File(filepath);
	if(!minitmDir.exists())
	{
	  if(!minitmDir.mkdirs()) 
	  { 
	    throw new DataStoreException("Directory to contain Mini TM file did not exist and could not be created."); 
	  }
	}
	
	tempFile = File.createTempFile("mini-tm", "",minitmDir);
      }
      else
      {
	tempFile = File.createTempFile("mini-tm", "", new File("."));	
      }

      FileOutputStream tempOstream = new FileOutputStream(tempFile);
      OutputStreamWriter tempWriter = new OutputStreamWriter(tempOstream, "UTF8");
      
      //  Write to the temporary file
      writeHeader(tempWriter,tmName, sourceLang, targetLang);

      Enumeration myenum = tmunits.elements();
      while(myenum.hasMoreElements())
      {
	TMUnit unit = (TMUnit) myenum.nextElement();
	writeEntry(tempWriter, unit);
      }
      writeFooter(tempWriter);
      tempWriter.close();
     
      //  Nuke the old file
      File realFile = new File(fileName);

      if(realFile.exists())
      {
	if(!realFile.delete())
	{
	  throw new IOException("Could not delete the old Mini Tm file.");
	}
      }

      //  Rename the temporary file
      File file = new File(fileName);
      
      if(!tempFile.renameTo(file))
      {
	throw new IOException("Could not rename the temporary file '" +
			      tempFile.getName() + "' to its proper name " +
			      fileName);
      }
    }
    catch(IOException ex) 
    {
      throw new DataStoreException(ex.getMessage());
    }
  }



  protected void writeHeader(Writer writer,
			     String tmName,
			     String sourceLang,
			     String targetLang)
    throws IOException
  {
    writer.write("<?xml version=\"1.0\" encoding=\"utf8\" ?>\n");        
    writer.write("<minitm name=\"" + tmName + "\" srclang=\"" + sourceLang + "\" tgtlang=\"" + targetLang + "\">\n");        
  }  

  protected void writeFooter(Writer writer)
    throws IOException
  {
    writer.write("</minitm>\n");    
  }

  protected void writeEntry(Writer writer, TMUnit entry)
    throws IOException
  {
    writer.write("\t<entry>\n");

    writer.write("\t\t<source>");
    writeEscapedText(writer, entry.getSource());
    writer.write("</source>\n");

    writer.write("\t\t<translation>");
    writeEscapedText(writer, entry.getTranslation());
    writer.write("</translation>\n");

    writer.write("\t\t<translatorId>");
    writeEscapedText(writer, entry.getTranslatorID());
    writer.write("</translatorId>\n");

    writer.write("\t</entry>\n");
  }

  protected void writeEscapedText(Writer writer, String text)
    throws IOException
  {
    StringReader reader = new StringReader(text);
    int ch;
    while((ch = reader.read()) != -1)
    {
      switch(ch)
      {
      case ((int) '&'):
	writer.write("&amp;");
	break;
      case ((int) '<'):
	writer.write("&lt;");
	break;
      case ((int) '>'):
	writer.write("&gt;");
	break;
      default:
	writer.write(ch);
	break;
      }
    }
  }
}
