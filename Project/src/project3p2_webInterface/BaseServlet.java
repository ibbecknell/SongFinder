package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet superclass with methods common to all servlets for this
 * application.
 * 
 * @author srollins
 *
 */
public class BaseServlet extends HttpServlet {
	public static final String DATA = "data";
	public static final String UUID = "uuid";
	public static final String ITEM = "item";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	
	public static final String DELETE = "delete";
	public static final String STATUS = "status";
	public static final String ERROR = "error";
	public static final String NOT_LOGGED_IN = "not_logged_in";
	public static final String LOGGED_IN = "logged_in";
	
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		return response.getWriter();
	}

	protected String writeHTML() {
		String responseHtml = "<html>" + "<head><title>Song Finder</title>"
				+ "<style> p { padding-top: 1%; border-top: solid; border-top-width: 1px; border-top-color: #A9A9A9; }</style>"
				+ "</head>" + "<body>" + "<h1><center>Song Finder</center></h1>"
				+ "<form action=\"songs\" method=\"get\">"
				+ "Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.<br/>"
				+ "<p>Search Type: "
				+ "<select name = \"queryType\"><optgroup><option value = \"artist\">Artist</option><option value = \"title\">Song Title</option><option value = \"tag\">Tag</option></optgroup></select>"
				+ " Query: <input type=\"text\" name=\"songquery\"> "
				+ "<input type=\"submit\" value=\"Submit\"></form></p>";
		return responseHtml;
	}
	
	protected String writeUserInfo(String name){
		String responseHtml ="<style> div { position: absolute; right: 50px; width: 300px; padding: 0px;}</style><div>Hello, "+ name + " | " +writeLogout()+"</div>";
		return responseHtml;
	}
	
	protected String writeLogout(){
		String logout = " <a href=\"LogoutServlet?method=\"get\">Logout</a>";
//		String responseHtml = "<form action=\"logout\" method=\"get\">"
//				+ "<input type=\"submit\" value=\"Log Out\"></form>";
		return logout;
	}
	
	/*
	 * Given a request, return the name found in the 
	 * Cookies provided.
	 */
	protected String getCookieValue(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();

		String name = null;
		
		if(cookies != null) {
			//for each cookie, if the key is name, store the value
			for(Cookie c: cookies) {
				if(c.getName().equals(key)) {
					name = c.getValue();
				}
			}
		}
		return name;
	}
	
	/*
	 * Given a request, return the value of the parameter with the
	 * provided name or null if none exists.
	 */
	protected String getParameterValue(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
}