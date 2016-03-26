import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class that builds the Music Library given an input path
 * 
 * @author ibbecknell
 *
 */
public class MultithreadedMusicLibraryBuilder extends MusicLibraryBuilder {

	private final WorkQueue workers;
	
	public MultithreadedMusicLibraryBuilder(int nThreads){
		workers = new WorkQueue(nThreads);
	}

	public void shutdown(){
		workers.shutdown();
	}
	
	public void awaitTermination(){
		workers.awaitTermination();
	}
	
	private class Worker implements Runnable{
		
		private Path path;
		private ThreadSafeMusicLibrary tSafeLibrary;
		
		public Worker(Path path, ThreadSafeMusicLibrary tSafeLibrary){
			this.path = path;
			this.tSafeLibrary = tSafeLibrary;
		}
		
		@Override
		public void run() {
			parseSongs(path, tSafeLibrary);		
		}
		
	}
	/**
	 * Helper method that recursively traverses a specified directory. Calls
	 * parseSongs on JSON files.
	 * 
	 * @param directory
	 *            to traverse
	 * @param musicLibrary
	 *            to be built
	 */
	public synchronized void traverseDirectory(Path directory, ThreadSafeMusicLibrary musicLibrary) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file : stream) {
				if (Files.isDirectory(file)) {
					traverseDirectory(file, musicLibrary);
				} else {
					if (file.toString().toLowerCase().endsWith("json")) {
//						System.out.println("found json file");
						workers.execute(new Worker(file, musicLibrary));
					}
				}
			}

		} catch (IOException e) {
			System.err.println("There was an error reading the file " + directory.getFileName());
		}
//		workers.queueSize();
//		workers.shutdown();
//		workers.awaitTermination();
		
		
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
	public static void parseSongs(Path p, ThreadSafeMusicLibrary musicLibrary) {
		JSONParser parser = new JSONParser();
		JSONObject data = new JSONObject();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			String line = buffered_reader.readLine();
			data = (JSONObject) parser.parse(line);
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

	public synchronized void buildLibrary(String line, ThreadSafeMusicLibrary musicLibrary) throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject data = (JSONObject) parser.parse(line);
		System.out.println("creating song");
		Song song = new Song(data);
		System.out.println("adding song");
		musicLibrary.addSong(song);
		System.out.println("song added");
	}

}
