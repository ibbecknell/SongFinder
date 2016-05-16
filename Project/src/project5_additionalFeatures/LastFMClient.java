package project5_additionalFeatures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import project1_librarybuilding.MusicLibraryBuilder;
import project2_multithreading.ThreadSafeMusicLibrary;
import project4_database.DBHelper;

public class LastFMClient {
	public static int PORT = 80;
	
	public static int length;
	/**
	 * For each artist in the list artists, does the following: 
	 * 1. Fetches the information about that artist from the last.fm artist 
	 * API: http://www.last.fm/api/show/artist.getInfo
	 * 2. Extracts the artist name, number of listeners, playcount, and bio.
	 * 3. Stores all four pieces of information in a relational database table.
	 * The table must be called artist and must have columns name, listeners, playcount, and bio.
	 * The information stored in dbconfig must be used to connect to the database.
	 * This method assumes the table exists, but should catch any exceptions that occur if
	 * the table does not exist.
	 * 
	 * @param artists
	 * @param dbconfig
	 */
	public static void fetchAndStoreArtists(ArrayList<String> artists) {
		String apiKey = "43c024a9398715c9ce27667a458ef353";

		for(String artist : artists){
//			TODO fetch info about artist from last.fm artist API: http://www.last.fm/api/show/artist.getInfo
//			System.out.println(artist);
				artist = artist.replace(" ", "%20");
				
				
				String page = download("ws.audioscrobbler.com", 
						"/2.0/?method=artist.getinfo&artist=" + artist 
						+ "&api_key=" + apiKey + "&format=json");

				
				String[] pageContents = getPageContents(page);

				if(verifyOK(pageContents)){

//					TODO extract artist name, number of listeners, playcount and bio
					JSONObject obj = extractArtistJSON(page);
					if(obj != null){
//						TODO store all four pieces of info in a relational database table

						try {
							DBHelper.insertArtist(obj);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						System.out.println();
//						System.out.println();
					}
				}

		}

	}
	
	public static JSONObject extractArtistJSON(String page){
		JSONObject obj = new JSONObject();
		String[] jsonString = page.split("(?m:^(?=[\r\n]|\\z))");
		
		JSONObject artistObj = (JSONObject)parseResponse(jsonString[1].trim());
		if(artistObj.containsKey("artist")){
			JSONObject artistDet = (JSONObject)artistObj.get("artist");
			
			if(artistDet.containsKey("name")){
				String name = (String)artistDet.get("name");
				obj.put("name", name);
			}
			if(artistDet.containsKey("stats")){
				JSONObject stats = (JSONObject)artistDet.get("stats");
				if(stats.containsKey("listeners") && stats.containsKey("playcount")){
					int listeners = Integer.parseInt( (String)stats.get("listeners"));
					int playcount = Integer.parseInt((String)stats.get("playcount"));
					obj.put("listeners", listeners);
					obj.put("playcount", playcount);
				}
			}
			if(artistDet.containsKey("bio")){
				JSONObject bio = (JSONObject)artistDet.get("bio");
				
				if(bio.containsKey("summary")){
					
					String summary = (String)bio.get("summary");

					summary = summary.replaceAll("<a.*</a>", "");
					summary = summary.replaceAll("\r\n", " ");
					
					obj.put("bio", summary.trim());
				}
			}

		} else {
			obj = null;
		}
		
		return obj;
		
	}

	public static boolean verifyOK(String[] line){
		return line[0].contains("200 OK");
	}
	

	public static String[] getPageContents(String page){
		String[] contents = page.split("\n");
		return contents;
	}
	
	public static JSONObject parseResponse(String response){
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		try {
			obj = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static String download(String host, String path) {

		StringBuffer buf = new StringBuffer();
		
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
		) { 

			//debug
//			System.out.println("--------------------");
//			System.out.println("Sending request: ");
			
			//construct request
			String request = getRequest(host, path);
			
			//debug
//			System.out.println(request);
//			System.out.println("*****");
			
			//send request
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			String line = reader.readLine();
//			while(line != null) {	
			while(line != null){
				if(!line.equals("(?sm)(.+?:.+?)^\\s*$")){
//					System.out.println(line);
					buf.append(line + "\n"); //append the newline stripped by readline
					line = reader.readLine();

				}
				
			}

		} catch (IOException e) {
			System.out.println("LastfmClient::download " + e.getMessage() + path);
		}
		return buf.toString();
	}

	private static String getRequest(String host, String path) {
		String request = "GET " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n";								
		return request;
	}
	
	public static void main(String[] args){
//		DBConfig dbconfig = new DBConfig("user24", "user24", "user24", "127.0.0.1", "3306");
		MusicLibraryBuilder builder = new MusicLibraryBuilder();
		ThreadSafeMusicLibrary lib = new ThreadSafeMusicLibrary();
		Path path = Paths.get("input/lastfm_subset");
		lib = builder.buildLibrary(path, lib);
		if (lib == null) {
			lib = new ThreadSafeMusicLibrary();
		}
		ArrayList<String> artists = lib.getArtists();	
//		System.out.println(artists.size());
		fetchAndStoreArtists(artists);
		
	}
}

