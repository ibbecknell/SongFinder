package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;

public class SongsServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String queryType = request.getParameter("queryType");
		// get the parameter from the search box
		String query = request.getParameter("songquery");

		// retrieve the Music Library from the context
		ThreadSafeMusicLibrary book = (ThreadSafeMusicLibrary) request.getServletContext().getAttribute("musiclibrary");
		JSONObject result = new JSONObject();
		JSONArray searchByArray = new JSONArray();
		// get the JSON representation of the query.
		if (queryType.equals("artist")) {
			result = book.getJSONSearchByArtist(query);
			searchByArray = (JSONArray) result.get("similars");
		} else if (queryType.equals("title")) {
			result = book.getJSONSearchByTitle(query);
			searchByArray = (JSONArray) result.get("similars");
		} else if (queryType.equals("tag")) {
			result = book.getJSONSearchByTag(query);
			searchByArray = (JSONArray) result.get("similars");

		}

		String responseHtml = "<html>" + "<head><title>Song Finder</title>"
				+ "<style> p { padding-top: 1%; border-top: solid; border-top-width: 1px; border-top-color: #A9A9A9; }</style>"
				+ "</head>" +"<body>";
		if (result != null) {
			responseHtml = "<h1><center>Song Finder</center></h1>" + "<form action=\"songs\" method=\"get\">"
					+ "Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.<br/>"
					+ "<p>Search Type: "
					+ "<select name = \"queryType\"><optgroup><option value = \"artist\">Artist</option><option value = \"title\">Song Title</option><option value = \"tag\">Tag</option></optgroup></select>"
					+ " Query: <input type=\"text\" name=\"songquery\"> " + "<input type=\"submit\" value=\"Submit\">"
					+ "</form></p>" + "Here are some songs you might like!</br>"
					+ " <table border=\"2px\" width=\"100%\">" + "<tr><th>Artist</th><th>Song Title</th></tr>";

			responseHtml = getArray(searchByArray, responseHtml) + "</table>";
		} 

		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);

	}

	private String getArray(JSONArray students, String responseHTML) {
		for (int i = 0; i < students.size(); i++) {
			JSONObject student = (JSONObject) students.get(i);
			responseHTML = responseHTML.concat("<tr><td>" + (String) student.get("artist").toString()
					+ "</td><td>" + (String) student.get("title").toString() + "</td></tr>");
		}
		return responseHTML;
	}
}
