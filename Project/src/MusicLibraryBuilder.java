import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MusicLibraryBuilder {
	
	private Path path;
	
	
	/**
	 * Helper method that recursively traverses a specified directory. Calls
	 * parseSongs on JSON files.
	 * 
	 * @param directory
	 */
	public static void traverseDirectory(Path directory, MusicLibrary musicLibrary, boolean isRecursive) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file : stream) {
				if (Files.isDirectory(file) && isRecursive) {
					traverseDirectory(file, musicLibrary, isRecursive);
				} else {
					if (file.toString().toLowerCase().endsWith("json")) {
						parseSongs(file, musicLibrary);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("There was an error reading the file " + directory.getFileName());
		}
	}
	
	/**
	 * Helper method that parses each song in a JSON File to calculate the
	 * number of songs and number of similar songs each song has.
	 * 
	 * @param p
	 */
	private static void parseSongs(Path p, MusicLibrary musicLibrary) {
//		MusicLibrary musicLibrary = new MusicLibrary();
		JSONParser parser = new JSONParser();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			String line = buffered_reader.readLine();
//			while ((line = buffered_reader.readLine()) != null) {
			JSONObject data = (JSONObject) parser.parse(line);
			Song song = new Song(data);
//			System.out.println(song.toString());
			musicLibrary.addSong(song);

//			line = buffered_reader.readLine();

//			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find the file " + p.getFileName());
		} catch (IOException e) {
			System.err.println("There was an error reading the file " + p.getFileName());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main (String[] args){
		Path input = Paths.get("/Users/missionbit/Desktop/cs212s16/repositories/ibbecknell-project/Project/input/lastfm_simple/TRABBBV128F42967D7.json");
//		parseSongs(input);
	}
}
