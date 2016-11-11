package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
			
		SearchResult[] rs = new SearchResult[0];
		try{
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index-1"))));
	        QueryParser parser = new QueryParser("searchable", new StandardAnalyzer());
	        Query queryInput = parser.parse(query);        
	        TopDocs queryResult = searcher.search(queryInput, numResultsToSkip + numResultsToReturn);
	        //System.out.println("Results found: " + queryResult.totalHits);
			
			int size = Math.min(queryResult.totalHits, numResultsToReturn);
			rs = new SearchResult[size];
			
			
	        ScoreDoc[] hits = queryResult.scoreDocs;
	        for (int i = numResultsToSkip; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            // System.out.println(i + " " + doc.get("id") + " " + doc.get("name"));
				
	            rs[i-numResultsToSkip] = new SearchResult(doc.get("id"), doc.get("name"));
	        }
	    }
	    catch(Exception e){
	    	System.out.println(e.toString());
	    }
		
		
		
	    return rs;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		
		Connection conn = null;

		SearchResult[] ANSWER1 = new SearchResult[0];
		SearchResult[] ANSWER2 = new SearchResult[0];
        // create a connection to the database to retrieve Items from MySQL
		try {
			conn = DbManager.getConnection(true);
			//parse the region and make it a geometry object 
			double lx = region.getLx();
			double ly = region.getLy();
			double rx = region.getRx();
			double ry = region.getRy();
			//make the spatial query 
			String preparedQuery = "SELECT ItemID FROM ItemLocations WHERE MBRContains(GeomFromText('POLYGON((" + lx + " " + ly + ", " + lx + " " + ry + ", " + rx + " " + ry + ", " + rx + " " + ly + ", " + lx + " " + ly + "))'), Locations)";
			PreparedStatement s = conn.prepareStatement(preparedQuery);
			ResultSet rs = s.executeQuery();
			
			
			SearchResult[] basic = basicSearch(query, 0, Integer.MAX_VALUE);
			ANSWER1 = new SearchResult[basic.length];
			
			//loop through and find the intersection 
			int count = 0;
			while(rs.next() && count < numResultsToReturn){
				
				String id = rs.getString("ItemID");
				for(int i = 0; i < basic.length; i++){
					if(basic[i].getItemId().equals(id)){
						if(numResultsToSkip > 0){
							numResultsToSkip--;
						}
						else{
							ANSWER1[count] = new SearchResult(id, basic[i].getName());
							count++;
						}
						break;
					}
				}
				
			}
			
			ANSWER2 = new SearchResult[count];
			for (int i = 0; i < count; i++)
			{
				ANSWER2[i] = ANSWER1[i];
			}
			
			
			
		} catch (Exception ex) {
			System.out.println("Spatial Search Error");
		}
		
		
		return ANSWER2;
	}

	public String getXMLDataForItemId(String itemId) {
		
		Connection conn = null;
		String XML = "";
		try {
		
			//set up time formatting objects
			Date timeDate = new Date(); 
			SimpleDateFormat inputDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat outputDF = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
			
			//set up money formatting
			DecimalFormat formatter = new DecimalFormat("#,###.00");

			//establish connection and make a query for the itemid 
			conn = DbManager.getConnection(true);
			String preparedQueryItems = "SELECT * FROM Items WHERE ItemID = " + itemId;
			PreparedStatement s = conn.prepareStatement(preparedQueryItems);
			ResultSet rs = s.executeQuery();
			
			//if no items, then return an empty string, as per spec 
			if (!rs.isBeforeFirst())
				return "";
			
			rs.next();
			//get the item object and begin parsing out it's data 
			String ItemID = rs.getString("ItemID");
			String Name = XMLescape(rs.getString("Name"));
			double currently = rs.getDouble("Currently");
			
			double First_Bid = rs.getDouble("First_Bid");
			int Number_of_Bids = rs.getInt("Number_of_Bids");
			String Longitude = rs.getString("Longitude");
			String Country =  XMLescape(rs.getString("Country"));
			String Started = rs.getString("Started");
			String Ends = rs.getString("Ends");
			String SellerID =  XMLescape(rs.getString("SellerID"));
			String Description =  XMLescape(rs.getString("Description"));
			//print out XML stuff 
			XML += "<Item ItemID=\"" + ItemID + "\">\n";
			XML += "	<Name>" + Name + "</Name>\n";
			
			//extract category info 
			PreparedStatement s2 = conn.prepareStatement("SELECT CategoryName FROM Categories WHERE ItemID = " + itemId);
			ResultSet rs2 = s2.executeQuery();
			while(rs2.next()){
				XML += "	<Category>" +  XMLescape(rs2.getString("CategoryName")) + "</Category>\n";
			}
			//misc item info 
			XML += "	<Currently>$" + formatter.format(currently) + "</Currently>\n";
			double Buy_Price = rs.getDouble("Buy_Price");
			if (!rs.wasNull()){
				XML += "	<Buy_Price>$" + formatter.format(Buy_Price) + "</Buy_Price>\n";
			}
			XML += "	<First_Bid>$" + formatter.format(First_Bid) + "</First_Bid>\n";
			XML += "	<Number_of_Bids>" + Number_of_Bids + "</Number_of_Bids>\n";
			
			
			PreparedStatement s3 = conn.prepareStatement("SELECT * FROM Bids WHERE ItemID = " + itemId);
			ResultSet rs3 = s3.executeQuery();
			PreparedStatement s4 = conn.prepareStatement("SELECT * FROM Bidders WHERE UserID = ?");
			//extract bids info 
			if (rs3.isBeforeFirst())
			{
				XML += "	<Bids>\n";
				while(rs3.next()){
					String BidderID =  XMLescape(rs3.getString("UserID"));
					s4.setString(1, BidderID);
					ResultSet rs4 = s4.executeQuery();
					rs4.next();
					String Rating = rs4.getString("Rating");
					String BidTime = rs3.getString("Time");
					double BidAmount = rs3.getDouble("Amount");
					
					XML += "		<Bid>\n";
					XML += "			<Bidder Rating=\"" + Rating + "\" UserID=\"" + BidderID + "\">\n";
					String BidderLocation = rs4.getString("Location");
					if (!rs4.wasNull())
					{
						BidderLocation =  XMLescape(rs4.getString("Location"));
						XML += "				<Location>" + BidderLocation + "</Location>\n";
					}
					String BidderCountry =  rs4.getString("Country");
					if (!rs4.wasNull())
					{
						BidderCountry =  XMLescape(rs4.getString("Country"));
						XML += "				<Country>" + BidderCountry + "</Country>\n";
					}
					XML += "			</Bidder>\n";
					
					//format the date(s)
					try{
						timeDate = inputDF.parse(BidTime);
						BidTime = outputDF.format(timeDate);
					}
					catch(Exception e3){}
					
					XML += "			<Time>" + BidTime + "</Time>\n";
					XML += "			<Amount>$" + formatter.format(BidAmount) +"</Amount>\n";
					XML += "		</Bid>\n";
					
				}
				XML += "	</Bids>\n";
			}
			else 
			{
				XML += "	<Bids />\n";
			}
			
			String Location =  rs.getString("Location");
			String Latitude =  rs.getString("Latitude");
			if (!rs.wasNull())
			{
				XML += "	<Location Latitude=\"" + Latitude + "\" Longitude=\"" + Longitude + "\">" + Location + "</Location>\n";
			}
			else{
				XML += "	<Location>" + Location + "</Location>\n";	
			}
			XML += "	<Country>" + Country + "</Country>\n";
			
			//format started and ends 
			try{
				timeDate = inputDF.parse(Started);
				Started = outputDF.format(timeDate);
				timeDate = inputDF.parse(Ends);
				Ends = outputDF.format(timeDate);
			}
			catch(Exception e3){}
			XML += "	<Started>" + Started + "</Started>\n";
			XML += "	<Ends>" + Ends + "</Ends>\n";
			
			//get seller info 
			PreparedStatement s5 = conn.prepareStatement("SELECT * FROM Sellers WHERE UserID='" + SellerID + "'");
			ResultSet rs5 = s5.executeQuery();
			rs5.next();
			int SellerRating = rs5.getInt("Rating");

			XML += "	<SellerID Rating=\"" + SellerRating + "\" UserID=\"" + SellerID + "\" />\n";
			XML += "	<Description>" + Description + "</Description>\n";
			XML += "</Item>";
			
		
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		
		return XML;
	}
	
	public String XMLescape(String original) {
		original = original.replace("&", "&amp;");
		original = original.replace("<", "&lt;");
		original = original.replace(">", "&gt;");
		return original;
	}
	
	public String echo(String message) {
		return message;
	}

}

