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

		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);
		
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		
		//user is not logged in, redirect to login page
		try {
			if(name == null || !data.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		boolean hasQuery = false;
		String queryType = request.getParameter("queryType");
		// get the parameter from the search box
		String query = request.getParameter("songquery");

		// retrieve the Music Library from the context
		ThreadSafeMusicLibrary book = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");
		JSONObject result = new JSONObject();
		JSONArray searchByArray = new JSONArray();
		// get the JSON representation of the query.
		if (queryType.equals("artist")) {
			if (book.hasArtist(query)) {
				hasQuery = true;
				result = book.getJSONSearchByArtist(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		} else if (queryType.equals("title")) {
			if (book.hasTitle(query)) {
				hasQuery = true;
				result = book.getJSONSearchByTitle(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		} else if (queryType.equals("tag")) {
			if (book.hasTag(query)) {
				hasQuery = true;
				result = book.getJSONSearchByTag(query);
				searchByArray = (JSONArray) result.get("similars");
			}

		}
		String headResponseHtml = writeHTML();

		String responseHtml = null;

		if (hasQuery) {
			if (result != null) {
				responseHtml = " Here are some songs you might like!</br><br/><table border=\"2px\" width=\"100%\">"
						+ "<tr><th>Artist</th><th>Song Title</th></tr>";

				responseHtml = getArray(searchByArray, responseHtml) + "</table>";
			}
		} else {
			responseHtml = "\"" + query + "\"" + " not found! Please search again";
		}

		PrintWriter writer = prepareResponse(response);
//		String logout = writeLogout();
		writer.println(writeUserInfo(name)+headResponseHtml + responseHtml);

	}

	private String getArray(JSONArray students, String responseHTML) {
		for (int i = 0; i < students.size(); i++) {
			JSONObject student = (JSONObject) students.get(i);
			responseHTML = responseHTML.concat("<tr><td>" + (String) student.get("artist").toString() + "</td><td>"
					+ (String) student.get("title").toString() + "</td></tr>");
		}
		return responseHTML;
	}
}
