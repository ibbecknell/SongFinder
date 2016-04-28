package project3p1_querybuilding;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project1_librarybuilding.MusicLibrary;

public class JSONBuilder {
	protected JSONArray jsonArtistResults;
	
	protected JSONArray jsonTagResults;
	
	protected JSONArray jsonTitleResults;
	
	protected JSONObject jsonResults;
	
	
	
	
	public JSONBuilder(MusicLibrary library){
		this.jsonArtistResults = new JSONArray();
		this.jsonTagResults = new JSONArray();
		this.jsonTitleResults = new JSONArray();
	}
}
