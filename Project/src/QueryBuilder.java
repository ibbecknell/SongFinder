import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class QueryBuilder {
	
	/**
	 * Helper method that parses each song in a JSON File to calculate the
	 * number of songs and number of similar songs each song has.
	 * 
	 * @param p
	 *            path given to extract Song information
	 * @param musicLibrary
	 *            to add song information to
	 */
	public void readQueries(Path p, MusicLibrary lib) throws EmptyFileException{
		JSONParser parser = new JSONParser();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			if(buffered_reader != null){
				JSONObject contents = (JSONObject) parser.parse(buffered_reader);
				if(contents.containsKey("searchByArtist")){
					JSONArray artistQueries = (JSONArray)contents.get("searchByArtist");
					for(int i = 0; i < artistQueries.size(); i++){
						if(lib.artistMap.containsKey((String)artistQueries.get(i))){
							lib.searchByArtist((String)artistQueries.get(i));
						}
						else{
							System.err.println(artistQueries.get(i) + " not in library");
							System.out.println();
						}
					}
				}
				if(contents.containsKey("searchByTitle")){
					JSONArray titleQueries = (JSONArray)contents.get("searchByTitle");

					for(int i = 0; i < titleQueries.size(); i++){
						if(lib.titleMap.containsKey((String)titleQueries.get(i))){
							lib.searchByTitle((String)titleQueries.get(i));
						}
						else{
							System.err.println(titleQueries.get(i) + " not in library");
							System.out.println();
						}
					}
				}
				if(contents.containsKey("searchByTag")){
					JSONArray tagQueries = (JSONArray)contents.get("searchByTag");

					for(int i = 0; i < tagQueries.size(); i++){
						if(lib.tagMap.containsKey((String)tagQueries.get(i))){
							lib.searchByTag((String)tagQueries.get(i));
						}
						else{
							System.err.println(tagQueries.get(i) + " not in library");
							System.out.println();
						}
					}
				}
				else{
					
				}
			}
			else{
				throw new EmptyFileException("file is empty");
			}
			
		} catch (FileNotFoundException e) {
			System.err.println("Could not find the file " + p.getFileName());
		} catch (IOException e) {
			System.err.println("There was an error reading the file " + p.getFileName());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

}


