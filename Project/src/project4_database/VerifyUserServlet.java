package project4_database;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project3p2_webInterface.BaseServlet;

public class VerifyUserServlet extends BaseServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//VerifyUser does not accept GET requests. Just redirect to login with error status.
		response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + ERROR));
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		System.out.println("username: " + username);
		
		if(username == null || username.trim().equals("") || username.isEmpty() && password == null || password.trim().equals("") || password.isEmpty()) {
			
			response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}
		
		
		//OPTION 2: Store volatile data in a single, shared object that is stored in the servlet context and
		//can therefore be accessed by all users/sessions/servlets.
		
		//map id to name and userinfo
		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);
//		we assume no username conflicts and provide no ability to register for our service!
//		data.addUser(name);  
		try {
//			System.out.println("password: " + password);
			if(data.verifyUser(username, password)){
				System.out.println("user is in the database");
				HttpSession session = request.getSession();
				session.setAttribute(USERNAME, username);
				session.setAttribute(PASSWORD, password);
				response.sendRedirect(response.encodeRedirectURL("/search"));
				return;
			} else {
				System.out.println("username: " + username + " not in db");
				response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + ERROR));
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//redirect to list
//		response.sendRedirect(response.encodeRedirectURL("/search"));
		
	}
}
