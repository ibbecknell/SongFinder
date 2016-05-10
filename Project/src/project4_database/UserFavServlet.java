package project4_database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;
import project3p2_webInterface.BaseServlet;

public class UserFavServlet extends BaseServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		String trackId = request.getParameter("trackId");
		String title = request.getParameter("title");
		String querytype = (String)session.getAttribute(QUERYTYPE);
		String songquery = (String)session.getAttribute(SONGQUERY);

		try {
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			}else if(name != null && trackId != null && !DBHelper.verifyFav(name, trackId)){
				DBHelper.insertFavorite(name, trackId);
				response.sendRedirect(response.encodeRedirectURL("/songs?queryType="+querytype+"&songquery="+songquery));
				return;
			} else if(trackId == null || title == null){
				response.sendRedirect(response.encodeRedirectURL("/songs?"));
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		PrintWriter writer = prepareResponse(response);

		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		
		
		
		if(request.getParameterValues("remove") != null){
			String[] removed = request.getParameterValues("remove");
			for(String s : removed){
				removeFav(name, s);
			}
		}
		
		JSONArray favs = null;
		try {
			favs = DBHelper.getFavorites(name);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(writeUserInfo(name) + writeHTML() + name +"'s Favorites List!</br>");
		String responseHtml = "<br/><table border=\"2px\" width=\"100%\">"
						+ "<form action=\"user_favorites\" method=\"post\">"+"<tr><th>Artist</th><th>Song Title</th><th>Remove From Favorites</th></tr>";
			responseHtml = readFavs(favs, responseHtml, request, response) + "</table>"+ "</br><input type=\"submit\" value=\"Remove Favorite(s)\"></form>" ;
			
		writer.println(responseHtml);

	}
	
	private String readFavs(ArrayList<JSONObject> favs, String responseHTML, HttpServletRequest request, HttpServletResponse response) {
		ThreadSafeMusicLibrary book = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");

		
		for(int i = 0; i< favs.size(); i++){
			
			JSONObject result = new JSONObject();
			result = book.searchById(favs.get(i).get("trackId").toString());

			responseHTML = responseHTML.concat("<tr><td>" + (String) result.get("artist").toString() + "</td><td>"
					+ (String) result.get("title").toString() + "</td><td>"
			 
					+"<center><input type=\"checkbox\" name=\"remove\" value=\""+result.get("trackId")+"\"></center></td></tr>");
		}
		return responseHTML;
	}
	
	private void removeFav(String username, String trackId){
		try {
			if(!DBHelper.removeFav(username, trackId)){
			} else {
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	

