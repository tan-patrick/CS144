package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		// String message = "Test message";
		// String reply = as.echo(message);
		// System.out.println("Reply: " + reply);
		
		// SearchResult[] test;
		// String q;

		// q = "superman";
		// test = as.basicSearch(q, 0, 50000);
		// System.out.println("Basic Search Query: " + q);
		// System.out.println("Received " + test.length + " results");

		// q = "kitchenware";
		// test = as.basicSearch(q, 0, 50000);
		// System.out.println("Basic Search Query: " + q);
		// System.out.println("Received " + test.length + " results");

		// q = "star trek";
		// test = as.basicSearch(q, 0, 50000);
		// System.out.println("Basic Search Query: " + q);
		// System.out.println("Received " + test.length + " results");

		String query = "superman";
		SearchResult[] basicResults = as.basicSearch(query, 0, 70);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38); 
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Seacrh");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
		SearchResult[] spatialResults2 = as.spatialSearch("superman", region, 0, 200);
		System.out.println("Spatial Seacrh");
		System.out.println("Received " + spatialResults2.length + " results");
		for(SearchResult result : spatialResults2) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}

		String itemId = "1497567803";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here
	}
}
