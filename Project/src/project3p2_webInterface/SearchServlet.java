package project3p2_webInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project1_librarybuilding.MusicLibrary;
import project4_database.DBHelper;

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
//		
//		
//		//user is not logged in, redirect to login page
//		if(name == null || !data.userExists(name)) {
//			response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
//			return;
//		}
//		DBHelper data = (DBHelper) getServletConfig().getServletContext().getAttribute(DATA);
		
		HttpSession session = request.getSession();
		
		String name = (String) session.getAttribute(USERNAME);
		String password =(String) session.getAttribute(PASSWORD);
		
		//user is not logged in, redirect to login page
		try {
//			System.out.println("---------search servlet----------");
//			System.out.println(name);
//			System.out.println(password);
			if(!DBHelper.verifyUser(name, password)) {
//				System.out.println("redirecting from /search");
				response.sendRedirect(response.encodeRedirectURL("/" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} else {
//				System.out.println("/search");
				String headResponseHtml = writeHTML();
//				String logoutButton = writeLogout();
				PrintWriter writer = prepareResponse(response);
				writer.println(writeUserInfo(name) + headResponseHtml);
			}
//			System.out.println("-----------end of search servlet----------");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
