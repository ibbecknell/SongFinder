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

		HttpSession session = request.getSession();
		//if user is logged in, redirect
		if(session.getAttribute(USERNAME) != null && session.getAttribute(PASSWORD) !=null) {
			response.sendRedirect(response.encodeRedirectURL("/search"));
			return;
		}
		
		String status = request.getParameter(STATUS);
		boolean loggedOut = false;

		if(status!= null && status.equals(LOGGED_OUT)){

			loggedOut = true;
		}
		boolean statusok = status != null && status.equals(ERROR)?false:true;
		boolean redirected = status != null && status.equals(NOT_LOGGED_IN)?true:false;

		
		
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
			out.println(writeLogin());
		}
	}
	private String writeLogin(){
		String login = "<style>tr:hover {background-color: #141f1f} body{ color: #75a3a3; margin: 25px; background-color: #0a0f0f; font: 15px Verdana, Geneva, sans-serif; } </style><head><title>Song Finder</title></head>"
		+ "<h1><center> Welcome to Song Finder!</center></h1><br/>"
		+"<h3><center>Please Enter your username and password, or register</center></h3>"
		+"<center><form name=\"name\" action=\"verifyuser\" method=\"post\">"
		+ "Username:<br>"
		+"<input type=\"text\" name=\"username\"/><br/>"
		+"<br/>Password:<br>"
		+"<input type=\"password\" name=\"password\"/><br/>"
		+"<br/><input type=\"submit\" value=\"Login\"/>"
		+"</form>"
		+ "<form name=\"newUser\" action=\"register\" method=\"get\"> "
		+ "<input type=\"submit\" value=\"Register New User\"/></form></center>";
		
		return login;
	}
}
