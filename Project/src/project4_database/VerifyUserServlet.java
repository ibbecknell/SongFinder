package project4_database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import project3p2_webInterface.BaseServlet;

public class VerifyUserServlet extends BaseServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//VerifyUser does not accept GET requests. Just redirect to login with error status.
		response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + ERROR));
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
//		System.out.println("username: " + username);
		
		if(username == null || username.trim().equals("") || username.isEmpty() && password == null || password.trim().equals("") || password.isEmpty()) {

			response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}

		
		//map id to name and userinfo
//		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);

		try {
//			System.out.println("password: " + password);
//			System.out.println("---------verify user-----------");
			if(DBHelper.verifyUser(username, password)){
//				System.out.println("user is in the database");
				HttpSession session = request.getSession();
				session.setAttribute(USERNAME, username);
				session.setAttribute(PASSWORD, password);
//				session.setAttribute(USER_FAVS, new ArrayList<JSONObject>());
//				session.setAttribute(USER_PROFILE, DBHelper.getUser(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD));
				response.sendRedirect(response.encodeRedirectURL("/search"));
//				System.out.println("------------end of verify search------------");
				return;
			} else {
//				System.out.println("username: " + username + " not in db");
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + ERROR));
//				System.out.println("------------end of verify search------------");
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
