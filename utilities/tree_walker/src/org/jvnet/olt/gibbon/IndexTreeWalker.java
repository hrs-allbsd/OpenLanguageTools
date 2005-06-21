
/*
 * Copyright  2005 Sun Microsystems, Inc. 
 * All rights reserved Use is subject to license terms.
 *
 */

/*
 * IndexTreeWalker.java
 *
 * Created on 31 October 2002, 15:28
 */

package org.jvnet.olt.gibbon;

import java.util.Properties;
import java.io.*;
import java.sql.*;

/**
 *
 * @author  jc73554
 */
public class IndexTreeWalker
{
    private Connection conn;
    private PreparedStatement stmtFetchNode;
    private Writer writer;
    private String indexName;
    private String schemaName;
    private int segmentLength = 1;
    
    /** Creates a new instance of IndexTreeWalker */
    public IndexTreeWalker(String indexName, String schemaName, String segLen, String outputFile) throws SQLException, IOException, ClassNotFoundException, NumberFormatException
    {
        this.indexName = indexName;
        this.schemaName = schemaName;
        this.segmentLength = Integer.parseInt(segLen);
        
        //  Open Connection
        conn = openConnection();
        
        //  Prepare tree walking statement
        String sql = "select keydata,hison,loson from " + indexName + "_KDTREE where rowid=?";
        stmtFetchNode = conn.prepareStatement(sql);
        
        //  Open Writer
        File file = new File(outputFile);
        FileOutputStream ostream = new FileOutputStream(file);
        writer = new BufferedWriter(new OutputStreamWriter(ostream));
        
        //  Start the XML file
        writer.write("<?xml version=\"1.0\" ?>\n");
        writer.write("<!DOCTYPE node SYSTEM \"treestruct.dtd\">\n");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if(args.length != 4)
        {
            System.out.println("Incorrect number of arguments supplied.");
            System.out.println("Usage: treewalker <index name> <schema name> <segment length> <output file>");
            System.exit(2);
        }
        try
        {
            IndexTreeWalker walker = new IndexTreeWalker(args[0], args[1], args[2], args[3]);
            walker.walkTree();
            
            System.exit(0);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    public void walkTree() throws SQLException,IOException
    {
        try
        {
            //  Get initial node
            String rootId = getRootNodeId();
            Node rootNode = getNode(rootId);
            
            //  Write out node
            writeOutNode(rootNode, 'r', "");
        }
        catch(SQLException exSql)
        {
            exSql.printStackTrace();
            conn.close();
        }
        writer.flush();
        writer.close();
    }
    
    public void writeOutNode(Node node, char type, String prefix) throws SQLException,IOException
    {
        //  Write out node data
        writer.write(prefix);
        writer.write("<node type=\"");
        writer.write(type);
        writer.write("\" rowid=\"");
        writer.write(node.getId());        
        writer.write("\" key=\"");
        writer.write(node.getKey());
        writer.write("\" >\n");
        
        //  Test for Hison: write out if present
        if((node.getHison() != null) && (!node.getHison().equals("")))
        {
            Node hison = getNode(node.getHison());
            writeOutNode(hison,'h', prefix + "  ");
        }
        
        //  Test for Loson: write out if present
        if((node.getLoson() != null) && (!node.getLoson().equals("")))
        {
            Node loson = getNode(node.getLoson());
            writeOutNode(loson,'l', prefix + "  ");
        }
        writer.write(prefix);
        writer.write("</node>\n");
    }
    
    public String getRootNodeId() throws SQLException
    {
        //  Create ststement
        Statement stmt = conn.createStatement();
        String sql = "select index_id from rootnodes where indexname='" +
        schemaName.toUpperCase() + "." +
        indexName.toUpperCase() + "' AND SEG_LENGTH=" + segmentLength;
        
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            String strId = rs.getString("INDEX_ID");
            rs.close();
            return strId;
        }
        else
        {
            rs.close();
            return null;
        }
    }
    
    public Node getNode(String id) throws SQLException
    {
        stmtFetchNode.setString(1, id);
        Node node = null;
        ResultSet rs = stmtFetchNode.executeQuery();
        
        if(rs.next())
        {
            byte[] key = rs.getBytes("KEYDATA");
            String hison = rs.getString("HISON");
            String loson = rs.getString("LOSON");
            
            node = new Node(id, hison, loson, key);
        }
        rs.close();
        return node;
    }
    
    
    public Connection openConnection() throws SQLException, IOException, ClassNotFoundException
    {
        //  Load database properties
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
        File file = new File("dbconf.properties");
        FileInputStream istream = new FileInputStream(file);
        Properties dbProps = new Properties();
        dbProps.load(istream);
        
        istream.close();
        
        //  Hopefully we can get away wit this.
        String url = dbProps.getProperty("url");
        
        //  Create database connection.
        return DriverManager.getConnection(url,dbProps);
    }
}

class Node
{
    private String id;
    private String hison;
    private String loson;
    private String strKey;
    
    public Node(String id, String hison, String loson, byte[] key)
    {
        this.id = id;
        this.hison = hison;
        this.loson = loson;
        strKey = keyToString(key);
    }
    
    public static int[] unpackKey(byte[] packedKey)
    {        
        int[] key = new int[47];
        
        for(int i = 0; i < 47; i++)
        {
            int scaledIndex = (i * 4);
            key[i] = ( ((int) packedKey[scaledIndex] & 0xff ) |
            (((int)packedKey[scaledIndex + 1] & 0xff) << 8)  |
            (((int)packedKey[scaledIndex + 2] & 0xff) << 16) |
            (((int)packedKey[scaledIndex + 3] & 0xff) << 24)  );
        }
        return key;
    }
    
    private String keyToString(byte[] packedKey)
    {
        int[] key = unpackKey(packedKey);
        
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < 47; i++)
        {
            if(i != 0)
            {
                buffer.append(",");
            }
            buffer.append(key[i]);
        }
        return buffer.toString();
    }
    
    
    /** Getter for property id.
     * @return Value of property id.
     */
    public String getId()
    {
        return id;
    }
    
    /** Getter for property hison.
     * @return Value of property hison.
     */
    public String getHison()
    {
        return hison;
    }
    
    /** Getter for property loson.
     * @return Value of property loson.
     */
    public String getLoson()
    {
        return loson;
    }

    public String getKey()
    {
        return strKey;
    }

}
