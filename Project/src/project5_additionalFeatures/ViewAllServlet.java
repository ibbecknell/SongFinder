package project5_additionalFeatures;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;
import project3p2_webInterface.BaseServlet;
import project4_database.DBHelper;

public class ViewAllServlet extends BaseServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + ERROR));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		PrintWriter writer = prepareResponse(response);
		String headResponseHtml = writeHTML();
		ArrayList<String> artistsBy= new ArrayList<String>();
		ThreadSafeMusicLibrary library = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");
		JSONArray songsByArtist = library.getSongsByArtist();
		String responseHtml=null;
		try {

			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(request.getParameter("sortBy").equals("artist")){
			try {

				artistsBy = DBHelper.orderByAlpha();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	

			responseHtml = " Here is a complete list of songs sorted Alphabetically by Artist!"
					+ "</br><br/><table border=\"2px\" width=\"100%\">"
					+ "<tr><th>Artist</th></tr>";
		}
		else if(request.getParameter("sortBy").equals("playcount")){

			try {

				artistsBy = DBHelper.orderByPlaycount();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	

			responseHtml = " Here is a complete list of songs sorted by the Artist's Playcount!"
					+ "</br><br/><table border=\"2px\" width=\"100%\">"
					+ "<tr><th>Artist</th></tr>";
		}


		try {
			responseHtml = getArtistsBy(artistsBy, songsByArtist, responseHtml);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(writeUserInfo(name)+headResponseHtml + responseHtml);
		return;
	}


	private String getArtistsBy(ArrayList<String> artistsBy, JSONArray songs, String responseHTML) throws SQLException{
		for(String a : artistsBy){
			responseHTML= responseHTML.concat("<tr><td><center>" +"<a href=\"artist_info?artist=" +a+"\">"+a+"</a></center>");
		}
		return responseHTML;
	}
}
