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

public class SongInfoServlet extends BaseServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		String artist = request.getParameter("artist");
		String title = request.getParameter("title");
		ThreadSafeMusicLibrary library = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");
		JSONObject result = new JSONObject();
		JSONArray searchByArray = new JSONArray();
		String password = (String) session.getAttribute(PASSWORD);
		
		try {
			
			if(username == null || !DBHelper.verifyUser(username, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = library.getJSONSearchByArtist(artist);
		searchByArray = (JSONArray)result.get("similars");
		
		PrintWriter out = prepareResponse(response);
		
		out.println(writeUserInfo(username)+writeHTML() + "<h3><center> Song Info </center></h3><br/><b>Artist:</b> " + artist + "<br/><b>Title:</b> " + title
				+"<br/><b><center>Songs Similar to "+artist+":</center></b> <br/><table>"
//						+ " border=\"2px\" width=\"100%\">"
						+ "<tr><th>Artist</th><th>Song Title</th></tr>");
		writeSimilars(searchByArray, out);
		out.println("</table>");
		
		result = library.getJSONSearchByTitle(title);
		searchByArray = (JSONArray)result.get("similars");
		
		out.println("<br/><b><center>Songs Similar to "+title+":</center></b><br/><table> "
//				+ "border=\"2px\" width=\"100%\">"
						+ "<tr><th>Artist</th><th>Song Title</th></tr>");
		writeSimilars(searchByArray, out);
		out.println("</table>");
		
		
		
	}
	
	private void writeSimilars(JSONArray similars, PrintWriter writer){
//		String similar = null;
		
		for(int i = 0; i<similars.size(); i++){
			JSONObject obj = (JSONObject)similars.get(i);
			writer.write("<tr><td>"+"<a href=\"artist_info?artist=" + obj.get("artist").toString()+"\">"+(String) obj.get("artist").toString() + "</a></td><td>"
			+ "<a href=\"song_info?artist="+ obj.get("title").toString() + "&title="+obj.get("title")+"\">"+(String) obj.get("title").toString() + "</a></td></tr>");
//			writer.write();
//			writer.write("\n");
		}
		
//		return similar; 
	}
	
//	private void writeTable(PrintWriter out, String similarTo){
//		
//		out.println("<br/><b>Songs Similar to the "+similarTo+":</b> </br><br/><table border=\"2px\" width=\"100%\">"
//						+ "<tr><th>Artist</th><th>Song Title</th></tr>");
//		writeSimilars(searchByArray, out);
//		out.println("</table>");
//	}
}
