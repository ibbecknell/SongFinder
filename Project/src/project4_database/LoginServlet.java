package project4_database;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project1_librarybuilding.MusicLibrary;
import project3p2_webInterface.BaseServlet;

public class LoginServlet extends BaseServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);
//		String status = (String)getServletConfig().getServletContext().getAttribute(STATUS);
		HttpSession session = request.getSession();
//		System.out.println("--------------Login Servlet---------------");
		//if user is logged in, redirect
		if(session.getAttribute(USERNAME) != null && session.getAttribute(PASSWORD) !=null) {
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		
		String status = request.getParameter(STATUS);
		boolean loggedOut = false;
//		System.out.println("status: " + status);
//		System.out.println("status = null? " + status == null);
		if(status!= null && status.equals(LOGGED_OUT)){
//			System.out.println("status != null");
//			System.out.println("yes");
			loggedOut = true;
		}
		boolean statusok = status != null && status.equals(ERROR)?false:true;
		boolean redirected = status != null && status.equals(NOT_LOGGED_IN)?true:false;
//		boolean loggedOut = status != null && status.equals(LOGGED_OUT)? true: false;
//		
		
		
//		//output text box requesting user name
		PrintWriter out = prepareResponse(response);
		
//		//if the user was redirected here as a result of an error
		if(!statusok) {
			out.println(writeLogin() + "<h3><center><font color=\"red\">Invalid Request to Login! Please Re-Enter your Login</font></center></h3>");
		} else if(redirected) {
			out.println(writeLogin() + "<h3><center><font color=\"red\">Log in first!</font></center></h3>");
		} else if(loggedOut){
			out.println(writeLogin() + "<h3><center> You have successfully Logged Out!</center></h3>");
		} else {
//			System.out.println("error: " + statusok);
//			System.out.println("redirected: " + redirected);
//			System.out.println("loggedOut: " + loggedOut);
			
//			System.out.println(status.equals(LOGGED_OUT));

			out.println(writeLogin());
		}
		
//		System.out.println("----------------end of login Servlet-----------------");
//		out.println("<head><title>Song Finder</title></head>");
//		out.println("<h1><center> Welcome to Song Finder!</center></h1><br/>");
//		out.println("<h3><center>Please Enter your username and password, or register</center></h3>");
//		
//		out.println("<center><form name=\"name\" action=\"verifyuser\" method=\"post\">");
//		out.println("Username:");
//		out.println("<input type=\"text\" name=\"username\"/><br/>");
//		out.println("<br/>Password:");
//		out.println("<input type=\"text\" name=\"password\"/><br/>");
//		out.println("<br/><input type=\"submit\" value=\"Login\"/>");
//		out.println("</form>");
//		out.println("<form name=\"newUser\" action=\"register\" method=\"get\"> ");
//		out.println("<input type=\"submit\" value=\"Register New User\"/></form></center>");
	}
	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);
//		HttpSession session = request.getSession();
//	}
	
}
