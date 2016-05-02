package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet superclass with methods common to all servlets for this
 * application.
 * 
 * @author srollins
 *
 */
public class BaseServlet extends HttpServlet {

	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		return response.getWriter();
	}

	protected String writeHTML() {
		String responseHtml = "<html>" + "<head><title>Song Finder</title>"
				+ "<style> p { padding-top: 1%; border-top: solid; border-top-width: 1px; border-top-color: #A9A9A9; }</style>"
				+ "</head>" + "<body>" + "<h1><center>Song Finder</center></h1>"
				+ "<form action=\"songs\" method=\"get\">"
				+ "Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.<br/>"
				+ "<p>Search Type: "
				+ "<select name = \"queryType\"><optgroup><option value = \"artist\">Artist</option><option value = \"title\">Song Title</option><option value = \"tag\">Tag</option></optgroup></select>"
				+ " Query: <input type=\"text\" name=\"songquery\"> "
				+ "<input type=\"submit\" value=\"Submit\"></form></p>";
		return responseHtml;
	}

}