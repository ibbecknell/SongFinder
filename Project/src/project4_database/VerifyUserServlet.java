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
		
		if(username == null || username.trim().equals("") || username.isEmpty() && password == null || password.trim().equals("") || password.isEmpty()) {

			response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
			return;
		}

		try {
			if(DBHelper.verifyUser(username, password)){
				HttpSession session = request.getSession();
				session.setAttribute(USERNAME, username);
				session.setAttribute(PASSWORD, password);
				response.sendRedirect(response.encodeRedirectURL("/search"));
				return;
			} else {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + ERROR));
				return;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
