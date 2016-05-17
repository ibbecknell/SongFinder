package project5_additionalFeatures;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import project2_multithreading.ThreadSafeMusicLibrary;
import project3p2_webInterface.BaseServlet;
import project4_database.DBHelper;

public class SearchHistoryServlet extends BaseServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		String password = (String) session.getAttribute(PASSWORD);
		String querytype = (String)session.getAttribute(QUERYTYPE);
		String songquery = (String)session.getAttribute(SONGQUERY);
		PrintWriter writer = prepareResponse(response);
		String responseHtml=null;
		
//		System.out.println("request.getParameter(\"clear_history\") = " +request.getParameter("clear_history"));
		
		
//		if(request.getParameter("clear_history") != null){
//			clearHistory(name);
//		}
		ArrayList<JSONObject> history = null;
		try {
			history = DBHelper.getQueriesHistory(name);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			
			if(name == null || !DBHelper.verifyUser(name, password)) {
				response.sendRedirect(response.encodeRedirectURL("/?" + STATUS + "=" + NOT_LOGGED_IN));
				return;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(writeUserInfo(name) + writeHTML() + name +"'s Search History!</br>");
		if(history.size()==0 || history == null){
			responseHtml = "Your Search History is Empty";
		}else{
			responseHtml = "<br/><table> "
//					+ "border=\"2px\" width=\"100%\">"
							+"<tr><th>Recently Searched</th><th>Type of Search</th></tr>";
				responseHtml = readHistory(history, responseHtml, request, response) + "</table>"+ "<form action=\"search_history\" method=\"post\"></br><input type=\"submit\" value=\"Clear Search History\"></form>" ;
		}
		
			
		writer.println(responseHtml);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute(USERNAME);
		PrintWriter writer = prepareResponse(response);
		clearHistory(name);
		writer.println(writeUserInfo(name) + writeHTML() + "<br/> Your Search History is Empty");
		
		
		return;

	}
	
	private String readHistory(ArrayList<JSONObject> history, String responseHTML, HttpServletRequest request, HttpServletResponse response) {
		if(history.size()==0){
			responseHTML = "Your Search History is Empty";
		} else{
		
			for(int i = 0; i< history.size(); i++){
//				System.out.println(history.get(i).get("order"));
				responseHTML = responseHTML.concat("<tr><td><center>"+ history.get(i).get("query") + "</td><td><center>" + history.get(i).get("queryType")+"</center></td></tr>");
			}
		}
		return responseHTML;
	}
	
	private void clearHistory(String username){
		try {
			DBHelper.clearHistory(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
