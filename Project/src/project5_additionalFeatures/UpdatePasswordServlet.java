package project5_additionalFeatures;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project3p2_webInterface.BaseServlet;
import project4_database.DBHelper;

public class UpdatePasswordServlet extends BaseServlet{
	private String responseHtml = "<style>input[type=submit] {background-color: #75a3a3;"
										    + "color: 0a0f0f;"
											+ "width: 150px;"
										    + "height: 40px"
										    + "padding: 14px;"
										    +"font-size: 14px;"
										    +"border: none;"
										    +"cursor: pointer;}"
										    +"tr:hover {background-color: #141f1f} body{ color: #75a3a3; margin: 25px; background-color: #0a0f0f; font: 15px Verdana, Geneva, sans-serif; } </style><head><title>Song Finder</title></head>"
			+ "<h1><center> Welcome to Song Finder!</center></h1><br/>"
			+"<h3><center>Please Enter your old password and new password</center></h3>"
			+"<center><form name=\"name\" action=\"update_password\" method=\"post\">"
			+ "Old Password:<br>"
			+"<input type=\"password\" name=\"old_pw\"/><br/>"
			+"<br/>New Password:<br>"
			+"<input type=\"password\" name=\"new_pw1\"/><br/>"
			+"<br/>Re-Enter New Password:<br>"
			+"<input type=\"password\" name=\"new_pw2\"/><br/>"
			+"<br/><input type=\"submit\" value=\"Change Password\"/>"
			+"</form>"
			+"<form name = \"search\" action=\"login\" method=\"get\">"
			+"<br/><input type=\"submit\" value=\"Back to Search\"/>"
			+"</form>";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		
		try {
			if(username == null || !DBHelper.verifyUser(username, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			}  else {
				PrintWriter out = prepareResponse(response);
				out.println(responseHtml);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		String old_PW = request.getParameter("old_pw");
		String newPW1 = request.getParameter("new_pw1");
		String newPW2 = request.getParameter("new_pw2");
		PrintWriter out = prepareResponse(response);
		out.println(responseHtml);
		try {
			if(DBHelper.verifyUser(username, password) && !old_PW.equals(newPW1) && newPW1.equals(newPW2) && !old_PW.isEmpty() && !newPW1.isEmpty() && !newPW2.isEmpty()){
				System.out.println("updating password");
				DBHelper.updatePassword(username, newPW1);
				session.setAttribute(PASSWORD, newPW1);
				response.sendRedirect(response.encodeRedirectURL("/search"));
				return;
			} else if(old_PW.equals(newPW1)){
				out.println("<mark class=\"yellow\">Enter a new password!</mark><br/>");
			} else if(!newPW1.equals(newPW2)){
				out.println("<mark class=\"red\">New Passwords Do Not Match!</mark><br/>");
			} else if(!old_PW.isEmpty()){
				out.println("<mark class=\"red\">Enter your original password!</mark><br/>");
			} else if(!newPW1.isEmpty()){
				out.println("<mark class=\"yellow\">Enter a new password!</mark><br/>");
			} else if(!newPW2.isEmpty()){
				out.println("<mark class=\"yellow\">Re-enter a new password!</mark><br/>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
