package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		String q = request.getParameter("q");
		String numResultsToSkip = request.getParameter("numResultsToSkip");
		String numResultsToReturn = request.getParameter("numResultsToReturn");
		
		int numResultsToSkipInt = Integer.parseInt(numResultsToSkip);
		int numResultsToReturnInt = Integer.parseInt(numResultsToReturn);
		if (numResultsToReturnInt == 0)
			numResultsToReturnInt = Integer.MAX_VALUE;
		if(numResultsToSkipInt < 0)
			numResultsToSkipInt = 0;
		if(numResultsToReturnInt < 0)
			numResultsToReturnInt = 0;
			
		int nextPageStart = numResultsToSkipInt + 10;
		int prevPageStart = numResultsToSkipInt - 10;
		if (prevPageStart < 0)
			prevPageStart = 0; 
			
		request.setAttribute("nextPageStart", Integer.toString(nextPageStart));
		request.setAttribute("prevPageStart", Integer.toString(prevPageStart));
			
		
		SearchResult[] basicResults = AuctionSearchClient.basicSearch(q, numResultsToSkipInt,numResultsToReturnInt );
		
		String results = "";
		
		for(SearchResult result : basicResults) {
			results += result.getItemId() + ": " + "<a href=\"/eBay/item?itemId=" + result.getItemId() + "\">"+ result.getName() +  "</a>" + "<br>";
		}
		
		request.setAttribute("results", results);
		
		request.getRequestDispatcher("/search.jsp").forward(request, response);
		
		
		/*PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Servlet Example</title></head>");
        out.println("<body>");
		out.println(Integer.toString(nextPageStart));
		out.println("</body>");
        out.println("</html>");
        out.close();*/
		
		
    }
}
