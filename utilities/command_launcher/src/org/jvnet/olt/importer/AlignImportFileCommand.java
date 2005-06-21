
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * AlignImportFileCommand.java
 *
 * Created on 02 October 2002, 15:21
 */

package org.jvnet.olt.importer;

import java.util.Properties;
import java.io.File;
import org.jvnet.olt.util.FileCommand;
import org.jvnet.olt.alignment_import.TMXImportImpl;

/**
 *
 * @author  jc73554
 */
public class AlignImportFileCommand implements FileCommand
{    
    private TMXImportImpl importEngine;
    
    public AlignImportFileCommand(Properties properties, String attribsFile, String useIndex) throws Exception
    {
        String url = (String)properties.get((Object)"index.url");
        importEngine = new TMXImportImpl(attribsFile, properties, useIndex,url);
    }
    
    public int run(File file) throws Exception
    {
        importEngine.importFile(file);
        return 1;
    } 
        
    public void cleanUp() throws Exception
    {
        importEngine.releaseConnection();
    }
    
}
