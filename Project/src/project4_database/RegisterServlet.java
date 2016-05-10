package project4_database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project3p2_webInterface.BaseServlet;

public class RegisterServlet extends BaseServlet{
	String responseHtml = "<head><title>New User Registration</title></head>"
			+"<h1><center> Welcome to Song Finder!</center></h1><br/>"
			+"<h3><center>Please complete the form below</center></h3>"
			
			+"<center><form name=\"name\" action=\"register\" method=\"post\">"
			+"<br/>First Name:"
			+"<input type=\"text\" name=\"firstname\"/><br/>"
			+"<br/>Last Name:"
			+"<input type=\"text\" name=\"lastname\"/><br/>"
			+"<br/>Username:"
			+"<input type=\"text\" name=\"username\"/><br/>"
			+"<br/>Password:"
			+"<input type=\"text\" name=\"password1\"/><br/>"
			+"<br/>Re-enter Password:"
			+"<input type=\"text\" name=\"password2\"/><br/>"
			+"<input type=\"submit\" value=\"Register New User\"/>"
			+"</form></center>";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		PrintWriter out = prepareResponse(response);
		out.println(responseHtml);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String userName = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");

		PrintWriter out = prepareResponse(response);
		String errorResponse= " ";
		if(firstName.isEmpty()) {
			errorResponse = errorResponse + "<center><br/>Please enter your first name";

		}
		if(lastName.isEmpty()) {
			errorResponse = errorResponse + "<center><br/> Please enter your last name";

		}
		if(userName.isEmpty()){
			errorResponse = errorResponse + "<center><br/> Please enter a username";

		}
		if(password1.isEmpty()){
			errorResponse = errorResponse + "<center><br/> Please enter a password";

		}
		if(password2.isEmpty()){
			errorResponse = errorResponse + "<center><br/> Please re-enter your password";

		}
		if(!password1.equals(password2)){
			errorResponse = errorResponse + "<center><br/> Passwords do not match!";
		}

		if(!errorResponse.trim().isEmpty()){
			out.println(responseHtml + errorResponse );
			return;
		} 
		else {
			try {
				if(DBHelper.verifyUsername(userName)){
					out.println("<center><font color=\"red\">Username already Exists!</font></center>");
					return;
				
				} else if(DBHelper.insertUser(firstName, lastName, userName, password1)){
					HttpSession session = request.getSession();
					session.setAttribute(USERNAME, userName);
					session.setAttribute(PASSWORD, password1);
					response.sendRedirect(response.encodeRedirectURL("/search"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
