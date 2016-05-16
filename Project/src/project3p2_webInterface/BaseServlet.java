package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;

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
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String FIRST_NAME = "firstname";
	public static final String LAST_NAME = "lastname";
	
	public static final String STATUS = "status";
	public static final String ERROR = "error";
	public static final String NOT_LOGGED_IN = "not_logged_in";
	public static final String USER_EXISTS = "user_exists";
	public static final String LOGGED_OUT = "logged_out";
	public static final String QUERYTYPE ="queryType";
	public static final String SONGQUERY = "songquery";
			
	
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		return response.getWriter();
	}

	protected String writeHTML() {
		String responseHtml = "<html>" + "<head><title>Song Finder</title>"
				+ "<style> th{height: 80px} tr:hover {background-color: #141f1f}  a{color: #75a3a3;} body{ color: #75a3a3; margin: 25px; background-color: #0a0f0f; font: 15px Verdana, Geneva, sans-serif; } p {  padding-top: 1%; border-top: solid; border-top-width: 1px; border-top-color: #A9A9A9; }</style>"
				+ "</head>" + "<body>" + "<h1><center>Song Finder</center></h1>"
				+ "<form action=\"songs\" method=\"get\">"
				+ "Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.<br/>"
				+ "<p>Search Type: "
				+ "<select name = \"queryType\"><optgroup><option value = \"artist\">Artist</option><option value = \"title\">Song Title</option><option value = \"tag\">Tag</option></optgroup></select>"
				+" Query: <input type=\"text\" name=\"songquery\"> "
				+ "<input type=\"submit\" value=\"Submit\"></form></p>"
				+"<form action=\"view_all\" method=\"post\">"
				+ " View All Artists : <select name = \"sortBy\"><optgroup><option value = \"artist\">Alphabetically by Artist</option><option value = \"playcount\">By Playcount</option></optgroup></select>"
				+ "<input type=\"submit\" value=\"View All\"></form></p>";
		return responseHtml;
	}
	
	protected String writeUserInfo(String name){
		String responseHtml ="<style> div { position: absolute; right: 50px; width: 300px; padding: 0px;}</style><div>Hello, "+ name + "! | " +writeLogout()+ writeDropdown()+ "</div>";
//		+writeFavs() + "</div>";
		return responseHtml;
	}
	
//	http://www.w3schools.com/css/css_dropdowns.asp
	protected String writeDropdown(){
		String responseHtml = "<style>.dropbtn {"
											+"background-color: #75a3a3;"
										    + "color: 0a0f0f;"
											+ "width: 220px;"
										    + "height: 40px"
										    + "padding: 14px;"
										    +"font-size: 14px;"
										    +"border: none;"
										    +"cursor: pointer;"
										+"}"
									
									  +".dropdown {"
									    	+"position: relative;"
									    	+"display: inline-block;"
										+"}"
									
									+ ".dropdown-content {"
									    +"display: none;"
									    +"position: absolute;"
									    +"background-color: #ccc6b3"
									    +"min-width: 160px;"
									    +"box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);"
									+"}"
									
									+".dropdown-content a {"
									    +"color: #75a3a3;"
									    +"padding: 12px 16px;"
									    +"text-decoration: none;"
									    +"display: block;"
									+"}"
									
									+".dropdown-content a:hover {background-color: #f1f1f1}"
									
									+".dropdown:hover .dropdown-content {"
									    +"display: block;"
									+"}"
									
									+".dropdown:hover.dropbtn {"
									    +"background-color: #f5f4f0;"
									+"}"
								+"</style>"
							+"<div class=\"dropdown\">"
							+"<button class=\"dropbtn\">User Options</button>"
							+"<div class=\"dropdown-content\">"
								+ "<a href=\"favs_list\">Go to Favorites List</a>"
								+ "<a href=\"update_password\">Update Password</a>"
//								+"<a href=\"#\">Link 3</a>"
							+"</div>"
							+"</div>";
		return responseHtml;
	}
	
	protected String writeLogout(){
		String logout = " <a href=\"logout?name=UUID\">Logout</a>";
		return logout;
	}
	
	protected String writeFavs(){
		String favs = "&emsp;<form action=\"user_favorites\" method=\"post\">"
				+ "<input type=\"submit\" value=\"Go to Favorites List\"></form>";
		return favs;
	}

}