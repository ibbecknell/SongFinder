package project3p1_querybuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import project1_librarybuilding.MusicLibrary;


public class QueryBuilder {
	private boolean artist = false;
	private boolean title = false;
	private boolean tag = false;
	private JSONArray jsonArtistResults;

	private JSONArray jsonTagResults;

	private JSONArray jsonTitleResults;

	private JSONObject jsonResults;


	public void readQueries(Path p, Path outputPath, MusicLibrary lib) {
		QueryWriter writer = new QueryWriter();
		JSONParser parser = new JSONParser();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			if (buffered_reader != null) {
				JSONObject contents = (JSONObject) parser.parse(buffered_reader);
				buffered_reader.close();
				if (contents.containsKey("searchByArtist")) {
					jsonArtistResults = new JSONArray();
					artist = true;
					JSONArray artistQueries = (JSONArray) contents.get("searchByArtist");
					for (int i = 0; i < artistQueries.size(); i++) {
						JSONObject obj = lib.getJSONSearchByArtist((String) artistQueries.get(i));
						jsonArtistResults.add(obj);
						
					}
				}
				if (contents.containsKey("searchByTitle")) {
					jsonTitleResults = new JSONArray();
					title = true;
					JSONArray titleQueries = (JSONArray) contents.get("searchByTitle");

					for (int i = 0; i < titleQueries.size(); i++) {
						JSONObject obj = lib.getJSONSearchByTitle((String) titleQueries.get(i));
						jsonTitleResults.add(obj);
					}
				}
				if (contents.containsKey("searchByTag")) {
					jsonTagResults = new JSONArray();
					tag = true;
					JSONArray tagQueries = (JSONArray) contents.get("searchByTag");

					for (int i = 0; i < tagQueries.size(); i++) {
						JSONObject obj = lib.getJSONSearchByTag((String) tagQueries.get(i));
						jsonTagResults.add(obj);
					}
				}
				
				JSONObject results = getJSONResults(artist, title, tag);
				
				writer.writeQueries(outputPath, results);
			}

		} catch (IOException e) {
			System.err.println("There was an error reading the file " + p.getFileName());
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	public JSONObject getJSONResults(boolean artist, boolean title, boolean tag) {
		jsonResults = new JSONObject();
		if (artist) {
			jsonResults.put("searchByArtist", jsonArtistResults);
		}
		if (title) {
			jsonResults.put("searchByTitle", jsonTitleResults);
		}
		if (tag) {
			jsonResults.put("searchByTag", jsonTagResults);
		}
		return jsonResults;
	}


}
