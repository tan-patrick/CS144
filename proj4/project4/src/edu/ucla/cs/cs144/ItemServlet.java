package edu.ucla.cs.cs144;

import java.io.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.*;

import java.text.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;

import java.util.Date;

// class Bidder{
// 	public Bidder(){

// 	}
// }

// class BidderComparator implements Comparator<Bidder>{
 
// 	@Override
// 	public int compare(Bidder o1, Bidder o2) {
// 		return o1.size - o2.size;
// 	}
// }

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    public static Comparator<Element> BidComparator = new Comparator<Element>(){
    	public int compare(Element e1, Element e2){
    		String time1 = getElementTextByTagNameNR(e1, "Time");
    		String time2 = getElementTextByTagNameNR(e2, "Time");
			Date timeDate1 = new Date(); 
			Date timeDate2 = new Date();
			SimpleDateFormat inputDF = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");


			try{
				timeDate1 = inputDF.parse(time1);
				timeDate2 = inputDF.parse(time2);
			}
			catch(Exception e){}

			return timeDate2.compareTo(timeDate1);
    	}
    };
	
	    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
	static String processAttrString(String attr)
	{
		if (attr.equals("\\N"))
			return "none <br>";
		else
			return attr + "<br>";
	}	
	
	static String processItemXML(String xml)
	{
		Element e = null; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			e = document.getDocumentElement();
		}
		catch(Exception error){};
		
		
		
		String itemInfo = "";
	
		//date conversion stuff 
		Date timeDate = new Date(); 
		SimpleDateFormat inputDF = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		SimpleDateFormat outputDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		//extract information into strings 
		//money: currently, firstBid, buyPrice, amount - must rmv commas 
		//ITEM INFO
		String itemID = e.getAttribute("ItemID");
		String name = getElementTextByTagNameNR(e, "Name");
		String currently = getElementTextByTagNameNR(e, "Currently");
		//currently = currently.substring(1, currently.length());
		//currently = currently.replace(",", "");
		String buy_price = getElementTextByTagNameNR(e, "Buy_Price");
		//buy price is nullable
		if (getElementTextByTagNameNR(e, "Buy_Price") == "")
			buy_price = "\\N";
		else{
			//buy_price = buy_price.substring(1, buy_price.length());
			//buy_price = buy_price.replace(",", "");
		}
		String firstBid = getElementTextByTagNameNR(e, "First_Bid");
		firstBid = firstBid.substring(1, firstBid.length());
		firstBid = firstBid.replace(",", "");
		String numberOfBids = getElementTextByTagNameNR(e, "Number_of_Bids");
		Element locationElement = getElementByTagNameNR(e, "Location");
		String location = getElementText(locationElement);
		String latitude = locationElement.getAttribute("Latitude");
		String longitude = locationElement.getAttribute("Longitude");
		//lat and long are nullable
		if (locationElement.getAttribute("Latitude") == "")
			latitude = "\\N";
		if (locationElement.getAttribute("Longitude") == "")
			longitude = "\\N";
		String country = getElementTextByTagNameNR(e, "Country");
		String started = getElementTextByTagNameNR(e, "Started");		
		String ends = getElementTextByTagNameNR(e, "Ends");
		try {
			timeDate = inputDF.parse(started);
			started = outputDF.format(timeDate);
			timeDate = inputDF.parse(ends);
			ends = outputDF.format(timeDate);
		}
		catch (Exception e6)
		{}
		String description = getElementTextByTagNameNR(e, "Description");
		
		//SELLER INFO 
		Element seller = getElementByTagNameNR(e, "Seller");
		String sellerID = seller.getAttribute("UserID");
		String sellerRating = seller.getAttribute("Rating");
		
		//append onto the items.dat file
		itemInfo += "<b>Item ID: </b>\t" + processAttrString(itemID) + 
					"<b>Item Name: </b>\t" + processAttrString(name);
					
		
		//append onto the sellers.dat file the seller info  
		//sellersOut.write(sellerID + columnSeparator + sellerRating + "\n"); //remember to rmv unique using sort/uniq 
			
		//extract and append categories.dat file 
		Element[] cats = getElementsByTagNameNR(e, "Category");
		itemInfo += "<b>Categories: </b>\t";
		for (int i = 0; i < cats.length; i++)
		{
			//categoriesOut.write(itemID + columnSeparator + getElementText(cats[i]) + "\n");
			itemInfo += getElementText(cats[i]);
			if (i < cats.length-1)
				itemInfo += ", ";
			else 
				itemInfo += "<br>";
		}
		
		itemInfo += "<b>Current Bid: </b>\t" +  processAttrString(currently)  +
					"<b>Buy Price: </b>\t" + processAttrString(buy_price) + 
					"<b>First Bid: </b>\t" + processAttrString(firstBid)  + 
					"<b>Number of Bids: </b>\t" + processAttrString(numberOfBids) + 
					"<b>Location: </b>\t" + processAttrString(location) + 
					"<b>Latitude: </b>\t" +processAttrString(latitude)+ 
					"<b>Longitude: </b>\t" +processAttrString(longitude) +   
					"<b>Country: </b>\t" +processAttrString(country) + 
					"<b>Time Bidding Started: </b>\t" +processAttrString(started) +  
					"<b>Time Bidding Ends: </b>\t" +processAttrString(ends) + 
					"<b>Seller ID: </b>\t" +processAttrString(sellerID) +  
					"<b>Seller Rating: </b>\t" +processAttrString(sellerRating) +  
					"<b>Item Description: </b>\t" +processAttrString(description);
		
		
		
		//extract bids info and append onto user.dat and bids.dat 
		Element bids = getElementByTagNameNR(e, "Bids");
		Element[] bidsList = getElementsByTagNameNR(bids, "Bid");
		Arrays.sort(bidsList, BidComparator);
		itemInfo += "<h1>Bids </h1>";
		
		if (bidsList.length == 0)
			itemInfo += "No Bids<br>";
		
		for (int i = 0; i < bidsList.length; i++)
		{
			//get bid info 
			
			String time = getElementTextByTagNameNR(bidsList[i], "Time");

			try {
				timeDate = inputDF.parse(time);
			}
			catch(Exception e5){}
			time = outputDF.format(timeDate);
			
			String amount = getElementTextByTagNameNR(bidsList[i], "Amount");
			//amount = amount.substring(1, amount.length());
			//amount = amount.replace(",", "");
			//get bidder info 
			Element bidder = getElementByTagNameNR(bidsList[i], "Bidder");
			String bidderID = bidder.getAttribute("UserID");
			String bidderRating = bidder.getAttribute("Rating");
			String bidderLocation = getElementTextByTagNameNR(bidder, "Location");
			String bidderCountry = getElementTextByTagNameNR(bidder, "Country");
			
			itemInfo += "<b>Bid Info </b><br>";
			itemInfo += "<b>Bidder ID: </b>\t" +processAttrString(bidderID)+
						"<b>Bidder Rating: </b>\t" +processAttrString(bidderRating)+
						"<b>Bidder Location: </b>\t" +processAttrString(bidderLocation)+
						"<b>Bidder Country: </b>\t" +processAttrString(bidderCountry)+
						"<b>Time of Bid: </b>\t" +processAttrString(time)+
						"<b>Bid Amount: </b>\t" +processAttrString(amount) +
						"<br><br>";
			
			//biddersOut.write(bidderID + columnSeparator + bidderRating + columnSeparator + bidderLocation + columnSeparator + bidderCountry + "\n");
			//bidsOut.write(bidderID + columnSeparator + time + columnSeparator + amount + columnSeparator + itemID + "\n");	
		}
	
		return itemInfo;
		
	}
	
	static String processLatXML(String xml)
	{
		Element e = null; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			e = document.getDocumentElement();
		}
		catch(Exception error){};	
		
		Element locationElement = getElementByTagNameNR(e, "Location");
		String latitude = locationElement.getAttribute("Latitude");
		return latitude; 
	}
	static String processLngXML(String xml)
	{
		Element e = null; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			e = document.getDocumentElement();
		}
		catch(Exception error){};	
		
		Element locationElement = getElementByTagNameNR(e, "Location");
		String longitude = locationElement.getAttribute("Longitude");
		return longitude; 
	}
	static String processLocXML(String xml)
	{
		Element e = null; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			e = document.getDocumentElement();
		}
		catch(Exception error){};	
		
		Element locationElement = getElementByTagNameNR(e, "Location");
		String location = getElementText(locationElement);
		location = location.replace("'", "\\'");
		return "'" + location + "'"; 
	}
	
	static boolean checkFailed(String xml)
	{
		Element e = null; 
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			e = document.getDocumentElement();
			return false; 
		}
		catch(Exception error){
			return true; 
		}	
		
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		String itemId = request.getParameter("itemId");
		
		String itemXML = AuctionSearchClient.getXMLDataForItemId(itemId);
		
		if (checkFailed(itemXML))
		{
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head><title>Error!</title></head>");
			out.println("<body>");
			out.println("Your search did not yield any results.");
			out.println("<form action='/eBay/item' method='GET'>");
			out.println("Search another Item via ItemID:");
			out.println("<input type='text' name='itemId' value=''>");
			out.println("<input type='submit' value='Submit'>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			out.close();
			return;
		}

		
		String itemInfo = processItemXML(itemXML);
		request.setAttribute("item", itemInfo);
		
		String lat = processLatXML(itemXML);
		String lng = processLngXML(itemXML);
		String loc = processLocXML(itemXML);
		
		if (lat.equals(""))
		{
			lat = "'empty'";
			lng = "'empty'";
		}
	
		//pass latitude and longitude to the jsp to use in google maps
		request.setAttribute("lat", lat);
		request.setAttribute("lng", lng);
		request.setAttribute("loc", loc);
		request.getRequestDispatcher("/item.jsp").forward(request, response);
    }
}
