package project4_database;

import java.util.ArrayList;

public class UserProfile {
	String firstName;
	String lastName;
	String username;
	String password;
	ArrayList<String> favorites;
	
	public UserProfile(String firstname, String lastname, String username, String password){
		this.firstName = firstname;
		this.lastName = lastname;
		this.username = username;
		this.password = password;
		this.favorites = new ArrayList<String>();
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<String> getFavorites(){
		return favorites;
	}
	
	public void addToFav(String song){
		favorites.add(song);
	}


	@Override
	public String toString() {
		return "UserProfile [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + "]";
	}
	
	
}
