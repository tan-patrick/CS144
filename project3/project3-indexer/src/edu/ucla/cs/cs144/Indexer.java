package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() {

    Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */

	try{
    Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index-1"));
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
    config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
    IndexWriter indexWriter = new IndexWriter(indexDir, config);
    // indexWriter.deleteAll();
    String itemID, name, description, categories, fullSearchableText;
    int count = 0;
        PreparedStatement s = conn.prepareStatement("SELECT ItemId, Name, Description FROM Items");
        // Statement s = conn.createStatement();
        PreparedStatement s2 = conn.prepareStatement("SELECT CategoryName FROM Categories WHERE itemID = ?");

        // ResultSet rs = s.executeQuery("SELECT ItemId, Name, Description FROM Items");
        ResultSet rs = s.executeQuery();
        ResultSet resCat; 

        while( rs.next() ){
             categories = "";

             itemID = rs.getString("ItemID");
             name = rs.getString("Name");
             description = rs.getString("Description");
             
             s2.setString(1, itemID);
             resCat = s2.executeQuery();
             while (resCat.next()){
                categories += " " + resCat.getString("CategoryName");
             }

            Document doc = new Document();
            doc.add(new StringField("id", itemID, Field.Store.YES));
            doc.add(new StringField("name", name, Field.Store.YES));
            fullSearchableText = name + " " + description + categories;
            doc.add(new TextField("searchable", fullSearchableText, Field.Store.NO));
            indexWriter.addDocument(doc);

            System.out.println(count + " " + itemID);
            // System.out.println(fullSearchableText);
            count++;
        }
		indexWriter.close();
    }
    catch(Exception ex) {
        System.out.println("Indexer Error.");
    }
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
