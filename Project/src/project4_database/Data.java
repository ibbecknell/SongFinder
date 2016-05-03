package project4_database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONObject;

public class Data {

	//maintain a map of username to UserInfo object
	protected HashMap<String, UserProfile> userInfo;

	//constructor
	public Data() {
		userInfo = new HashMap<String, UserProfile>();
	}
		
	/*
	 * Returns true if the user exists in the data store.
	 */
	public synchronized boolean userExists(String username) {
		return userInfo.containsKey(username);
	}
	
	/*
	 * Add a new UserInfo object for a particular user.
	 */
	public synchronized void addUser(UserProfile user) {
		if(!userInfo.containsKey(user.getUsername())) {
			userInfo.put(user.getUsername(), user);
		}
	}

	/*
	 * For a given user, add a new todo.
	 */
	public synchronized boolean addFav(String username, String item) {
		if(!userInfo.containsKey(username)) {
			return false;
		}
		userInfo.get(username).addToFav(item);
		return true;
	}
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
	public static void createArtistTable(UserProfile user) throws SQLException {		
		Connection con = getConnection(user);
		
		String sql = "CREATE TABLE artist(" 
				+ "name VARCHAR(200) not null primary key," 
				+ "listeners INTEGER,"
				+ "playcount INTEGER," 
				+ "bio VARCHAR(9000));";
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
	public static Connection getConnection(UserProfile user) throws SQLException {

		String username  = user.getUsername();
		String password  = user.getPassword();
		String db  = "user24";

		try {
			// load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
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

	/**
	 * If the artist table exists in the database, removes that table.
	 * 
	 * @param dbconfig
	 * @param tables
	 * @throws SQLException
	 */
	public static void clearTables(UserProfile user, ArrayList<String> tables) throws SQLException {

		Connection con = getConnection(user);

		for(String table: tables) {
			//create a statement object
			Statement stmt = con.createStatement();
			if(tableExists(con, table)) {
				System.out.println("table exists");
				String dropStmt = "DROP TABLE " + table;
				stmt.executeUpdate(dropStmt);
			}
			
		}
		con.close();
	}

	/**
	 * Helper method that determines whether a table exists in the database.
	 * @param con
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	private static boolean tableExists(Connection con, String table) throws SQLException {

		DatabaseMetaData metadata = con.getMetaData();
		ResultSet resultSet;
		resultSet = metadata.getTables(null, null, table, null);

		if(resultSet.next()) {
			// Table exists
			return true;
		}		
		return false;
	}
	
	public static void insertUser(UserProfile user, JSONObject obj) throws SQLException{
		
		Connection con = getConnection(user);
		
		String selectStmt = "SELECT * FROM artist";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();

		
		while(result.next()){
//			System.out.println(result);
			String artistName =result.getString("name");
			int artistListeners = result.getInt("listeners");
			int artistPlaycount = result.getInt("playcount");
			String artistBio = result.getString("bio");
			
			System.out.printf("name: %s, listeners: %s, playcount: %s, bio: %s", artistName, artistListeners, artistPlaycount, artistBio);
		}
		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO artist (name, listeners, playcount, bio) VALUES (?, ?, ?, ?);");
		updateStmt.setString(1, obj.get("name").toString());
		updateStmt.setInt(2, Integer.parseInt(obj.get("listeners").toString()));
		updateStmt.setInt(3, Integer.parseInt(obj.get("playcount").toString()));
		updateStmt.setString(4, obj.get("bio").toString());
		
		System.out.println();
		updateStmt.execute();
		System.out.println("\n*****\n");
		
		result = stmt.executeQuery();
		
		while(result.next()){
			String artistName =result.getString("name");
			int artistListeners = result.getInt("listeners");
			int artistPlaycount = result.getInt("playcount");
			String artistBio = result.getString("bio");
			
			System.out.printf("name: %s, listeners: %s, playcount: %s, bio: %s\n", artistName, artistListeners, artistPlaycount, artistBio);
		}
		con.close();
	}

}

