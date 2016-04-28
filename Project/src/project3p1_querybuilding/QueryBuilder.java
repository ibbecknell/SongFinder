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

//TODO
public class QueryBuilder {
	private boolean artist = false;
	private boolean title = false;
	private boolean tag = false;

	/**
	 * 
	 * TODO
	 * @param p
	 * @param outputPath
	 * @param lib
	 */
	public void readQueries(Path p, Path outputPath, MusicLibrary lib) {
		JSONParser parser = new JSONParser();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			if (buffered_reader != null) {
				JSONObject contents = (JSONObject) parser.parse(buffered_reader);
				if (contents.containsKey("searchByArtist")) {
					artist = true;
					JSONArray artistQueries = (JSONArray) contents.get("searchByArtist");
					for (int i = 0; i < artistQueries.size(); i++) {
						lib.getJSONSearchByArtist((String) artistQueries.get(i));
					}
				}
				if (contents.containsKey("searchByTitle")) {
					title = true;
					JSONArray titleQueries = (JSONArray) contents.get("searchByTitle");

					for (int i = 0; i < titleQueries.size(); i++) {
						lib.getJSONSearchByTitle((String) titleQueries.get(i));
					}
				}
				if (contents.containsKey("searchByTag")) {
					tag = true;
					JSONArray tagQueries = (JSONArray) contents.get("searchByTag");

					for (int i = 0; i < tagQueries.size(); i++) {
						lib.getJSONSearchByTag((String) tagQueries.get(i));
					}
				}
				lib.getJSONResults(artist, title, tag);
				lib.writeToJSON(outputPath);
			}

		} catch (IOException e) {
			System.err.println("There was an error reading the file " + p.getFileName());
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
