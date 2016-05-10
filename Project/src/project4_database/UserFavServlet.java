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
//	public static final ArrayList<JSONObject> FAVS = new ArrayList<JSONObject>();
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + ERROR));
//		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);

		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		String trackId = request.getParameter("trackId");
		String title = request.getParameter("title");
//		String artist = request.getParameter("artist");
//		JSONArray favs = null;
//		try {
//			favs = DBHelper.getFavorites(name);
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

//		String queryType = (String)session.getAttribute(QUERYTYPE);
//		String songquery = (String)session.getAttribute(SONGQUERY);
//		System.out.println("info is: "+ trackId);
//		//user is not logged in, redirect to login page
//		System.out.println("request parameter is: "+request.getParameter("name"));
//		try {
//			System.out.println(data.verifyFav(name, password));
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		try {
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			}else if(!DBHelper.verifyFav(name, trackId)){
//				JSONObject song = new JSONObject();
//				song.put("artist", artist);
//				song.put("title", title);
//				UserProfile user = DBHelper.getUser(name, password);
//				System.out.println(user.toString());
//				user.addToFav(song);
//				FAVS.add(song);
//				USER_FAVS.add(song);
				DBHelper.insertFavorite(name, trackId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		PrintWriter writer = prepareResponse(response);


		writer.println( writeUserInfo(name) + writeHTML() + "Added \"" + title + "\" to your favorites! </br> </br>Continue your search or look at your Favorites!</br>");
		writer.println("<form action=\"user_favorites\" method=\"post\">"
				+ "<input type=\"submit\" value=\"Go to Favorites List\"></form>");
		
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
						+ "<form action=\"user_favorites\" method=\"post\">"+"<tr><th>Artist</th><th>Song Title</th><th>Remove</th></tr>";
			responseHtml = readFavs(favs, responseHtml, request, response) + "</table>"+ "</br><input type=\"submit\" value=\"Remove Favorite(s)\"></form>" ;
			
		writer.println(responseHtml);

	}
	
	private String readFavs(ArrayList<JSONObject> favs, String responseHTML, HttpServletRequest request, HttpServletResponse response) {
//		for (int i = 0; i < favs.size(); i++) {
		ThreadSafeMusicLibrary book = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");

		
//		System.out.println("reading array");
		for(int i = 0; i< favs.size(); i++){
			
//			System.out.println("user fav: " + favs.get(i));
			JSONObject result = new JSONObject();
//			System.out.println(favs.get(i).get("trackId").toString());
			result = book.searchById(favs.get(i).get("trackId").toString());
//			System.out.println(result.toString());
//			JSONObject student = (JSONObject) favs.get(i);
			responseHTML = responseHTML.concat("<tr><td>" + (String) result.get("artist").toString() + "</td><td>"
					+ (String) result.get("title").toString() + "</td><td>"
			 
					+"<center><input type=\"checkbox\" name=\"remove\" value=\""+result.get("trackId")+"\"></center></td></tr>");
		}
		return responseHTML;
	}
	
	private void removeFav(String username, String trackId){
//		String response = "<a href= \"\">Remove from Favs</a>";
		try {
			if(!DBHelper.removeFav(username, trackId)){
//				response = "<a href= \"\">Remove from Favs</a>";
			} else {
//				response = "<a href= \"\">Removed</a>";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return response;
	}
}
	

