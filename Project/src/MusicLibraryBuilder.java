import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class that builds the Music Library given an input path
 * 
 * @author ibbecknell
 *
 */
public class MusicLibraryBuilder {

	/**
	 * Helper method that recursively traverses a specified directory. Calls
	 * parseSongs on JSON files.
	 * 
	 * @param directory
	 *            to traverse
	 * @param musicLibrary
	 *            to be built
	 */
	public static void traverseDirectory(Path directory, MusicLibrary musicLibrary) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file : stream) {
				if (Files.isDirectory(file)) {
					traverseDirectory(file, musicLibrary);
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
	 *            path given to extract Song information
	 * @param musicLibrary
	 *            to add song information to
	 */
	private static void parseSongs(Path p, MusicLibrary musicLibrary) {
		JSONParser parser = new JSONParser();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			String line = buffered_reader.readLine();
			JSONObject data = (JSONObject) parser.parse(line);
			Song song = new Song(data);
			musicLibrary.addSong(song);

		} catch (FileNotFoundException e) {
			System.err.println("Could not find the file " + p.getFileName());
		} catch (IOException e) {
			System.err.println("There was an error reading the file " + p.getFileName());
		} catch (ParseException e) {
			System.err.println("Could not parse the file");
		}
	}

	/**
	 * Driver method for debugging
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		// Path input =
		// Paths.get("/Users/missionbit/Desktop/cs212s16/repositories/ibbecknell-project/Project/input/lastfm_simple/TRABBBV128F42967D7.json");
		// parseSongs(input);
	}
}
