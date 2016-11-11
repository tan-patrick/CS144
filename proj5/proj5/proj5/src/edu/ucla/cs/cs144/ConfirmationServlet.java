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
import java.util.Date;

public class ConfirmationServlet extends HttpServlet implements Servlet {
       
    public ConfirmationServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	HttpSession session = request.getSession(true);
		request.setAttribute("itemID", (String) session.getAttribute("itemID"));
		request.setAttribute("itemName", (String) session.getAttribute("itemName"));
		request.setAttribute("buyPrice", (String) session.getAttribute("buyPrice"));
		request.setAttribute("creditCard", request.getParameter("creditCard"));
		request.setAttribute("time", new Date(session.getLastAccessedTime()));
		
		request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
		
		
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
