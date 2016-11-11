package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class InputCardServlet extends HttpServlet implements Servlet {
       
    public InputCardServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	HttpSession session = request.getSession(true);
		request.setAttribute("itemID", (String) session.getAttribute("itemID"));
		request.setAttribute("itemName", (String) session.getAttribute("itemName"));
		request.setAttribute("buyPrice", (String) session.getAttribute("buyPrice"));
		
		String host = request.getServerName();
		int port = request.getServerPort();	
		request.setAttribute("host", host);
		
		request.getRequestDispatcher("/inputCard.jsp").forward(request, response);
		
		
		
    }
}
