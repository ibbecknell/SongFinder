package project4_database;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project3p2_webInterface.BaseServlet;

public class LogoutServlet extends BaseServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getSession().invalidate();
		response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + LOGGED_OUT));
		
	}
}
