import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Search for grades of a single student.
 * @author srollins
 *
 */
public class SearchServlet extends BaseServlet {

	/**
	 * GET /search returns a web page containing a search box where a student's name may be entered.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String responseHtml = "<html" + 
							"<head><title>Song Finder</title>"+
							"<style> p { padding-top: 1%; border-top: solid; border-top-width: 1px; border-top-color: #A9A9A9; }</style>"
							+"</head>" +
							"<body>" + "<h1><center> Song Finder</center></h1>" + 
							"<form action=\"songs\" method=\"get\">" +
							"Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.<br/>"+
							"<p>Search Type: "+ "<select name = \"queryType\"><optgroup><option value = \"artist\">Artist</option><option value = \"title\">Song Title</option><option value = \"tag\">Tag</option></optgroup></select>"+
							" Query: <input type=\"text\" name=\"songquery\"> " +
							"<input type=\"submit\" value=\"Submit\">" +
							"</form></p>" +
							"</body>" +
							"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);
	}
}
