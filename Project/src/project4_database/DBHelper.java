package project4_database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DBHelper {
//	/**
//	 * Creates a table called artist in the database specified by the configuration information.
//	 * The table must have four columns:
//	 * name - should be a 100 character string that cannot be null and is the primary key
//	 * listeners - an integer
//	 * playcount - an integer
//	 * bio - a long text string
//	 * 
//	 * @param dbconfig
//	 * @throws SQLException
//	 */
//	public static void createUserTable() throws SQLException {		
//		Connection con = getConnection();
//		
//		String sql = "CREATE TABLE user(" 
//				+ "name VARCHAR(200) not null," 
//				+ "username VARCHAR(100) primary key not null,"
//				+ "password VARCHAR(100) not null);";
//		PreparedStatement st =con.prepareStatement(sql);
//		st.executeUpdate();
//		
//		con.close();
//	}
//	
//	public static void createFavsTable() throws SQLException {		
//		Connection con = getConnection();
//		
//		String sql = "CREATE TABLE favorites("  
//				+ "username VARCHAR(100) primary key not null,"
//				+ "artist VARCHAR(100) not null,"
//				+ "title VARCHAR(100) not null);";
//		PreparedStatement st =con.prepareStatement(sql);
//		st.executeUpdate();
//		
//		con.close();
//	}
	
	
	
	public static boolean verifyHistory(String username) throws SQLException{
		boolean hasHistory = false;
		Connection con = getConnection();
		String selectStmt = "SELECT * FROM search_history";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			String currentName = (String)result.getString("username");
			if(currentName.equals(username)){
				hasHistory = true;
				con.close();
				return hasHistory;
			}
		}
		con.close();
		return hasHistory;
	}
	
	public static boolean clearHistory(String username) throws SQLException{
		Connection con = getConnection();
		
		if(verifyHistory(username)){
//			String selectStmt = "SELECT * FROM  .search_history where ";

				PreparedStatement updateStmt = con.prepareStatement("DELETE FROM search_history WHERE username = ?;" );
				updateStmt.setString(1,username);
				updateStmt.execute();
			
			con.close();
			return true;
		}
		con.close();
		return false;
	}
	
	
	
	public static ArrayList<JSONObject> getQueriesHistory(String username) throws SQLException{
		ArrayList<JSONObject>queries = new ArrayList<JSONObject>();
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM search_history where username = \"" + username + "\" order by search_order DESC";
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			int search_order = result.getInt("search_order");
			String query = result.getString("query");
			String queryType = result.getString("queryType");
			JSONObject obj = new JSONObject();
			obj.put("order", search_order);
			obj.put("query", query);
			obj.put("queryType", queryType);
			
			queries.add(obj);
			
		}
		
		con.close();
		return queries;
	}
	
	public static void insertQuery(String username, String query, String queryType) throws SQLException{
		Connection con = getConnection();
		int i = 0;
		String selectStmt = "SELECT * FROM search_history where username = \"" + username + "\"";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			i++;
		}
		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO search_history (username, search_order, query, queryType) VALUES (?, ?, ?, ?);");
		updateStmt.setString(1, username);
		updateStmt.setInt(2, i+1);
		updateStmt.setString(3, query);
		updateStmt.setString(4, queryType);
		
		updateStmt.execute();
		
		con.close();
	}
	
	public static void updatePassword(String username, String password)throws SQLException{
		System.out.println("updating password for " + username);
		Connection con = getConnection();
		String selectStmt = "SELECT * FROM user ";

			PreparedStatement updateStmt = con.prepareStatement("UPDATE user SET password=? WHERE username =?" );
			updateStmt.setString(1, password);
			updateStmt.setString(2,username);
			updateStmt.execute();
		con.close();
	}
	
	/**
	 * A helper method that returns a database connection.
	 * A calling method is responsible for closing the connection when finished.
	 * @param dbconfig
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {

		String username  = "user24";
		String password  = "user24";
		String db  = "user24";

		try {
			// load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			e.printStackTrace();
			System.exit(1);
		}

		// format "jdbc:mysql://[hostname][:port]/[dbname]"
		//note: if connecting through an ssh tunnel make sure to use 127.0.0.1 and
		//also to that the ports are set up correctly
//		String host = "sql.cs.usfca.edu";
		String host = "127.0.0.1";
		String port = "3306";
		String urlString = "jdbc:mysql://" + host + ":" + port + "/"+db+"?useUnicode=true&characterEncoding=UTF-8";
		Connection con = DriverManager.getConnection(urlString,
				username,
				password);

		return con;
	}
	
	public static JSONObject getArtistInfo(String artist) throws SQLException{
		Connection con = getConnection();
		JSONObject info = new JSONObject();
		
		String selectStmt = "SELECT * FROM artist where name = " + "\"" + artist + "\"";
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			String name = result.getString("name");	
			int listeners = result.getInt("listeners");
			int playcount = result.getInt("playcount");
			String bio = result.getString("bio");
			
			info.put("name", name);
			info.put("listeners", listeners);
			info.put("playcount", playcount);
			info.put("bio", bio);
		}
		con.close();
		return info;
	}
	
	public static ArrayList<String> orderByAlpha() throws SQLException{
		ArrayList<String> sorted = new ArrayList<String>();
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM artist ORDER BY name;";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			String current = result.getString("name");	
			sorted.add(current);
		}
		
		con.close();
		return sorted;
	}
	
	public static ArrayList<String> orderByPlaycount() throws SQLException{
		ArrayList<String> sorted = new ArrayList<String>();
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM artist ORDER BY playcount DESC;";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			String current = result.getString("name");	
			sorted.add(current);
		}
		
		con.close();
		return sorted;
	}
	
	public static boolean verifyArtist(JSONObject obj) throws SQLException  {
		boolean hasArtist = false;
		Connection con = getConnection();
		String selectStmt = "SELECT * FROM artist";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
			String currentName = (String)result.getString("name");
			if(currentName.equals(obj.get("name"))){
				hasArtist = true;
				con.close();
				return hasArtist;
			}
		}
		con.close();
		return hasArtist;
		
	}
	
	public static void insertArtist(JSONObject obj) throws SQLException{
		if(verifyArtist(obj)){
			return;
		}
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM artist";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO artist (name, listeners, playcount, bio) VALUES (?, ?, ?, ?);");
		updateStmt.setString(1, obj.get("name").toString());
		updateStmt.setInt(2, Integer.parseInt(obj.get("listeners").toString()));
		updateStmt.setInt(3, Integer.parseInt(obj.get("playcount").toString()));
		updateStmt.setString(4, obj.get("bio").toString());
		
		updateStmt.execute();

		con.close();
	}
	
	//TODO: consider modifying to return a list of Strings (Array list of track id)
		public static ArrayList<String> getFavorites(String username) throws SQLException{
			
			ArrayList<String> favs = new ArrayList<String>();
			Connection con = getConnection();
			
			String selectStmt = "SELECT * FROM favorites where username = \"" + username+"\"";
			
			PreparedStatement stmt = con.prepareStatement(selectStmt);
			
			ResultSet result = stmt.executeQuery();
			
			while(result.next()){
				String currentTrackId = result.getString("trackId");	
				favs.add(currentTrackId);
			}
			
			con.close();
			return favs;
		}
	
	public static boolean insertFavorite(String username, String trackId) throws SQLException{
		Connection con = getConnection();
		if(verifyFav(username, trackId)){
			return false;
		}
		String selectStmt = "SELECT * FROM favorites";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();

		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO favorites (username, trackId) VALUES (?, ?);");
		updateStmt.setString(1, username);
		updateStmt.setString(2, trackId);
		
		updateStmt.execute();
		
		result = stmt.executeQuery();

		con.close();
		return true;
	}
	
	public static boolean verifyFav(String username, String trackId) throws SQLException{
		boolean hasFav = false;
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM favorites";
			
		PreparedStatement stmt = con.prepareStatement(selectStmt);
			
		ResultSet result = stmt.executeQuery();
	
			
		while(result.next()){
			String currentUsername = result.getString("username");
			String currentTrackId = result.getString("trackId");
			
			if(currentUsername.equals(username) && currentTrackId.equals(trackId)){
				hasFav = true;
				con.close();
				return hasFav;
			}
		}
		con.close();
		return hasFav;
	}
	
	
	
	public static boolean removeFav(String username, String trackId) throws SQLException{
		Connection con = getConnection();
		
		if(verifyFav(username, trackId)){
			String selectStmt = "SELECT * FROM favorites WHERE username = ?";
			
			PreparedStatement stmt = con.prepareStatement(selectStmt);
			
			PreparedStatement updateStmt = con.prepareStatement("DELETE FROM favorites WHERE username = ? AND trackId = ?;");
			updateStmt.setString(1,username);
			updateStmt.setString(2, trackId);

			updateStmt.execute();
			
			con.close();
			return true;
		}
		return false;
	}
	
	
	public static boolean insertUser(String firstname, String lastname, String username, String password) throws SQLException{
		if(verifyUser(username, password)){
			return false;
		}
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM user";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();

		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO user (name, username, password) VALUES (?, ?, ?);");
		updateStmt.setString(1, firstname+ " " + lastname);
		updateStmt.setString(2, username);
		updateStmt.setString(3, password);
		
		updateStmt.execute();
		
		result = stmt.executeQuery();
		
		con.close();
		return true;
	}
	
	public static boolean verifyUsername(String username) throws SQLException{
		boolean hasUsername = false;
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM user";
			
		PreparedStatement stmt = con.prepareStatement(selectStmt);
			
		ResultSet result = stmt.executeQuery();
	
			
		while(result.next()){
			String currentUsername = result.getString("username");
			
			if(currentUsername.equals(username)){
				hasUsername = true;
				con.close();
				return hasUsername;
			}
		}
		con.close();
		return hasUsername;
	}
	
	public static boolean verifyUser(String username, String password) throws SQLException{
		
		boolean hasUser = false;
		Connection con = getConnection();

		String selectStmt = "SELECT * FROM user";
			
		PreparedStatement stmt = con.prepareStatement(selectStmt);
			
		ResultSet result = stmt.executeQuery();
	
			
		while(result.next()){
			String currentUsername = result.getString("username");
			String currentPassword = result.getString("password");
			
			if(currentUsername.equals(username) && currentPassword.equals(password)){
				hasUser = true;
				con.close();
				return hasUser;
			}
		}
		con.close();
		return hasUser;
	}
	
}
