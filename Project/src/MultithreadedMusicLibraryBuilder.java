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
 * Utility class that builds the Music Library concurrently, given an input path
 * 
 * @author ibbecknell
 *
 */
public class MultithreadedMusicLibraryBuilder extends MusicLibraryBuilder {

	private final WorkQueue workers;

	public MultithreadedMusicLibraryBuilder(int nThreads) {
		workers = new WorkQueue(nThreads);
	}

	/**
	 * Method to shutdown the work queue and stop accepting new jobs
	 */
	public void shutdown() {
		workers.shutdown();
	}

	/**
	 * Method to wait until all threads finish working
	 */
	public void awaitTermination() {
		workers.awaitTermination();
	}

	/**
	 * Private inner class to build a thread-safe music library
	 * 
	 * @author ibbecknell
	 *
	 */
	private class Worker implements Runnable {

		private Path path;
		private ThreadSafeMusicLibrary tSafeLibrary;

		/**
		 * Constructor to implement runnable method of parsing song file
		 * 
		 * @param path
		 *            of file to be parsed
		 * @param tSafeLibrary
		 *            to be built
		 */
		public Worker(Path path, ThreadSafeMusicLibrary tSafeLibrary) {
			this.path = path;
			this.tSafeLibrary = tSafeLibrary;
		}

		/**
		 * Overridden run method to parse a song file concurrently
		 */
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
	 *            thread-safe library to be built
	 */
	public synchronized void traverseDirectory(Path directory, ThreadSafeMusicLibrary musicLibrary) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file : stream) {
				if (Files.isDirectory(file)) {
					traverseDirectory(file, musicLibrary);
				} else {
					if (file.toString().toLowerCase().endsWith("json")) {
						workers.execute(new Worker(file, musicLibrary));
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
	 *            thread-safe library to add song information to
	 */
	public static void parseSongs(Path p, ThreadSafeMusicLibrary musicLibrary) {
		JSONParser parser = new JSONParser();
		JSONObject data = new JSONObject();
		try (BufferedReader buffered_reader = Files.newBufferedReader(p, Charset.forName("UTF-8"))) {
			String line = buffered_reader.readLine();
//TODO: use the parse version that takes as input the reader.			
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

}
