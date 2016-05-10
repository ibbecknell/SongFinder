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
	/**
	 * Creates a table called artist in the database specified by the configuration information.
	 * The table must have four columns:
	 * name - should be a 100 character string that cannot be null and is the primary key
	 * listeners - an integer
	 * playcount - an integer
	 * bio - a long text string
	 * 
	 * @param dbconfig
	 * @throws SQLException
	 */
	public static void createUserTable() throws SQLException {		
		Connection con = getConnection();
		
		String sql = "CREATE TABLE user(" 
				+ "name VARCHAR(200) not null," 
				+ "username VARCHAR(100) primary key not null,"
				+ "password VARCHAR(100) not null);";
		PreparedStatement st =con.prepareStatement(sql);
		st.executeUpdate();
		
		con.close();
	}
	
	public static void createFavsTable() throws SQLException {		
		Connection con = getConnection();
		
		String sql = "CREATE TABLE favorites("  
				+ "username VARCHAR(100) primary key not null,"
				+ "artist VARCHAR(100) not null,"
				+ "title VARCHAR(100) not null);";
		PreparedStatement st =con.prepareStatement(sql);
		st.executeUpdate();
		
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
		String host = "127.0.0.1";
		String port = "3306";
		String urlString = "jdbc:mysql://" + host + ":" + port + "/"+db;
		Connection con = DriverManager.getConnection(urlString,
				username,
				password);

		return con;
	}


	public static JSONArray getFavorites(String username) throws SQLException{
		
		JSONArray favs = new JSONArray();
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM favorites where username = \"" + username+"\"";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();
		
		while(result.next()){
//			String currentUsername = result.getString("username");
			String currentTrackId = result.getString("trackId");
			
//			if(currentUsername.equals(username)){
				JSONObject userFavs = new JSONObject();
				userFavs.put("trackId", currentTrackId );
				favs.add(userFavs);
//			}
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
//			String deleteStmt = "DELETE FROM CUSTOMERS WHERE username = \"" + username + "\" AND trackId = \"" + trackId +"\"";
			
//			PreparedStatement stmt = con.prepareStatement(deleteStmt);
			
//			ResultSet result = stmt.executeQuery();
			
			PreparedStatement updateStmt = con.prepareStatement("DELETE FROM favorites WHERE username = ? AND trackId = ?;");
			updateStmt.setString(1,username);
			updateStmt.setString(2, trackId);

			
//			System.out.println();
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

		
//		while(result.next()){
////			System.out.println(result);
//			String userName =result.getString("name");
//			String user_name = result.getString("username");
//			String password = result.getString("password");
//			
////			System.out.printf("name: %s, username: %s, password: %s", userName, username, password);
//		}
		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO user (name, username, password) VALUES (?, ?, ?);");
		updateStmt.setString(1, firstname+ " " + lastname);
		updateStmt.setString(2, username);
		updateStmt.setString(3, password);
		
//		System.out.println();
		updateStmt.execute();
//		System.out.println("\n*****\n");
		
		result = stmt.executeQuery();
		
//		while(result.next()){
////			System.out.println(result);
//			String userName =result.getString("name");
//			String username = result.getString("username");
//			String password = result.getString("password");
//			
////			System.out.printf("\n name: %s, username: %s, password: %s", userName, username, password);
//		}
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
	
//	public static UserProfile getUser(String username, String password) throws SQLException{
//		Connection con = getConnection();
//		
////		if(!tableExists(con, "user")){
////			createUserTable(user);
////		}
//		
//		String selectStmt = "SELECT * FROM user";
//			
//		PreparedStatement stmt = con.prepareStatement(selectStmt);
//			
//		ResultSet result = stmt.executeQuery();
//	
//			
//		while(result.next()){
//			String currentUsername = result.getString("username");
//			String currentPassword = result.getString("password");
////			String currentFName = result.getString("firstname");
//			
////			System.out.println("username: " + currentUsername + " vs " + username + ": " + currentUsername.equals(username));
////			System.out.println("password: " + currentPassword + " vs " + password + ": " + currentPassword.equals(password));
//			
//			if(currentUsername.equals(username) && currentPassword.equals(password)){
////				System.out.println("user is present in database");
//				String name = result.getString("name");
////				hasUser = true;
//				UserProfile user = new UserProfile(username, password);
//				con.close();
//				return user;
//				
//				
//			}
//		}
//		con.close();
//		return null;
//		
//	}
	
	public static boolean verifyUser(String username, String password) throws SQLException{
		
		boolean hasUser = false;
		Connection con = getConnection();
			
//		if(!tableExists(con, "user")){
//			createUserTable(user);
//		}
		
		String selectStmt = "SELECT * FROM user";
			
		PreparedStatement stmt = con.prepareStatement(selectStmt);
			
		ResultSet result = stmt.executeQuery();
	
			
		while(result.next()){
			String currentUsername = result.getString("username");
			String currentPassword = result.getString("password");
			
//			System.out.println("username: " + currentUsername + " vs " + username + ": " + currentUsername.equals(username));
//			System.out.println("password: " + currentPassword + " vs " + password + ": " + currentPassword.equals(password));
			
			if(currentUsername.equals(username) && currentPassword.equals(password)){
//				System.out.println("user is present in database");
				hasUser = true;
//				con.close();
				return hasUser;
			}
		}
		con.close();
		return hasUser;
	}
	
}
