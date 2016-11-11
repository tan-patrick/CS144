/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
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
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
		//create the six files 
		BufferedWriter itemsOut = null;
		BufferedWriter categoriesOut = null;
		BufferedWriter sellersOut = null;
		BufferedWriter biddersOut = null;
		BufferedWriter bidsOut = null;
		try {
			//create streams for the six files
			FileWriter fstreamItems = new FileWriter("Items.dat", true);
			FileWriter fstreamCategories = new FileWriter("Categories.dat", true);
			FileWriter fstreamSellers = new FileWriter("Sellers.dat", true);
			FileWriter fstreamBidders = new FileWriter("Bidders.dat", true);
			FileWriter fstreamBids = new FileWriter("Bids.dat", true);
			//link the streams to their writers
			itemsOut = new BufferedWriter(fstreamItems);
			categoriesOut = new BufferedWriter(fstreamCategories);
			sellersOut = new BufferedWriter(fstreamSellers);
			biddersOut = new BufferedWriter(fstreamBidders);
			bidsOut = new BufferedWriter(fstreamBids);
		}
		catch(IOException e){
			System.err.println("Error: " + e.getMessage());
		}
		
		//create a list of all the items
		
		Element[] loi = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
		//foreach item, process it 
		int size = loi.length; 
		for (int i = 0; i < size; i++){
			try {
				processItem(loi[i], itemsOut, categoriesOut, sellersOut, biddersOut, bidsOut);
			}
			catch(IOException e){
				System.err.println("Error: " + e.getMessage());
			}
		}
		try {
			itemsOut.close();
			categoriesOut.close();
			sellersOut.close();
			biddersOut.close();
			bidsOut.close();
		}
		catch (IOException e){
			System.err.println("Error: " + e.getMessage());
		}
        /**************************************************************/
        
        //recursiveDescent(doc, 0);
    }
    
	static void processItem(Element e, BufferedWriter itemsOut, BufferedWriter categoriesOut, 
		BufferedWriter sellersOut, BufferedWriter biddersOut, 
		BufferedWriter bidsOut) throws IOException{
		
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
		currently = currently.substring(1, currently.length());
		currently = currently.replace(",", "");
		String buy_price = getElementTextByTagNameNR(e, "Buy_Price");
		//buy price is nullable
		if (getElementTextByTagNameNR(e, "Buy_Price") == "")
			buy_price = "\\N";
		else{
			buy_price = buy_price.substring(1, buy_price.length());
			buy_price = buy_price.replace(",", "");
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
		itemsOut.write(itemID + columnSeparator + name + columnSeparator + currently + 
					columnSeparator + buy_price + columnSeparator + firstBid + columnSeparator + 
					numberOfBids + columnSeparator +  location + columnSeparator + latitude
					+ columnSeparator + longitude + columnSeparator +  country 
					+ columnSeparator + started + columnSeparator +  ends + columnSeparator + 
					sellerID + columnSeparator + description + "\n");
		
		//append onto the sellers.dat file the seller info  
		sellersOut.write(sellerID + columnSeparator + sellerRating + "\n"); //remember to rmv unique using sort/uniq 
			
		//extract and append categories.dat file 
		Element[] cats = getElementsByTagNameNR(e, "Category");
		for (int i = 0; i < cats.length; i++)
		{
			categoriesOut.write(itemID + columnSeparator + getElementText(cats[i]) + "\n");
		}
		
		//extract bids info and append onto user.dat and bids.dat 
		Element bids = getElementByTagNameNR(e, "Bids");
		Element[] bidsList = getElementsByTagNameNR(bids, "Bid");
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
			amount = amount.substring(1, amount.length());
			amount = amount.replace(",", "");
			//get bidder info 
			Element bidder = getElementByTagNameNR(bidsList[i], "Bidder");
			String bidderID = bidder.getAttribute("UserID");
			String bidderRating = bidder.getAttribute("Rating");
			String bidderLocation = getElementTextByTagNameNR(bidder, "Location");
			String bidderCountry = getElementTextByTagNameNR(bidder, "Country");
			
			biddersOut.write(bidderID + columnSeparator + bidderRating + columnSeparator + bidderLocation + columnSeparator + bidderCountry + "\n");
			bidsOut.write(bidderID + columnSeparator + time + columnSeparator + amount + columnSeparator + itemID + "\n");
			
		}
	}
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
