package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //create query string
		String q = request.getParameter("q");	
		String url = "http://www.google.com/complete/search?output=toolbar&q=" + q;
		//set up and send connection to google suggest
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//create reader out of the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		//setup this page's xml writer
		PrintWriter out = response.getWriter();
		
		String googleXMLResponse = "";
		String inputLine;
		//printout and build the xml response
		while ((inputLine = in.readLine()) != null) {
			out.println(inputLine);
			googleXMLResponse += inputLine; 
		}
		in.close();
		
		//out.println("[\"a\", \"b\", \"c\"]");
        out.close();

    }
}
