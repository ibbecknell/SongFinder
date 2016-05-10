package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;
import project4_database.DBHelper;

public class SongsServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {		
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		PrintWriter writer = prepareResponse(response);
		String headResponseHtml = writeHTML();
		//user is not logged in, redirect to login page
		try {
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getParameter("songquery") == null || request.getParameter("queryType") == null){
			
			writer.println(writeUserInfo(name) + headResponseHtml + "Invalid Search Request! Please Search again!");
			return;
		}
		
		boolean hasQuery = false;
		String queryType = request.getParameter("queryType");
	
		// get the parameter from the search box
		String query = request.getParameter("songquery").trim();

		session.setAttribute(QUERYTYPE, queryType);
		session.setAttribute(SONGQUERY, query);
		
		// retrieve the Music Library from the context
		ThreadSafeMusicLibrary library = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");
		JSONObject result = new JSONObject();
		JSONArray searchByArray = new JSONArray();
		
		// get the JSON representation of the query.
		if (queryType.equals("artist")) {
			if (library.hasArtist(query)) {
				hasQuery = true;
				result = library.getJSONSearchByArtist(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		} else if (queryType.equals("title")) {
			if (library.hasTitle(query)) {
				hasQuery = true;
				result = library.getJSONSearchByTitle(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		} else if (queryType.equals("tag")) {
			if (library.hasTag(query)) {
				hasQuery = true;
				result = library.getJSONSearchByTag(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		} else{
			writer.println(writeUserInfo(name) + headResponseHtml + "Invalid Search Request! Please Search again!");
			return;
		}
		

		String responseHtml = null;

		if (hasQuery) {
			if (result != null) {
				responseHtml = " Here are some songs you might like!"
						+ "</br><br/><table border=\"2px\" width=\"100%\">"
						+ "<tr><th>Artist</th><th>Song Title</th><th>Favorite</th></tr>";

				try {
					responseHtml = getArray(name,searchByArray, responseHtml, request, response) + "</table>" + writeFavs();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			responseHtml = "\"" + query + "\"" + " not found! Please search again";
		}

		writer.println(writeUserInfo(name)+headResponseHtml + responseHtml);

	}

	private String getArray(String name, JSONArray students, String responseHTML, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		for (int i = 0; i < students.size(); i++) {
			JSONObject student = (JSONObject) students.get(i);
			responseHTML = responseHTML.concat("<tr><td>" + (String) student.get("artist").toString() + "</td><td>"
					+ (String) student.get("title").toString() + "</td><td><center>"+ verifyFav(name, student.get("title").toString(), student.get("trackId").toString()) + "</center></td></tr>");
		}
		return responseHTML;
	}
	
	private String verifyFav(String username, String title, String trackId){
		String response= "<a id = \"link\" href=\"user_favorites?title="+title+"&trackId="+trackId+"\">Add to Favs!</a>";
		try {
			if(DBHelper.verifyFav(username, trackId)){
				
				response = "Liked!";
			} else {
				
				response = "<a id = \"link\" href=\"user_favorites?title="+title+"&trackId="+trackId+"\">Add to Favs!</a>";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}
