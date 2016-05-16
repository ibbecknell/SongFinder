package project5_additionalFeatures;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;
import project3p2_webInterface.BaseServlet;

public class ViewAllServlet extends BaseServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		PrintWriter writer = prepareResponse(response);
		String headResponseHtml = writeHTML();
		
		ThreadSafeMusicLibrary library = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");

		if(request.getParameter("sortBy").equals("artist")){
			JSONArray songsByArtist = library.getSongsByArtist();
			String responseHtml = " Here is a complete list of songs sorted Alphabetically by Artist!"
					+ "</br><br/><table border=\"2px\" width=\"100%\">"
					+ "<tr><th>Artist</th><th>Song Title</th><th>Favorite</th></tr>";
			
			
			try {
				responseHtml = getArray(name, songsByArtist, responseHtml, request, response) + "</table>" + writeFavs();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.println(writeUserInfo(name)+headResponseHtml + responseHtml);
			return;
		}
		else if(request.getParameter("sortBy").equals("playcount")){
//			TODO get playcount info
		}
	}
	private String getArray(String name, JSONArray students, String responseHTML, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		for (int i = 0; i < students.size(); i++) {
			JSONObject student = (JSONObject) students.get(i);
			responseHTML = responseHTML.concat("<tr><td>" + (String) student.get("artist").toString() + "</td><td>"
					+ "<a href=\"song_info?artist="+student.get("artist")+"&title="+student.get("title")+"\">"+(String) student.get("title").toString() + "</a></td></tr>");
		}
		return responseHTML;
	}
}
