package project5_additionalFeatures;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import project3p2_webInterface.BaseServlet;
import project4_database.DBHelper;

public class ArtistInfoServlet extends BaseServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		PrintWriter writer = prepareResponse(response);
		String headResponseHtml = writeHTML();
		String artist = request.getParameter("artist");
		JSONObject obj = new JSONObject();
		try {
			
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			obj = DBHelper.getArtistInfo(artist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String responseHtml =  "<h3><center> Artist Info </center></h3><br/><b>Artist:</b> " + artist + "<br/><b>Listeners:</b> " + obj.get("listeners")
				+"<br/><b>Playcount: </b>" + obj.get("playcount") + "<br/><b>Bio: </b><br/>" + obj.get("bio");
		
		writer.println(writeUserInfo(name)+headResponseHtml + responseHtml);
	}
}
