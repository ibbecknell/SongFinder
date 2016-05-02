package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Search for Song info.
 * 
 *
 */
public class SearchServlet extends BaseServlet {

	/**
	 * GET /search returns a web page containing a dropdown menu where a user
	 * may select to search for an artist name, song title, or tag and search
	 * box where a song's artist, title, or tag may be entered.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String headResponseHtml = writeHTML();
		PrintWriter writer = prepareResponse(response);
		writer.println(headResponseHtml);
	}
}
