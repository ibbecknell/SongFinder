package project4_database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	public static void createUserTable(UserProfile user) throws SQLException {		
		Connection con = getConnection();
		
		String sql = "CREATE TABLE user(" 
				+ "name VARCHAR(200) not null," 
				+ "username VARCHAR(100) primary key not null,"
				+ "password VARCHAR(100) not null);";
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

	/**
	 * If the artist table exists in the database, removes that table.
	 * 
	 * @param dbconfig
	 * @param tables
	 * @throws SQLException
	 */
	public static void clearTables(UserProfile user, ArrayList<String> tables) throws SQLException {

		Connection con = getConnection();

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
	
	public static boolean insertUser(UserProfile user) throws SQLException{
		if(verifyUser(user.getUsername(), user.getPassword())){
			return false;
		}
		Connection con = getConnection();
		
		String selectStmt = "SELECT * FROM user";
		
		PreparedStatement stmt = con.prepareStatement(selectStmt);
		
		ResultSet result = stmt.executeQuery();

		
		while(result.next()){
//			System.out.println(result);
			String userName =result.getString("name");
			String username = result.getString("username");
			String password = result.getString("password");
			
			System.out.printf("name: %s, username: %s, password: %s", userName, username, password);
		}
		
		PreparedStatement updateStmt = con.prepareStatement("INSERT INTO user (name, username, password) VALUES (?, ?, ?);");
		updateStmt.setString(1, user.firstName+ " " + user.lastName);
		updateStmt.setString(2, user.username);
		updateStmt.setString(3, user.password);
		
		System.out.println();
		updateStmt.execute();
		System.out.println("\n*****\n");
		
		result = stmt.executeQuery();
		
		while(result.next()){
//			System.out.println(result);
			String userName =result.getString("name");
			String username = result.getString("username");
			String password = result.getString("password");
			
			System.out.printf("\n name: %s, username: %s, password: %s", userName, username, password);
		}
		con.close();
		return true;
	}
	
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
				System.out.println("user is present in database");
				hasUser = true;
//				con.close();
				return hasUser;
			}
		}
		con.close();
		return hasUser;
	}
}
